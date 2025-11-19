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

@DisplayName("Тесты на логин курьера")
public class CourierLoginTest {

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
        courierSteps.createCourier(login, password, firstName);
        courierId = courierSteps.getCourierId(login, password);
    }

    @After
    @Step("Очистка тестовых данных")
    public void tearDown() {
        if (courierId != null) {
            courierSteps.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Курьер может авторизоваться")
    @Description("Проверка успешной авторизации курьера с валидными данными")
    public void testCourierCanLogin() {
        Response response = courierSteps.loginCourier(login, password);

        response.then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Для авторизации нужно передать все обязательные поля - логин")
    @Description("Проверка ошибки при отсутствии логина при авторизации")
    public void testLoginRequiresLoginField() {
        Response response = courierSteps.loginCourier("", password);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Для авторизации нужно передать все обязательные поля - пароль")
    @Description("Проверка ошибки при отсутствии пароля при авторизации")
    public void testLoginRequiresPasswordField() {
        Response response = courierSteps.loginCourier(login, "");

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Система вернет ошибку при неправильном логине")
    @Description("Проверка ошибки при авторизации с неверным логином")
    public void testLoginWithIncorrectLogin() {
        Response response = courierSteps.loginCourier("incorrect" + login, password);

        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Система вернет ошибку при неправильном пароле")
    @Description("Проверка ошибки при авторизации с неверным паролем")
    public void testLoginWithIncorrectPassword() {
        Response response = courierSteps.loginCourier(login, "incorrect" + password);

        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Запрос возвращает ошибку при отсутствии логина")
    @Description("Проверка ошибки при авторизации под несуществующим пользователем")
    public void testLoginWithoutLoginReturnsError() {
        Response response = courierSteps.loginCourier("", password);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Запрос возвращает ошибку при отсутствии пароля")
    @Description("Проверка ошибки при авторизации без пароля")
    public void testLoginWithoutPasswordReturnsError() {
        Response response = courierSteps.loginCourier(login, "");

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Успешный запрос возвращает id")
    @Description("Проверка что успешная авторизация возвращает идентификатор курьера")
    public void testSuccessfulLoginReturnsId() {
        Response response = courierSteps.loginCourier(login, password);

        response.then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("id", not(equalTo("")));
    }

    @Test
    @DisplayName("Проверка формата ID при успешной авторизации")
    @Description("Проверка что ID курьера имеет праильный формат")
    public void testLoginReturnsValidIdFormat() {
        Response response = courierSteps.loginCourier(login, password);

        response.then()
                .statusCode(200)
                .body("id", greaterThan(0));
    }

    @Test
    @DisplayName("Авторизация с пустыми обязательными полями")
    @Description("Проверка ошибки при авторизации с пустыми логином и паролем")
    public void testLoginWithEmptyRequiredFields() {
        Response response = courierSteps.loginCourier("", "");

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }
}