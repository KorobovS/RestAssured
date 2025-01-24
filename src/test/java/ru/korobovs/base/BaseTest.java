package ru.korobovs.base;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

public abstract class BaseTest {

    public static Specifications specifications = new Specifications();
    public static RequestSpecification requestSpec;
    public static ResponseSpecification responseSpec;

    public static void spec(){
        requestSpec = specifications.setupRequest();
        responseSpec = specifications.setupResponse();
    }

    @BeforeMethod
    @Step("Выполнение beforeMethod")
    protected void beforeMethod(Method method) {
        ProjectUtils.logf("Запускается %s.%s", this.getClass().getName(), method.getName());
        Allure.step(String.format("Запускается %s.%s", this.getClass().getName(), method.getName()));
        spec();
        Allure.step("Файл спецификаций подгружен");
    }

    @AfterMethod
    protected void afterMethod(Method method, ITestResult testResult) {
        ProjectUtils.logf("Время выполнения %.3f сек", (testResult.getEndMillis() - testResult.getStartMillis()) / 1000.0);
        Allure.step(String.format("Время выполнения %.3f сек", (testResult.getEndMillis() - testResult.getStartMillis()) / 1000.0));
    }
}
