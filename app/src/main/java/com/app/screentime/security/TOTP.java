package com.app.screentime.security;

import com.app.screentime.BuildConfig;
import com.app.screentime.config.AppSecrets;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.Arrays;

public class TOTP {
    // Default parameters
    private static final String HMAC_ALGO = "HmacSHA1"; // RFC default
    private static final int DIGITS = 6;                // 6-digit code
    private static final long TIME_STEP_SECONDS = 60L;  // 60 seconds = 1 minute

    static String base32Secret = BuildConfig.TOTP_FALLBACK_SECRET;

    /**
     * Generate a TOTP code for the supplied Base32-encoded secret.
     *
     * @param secret Base32-encoded secret key (if null, uses default)
     * @return zero-padded numeric code as String
     */
    public static String generateTOTP(String secret) {
        String secretToUse = (secret != null && !secret.isEmpty()) ? secret : base32Secret;
        byte[] key = base32Decode(secretToUse);
        long timestamp = Instant.now().getEpochSecond();
        long counter = timestamp / TIME_STEP_SECONDS;
        return generateTOTPFromKeyAndCounter(key, counter, DIGITS, HMAC_ALGO);
    }

    /**
     * Generate a TOTP code using the default secret (for backward compatibility).
     *
     * @return zero-padded numeric code as String
     */
    public static String generateTOTP() {
        return generateTOTP(null);
    }

    private static String generateTOTPFromKeyAndCounter(byte[] key, long counter, int digits, String hmacAlgo) {
        try {
            // 8-byte big-endian counter
            byte[] counterBytes = ByteBuffer.allocate(8).putLong(counter).array();

            Mac mac = Mac.getInstance(hmacAlgo);
            SecretKeySpec keySpec = new SecretKeySpec(key, hmacAlgo);
            mac.init(keySpec);
            byte[] hmac = mac.doFinal(counterBytes);

            // Dynamic truncation (RFC4226)
            int offset = hmac[hmac.length - 1] & 0x0F;
            int binary =
                    ((hmac[offset] & 0x7f) << 24) |
                            ((hmac[offset + 1] & 0xff) << 16) |
                            ((hmac[offset + 2] & 0xff) << 8) |
                            (hmac[offset + 3] & 0xff);

            int otp = binary % (int) Math.pow(10, digits);
            return String.format("%0" + digits + "d", otp);
        } catch (Exception e) {
            throw new RuntimeException("TOTP generation failed", e);
        }
    }

    /**
     * Minimal Base32 decoder (RFC 4648, no padding required).
     * If you prefer, replace this with Apache Commons Codec Base32.decode().
     */
    private static byte[] base32Decode(String base32) {
        String base32Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
        // remove padding and whitespace, uppercase
        String s = base32.trim().replace("=", "").replaceAll("\\s+", "").toUpperCase();

        // each 8 chars -> 5 bytes
        int outputLength = s.length() * 5 / 8;
        byte[] result = new byte[outputLength];

        int buffer = 0;
        int bitsLeft = 0;
        int index = 0;

        for (char c : s.toCharArray()) {
            int val = base32Chars.indexOf(c);
            if (val < 0) throw new IllegalArgumentException("Invalid Base32 character: " + c);

            buffer = (buffer << 5) | val;
            bitsLeft += 5;

            if (bitsLeft >= 8) {
                bitsLeft -= 8;
                result[index++] = (byte) ((buffer >> bitsLeft) & 0xFF);
            }
        }
        // if index < result.length, return trimmed array
        if (index != result.length) {
            return Arrays.copyOf(result, index);
        }
        return result;
    }
}
