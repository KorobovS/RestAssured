package ru.korobovs.tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class RegresTest {

    private static final String URL = "https://reqres.in";
    private static final String USER_RESOURCE = "fuchsia rose";

    @Test
    public void testListUsers() {

        Response response = given()
                .when().log().all()
                .contentType(ContentType.JSON)
                .get(URL + "/api/users?page=2")
                .then().log().all()
                .statusCode(200)
                .extract().response();

        JsonPath data = response.jsonPath();

        Assert.assertEquals(data.getInt("page"), 2);
        Assert.assertEquals(Optional.ofNullable(data.get("page").toString()).get(), "2");
    }

    @Test
    public void testSingleUser() {

        RestAssured.given()
                .when().log().all()
                .contentType(ContentType.JSON)
                .get(URL + "/api/users/2")
                .then().log().all()
                .statusCode(200)
                .body("data.id", equalTo(2))
                .body("data.email", equalTo("janet.weaver@reqres.in"));
    }

    @Test
    public void testSingleUserNotFound() {

        RestAssured.given()
                .when().log().all()
                .contentType(ContentType.JSON)
                .get(URL + "/api/users/23")
                .then().log().all()
                .statusCode(404)
                .body(equalTo("{}"))
                .extract().response();
    }

    @Test
    public void testListResource() {

        Response response = given()
                .when().log().all()
                .contentType(ContentType.JSON)
                .get(URL + "/api/unknown")
                .then().log().all()
                .statusCode(200)
                .extract().response();

        Assert.assertTrue(response.body().asString().contains(USER_RESOURCE));
    }

    @Test
    public void testSingleResource() {

        RestAssured.given()
                .when().log().all()
                .contentType(ContentType.JSON)
                .get(URL + "/api/unknown/2")
                .then().log().all()
                .statusCode(200)
                .body("data.id", equalTo(2))
                .body("data.name", equalTo(USER_RESOURCE));
    }

    @Test
    public void testSingleResourceNotFound() {

        RestAssured.given()
                .when().log().all()
                .contentType(ContentType.JSON)
                .get(URL + "/api/unknown/23")
                .then().log().all()
                .statusCode(404)
                .body(equalTo("{}"))
                .extract().response();
    }

    @Test
    public void testCreate() {

        String body = """
                {
                "name":"morpheus",
                "job":"leader"
                }
                """;

        RestAssured.given()
                .when().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .post(URL + "/api/users")
                .then().log().all()
                .statusCode(201)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("leader"));
    }

    @Test
    public void testUpdatePut() {

        String body = """
                {
                "name":"morpheus",
                "job":"zion resident"
                }
                """;

        RestAssured.given()
                .when().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .put(URL + "/api/users/2")
                .then().log().all()
                .statusCode(200)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("zion resident"));
    }

    @Test
    public void testUpdatePatch() {

        String body = """
                {
                "name":"Morpheus",
                "job":"Zion Resident"
                }
                """;

        RestAssured.given()
                .when().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .patch(URL + "/api/users/2")
                .then().log().all()
                .statusCode(200)
                .body("name", equalTo("Morpheus"))
                .body("job", equalTo("Zion Resident"));
    }

    @Test
    public void testDelete() {

        RestAssured.given()
                .when().log().all()
                .contentType(ContentType.JSON)
                .delete(URL + "/api/users/2")
                .then().log().all()
                .statusCode(204)
                .body(equalTo(""))
                .extract().response();
    }

    @Test
    public void testRegisterSuccessful() {

        String body = """
                {
                "email":"eve.holt@reqres.in",
                "password":"pistol"
                }
                """;

        RestAssured.given()
                .when().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .post(URL + "/api/register")
                .then().log().all()
                .statusCode(200)
                .body("id", equalTo(4))
                .body("token", equalTo("QpwL5tke4Pnpja7X4"));
    }

    @Test
    public void testRegisterUnsuccessful() {

        String body = """
                {
                "email":"sydney@file"
                }
                """;

        RestAssured.given()
                .when().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .post(URL + "/api/register")
                .then().log().all()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }

    @Test
    public void testLoginSuccessful() {

        String body = """
                {
                "email": "eve.holt@reqres.in",
                "password": "cityslicka"
                }
                """;

        RestAssured.given()
                .when().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .post(URL + "/api/login")
                .then().log().all()
                .statusCode(200)
                .body("token", equalTo("QpwL5tke4Pnpja7X4"));
    }

    @Test
    public void testLoginUnsuccessful() {

        String body = """
                {
                "email": "peter@klaven"
                }
                """;

        RestAssured.given()
                .when().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .post(URL + "/api/login")
                .then().log().all()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }

    @Test
    public void testDelayedResponse() {

        RestAssured.given()
                .when().log().all()
                .contentType(ContentType.JSON)
                .get(URL + "/api/users?delay=3")
                .then().log().all()
                .statusCode(200)
                .time(lessThan(6000L));
    }
}
