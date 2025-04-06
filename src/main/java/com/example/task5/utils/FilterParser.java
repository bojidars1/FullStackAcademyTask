package com.example.task5.utils;

import org.apache.poi.ss.usermodel.*;

import java.util.Map;
import java.util.function.Predicate;

/**
 * A utility class for parsing user-defined filter expressions
 * (e.g., "price > 100", "name startsWith A") into executable predicates
 */
public class FilterParser {

    /**
     * Parses a user-provided filter expression and returns a corresponding predicate.
     * Supports numeric comparisons and string-based startsWith expressions.
     *
     * @param input the filter string (e.g., "price > 100")
     * @param headerMap a map of column names to their index positions
     * @return a Predicate<Row> representing the filter condition
     */
    public static Predicate<Row> from(String input, Map<String, Integer> headerMap) {
        input = input.trim().toLowerCase();

        if (input.contains("startswith")) {
            return handleStartsWith(input, headerMap);
        }

        return handleNumericComparison(input, headerMap);
    }

    /**
     * Handles filters that use 'startsWith' on string columns.
     * Example: "name startsWith a"
     *
     * @param input the full filter expression
     * @param headerMap a map of column headers to indexes
     * @return a predicate to test the 'startsWith' condition
     */
    private static Predicate<Row> handleStartsWith(String input, Map<String, Integer> headerMap) {
        String[] parts = input.split("startswith");
        if (parts.length != 2) return row -> false;

        String column = parts[0].trim();
        String prefix = parts[1].trim();
        int colIndex = headerMap.getOrDefault(column, -1);

        return row -> {
            if (colIndex == -1) return false;
            Cell cell = row.getCell(colIndex);
            return cell != null &&
                    cell.getCellType() == CellType.STRING &&
                    cell.getStringCellValue().toLowerCase().startsWith(prefix);
        };
    }

    /**
     * Handles filters involving numeric comparisons (>, <, >=, <=, =, !=).
     * Parses column, operator, and value and builds an appropriate predicate.
     *
     * @param input the full filter expression
     * @param headerMap a map of column headers to indexes
     * @return a predicate to test numeric conditions
     */
    private static Predicate<Row> handleNumericComparison(String input, Map<String, Integer> headerMap) {
        String[] operators = {">=", "<=", "!=", ">", "<", "="};

        for (String op : operators) {
            if (input.contains(op)) {
                String[] parts = input.split(op);
                if (parts.length != 2) break;

                String col = parts[0].trim();
                String valueStr = parts[1].trim();
                int colIndex = headerMap.getOrDefault(col, -1);

                if (colIndex == -1) {
                    System.out.println("Column not found: " + col);
                    return row -> false;
                }

                try {
                    double value = Double.parseDouble(valueStr);
                    switch (op) {
                        case ">":
                            return row -> compareValues(row, colIndex, value, Comparison.GREATER);
                        case "<":
                            return row -> compareValues(row, colIndex, value, Comparison.LESS);
                        case "=":
                            return row -> compareValues(row, colIndex, value, Comparison.EQUAL);
                        case ">=":
                            return row -> compareValues(row, colIndex, value, Comparison.GREATER_EQUAL);
                        case "<=":
                            return row -> compareValues(row, colIndex, value, Comparison.LESS_EQUAL);
                        case "!=":
                            return row -> compareValues(row, colIndex, value, Comparison.NOT_EQUAL);
                        default:
                            return row -> false;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format: " + valueStr);
                    return row -> false;
                }
            }
        }

        System.out.println("Unsupported or invalid filter. Returning 'always true' as a fallback.");
        return row -> true;
    }

    /**
     * Compares a numeric cell value to a given constant based on the provided comparison type.
     *
     * @param row the Excel row being evaluated
     * @param colIndex the index of the target column
     * @param value the value to compare against
     * @param comparison the type of comparison to apply
     * @return true if the cell matches the condition, false otherwise
     */
    private static boolean compareValues(Row row, int colIndex, double value, Comparison comparison) {
        Cell cell = row.getCell(colIndex);
        if (cell == null || cell.getCellType() != CellType.NUMERIC) {
            return false;
        }

        double cellValue = cell.getNumericCellValue();

        switch (comparison) {
            case GREATER:
                return cellValue > value;
            case LESS:
                return cellValue < value;
            case EQUAL:
                return cellValue == value;
            case GREATER_EQUAL:
                return cellValue >= value;
            case LESS_EQUAL:
                return cellValue <= value;
            case NOT_EQUAL:
                return cellValue != value;
            default:
                return false;
        }
    }
}