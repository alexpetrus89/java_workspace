package com.alex.universitymanagementsystem.dto;

import java.util.HashMap;
import java.util.Map;

public class FieldErrorViewMappings {

    private Map<String, String> errorViewByField = new HashMap<>();

    public Map<String, String> getErrorViewByField() {
        return errorViewByField;
    }

    public void setErrorViewByField(Map<String, String> errorViewByField) {
        this.errorViewByField = errorViewByField;
    }

    public String resolveErrorView(String fieldName) {
        return errorViewByField.getOrDefault(fieldName, "exception/illegal/illegal-parameter");
    }
}

