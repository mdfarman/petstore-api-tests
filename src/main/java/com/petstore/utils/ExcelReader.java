package com.petstore.utils;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.*;

public class ExcelReader {

    public static List<Map<String, String>> getAllTestCaseData(String filePath, String scenarioType) {
        List<Map<String, String>> testCases = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            int rowCount = sheet.getPhysicalNumberOfRows();
            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                Map<String, String> rowData = getRowData(row, getHeaders(headerRow));
                // Filter by ScenarioType if provided
                if (scenarioType == null || scenarioType.isEmpty() ||
                    rowData.get("ScenarioType").equalsIgnoreCase(scenarioType)) {
                    testCases.add(rowData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return testCases;
    }

    public static Map<String, String> getRowData(Row row, List<String> headers) {
        Map<String, String> rowData = new HashMap<>();
        for (int i = 0; i < headers.size(); i++) {
            Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            String value = "";
            switch (cell.getCellType()) {
                case STRING:
                    value = cell.getStringCellValue().trim();
                    break;
                case NUMERIC:
                    value = String.valueOf((long) cell.getNumericCellValue());
                    break;
                case BOOLEAN:
                    value = String.valueOf(cell.getBooleanCellValue());
                    break;
                case BLANK:
                default:
                    value = "";
            }
            // Provide default values for critical fields if empty
            if (headers.get(i).equalsIgnoreCase("id") && value.isEmpty()) {
                value = "0";
            }
            if (headers.get(i).equalsIgnoreCase("userStatus") && value.isEmpty()) {
                value = "1";
            }
            rowData.put(headers.get(i), value);
        }
        return rowData;
    }

    private static List<String> getHeaders(Row headerRow) {
        List<String> headers = new ArrayList<>();
        for (Cell cell : headerRow) {
            headers.add(cell.getStringCellValue());
        }
        return headers;
    }
}
