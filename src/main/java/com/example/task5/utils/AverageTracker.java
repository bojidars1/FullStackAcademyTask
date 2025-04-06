package com.example.task5.utils;

/**
 * A utility class used to track the running total (sum), count of entries,
 * and the next available row index when processing Excel data.
 */
public class AverageTracker {

    /**
     * The sum of numeric values (e.g., prices).
     */
    private double sum;

    /**
     * The number of values accumulated in the sum.
     */
    private int count;

    /**
     * The next available row index to write to in the output Excel sheet.
     */
    private int nextRowIndex;

    /**
     * Gets the accumulated sum.
     * @return the current sum
     */
    public double getSum() {
        return sum;
    }

    /**
     * Sets the accumulated sum.
     * @param sum the sum to set
     */
    public void setSum(double sum) {
        this.sum = sum;
    }

    /**
     * Gets the number of entries counted.
     * @return the current count
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets the number of entries.
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Gets the next available row index.
     * @return the next row index
     */
    public int getNextRowIndex() {
        return nextRowIndex;
    }

    /**
     * Sets the next available row index.
     * @param nextRowIndex the row index to set
     */
    public void setNextRowIndex(int nextRowIndex) {
        this.nextRowIndex = nextRowIndex;
    }

    /**
     * Constructs an AverageTracker with the given initial values.
     * @param sum the initial sum
     * @param count the initial count
     * @param nextRowIndex the initial row index
     */
    public AverageTracker(double sum, int count, int nextRowIndex) {
        this.sum = sum;
        this.count = count;
        this.nextRowIndex = nextRowIndex;
    }
}