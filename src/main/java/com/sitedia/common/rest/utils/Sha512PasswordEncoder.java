package com.sitedia.common.rest.utils;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * SHA 512 encoder
 * @author cedric
 *
 */
public final class Sha512PasswordEncoder implements PasswordEncoder {

    private static final int STRENGTH = 512;

    private final String secret;

    private final ShaPasswordEncoder passwordEncoder;

    /**
     * Default constructor
     * @param secret
     */
    public Sha512PasswordEncoder(String secret) {
        this.secret = secret;
        this.passwordEncoder = new ShaPasswordEncoder(STRENGTH);
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return passwordEncoder.encodePassword(rawPassword.toString(), secret);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return passwordEncoder.encodePassword(rawPassword.toString(), secret).equals(encodedPassword);
    }

}
