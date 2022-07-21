package com.ya;

import com.ya.model.Courier;
import com.ya.model.CourierCredentials;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.ya.CourierClient.*;
import static com.ya.model.Courier.getRandomCourier;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CourierTest {
    // чтобы хранить ид курьера
    int courierId;
    Courier courier;

    @Test
    @Description("Успешное создание заказа с рандомным курьером")
    public void createRandomCourierSuccessful() {
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
    @Description("Создание двух одинаковых курьеров не возможно")
    public void createDuplicateCourierImpossible() {
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
        // повторная регистрация того же пользователя - отправили сгенерированные данные на ручку АПИ
        responseCreate = createCourier(courier);
        // сравнили статус-код
        assertEquals(409, responseCreate.statusCode());

    }

    @Test
    @Description("Невозможно создание курьера без логина")
    public void createCourierWithoutLoginImpossible() {
        Courier courier = Courier.getRandomNoLogin();
        Response responseCreate = createCourier(courier);
        // сравнили статус-код
        assertEquals(400, createCourier(courier).statusCode());

    }

    @Test
    @Description("Невозможно создание курьера без пароля")
    public void createCourierWithoutPasswordImpossible() {
        Courier courier = Courier.getRandomNoPassword();
        Response responseCreate = createCourier(courier);
        // сравнили статус-код
        assertEquals(400, createCourier(courier).statusCode());

    }

    @After
    public void clear() {
        // чистка
        // вызываем метод удаления курьера в тесте
        if (courierId != 0) {
            deleteCourier(courierId);
        }
    }

    @Before
    public void ini() {
        // готовим данные
        // генерируем объект
        courier = getRandomCourier();


    }
}
