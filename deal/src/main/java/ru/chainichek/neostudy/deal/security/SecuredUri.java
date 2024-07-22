package ru.chainichek.neostudy.deal.security;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Component
public class SecuredUri {
    private final List<String> uris = List.of("/deal/admin/**");
}
