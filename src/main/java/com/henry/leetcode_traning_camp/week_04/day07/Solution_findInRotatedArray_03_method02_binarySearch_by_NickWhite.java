package com.henry.leetcode_traning_camp.week_04.day07;

import java.util.Arrays;

public class Solution_findInRotatedArray_03_method02_binarySearch_by_NickWhite {
    public static void main(String[] args) {
        // 题设信息
        int[] nums = {4, 5, 6, 7, 0, 1, 2};
        int target = 0;


        int resIndex = search(nums, target);

        System.out.println(target + "在数组" + Arrays.toString(nums) + "中的下标位置为： " + resIndex);

    }

    private static int search(int nums[], int target) {
        if (nums == null || nums.length == 0) return -1;

        int left = 0;
        int right = nums.length - 1;

        // 作用：让left指针与right指针在最小值处(相邻降序对的第二个元素)相遇
        while (left < right) {
            int midPoint = left + (right - left) / 2;

            // 判断是否出现了逆序对，如果出现了，则：缩减查找范围
            if (nums[midPoint] > nums[right]) {
                left = midPoint + 1;
            } else {
                right = midPoint;
            }
        }

        // 记录下最小元素的下标
        int start = left;

        // 重置指针变量的值，用于朴素版本的二分查找
        left = 0;
        right = nums.length - 1;

        // 判断应该在哪一个部分进行二分查找
        if (target >= nums[start] && target <= nums[right]) {
            left = start;
        } else {
            right = start - 1; // #Q: does this even matter?
        }

        while (left <= right) {
            int midpoint = left + (right - left) / 2;

            if (nums[midpoint] == target) {
                return midpoint;
            } else if (nums[midpoint] < target) {
                left = midpoint + 1;
            } else {
                right = midpoint - 1;
            }
        }

        return -1;

    }
}
