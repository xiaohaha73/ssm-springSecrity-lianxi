package com.EncodingTest;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncodingTest {

    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // $2a$10$qJt3wgT9PxdZSijKRyHPDeyue8n/zAPDobU4FHHP2fBQRVWy9.8H2
        System.out.println(passwordEncoder.encode("123"));
    }
}
