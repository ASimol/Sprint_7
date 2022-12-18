import courier.ClientGenerator;
import courier.Courier;
import courier.LoginCourier;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import step.StepForCourier;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest {

    int id;


    @Test
    @DisplayName("Create courier and login")
    public void checkCourierLogin() {

        Courier request = ClientGenerator.checkCreateCourier();
        Response response = StepForCourier.createCourier(request);
        Response loginResponse = StepForCourier.loginCourier(new LoginCourier(request.getLogin(), request.getPassword()));

        loginResponse.then().log().all().assertThat().body("id", notNullValue())
                .statusCode(200);
    }

    @Test
    @DisplayName("Login courier without password")
    public void checkCourierLoginWithoutPassword() {
        Courier request = ClientGenerator.checkCreateCourier();

        Response loginResponse = StepForCourier.loginCourier(new LoginCourier(request.getLogin(), null));

        loginResponse.then().log().all().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .statusCode(400);
    }

    @Test
    @DisplayName("Courier login without required field")
    public void checkCourierLoginWithoutRequiredField() {
        Courier request = ClientGenerator.checkCreateCourier();
        Response loginResponse = StepForCourier.loginCourier(new LoginCourier(null, request.getPassword()));
        loginResponse.then().log().all().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .statusCode(400);
    }

    @Test
    @DisplayName("Login under another user")
    public void checkCourierLoginAnotherUser() {
        Response loginResponse = StepForCourier.loginCourier(new LoginCourier
                ("Courier111" + (int) (Math.random() * (10)),
                        "Password222" + (int) (Math.random() * (10))));

        loginResponse.then().log().all().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .statusCode(404);
    }
    @After
    @Step("Delete courier")
    public void delete() {
        StepForCourier.deleteCourier(id);
    }
    }