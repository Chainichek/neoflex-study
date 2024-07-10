package ru.chainichek.neostudy.dossier.service;

import jakarta.mail.internet.MimeMessage;
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
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import ru.chainichek.neostudy.dossier.model.dossier.EmailTheme;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {
    @InjectMocks
    MailService mailService;

    @Mock
    JavaMailSender mailSender;
    @Mock
    MessageSource mailMessageSource;

    String to = "mail@service.test";

    @ParameterizedTest
    @ArgumentsSource(AllowedSimpleMailThemeArgumentProvider.class)
    void sendSimpleMail_whenThemeIsAllowed_thenDoesNotThrow(EmailTheme theme) {
        when(mailMessageSource.getMessage(any(), any(), any())).thenReturn("");

        assertDoesNotThrow(() -> mailService.sendSimpleMail(to, theme));

        verify(mailMessageSource, times(2)).getMessage(any(), any(), any());
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    static final class AllowedSimpleMailThemeArgumentProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(Arguments.of(EmailTheme.FINISH_REGISTRATION),
                    Arguments.of(EmailTheme.CREATE_DOCUMENTS),
                    Arguments.of(EmailTheme.CREATE_DOCUMENTS),
                    Arguments.of(EmailTheme.STATEMENT_DENIED));
        }
    }

    @ParameterizedTest
    @ArgumentsSource(NotAllowedSimpleMailThemeArgumentProvider.class)
    void sendSimpleMail_whenThemeIsNotAllowed_thenThrowIllegalArgumentException(EmailTheme theme) {
        assertThrows(IllegalArgumentException.class, () -> mailService.sendSimpleMail(to, theme));

        verify(mailMessageSource, never()).getMessage(any(), any(), any());
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }


    static final class NotAllowedSimpleMailThemeArgumentProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(Arguments.of(EmailTheme.SEND_DOCUMENTS),
                    Arguments.of(EmailTheme.SEND_SES));
        }
    }

    @Test
    void sendDocumentMail() {
        UUID statementId = mock(UUID.class);
        MimeMessage mimeMessage = mock(MimeMessage.class);

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(mailMessageSource.getMessage(any(), any(), any())).thenReturn("");

        assertDoesNotThrow(() -> mailService.sendDocumentMail(to, statementId, new byte[]{}));

        verify(mailMessageSource, times(2)).getMessage(any(), any(), any());
        verify(mailSender).send(eq(mimeMessage));
    }

    @Test
    void sendSesMail() {
        UUID statementId = mock(UUID.class);

        when(mailMessageSource.getMessage(any(), any(), any())).thenReturn("");

        assertDoesNotThrow(() -> mailService.sendSesMail(to, statementId, ""));

        verify(mailMessageSource, times(2)).getMessage(any(), any(), any());
        verify(mailSender).send(any(SimpleMailMessage.class));
    }
}