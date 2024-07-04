package ru.chainichek.neostudy.deal.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SignatureService {
    @Value("${app.ses.length}")
    private int length;

    public String generateSignature() {
        return String.format("%" + length + "d", (int)(Math.random() * 1000000));
    }

    public boolean verifySignature(String publicSignature, String privateSignature) {
        return publicSignature.equals(privateSignature);
    }
}
