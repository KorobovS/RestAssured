package ru.korobovs.tests;

import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.korobovs.base.BaseTest;
import ru.korobovs.controllers.RegisterController;

@Epic("Reqres API Tests")
@Feature("Register")
public class RegisterTest extends BaseTest {

    private RegisterController registerController = new RegisterController();

    @Test(groups = "positive")
    @Story("Register")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Регистрация с валидными данными")
    @Owner("SergeyK")
    @Link(value = "REQRES", url = "https://reqres.in/")
    @Tag("Позитивный")
    public void testRegisterPositive() {
        Response response = registerController.register("eve.holt@reqres.in", "pistol");

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.body().jsonPath().getInt("id"), 4);
        Assert.assertEquals(response.path("token"), "QpwL5tke4Pnpja7X4");
    }

    @Test
    @Story("Register")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Регистрация с не валидными данными")
    @Owner("SergeyK")
    @Link(value = "REQRES", url = "https://reqres.in/")
    @Tag("Негативный")
    public void testRegisterNegative() {
        Response response = registerController.register("sydney@fife", null);

        Assert.assertEquals(response.statusCode(), 400);
    }
}
