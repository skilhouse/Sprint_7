package tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import steps.OrderSteps;
import java.util.Arrays;
import java.util.Collection;
import static org.hamcrest.Matchers.*;

@DisplayName("Тесты на создание заказа")
@RunWith(Parameterized.class)
public class OrderCreationTest {

    private final String[] colors;
    private OrderSteps orderSteps;

    public OrderCreationTest(String[] colors) {
        this.colors = colors;
        this.orderSteps = new OrderSteps();
    }

    @Parameterized.Parameters(name = "Цвета: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{}}
        });
    }

    @Test
    @DisplayName("Создание заказа с разными цветами")
    @Description("Параметризованный тест создания заказа с различными комбинациями цветов: BLACK, GREY, оба цвета, без цвета")
    public void testCreateOrderWithDifferentColors() {
        Response response = orderSteps.createOrder(colors);

        response.then()
                .statusCode(201)
                .body("track", notNullValue());
    }
}