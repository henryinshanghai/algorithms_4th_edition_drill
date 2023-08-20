package com.henry.basic_chapter_01.data_abstract;

import edu.princeton.cs.algs4.StdRandom;

public class TestAccumulator {
    public static void main(String[] args) {
        int totalTimes = Integer.parseInt(args[0]);
        Accumulator accumulator = new Accumulator();

        for (int cursor = 0; cursor < totalTimes; cursor++) {
            accumulator.addDataValue(StdRandom.random());
        }

        System.out.println(accumulator);
    }
}
