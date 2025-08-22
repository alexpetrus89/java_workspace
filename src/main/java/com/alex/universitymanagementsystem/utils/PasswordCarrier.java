package com.alex.universitymanagementsystem.utils;

import com.alex.universitymanagementsystem.annotation.PasswordMatches;

@PasswordMatches
public interface PasswordCarrier {

    // getters
    String getPassword();
    String getConfirm();

    // setters
    void setPassword(String password);
    void setConfirm(String confirm);


}
