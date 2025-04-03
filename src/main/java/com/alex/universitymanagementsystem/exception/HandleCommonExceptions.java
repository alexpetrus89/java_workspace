package com.alex.universitymanagementsystem.exception;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HandleCommonExceptions {

    Class<? extends Exception> exceptionType() default NullPointerException.class;

    String errorMessage() default "An error occurred";

    int statusCode() default 400;

    int errorCode() default 400;

    boolean logException() default true;

}
