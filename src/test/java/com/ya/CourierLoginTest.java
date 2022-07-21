package com.ya;

import com.ya.model.Courier;
import com.ya.model.CourierCredentials;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.Before;
import org.junit.Test;

import static com.ya.CourierClient.*;
import static com.ya.model.Courier.getRandomCourier;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CourierLoginTest {
    // чтобы хранить ид курьера
    int courierId;
    Courier courier;

    @Test
    @Description("Курьер может успешно залогиниться")
    public void courierCanLogInSuccessfully() {
        // делаем действие
        // сгенерированные объект передаем в апи и нам возвращается ответ
        Response responseCreate = createCourier(courier);
        // проверяем что в ответе статус код 201
        assertEquals(201, responseCreate.statusCode());
        // проверяем что в поле ,булеан ок
        assertTrue(responseCreate.body().jsonPath().getBoolean("ok"));
        // проверка дополнительная (пытаемся залогиниться созданным курьером)
        CourierCredentials courierCredentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
        Response responseLogin = login(courierCredentials);
        assertEquals(200, responseLogin.statusCode());
        // мы залогинились и нужно из респонс логина достать ид курьера
        courierId = responseLogin.body().jsonPath().getInt("id");
    }

    @Test
    @Description("Нельзя залониниться с несуществующим логином")
    public void canNotLogInIfLoginIsIncorrect() {
        Response responseCreate = createCourier(courier);
        assertEquals(201, responseCreate.statusCode());
        assertTrue(responseCreate.body().jsonPath().getBoolean("ok"));
        CourierCredentials courierCredentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
        Response responseLogin = login(courierCredentials);
        assertEquals(200, responseLogin.statusCode());
        // исказили логин
        CourierCredentials courierCredentialsWrongLogin = new CourierCredentials(courier.getLogin() + "Y", courier.getPassword());
        Response responseNoLogin = login(courierCredentialsWrongLogin);
        assertEquals(404, responseNoLogin.statusCode());
    }

    @Test
    @Description("Невозможно залогиниться без пароля")
    public void loginWithoutPasswordIsBadRequest() {
        Response responseCreate = createCourier(courier);
        assertEquals(201, responseCreate.statusCode());
        assertTrue(responseCreate.body().jsonPath().getBoolean("ok"));
        CourierCredentials courierCredentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
        Response responseLogin = login(courierCredentials);
        assertEquals(200, responseLogin.statusCode());
        // исказили логин
        CourierCredentials courierCredentialsWrongPassword = new CourierCredentials(courier.getLogin(), "");
        Response responseNoPassword = login(courierCredentialsWrongPassword);
        assertEquals(400, responseNoPassword.statusCode());
    }

    @Test
    @Description("Невозможно залогиниться с несуществующим курьером")
    public void loginWithNonexistentCourierIsNotFound() {
        Response responseCreate = createCourier(courier);
        // проверяем что в ответе статус код 201
        assertEquals(201, responseCreate.statusCode());
        // проверяем что в поле ,булеан ок
        assertTrue(responseCreate.body().jsonPath().getBoolean("ok"));
        // проверка дополнительная (пытаемся залогиниться созданным курьером)
        CourierCredentials courierCredentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
        Response responseLogin = login(courierCredentials);
        assertEquals(200, responseLogin.statusCode());
        // мы залогинились и нужно из респонс логина достать ид курьера
        courierId = responseLogin.body().jsonPath().getInt("id");
        // удаляем созданного пользователя
        deleteCourier(courierId);
        // логинимся под удаленным курьером
        Response responseDeletedLogin = login(courierCredentials);
        assertEquals(404, responseDeletedLogin.statusCode());
    }


    @Before
    public void ini() {
        // готовим данные
        // генерируем объект
        courier = getRandomCourier();


    }
}

