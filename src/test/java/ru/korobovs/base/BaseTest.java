package ru.korobovs.base;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeTest;

public abstract class BaseTest {

    @BeforeTest
    public void setUp() {
        Specifications.installSpecifications();
//        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
