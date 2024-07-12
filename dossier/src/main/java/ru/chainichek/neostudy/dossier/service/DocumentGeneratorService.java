package ru.chainichek.neostudy.dossier.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.chainichek.neostudy.dossier.dto.admin.StatementDto;
import ru.chainichek.neostudy.dossier.util.validation.Validation;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class DocumentGeneratorService {
    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
    private static final Font SECTION_TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final MessageSource documentMessageSource;
    private final Locale defaultLocale;

    @SneakyThrows
    public byte[] generateDocument(@NonNull StatementDto statement) {

        if (statement.client() == null || statement.client().passport() == null || statement.credit() == null) {
            throw new IllegalArgumentException("Statement is invalid: document generation requires client, passport and credit be not null");
        }

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final Document document = new Document();

        PdfWriter.getInstance(document, byteArrayOutputStream);
        document.open();

        document.add(new Paragraph(documentMessageSource.getMessage("document.title", null, defaultLocale), TITLE_FONT));
        document.add(new Paragraph("%s: %s".formatted(documentMessageSource.getMessage("document.statement", null, defaultLocale), statement.id())));
        document.add(new Paragraph("%s: %s".formatted(documentMessageSource.getMessage("document.datetime", null, defaultLocale), LocalDateTime.now().format(FORMATTER))));

        document.add((new Paragraph("%s: ".formatted(documentMessageSource.getMessage("document.client.title", null, defaultLocale)), SECTION_TITLE_FONT)));
        document.add(new Paragraph("%s: %s".formatted(documentMessageSource.getMessage("document.client.full-name", null, defaultLocale), "%s %s %s".formatted(
                statement.client().lastName(),
                statement.client().firstName(),
                statement.client().middleName()))));
        document.add(new Paragraph("%s: %s".formatted(documentMessageSource.getMessage("document.client.birthday", null, defaultLocale), statement.client().birthdate())));
        document.add(new Paragraph("%s: %s".formatted(documentMessageSource.getMessage("document.client.passport", null, defaultLocale), "%s %s %s %s".formatted(
                statement.client().passport().series(),
                statement.client().passport().number(),
                statement.client().passport().issueBranch(),
                statement.client().passport().issueDate()
        ))));
        document.add(new Paragraph("%s: %s".formatted(documentMessageSource.getMessage("document.client.account", null, defaultLocale), statement.client().accountNumber())));

        document.add((new Paragraph("%s: ".formatted(documentMessageSource.getMessage("document.credit.title", null, defaultLocale)), SECTION_TITLE_FONT)));
        document.add(new Paragraph("%s: %s".formatted(documentMessageSource.getMessage("document.credit.amount", null, defaultLocale), statement.credit().amount())));
        document.add(new Paragraph("%s: %s".formatted(documentMessageSource.getMessage("document.credit.term", null, defaultLocale), statement.credit().term())));
        document.add(new Paragraph("%s: %s".formatted(documentMessageSource.getMessage("document.credit.monthly-payment", null, defaultLocale), statement.credit().monthlyPayment())));
        document.add(new Paragraph("%s: %s".formatted(documentMessageSource.getMessage("document.credit.rate", null, defaultLocale), statement.credit().rate())));
        document.add(new Paragraph("%s: %s".formatted(documentMessageSource.getMessage("document.credit.psk", null, defaultLocale), statement.credit().psk())));
        document.add(new Paragraph("%s: %s".formatted(documentMessageSource.getMessage("document.credit.is-insurance-enabled", null, defaultLocale), statement.credit().isInsuranceEnabled())));
        document.add(new Paragraph("%s: %s".formatted(documentMessageSource.getMessage("document.credit.is-salary-client", null, defaultLocale), statement.credit().isSalaryClient())));

        document.add((new Paragraph("%s: ".formatted(documentMessageSource.getMessage("document.credit.payment-schedule.title", null, defaultLocale)), SECTION_TITLE_FONT)));

        final PdfPTable table = new PdfPTable(new float[]{1, 2, 2, 2, 2, 2});
        table.setWidthPercentage(100);
        addTableHeader(table);

        statement.credit().paymentSchedule().forEach(element -> {
            table.addCell(element.number().toString());
            table.addCell(element.date().format(DateTimeFormatter.ofPattern(Validation.DATE_FORMAT_PATTERN)));
            table.addCell(element.totalPayment().toString());
            table.addCell(element.interestPayment().toString());
            table.addCell(element.debtPayment().toString());
            table.addCell(element.remainingDebt().toString());
        });

        document.add(table);
        document.close();

        return byteArrayOutputStream.toByteArray();
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of(documentMessageSource.getMessage("document.credit.payment-schedule.number", null, defaultLocale),
                        documentMessageSource.getMessage("document.credit.payment-schedule.date", null, defaultLocale),
                        documentMessageSource.getMessage("document.credit.payment-schedule.total-payment", null, defaultLocale),
                        documentMessageSource.getMessage("document.credit.payment-schedule.interest-payment", null, defaultLocale),
                        documentMessageSource.getMessage("document.credit.payment-schedule.debt-payment", null, defaultLocale),
                        documentMessageSource.getMessage("document.credit.payment-schedule.remaining-debt", null, defaultLocale))
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setPhrase(new Phrase(columnTitle));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });
    }
}
