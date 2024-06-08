package ru.chainichek.neostudy.deal.util.validation.client;

public class ClientValidation {
    public static final int NAME_SIZE_MAX = 30;

    public static final String NAME_PATTERN = "^[a-zA-Z]{2,%d}$".formatted(NAME_SIZE_MAX);

    public static final int EMAIL_SIZE_MAX = 320;

    // The pattern was changed with length limitations because RFC 5321
    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]{1,64}@[a-zA-Z0-9.-]{1,255}$";

    public static final int AGE_MIN = 18;

    public static final int ACCOUNT_NUMBER_SIZE_MAX = 20;
    public static final String ACCOUNT_NUMBER_PATTERN = "^\\d{%d}$".formatted(ACCOUNT_NUMBER_SIZE_MAX);
}
