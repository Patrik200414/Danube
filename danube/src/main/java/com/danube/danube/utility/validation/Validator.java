package com.danube.danube.utility.validation;

public interface Validator {
    void validateTextInputIsNotEmpty(String value);
    void validateEmailFormat(String email);
}
