import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import org.example.Order;
import org.example.OrderGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import step.StepForOrder;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;

@AllArgsConstructor
@RunWith(Parameterized.class)
public class CreatingOrderTest {

    private List<String> colorScooter;


    @Parameterized.Parameters()
    public static Object[][] dataGen() {
        return new Object[][]{
                {List.of("GREY")},
                {List.of("BLACK")},
                {Arrays.asList("BLACK", "GREY")},
                {null}
        };
    }

    @Test
    @DisplayName("Create order")
    public void checkCreateOrder() {
        Order request = OrderGenerator.newOrder(colorScooter);
        Response response = StepForOrder.order(request);
        response.then().log().all().assertThat().body("track", notNullValue())
                .statusCode(201);

    }
}