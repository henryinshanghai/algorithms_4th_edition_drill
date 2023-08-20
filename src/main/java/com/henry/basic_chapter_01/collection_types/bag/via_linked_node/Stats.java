package com.henry.basic_chapter_01.collection_types.bag.via_linked_node;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

// 验证：使用Bag，能够轻松地汇总元素，并得到平均数、方差这样的统计结果
public class Stats {
    public static void main(String[] args) {
        Bag<Double> numbers = new Bag<Double>();

        while (!StdIn.isEmpty()) {
            numbers.add(StdIn.readDouble());
        }
        int numberAmount = numbers.size();

        double sum = 0.0;
        for (Double number : numbers) {
            sum += number;
        }
        double mean = sum / numberAmount;

        sum = 0.0;
        for (Double num : numbers) {
            sum += (num - mean) * (num - mean);

        }
        double std = Math.sqrt(sum / (numberAmount - 1));

        StdOut.printf("Mean: %.2f\n", mean);
        StdOut.printf("Std dev: %.2f\n", std);
    }
}
