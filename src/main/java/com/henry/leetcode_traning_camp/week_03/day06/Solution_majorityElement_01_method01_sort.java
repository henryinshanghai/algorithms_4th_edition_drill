package com.henry.leetcode_traning_camp.week_03.day06;

import java.util.Arrays;

public class Solution_majorityElement_01_method01_sort {
    public static void main(String[] args) {
        int[] arr = {1, 1, 1, 2, 2, 2, 1};
        int res = majorityElement(arr);
        System.out.println("数组中的最多元素是： " + res);
    }

    private static int majorityElement(int[] arr) {
        // 先对数组进行排序
        Arrays.sort(arr);

        // 然后返回排序后的数组中间位置的元素
        return arr[arr.length / 2];
    }
}
