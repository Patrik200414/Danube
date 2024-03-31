package com.danube.danube.custom_exception.user;

public class NewPasswordNotMatchesReenterPasswordException extends RuntimeException{
    public NewPasswordNotMatchesReenterPasswordException() {
        super("The new password and the reenter password does not matches!");
    }
}
