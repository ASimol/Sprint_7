import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreatingOrderTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private List<String> color;
    private Response OrderResponse;
    protected final String ROOT = "/api/v1/orders";

    public CreatingOrderTest(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, List color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters(name = "firstName = {0}, lastName = {1}, address = {2}, metroStation = {3}, phone = {4}, rentTime = {5}, " +
            "deliveryDate = {6}, comment = {7}, color = {8}")
    public static Object[][] dataGen() {
        return new Object[][]{
                {"Семен", "Семенов", "Родионова, 1", "Парк Культуры", "+78000001111", 5, "10.12.2022", "Жду", List.of("GREY")},
                {"Вася", "Васенев", "Родионова, 2", "Черкизовская", "+78000002222", 7, "11.12.2022", "Быстрее", List.of("GREY", "BLACK")},
                {"Петя", "Петяев", "Родионова, 3", "Курская", "+78000003333", 9, "12.12.2022", "Хочу кататься", null},
        };
    }

    @Test
    @DisplayName("Create order")
    public void checkCreateOrder() {
        Order orderBody = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        OrderResponse = given()
                .contentType(ContentType.JSON)
                .body(orderBody)
                .when()
                .post(ROOT)
                .then().log().all()
                .extract().response();
        OrderResponse.then().assertThat().body("track", notNullValue())
                .statusCode(201);
    }

    @After
    @Step("Send PUT request to /api/v1/orders/cancel")
    public void cancelOrder() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .put(ROOT+"/cancel?track=" + OrderResponse.jsonPath().getString("track"))
                .then().log().all().assertThat().body("ok", equalTo(true));
    }
}