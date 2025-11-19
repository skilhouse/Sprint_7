package tests;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.CourierSteps;
import static org.hamcrest.Matchers.*;

@DisplayName("Тесты на создание курьера")
public class CourierCreationTest {

    private CourierSteps courierSteps;
    private String courierId;
    private String login;
    private final String password = "password123";
    private final String firstName = "Test Courier";

    @Before
    @Step("Подготовка тестовых данных")
    public void setUp() {
        courierSteps = new CourierSteps();
        login = "testcourier" + System.currentTimeMillis();
    }

    @After
    @Step("Очистка тестовых данных")
    public void tearDown() {
        if (courierId != null) {
            courierSteps.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Успешное создание курьера")
    @Description("Проверка что курьер может быть успешно создан")
    public void testCreateCourierSuccess() {
        Response response = courierSteps.createCourier(login, password, firstName);

        response.then()
                .statusCode(201)
                .body("ok", equalTo(true));

        courierId = courierSteps.getCourierId(login, password);
    }

    @Test
    @DisplayName("Создание дубликата курьера")
    @Description("Проверка что нельзя создать двух одинаковых курьеров")
    public void testCreateDuplicateCourier() {
        courierSteps.createCourier(login, password, firstName);
        courierId = courierSteps.getCourierId(login, password);

        Response response = courierSteps.createCourier(login, password, firstName);

        response.then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Проверка ошибки при создании курьера без логина")
    public void testCreateCourierWithoutLogin() {
        Response response = courierSteps.createCourier("", password, firstName);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Проверка ошибки при создании курьера без пароля")
    public void testCreateCourierWithoutPassword() {
        Response response = courierSteps.createCourier(login, "", firstName);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без имени")
    @Description("Проверка что курьер может быть успешно создан без имени")
    public void testCreateCourierWithoutFirstName() {
        Response response = courierSteps.createCourier(login, password, "");

        response.then()
                .statusCode(201)
                .body("ok", equalTo(true));

        courierId = courierSteps.getCourierId(login, password);
    }

    @Test
    @DisplayName("Создание курьера с уже существующим логином")
    @Description("Проверка ошибки при создании курьера с существующим логином")
    public void testCreateCourierWithExistingLogin() {
        courierSteps.createCourier(login, password, firstName);
        courierId = courierSteps.getCourierId(login, password);

        String differentPassword = "differentpassword";
        String differentFirstName = "Different Courier";

        Response response = courierSteps.createCourier(login, differentPassword, differentFirstName);

        response.then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Проверка кода ответа при успешном создании курьера")
    @Description("Проверка 201 кода при успешном создании курьера")
    public void testCreateCourierResponseCode() {
        Response response = courierSteps.createCourier(login, password, firstName);

        response.then()
                .statusCode(201);

        courierId = courierSteps.getCourierId(login, password);
    }

    @Test
    @DisplayName("Проверка тела ответа при успешном создании курьера")
    @Description("Проверка ок true при успешном создании курьера")
    public void testCreateCourierResponseBody() {
        Response response = courierSteps.createCourier(login, password, firstName);

        response.then()
                .body("ok", equalTo(true));

        courierId = courierSteps.getCourierId(login, password);
    }

    @Test
    @DisplayName("Проверка кода ответа при отсутствии обязательных полей")
    @Description("Проверка ошибки при создании курьера без обязательных полей")
    public void testCreateCourierWithoutRequiredFieldsResponseCode() {
        Response response = courierSteps.createCourier("", "", firstName);

        response.then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Проверка тела ответа при отсутствии обязательных полей")
    @Description("Проверка тела ответа при отсутствии обязательных полей")
    public void testCreateCourierWithoutRequiredFieldsResponseBody() {
        Response response = courierSteps.createCourier("", "", firstName);

        response.then()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Проверка ошибки при создании курьера с существующим логином")
    @Description("Проверка ошибки при создании курьера с использемым логином")
    public void testCreateCourierWithDuplicateLoginError() {
        courierSteps.createCourier(login, password, firstName);
        courierId = courierSteps.getCourierId(login, password);

        Response response = courierSteps.createCourier(login, "newpassword", "New Courier");

        response.then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }
}