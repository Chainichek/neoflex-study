package ru.chainichek.neostudy.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class GatewayApplicationTest {
    @Test
    void contextLoads() {
    }
    @Test
    void applicationContextTest() {
        GatewayApplication.main(new String[] {});
    }
}