package duoc.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Seguridad {

    private Seguridad() {}

    public static String sha256Hex(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar SHA-256", e);
        }
    }
}