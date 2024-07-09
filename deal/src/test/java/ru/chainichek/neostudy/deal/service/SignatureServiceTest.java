package ru.chainichek.neostudy.deal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SignatureServiceTest {
    SignatureService signatureService;
    int length = 6;

    @BeforeEach
    void setUp() {
        signatureService = new SignatureService();
        ReflectionTestUtils.setField(signatureService, "length", length);
    }

    @Test
    void generateSignature() {
        String signature = signatureService.generateSignature();
        assertEquals(length, signature.length());
        assertTrue(signature.matches("^[0-9]{" + length + "}$"));
    }

    @Test
    void verifySignature() {
        String signature = signatureService.generateSignature();
        assertTrue(signatureService.verifySignature(signature, signature));
    }
}