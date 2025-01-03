package security;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import org.apache.commons.codec.binary.Base32;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Handles Two-Factor Authentication (2FA) using Time-based One-Time Passwords (TOTP)
 * Implements the TOTP algorithm as specified in RFC 6238
 */
public class TwoFactorAuth {
    private static final int SECRET_SIZE = 20;
    private static final String ALGORITHM = "HmacSHA1";
    private static final int CODE_DIGITS = 6;
    private static final int INTERVAL = 30;
    private static final Base32 base32 = new Base32();

    /**
     * Generates a new secret key for 2FA
     * @return Base32 encoded secret key
     */
    public static String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[SECRET_SIZE];
        random.nextBytes(bytes);
        return base32.encodeToString(bytes);
    }

    /**
     * Generates a QR code for easy setup in authenticator apps
     * @param user Username
     * @param host Application host
     * @param secret Secret key
     * @return BitMatrix representing the QR code
     * @throws WriterException if QR code generation fails
     */
    public static BitMatrix generateQRCode(String user, String host, String secret) throws WriterException {
        String otpAuth = String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s",
                host, user, secret, host);
        
        return new MultiFormatWriter().encode(
                otpAuth,
                BarcodeFormat.QR_CODE,
                200,
                200
        );
    }

    /**
     * Generates a TOTP code for the current time interval
     * @param secret Secret key in Base32 format
     * @return 6-digit TOTP code
     */
    public static String generateTOTP(String secret) {
        try {
            byte[] key = base32.decode(secret);
            byte[] data = getTimeByteArray();
            
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(new SecretKeySpec(key, ALGORITHM));
            byte[] hash = mac.doFinal(data);
            
            int offset = hash[hash.length - 1] & 0xF;
            long truncatedHash = 0;
            for (int i = 0; i < 4; ++i) {
                truncatedHash <<= 8;
                truncatedHash |= (hash[offset + i] & 0xFF);
            }
            
            truncatedHash &= 0x7FFFFFFF;
            truncatedHash %= Math.pow(10, CODE_DIGITS);
            
            return String.format("%0" + CODE_DIGITS + "d", truncatedHash);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Error generating TOTP", e);
        }
    }

    /**
     * Verifies a TOTP code
     * @param secret Secret key in Base32 format
     * @param code Code to verify
     * @return true if code is valid
     */
    public static boolean verifyTOTP(String secret, String code) {
        // Check current and adjacent intervals to account for time skew
        String currentCode = generateTOTP(secret);
        if (code.equals(currentCode)) {
            return true;
        }
        
        // Check previous interval
        byte[] key = base32.decode(secret);
        long currentInterval = System.currentTimeMillis() / 1000L / INTERVAL;
        String previousCode = generateTOTPForInterval(key, currentInterval - 1);
        if (code.equals(previousCode)) {
            return true;
        }
        
        // Check next interval
        String nextCode = generateTOTPForInterval(key, currentInterval + 1);
        return code.equals(nextCode);
    }

    /**
     * Gets the current time interval as a byte array
     * @return byte array representing current time interval
     */
    private static byte[] getTimeByteArray() {
        long time = System.currentTimeMillis() / 1000L / INTERVAL;
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; --i) {
            result[i] = (byte) (time & 0xFF);
            time >>= 8;
        }
        return result;
    }

    /**
     * Generates TOTP for a specific time interval
     * @param key Secret key bytes
     * @param interval Time interval
     * @return TOTP code for the specified interval
     */
    private static String generateTOTPForInterval(byte[] key, long interval) {
        try {
            byte[] data = new byte[8];
            for (int i = 7; i >= 0; --i) {
                data[i] = (byte) (interval & 0xFF);
                interval >>= 8;
            }
            
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(new SecretKeySpec(key, ALGORITHM));
            byte[] hash = mac.doFinal(data);
            
            int offset = hash[hash.length - 1] & 0xF;
            long truncatedHash = 0;
            for (int i = 0; i < 4; ++i) {
                truncatedHash <<= 8;
                truncatedHash |= (hash[offset + i] & 0xFF);
            }
            
            truncatedHash &= 0x7FFFFFFF;
            truncatedHash %= Math.pow(10, CODE_DIGITS);
            
            return String.format("%0" + CODE_DIGITS + "d", truncatedHash);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Error generating TOTP", e);
        }
    }
}
