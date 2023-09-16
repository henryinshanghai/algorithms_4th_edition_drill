package com.henry.basic_chapter_01.algorithm_analysis.better_algorithm_05;

import com.henry.basic_chapter_01.algorithm_analysis.math_model_03.BinarySearch;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class ThreeSumFast {
    public static int count(int[] a) {
        // 计算和为0的三元组数目
        Arrays.sort(a);

        int itemAmount = a.length;
        int count = 0;

        for (int currentSpotI = 0; currentSpotI < itemAmount; currentSpotI++) {
            for (int currentSpotJ = currentSpotI + 1; currentSpotJ < itemAmount; currentSpotJ++) {
                if (BinarySearch.rankOf(-a[currentSpotI] - a[currentSpotJ], a) > currentSpotJ) {
                    count++;
                }
            }
        }

        return count;
    }

    public static void main(String[] args) {
        int[] a = In.readInts(args[0]);
        StdOut.println(count(a));
    }
}
