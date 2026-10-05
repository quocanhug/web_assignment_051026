package vn.iotstar.util;

import java.security.SecureRandom;

public class OtpUtil_24133003 {
    private static final SecureRandom random = new SecureRandom();

    public static String generateOtp(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
