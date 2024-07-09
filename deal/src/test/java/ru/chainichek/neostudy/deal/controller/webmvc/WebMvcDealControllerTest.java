package ru.chainichek.neostudy.deal.controller.webmvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.chainichek.neostudy.deal.controller.DealController;
import ru.chainichek.neostudy.deal.dto.offer.LoanOfferDto;
import ru.chainichek.neostudy.deal.dto.offer.LoanStatementRequestDto;
import ru.chainichek.neostudy.deal.dto.statement.EmploymentDto;
import ru.chainichek.neostudy.deal.dto.statement.FinishRegistrationRequestDto;
import ru.chainichek.neostudy.deal.dto.util.ErrorMessage;
import ru.chainichek.neostudy.deal.dto.util.InternalErrorMessage;
import ru.chainichek.neostudy.deal.exception.NotFoundException;
import ru.chainichek.neostudy.deal.exception.WrongStatusException;
import ru.chainichek.neostudy.deal.model.client.EmploymentPosition;
import ru.chainichek.neostudy.deal.model.client.EmploymentStatus;
import ru.chainichek.neostudy.deal.model.client.Gender;
import ru.chainichek.neostudy.deal.model.client.MaritalStatus;
import ru.chainichek.neostudy.deal.service.DealService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DealController.class)
class WebMvcDealControllerTest {

    public static final FinishRegistrationRequestDto FIRST_REGISTRATION_REQUEST = new FinishRegistrationRequestDto(
            Gender.MALE,
            MaritalStatus.SINGLE,
            0,
            LocalDate.now(),
            "",
            new EmploymentDto(EmploymentStatus.UNEMPLOYED,
                    "",
                    BigDecimal.ZERO,
                    EmploymentPosition.WORKER,
                    0,
                    0),
            ""
    );
    public static final UUID STATEMENT_ID = UUID.randomUUID();
    public static final LoanOfferDto OFFER = new LoanOfferDto(null,
            BigDecimal.valueOf(30000),
            BigDecimal.valueOf(300000),
            6,
            BigDecimal.valueOf(5235.91),
            BigDecimal.valueOf(16),
            false,
            false);
    public static final LoanStatementRequestDto REQUEST = new LoanStatementRequestDto(BigDecimal.valueOf(30000),
            6,
            "Ivan",
            "Fedorov",
            null,
            "ivanfedorov@yandex",
            LocalDate.now().minusYears(20),
            "6161",
            "345678");
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    DealService dealService;

    @SneakyThrows
    @Test
    void createStatement_whenRequestIsValid_ThenReturnListOfOffers() {
        List<LoanOfferDto> offers = List.of(
                new LoanOfferDto(null, BigDecimal.valueOf(30000), BigDecimal.valueOf(300000), 6, BigDecimal.valueOf(5235.91), BigDecimal.valueOf(16), false, false),
                new LoanOfferDto(null, BigDecimal.valueOf(30000), BigDecimal.valueOf(300000), 6, BigDecimal.valueOf(5221.01), BigDecimal.valueOf(15), false, true),
                new LoanOfferDto(null, BigDecimal.valueOf(30000), BigDecimal.valueOf(31800), 6, BigDecimal.valueOf(5502.76), BigDecimal.valueOf(13), true, false),
                new LoanOfferDto(null, BigDecimal.valueOf(30000), BigDecimal.valueOf(31800), 6, BigDecimal.valueOf(5487.04), BigDecimal.valueOf(12), true, true)
        );

        when(dealService.createStatement(any(LoanStatementRequestDto.class))).thenReturn(offers);

        mockMvc.perform(post("/deal/statement")
                        .content(objectMapper.writeValueAsString(REQUEST))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(offers)));
    }

    @SneakyThrows
    @Test
    void createStatement_whenRequestIsNull_ThenReturnBadRequestErrorMessage() {
        String response = mockMvc.perform(post("/deal/statement")
                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        assertDoesNotThrow(() -> objectMapper.readValue(response, ErrorMessage.class));
    }

    @SneakyThrows
    @Test
    void createStatement_whenServiceThrowsRuntimeException_ThenReturnInternalErrorMessage() {
        doThrow(RuntimeException.class).when(dealService).createStatement(any());

        String response = mockMvc.perform(post("/deal/statement")
                        .content(objectMapper.writeValueAsString(REQUEST))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn().getResponse().getContentAsString();

        assertDoesNotThrow(() -> objectMapper.readValue(response, InternalErrorMessage.class));
    }


    @SneakyThrows
    @Test
    void selectOffer_whenRequestIsValid_ThenReturnOk() {
        mockMvc.perform(post("/deal/offer/select")
                        .content(objectMapper.writeValueAsString(OFFER))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void selectOffer_whenRequestIsNull_ThenReturnBadRequestErrorMessage() {
        String response = mockMvc.perform(post("/deal/offer/select")
                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        assertDoesNotThrow(() -> objectMapper.readValue(response, ErrorMessage.class));
    }

    @SneakyThrows
    @Test
    void selectOffer_whenServiceThrowsWrongStatusException_ThenReturnPreconditionFailedErrorMessage() {
        doThrow(WrongStatusException.class).when(dealService).selectOffer(any());

        String response = mockMvc.perform(post("/deal/offer/select")
                        .content(objectMapper.writeValueAsString(OFFER))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isPreconditionFailed())
                .andReturn().getResponse().getContentAsString();

        assertDoesNotThrow(() -> objectMapper.readValue(response, ErrorMessage.class));
    }

    @SneakyThrows
    @Test
    void selectOffer_whenServiceThrowsNotFoundException_ThenReturnNotFoundErrorMessage() {
        doThrow(NotFoundException.class).when(dealService).selectOffer(any());

        String response = mockMvc.perform(post("/deal/offer/select")
                        .content(objectMapper.writeValueAsString(OFFER))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        assertDoesNotThrow(() -> objectMapper.readValue(response, ErrorMessage.class));
    }

    @SneakyThrows
    @Test
    void selectOffer_whenServiceThrowsRuntimeException_ThenReturnInternalErrorMessage() {
        doThrow(RuntimeException.class).when(dealService).selectOffer(any());

        String response = mockMvc.perform(post("/deal/offer/select")
                        .content(objectMapper.writeValueAsString(OFFER))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn().getResponse().getContentAsString();

        assertDoesNotThrow(() -> objectMapper.readValue(response, InternalErrorMessage.class));
    }

    @SneakyThrows
    @Test
    void completeStatement_whenRequestIsValid_ThenReturnOk() {
        mockMvc.perform(post("/deal/calculate/{statementId}", STATEMENT_ID)
                        .content(objectMapper.writeValueAsString(FIRST_REGISTRATION_REQUEST))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void completeStatement_whenRequestIsNull_ThenReturnBadRequestErrorMessage() {
        UUID statementId = UUID.randomUUID();
        String response = mockMvc.perform(post("/deal/calculate/{statementId}", statementId)
                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        assertDoesNotThrow(() -> objectMapper.readValue(response, ErrorMessage.class));
    }

    @SneakyThrows
    @Test
    void completeStatement_whenServiceThrowsFeignExceptionWith400Status_ThenReturnBadRequestErrorMessage() {
        FeignException feignException = mock(FeignException.class);
        when(feignException.status()).thenReturn(400);

        String exceptionContentUtf8 = objectMapper.writeValueAsString(new ErrorMessage(LocalDateTime.now(), "", 400, "", ""));
        feign.Request request = mock(feign.Request.class);
        when(feignException.contentUTF8()).thenReturn(exceptionContentUtf8);
        when(feignException.request()).thenReturn(request);
        when(request.url()).thenReturn("");

        doThrow(feignException).when(dealService).completeStatement(any(), any());

        String response = mockMvc.perform(post("/deal/calculate/{statementId}", STATEMENT_ID)
                        .content(objectMapper.writeValueAsString(FIRST_REGISTRATION_REQUEST))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        assertDoesNotThrow(() -> objectMapper.readValue(response, ErrorMessage.class));
    }

    @SneakyThrows
    @Test
    void completeStatement_whenServiceThrowsWrongStatusException_ThenReturnPreconditionFailedErrorMessage() {
        doThrow(WrongStatusException.class).when(dealService).completeStatement(any(), any());

        String response = mockMvc.perform(post("/deal/calculate/{statementId}", STATEMENT_ID)
                        .content(objectMapper.writeValueAsString(FIRST_REGISTRATION_REQUEST))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isPreconditionFailed())
                .andReturn().getResponse().getContentAsString();

        assertDoesNotThrow(() -> objectMapper.readValue(response, ErrorMessage.class));
    }

    @SneakyThrows
    @Test
    void completeStatement_whenServiceThrowsFeignExceptionWith403Status_ThenReturnForbiddenErrorMessage() {
        FeignException feignException = mock(FeignException.class);
        when(feignException.status()).thenReturn(403);

        String exceptionContentUtf8 = objectMapper.writeValueAsString(new ErrorMessage(LocalDateTime.now(), "", 403, "", ""));
        feign.Request request = mock(feign.Request.class);
        when(feignException.contentUTF8()).thenReturn(exceptionContentUtf8);
        when(feignException.request()).thenReturn(request);
        when(request.url()).thenReturn("");

        doThrow(feignException).when(dealService).completeStatement(any(), any());

        String response = mockMvc.perform(post("/deal/calculate/{statementId}", STATEMENT_ID)
                        .content(objectMapper.writeValueAsString(FIRST_REGISTRATION_REQUEST))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn().getResponse().getContentAsString();

        assertDoesNotThrow(() -> objectMapper.readValue(response, ErrorMessage.class));
    }

    @SneakyThrows
    @Test
    void completeStatement_whenServiceThrowsNotFoundException_ThenReturnNotFoundErrorMessage() {
        doThrow(NotFoundException.class).when(dealService).completeStatement(any(), any());

        String response = mockMvc.perform(post("/deal/calculate/{statementId}", STATEMENT_ID)
                        .content(objectMapper.writeValueAsString(FIRST_REGISTRATION_REQUEST))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        assertDoesNotThrow(() -> objectMapper.readValue(response, ErrorMessage.class));
    }

    @SneakyThrows
    @Test
    void completeStatement_whenServiceThrowsRuntimeException_ThenReturnInternalErrorMessage() {
        doThrow(RuntimeException.class).when(dealService).completeStatement(any(), any());

        String response = mockMvc.perform(post("/deal/calculate/{statementId}", STATEMENT_ID)
                        .content(objectMapper.writeValueAsString(FIRST_REGISTRATION_REQUEST))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn().getResponse().getContentAsString();

        assertDoesNotThrow(() -> objectMapper.readValue(response, InternalErrorMessage.class));
    }

    @SneakyThrows
    @Test
    void completeStatement_whenServiceThrowsFeignExceptionWith500Status_ThenReturnInternalErrorMessage() {
        FeignException feignException = mock(FeignException.class);
        when(feignException.status()).thenReturn(500);

        String exceptionContentUtf8 = objectMapper.writeValueAsString(new ErrorMessage(LocalDateTime.now(), "", 500, "", ""));
        feign.Request request = mock(feign.Request.class);
        when(feignException.contentUTF8()).thenReturn(exceptionContentUtf8);
        when(feignException.request()).thenReturn(request);
        when(request.url()).thenReturn("");

        doThrow(feignException).when(dealService).completeStatement(any(), any());

        String response = mockMvc.perform(post("/deal/calculate/{statementId}", STATEMENT_ID)
                        .content(objectMapper.writeValueAsString(FIRST_REGISTRATION_REQUEST))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn().getResponse().getContentAsString();

        assertDoesNotThrow(() -> objectMapper.readValue(response, InternalErrorMessage.class));
    }
}