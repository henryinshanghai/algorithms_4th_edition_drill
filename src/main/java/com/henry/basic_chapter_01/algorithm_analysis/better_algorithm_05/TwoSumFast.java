package com.henry.basic_chapter_01.algorithm_analysis.better_algorithm_05;

import com.henry.basic_chapter_01.algorithm_analysis.math_model_03.BinarySearch;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class TwoSumFast {
    public static int count(int[] a) {
        // 计算和为0的整数对数目
        Arrays.sort(a);

        int itemAmount = a.length;
        int count = 0;

        for (int currentSpot = 0; currentSpot < itemAmount; currentSpot++) {
            if (BinarySearch.rankOf(-a[currentSpot], a) > currentSpot) {
                count++;
            }
        }

        return count;
    }

    public static void main(String[] args) {
        int[] a = In.readInts(args[0]);
        StdOut.println(count(a));
    }
}
