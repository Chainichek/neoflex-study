package ru.chainichek.neostudy.dossier.config;

import com.itextpdf.text.Font;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

@Slf4j
@Configuration
public class DocumentConfig {
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final Font.FontFamily DEFAULT_FONT_FAMILY = Font.FontFamily.HELVETICA;
    private static final Font.FontStyle DEFAULT_FONT_STYLE = Font.FontStyle.NORMAL;
    private static final float DEFAULT_FONT_SIZE = 12.0f;

    @Bean
    public DateTimeFormatter formatter(@Value("${app.document.date-time-formatter.pattern:#{null}}") String pattern) {
        return getDateTimeFormatter(pattern);
    }

    @Bean
    public Font titleFont(@Value("${app.document.font.title.font-family:#{null}}") String fontFamily,
                          @Value("${app.document.font.title.font-size:#{null}}") Float fontSize,
                          @Value("${app.document.font.title.font-style:#{null}}") String fontStyle) {
        final Font font = new Font(getFontFamily(fontFamily),
                getFontSize(fontSize),
                getFontStyle(fontStyle).ordinal());
        log.debug(LogMessage.FONT_CREATION_DEBUG_MESSAGE, font, "title");
        return font;
    }


    @Bean
    public Font sectionTitleFont(@Value("${app.document.font.section-title.font-family:#{null}}") String fontFamily,
                          @Value("${app.document.font.section-title.font-size:#{null}}") Float fontSize,
                          @Value("${app.document.font.section-title.font-style:#{null}}") String fontStyle) {
        final Font font = new Font(getFontFamily(fontFamily),
                getFontSize(fontSize),
                getFontStyle(fontStyle).ordinal());
        log.debug(LogMessage.FONT_CREATION_DEBUG_MESSAGE, font, "sectionTitle");
        return font;
    }


    @Bean
    public Font contentFont(@Value("${app.document.font.content.font-family:#{null}}") String fontFamily,
                          @Value("${app.document.font.content.font-size:#{null}}") Float fontSize,
                          @Value("${app.document.font.content.font-style:#{null}}") String fontStyle) {
        final Font font = new Font(getFontFamily(fontFamily),
                getFontSize(fontSize),
                getFontStyle(fontStyle).ordinal());
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

    private Font.FontFamily getFontFamily(String fontFamilyCode) {
        if (fontFamilyCode == null) {
            return DEFAULT_FONT_FAMILY;
        }
        try {
            return Font.FontFamily.valueOf(fontFamilyCode.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn(LogMessage.CANNOT_RESOLVE_FONT_FAMILY_WARNING_MESSAGE, fontFamilyCode);
            return DEFAULT_FONT_FAMILY;
        }
    }

    private float getFontSize(Float fontSizeValue) {
        return fontSizeValue == null ? DEFAULT_FONT_SIZE : fontSizeValue;
    }

    private Font.FontStyle getFontStyle(String fontStyleCode) {
        if (fontStyleCode == null) {
            return DEFAULT_FONT_STYLE;
        }
        try {
            return Font.FontStyle.valueOf(fontStyleCode.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn(LogMessage.CANNOT_RESOLVE_FONT_STYLE_WARNING_MESSAGE, fontStyleCode);
            return DEFAULT_FONT_STYLE;
        }
    }

    private static final class LogMessage {
        public static final String FONT_CREATION_DEBUG_MESSAGE = "Created font {} for {}";
        public static final String CANNOT_RESOLVE_DATE_TIME_FORMATTER_PATTERN_WARNING_MESSAGE = "Can't resolve date time formatter pattern {}, using default date time formatter";
        public static final String CANNOT_RESOLVE_FONT_FAMILY_WARNING_MESSAGE = "Can't resolve font family code {}, using default font family";
        public static final String CANNOT_RESOLVE_FONT_STYLE_WARNING_MESSAGE = "Can't resolve font style code {}, using default font style";
    }
}
