import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import step.StepForOrder;

import static org.hamcrest.Matchers.notNullValue;

public class GetOrderListTest {
    @Test
    @DisplayName("Get list of orders")
    public void checkGetOrderList() {
        Response OrdersList = StepForOrder.checkGetOrderList();
        OrdersList.then().log().all()
                .statusCode(200)
                .and()
                .body("orders", notNullValue());
    }
}
