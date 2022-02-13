package com.henry.leetcode_traning_camp.week_04.day07;

import java.util.Arrays;

public class Solution_findInRotatedArray_03_method01_binarySearch_by_yijie {
    public static void main(String[] args) {
        // 题设信息
        int[] nums = {4, 5, 6, 7, 0, 1, 2};
        int target = 0;


        int resIndex = search(nums, target);

        System.out.println(target + "在数组" + Arrays.toString(nums) + "中的下标位置为： " + resIndex);
    }

    public static int search(int[] nums, int target) {
        if (nums == null || nums.length == 0) return -1;

        // step1 找出原始数组中的最大元素————这个元素就是两个升序区间的交界/旋转点
        int peakIndex = findPeakIndex(nums);

        // step2 在正确的升序序列中执行朴素版本的二分查找
        if (peakIndex >= 0 && target >= nums[0] && target <= nums[peakIndex]) {
            // 这个二分查找的函数需要指定在数组array中查找target的返回
            // 如果target在[0, peakIndex]区间内，则：
            return binarySearch(nums, 0, peakIndex, target);
        } else { // 否则target必然在[peakIndex+1, array.length-1]的区间内。则：
            return binarySearch(nums, peakIndex + 1, nums.length - 1, target);
        }

    }

    /**
     * 找到翻转后的数组中的最大元素的索引
     * @param nums
     * @return
     */
    private static int findPeakIndex(int[] nums) {
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
            if (nums[mid] > nums[mid + 1]) return mid;
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

    /**
     * 这就只是一个典型的二分查找
     * 但是在方法参数列表中指定了查找的区间边界
     * @param array
     * @param left
     * @param right
     * @param target
     * @return
     */
    public static int binarySearch(int[] array, int left, int right, int target) {
        while (left <= right) { // EXPR1： <=
            // 1 计算中间位置
            int mid = left + (right - left) / 2;

            // 2 比较中间位置的元素 与 target
            // 如果相等，就返回 mid
            if(array[mid] == target) return mid;
            // 如果不相等，则：缩减查找区间
            else if (array[mid] < target) { // EXPR2: left = mid + 1
                left = mid + 1;
            } else { // EXPR3: right = mid - 1
                right = mid - 1;
            }
        }

        return -1;
    }
}
