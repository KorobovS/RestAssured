package ru.korobovs.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.korobovs.base.EndPoint;
import ru.korobovs.utils.ApiClient;

public class CRUDUserSteps {
    private ApiClient apiClient = ApiClient.getInstance();

    @Step("Create user with name: {name} and job: {job}")
    public Response createUser(String name,String job) {
        String body = String.format("""
                {
                "name" : "%s",
                "job" : "%s"
                }
                """, name, job);

        return apiClient.post(EndPoint.USERS.getEndPoint(), body);
    }

    @Step("Update user with id: {userId}, name: {name} and job: {job}")
    public Response putUser(int userId, String name, String job) {
        String body = String.format("""
                {
                "name" : "%s",
                "job" : "%s"
                }
                """, name, job);

        return apiClient.put(userId, EndPoint.USERS.getEndPoint(), body);
    }

    @Step("Delete user with id: {userId}")
    public Response delete(int userId, String endPoint) {
        return apiClient.delete(userId, endPoint);
    }
}
