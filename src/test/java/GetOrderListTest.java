import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.JsonForGetListOrder;
import org.example.Order;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class GetOrderListTest {
    private String track;
    protected final String ROOT = "/api/v1/orders";

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        track = createOrder().body().jsonPath().getString("track");
    }

    @Test
    @DisplayName("Get list of orders")
    public void checkGetOrderList() {
        JsonForGetListOrder jsonOrder = given()
                .contentType(ContentType.JSON)
                .queryParam("t", track)
                .get(ROOT + "/track")
                .body()
                .as(JsonForGetListOrder.class);

        Assert.assertEquals("Nastya", jsonOrder.getOrder().getFirstName());
        Assert.assertEquals("SSS", jsonOrder.getOrder().getLastName());
        Assert.assertEquals("Родионова, 2", jsonOrder.getOrder().getAddress());
        Assert.assertEquals("Калужская", jsonOrder.getOrder().getMetroStation());
        Assert.assertEquals("+78001112233", jsonOrder.getOrder().getPhone());
        Assert.assertEquals(5, jsonOrder.getOrder().getRentTime());
        Assert.assertTrue(jsonOrder.getOrder().getDeliveryDate().contains("2022-12-10"));
        Assert.assertEquals("Жду заказ", jsonOrder.getOrder().getComment());
        Assert.assertEquals("GREY", jsonOrder.getOrder().getColor().get(0));
    }

    @Step("Create order. Send POST requests to /api/v1/orders")
    public Response createOrder() {
        Order order = new Order("Nastya", "SSS", "Родионова, 2", "Калужская", "+78001112233",
                5, "2022-12-10", "Жду заказ", List.of("GREY"));
        return given()
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(ROOT)
                .then().log().all().assertThat().statusCode(201)
                .extract().response();
    }

    @After
    @Step("Send PUT request to /api/v1/orders/cancel")
    public void cancelOrder() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .put("/api/v1/orders/cancel?track=" + track)
                .then().log().all().assertThat().body("ok", equalTo(true));
    }
}
