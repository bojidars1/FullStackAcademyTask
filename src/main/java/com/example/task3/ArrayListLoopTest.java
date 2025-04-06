package com.example.task3;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class that compares the performance of different
 * ways to traverse an ArrayList in Java.
 */
public class ArrayListLoopTest {

    /**
     * The size of the ArrayList to be used for performance comparison.
     */
    private static final int SIZE = 1_000_00;

    /**
     * The ArrayList that will be filled and traversed.
     */
    private static ArrayList<Integer> arrList = new ArrayList<>(SIZE);

    /**
     * Start time for performance measurement.
     */
    private static long start;

    /**
     * End time for performance measurement.
     */
    private static long end;

    /**
     * Used to accumulate sum during traversal.
     */
    private static int sum = 0;

    /**
     * Fills the ArrayList with integers and compares
     * the traversal time using different looping mechanisms.
     */
    public static void compareArrListTraversalPerformance() {
        fillArray();

        String forLoopMs = getForLoopMs();
        String foreachLoopMs = getForeachLoopMs();
        String whileLoopMs = getWhileLoopMs();
        String iteratorMs = getIteratorMs();

        System.out.println("ArrayList Traversal Performance:");
        System.out.println(forLoopMs);
        System.out.println(foreachLoopMs);
        System.out.println(whileLoopMs);
        System.out.println(iteratorMs);
    }

    /**
     * Populates the ArrayList with values from 1 to SIZE.
     */
    private static void fillArray() {
        arrList = new ArrayList<>(SIZE);
        for (int i = 1; i <= SIZE; i++) {
            arrList.add(i);
        }
    }

    /**
     * Measures time taken to traverse the list using a classic for loop.
     * @return formatted result with time in ms
     */
    private static String getForLoopMs() {
        start = System.nanoTime();
        sum = 0;
        for (int i = 1; i < arrList.size(); i++) {
            sum += arrList.get(i);
        }
        end = System.nanoTime();

        return "For Loop: " + (end - start) / 100_000 + " ms";
    }

    /**
     * Measures time taken using a for-each loop.
     * @return formatted result with time in ms
     */
    private static String getForeachLoopMs() {
        start = System.nanoTime();
        sum = 0;
        for (int num : arrList) {
            sum += num;
        }
        end = System.nanoTime();

        return "For-Each Loop: " + (end - start) / 100_000 + " ms";
    }

    /**
     * Measures time taken using a while loop.
     * @return formatted result with time in ms
     */
    private static String getWhileLoopMs() {
        start = System.nanoTime();
        sum = 0;
        int i = 0;
        while (i < arrList.size()) {
            sum += arrList.get(i);
            i++;
        }
        end = System.nanoTime();

        return "While Loop: " + (end - start) / 100_000 + " ms";
    }

    /**
     * Measures time taken using an iterator.
     * @return formatted result with time in ms
     */
    private static String getIteratorMs() {
        start = System.nanoTime();
        sum = 0;
        Iterator<Integer> iterator = arrList.iterator();
        while (iterator.hasNext()) {
            sum += iterator.next();
        }
        end = System.nanoTime();

        return "Iterator: " + (end - start) / 100_000 + " ms";
    }
}