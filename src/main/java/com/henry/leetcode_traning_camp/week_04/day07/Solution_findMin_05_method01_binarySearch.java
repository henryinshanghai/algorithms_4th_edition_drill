package com.henry.leetcode_traning_camp.week_04.day07;

import java.util.Arrays;

public class Solution_findMin_05_method01_binarySearch {
    public static void main(String[] args) {
        int[] nums = {4,5,6,7,0,1,2};

        int res = findMin(nums);

        System.out.println(Arrays.toString(nums) + "数组中的最小值为： " + res);
    }

    private static int findMin(int[] nums) {
        /* 〇 corner case */
        if (nums.length == 1) return 0;

        if (nums[0] < nums[nums.length - 1]) return nums.length - 1;

        /* Ⅰ 初始化BS需要用到的一些变量/指针 */
        int left = 0, right = nums.length - 1;
        int mid = 0;

        /* Ⅱ 准备循环，找到原始数组中出现的相邻降序对 */
        while (left <= right) {
            // 1 计算当前区间的中间位置
            mid = left + (right - left) / 2;

            // 如果出现了降序对，则返回降序对的前面一个元素的下标
            if (nums[mid] > nums[mid + 1]) return nums[mid + 1];
                // 对于其他的情况，则：根据中间位置元素的情况来缩减区间
            else if (nums[left] <= nums[mid]) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        /* Ⅲ 返回一个标识，表示查找失败 */
        return 0;

    }
}
