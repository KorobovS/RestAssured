package ru.korobovs.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.korobovs.base.Specifications;

import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

@Epic("API tests")
public class ApiTest {

    private static final String USER_RESOURCE = "fuchsia rose";

    Specifications specifications = new Specifications();

    @Test
    public void testListUsers() {

        specifications.installSpecifications();

        Response response = given()
                .filter(new AllureRestAssured())
                .get("/api/users?page=2")
                .then()
                .statusCode(200)
                .extract().response();

        JsonPath data = response.jsonPath();
        List<Integer> listUserId = data.getList("data.id");
        List<Object> usersList = data.getList("data");
        HashMap<String, Object> user0 = (HashMap<String, Object>) data.getList("data").get(0);

        Assert.assertEquals(data.getInt("page"), 2);
        Assert.assertEquals(Optional.ofNullable(data.get("page").toString()).get(), "2");
        Assert.assertEquals(usersList.size(), 6);
        Assert.assertEquals(List.of(7, 8, 9, 10, 11, 12), listUserId);
        Assert.assertEquals(user0.get("id"), 7);
        Assert.assertEquals(user0.get("email"), "michael.lawson@reqres.in");
        Assert.assertEquals(user0.get("first_name"), "Michael");
        Assert.assertEquals(user0.get("last_name"), "Lawson");
        Assert.assertEquals(user0.get("avatar"), "https://reqres.in/img/faces/7-image.jpg");
    }

    @Test
    public void testSingleUser() {

        specifications.installSpecifications();

        RestAssured.given()
                .filter(new AllureRestAssured())
                .get("/api/users/2")
                .then()
                .statusCode(200)
                .body("data.id", equalTo(2))
                .body("data.email", equalTo("janet.weaver@reqres.in"));
    }

    @Test
    public void testSingleUserNotFound() {

        specifications.installSpecifications();

        RestAssured.given()
                .filter(new AllureRestAssured())
                .get("/api/users/23")
                .then()
                .statusCode(404)
                .body(equalTo("{}"))
                .extract().response();
    }

    @Test
    public void testListResource() {

        specifications.installSpecifications();

        Response response = given()
                .filter(new AllureRestAssured())
                .get("/api/unknown")
                .then().log().all()
                .statusCode(200)
                .extract().response();

        Assert.assertTrue(response.body().asString().contains(USER_RESOURCE));
    }

    @Test
    public void testSingleResource() {

        specifications.installSpecifications();

        RestAssured.given()
                .filter(new AllureRestAssured())
                .get("/api/unknown/2")
                .then()
                .statusCode(200)
                .body("data.id", equalTo(2))
                .body("data.name", equalTo(USER_RESOURCE));
    }

    @Test
    public void testSingleResourceNotFound() {

        specifications.installSpecifications();

        RestAssured.given()
                .filter(new AllureRestAssured())
                .get("/api/unknown/23")
                .then()
                .statusCode(404)
                .body(equalTo("{}"))
                .extract().response();
    }

    @Test
    public void testCreate() {

        specifications.installSpecifications();

        String body = """
                {
                "name":"morpheus",
                "job":"leader"
                }
                """;

        RestAssured.given()
                .filter(new AllureRestAssured())
                .body(body)
                .post("/api/users")
                .then()
                .statusCode(201)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("leader"));
    }

    @Test
    public void testUpdatePut() {

        specifications.installSpecifications();

        String body = """
                {
                "name":"morpheus",
                "job":"zion resident"
                }
                """;

        RestAssured.given()
                .filter(new AllureRestAssured())
                .body(body)
                .put("/api/users/2")
                .then()
                .statusCode(200)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("zion resident"));
    }

    @Test
    public void testUpdatePatch() {

        specifications.installSpecifications();

        String body = """
                {
                "name":"Morpheus",
                "job":"Zion Resident"
                }
                """;

        RestAssured.given()
                .filter(new AllureRestAssured())
                .body(body)
                .patch("/api/users/2")
                .then()
                .statusCode(200)
                .body("name", equalTo("Morpheus"))
                .body("job", equalTo("Zion Resident"));
    }

    @Test
    public void testDelete() {

        specifications.installSpecifications();

        RestAssured.given()
                .filter(new AllureRestAssured())
                .delete("/api/users/2")
                .then()
                .statusCode(204)
                .body(equalTo(""))
                .extract().response();
    }

    @Test
    public void testRegisterSuccessful() {

        specifications.installSpecifications();

        String body = """
                {
                "email":"eve.holt@reqres.in",
                "password":"pistol"
                }
                """;

        RestAssured.given()
                .filter(new AllureRestAssured())
                .body(body)
                .post("/api/register")
                .then()
                .statusCode(200)
                .body("id", equalTo(4))
                .body("token", equalTo("QpwL5tke4Pnpja7X4"));
    }

    @Test
    public void testRegisterUnsuccessful() {

        specifications.installSpecifications();

        String body = """
                {
                "email":"sydney@file"
                }
                """;

        RestAssured.given()
                .filter(new AllureRestAssured())
                .body(body)
                .post("/api/register")
                .then()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }

    @Test
    public void testLoginSuccessful() {

        specifications.installSpecifications();

        String body = """
                {
                "email": "eve.holt@reqres.in",
                "password": "cityslicka"
                }
                """;

        RestAssured.given()
                .filter(new AllureRestAssured())
                .body(body)
                .post("/api/login")
                .then()
                .statusCode(200)
                .body("token", equalTo("QpwL5tke4Pnpja7X4"));
    }

    @Test
    public void testLoginUnsuccessful() {

        specifications.installSpecifications();

        String body = """
                {
                "email": "peter@klaven"
                }
                """;

        RestAssured.given()
                .filter(new AllureRestAssured())
                .body(body)
                .post("/api/login")
                .then().log().all()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }

    @Test
    public void testDelayedResponse() {

        specifications.installSpecifications();

        RestAssured.given()
                .filter(new AllureRestAssured())
                .get("/api/users?delay=3")
                .then()
                .statusCode(200)
                .time(lessThan(6000L));
    }
}
