package edu.uptc.swii.loginservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class PasetoService {
    private static final byte[] SECRET_KEY = "01234567890123456789012345678901".getBytes();
    private static final String PREFIX = "v2.local.";
    private final ObjectMapper mapper = new ObjectMapper();
    private final SecureRandom random = new SecureRandom();

    public String generateToken(String username, String role) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("sub", username);
            claims.put("role", role);
            claims.put("iat", Instant.now().getEpochSecond());
            claims.put("exp", Instant.now().plus(2, ChronoUnit.HOURS).getEpochSecond());
            String payload = mapper.writeValueAsString(claims);

            byte[] iv = new byte[12];
            random.nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKey key = new SecretKeySpec(SECRET_KEY, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);
            byte[] ciphertext = cipher.doFinal(payload.getBytes());

            ByteBuffer buffer = ByteBuffer.allocate(iv.length + ciphertext.length);
            buffer.put(iv);
            buffer.put(ciphertext);
            String tokenBody = Base64.getUrlEncoder().withoutPadding().encodeToString(buffer.array());
            return PREFIX + tokenBody;
        } catch (Exception e) {
            throw new RuntimeException("Error creando token seguro", e);
        }
    }

    public String parseToken(String token) {
        try {
            if (token.startsWith(PREFIX)) {
                String body = token.substring(PREFIX.length());
                byte[] all = Base64.getUrlDecoder().decode(body);
                ByteBuffer buffer = ByteBuffer.wrap(all);
                byte[] iv = new byte[12];
                buffer.get(iv);
                byte[] cipherBytes = new byte[buffer.remaining()];
                buffer.get(cipherBytes);

                Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
                SecretKey key = new SecretKeySpec(SECRET_KEY, "AES");
                GCMParameterSpec spec = new GCMParameterSpec(128, iv);
                cipher.init(Cipher.DECRYPT_MODE, key, spec);
                byte[] plain = cipher.doFinal(cipherBytes);
                return new String(plain);
            }
            throw new IllegalArgumentException("Token no válido");
        } catch (Exception e) {
            throw new RuntimeException("Error parseando token", e);
        }
    }
}


