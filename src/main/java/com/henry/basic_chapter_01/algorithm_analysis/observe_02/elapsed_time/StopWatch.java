package com.henry.basic_chapter_01.algorithm_analysis.observe_02.elapsed_time;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.ThreeSum;

// 计时器数据类型
public class StopWatch {
    private final long start;

    public StopWatch() {
        start = System.currentTimeMillis();
    }

    // 返回自对象创建之后，所经历的时间长度
    public double elapsedTime() {
        long now = System.currentTimeMillis();
        return (now - start) / 1000.0;
    }

    public static void main(String[] args) {
        int numAmount = Integer.parseInt(args[0]);
        int[] numArray = new int[numAmount];

        for (int cursor = 0; cursor < numAmount; cursor++) {
            // 返回指定区间中的一个随机数 - 手段:uniform(a, b)
            numArray[cursor] = StdRandom.uniform(-1000000, 1000000);
        }

        StopWatch timer = new StopWatch();
        int count = ThreeSum.count(numArray);
        double elapsedTime = timer.elapsedTime();

        StdOut.println(count + " triples " + elapsedTime + " seconds");
    }
}
