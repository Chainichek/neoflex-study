package ru.chainichek.neostudy.calculator.domain.util;

public class ValidationMessage {
    public static final String AMOUNT_MESSAGE = "The loan amount must be more than 30,000";

    public static final String TERM_MESSAGE = "The loan term must be more than 6 months";

    public static final String NAME_MESSAGE = "Names must be between 2 and 30 latin characters";

    public static final String EMAIL_MESSAGE = "Email must comply with RFC 5321";

    public static final String AGE_MESSAGE = "Age must be greater than or equal to " + Validation.AGE_MIN;

    public static final String PASSPORT_SERIES_MESSAGE = "Passport series must be 4 digits";

    public static final String PASSPORT_NUMBER_MESSAGE = "Passport number must be 6 digits";

    public static final String DEPENDENT_AMOUNT_MESSAGE = "The number of dependent cannot be lower than " + Validation.DEPENDENT_AMOUNT_MIN;

    public static final String INN_MESSAGE = "INN number must be 12 digits";

    public static final String INN_CHECK_NUMBER_MESSAGE = "The INN check number must be valid";

    public static final String SALARY_MESSAGE = "Salary cannot be lower than " + Validation.SALARY_MIN;

    public static final String WORK_EXPERIENCE_MESSAGE = "Work experience cannot be lower than " + Validation.WORK_EXPERIENCE_MIN;

    public static final String ACCOUNT_NUMBER_MESSAGE = "Account number must be 20 digits";
}
