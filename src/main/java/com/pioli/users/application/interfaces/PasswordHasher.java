package com.pioli.users.application.interfaces;

public interface PasswordHasher {
    String hash(String plainPassword);
    boolean matches(String plainPassword, String encryptedPassword);
}
