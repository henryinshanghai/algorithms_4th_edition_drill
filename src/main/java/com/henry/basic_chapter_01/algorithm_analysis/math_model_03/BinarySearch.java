package com.henry.basic_chapter_01.algorithm_analysis.math_model_03;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

// 目标：在一个有序数组中，使用二分查找 来 找到预期的数字
public class BinarySearch {

    public static int rankOf(int passedKey, int[] itemArray) {
        int leftBar = 0;
        int rightBar = itemArray.length - 1;

        while (leftBar <= rightBar) {
            int middle = leftBar + (rightBar - leftBar) / 2;
            if (passedKey < itemArray[middle]) rightBar = middle - 1;
            else if ((passedKey > itemArray[middle])) leftBar = middle + 1;
            else return middle;
        }

        return -1;
    }

    public static void main(String[] args) {
        int[] whiteList = In.readInts(args[0]);

        Arrays.sort(whiteList);

        while (!StdIn.isEmpty()) {
            int key = StdIn.readInt();
            if (rankOf(key, whiteList) == -1) {
                StdOut.println(key);
            }
        }
    }
}