package com.petstore.base;

import com.petstore.utils.ExcelReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.restassured.response.Response;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;

public class BaseTest {

    protected String excelPath = "src/test/resource/TestCases.xlsx";
    protected Object lastRequest;
    protected Response lastResponse;
    protected Iterator<Object[]> getTestDataForCurrentMethod(String methodName, String scenarioType) {
    List<Map<String, String>> allData = ExcelReader.getAllTestCaseData(excelPath, scenarioType);
    List<Object[]> filtered = new java.util.ArrayList<>();

    for (Map<String, String> data : allData) {
        String execution = data.get("Execution");
        String testCaseName = data.get("TestCaseName");
        if (execution != null && testCaseName != null &&
            "Yes".equalsIgnoreCase(execution) &&
            methodName.replace("_", "").equalsIgnoreCase(testCaseName.replace("_", ""))) {
            filtered.add(new Object[]{data});
        }
    }
    System.out.println("DataProvider for method: " + methodName + ", found rows: " + filtered.size());
    return filtered.iterator();
    }

    protected String getCurrentTestMethodName() {
        // Find the first method in the stack trace that is annotated with @Test
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stack) {
            if (element.getClassName().contains("tests.") && !element.getMethodName().equals("getCurrentTestMethodName")) {
                return element.getMethodName();
            }
        }
        return "";
    }

      @AfterMethod
    public void printRequestAndResponse(ITestResult result) {
        System.out.println("----- Test: " + result.getMethod().getMethodName() + " -----");
        if (lastRequest != null) {
            System.out.println("Request: " + lastRequest);
        } else {
            System.out.println("Request: [null]");
        }
        if (lastResponse != null) {
            System.out.println("Response Status: " + lastResponse.getStatusCode());
            System.out.println("Response Body: " + lastResponse.asString());
        } else {
            System.out.println("Response: [null]");
        }
        System.out.println("---------------------------------------------");
        // Reset for next test
        lastRequest = null;
        lastResponse = null;
    }
}