package com.app.screentime.core.network.security

import java.math.BigInteger
import java.security.SecureRandom

/**
 * Curve25519 (RFC 7748) Montgomery ladder scalar multiplication implementation.
 *
 * Fully compliant with RFC 7748 and Go standard library `crypto/ecdh.X25519()`.
 * Runs natively on all Android versions (API 21+) using java.math.BigInteger.
 */
object Curve25519 {
    // Prime p = 2^255 - 19
    private val P: BigInteger = BigInteger.valueOf(2).pow(255).subtract(BigInteger.valueOf(19))

    // a24 = (486662 - 2) / 4 = 121665
    private val A24: BigInteger = BigInteger.valueOf(121665)

    // Base point u = 9
    private val BASE_POINT: ByteArray = ByteArray(32).apply { this[0] = 9 }

    private val secureRandom = SecureRandom()

    /**
     * Generates a secure random 32-byte private key.
     */
    fun generatePrivateKey(): ByteArray {
        val bytes = ByteArray(32)
        secureRandom.nextBytes(bytes)
        return bytes
    }

    /**
     * Computes the 32-byte public key for a given private key.
     */
    fun generatePublicKey(privateKey: ByteArray): ByteArray {
        return x25519(privateKey, BASE_POINT)
    }

    /**
     * Computes the shared secret using Diffie-Hellman (X25519).
     */
    fun deriveSharedSecret(myPrivateKey: ByteArray, theirPublicKey: ByteArray): ByteArray {
        return x25519(myPrivateKey, theirPublicKey)
    }

    /**
     * RFC 7748 Section 5: X25519 Montgomery Ladder scalar multiplication.
     * Computes res = scalar * uCoordinate on Curve25519.
     */
    fun x25519(scalarBytes: ByteArray, uBytes: ByteArray): ByteArray {
        require(scalarBytes.size == 32) { "scalar must be 32 bytes" }
        require(uBytes.size == 32) { "u coordinate must be 32 bytes" }

        // 1. Clamping the scalar per RFC 7748
        val k = scalarBytes.copyOf(32)
        k[0] = (k[0].toInt() and 248).toByte()
        k[31] = (k[31].toInt() and 127).toByte()
        k[31] = (k[31].toInt() or 64).toByte()

        // 2. Decode u coordinate as little-endian BigInteger
        val uRev = ByteArray(32)
        for (i in 0 until 32) {
            uRev[i] = uBytes[31 - i]
        }
        val x1 = BigInteger(1, uRev).mod(P)

        var x2 = BigInteger.ONE
        var z2 = BigInteger.ZERO
        var x3 = x1
        var z3 = BigInteger.ONE

        var swap = 0
        for (t in 254 downTo 0) {
            val kt = (k[t / 8].toInt() ushr (t % 8)) and 1
            val s = kt xor swap
            swap = kt

            if (s == 1) {
                val tx = x2; x2 = x3; x3 = tx
                val tz = z2; z2 = z3; z3 = tz
            }

            val a = x2.add(z2).mod(P)
            val aa = a.multiply(a).mod(P)
            val b = x2.subtract(z2).mod(P)
            val bb = b.multiply(b).mod(P)
            val e = aa.subtract(bb).mod(P)

            val c = x3.add(z3).mod(P)
            val d = x3.subtract(z3).mod(P)
            val da = d.multiply(a).mod(P)
            val cb = c.multiply(b).mod(P)

            x3 = da.add(cb).pow(2).mod(P)
            z3 = x1.multiply(da.subtract(cb).pow(2)).mod(P)
            x2 = aa.multiply(bb).mod(P)
            z2 = e.multiply(aa.add(A24.multiply(e))).mod(P)
        }

        if (swap == 1) {
            val tx = x2; x2 = x3; x3 = tx
            val tz = z2; z2 = z3; z3 = tz
        }

        // res = x2 * z2^(p - 2) mod p
        val invZ2 = z2.modPow(P.subtract(BigInteger.valueOf(2)), P)
        val res = x2.multiply(invZ2).mod(P)

        val out = ByteArray(32)
        var temp = res
        for (i in 0 until 32) {
            out[i] = temp.and(BigInteger.valueOf(0xFF)).toByte()
            temp = temp.shiftRight(8)
        }
        return out
    }
}
