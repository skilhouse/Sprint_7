package tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.qameta.allure.Description;
import org.junit.Test;
import steps.OrderSteps;
import static org.hamcrest.Matchers.*;

@DisplayName("Тесты на получение списка заказов")
public class OrderListTest {

    private OrderSteps orderSteps;

    public OrderListTest() {
        this.orderSteps = new OrderSteps();
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверка что запрос на получение списка заказов возвращает непустой список")
    public void testGetOrderList() {
        Response response = orderSteps.getOrderList();

        response.then()
                .statusCode(200)
                .body("orders", notNullValue())
                .body("orders.size()", greaterThan(0));
    }
}