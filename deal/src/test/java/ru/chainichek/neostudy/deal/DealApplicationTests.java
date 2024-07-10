package ru.chainichek.neostudy.deal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@EmbeddedKafka
@TestPropertySource(properties = "spring.liquibase.enabled=false")
class DealApplicationTests {

	@Test
	void contextLoads() {
	}

}
