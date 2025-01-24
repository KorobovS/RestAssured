package ru.korobovs.tests;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.korobovs.base.BaseTest;
import ru.korobovs.pojo.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.rootPath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class ApiWithAllureTest extends BaseTest {

    private static final int USER_ID = 2;
    private static final int NOT_USER_ID = 23;
    private static final String NAME = "morpheus";
    private static final String PATCH_NAME = "Morpheus";
    private static final String JOB = "leader";
    private static final String PUT_JOB = "zion resident";
    private static final String PATCH_JOB = "Zion Resident";

    @Test
    @Epic("Тесты с Users")
    @Feature("Получение списка Users")
    @Story("Положительные")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Получаем список пользователей на странице 2")
    @Owner("SergeyK")
    @Issue("Задача-1")
    @Link(value = "Яндекс", url = "https://ya.ru")
    public void testListUsers() {

        Response response = given(requestSpec.queryParam("page", 2), responseSpec)
                .get("/api/users")
                .then()
                .statusCode(200)
                .extract().response();

        JsonPath data = response.body().jsonPath();
        List<UserPojo> usersList = data.getList("data", UserPojo.class);

        Assert.assertEquals(data.getInt("page"), 2);
        Assert.assertEquals(data.getInt("per_page"), 6);
        Assert.assertEquals(data.getList("data.id"), List.of(7, 8, 9, 10, 11, 12));
        Assert.assertEquals(usersList.size(), 6);
        Assert.assertEquals(usersList.get(0).getId(), 7);
        Assert.assertEquals(usersList.get(0).getEmail(), "michael.lawson@reqres.in");
        Assert.assertEquals(usersList.get(0).getFirst_name(), "Michael");
        Assert.assertEquals(usersList.get(0).getLast_name(), "Lawson");
        Assert.assertEquals(usersList.get(0).getAvatar(), "https://reqres.in/img/faces/7-image.jpg");
    }

    @Test
    @Epic("Тесты с Users")
    @Feature("Получение одного User")
    @Story("Положительные")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Получаем данные одного пользователя с id 2")
    @Owner("SergeyK")
    @Issue("Задача-2")
    @Link(value = "Яндекс", url = "https://ya.ru")
    public void testSingleUser() {

        UserPojo user = given(requestSpec.pathParam("id", USER_ID), responseSpec)
                .get("/api/users/{id}")
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getObject("data", UserPojo.class);

        Assert.assertEquals(user.getId(), USER_ID);
        Assert.assertEquals(user.getEmail(), "janet.weaver@reqres.in");
        Assert.assertEquals(user.getFirst_name(), "Janet");
        Assert.assertEquals(user.getLast_name(), "Weaver");
        Assert.assertEquals(user.getAvatar(), "https://reqres.in/img/faces/2-image.jpg");
    }

    @Test
    @Epic("Тесты с Users")
    @Feature("Получение одного User")
    @Story("Отрицательный")
    @Severity(SeverityLevel.NORMAL)
    @Description("Попытка получить данные несуществующего пользователя")
    @Owner("SergeyK")
    @Issue("Задача-3")
    @Link(value = "Яндекс", url = "https://ya.ru")
    public void testSingleUserNotFound() {

        RestAssured.given(requestSpec.pathParam("id", NOT_USER_ID), responseSpec)
                .get("/api/users/{id}")
                .then()
                .statusCode(404)
                .body(equalTo("{}"))
                .extract().response();
    }

//    @Test
//    @Epic("Тесты с Resources")
//    @Feature("Получение List Resource")
//    @Story("Положительный")
//    public void testListResource() {
//
//        Response response = given(requestSpec, responseSpec)
//                .get("/api/unknown")
//                .then()
//                .statusCode(200)
//                .extract().response();
//
//        List<ResourcePojo> resourceList = response.body().jsonPath().getList("data", ResourcePojo.class);
//
//        Assert.assertEquals(response.body().jsonPath().getInt("per_page"), resourceList.size());
//        Assert.assertEquals(resourceList.get(2).getId(), 3);
//        Assert.assertEquals(resourceList.get(2).getName(), "true red");
//        Assert.assertEquals(resourceList.get(2).getYear(), 2002);
//        Assert.assertEquals(resourceList.get(2).getColor(), "#BF1932");
//        Assert.assertEquals(resourceList.get(2).getPantone_value(), "19-1664");
//    }
//
//    @Test
//    @Epic("Тесты с Resources")
//    @Feature("Получение одного Resource")
//    @Story("Положительный")
//    public void testSingleResource() {
//
//        ResourcePojo resource = given(requestSpec.pathParam("id", USER_ID), responseSpec)
//                .get("/api/unknown/{id}")
//                .then()
//                .statusCode(200)
//                .extract().body().jsonPath().getObject("data", ResourcePojo.class);
//
//        Assert.assertEquals(resource.getId(), 2);
//        Assert.assertEquals(resource.getName(), "fuchsia rose");
//        Assert.assertEquals(resource.getYear(), 2001);
//        Assert.assertEquals(resource.getColor(), "#C74375");
//        Assert.assertEquals(resource.getPantone_value(), "17-2031");
//    }
//
//    @Test
//    @Epic("Тесты с Resources")
//    @Feature("Получение одного Resource")
//    @Story("Отрицательный")
//    public void testSingleResourceNotFound() {
//
//        RestAssured.given(requestSpec.pathParam("id", NOT_USER_ID), responseSpec)
//                .get("/api/unknown/{id}")
//                .then()
//                .statusCode(404)
//                .body(equalTo("{}"))
//                .extract().response();
//    }
//
//    @Test
//    @Epic("Тесты CRUD user")
//    @Feature("Создание user")
//    @Story("Положительный")
//    public void testCreate() {
//
//        UserBodyPojo bodyUser = new UserBodyPojo(NAME, JOB);
//
//        UserNewPojo user = given(requestSpec.body(bodyUser), responseSpec)
//                .post("/api/users")
//                .then()
//                .statusCode(201)
//                .extract().as(UserNewPojo.class);
//
//        Assert.assertEquals(user.getName(), NAME);
//        Assert.assertEquals(user.getJob(), JOB);
//    }
//
//    @Test
//    @Epic("Тесты CRUD user")
//    @Feature("Обновление user")
//    @Story("Положительный")
//    public void testUpdatePut() {
//
//        UserBodyPojo userBody = new UserBodyPojo(NAME, PUT_JOB);
//
//        UserNewPojo user = given(requestSpec.pathParam("id", USER_ID).body(userBody), responseSpec)
//                .put("/api/users/{id}")
//                .then()
//                .statusCode(200)
//                .extract().as(UserNewPojo.class);
//
//        Assert.assertEquals(user.getName(), NAME);
//        Assert.assertEquals(user.getJob(), PUT_JOB);
//    }
//
//    @Test
//    @Epic("Тесты CRUD user")
//    @Feature("Обновление user")
//    @Story("Положительный")
//    public void testUpdatePatch() {
//
//        UserBodyPojo userBody = new UserBodyPojo(PATCH_NAME, PATCH_JOB);
//
//        UserNewPojo user = RestAssured.given(requestSpec.pathParam("id", USER_ID).body(userBody), responseSpec)
//                .patch("/api/users/{id}")
//                .then()
//                .statusCode(200)
//                .extract().as(UserNewPojo.class);
//
//        Assert.assertEquals(user.getName(), PATCH_NAME);
//        Assert.assertEquals(user.getJob(), PATCH_JOB);
//    }
//
//    @Test
//    @Epic("Тесты CRUD user")
//    @Feature("Удаление user")
//    @Story("Положительный")
//    public void testDelete() {
//
//        RestAssured.given(requestSpec.pathParam("id", USER_ID), responseSpec)
//                .delete("/api/users/{id}")
//                .then()
//                .statusCode(204)
//                .body(equalTo(""))
//                .extract().response();
//    }
//
//    @Test
//    @Epic("Тесты Register")
//    @Feature("Регистрация")
//    @Story("Положительный")
//    public void testRegisterSuccessful() {
//
//        RegisterBodyPojo registerBody = new RegisterBodyPojo("eve.holt@reqres.in", "pistol");
//
//        RegisterPojo register = given(requestSpec.body(registerBody), responseSpec)
//                .post("/api/register")
//                .then()
//                .statusCode(200)
//                .extract().body().jsonPath().getObject(rootPath, RegisterPojo.class);
//
//        Assert.assertEquals(register.getId(), 4);
//        Assert.assertEquals(register.getToken(), "QpwL5tke4Pnpja7X4");
//    }
//
//    @Test
//    @Epic("Тесты Register")
//    @Feature("Регистрация")
//    @Story("Отрицательный")
//    public void testRegisterUnsuccessful() {
//
//        RegisterBodyPojo registerBody = new RegisterBodyPojo("sydney@file", null);
//
//        ErrorPojo error = given(requestSpec.body(registerBody), responseSpec)
//                .post("/api/register")
//                .then()
//                .statusCode(400)
//                .extract().body().jsonPath().getObject(rootPath, ErrorPojo.class);
//
//        Assert.assertEquals(error.getError(), "Missing password");
//    }
//
//    @Test
//    @Epic("Тесты Login")
//    @Feature("Вход")
//    @Story("Положительный")
//    public void testLoginSuccessful() {
//
//        LoginPojo login = new LoginPojo("eve.holt@reqres.in", "cityslicka");
//
//        TokenPojo token = given(requestSpec.body(login), responseSpec)
//                .post("/api/login")
//                .then()
//                .statusCode(200)
//                .extract().body().jsonPath().getObject(rootPath, TokenPojo.class);
//
//        Assert.assertEquals(token.getToken(), "QpwL5tke4Pnpja7X4");
//    }
//
//    @Test
//    @Epic("Тесты Login")
//    @Feature("Вход")
//    @Story("Отрицательный")
//    public void testLoginUnsuccessful() {
//
//        LoginPojo login = new LoginPojo("peter@klaven", null);
//
//        ErrorPojo error = given(requestSpec.body(login), responseSpec)
//                .post("/api/login")
//                .then()
//                .statusCode(400)
//                .extract().body().jsonPath().getObject(rootPath, ErrorPojo.class);
//
//        Assert.assertEquals(error.getError(), "Missing password");
//    }
//
//    @Test
//    @Epic("Тесты производительности")
//    public void testDelayedResponse() {
//
//        RestAssured.given(requestSpec.queryParam("delay", 3), responseSpec)
//                .get("/api/users")
//                .then()
//                .statusCode(200)
//                .time(lessThan(6000L));
//    }
}
