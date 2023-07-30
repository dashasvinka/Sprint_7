import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;

import static io.restassured.RestAssured.given;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    private final String fileOrder;


    public CreateOrderTest(String fileOrder) {
        this.fileOrder = fileOrder;
    }



        @Parameterized.Parameters
        public static Object[][] getData () {
            return new Object[][]{
                    {"src/test/resources/createOrderGrey.json"},
                    {"src/test/resources/createOrderBlack.json"},
                    {"src/test/resources/createOrderGreyAndBlack.json"},
                    {"src/test/resources/createOrderWithOutColor.json"}
            };
        }

        @Test
        public void CreateOrderColorOptionsAndCheckResponseBody () {
            File json = new File(fileOrder);
            Response response =
                    given()
                            .header("Content-type", "application/json")
                            .and()
                            .body(json)
                            .when()
                            .post("/api/v1/orders");
            response.then().assertThat().statusCode(201);
            CreateOrderRequest createOrderRequest = response.body().as(CreateOrderRequest.class);
            Assert.assertTrue(createOrderRequest.getTrack() > 0);
        }

    }
