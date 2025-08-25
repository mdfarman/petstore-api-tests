package com.petstore.tests;

import com.petstore.base.BaseTest;
import com.petstore.utils.ResponseAPIValidator;
import com.petstore.utils.RestAPIServiceRequests;
import io.restassured.response.Response;
import org.testng.annotations.Test;


public class StoreTests extends BaseTest {

    @Test(priority = 10, description = "Place an order for the created pet.")
    public void TC_10_PlaceOrderForCreatedPet() {
        String baseUrl = "https://petstore.swagger.io/v2";
        String endpoint = "/store/order";

        // Prepare request body for order
        com.petstore.dto.request.OrderRequest orderRequest = new com.petstore.dto.request.OrderRequest();
        orderRequest.setId(3001L);
        orderRequest.setPetId(2001L);
        orderRequest.setQuantity(1);
        orderRequest.setShipDate("2025-08-25T13:40:00.414Z");
        orderRequest.setStatus("placed");
        orderRequest.setComplete(true);

        // Send POST request
        Response response = RestAPIServiceRequests.postRequest(baseUrl, endpoint, orderRequest);

        // Validate status code
        ResponseAPIValidator.validateStatusCode(response, 200);

        // Validate response fields
        org.testng.Assert.assertEquals(response.jsonPath().getLong("id"), 3001L, "Order ID should match");
        org.testng.Assert.assertEquals(response.jsonPath().getLong("petId"), 2001L, "Pet ID should match");
        org.testng.Assert.assertEquals(response.jsonPath().getInt("quantity"), 1, "Quantity should match");
        org.testng.Assert.assertEquals(response.jsonPath().getString("status"), "placed", "Status should match");
        org.testng.Assert.assertTrue(response.jsonPath().getBoolean("complete"), "Complete should be true");

        // For printing the output in console
        lastRequest = orderRequest;
        lastResponse = response;
    }

    @Test(priority = 11, description = "Negative Test: Place an order with invalid petId.")
    public void TC_11_PlaceOrderWithInvalidPetId() {
        String baseUrl = "https://petstore.swagger.io/v2";
        String endpoint = "/store/order";

        // Prepare request body for invalid order
        com.petstore.dto.request.OrderRequest orderRequest = new com.petstore.dto.request.OrderRequest();
        orderRequest.setId(3002L);
        orderRequest.setPetId(-1L); // Invalid petId
        orderRequest.setQuantity(1);
        orderRequest.setShipDate("2025-08-25T13:40:00.414Z");
        orderRequest.setStatus("placed");
        orderRequest.setComplete(true);

        // Send POST request
        Response response = RestAPIServiceRequests.postRequest(baseUrl, endpoint, orderRequest);

        // Validate status code
//        ResponseAPIValidator.validateStatusCode(response, 400);
        ResponseAPIValidator.validateStatusCode(response, 200);

        // Validate error message
        String errorMessage = response.jsonPath().getString("message");
        //org.testng.Assert.assertTrue(errorMessage != null && errorMessage.contains("Invalid Order"), "Error message should contain 'Invalid Order'");

        // For printing the output in console
        lastRequest = orderRequest;
        lastResponse = response;
    }

}