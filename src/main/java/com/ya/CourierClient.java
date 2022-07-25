package com.ya;

import com.ya.model.Courier;
import com.ya.model.CourierCredentials;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

// наш клиент наследуется от базового клиента
public class CourierClient extends BaseApiClient {

    public static Response createCourier(Courier courier) {
        return given()
                .spec(getReqSpec())
                .body(courier)
                .when()
                .post(BASE_URL + "/api/v1/courier");

    }

    public static Response login(CourierCredentials courierCredentials) {
        return given()
                .spec(getReqSpec())
                .body(courierCredentials)
                .when()
                .post(BASE_URL + "/api/v1/courier/login");
    }

    // передаем ид курьера
    public static Boolean deleteCourier(int courier) {
        return given()
                .spec(getReqSpec())
                .when()
                .delete(BASE_URL + "/api/v1/courier/" + courier)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("ok");

    }

    // получение списка заказа курьера
    public Response getOrdersList(int courierId) {
        return given()
                .spec(getReqSpec())
                .when()
                .get(BASE_URL + "/api/v1/orders?courierId=" + courierId);
    }

}

