package ru.korobovs.tests;

import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.korobovs.base.BaseTest;
import ru.korobovs.base.EndPoint;
import ru.korobovs.pojo.CRUDUserPojo;
import ru.korobovs.controllers.CRUDUserController;

import static io.restassured.RestAssured.rootPath;

@Epic("Reqres API Tests")
@Feature("CRUD user")
public class CRUDUsersTest extends BaseTest {

    private CRUDUserController userController = new CRUDUserController();

    @Test(groups = "positive")
    @Story("Create user")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Создаем пользователя с валидными данными")
    @Owner("SergeyK")
    @Link(value = "REQRES", url = "https://reqres.in/")
    @Tag("Позитивный")
    public void testCreateUser() {
        String name = "morpheus";
        String job = "leader";

        Response response = userController.createUser(name, job);
        CRUDUserPojo user = response.body().jsonPath().getObject(rootPath, CRUDUserPojo.class);

        Assert.assertEquals(response.statusCode(), 201);
        Assert.assertEquals(user.getName(), name);
        Assert.assertEquals(user.getJob(), job);
    }

    @Test
    @Story("Create user")
    @Severity(SeverityLevel.NORMAL)
    @Description("Создаем пользователя с именем \"null\"")
    @Owner("SergeyK")
    @Link(value = "REQRES", url = "https://reqres.in/")
    @Tag("Негативный")
    public void testCreateUserNullName() {
        Response response = userController.createUser(null, "leader");

        Assert.assertEquals(response.statusCode(), 201);
    }

    @Test(groups = "positive")
    @Story("Update user")
    @Severity(SeverityLevel.NORMAL)
    @Description("Обновление поля job на валидное значение")
    @Owner("SergeyK")
    @Link(value = "REQRES", url = "https://reqres.in/")
    @Tag("Позитивный")
    public void testUpdateUser() {
        String job = "zion resident";

        Response response = userController.putUser(2, "morpheus", job);
        CRUDUserPojo user = response.body().jsonPath().getObject(rootPath, CRUDUserPojo.class);

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(user.getJob(), job);
    }

    @Test
    @Story("Update user")
    @Severity(SeverityLevel.NORMAL)
    @Description("Обновление поля job с несуществующим userId")
    @Owner("SergeyK")
    @Link(value = "REQRES", url = "https://reqres.in/")
    @Tag("Негативный")
    public void testUpdateUserNotUserId() {
        Response response = userController.putUser(200, "morpheus", "zion resident");

        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test(groups = "positive")
    @Story("Delete user")
    @Severity(SeverityLevel.NORMAL)
    @Description("Удаляем нужного user")
    @Owner("SergeyK")
    @Link(value = "REQRES", url = "https://reqres.in/")
    @Tag("Позитивный")
    public void testDeleteUser() {
        Response response = userController.delete(2, EndPoint.USERS.getEndPoint());

        Assert.assertEquals(response.statusCode(), 204);
    }

    @Test
    @Story("Delete user")
    @Severity(SeverityLevel.MINOR)
    @Description("Обновление поля job с несуществующим userId")
    @Owner("SergeyK")
    @Link(value = "REQRES", url = "https://reqres.in/")
    @Tag("Негативный")
    public void testDeleteUserNotUserId() {
        Response response = userController.delete(200, EndPoint.USERS.getEndPoint());

        Assert.assertEquals(response.statusCode(), 204);
    }
}
