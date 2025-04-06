package com.example.task1;

/**
 * Class that reverses strings that may contain
 * Unicode characters, emojis, or special symbols.
 */
public class StringReverser {

    /**
     * Reverses a given string while preserving proper handling of Unicode characters.
     *
     * @param input the string to be reversed
     * @return the reversed version of the input string
     */
    public static String reverse(String input) {
        int[] unicodeInput = input.codePoints().toArray();

        String output = "";
        for (int i = unicodeInput.length - 1; i >= 0; i--) {
            String symbol = Character.toString(unicodeInput[i]);
            output += symbol;
        }

        return output;
    }
}