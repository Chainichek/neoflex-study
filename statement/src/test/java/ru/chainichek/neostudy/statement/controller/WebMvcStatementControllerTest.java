package ru.chainichek.neostudy.statement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.chainichek.neostudy.statement.dto.prescore.LoanOfferDto;
import ru.chainichek.neostudy.statement.dto.prescore.LoanStatementRequestDto;
import ru.chainichek.neostudy.statement.dto.util.ErrorMessage;
import ru.chainichek.neostudy.statement.service.StatementService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatementController.class)
public class WebMvcStatementControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    StatementService statementService;

    @SneakyThrows
    @Test
    void getOffers_whenRequestIsValid_ThenReturnListOfOffers() {
        LoanStatementRequestDto request = new LoanStatementRequestDto(BigDecimal.valueOf(30000),
                6,
                "Ivan",
                "Fedorov",
                null,
                "ivanfedorov@yandex",
                LocalDate.now().minusYears(20),
                "6161",
                "345678");
        List<LoanOfferDto> offers = List.of(
                new LoanOfferDto(null, BigDecimal.valueOf(30000), BigDecimal.valueOf(300000), 6, BigDecimal.valueOf(5235.91), BigDecimal.valueOf(16), false, false),
                new LoanOfferDto(null, BigDecimal.valueOf(30000), BigDecimal.valueOf(300000), 6, BigDecimal.valueOf(5221.01), BigDecimal.valueOf(15), false, true),
                new LoanOfferDto(null, BigDecimal.valueOf(30000), BigDecimal.valueOf(31800), 6, BigDecimal.valueOf(5502.76), BigDecimal.valueOf(13), true, false),
                new LoanOfferDto(null, BigDecimal.valueOf(30000), BigDecimal.valueOf(31800), 6, BigDecimal.valueOf(5487.04), BigDecimal.valueOf(12), true, true)
        );

        when(statementService.getOffers(any(LoanStatementRequestDto.class))).thenReturn(offers);

        mockMvc.perform(post("/statement")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(offers)));
    }

    @SneakyThrows
    @Test
    void getStatement_whenRequestIsNull_ThenReturnBadRequestErrorMessage() {
        String response = mockMvc.perform(post("/statement")
                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        assertDoesNotThrow(() -> objectMapper.readValue(response, ErrorMessage.class));
    }

    @SneakyThrows
    @ParameterizedTest
    @ArgumentsSource(InvalidRequestArgumentsProvider.class)
    void getStatement_whenRequestIsInvalid_ThenReturnBadRequestErrorMessage(LoanStatementRequestDto invalidRequest) {
        String response = mockMvc.perform(post("/statement")
                        .content(objectMapper.writeValueAsString(invalidRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        assertDoesNotThrow(() -> objectMapper.readValue(response, ErrorMessage.class));
    }

    static final class InvalidRequestArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(new LoanStatementRequestDto(BigDecimal.ZERO,
                            6,
                            "Ivan",
                            "Fedorov",
                            null,
                            "ivanfedorov@yandex",
                            LocalDate.now().minusYears(20),
                            "6161",
                            "345678")),
                    Arguments.of(new LoanStatementRequestDto(BigDecimal.valueOf(30000),
                            5,
                            "Ivan",
                            "Fedorov",
                            null,
                            "ivanfedorov@yandex",
                            LocalDate.now().minusYears(20),
                            "6161",
                            "345678")),
                    Arguments.of(new LoanStatementRequestDto(BigDecimal.valueOf(30000),
                            6,
                            null,
                            "Fedorov",
                            null,
                            "ivanfedorov@yandex",
                            LocalDate.now().minusYears(20),
                            "6161",
                            "345678")),
                    Arguments.of(new LoanStatementRequestDto(BigDecimal.valueOf(30000),
                            6,
                            "Ivan",
                            "Fedorov",
                            "1",
                            "ivanfedorov@yandex",
                            LocalDate.now().minusYears(20),
                            "6161",
                            "345678")),
                    Arguments.of(new LoanStatementRequestDto(BigDecimal.valueOf(30000),
                            6,
                            "Ivan",
                            "Fedorov",
                            null,
                            "ivanfedorov@yandex",
                            LocalDate.now(),
                            "6161",
                            "345678")),
                    Arguments.of(new LoanStatementRequestDto(BigDecimal.valueOf(30000),
                            6,
                            "Ivan",
                            "Fedorov",
                            null,
                            "ivanfedorovyandex",
                            LocalDate.now().minusYears(20),
                            "6161",
                            "345678")),
                    Arguments.of(new LoanStatementRequestDto(BigDecimal.valueOf(30000),
                            6,
                            "Ivan",
                            "Fedorov",
                            null,
                            "ivanfedorov@yandex",
                            LocalDate.now().minusYears(20),
                            "series",
                            "345678")),
                    Arguments.of(new LoanStatementRequestDto(BigDecimal.valueOf(30000),
                            6,
                            "Ivan",
                            "Fedorov",
                            null,
                            "ivanfedorov@yandex",
                            LocalDate.now().minusYears(20),
                            "6161",
                            "number")),
                    Arguments.of(new LoanStatementRequestDto(BigDecimal.ZERO,
                            5,
                            "1",
                            "1",
                            "1",
                            "ivanfedorovyandex",
                            LocalDate.now(),
                            "series",
                            "number")
                    )
            );
        }
    }

    @SneakyThrows
    @Test
    void selectOffer_whenRequestIsValid_ThenReturnOk() {
        final LoanOfferDto offer = new LoanOfferDto(null,
                BigDecimal.valueOf(30000),
                BigDecimal.valueOf(300000),
                6,
                BigDecimal.valueOf(5235.91),
                BigDecimal.valueOf(16),
                false,
                false);

        mockMvc.perform(post("/statement/offer")
                        .content(objectMapper.writeValueAsString(offer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void selectOffer_whenRequestIsValid_ThenReturnBadRequestErrorMessage() {
        String response = mockMvc.perform(post("/statement/offer")
                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        assertDoesNotThrow(() -> objectMapper.readValue(response, ErrorMessage.class));
    }
}
