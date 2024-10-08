package ru.chainichek.neostudy.calculator.util;

public class Validation {
    public static final String AMOUNT_MIN = "30000";

    public static final int TERM_MIN = 6;

    public static final String NAME_PATTERN = "^[a-zA-Z]{2,30}$";

    // The pattern was changed with length limitations because RFC 5321
    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]{1,64}@[a-zA-Z0-9.-]{1,255}$";

    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    public static final int AGE_MIN = 18;

    public static final String PASSPORT_SERIES_PATTERN = "^\\d{4}$";

    public static final String PASSPORT_NUMBER_PATTERN = "^\\d{6}$";

    public static final int DEPENDENT_AMOUNT_MIN = 0;

    public static final String INN_PATTERN = "^\\d{12}$";

    public static final String SALARY_MIN = "19242";

    public static final int WORK_EXPERIENCE_MIN = 0;

    public static final String ACCOUNT_NUMBER_PATTERN = "^\\d{20}$";
}
