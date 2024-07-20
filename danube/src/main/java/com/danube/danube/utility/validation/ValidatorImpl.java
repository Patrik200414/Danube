package com.danube.danube.utility.validation;

import com.danube.danube.custom_exception.input.InvalidInputException;

import java.util.regex.Pattern;

public class ValidatorImpl implements Validator{
    public void validateTextInputIsNotEmpty(String value){
        if(value == null || value.isBlank()){
            throw new InvalidInputException("Input is empty!");
        }
    }

    public void validateEmailFormat(String email){
        boolean matches = Pattern.matches(".+\\@.+\\..+", email);
        if(!matches){
            throw new InvalidInputException("Invalid email format!");
        }
    }
}
