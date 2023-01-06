import courier.ClientGenerator;
import courier.Courier;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import step.StepForCourier;


public class CreatingCourierTest {
    int id;

    @Test
    @DisplayName("Create courier")
    public void checkCreateCourier() {
        Courier request = ClientGenerator.checkCreateCourier();
        Response response = StepForCourier.createCourier(request);
        Assert.assertEquals(201, response.statusCode());
        Assert.assertEquals("true", response.jsonPath().getString("ok"));
    }

    @Test
    @DisplayName("Create same couriers")
    public void checkCreateSameCouriers() {
        Courier request = ClientGenerator.checkCourierWithoutFirstName();
        Response response = StepForCourier.createCourier(request);
        Response sameCourier = StepForCourier.createCourier(request);
        Assert.assertEquals(409, sameCourier.statusCode());
        Assert.assertEquals("Этот логин уже используется. Попробуйте другой.", sameCourier.jsonPath().getString("message"));
        // в API ошибка - нет фразы "Попробуйте другой."
    }

    @Test
    @DisplayName("Return error if one of fields is missing")
    public void checkCreateCourierWithoutLogin() {
        Courier request = ClientGenerator.checkCourierWithoutLogin();
        Response response = StepForCourier.createCourier(request);
        Assert.assertEquals(400, response.statusCode());
        Assert.assertEquals("Недостаточно данных для создания учетной записи", response.jsonPath().getString("message"));
    }

    @Test
    @DisplayName("Create courier without password")
    public void checkCreateCourierWithoutPassword() {
        Courier request = ClientGenerator.checkCourierWithoutPassword();
        Response response = StepForCourier.createCourier(request);
        Assert.assertEquals(400, response.statusCode());
        Assert.assertEquals("Недостаточно данных для создания учетной записи", response.jsonPath().getString("message"));
    }

    @After
    @Step("Delete courier")
    public void delete() {
        StepForCourier.deleteCourier(id);
    }
}