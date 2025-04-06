package com.example.task4;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class that finds the duplicate characters
 * of a given string.
 */
public class FindDuplicateChars {

    /**
     * Finds the duplicate characters within a given string
     * and then prints them on the console.
     * @param input the string used to extract the duplicate characters
     */
    public static void findDuplicateChars(String input) {
        Map<String, Integer> duplicatedChars = new LinkedHashMap<>();

        input.codePoints().forEach(cp -> {
            String ch = new String(Character.toChars(cp));
            duplicatedChars.put(ch, duplicatedChars.getOrDefault(ch, 0) + 1);
        });

        System.out.println("Duplicate chars:");
        duplicatedChars.forEach((ch, count) -> {
            if (count > 1) {
                System.out.println(ch + " - " + count + " times.");
            }
        });
    }
}