package ru.chainichek.neostudy.statement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
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


    LoanStatementRequestDto loanStatementRequest;
    LoanOfferDto loanOffer;
    @BeforeEach
    void setUp() {
        loanStatementRequest = new LoanStatementRequestDto(BigDecimal.valueOf(30000),
                6,
                "Ivan",
                "Fedorov",
                null,
                "ivanfedorov@yandex",
                LocalDate.now().minusYears(20),
                "6161",
                "345678");
        loanOffer = new LoanOfferDto(null,
                BigDecimal.valueOf(30000),
                BigDecimal.valueOf(300000),
                6,
                BigDecimal.valueOf(5235.91),
                BigDecimal.valueOf(16),
                false,
                false);
    }

    @SneakyThrows
    @Test
    void getOffers_whenRequestIsValid_ThenReturnListOfOffers() {
        List<LoanOfferDto> offers = List.of(
                new LoanOfferDto(null, BigDecimal.valueOf(30000), BigDecimal.valueOf(300000), 6, BigDecimal.valueOf(5235.91), BigDecimal.valueOf(16), false, false),
                new LoanOfferDto(null, BigDecimal.valueOf(30000), BigDecimal.valueOf(300000), 6, BigDecimal.valueOf(5221.01), BigDecimal.valueOf(15), false, true),
                new LoanOfferDto(null, BigDecimal.valueOf(30000), BigDecimal.valueOf(31800), 6, BigDecimal.valueOf(5502.76), BigDecimal.valueOf(13), true, false),
                new LoanOfferDto(null, BigDecimal.valueOf(30000), BigDecimal.valueOf(31800), 6, BigDecimal.valueOf(5487.04), BigDecimal.valueOf(12), true, true)
        );

        when(statementService.getOffers(any(LoanStatementRequestDto.class))).thenReturn(offers);

        mockMvc.perform(post("/statement")
                        .content(objectMapper.writeValueAsString(loanStatementRequest))
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

    @SneakyThrows
    @Test
    void getStatement_whenRequestIsPassedAsValidButActuallyInvalidFromDeal_ThenReturnBadRequestErrorMessage() {
        when(statementService.getOffers(loanStatementRequest)).thenThrow(FeignException.BadRequest.class);
        String response = mockMvc.perform(post("/statement")
                        .content(objectMapper.writeValueAsString(loanStatementRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        assertDoesNotThrow(() -> objectMapper.readValue(response, ErrorMessage.class));
    }

    @SneakyThrows
    @Test
    void getStatement_whenUnexpectedErrorFromDeal_ThenReturnInternalErrorMessage() {
        FeignException exception = mock(FeignException.class);
        when(exception.status()).thenReturn(500);

        String exceptionContentUtf8 = objectMapper.writeValueAsString(new ErrorMessage(LocalDateTime.now(), "", 500, "", ""));
        feign.Request request = mock(feign.Request.class);
        when(exception.contentUTF8()).thenReturn(exceptionContentUtf8);
        when(exception.request()).thenReturn(request);
        when(request.url()).thenReturn("");

        when(statementService.getOffers(loanStatementRequest)).thenThrow(exception);
        String response = mockMvc.perform(post("/statement")
                        .content(objectMapper.writeValueAsString(loanStatementRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn().getResponse().getContentAsString();

        assertDoesNotThrow(() -> objectMapper.readValue(response, ErrorMessage.class));
    }

    @SneakyThrows
    @Test
    void getStatement_whenUnexpectedError_ThenReturnInternalErrorMessage() {
        when(statementService.getOffers(loanStatementRequest)).thenThrow(RuntimeException.class);
        String response = mockMvc.perform(post("/statement")
                        .content(objectMapper.writeValueAsString(loanStatementRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
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
        mockMvc.perform(post("/statement/offer")
                        .content(objectMapper.writeValueAsString(loanOffer))
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

    @SneakyThrows
    @Test
    void selectOffer_whenRequestFailsAccess_ThenReturnForbiddenErrorMessage() {

        doThrow(FeignException.Forbidden.class).when(statementService).selectOffer(any());

        String response = mockMvc.perform(post("/statement/offer")
                        .content(objectMapper.writeValueAsString(loanOffer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn().getResponse().getContentAsString();

        assertDoesNotThrow(() -> objectMapper.readValue(response, ErrorMessage.class));
    }

    @SneakyThrows
    @Test
    void selectOffer_whenRequestStatementIsNotFound_ThenReturnNotFoundErrorMessage() {

        doThrow(FeignException.NotFound.class).when(statementService).selectOffer(any());

        String response = mockMvc.perform(post("/statement/offer")
                        .content(objectMapper.writeValueAsString(loanOffer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        assertDoesNotThrow(() -> objectMapper.readValue(response, ErrorMessage.class));
    }

    @SneakyThrows
    @Test
    void selectOffer_whenRequestFailsPrecondition_ThenReturnPreconditionFailedErrorMessage() {
        FeignException exception = mock(FeignException.class);
        when(exception.status()).thenReturn(412);

        String exceptionContentUtf8 = objectMapper.writeValueAsString(new ErrorMessage(LocalDateTime.now(), "", 412, "", ""));
        feign.Request request = mock(feign.Request.class);
        when(exception.contentUTF8()).thenReturn(exceptionContentUtf8);
        when(exception.request()).thenReturn(request);
        when(request.url()).thenReturn("");

        doThrow(exception).when(statementService).selectOffer(any());

        String response = mockMvc.perform(post("/statement/offer")
                        .content(objectMapper.writeValueAsString(loanOffer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isPreconditionFailed())
                .andReturn().getResponse().getContentAsString();

        assertDoesNotThrow(() -> objectMapper.readValue(response, ErrorMessage.class));
    }

    @SneakyThrows
    @Test
    void selectOffer_whenUnexpectedErrorFromDeal_ThenReturnInternalErrorMessage() {
        FeignException exception = mock(FeignException.class);
        when(exception.status()).thenReturn(500);

        String exceptionContentUtf8 = objectMapper.writeValueAsString(new ErrorMessage(LocalDateTime.now(), "", 500, "", ""));
        feign.Request request = mock(feign.Request.class);
        when(exception.contentUTF8()).thenReturn(exceptionContentUtf8);
        when(exception.request()).thenReturn(request);
        when(request.url()).thenReturn("");

        doThrow(exception).when(statementService).selectOffer(any());
        String response = mockMvc.perform(post("/statement/offer")
                        .content(objectMapper.writeValueAsString(loanOffer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn().getResponse().getContentAsString();

        assertDoesNotThrow(() -> objectMapper.readValue(response, ErrorMessage.class));
    }

    @SneakyThrows
    @Test
    void selectOffer_whenUnexpectedErrorFromDealWithWrongFormat_ThenReturnInternalErrorMessage() {
        FeignException exception = mock(FeignException.class);
        when(exception.status()).thenReturn(600);
        when(exception.getStackTrace()).thenReturn(new StackTraceElement[]{});

        String exceptionContentUtf8 = objectMapper.writeValueAsString(new ErrorMessage(LocalDateTime.now(), "", 600, "", ""));
        feign.Request request = mock(feign.Request.class);
        when(exception.contentUTF8()).thenReturn(exceptionContentUtf8);
        when(exception.request()).thenReturn(request);
        when(request.url()).thenReturn("");

        doThrow(exception).when(statementService).selectOffer(any());
        String response = mockMvc.perform(post("/statement/offer")
                        .content(objectMapper.writeValueAsString(loanOffer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn().getResponse().getContentAsString();

        assertDoesNotThrow(() -> objectMapper.readValue(response, ErrorMessage.class));
    }

    @SneakyThrows
    @Test
    void selectOffer_whenUnexpectedError_ThenReturnInternalErrorMessage() {
        doThrow(RuntimeException.class).when(statementService).selectOffer(any());

        String response = mockMvc.perform(post("/statement/offer")
                        .content(objectMapper.writeValueAsString(loanOffer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn().getResponse().getContentAsString();

        assertDoesNotThrow(() -> objectMapper.readValue(response, ErrorMessage.class));
    }
}
