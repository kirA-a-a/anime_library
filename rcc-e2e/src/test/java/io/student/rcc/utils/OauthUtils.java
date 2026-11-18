package io.student.rcc.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import org.jspecify.annotations.NonNull;

public class OauthUtils {

  public static @NonNull String generateCodeVerifier() {
    final String codeVerifierAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~";
    SecureRandom secureRandom = new SecureRandom();
    int length = secureRandom.nextInt(43, 129);
    StringBuilder codeVerifier = new StringBuilder(length);

    for (int i = 0; i < length; i++) {
      int index = secureRandom.nextInt(codeVerifierAlphabet.length());
      codeVerifier.append(codeVerifierAlphabet.charAt(index));
    }

    return codeVerifier.toString();
  }

  public static String generateCodeChallenge(String codeVerifier) throws NoSuchAlgorithmException {
    byte[] bytes = codeVerifier.getBytes(StandardCharsets.UTF_8);
    byte[] digest = MessageDigest.getInstance("SHA-256").digest(bytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
  }

}
