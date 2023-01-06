package step;

import courier.Courier;
import courier.LoginCourier;
import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class StepForCourier {
    public static final RequestSpecification REQUEST_SPECIFICATION = new RequestSpecBuilder()
            .setBaseUri("http://qa-scooter.praktikum-services.ru/api/v1")
            .setBasePath("/courier")
            .setContentType(ContentType.JSON)
            .build();

    @Step("Create courier")
    public static Response createCourier(Courier body) {
        return given()
                .spec(REQUEST_SPECIFICATION)
                .body(body)
                .when()
                .post();
    }

    @Step("Login courier")
    public static Response loginCourier(LoginCourier body) {
        return given()
                .spec(REQUEST_SPECIFICATION)
                .body(body)
                .when()
                .post("/login");
    }
    @Step("Delete courier")
    public static Response deleteCourier(int id) {
        return given()
                .spec(REQUEST_SPECIFICATION)
                .body(id)
                .when()
                .delete("/" + id);
    }
}

