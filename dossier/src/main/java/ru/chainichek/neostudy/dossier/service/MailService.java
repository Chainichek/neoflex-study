package ru.chainichek.neostudy.dossier.service;

import jakarta.mail.internet.MimeMessage;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.chainichek.neostudy.dossier.model.dossier.EmailTheme;

import java.util.Locale;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private final MessageSource mailMessageSource;
    private final Locale defaultLocale;

    @Value("${app.message.send-ses.base-path}")
    private String sendSesBasePath;
    @Value("${app.message.send-ses.send-path}")
    private String sendSesSendPath;

    public void sendSimpleMail(@NonNull String to,
                               @NonNull EmailTheme theme) {
        String subject = null;
        String body = null;

        switch (theme) {
            case FINISH_REGISTRATION -> {
                subject = mailMessageSource.getMessage("mail.finish-registration.subject", null, defaultLocale);
                body = mailMessageSource.getMessage("mail.finish-registration.body", null, defaultLocale);
            }
            case CREATE_DOCUMENTS -> {
                subject = mailMessageSource.getMessage("mail.create-documents.subject", null, defaultLocale);
                body = mailMessageSource.getMessage("mail.create-documents.body", null, defaultLocale);
            }
            case CREDIT_ISSUED -> {
                subject = mailMessageSource.getMessage("mail.credit-issued.subject", null, defaultLocale);
                body = mailMessageSource.getMessage("mail.credit-issued.body", null, defaultLocale);
            }
            case STATEMENT_DENIED -> {
                subject = mailMessageSource.getMessage("mail.statement-denied.subject", null, defaultLocale);
                body = mailMessageSource.getMessage("mail.statement-denied.body", null, defaultLocale);
            }
            default -> {
                log.debug("Can't go further and throwing exception because followed theme is not allowed in this method: {}", theme);
                throw new IllegalStateException("Unexpected value of email theme: %s".formatted(theme));
            }
        }

        final SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    @SneakyThrows
    public void sendDocumentMail(@NonNull String to,
                                 @NonNull UUID statementId,
                                 byte @NonNull [] file) {
        final MimeMessage message = mailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(mailMessageSource.getMessage("mail.send-documents.subject", null, defaultLocale));
        helper.setText(mailMessageSource.getMessage("mail.send-documents.body", null, defaultLocale), false);
        helper.addAttachment("offer-%s.pdf".formatted(statementId), new ByteArrayResource(file));

        mailSender.send(message);
    }

    public void sendSesMail(@NonNull String to,
                            @NonNull UUID statementId,
                            @NonNull String sesCode) {
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(mailMessageSource.getMessage("mail.send-ses.subject", null, defaultLocale));
        message.setText(mailMessageSource.getMessage("mail.send-ses.body",
                new Object[]{"%s/%s/%s".formatted(sendSesBasePath, statementId, sendSesSendPath), sesCode},
                defaultLocale));
        mailSender.send(message);
    }
}
