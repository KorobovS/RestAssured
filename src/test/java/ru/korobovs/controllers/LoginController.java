package ru.korobovs.controllers;

import io.restassured.response.Response;
import ru.korobovs.base.EndPoint;
import ru.korobovs.client.ApiClient;

public class LoginController {

    private ApiClient apiClient = ApiClient.getInstance();

    public Response login(String email, String password) {
        String body = String.format("""
                {"email": "%s",
                "password": "%s"}""", email, password);
        return apiClient.post(EndPoint.LOGIN.getEndPoint(), body);
    }
}
