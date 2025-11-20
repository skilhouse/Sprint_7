package steps;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import static config.Config.*;

public class CourierSteps {

    public CourierSteps() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.filters(new AllureRestAssured());
    }

    @Step("Создать курьера с логином: {login}, паролем: {password} и именем: {firstName}")
    public Response createCourier(String login, String password, String firstName) {
        JSONObject requestBody = new JSONObject();
        if (login != null) requestBody.put("login", login);
        if (password != null) requestBody.put("password", password);
        if (firstName != null) requestBody.put("firstName", firstName);

        return RestAssured.given()
                .header("Content-type", "application/json")
                .body(requestBody.toString())
                .when()
                .post(COURIER_API);
    }

    @Step("Логин курьера с логином: {login} и паролем: {password}")
    public Response loginCourier(String login, String password) {
        JSONObject requestBody = new JSONObject();
        if (login != null) requestBody.put("login", login);
        if (password != null) requestBody.put("password", password);

        return RestAssured.given()
                .header("Content-type", "application/json")
                .body(requestBody.toString())
                .when()
                .post(COURIER_LOGIN_API);
    }

    @Step("Удалить курьера с ID: {courierId}")
    public void deleteCourier(String courierId) {
        if (courierId != null && !courierId.isEmpty()) {
            RestAssured.given()
                    .when()
                    .delete(COURIER_API + "/" + courierId);
        }
    }

    @Step("Получить ID курьера по логину: {login} и паролю: {password}")
    public String getCourierId(String login, String password) {
        Response loginResponse = loginCourier(login, password);
        if (loginResponse.statusCode() == 200) {
            return loginResponse.jsonPath().getString("id");
        }
        return null;
    }
}