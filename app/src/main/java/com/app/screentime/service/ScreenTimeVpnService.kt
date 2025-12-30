package com.app.screentime.service

import android.app.*
import android.content.Intent
import android.net.VpnService
import android.os.Build
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.core.app.NotificationCompat
import com.app.screentime.R
import com.app.screentime.database.ScreenTimeDatabase
import com.app.screentime.database.repository.BlockedLinkRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.*
import java.net.*
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicReference


class ScreenTimeVpnService : VpnService() {

    private var vpnInterface: ParcelFileDescriptor? = null
    private val TAG = "ScreenTimeVpn"

    @Volatile
    private var isRunning = false
    private var packetHandlerThread: Thread? = null

    // ✅ Blocklist (safe & smart) - Default domains
    private val defaultBlockedDomains = setOf(
        // Social & entertainment
        // DNS-over-HTTPS (DoH) providers
//        "dns.google",
        "cloudflare-dns.com",
        "mozilla.cloudflare-dns.com",
        "one.one.one.one",
        "security.cloudflare-dns.com",
        "quad9.net",
        "dns.quad9.net",
        "dns.nextdns.io",
        "rr5---sn-gwpa-pmfe.googlevideo.com",
        "www.googleadservices.com"
    ).map { it.lowercase() }.toSet()

    // Blocked domains from Room database (updated dynamically)
    private val blockedDomainsFromDb = AtomicReference<Set<String>>(emptySet())
    @Volatile
    private var lastDbUpdateTime = 0L
    private val DB_UPDATE_INTERVAL_MS = 1000L // Update cache every 1 second max

    private val blockedLinkRepository by lazy {
        val database = ScreenTimeDatabase.getDatabase(applicationContext)
        BlockedLinkRepository(database.blockedLinkDao())
    }

    // ✅ Common DoH IPs — for TCP blocking
    private val dohIps = setOf(
        "8.8.8.8", "8.8.4.4",         // Google DoH
        "1.1.1.1", "1.0.0.1",         // Cloudflare
        "9.9.9.9", "149.112.112.112"  // Quad9
    )

    private val realDnsServer = InetAddress.getByName("8.8.8.8")
    private val tunIp = "10.0.0.2"
    private val executor = Executors.newCachedThreadPool()
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    companion object {
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "screentime_vpn"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Check if this is a stop request
        if (intent?.getBooleanExtra("stop", false) == true) {
            stopVpn()
            stopSelf()
        } else {
            if (isRunning) {
                Log.d(TAG, "VPN already running, ignoring start request")
                return START_NOT_STICKY
            }

            createNotificationChannel()
            loadBlockedLinks()
            startBlockedLinksObserver() // Start observing database changes
            startForeground(NOTIFICATION_ID, createNotification())
            startVpn()
        }
        return START_NOT_STICKY
    }

    // ---------------- VPN setup -----------------
    private fun startVpn() {
        if (isRunning) {
            Log.w(TAG, "VPN already running")
            return
        }

        isRunning = true
        val builder = Builder()
            .setSession("ScreenTime Blocker")
            .addAddress(tunIp, 32)
            .addDnsServer(realDnsServer.hostAddress)
            // ✅ Only capture DNS traffic, not all traffic
            .addRoute(realDnsServer.hostAddress, 32)
            .setMtu(1500)

        vpnInterface = builder.establish()
        if (vpnInterface == null) {
            Log.e(TAG, "Failed to establish VPN")
            isRunning = false
            stopSelf()
            return
        }

        Log.d(TAG, "VPN established")
        val input = FileInputStream(vpnInterface!!.fileDescriptor).channel
        val output = FileOutputStream(vpnInterface!!.fileDescriptor).channel
        val buffer = ByteBuffer.allocate(32767)

        packetHandlerThread = Thread { handlePackets(input, output, buffer) }
        packetHandlerThread?.start()
    }

    // ---------------- VPN stop -----------------
    private fun stopVpn() {
        Log.d(TAG, "Stopping VPN service")
        isRunning = false
        vpnInterface?.close()
        vpnInterface = null
        packetHandlerThread?.interrupt()
        packetHandlerThread = null
        stopForeground(STOP_FOREGROUND_REMOVE)

        Log.d(TAG, "VPN service stopped")
    }

    private fun handlePackets(input: FileChannel, output: FileChannel, buffer: ByteBuffer) {
        try {
            while (isRunning) {
                try {
                    buffer.clear()
                    val length = input.read(buffer)
                    if (length > 0 && isRunning) {
                        buffer.flip()
                        processPacket(buffer, length, output)
                    } else if (length < 0) {
                        // EOF reached, VPN interface closed
                        Log.d(TAG, "VPN interface closed (EOF)")
                        break
                    }
                } catch (e: java.io.IOException) {
                    if (isRunning) {
                        Log.e(TAG, "Error reading from VPN interface", e)
                    }
                    break
                }
            }
        } catch (e: Exception) {
            if (isRunning) {
                Log.e(TAG, "VPN loop error", e)
            }
        } finally {
            Log.d(TAG, "Packet handler thread exiting")
            isRunning = false
            vpnInterface?.close()
        }
    }

    // ---------------- Packet processing -----------------
    private fun processPacket(packet: ByteBuffer, length: Int, output: FileChannel) {
        if (length < 20) return
        packet.position(0)
        val versionIhl = packet.get().toInt() and 0xFF
        if ((versionIhl shr 4) != 4) return // only IPv4

        val ihl = versionIhl and 0x0F
        val ipHeaderLen = ihl * 4
        packet.position(9)
        val protocol = packet.get().toInt() and 0xFF

        packet.position(12)
        val srcIp = readIp(packet)
        packet.position(16)
        val destIp = readIp(packet)

        // 🚫 Block known DoH TCP endpoints
        if (protocol == 6 && dohIps.contains(destIp)) {
            Log.d(TAG, "🚫 Blocking TCP connection to DoH IP: $destIp")
            return
        }

        if (protocol != 17) return // only UDP

        packet.position(ipHeaderLen)
        val srcPort = packet.short.toUShort().toInt()
        val destPort = packet.short.toUShort().toInt()
        val udpLen = packet.short.toUShort().toInt()

        if (destPort != 53) return // only DNS

        val udpPayloadStart = ipHeaderLen + 8
        packet.position(udpPayloadStart)
        val udpPayloadLen = udpLen - 8
        if (udpPayloadLen < 12 || packet.remaining() < udpPayloadLen) return

        val udpPayload = ByteArray(udpPayloadLen)
        packet.get(udpPayload)

        val dnsBuffer = ByteBuffer.wrap(udpPayload)
        val (domain, nameLen, dnsId) = extractDnsDomain(dnsBuffer)
        if (domain.isNullOrEmpty()) return

        val domainLower = domain.lowercase()
        if (isBlocked(domainLower)) {
            Log.d(TAG, "❌ BLOCKED DNS query: $domain")
            // Track blocked site in Room database
            serviceScope.launch {
                blockedLinkRepository.trackBlockedLink(domainLower)
            }
            val questionBytes = ByteArray(nameLen + 4)
            dnsBuffer.position(12)
            dnsBuffer.get(questionBytes)
            craftAndSendBlockedDnsResponse(dnsId, questionBytes, srcIp, srcPort, output)
        } else {
            Log.v(TAG, "✅ Allowing DNS query: $domain")
            forwardDnsAsync(udpPayload, srcIp, srcPort, output)
        }
    }

    // ---------------- DNS helper methods -----------------
    private fun extractDnsDomain(buffer: ByteBuffer): Triple<String?, Int, Int> {
        if (buffer.remaining() < 12) return Triple(null, 0, 0)
        buffer.position(0)
        val dnsId = buffer.short.toInt() and 0xFFFF
        buffer.position(12)
        val sb = StringBuilder()
        var totalLen = 0
        try {
            while (buffer.hasRemaining()) {
                val len = buffer.get().toInt() and 0xFF
                totalLen++
                if (len == 0) break
                if (len > 63 || buffer.remaining() < len) return Triple(null, 0, dnsId)
                repeat(len) { sb.append(buffer.get().toInt().toChar()) }
                sb.append('.')
                totalLen += len
            }
        } catch (e: Exception) {
            return Triple(null, 0, dnsId)
        }
        return Triple(sb.trimEnd('.').toString(), totalLen, dnsId)
    }

    /**
     * Check if a domain should be blocked
     * Handles subdomains: if "aajtak.in" is blocked, it blocks "www.aajtak.in", "m.aajtak.in", etc.
     */
    private fun isBlocked(domain: String): Boolean {
        // Normalize domain (remove trailing dots, lowercase)
        val normalizedDomain = domain.lowercase().trim().removeSuffix(".")
        
        // Check default domains
        if (defaultBlockedDomains.any { blockedDomain ->
            matchesDomain(normalizedDomain, blockedDomain)
        }) {
            Log.d(TAG, "✅ Blocked by default: $normalizedDomain")
            return true
        }
        
        // Use cached database domains for fast lookup
        val dbDomains = blockedDomainsFromDb.get()
        
        // Periodically refresh cache if it's been a while (non-blocking check)
        val now = System.currentTimeMillis()
        if (now - lastDbUpdateTime > DB_UPDATE_INTERVAL_MS) {
            // Trigger async refresh without blocking
            serviceScope.launch {
                refreshBlockedLinksCache()
            }
        }
        
        // Check domains from Room database
        val isBlocked = dbDomains.any { blockedDomain ->
            matchesDomain(normalizedDomain, blockedDomain)
        }
        
        if (isBlocked) {
            Log.d(TAG, "✅ Blocked by DB: $normalizedDomain (matched against ${dbDomains.size} blocked domains)")
        }
        
        return isBlocked
    }
    
    /**
     * Check if a domain matches a blocked domain pattern
     * Examples:
     * - "aajtak.in" matches "aajtak.in" ✓
     * - "www.aajtak.in" matches "aajtak.in" ✓
     * - "m.aajtak.in" matches "aajtak.in" ✓
     * - "aajtak.in" matches "www.aajtak.in" ✓
     */
    private fun matchesDomain(domain: String, blockedDomain: String): Boolean {
        // Exact match
        if (domain == blockedDomain) {
            return true
        }
        
        // If domain ends with .blockedDomain (subdomain case)
        // e.g., "www.aajtak.in" ends with ".aajtak.in"
        if (domain.endsWith(".$blockedDomain")) {
            return true
        }
        
        // If blockedDomain ends with .domain (reverse subdomain case)
        // e.g., "www.aajtak.in" contains "aajtak.in"
        if (blockedDomain.endsWith(".$domain")) {
            return true
        }
        
        // Contains check for partial matches
        // e.g., "aajtak.in" is contained in "www.aajtak.in"
        if (domain.contains(blockedDomain) || blockedDomain.contains(domain)) {
            return true
        }
        
        return false
    }

    /**
     * Load blocked links from Room database (initial load)
     */
    private fun loadBlockedLinks() {
        serviceScope.launch {
            refreshBlockedLinksCache()
        }
    }
    
    /**
     * Refresh blocked links cache from database
     */
    private suspend fun refreshBlockedLinksCache() {
        try {
            val links = blockedLinkRepository.getAllBlockedLinkStrings()
            val normalizedLinks = links.map { it.lowercase().trim().removeSuffix(".") }
            blockedDomainsFromDb.set(normalizedLinks.toSet())
            lastDbUpdateTime = System.currentTimeMillis()
            Log.d(TAG, "🔄 Refreshed blocked links cache: ${normalizedLinks.size} domains")
        } catch (e: Exception) {
            Log.e(TAG, "Error refreshing blocked links cache", e)
        }
    }
    
    /**
     * Start observing database changes via Flow
     * This ensures cache is updated immediately when links are added/removed
     */
    private fun startBlockedLinksObserver() {
        serviceScope.launch {
            try {
                blockedLinkRepository.getAllBlockedLinkStringsFlow().collect { links ->
                    val normalizedLinks = links.map { it.lowercase().trim().removeSuffix(".") }
                    blockedDomainsFromDb.set(normalizedLinks.toSet())
                    lastDbUpdateTime = System.currentTimeMillis()
                    Log.d(TAG, "🔄 Blocked links updated via Flow: ${normalizedLinks.size} domains - ${normalizedLinks.take(5).joinToString(", ")}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error observing blocked links Flow", e)
            }
        }
    }

    /**
     * Reload blocked links (call this when links are added/removed)
     * Note: Flow observer should handle this automatically, but this is a backup
     */
    fun reloadBlockedLinks() {
        serviceScope.launch {
            refreshBlockedLinksCache()
        }
    }

    // ---------------- DNS forwarding -----------------
    private fun forwardDnsAsync(
        udpPayload: ByteArray,
        srcIp: String,
        srcPort: Int,
        output: FileChannel
    ) {
        executor.execute {
            try {
                DatagramSocket().use { socket ->
                    protect(socket)
                    socket.soTimeout = 5000
                    val destAddr = InetSocketAddress(realDnsServer, 53)
                    socket.send(DatagramPacket(udpPayload, udpPayload.size, destAddr))

                    val recvBuf = ByteArray(512)
                    val recvPacket = DatagramPacket(recvBuf, recvBuf.size)
                    socket.receive(recvPacket)
                    val resp = recvPacket.data.copyOf(recvPacket.length)

                    sendUdpResponse(resp, realDnsServer.hostAddress, srcIp, 53, srcPort, output)
                }
            } catch (e: SocketTimeoutException) {
                Log.w(TAG, "DNS timeout")
            } catch (e: Exception) {
                Log.e(TAG, "DNS forward error", e)
            }
        }
    }

    // ---------------- Craft blocked DNS response -----------------
    private fun craftAndSendBlockedDnsResponse(
        dnsId: Int,
        question: ByteArray,
        srcIp: String,
        srcPort: Int,
        output: FileChannel
    ) {
        val resp = ByteBuffer.allocate(512)
        resp.putShort(dnsId.toShort())
        resp.putShort(0x8180.toShort()) // standard response
        resp.putShort(1) // QDCOUNT
        resp.putShort(1) // ANCOUNT
        resp.putShort(0)
        resp.putShort(0)
        resp.put(question)
        resp.putShort(0xc00c.toShort())
        resp.putShort(1.toShort())
        resp.putShort(1.toShort())
        resp.putInt(300)
        resp.putShort(4.toShort())
        resp.putInt(0) // 0.0.0.0
        resp.flip()
        val data = ByteArray(resp.remaining())
        resp.get(data)
        sendUdpResponse(data, realDnsServer.hostAddress, srcIp, 53, srcPort, output)
        Log.d(TAG, "Sent fake DNS response for blocked domain to $srcIp:$srcPort")
    }

    // ---------------- UDP/IP packet builder -----------------
    private fun sendUdpResponse(
        udpPayload: ByteArray,
        srcIp: String,
        destIp: String,
        srcPort: Int,
        destPort: Int,
        output: FileChannel
    ) {
        val udpLen = 8 + udpPayload.size
        val ipLen = 20 + udpLen
        val packet = ByteBuffer.allocate(ipLen)

        packet.put(0x45.toByte())
        packet.put(0)
        packet.putShort(ipLen.toShort())
        packet.putShort(0)
        packet.putShort(0)
        packet.put(64.toByte())
        packet.put(17.toByte())
        packet.putShort(0)
        packet.put(InetAddress.getByName(srcIp).address)
        packet.put(InetAddress.getByName(destIp).address)

        packet.putShort(srcPort.toShort())
        packet.putShort(destPort.toShort())
        packet.putShort(udpLen.toShort())
        packet.putShort(0)
        packet.put(udpPayload)
        packet.flip()

        val header = ByteArray(20)
        packet.position(0)
        packet.get(header, 0, 20)
        val checksum = computeChecksum(header)
        packet.putShort(10, checksum)

        packet.position(0)
        synchronized(output) {
            output.write(packet)
        }
    }

    private fun computeChecksum(data: ByteArray): Short {
        var sum = 0
        var i = 0
        while (i < data.size - 1) {
            val word = ((data[i].toInt() and 0xFF) shl 8) + (data[i + 1].toInt() and 0xFF)
            sum += word
            if (sum > 0xFFFF) sum = (sum and 0xFFFF) + 1
            i += 2
        }
        if (data.size % 2 != 0) {
            val last = (data[data.size - 1].toInt() and 0xFF) shl 8
            sum += last
            if (sum > 0xFFFF) sum = (sum and 0xFFFF) + 1
        }
        return (sum.inv() and 0xFFFF).toShort()
    }

    private fun readIp(buf: ByteBuffer): String {
        val b = ByteArray(4)
        buf.get(b)
        return "${b[0].toUByte()}.${b[1].toUByte()}.${b[2].toUByte()}.${b[3].toUByte()}"
    }

    // ---------------- Notification -----------------
    private fun createNotification(): Notification =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("ScreenTime Active")
            .setContentText("Blocking ${blockedDomainsFromDb.get().size} domains")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setOngoing(true)
            .build()

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, "ScreenTime VPN",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy called")
        stopVpn()
        executor.shutdown()
        try {
            if (!executor.awaitTermination(2, java.util.concurrent.TimeUnit.SECONDS)) {
                executor.shutdownNow()
            }
        } catch (e: InterruptedException) {
            executor.shutdownNow()
            Thread.currentThread().interrupt()
        }
        // Optionally clear blocked sites when VPN is stopped
        // BlockedSitesManager.clearBlockedSites(this)
        super.onDestroy()
    }
}



