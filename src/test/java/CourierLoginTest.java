import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest {

    private String login;
    private String password;
    private String firstName;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        login = "Courier111" + (int) (Math.random() * (1000));
        password = "Password222" + (int) (Math.random() * (1000));
        firstName = "LoginName333" + (int) (Math.random() * (1000));

        createCourier(login, password, firstName);
    }

    @Test
    @DisplayName("Create courier and login")
    public void checkCourierLogin() {
        Response loginResponse = loginCourier(login, password);

        loginResponse.then().log().all().assertThat().body("id", notNullValue()).statusCode(200);
    }

    @Test
    @DisplayName("Login courier without password")
    public void checkCourierLoginWithoutPassword() {
        Response loginResponse = loginCourier(login, null);

        loginResponse.then().log().all().assertThat().body("message", equalTo("Учетная запись не найдена")).statusCode(404);
    }

    @Test
    @DisplayName("Login courier with wrong password")
    public void checkCourierLoginWithWrongPassword() {
        Response loginResponse = loginCourier(login, password + "Z");

        loginResponse.then().assertThat().body("message", equalTo("Учетная запись не найдена")).statusCode(404);
    }

    @Test
    @DisplayName("Courier login without required field")
    public void checkCourierLoginWithoutRequiredField() {
        String loginRequest = "{\"password\":\"null\"}";
        Response loginResponse = given().header("Content-type", "application/json").body(loginRequest).when().post("/api/v1/courier/login").then().log().all().extract().response();
        loginResponse.then().assertThat().body("message", equalTo("Недостаточно данных для входа")).statusCode(400);
    }

    @Test
    @DisplayName("Login under another user")
    public void checkCourierLoginAnotherUser() {
        Response loginResponse = loginCourier(login + "Z", password);

        loginResponse.then().log().all().assertThat().body("message", equalTo("Учетная запись не найдена")).statusCode(404);
    }

    @Step("Send POST request to /api/v1/courier")
    public Response createCourier(String login, String password, String firstName) {
        String requestFields = "{ \"login\" : \"" + login + "\", \"password\":\"" + password + "\", \"firstName\":\"" + firstName + "\"}";

        return given().header("Content-type", "application/json").body(requestFields).when().post("/api/v1/courier").then().extract().response();
    }

    @Step("Send POST request to /api/v1/courier/login")
    public Response loginCourier(String login, String password) {
        String loginRequest = "{ \"login\" : \"" + login + "\", \"password\":\"" + password + "\"}";

        return given().header("Content-type", "application/json").body(loginRequest).when().post("/api/v1/courier/login").then().extract().response();
    }

    @After
    @Step("Delete courier")
    public void deleteCourier() {
        given().header("Content-type", "application/json").when().delete("/api/v1/courier/" + loginCourier(login, password).jsonPath().getString("id")).then().extract().response();
    }
}