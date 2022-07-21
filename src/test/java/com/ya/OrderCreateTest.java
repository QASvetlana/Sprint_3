package com.ya;

import com.ya.model.Courier;
import com.ya.model.CourierCredentials;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static com.ya.CourierClient.createCourier;
import static com.ya.CourierClient.login;
import static com.ya.model.Courier.getRandomCourier;
import static org.junit.Assert.assertEquals;


public class OrderCreateTest {
    int courierId;
    Courier courier;
    CourierCredentials courierCredentials;
    CourierClient courierClient = new CourierClient();

    @Test
    @Description("Проверка списка заказов")
    public void createCourierWithoutLogin() {
        // создали курьера
        Response responseCreate = createCourier(courier);
        assertEquals(201, responseCreate.statusCode());
        // залогинились курьером
        courierCredentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
        Response responseLogin = login(courierCredentials);
        assertEquals(200, responseLogin.statusCode());
        // мы залогинились и нужно из респонс логина достать ид курьера
        courierId = responseLogin.body().jsonPath().getInt("id");
        // получили список заказов курьера
        Response responseOrdersList = courierClient.getOrdersList(courierId);
        assertEquals(200, responseOrdersList.statusCode());
        // удалили курьера
        CourierClient.deleteCourier(courierId);

    }

    @Before
    public void ini() {
        // готовим данные
        // генерируем объект
        courier = getRandomCourier();


    }
}