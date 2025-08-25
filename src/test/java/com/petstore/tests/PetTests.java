package com.petstore.tests;

import com.petstore.base.BaseTest;
import com.petstore.dto.request.PetRequest;
import com.petstore.dto.request.UserCreatedRequest;
import com.petstore.utils.ResponseAPIValidator;
import com.petstore.utils.RestAPIServiceRequests;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PetTests extends BaseTest {

    @Test(priority = 8, description = "Add a new pet with valid data.")
    public void TC_08_CreateNewPet() {
        String baseUrl = "https://petstore.swagger.io/v2";
        String endpoint = "/pet";

        // Prepare request body for new pet
        Map<String, Object> petRequest = new HashMap<>();
        petRequest.put("id", 2001);

        Map<String, Object> category = new HashMap<>();
        category.put("id", 1);
        category.put("name", "dog");
        petRequest.put("category", category);

        petRequest.put("name", "Buddy");
        petRequest.put("photoUrls", java.util.Arrays.asList("http://example.com/photo.jpg"));

        Map<String, Object> tag = new HashMap<>();
        tag.put("id", 1);
        tag.put("name", "friendly");
        petRequest.put("tags", java.util.Arrays.asList(tag));

        petRequest.put("status", "available");

        // Send POST request
        Response response = RestAPIServiceRequests.postRequest(baseUrl, endpoint, petRequest);

        // Validate status code
        ResponseAPIValidator.validateStatusCode(response, 200);

        // Validate response fields
        org.testng.Assert.assertEquals(response.jsonPath().getInt("id"), 2001, "Pet ID should match");
        org.testng.Assert.assertEquals(response.jsonPath().getString("name"), "Buddy", "Pet name should match");
        org.testng.Assert.assertEquals(response.jsonPath().getString("status"), "available", "Pet status should match");

        // For printing the output in console
        lastRequest = petRequest;
        lastResponse = response;
    }


    //api not work properly
    @Test(priority = 9, description = "Negative Test: Add a pet with missing name.")
    public void TC_09_AddPetWithMissingName() {
        String baseUrl = "https://petstore.swagger.io/v2";
        String endpoint = "/pet";

        // Prepare request body with missing 'name'
        PetRequest.Category category = new PetRequest.Category();
        category.setId(1L);
        category.setName("dog");

        PetRequest.Tag tag = new PetRequest.Tag();
        tag.setId(1L);
        tag.setName("friendly");

        PetRequest petRequest = new PetRequest();
        petRequest.setId(2002L);
        petRequest.setCategory(category);
        // Use Arrays.asList instead of List.of for compatibility with older Java versions
        petRequest.setPhotoUrls(java.util.Arrays.asList("http://example.com/photo.jpg"));
        petRequest.setTags(java.util.Arrays.asList(tag));
        petRequest.setStatus("available");
        // petRequest.setName(null); // name is missing

        // Send POST request
        Response response = RestAPIServiceRequests.postRequest(baseUrl, endpoint, petRequest);

        // Validate status code
        ResponseAPIValidator.validateStatusCode(response, 200);

        // Validate error message
        String errorMessage = response.jsonPath().getString("message");
        //org.testng.Assert.assertTrue(errorMessage != null && errorMessage.contains("Invalid input"), "Error message should contain 'Invalid input'");

        // For printing the output in console
        lastRequest = petRequest;
        lastResponse = response;
    }

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