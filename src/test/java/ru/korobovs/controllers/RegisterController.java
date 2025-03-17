package ru.korobovs.controllers;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.korobovs.base.EndPoint;
import ru.korobovs.client.ApiClient;

public class RegisterController {

    private ApiClient apiClient = ApiClient.getInstance();

    @Step("Register: email = {email}, password = {password}")
    public Response register(String email, String password) {
        String body = String.format("""
                {"email": "%s",
                "password": "%s"}
                """, email, password);
        return apiClient.post(EndPoint.REGISTER.getEndPoint(), body);
    }
}
