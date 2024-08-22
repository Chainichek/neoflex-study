package ru.chainichek.neostudy.dossier.properties;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.document")
public class DocumentProperties {
    @Getter
    @Setter
    public static final class FontProperties {
        @NotNull
        private com.itextpdf.text.Font.FontFamily fontFamily = com.itextpdf.text.Font.FontFamily.HELVETICA;
        @Min(-1) @Max(72)
        private float fontSize = 12.0f;
        @NotNull
        private com.itextpdf.text.Font.FontStyle fontStyle = com.itextpdf.text.Font.FontStyle.NORMAL;
    }

    @Getter
    @Setter
    public static final class Font {
        @NotNull
        private FontProperties title;
        @NotNull
        private FontProperties sectionTitle;
        @NotNull
        private FontProperties content;
    }

    @NotBlank
    private String dateTimePattern = "dd/MM/yyyy HH:mm:ss";
    @NotNull
    private Font font;
}
