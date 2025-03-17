package ru.korobovs.client;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.korobovs.base.Specifications;

public class ApiClient {

    private static ApiClient instance;
    private RequestSpecification requestSpecification;
    private static final Logger logger = LogManager.getLogger(ApiClient.class);

    private ApiClient() {
        logger.info("Creating ApiClient");

        requestSpecification = RestAssured.given()
                .spec(Specifications.setupRequest());

        RestAssured.responseSpecification = Specifications.setupResponse();
    }

    public static ApiClient getInstance() {
        logger.info("Get ApiClient instance");

        if (instance == null) {
            logger.info("Initializing ApiClient instance");
            instance = new ApiClient();
        }

        return instance;
    }

    public void getRequestSpec() {

        requestSpecification = RestAssured.given()
                .spec(Specifications.setupRequest());

        RestAssured.responseSpecification = Specifications.setupResponse();
    }

    public Response post(String endPoint, String body) {
        logger.info("Executing POST request to: " + endPoint);

        return requestSpecification.body(body).post(endPoint);
    }

    public Response put(int userId, String endPoint, String body) {
        logger.info(String.format("Executing PUT request to: %s/%d", endPoint, userId));

        return requestSpecification.body(body).put(endPoint + "/" + userId);
    }

    public Response delete(int userId, String endPoint) {
        logger.info(String.format("Executing DELETE request to: %s/%d", endPoint, userId));

        return requestSpecification.delete(endPoint + "/" + userId);
    }
}
