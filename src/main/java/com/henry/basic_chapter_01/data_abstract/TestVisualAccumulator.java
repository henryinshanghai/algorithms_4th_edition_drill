package com.henry.basic_chapter_01.data_abstract;

import edu.princeton.cs.algs4.StdRandom;

public class TestVisualAccumulator {
    public static void main(String[] args) {
        int totalAmount = Integer.parseInt(args[0]);
        VisualAccumulator visualAccumulator = new VisualAccumulator(totalAmount, 1.0);

        for (int cursor = 0; cursor < totalAmount; cursor++) {
            visualAccumulator.addDataValue(StdRandom.random());
        }

        System.out.println(visualAccumulator);
    }
}
