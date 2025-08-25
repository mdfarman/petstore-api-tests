package com.petstore.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class RestAPIServiceRequests {

    public static Response postRequest(String baseUrl, String endpoint, Object body) {
        return RestAssured.given()
                .baseUri(baseUrl)
                .contentType("application/json")
                .body(body)
                .post(endpoint)
                .then()
                .extract()
                .response();
    }

    public static Response getRequest(String baseUrl, String endpoint) {
        return RestAssured
                .given()
                .baseUri(baseUrl)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    public static Response deleteRequest(String baseUrl, String endpoint) {
        return RestAssured
                .given()
                .baseUri(baseUrl)
                .when()
                .delete(endpoint)
                .then()
                .extract()
                .response();
    }

    public static Response putRequest(String baseUrl, String endpoint, Object body) {
        return io.restassured.RestAssured
                .given()
                .baseUri(baseUrl)
                .contentType("application/json")
                .accept("application/json")
                .body(body)
                .when()
                .put(endpoint)
                .then()
                .extract()
                .response();
    }
}
