package ru.chainichek.neostudy.calculator.domain.util;

public class ValidationMessage {
    public static final String AMOUNT_MESSAGE = "The loan amount must be more than 30,000";

    public static final String TERM_MESSAGE = "The loan term must be more than 6 months";

    public static final String NAME_MESSAGE = "Names must be between 2 and 30 characters";

    public static final String EMAIL_MESSAGE = "Email must comply with RFC 5321";

    public static final String PASSPORT_SERIES_MESSAGE = "Passport series must be 4 digits";

    public static final String PASSPORT_NUMBER_MESSAGE = "Passport number must be 6 digits";
}
