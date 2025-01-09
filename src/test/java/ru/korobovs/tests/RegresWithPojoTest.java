package ru.korobovs.tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.korobovs.pojo.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.rootPath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class RegresWithPojoTest {

    private static final String URL = "https://reqres.in";
    private static final String USER_RESOURCE = "fuchsia rose";
    private static final String NAME = "morpheus";
    private static final String PATCH_NAME = "Morpheus";
    private static final String JOB = "leader";
    private static final String PUT_JOB = "zion resident";
    private static final String PATCH_JOB = "Zion Resident";

    @Test
    public void testListUsers() {

        Response response = given()
                .when().log().all()
                .contentType(ContentType.JSON)
                .get(URL + "/api/users?page=2")
                .then().log().all()
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

        UserPojo user = given()
                .when().log().all()
                .contentType(ContentType.JSON)
                .get(URL + "/api/users/2")
                .then().log().all()
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

        ResourcePojo resource = given()
                .when().log().all()
                .contentType(ContentType.JSON)
                .get(URL + "/api/unknown/2")
                .then().log().all()
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

        UserBodyPojo bodyUser = new UserBodyPojo(NAME, JOB);

        UserBodyPojo user = given()
                .when().log().all()
                .contentType(ContentType.JSON)
                .body(bodyUser)
                .post(URL + "/api/users")
                .then().log().all()
                .statusCode(201)
                .extract().as(UserBodyPojo.class);

        Assert.assertEquals(user.getName(), NAME);
        Assert.assertEquals(user.getJob(), JOB);
    }

    @Test
    public void testUpdatePut() {

        UserBodyPojo userBody = new UserBodyPojo(NAME, PUT_JOB);

        UserNewPojo user = given()
                .when().log().all()
                .contentType(ContentType.JSON)
                .body(userBody)
                .put(URL + "/api/users/2")
                .then().log().all()
                .statusCode(200)
                .extract().as(UserNewPojo.class);

        Assert.assertEquals(user.getName(), NAME);
        Assert.assertEquals(user.getJob(), PUT_JOB);
    }

    @Test
    public void testUpdatePatch() {

        UserBodyPojo userBody = new UserBodyPojo(PATCH_NAME, PATCH_JOB);

        UserNewPojo user = RestAssured.given()
                .when().log().all()
                .contentType(ContentType.JSON)
                .body(userBody)
                .patch(URL + "/api/users/2")
                .then().log().all()
                .statusCode(200)
                .extract().as(UserNewPojo.class);

        Assert.assertEquals(user.getName(), PATCH_NAME);
        Assert.assertEquals(user.getJob(), PATCH_JOB);
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

        RegisterBodyPojo registerBody = new RegisterBodyPojo("eve.holt@reqres.in", "pistol");

        RegisterPojo register = given()
                .when().log().all()
                .contentType(ContentType.JSON)
                .body(registerBody)
                .post(URL + "/api/register")
                .then().log().all()
                .statusCode(200)
                .extract().body().jsonPath().getObject(rootPath, RegisterPojo.class);

        Assert.assertEquals(register.getId(), 4);
        Assert.assertEquals(register.getToken(), "QpwL5tke4Pnpja7X4");
    }

    @Test
    public void testRegisterUnsuccessful() {

        RegisterBodyPojo registerBody = new RegisterBodyPojo("sydney@file", null);

        ErrorPojo error = given()
                .when().log().all()
                .contentType(ContentType.JSON)
                .body(registerBody)
                .post(URL + "/api/register")
                .then().log().all()
                .statusCode(400)
                .extract().body().jsonPath().getObject(rootPath, ErrorPojo.class);

        Assert.assertEquals(error.getError(), "Missing password");
    }

    @Test
    public void testLoginSuccessful() {

        LoginPojo login = new LoginPojo("eve.holt@reqres.in", "cityslicka");

        TokenPojo token = given()
                .when().log().all()
                .contentType(ContentType.JSON)
                .body(login)
                .post(URL + "/api/login")
                .then().log().all()
                .statusCode(200)
                .extract().body().jsonPath().getObject(rootPath, TokenPojo.class);

        Assert.assertEquals(token.getToken(), "QpwL5tke4Pnpja7X4");
    }

    @Test
    public void testLoginUnsuccessful() {

        LoginPojo login = new LoginPojo("peter@klaven", null);

        ErrorPojo error = given()
                .when().log().all()
                .contentType(ContentType.JSON)
                .body(login)
                .post(URL + "/api/login")
                .then().log().all()
                .statusCode(400)
                .extract().body().jsonPath().getObject(rootPath, ErrorPojo.class);

        Assert.assertEquals(error.getError(), "Missing password");
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
