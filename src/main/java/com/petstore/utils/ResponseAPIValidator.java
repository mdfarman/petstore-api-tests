package com.petstore.utils;

import io.restassured.response.Response;
import org.testng.Assert;

public class ResponseAPIValidator {

    public static void validateStatusCode(Response response, int expectedStatus) {
        Assert.assertEquals(response.getStatusCode(), expectedStatus, "Status code mismatch");
    }

    public static void validateResponseField(Response response, String field, Object expectedValue) {
        Assert.assertEquals(response.jsonPath().get(field), expectedValue, "Response field mismatch: " + field);
    }
}
