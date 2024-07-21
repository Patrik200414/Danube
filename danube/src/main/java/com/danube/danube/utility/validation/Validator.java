package com.danube.danube.utility.validation;

public interface Validator {
    void validateTextInput(String value, int minLength, int maxLength);
    void validateEmailFormat(String email);
    void validateNumericValue(int minValue, int maxValue, int value);
    void validateNumericValue(double minValue, double maxValue, double value);
    void validateNumericValue(double minValue, double value);
}
