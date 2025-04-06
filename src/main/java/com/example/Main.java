package com.example;

import com.example.task1.StringReverser;
import com.example.task2.WordFrequencyCounter;
import com.example.task3.ArrayListLoopTest;
import com.example.task4.FindDuplicateChars;
import com.example.task5.ExcelProcessor;
import com.example.task5.utils.FilterParser;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n====== FullStack Academy Tasks ======");
            System.out.println("1. Reverse String");
            System.out.println("2. Word Frequency Counter");
            System.out.println("3. ArrayList Traversal Performance Test");
            System.out.println("4. Find Duplicate Chars in String");
            System.out.println("5. Excel Processing");
            System.out.println("0. Exit");
            System.out.print("Choose a task: ");

            int choice;
            try {
                choice = scanner.nextInt();
            } catch(Exception e) {
                choice = -1;
            }
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Enter a string to reverse:");
                    String reversed = StringReverser.reverse(scanner.nextLine());
                    System.out.println("Reversed: " + reversed);
                    break;
                case 2:
                    System.out.println("Enter a text to count words:");
                    WordFrequencyCounter.countWords(scanner.nextLine());
                    break;
                case 3:
                    ArrayListLoopTest.compareArrListTraversalPerformance();
                    break;
                case 4:
                    System.out.println("Enter a string to find duplicate chars:");
                    FindDuplicateChars.findDuplicateChars(scanner.nextLine());
                    break;
                case 5:
                    try {
                        Workbook workbook = new XSSFWorkbook(new FileInputStream("data/input.xlsx"));
                        Sheet sheet = workbook.getSheetAt(0);
                        Map<String, Integer> headerMap = ExcelProcessor.getHeadersMap(sheet);
                        workbook.close();

                        System.out.println("Enter filter (e.g price > 100 or name startsWith A):");
                        String userFilter = scanner.nextLine();

                        Predicate<Row> filter = FilterParser.from(userFilter, headerMap);
                        String inputPath = "data/input.xlsx";
                        String outputPath = "data/output.xlsx";
                        int avgColumnKey = 2; // Hello Methodia, change according to your input file (MUST BE NUMERIC COLUMN)
                        ExcelProcessor.processExcel("data/input.xlsx", "data/output.xlsx", filter,
                                headerMap.getOrDefault("price", avgColumnKey));
                    } catch (IOException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case 0:
                    running = false;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Try again");
                    break;
            }
        }
        scanner.close();
    }
}