package ru.korobovs.tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.korobovs.base.Specifications;
import ru.korobovs.pojo.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.rootPath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class RegresWithPojoTest {

    private static final String NAME = "morpheus";
    private static final String PATCH_NAME = "Morpheus";
    private static final String JOB = "leader";
    private static final String PUT_JOB = "zion resident";
    private static final String PATCH_JOB = "Zion Resident";

    Specifications specifications = new Specifications();

    @Test
    public void testListUsers() {

        specifications.installSpecifications();

        Response response = given()
                .get("/api/users?page=2")
                .then()
                .statusCode(200)
                .extract().response();

        JsonPath data = response.body().jsonPath();
        List<UserPojo> usersList = data.getList("data", UserPojo.class);

        Assert.assertEquals(data.getInt("page"), 2);
        Assert.assertEquals(usersList.size(), 6);
        Assert.assertEquals(usersList.get(0).getId(), 7);
        Assert.assertEquals(usersList.get(0).getEmail(), "michael.lawson@reqres.in");
        Assert.assertEquals(usersList.get(0).getFirst_name(), "Michael");
        Assert.assertEquals(usersList.get(0).getLast_name(), "Lawson");
        Assert.assertEquals(usersList.get(0).getAvatar(), "https://reqres.in/img/faces/7-image.jpg");
    }

    @Test
    public void testSingleUser() {

        specifications.installSpecifications();

        UserPojo user = given()
                .get("/api/users/2")
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getObject("data", UserPojo.class);

        Assert.assertEquals(user.getId(), 2);
        Assert.assertEquals(user.getEmail(), "janet.weaver@reqres.in");
        Assert.assertEquals(user.getFirst_name(), "Janet");
        Assert.assertEquals(user.getLast_name(), "Weaver");
        Assert.assertEquals(user.getAvatar(), "https://reqres.in/img/faces/2-image.jpg");
    }

    @Test
    public void testSingleUserNotFound() {

        specifications.installSpecifications();

        RestAssured.given()
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
                .get("/api/unknown")
                .then()
                .statusCode(200)
                .extract().response();

        List<ResourcePojo> resourceList = response.body().jsonPath().getList("data", ResourcePojo.class);

        Assert.assertEquals(response.body().jsonPath().getInt("per_page"), resourceList.size());
        Assert.assertEquals(resourceList.get(2).getId(), 3);
        Assert.assertEquals(resourceList.get(2).getName(), "true red");
        Assert.assertEquals(resourceList.get(2).getYear(), 2002);
        Assert.assertEquals(resourceList.get(2).getColor(), "#BF1932");
        Assert.assertEquals(resourceList.get(2).getPantone_value(), "19-1664");
    }

    @Test
    public void testSingleResource() {

        specifications.installSpecifications();

        ResourcePojo resource = given()
                .get("/api/unknown/2")
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getObject("data", ResourcePojo.class);

        Assert.assertEquals(resource.getId(), 2);
        Assert.assertEquals(resource.getName(), "fuchsia rose");
        Assert.assertEquals(resource.getYear(), 2001);
        Assert.assertEquals(resource.getColor(), "#C74375");
        Assert.assertEquals(resource.getPantone_value(), "17-2031");
    }

    @Test
    public void testSingleResourceNotFound() {

        specifications.installSpecifications();

        RestAssured.given()
                .get("/api/unknown/23")
                .then()
                .statusCode(404)
                .body(equalTo("{}"))
                .extract().response();
    }

    @Test
    public void testCreate() {

        specifications.installSpecifications();

        UserBodyPojo bodyUser = new UserBodyPojo(NAME, JOB);

        UserNewPojo user = given()
                .body(bodyUser)
                .post("/api/users")
                .then()
                .statusCode(201)
                .extract().as(UserNewPojo.class);

        Assert.assertEquals(user.getName(), NAME);
        Assert.assertEquals(user.getJob(), JOB);
    }

    @Test
    public void testUpdatePut() {

        specifications.installSpecifications();

        UserBodyPojo userBody = new UserBodyPojo(NAME, PUT_JOB);

        UserNewPojo user = given()
                .body(userBody)
                .put("/api/users/2")
                .then()
                .statusCode(200)
                .extract().as(UserNewPojo.class);

        Assert.assertEquals(user.getName(), NAME);
        Assert.assertEquals(user.getJob(), PUT_JOB);
    }

    @Test
    public void testUpdatePatch() {

        specifications.installSpecifications();

        UserBodyPojo userBody = new UserBodyPojo(PATCH_NAME, PATCH_JOB);

        UserNewPojo user = RestAssured.given()
                .body(userBody)
                .patch("/api/users/2")
                .then()
                .statusCode(200)
                .extract().as(UserNewPojo.class);

        Assert.assertEquals(user.getName(), PATCH_NAME);
        Assert.assertEquals(user.getJob(), PATCH_JOB);
    }

    @Test
    public void testDelete() {

        specifications.installSpecifications();

        RestAssured.given()
                .delete("/api/users/2")
                .then()
                .statusCode(204)
                .body(equalTo(""))
                .extract().response();
    }

    @Test
    public void testRegisterSuccessful() {

        specifications.installSpecifications();

        RegisterBodyPojo registerBody = new RegisterBodyPojo("eve.holt@reqres.in", "pistol");

        RegisterPojo register = given()
                .body(registerBody)
                .post("/api/register")
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getObject(rootPath, RegisterPojo.class);

        Assert.assertEquals(register.getId(), 4);
        Assert.assertEquals(register.getToken(), "QpwL5tke4Pnpja7X4");
    }

    @Test
    public void testRegisterUnsuccessful() {

        specifications.installSpecifications();

        RegisterBodyPojo registerBody = new RegisterBodyPojo("sydney@file", null);

        ErrorPojo error = given()
                .body(registerBody)
                .post("/api/register")
                .then()
                .statusCode(400)
                .extract().body().jsonPath().getObject(rootPath, ErrorPojo.class);

        Assert.assertEquals(error.getError(), "Missing password");
    }

    @Test
    public void testLoginSuccessful() {

        specifications.installSpecifications();

        LoginPojo login = new LoginPojo("eve.holt@reqres.in", "cityslicka");

        TokenPojo token = given()
                .body(login)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getObject(rootPath, TokenPojo.class);

        Assert.assertEquals(token.getToken(), "QpwL5tke4Pnpja7X4");
    }

    @Test
    public void testLoginUnsuccessful() {

        specifications.installSpecifications();

        LoginPojo login = new LoginPojo("peter@klaven", null);

        ErrorPojo error = given()
                .body(login)
                .post("/api/login")
                .then()
                .statusCode(400)
                .extract().body().jsonPath().getObject(rootPath, ErrorPojo.class);

        Assert.assertEquals(error.getError(), "Missing password");
    }

    @Test
    public void testDelayedResponse() {

        specifications.installSpecifications();

        RestAssured.given()
                .get("/api/users?delay=3")
                .then()
                .statusCode(200)
                .time(lessThan(6000L));
    }
}
