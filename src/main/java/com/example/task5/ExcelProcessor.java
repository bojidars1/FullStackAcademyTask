package com.example.task5;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.example.task5.utils.AverageTracker;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Class for processing Excel files:
 * - Filtering rows based on a user condition
 * - Calculating an average of a numeric column
 * - Writing the result to a new Excel file
 */
public class ExcelProcessor {
    /**
     * Processes an Excel file by applying a filter to rows,
     * calculating the average of a numeric column, and writing the result to a new file.
     *
     * @param inputPath path to the input Excel file
     * @param outputPath path to the output Excel file
     * @param filter the condition used to filter rows
     * @param avgColumnIndex the column index to average
     */
    public static void processExcel(String inputPath, String outputPath, Predicate<Row> filter, int avgColumnIndex) {
        try (
                Workbook inputWorkbook = loadWorkbook(inputPath);
                Workbook outputWorkbook = new XSSFWorkbook()
        ) {
            Sheet inputSheet = inputWorkbook.getSheetAt(0);
            Sheet outputSheet = outputWorkbook.createSheet("Filtered");

            int rowCount = copyHeaderRow(inputSheet, outputSheet);

            AverageTracker avgTracker = filterAndCopyRows(inputSheet, outputSheet, rowCount, filter, avgColumnIndex);

            writeAverageRow(outputSheet, avgTracker, avgColumnIndex);

            saveWorkbook(outputWorkbook, outputPath);

            System.out.println("Excel processed successfully. Filtered rows: " + avgTracker.getCount());
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Gets the column headers from the first row of a sheet
     * and maps them to their column indexes.
     *
     * @param sheet the Excel sheet to read headers from
     * @return a map of column names to their index positions
     */
    public static Map<String, Integer> getHeadersMap(Sheet sheet) {
        Map<String, Integer> headersMap = new HashMap<>();
        Row header = sheet.getRow(0);
        if (header != null) {
            for (int i = 0; i < header.getLastCellNum(); i++) {
                Cell cell = header.getCell(i);
                if (cell != null) {
                    String colName = cell.getStringCellValue().trim().toLowerCase();
                    headersMap.put(colName, i);
                }
            }
        }
        return headersMap;
    }

    /**
     * Loads an Excel workbook from the given file path.
     *
     * @param path the file path to read
     * @return a Workbook representing the Excel file
     * @throws IOException if the file can't be read due to some reason
     */
    private static Workbook loadWorkbook(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        return new XSSFWorkbook(fis);
    }

    /**
     * Copies the header row from the input sheet to the output sheet.
     *
     * @param input the input Excel sheet
     * @param output the output Excel sheet
     * @return the next row index after the header
     */
    private static int copyHeaderRow(Sheet input, Sheet output) {
        Row header = input.getRow(0);
        if (header == null) {
            return 0;
        }

        Row outputHeader = output.createRow(0);
        for (int i = 0; i < header.getLastCellNum(); i++) {
            Cell cell = header.getCell(i);
            if (cell != null) {
                outputHeader.createCell(i).setCellValue(cell.toString());
            }
        }

        return 1;
    }

    /**
     * Filters rows based on a predicate and copies them to the output sheet.
     * Also tracks sum and count for averaging using AverageTracker class.
     *
     * @param input the input sheet
     * @param output the output sheet
     * @param startRow the starting row index in the output
     * @param filter the predicate to apply for filtering
     * @param avgColIndex the index of the column to average
     * @return an AverageTracker with updated values
     */
    private static AverageTracker filterAndCopyRows(Sheet input, Sheet output, int startRow,
                                                    Predicate<Row> filter, int avgColIndex) {
        double sum = 0;
        int count = 0;
        int outputRowIndex = startRow;

        for (Row row : input) {
            if (row.getRowNum() == 0) continue;

            if (filter.test(row)) {
                Row outputRow = output.createRow(outputRowIndex++);
                copyRow(row, outputRow);

                Cell avgCell = row.getCell(avgColIndex);
                if (avgCell != null && avgCell.getCellType() == CellType.NUMERIC) {
                    sum += avgCell.getNumericCellValue();
                    count++;
                }
            }
        }

        return new AverageTracker(sum, count, outputRowIndex);
    }

    /**
     * Copies all cells from one row to another.
     *
     * @param from the source row
     * @param to the target row
     */
    private static void copyRow(Row from, Row to) {
        for (int i = 0; i < from.getLastCellNum(); i++) {
            Cell cell = from.getCell(i);
            if (cell != null) {
                Cell newCell = to.createCell(i);
                CellType cellType = cell.getCellType();
                switch (cellType) {
                    case STRING:
                        newCell.setCellValue(cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        newCell.setCellValue(cell.getNumericCellValue());
                        break;
                    default:
                        newCell.setCellValue(cell.toString());
                        break;
                }
            }
        }
    }

    /**
     * Writes a row showing the average value below the filtered data.
     *
     * @param sheet the Excel sheet to write to
     * @param avgTracker the tracker holding sum and count
     * @param colIndex the index of the column to write the average in
     */
    private static void writeAverageRow(Sheet sheet, AverageTracker avgTracker, int colIndex) {
        Row avgRow = sheet.createRow(avgTracker.getNextRowIndex());
        avgRow.createCell(colIndex - 1).setCellValue("Average:");
        avgRow.createCell(colIndex).setCellValue(
                avgTracker.getCount() == 0 ? 0 : avgTracker.getSum() / avgTracker.getCount()
        );
    }

    /**
     * Saves the modified workbook to the specified file path.
     *
     * @param workbook the workbook to save
     * @param path the output file path
     * @throws IOException if the file can't be written
     */
    private static void saveWorkbook(Workbook workbook, String path) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(path)) {
            workbook.write(fos);
        }
    }
}