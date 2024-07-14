package ru.chainichek.neostudy.dossier.config;

import com.itextpdf.text.Font;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.chainichek.neostudy.dossier.properties.DocumentProperties;

import java.time.format.DateTimeFormatter;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(DocumentProperties.class)
public class DocumentConfig {
    private final static DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private final DocumentProperties properties;

    @Bean
    public DateTimeFormatter formatter() {
        return getDateTimeFormatter(properties.getDateTimePattern());
    }

    @Bean
    public Font titleFont() {
        final DocumentProperties.FontProperties title = properties.getFont().getTitle();
        final Font font = new Font(title.getFontFamily(),
                title.getFontSize(),
                title.getFontStyle().ordinal());
        log.debug(LogMessage.FONT_CREATION_DEBUG_MESSAGE, font, "title");
        return font;
    }


    @Bean
    public Font sectionTitleFont() {
        final DocumentProperties.FontProperties title = properties.getFont().getSectionTitle();
        final Font font = new Font(title.getFontFamily(),
                title.getFontSize(),
                title.getFontStyle().ordinal());
        log.debug(LogMessage.FONT_CREATION_DEBUG_MESSAGE, font, "sectionTitle");
        return font;
    }


    @Bean
    public Font contentFont() {
        final DocumentProperties.FontProperties title = properties.getFont().getContent();
        final Font font = new Font(title.getFontFamily(),
                title.getFontSize(),
                title.getFontStyle().ordinal());
        log.debug(LogMessage.FONT_CREATION_DEBUG_MESSAGE, font, "content");
        return font;
    }

    private DateTimeFormatter getDateTimeFormatter(String pattern) {
        if (pattern == null) {
            return DEFAULT_FORMATTER;
        }
        try {
            return DateTimeFormatter.ofPattern(pattern);
        } catch (IllegalArgumentException e) {
            log.warn(LogMessage.CANNOT_RESOLVE_DATE_TIME_FORMATTER_PATTERN_WARNING_MESSAGE, pattern);
            return DEFAULT_FORMATTER;
        }
    }


    private static final class LogMessage {
        public static final String FONT_CREATION_DEBUG_MESSAGE = "Created font {} for {}";
        public static final String CANNOT_RESOLVE_DATE_TIME_FORMATTER_PATTERN_WARNING_MESSAGE = "Can't resolve date time formatter pattern {}, using default date time formatter";
    }
}
