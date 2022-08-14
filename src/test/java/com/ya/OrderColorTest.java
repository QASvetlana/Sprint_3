package com.ya;

import io.restassured.response.Response;
import jdk.jfr.Description;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static com.ya.BaseApiClient.getReqSpec;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderColorTest {
    private final String firstName = RandomStringUtils.randomAlphabetic(7);
    private final String lastName = RandomStringUtils.randomAlphabetic(7);
    private final String address = RandomStringUtils.randomAlphabetic(7) + " Street, " + RandomStringUtils.randomNumeric(2) + " apt.";
    private final int metroStation = 5;
    private final String phone = "+7 900 " + RandomStringUtils.randomNumeric(7);
    private final int rentTime = 5;
    private final String deliveryDate = "2022-01-01";
    private final String comment = RandomStringUtils.randomAlphabetic(7);
    private final String colorData;

    public OrderColorTest(String color) {
        this.colorData = color;

    }

    @Parameterized.Parameters
    public static Object[][] getColorData() {
        return new Object[][]{
                {"GREY"},
                {"BLACK"},
                {"BLACK, GREY"},
                {""},
        };
    }

    @Test
    @Description("Создается заказ с параметрами")
    public void createOrderWithColorParam() {

        String actual = "{\"firstName\":\"" + firstName + "\","
                + "\"lastName\":\"" + lastName + "\","
                + "\"address\":\"" + address + "\","
                + "\"metroStation\":\"" + metroStation + "\","
                + "\"phone\":\"" + phone + "\","
                + "\"rentTime\":\"" + rentTime + "\","
                + "\"deliveryDate\":\"" + deliveryDate + "\","
                + "\"comment\":\"" + comment + "\","
                + "\"color\":[\"" + colorData + "\"]}";

        Response response = given()
                .spec(getReqSpec())
                .body(actual)
                .post("https://qa-scooter.praktikum-services.ru/api/v1/orders");

        response.then().assertThat().body("track", notNullValue())
                .and()
                .statusCode(SC_CREATED);

    }

}