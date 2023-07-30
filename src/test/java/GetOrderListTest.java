import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

public class GetOrderListTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void GetOrderListAndCheckResponseBody() {
        Response response =
                given()
                        .get("/api/v1/orders");
        response.then().assertThat().statusCode(200);
        ListOrder listOrder = response.body().as(ListOrder.class);
        Assert.assertTrue(listOrder.getOrders().size() > 0);
    }
}

