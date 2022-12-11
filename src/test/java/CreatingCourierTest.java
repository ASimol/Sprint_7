import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class CreatingCourierTest {

    private String login;
    private String password;
    private String firstName;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";

        login = "Courier111" + (int) (Math.random() * (1000));
        password = "Password222" + (int) (Math.random() * (1000));
        firstName = "LoginName333" + (int) (Math.random() * (1000));
    }

    @Test
    @DisplayName("Create courier")
    public void checkCreateCourier() {
        Response response = createCourier(login, password, firstName);

        Assert.assertEquals(201, response.statusCode());
        Assert.assertEquals("true", response.jsonPath().getString("ok"));
    }

    @Test
    @DisplayName("Create same couriers")
    public void checkCreateSameCouriers() {
        Response courier = createCourier(login, password, firstName);
        Response sameCourier = createCourier(login, password, firstName);
        Assert.assertEquals(409, sameCourier.statusCode());
        Assert.assertEquals("Этот логин уже используется. Попробуйте другой.", sameCourier.jsonPath().getString("message"));
        // в API ошибка - нет фразы "Попробуйте другой."
    }

    @Test
    @DisplayName("Create courier with required fields")
    public void checkCreateCourierWithRequiredFields() {
        Response courier = createCourier(login, password, null);
        Assert.assertEquals(201, courier.statusCode());
        Assert.assertEquals("true", courier.jsonPath().getString("ok"));
    }

    @Test
    @DisplayName("Return error if one of fields is missing")
    public void checkCreateCourierWithoutField() {
        String requestBody = "{ \"password\" : \"" + password + "\", \"firstName\":\"" + firstName + "\"}";

        Response response = given().header("Content-type", "application/json").body(requestBody).when().post("/api/v1/courier").then().log().all().extract().response();

        Assert.assertEquals(400, response.statusCode());
        Assert.assertEquals("Недостаточно данных для создания учетной записи", response.jsonPath().getString("message"));
    }

    @Test
    @DisplayName("Create courier with same login")
    public void checkCreateCourierWithSameLogin() {
        Response courier = createCourier(login, password, firstName);
        Response courierSameLogin = createCourier(login, password + "Z", firstName + "Z");
        Assert.assertEquals(409, courierSameLogin.statusCode());
        Assert.assertEquals("Этот логин уже используется. Попробуйте другой.", courierSameLogin.jsonPath().getString("message"));
    }

    @Step("Send POST request to /api/v1/courier")
    public Response createCourier(String login, String password, String firstName) {
        String requestFields = "{ \"login\" : \"" + login + "\", \"password\":\"" + password + "\", \"firstName\":\"" + firstName + "\"}";

        return given().header("Content-type", "application/json").body(requestFields).when().post("/api/v1/courier").then().extract().response();
    }

    @Step("Send POST request to /api/v1/courier/login")
    public Response loginCourier() {
        String loginRequest = "{ \"login\" : \"" + login + "\", \"password\":\"" + password + "\"}";

        return given().header("Content-type", "application/json").body(loginRequest).when().post("/api/v1/courier/login").then().extract().response();
    }

    @After
    @Step("Delete courier")
    public void deleteCourier() {
        given().header("Content-type", "application/json").when().delete("/api/v1/courier/" + loginCourier().jsonPath().getString("id")).then().extract().response();
    }

}