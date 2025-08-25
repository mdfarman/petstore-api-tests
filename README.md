# Petstore API Automation Framework

## Overview

This project is an automated API testing framework for the [Swagger Petstore API](https://petstore.swagger.io/). It leverages **TestNG** for test orchestration, **Rest-Assured** for HTTP requests, **Allure** for reporting, and **Apache POI** for Excel-based test data management. The framework is designed for scalable, maintainable, and data-driven API testing.

---

## Features

- **Modular Structure:** Organized by API domain (User, Pet, Store) for clarity and scalability.
- **Data-Driven Testing:** Test cases and input data are managed via Excel files.
- **Reusable Utilities:** Common functions for API requests, response validation, and test data generation.
- **Reporting:** Integrated with Allure for rich, interactive test reports.
- **Configurable Execution:** TestNG suite allows selective and parallel test execution.

---

## Project Structure

```
src/
  main/
    java/
      com/
        petstore/
          base/           # Base test setup and configuration
          dto/
            request/      # Request payload models
            response/     # Response payload models
          utils/          # Utilities (Excel reader, API service, validators, data generator)
    resource/
  test/
    java/
      com/
        petstore/
          tests/          # Test classes for User, Pet, Store APIs
    resource/
      TestCases.xlsx      # Test data for data-driven tests
pom.xml                  # Maven dependencies and build configuration
TestNg.xml               # TestNG suite configuration
```

---

## Framework Components

### 1. **BaseTest**
- Handles common setup and teardown for API tests.
- Manages configuration and environment variables.

### 2. **DTOs (Data Transfer Objects)**
- **Request DTOs:** Define the structure for API request payloads.
- **Response DTOs:** Map API responses for validation.

### 3. **Utils**
- **ExcelReader:** Reads test data from Excel files using Apache POI.
- **RestAPIServiceRequests:** Encapsulates Rest-Assured HTTP methods.
- **ResponseAPIValidator:** Validates API responses against expected results.
- **TestDataGenerator:** Generates dynamic test data if needed.

### 4. **Test Classes**
- **UserTests, PetTests, StoreTests:** Contain TestNG test methods for respective API endpoints.
- Support for positive and negative scenarios.

### 5. **Test Data**
- Managed in `TestCases.xlsx` for easy updates and scalability.
- Supports multiple scenarios and parameterization.

---

## Sample Test Data

| TestCaseID | TestCaseName           | Execution | Description                    | Endpoint | Method | Expected Status | ScenarioType | id | username        | firstName   | lastName    | email                  | password      | phone      | userStatus |
|------------|------------------------|-----------|-------------------------------|----------|--------|-----------------|--------------|----|------------------|-------------|-------------|------------------------|---------------|------------|------------|
| TC-01      | TC_01_CreateANewUser   | Yes       | Create a new user with valid data | /user    | POST   | 200             | Positive     | 0  | AutoFarman      | Auto        | Farman      | autofarman@gmail.com   | autofarman    | 9712345678 | 1          |
| TC-01      | TC_01_CreateANewUser   | Yes       | Create a new user with valid data | /user    | POST   | 200             | Positive     | 0  | AutoFarman123   | Auto123     | Farman123   | autofarman123@gmail.com| autofarman123 | 9712345678 | 1          |

---

## How to Run

1. **Install Dependencies:**  
   Run `mvn clean install` to download all required libraries.

2. **Execute Tests:**  
   Run with TestNG suite:
   ```
   mvn test -DsuiteXmlFile=TestNg.xml
   ```

3. **View Reports:**  
   After execution, generate Allure report:
   ```
   allure serve target/allure-results
   ```

---

## Extending the Framework

- Add new test cases to `TestCases.xlsx`.
- Create new DTOs for additional endpoints.
- Implement new test classes in `src/test/java/com/petstore/tests/`.

---

## Contact

For questions or contributions, please open an issue or submit a pull request.