package com.henry.leetcode_traning_camp.week_03.day06;

import java.util.HashMap;
import java.util.Map;

public class Solution_majorityElement_01_method02_count {
    public static void main(String[] args) {
        int[] arr = {1, 1, 1, 2, 2, 2, 1};
        int res = majorityElement(arr);
        System.out.println("数组中的最多元素是： " + res);
    }

    private static int majorityElement(int[] nums) {
        Map<Integer, Integer> counter = new HashMap<>();

        // 遍历每个数统计次数
        for (int num: nums) {
            counter.put(num, counter.getOrDefault(num, 0) + 1);
            // 如果某个数次数超过了n/2就返回
            if (counter.get(num) > nums.length / 2) {
                return num;
            }
        }
        return -1;
    }
}
