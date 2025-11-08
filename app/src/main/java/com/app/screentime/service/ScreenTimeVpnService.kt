package com.app.screentime.service

import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.net.VpnService
import android.os.Binder
import android.os.IBinder
import android.os.ParcelFileDescriptor
import android.util.Log
import com.app.screentime.blocker.AppBlockManager
import java.io.IOException
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.net.SocketException
import java.nio.channels.DatagramChannel
import java.util.concurrent.atomic.AtomicReference

/**
 * A VPN service that manages internet access by blocking specified apps.
 */
class ScreenTimeVpnService : VpnService() {
    companion object {
        private const val TAG = "ScreenTime.VpnService"
    }

    private val mBinder = ServiceBinder(this@ScreenTimeVpnService)
    private val mAtomicVpnThread = AtomicReference<Thread?>(null)
    private var mVpnInterface: ParcelFileDescriptor? = null
    private var mIsServiceRunning = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ServiceBinder.ACTION_START_MINDFUL_SERVICE) {
            startFgService()
            return START_STICKY
        }

        stopAndDisposeService()
        return START_NOT_STICKY
    }

    private fun startFgService() {
        if (mIsServiceRunning) return
        try {
            mIsServiceRunning = true
            Log.d(TAG, "startFgService: VPN service started successfully")
        } catch (e: Exception) {
            Log.e(TAG, "startFgService: Failed to start VPN service", e)
            stopAndDisposeService()
        }
    }

    /**
     * Restarts the VPN connection by disconnecting and then reconnecting the VPN.
     */
    private fun reconnectVpn() {
        disconnectVpn()
        connectVpn()
        Log.d(TAG, "reconnectVpn: VPN reconnected successfully")
    }

    /**
     * Establishes a VPN connection based on blocked apps.
     * If there are no blocked apps, the service will stop itself.
     */
    private fun connectVpn() {
        val blockedApps = AppBlockManager.getBlockedApps()
        if (blockedApps.isEmpty()) {
            Log.w(TAG, "connectVpn: Tried to Connect Vpn without any blocked apps, Exiting")
            stopAndDisposeService()
            return
        }

        val newThread = Thread(vpnThread, TAG)
        setVpnThread(newThread)
        newThread.start()
    }

    /**
     * Disconnects the VPN connection if established.
     */
    private fun disconnectVpn() {
        try {
            mVpnInterface?.close()
            setVpnThread(null)
            Log.d(TAG, "disconnectVpn: VPN disconnected successfully")
        } catch (e: IOException) {
            Log.e(TAG, "disconnectVpn: Failed to disconnect VPN", e)
        }
    }

    /**
     * Stops the foreground service and disconnects the VPN.
     */
    private fun stopAndDisposeService() {
        disconnectVpn()
        stopSelf()
    }

    /**
     * Returns a Runnable that configures and establishes the VPN connection.
     */
    private val vpnThread: Runnable
        get() = Runnable {
            try {
                DatagramChannel.open().use { tunnel ->
                    check(this@ScreenTimeVpnService.protect(tunnel.socket())) { "Cannot protect the vpn socket tunnel" }
                    val serverAddress: SocketAddress = InetSocketAddress("localhost", 0)
                    tunnel.connect(serverAddress)
                    tunnel.configureBlocking(false)

                    val builder = this@ScreenTimeVpnService.Builder()
                    builder.addAddress("192.168.0.0", 24)
                    builder.addRoute("0.0.0.0", 0)

                    // Add blocked app's packages
                    for (packageName in AppBlockManager.getBlockedApps()) {
                        try {
                            builder.addDisallowedApplication(packageName)
                        } catch (e: PackageManager.NameNotFoundException) {
                            Log.w(TAG, "getVpnThread: Cannot find app with package $packageName")
                        }
                    }
                    synchronized(this@ScreenTimeVpnService) {
                        mVpnInterface = builder.establish()
                        Log.d(TAG, "getVpnThread: VPN connected successfully")
                    }
                }
            } catch (e: SocketException) {
                Log.e(TAG, "getVpnThread: Cannot use socket for VPN", e)
                stopAndDisposeService()
            } catch (e: IOException) {
                Log.e(TAG, "getVpnThread: VPN connection failed, exiting", e)
                stopAndDisposeService()
            } catch (e: IllegalArgumentException) {
                Log.e(TAG, "getVpnThread: VPN connection failed, exiting", e)
                stopAndDisposeService()
            } catch (e: Exception) {
                Log.e(TAG, "getVpnThread: Something went wrong", e)
                stopAndDisposeService()
            }
        }

    /**
     * Sets the current VPN thread, interrupting the previous thread if necessary.
     */
    private fun setVpnThread(thread: Thread?) {
        val oldThread = mAtomicVpnThread.getAndSet(thread)
        oldThread?.interrupt()
    }

    override fun onDestroy() {
        disconnectVpn()
        stopForeground(STOP_FOREGROUND_REMOVE)
        Log.d(TAG, "onDestroy: VPN service destroyed successfully")
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return if (intent.action == ServiceBinder.ACTION_BIND_TO_MINDFUL) mBinder else null
    }
}

/**
 * ServiceBinder is a generic binder class used to provide a reference to a service.
 * It allows the client to retrieve the service instance that is bound to it.
 */
class ServiceBinder<T : Service?>(val service: T) : Binder() {
    companion object {
        const val ACTION_START_MINDFUL_SERVICE: String = "com.app.screentime.action.startMindfulService"
        const val ACTION_BIND_TO_MINDFUL: String = "com.app.screentime.action.bindToMindful"
    }
}