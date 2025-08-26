package com.petstore.tests;

import com.petstore.base.BaseTest;
import com.petstore.dto.request.UserCreatedRequest;
import com.petstore.utils.RestAPIServiceRequests;
import com.petstore.utils.ResponseAPIValidator;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Iterator;
import java.util.Map;

public class UserTests extends BaseTest {

    @DataProvider(name = "userTestData")
    public Iterator<Object[]> userTestData(java.lang.reflect.Method method) {
        // Pass the actual test method name to BaseTest
        return getTestDataForCurrentMethod(method.getName(), "Positive");
    }



    @Test(priority = 1, dataProvider = "userTestData", description = "Validate the ability to create a new user using a valid user object.\n")
    public void TC_01_CreateANewUser(Map<String, String> tData) {
        String baseUrl = "https://petstore.swagger.io/v2";
        String endpoint = tData.get("Endpoint");

        UserCreatedRequest request = new UserCreatedRequest();
        request.setId(Integer.parseInt(tData.getOrDefault("id", "0")));
        request.setUsername(tData.getOrDefault("username", "AutoFarman"));
        request.setFirstName(tData.getOrDefault("firstName", "Auto"));
        request.setLastName(tData.getOrDefault("lastName", "Farman"));
        request.setEmail(tData.getOrDefault("email", "autofarman@gmail.com"));
        request.setPassword(tData.getOrDefault("password", "autofarman"));
        request.setPhone(tData.getOrDefault("phone", "9712345678"));
        request.setUserStatus(Integer.parseInt(tData.getOrDefault("userStatus", "1")));

        Response response = RestAPIServiceRequests.postRequest(baseUrl, endpoint, request);

        ResponseAPIValidator.validateStatusCode(response, Integer.parseInt(tData.get("Expected Status")));
        ResponseAPIValidator.validateResponseField(response, "code", Integer.parseInt(tData.get("Expected Status")));
        ResponseAPIValidator.validateResponseField(response, "type", "unknown");
        String message = response.jsonPath().getString("message");
        org.testng.Assert.assertNotNull(message, "Message should not be null");
        org.testng.Assert.assertFalse(message.isEmpty(), "Message should not be empty");
        //For printing the output in console
        lastRequest = request;
        lastResponse = response;
    }


    @Test(priority = 2, dataProvider = "userTestData", description = "Verify login functionality using valid username and password; check for session/token in the response.")
    public void TC_02_UserLoginWithValidCredentials(Map<String, String> tData) {
        String baseUrl = "https://petstore.swagger.io/v2";
        String endpoint = tData.get("Endpoint");

        // Prepare query parameters from test data or use defaults
        String username = tData.get("username");
        String password = tData.get("password");

        // Build the full endpoint with query params for login
        String loginEndpoint = endpoint + "/login?username=" + username + "&password=" + password;

        // Send GET request
        Response response = RestAPIServiceRequests.getRequest(baseUrl, loginEndpoint);

        // Validate status code
        ResponseAPIValidator.validateStatusCode(response, Integer.parseInt(tData.get("Expected Status")));

        // Validate response fields
        ResponseAPIValidator.validateResponseField(response, "code", Integer.parseInt(tData.get("Expected Status")));
        ResponseAPIValidator.validateResponseField(response, "type", "unknown");

        // Validate that a session/token is present in the message
        String message = response.jsonPath().getString("message");
        org.testng.Assert.assertNotNull(message, "Login response should contain a message (token/session info)");
        org.testng.Assert.assertTrue(message.startsWith("logged in user session:"), "Message should start with 'logged in user session:'");

        // For printing the output in console
        lastRequest = "GET " + loginEndpoint;
        lastResponse = response;
    }

    //what ever the username and password this api always run and give the status code 200
    @Test(priority = 3, dataProvider = "userTestData", description = "Attempt to log in using incorrect username or password and validate error response.")
    public void TC_03_UserLoginWithInvalidCredentials(Map<String, String> tData) {
        String baseUrl = "https://petstore.swagger.io/v2";
        String endpoint = tData.get("Endpoint");

        // Use test data or defaults for invalid credentials
        String username = tData.get("username");
        String password = tData.get("password");

        // Build the login endpoint with query params
        String loginEndpoint = endpoint + "/login?username=" + username + "&password=" + password;

        // Send GET request
        Response response = RestAPIServiceRequests.getRequest(baseUrl, loginEndpoint);

        // Validate status code
        ResponseAPIValidator.validateStatusCode(response, Integer.parseInt(tData.get("Expected Status")));

        // Validate response fields
        ResponseAPIValidator.validateResponseField(response, "code", Integer.parseInt(tData.get("Expected Status")));
        ResponseAPIValidator.validateResponseField(response, "type", "unknown");

        // Validate message format (should still return a session message even for invalid credentials)
        String message = response.jsonPath().getString("message");
        org.testng.Assert.assertNotNull(message, "Login response should contain a message");
        org.testng.Assert.assertTrue(message.startsWith("logged in user session:"), "Message should start with 'logged in user session:'");

        // For printing the output in console
        lastRequest = "GET " + loginEndpoint;
        lastResponse = response;
    }



    @Test(priority = 4, dataProvider = "userTestData", description = "Validate retrieval of user details using a valid username.")
    public void TC_04_FetchUserByUsername(Map<String, String> tData) throws InterruptedException {
        String baseUrl = "https://petstore.swagger.io/v2";
        String endpoint = tData.get("Endpoint");
        String username = tData.get("username");
        Thread.sleep(2000);
        // Build the endpoint for fetching user by username
        String fetchUserEndpoint = endpoint + "/" + username;

        // Send GET request
        Response response = RestAPIServiceRequests.getRequest(baseUrl, fetchUserEndpoint);

        // Validate status code
        ResponseAPIValidator.validateStatusCode(response, Integer.parseInt(tData.get("Expected Status")));

        // Validate response fields
        org.testng.Assert.assertEquals(response.jsonPath().getString("username"), username, "Username should match");
        org.testng.Assert.assertNotNull(response.jsonPath().getString("id"), "User ID should not be null");
        org.testng.Assert.assertEquals(response.jsonPath().getString("firstName"), tData.getOrDefault("firstName", "Auto"), "First name should match");
        org.testng.Assert.assertEquals(response.jsonPath().getString("lastName"), tData.getOrDefault("lastName", "Farman"), "Last name should match");
        org.testng.Assert.assertEquals(response.jsonPath().getString("email"), tData.getOrDefault("email", "autofarman@gmail.com"), "Email should match");
        org.testng.Assert.assertEquals(response.jsonPath().getString("phone"), tData.getOrDefault("phone", "9712345678"), "Phone should match");
        org.testng.Assert.assertEquals(response.jsonPath().getInt("userStatus"), Integer.parseInt(tData.getOrDefault("userStatus", "1")), "User status should match");

        // For printing the output in console
        lastRequest = "GET " + fetchUserEndpoint;
        lastResponse = response;
    }



    @Test(priority = 5, dataProvider = "userTestData", description = "Test updating user information (e.g., name, email) for an existing user.")
    public void TC_05_UpdateUserProfile(Map<String, String> tData) {
        String baseUrl = "https://petstore.swagger.io/v2";
        String endpoint = tData.get("Endpoint");
        String username = tData.getOrDefault("username", "AutoFarman");

        // Build the endpoint for updating user by username
        String updateUserEndpoint = endpoint + "/" + username;

        // Prepare updated user object from test data
        UserCreatedRequest request = new UserCreatedRequest();
        request.setId(Integer.parseInt(tData.getOrDefault("id", "1995627276")));
        request.setUsername(username);
        request.setFirstName(tData.getOrDefault("firstName", "Auto"));
        request.setLastName(tData.getOrDefault("lastName", "Farman"));
        request.setEmail(tData.getOrDefault("email", "autofarmanUpdate123@gmail.com"));
        request.setPassword(tData.getOrDefault("password", "autofarman"));
        request.setPhone(tData.getOrDefault("phone", "9712345678"));
        request.setUserStatus(Integer.parseInt(tData.getOrDefault("userStatus", "1")));

        // Send PUT request
        Response response = RestAPIServiceRequests.putRequest(baseUrl, updateUserEndpoint, request);

        // Validate status code
        ResponseAPIValidator.validateStatusCode(response, Integer.parseInt(tData.get("Expected Status")));

        // Validate response fields
        ResponseAPIValidator.validateResponseField(response, "code", Integer.parseInt(tData.get("Expected Status")));
        ResponseAPIValidator.validateResponseField(response, "type", "unknown");
        ResponseAPIValidator.validateResponseField(response, "message", tData.getOrDefault("id", "1995627276"));

        // For printing the output in console
        lastRequest = request;
        lastResponse = response;
    }

    @Test(priority = 6, dataProvider = "userTestData", description = "Test deleting a user by username.")
    public void TC_06_DeleteUser(Map<String, String> tData) {
        String baseUrl = "https://petstore.swagger.io/v2";
        String endpoint = tData.get("Endpoint"); // Should be "/user"
        String username = tData.getOrDefault("username", "AutoFarman");

        // Build the endpoint for deleting user by username
        String deleteUserEndpoint = endpoint + "/" + username;

        // Send DELETE request
        Response response = RestAPIServiceRequests.deleteRequest(baseUrl, deleteUserEndpoint);

        // Validate status code
        ResponseAPIValidator.validateStatusCode(response, Integer.parseInt(tData.get("Expected Status")));

        // Validate response fields
        ResponseAPIValidator.validateResponseField(response, "code", Integer.parseInt(tData.get("Expected Status")));
        ResponseAPIValidator.validateResponseField(response, "type", "unknown");
        ResponseAPIValidator.validateResponseField(response, "message", username);

        // For printing the output in console
        lastRequest = "DELETE " + deleteUserEndpoint;
        lastResponse = response;
    }

    @Test(priority = 7, dataProvider = "userTestData", description = "Verify user logout functionality and ensure session is terminated or token is invalidated.")
    public void TC_07_LogoutUser(Map<String, String> tData) {
        String baseUrl = "https://petstore.swagger.io/v2";
        String endpoint = tData.get("Endpoint"); // Should be "/user"

        // Build the endpoint for logout
        String logoutEndpoint = endpoint + "/logout";

        // Send GET request
        Response response = RestAPIServiceRequests.getRequest(baseUrl, logoutEndpoint);

        // Validate status code
        ResponseAPIValidator.validateStatusCode(response, Integer.parseInt(tData.get("Expected Status")));

        // Validate response fields
        ResponseAPIValidator.validateResponseField(response, "code", Integer.parseInt(tData.get("Expected Status")));
        ResponseAPIValidator.validateResponseField(response, "type", "unknown");
        ResponseAPIValidator.validateResponseField(response, "message", "ok");

        // For printing the output in console
        lastRequest = "GET " + logoutEndpoint;
        lastResponse = response;
    }

}