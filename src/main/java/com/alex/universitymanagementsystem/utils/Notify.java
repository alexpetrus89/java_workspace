package com.alex.universitymanagementsystem.utils;


/**
 * Notify class represents a notification message.
 * It contains a single field 'content' which holds the message.
 */
public class Notify {

    // instance variables
    private final String content;

    // constructor
    public Notify(String content) {
        this.content = content;
    }

    // getters
    public String getContent() {
        return content;
    }


}
