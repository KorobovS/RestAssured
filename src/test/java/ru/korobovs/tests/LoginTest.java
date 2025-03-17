package ru.korobovs.tests;

import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.korobovs.base.BaseTest;
import ru.korobovs.controllers.LoginController;

@Epic("Reqres API Tests")
@Feature("Login")
public class LoginTest extends BaseTest {

    private LoginController loginController = new LoginController();

    @Test(groups = "positive")
    @Story("Login")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Логирование с валидными данными")
    @Owner("SergeyK")
    @Link(value = "REQRES", url = "https://reqres.in/")
    @Tag("Позитивный")
    public void testLoginPositive() {
        Response response = loginController.login("eve.holt@reqres.in", "cityslicka");

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.path("token"), "QpwL5tke4Pnpja7X4");
    }

    @Test
    @Story("Login")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Логирование с не валидными данными")
    @Owner("SergeyK")
    @Link(value = "REQRES", url = "https://reqres.in/")
    @Tag("Негативный")
    public void testLoginNegative() {
        Response response = loginController.login("peter@klaven", null);

        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.path("error"), "user not found");
    }
}
