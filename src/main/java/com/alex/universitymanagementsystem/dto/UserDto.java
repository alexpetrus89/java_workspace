package com.alex.universitymanagementsystem.dto;

import com.alex.universitymanagementsystem.domain.User;

public class UserDto {

    // instance variable
    private User user;

    // constructor
    public UserDto(User user) {
        this.user = user;
    }

    // getter
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
