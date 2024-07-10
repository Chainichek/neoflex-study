package ru.chainichek.neostudy.dossier.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

@Configuration
public class MessageSourceConfig {
    @Value("${app.default-locale}")
    private String defaultLocale;

    @Bean
    public Locale defaultLocale() {
        return new Locale(defaultLocale);
    }

    @Bean
    public MessageSource mailMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:/messages/mail_messages");
        return messageSource;
    }

    @Bean
    public MessageSource documentMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:/messages/document_messages");
        return messageSource;
    }
}
