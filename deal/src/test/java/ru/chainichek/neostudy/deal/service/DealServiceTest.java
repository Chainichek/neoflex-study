package ru.chainichek.neostudy.deal.service;

import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.chainichek.neostudy.deal.dto.calculation.CreditDto;
import ru.chainichek.neostudy.deal.dto.offer.LoanOfferDto;
import ru.chainichek.neostudy.deal.dto.offer.LoanStatementRequestDto;
import ru.chainichek.neostudy.deal.dto.statement.EmploymentDto;
import ru.chainichek.neostudy.deal.dto.statement.FinishRegistrationRequestDto;
import ru.chainichek.neostudy.deal.exception.NotFoundException;
import ru.chainichek.neostudy.deal.exception.WrongStatusException;
import ru.chainichek.neostudy.deal.model.client.Client;
import ru.chainichek.neostudy.deal.model.statement.ApplicationStatus;
import ru.chainichek.neostudy.deal.model.statement.Statement;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DealServiceTest {
    @InjectMocks
    DealService dealService;

    @Mock
    CalculatorService calculatorService;
    @Mock
    StatementService statementService;
    @Mock
    ClientService clientService;
    @Mock
    CreditService creditService;
    @Mock
    DossierService dossierService;

    @Test
    void createStatement() {
        final LoanStatementRequestDto request = mock(LoanStatementRequestDto.class);
        final List<LoanOfferDto> offers = List.of(mock(LoanOfferDto.class));

        final Client client = mock(Client.class);
        final Statement statement = mock(Statement.class);
        final UUID uuid = mock(UUID.class);


        when(clientService.createClient(request)).thenReturn(client);
        when(statementService.createStatement(client)).thenReturn(statement);
        when(statement.getId()).thenReturn(uuid);
        when(calculatorService.getOffers(request, uuid)).thenReturn(offers);

        assertNotEquals(0, dealService.createStatement(request).size());
    }

    @Test
    void selectOffer_whenStatementApplicationStatusIsPreapproval_ThenDoesNotThrow() {
        final LoanOfferDto offer = mock(LoanOfferDto.class);
        final Statement statement = mock(Statement.class);
        final UUID uuid = mock(UUID.class);

        when(offer.statementId()).thenReturn(uuid);
        when(statementService.getStatement(uuid)).thenReturn(statement);
        when(statement.getStatus()).thenReturn(ApplicationStatus.PREAPPROVAL);

        assertDoesNotThrow(() -> dealService.selectOffer(offer));

        verify(dossierService).sendFinishRegistration(eq(statement));
    }

    @Test
    void selectOffer_whenStatementIsNull_ThenThrowNotFoundException() {
        final LoanOfferDto offer = mock(LoanOfferDto.class);
        final UUID uuid = mock(UUID.class);

        when(offer.statementId()).thenReturn(uuid);
        when(statementService.getStatement(uuid)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> dealService.selectOffer(offer));
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidSelectApplicationStatusArgumentsProvider.class)
    void selectOffer_whenStatementApplicationStatusIsNotPreapproval_ThenThrowWrongStatusException(ApplicationStatus applicationStatus) {
        final LoanOfferDto offer = mock(LoanOfferDto.class);
        final Statement statement = mock(Statement.class);
        final UUID uuid = mock(UUID.class);

        when(offer.statementId()).thenReturn(uuid);
        when(statementService.getStatement(uuid)).thenReturn(statement);
        when(statement.getStatus()).thenReturn(applicationStatus);

        assertThrows(WrongStatusException.class, () -> dealService.selectOffer(offer));
    }

    static final class InvalidSelectApplicationStatusArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(ApplicationStatus.values())
                    .filter(x -> x != ApplicationStatus.PREAPPROVAL)
                    .map(Arguments::of);
        }
    }

    @Test
    void completeStatement_whenStatementApplicationIsApprovedAndCalculatorServiceDoesNotThrow_ThenDoesNotThrow() {
        final Statement statement = mock(Statement.class);
        final UUID uuid = mock(UUID.class);

        final FinishRegistrationRequestDto finishRegistrationRequest = mock(FinishRegistrationRequestDto.class);
        final EmploymentDto employment = mock(EmploymentDto.class);

        when(finishRegistrationRequest.employment()).thenReturn(employment);

        when(statementService.getStatement(uuid)).thenReturn(statement);
        when(statement.getStatus()).thenReturn(ApplicationStatus.APPROVED);

        when(clientService.updateClientOnFinishRegistration(statement.getClient(), finishRegistrationRequest)).thenReturn(null);

        when(calculatorService.calculateCredit(statement, finishRegistrationRequest.employment())).thenReturn(mock(CreditDto.class));
        when(creditService.createCredit(any(CreditDto.class))).thenReturn(null);

        assertDoesNotThrow(() -> dealService.completeStatement(uuid, finishRegistrationRequest));

        verify(dossierService).sendCreateDocuments(eq(statement));
    }

    @Test
    void completeStatement_whenStatementIsNull_ThenThrowNotFoundException() {
        final UUID uuid = mock(UUID.class);

        final FinishRegistrationRequestDto finishRegistrationRequest = mock(FinishRegistrationRequestDto.class);

        when(statementService.getStatement(uuid)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> dealService.completeStatement(uuid, finishRegistrationRequest));
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidCompleteApplicationStatusArgumentsProvider.class)
    void completeStatement_whenStatementApplicationIsNotApproved_ThenThrowWrongStatusException(ApplicationStatus applicationStatus) {
        final Statement statement = mock(Statement.class);
        final UUID uuid = mock(UUID.class);

        final FinishRegistrationRequestDto finishRegistrationRequest = mock(FinishRegistrationRequestDto.class);

        when(statementService.getStatement(uuid)).thenReturn(statement);
        when(statement.getStatus()).thenReturn(applicationStatus);

        assertThrows(WrongStatusException.class, () -> dealService.completeStatement(uuid, finishRegistrationRequest));
    }

    static final class InvalidCompleteApplicationStatusArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(ApplicationStatus.values())
                    .filter(x -> x != ApplicationStatus.APPROVED)
                    .map(Arguments::of);
        }
    }

    @Test
    void completeStatement_whenStatementApplicationIsApprovedAndCalculatorServiceThrowsFeignExceptionWith400Status_ThenThrowThisException() {
        final Statement statement = mock(Statement.class);
        final UUID uuid = mock(UUID.class);

        final FinishRegistrationRequestDto finishRegistrationRequest = mock(FinishRegistrationRequestDto.class);
        final EmploymentDto employment = mock(EmploymentDto.class);

        final FeignException feignException = mock(FeignException.class);

        when(finishRegistrationRequest.employment()).thenReturn(employment);

        when(statementService.getStatement(uuid)).thenReturn(statement);
        when(statement.getStatus()).thenReturn(ApplicationStatus.APPROVED);

        when(calculatorService.calculateCredit(statement, finishRegistrationRequest.employment())).thenThrow(feignException);
        when(feignException.status()).thenReturn(400);

        assertThrows(FeignException.class, () -> dealService.completeStatement(uuid, finishRegistrationRequest));

        verify(statementService).updateStatementOnDenied(eq(statement));
    }


    @Test
    void completeStatement_whenStatementApplicationIsApprovedAndCalculatorServiceThrowsFeignExceptionWith403Status_ThenThrowThisException() {
        final Statement statement = mock(Statement.class);
        final UUID uuid = mock(UUID.class);

        final FinishRegistrationRequestDto finishRegistrationRequest = mock(FinishRegistrationRequestDto.class);
        final EmploymentDto employment = mock(EmploymentDto.class);

        final FeignException feignException = mock(FeignException.class);

        when(finishRegistrationRequest.employment()).thenReturn(employment);

        when(statementService.getStatement(uuid)).thenReturn(statement);
        when(statement.getStatus()).thenReturn(ApplicationStatus.APPROVED);

        when(calculatorService.calculateCredit(statement, finishRegistrationRequest.employment())).thenThrow(feignException);
        when(feignException.status()).thenReturn(403);

        assertThrows(FeignException.class, () -> dealService.completeStatement(uuid, finishRegistrationRequest));

        verify(clientService).updateClientOnFinishRegistration(eq(statement.getClient()), eq(finishRegistrationRequest));
        verify(statementService).updateStatementOnDenied(eq(statement));

    }

    @Test
    void completeStatement_whenStatementApplicationIsApprovedAndCalculatorServiceThrowsFeignExceptionWithAnotherStatus_ThenThrowThisException() {
        final Statement statement = mock(Statement.class);
        final UUID uuid = mock(UUID.class);

        final FinishRegistrationRequestDto finishRegistrationRequest = mock(FinishRegistrationRequestDto.class);
        final EmploymentDto employment = mock(EmploymentDto.class);

        final FeignException feignException = mock(FeignException.class);

        when(finishRegistrationRequest.employment()).thenReturn(employment);

        when(statementService.getStatement(uuid)).thenReturn(statement);
        when(statement.getStatus()).thenReturn(ApplicationStatus.APPROVED);

        when(calculatorService.calculateCredit(statement, finishRegistrationRequest.employment())).thenThrow(feignException);
        when(feignException.status()).thenReturn(500);

        assertThrows(FeignException.class, () -> dealService.completeStatement(uuid, finishRegistrationRequest));

        verify(clientService, never()).updateClientOnFinishRegistration(eq(statement.getClient()), eq(finishRegistrationRequest));
        verify(statementService, never()).updateStatementOnDenied(eq(statement));

    }


}