package com.example.task2;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class for analyzing word frequency in a given text.
 * The analysis ignores punctuation, capitalization, and sorts results
 * by frequency (descending), then alphabetically.
 */
public class WordFrequencyCounter {

    /**
     * com.example.Main method to process the input text and print word frequencies.
     * @param input the input text to analyze
     */
    public static void countWords(String input) {
        String[] words = getWords(input);
        HashMap<String, Integer> wordsFrequency = getWordsFrequency(words);
        Map<String, Integer> sortedWordsFrequency = getSortedWordsFrequency(wordsFrequency);

        printWordsFrequency(sortedWordsFrequency);
    }

    /**
     * Prints each word and its frequency from the provided map.
     * @param wordsFrequency a map of words and their frequency
     */
    private static void printWordsFrequency(Map<String, Integer> wordsFrequency) {
        System.out.println("Words Frequency Analysis: ");
        for (String word : wordsFrequency.keySet()) {
            System.out.println(word + " - " + wordsFrequency.get(word));
        }
    }

    /**
     * Cleans the input string from non-letter characters.
     * Trims, splits and lowers the provided string.
     * @param input the raw text
     * @return an array of cleaned, lowercase words
     */
    private static String[] getWords(String input) {
        // Removes all non-letter characters with space.
        input = input.replaceAll("[^'a-zA-Z\\s]+", "");

        // Splits the words by space as delimiter, ignores upper/lower case
        String[] words = input.trim().toLowerCase().split(" ");
        return words;
    }

    /**
     * Counts the frequency of each word and
     * saves it to a HashMap.
     * @param words array of words to be counted
     * @return a HashMap of words and their frequency
     */
    private static HashMap<String, Integer> getWordsFrequency(String[] words) {
        HashMap<String, Integer> wordsFrequency = new HashMap<String, Integer>();
        for (String word : words) {
            if (wordsFrequency.containsKey(word)) {
                wordsFrequency.put(word, wordsFrequency.get(word) + 1);
            } else {
                wordsFrequency.put(word, 1);
            }
        }

        return wordsFrequency;
    }

    /**
     * Sorts the word frequency map by count descending and
     * then by words alphabetically.
     * @param wordsFrequency unsorted words frequency map
     * @return sorted words frequency map
     */
    private static LinkedHashMap<String, Integer> getSortedWordsFrequency(HashMap<String, Integer> wordsFrequency) {
        LinkedHashMap<String, Integer> wordsFrequencySorted = new LinkedHashMap<String, Integer>();
        wordsFrequency.entrySet()
                .stream()
                .sorted((e1, e2) -> {
                    int compare = e2.getValue().compareTo(e1.getValue());
                    if (compare == 0) {
                        return e1.getKey().compareTo(e2.getKey());
                    }
                    return compare;
                })
                .forEachOrdered(entry -> wordsFrequencySorted.put(entry.getKey(), entry.getValue()));

        return wordsFrequencySorted;
    }
}