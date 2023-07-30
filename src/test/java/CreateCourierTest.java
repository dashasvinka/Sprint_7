import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CreateCourierTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }
    @Test
    public void createNewUniqueCourierAndCheckResponse(){
        File json = new File("src/test/resources/newCourier.json");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().statusCode(201);
    }

    @Test
    public void createNotUniqueCourierAndCheckResponse(){
        File json = new File("src/test/resources/newCourier.json");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().statusCode(201);
        Response secondResponse =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        secondResponse.then().assertThat().statusCode(409);
    }

    @Test
    public void createNewUniqueCourierWithOutLogin(){
        File json = new File("src/test/resources/newCourierWithOutLogin.json");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().statusCode(400);
    }

    @Test
    public void createNewUniqueCourierWithOutPassword(){
        File json = new File("src/test/resources/newCourierWithOutPassword.json");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().statusCode(400);
    }

    @Test
    public void createNewUniqueCourierAndCheckResponseBody(){
        File json = new File("src/test/resources/newCourier.json");
        CourierCreatePassResponse courierCreatePassResponse =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier")
                        .body()
                        .as(CourierCreatePassResponse.class);
        Assert.assertEquals(true, courierCreatePassResponse.isOk());
    }

    @Test
    public void createNewUniqueCourierWithOutLoginAndCheckResponseBody(){
        File json = new File("src/test/resources/newCourierWithOutLogin.json");
        CourierCreateFailResponseWithOutField courierCreateFailResponseWithOutField =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier")
                        .body()
                        .as(CourierCreateFailResponseWithOutField.class);
        Assert.assertEquals("Недостаточно данных для создания учетной записи", courierCreateFailResponseWithOutField.getMessage());
        Assert.assertEquals(400, courierCreateFailResponseWithOutField.getCode());
    }

    @Test
    public void createNewUniqueCourierWithOutPasswordAndCheckResponseBody(){
        File json = new File("src/test/resources/newCourierWithOutPassword.json");
        CourierCreateFailResponseWithOutField сourierCreateFailResponseWithOutFieldPassword =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier")
                        .body()
                        .as(CourierCreateFailResponseWithOutField.class);
        Assert.assertEquals("Недостаточно данных для создания учетной записи", сourierCreateFailResponseWithOutFieldPassword.getMessage());
        Assert.assertEquals(400, сourierCreateFailResponseWithOutFieldPassword.getCode());
    }

    @Test
    public void createNotUniqueCourierAndCheckResponseBody(){
        File json = new File("src/test/resources/newCourier.json");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().statusCode(201);
        CourierCreateFailResponseForNotUnique сourierCreateFailResponseForNotUnique =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier")
                        .body()
                        .as(CourierCreateFailResponseForNotUnique.class);
        Assert.assertEquals("Этот логин уже используется. Попробуйте другой.", сourierCreateFailResponseForNotUnique.getMessage());
        Assert.assertEquals(409, сourierCreateFailResponseForNotUnique.getCode());
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
