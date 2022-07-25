package com.ya;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseApiClient {
    public static final String BASE_URL = "http://qa-scooter.praktikum-services.ru";

    public static RequestSpecification getReqSpec() {
        return new RequestSpecBuilder().log(LogDetail.ALL)
                .setContentType(ContentType.JSON).build();
    }
}
