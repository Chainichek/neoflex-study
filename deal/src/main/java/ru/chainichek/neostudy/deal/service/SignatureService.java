package ru.chainichek.neostudy.deal.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class SignatureService {
    private final SecureRandom random = new SecureRandom();
    @Value("${app.ses.length}")
    private int length;

    public String generateSignature() {
        final StringBuilder code = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10);
            code.append(digit);
        }

        return code.toString();
    }

    public boolean verifySignature(String publicSignature, String privateSignature) {
        return publicSignature.equals(privateSignature);
    }
}
