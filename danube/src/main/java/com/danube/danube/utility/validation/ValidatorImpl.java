package com.danube.danube.utility.validation;

import com.danube.danube.custom_exception.input.InvalidInputException;

import java.util.regex.Pattern;

public class ValidatorImpl implements Validator{
    @Override
    public void validateTextInput(String value, int minLength, int maxLength){
        validateIfTextInputIsNotEmpty(value);
        if(value.length() < minLength || value.length() > maxLength){
            throw new InvalidInputException(String.format(
                    "Input should be at least %s and upmost %s characters long!", minLength, maxLength
            ));
        }
    }

    @Override
    public void validateEmailFormat(String email){
        validateIfTextInputIsNotEmpty(email);
        boolean matches = Pattern.matches(".+\\@.+\\..+", email);
        if(!matches){
            throw new InvalidInputException("Invalid email format!");
        }
    }

    @Override
    public void validateNumericValue(int minValue, int maxValue, int value) {
        if(value < minValue || value > maxValue){
            throw new InvalidInputException(String.format("The value is not between %s and %s", minValue, maxValue));
        }
    }

    @Override
    public void validateNumericValue(double minValue, double maxValue, double value) {
        if(value < minValue || value > maxValue){
            throw new InvalidInputException(String.format("The value is not between %s and %s", minValue, maxValue));
        }
    }

    @Override
    public void validateNumericValue(double minValue, double value) {
        if(value < minValue){
            throw new InvalidInputException(String.format("The value is smaller than %s", minValue));
        }
    }

    private void validateIfTextInputIsNotEmpty(String value){
        if(value == null || value.isBlank()){
            throw new InvalidInputException("Input is empty!");
        }
    }
}
