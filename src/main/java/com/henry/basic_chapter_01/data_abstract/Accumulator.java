package com.henry.basic_chapter_01.data_abstract;

// 数据抽象：累加器
public class Accumulator {
    private double total;
    private int N;

    public void addDataValue(double value) {
        N++;
        total += value;
    }

    public double mean() {
        return total / N;
    }

    public String toString() {
        return "Mean (" + N + " values): " + String.format("%7.5f", mean());
    }
}
