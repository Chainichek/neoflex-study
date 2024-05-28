package ru.chainichek.neostudy.calculator.domain.util;

public class Validation {
    public static final String AMOUNT_MIN = "30000";

    public static final int TERM_MIN = 6;

    public static final int NAME_SIZE_MIN = 2;

    public static final int NAME_SIZE_MAX = 30;

    // The pattern was changed with length limitations because RFC 5321
    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]{1,64}@[a-zA-Z0-9.-]{255}$";

    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";

    public static final String PASSPORT_SERIES_PATTERN = "^\\d{4}$";

    public static final String PASSPORT_NUMBER_PATTERN = "^\\d{6}$";
}
