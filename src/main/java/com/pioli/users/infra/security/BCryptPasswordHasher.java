package com.pioli.users.infra.security;

import org.mindrot.jbcrypt.BCrypt;

import com.pioli.users.application.interfaces.PasswordHasher;

public class BCryptPasswordHasher implements PasswordHasher {

    @Override
    public String hash(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    @Override
    public boolean matches(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}