package step;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.example.Order;

import static io.restassured.RestAssured.given;

public class StepForOrder {
    public static final RequestSpecification REQUEST_SPECIFICATION = new RequestSpecBuilder()
            .setBaseUri("http://qa-scooter.praktikum-services.ru/api/v1")
            .setBasePath("/orders")
            .setContentType(ContentType.JSON)
            .build();

    @Step("Create order")
    public static Response order(Order body) {
        return given()
                .spec(REQUEST_SPECIFICATION)
                .body(body)
                .when()
                .post();
    }

    @Step("Get list of orders")
    public static Response checkGetOrderList() {
        return given()
                .spec(REQUEST_SPECIFICATION)
                .when()
                .get();
    }
}