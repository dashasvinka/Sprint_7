import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

public class LoginCourierTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        File json = new File("src/test/resources/newCourier.json");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
    }

    @Test
    public void loginCourierHappyPathAndCheckResponseBody() {
        File json = new File("src/test/resources/loginCourierHappyPath.json");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().statusCode(200);
        LoginCourierPass loginCourierPass = response.body().as(LoginCourierPass.class);
        Assert.assertTrue(loginCourierPass.getId() > 0);
    }

    @Test
    public void loginCourierWithOutLoginAndCheckResponseBody() {
        File json = new File("src/test/resources/loginCourierWithOutLogin.json");
        LoginCourierWithOutAnyFields loginCourierWithOutAnyFields =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("api/v1/courier/login")
                        .body()
                        .as(LoginCourierWithOutAnyFields.class);
        Assert.assertEquals("Недостаточно данных для входа", loginCourierWithOutAnyFields.getMessage());
        Assert.assertEquals(400, loginCourierWithOutAnyFields.getCode());
    }

    @Test
    public void loginCourierWithOutPasswordAndCheckResponseBody() throws InterruptedException {
        File json = new File("src/test/resources/loginCourierWithOutPassword.json");
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("api/v1/courier/login");

        response.then().assertThat().wait(5000);
        LoginCourierWithOutAnyFields loginCourierWithOutAnyFields =
                response.body()
                        .as(LoginCourierWithOutAnyFields.class);
        Assert.assertEquals("Недостаточно данных для входа", loginCourierWithOutAnyFields.getMessage());
        Assert.assertEquals(400, loginCourierWithOutAnyFields.getCode());
    }

    @Test
    public void loginCourierErrorPasswordAndCheckResponseBody() {
        File json = new File("src/test/resources/loginCourierErrorPassword.json");
        LoginCourierNotFound loginCourierNotFound =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("api/v1/courier/login")
                        .body()
                        .as(LoginCourierNotFound.class);
        Assert.assertEquals("Учетная запись не найдена", loginCourierNotFound.getMessage());
        Assert.assertEquals(404, loginCourierNotFound.getCode());
    }

    @Test
    public void loginCourierErrorLoginAndCheckResponseBody() {
        File json = new File("src/test/resources/loginCourierErrorLogin.json");
        LoginCourierNotFound loginCourierNotFound =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("api/v1/courier/login")
                        .body()
                        .as(LoginCourierNotFound.class);
        Assert.assertEquals("Учетная запись не найдена", loginCourierNotFound.getMessage());
        Assert.assertEquals(404, loginCourierNotFound.getCode());
    }

    @Test
    public void loginNonexistentCourierAndCheckResponseBody() {
        File json = new File("src/test/resources/loginCourierNonexistentCourier.json");
        LoginCourierNotFound loginCourierNotFound =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("api/v1/courier/login")
                        .body()
                        .as(LoginCourierNotFound.class);
        Assert.assertEquals("Учетная запись не найдена", loginCourierNotFound.getMessage());
        Assert.assertEquals(404, loginCourierNotFound.getCode());
    }

    @After
    public void deleteCourier() {
        File json = new File("src/test/resources/courierLogin.json");
        CourierLoginResponse courierLoginResponse =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login")
                        .body()
                        .as(CourierLoginResponse.class);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .delete("/api/v1/courier/{id}", courierLoginResponse.getId());
    }
}
