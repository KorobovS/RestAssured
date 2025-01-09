package ru.korobovs.base;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Specifications {

    public RequestSpecification setupRequest() {
        return new RequestSpecBuilder()
                .log(LogDetail.ALL)
                .setContentType(ContentType.JSON)
                .setBaseUri("https://reqres.in")
                .build();
    }

    public ResponseSpecification setupResponse () {
        return new ResponseSpecBuilder()
                .log(LogDetail.ALL)
                .build();
    }

    public void installSpecifications() {
        RestAssured.requestSpecification = setupRequest();
        RestAssured.responseSpecification = setupResponse();
    }
}
