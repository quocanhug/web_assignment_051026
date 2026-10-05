package vn.iotstar.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordUtil_24133003 {
    private static final int ITERATIONS = 600_000;
    private static byte[] derive(String password, byte[] salt, int iterations) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, 256);
        try { return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded(); }
        finally { spec.clearPassword(); }
    }
    public static String hash(String password) {
        try {
            byte[] salt = new byte[16]; new SecureRandom().nextBytes(salt);
            return "pbkdf2$" + ITERATIONS + "$" + Base64.getEncoder().encodeToString(salt) + "$"
                    + Base64.getEncoder().encodeToString(derive(password, salt, ITERATIONS));
        } catch (Exception e) { throw new IllegalStateException("Không thể bảo vệ mật khẩu.", e); }
    }
    public static boolean verify(String password, String stored) {
        if (password == null || stored == null) return false;
        if (!stored.startsWith("pbkdf2$")) // Existing demo accounts migrate after a successful login.
            return MessageDigest.isEqual(password.getBytes(StandardCharsets.UTF_8), stored.getBytes(StandardCharsets.UTF_8));
        try {
            String[] parts = stored.split("\\$");
            int iterations = Integer.parseInt(parts[1]);
            if (parts.length != 4 || iterations < 1 || iterations > 2_000_000) return false;
            return MessageDigest.isEqual(Base64.getDecoder().decode(parts[3]),
                    derive(password, Base64.getDecoder().decode(parts[2]), iterations));
        } catch (Exception e) { return false; }
    }
}
