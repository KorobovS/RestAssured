package ru.korobovs.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenPojo {

    private String token;

    public TokenPojo() {
    }

    public TokenPojo(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
