package steps;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import static config.Config.*;

public class OrderSteps {

    public OrderSteps() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.filters(new AllureRestAssured());
    }

    @Step("Создать заказ с цветами: {colors}")
    public Response createOrder(String[] colors) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("firstName", "Иван");
        requestBody.put("lastName", "Иванов");
        requestBody.put("address", "Москва, ул. Ленина, 1");
        requestBody.put("metroStation", "4");
        requestBody.put("phone", "+79991234567");
        requestBody.put("rentTime", 3);
        requestBody.put("deliveryDate", "2024-12-31");
        requestBody.put("comment", "Тестовый заказ");

        if (colors.length > 0) {
            requestBody.put("color", colors);
        }

        return RestAssured.given()
                .header("Content-type", "application/json")
                .body(requestBody.toString())
                .when()
                .post(ORDERS_API);
    }

    @Step("Получить список заказов")
    public Response getOrderList() {
        return RestAssured.given()
                .when()
                .get(ORDERS_API);
    }
}