package ru.korobovs.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorPojo {

    private String error;

    public ErrorPojo() {
    }

    public ErrorPojo(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
