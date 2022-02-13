package com.henry.leetcode_traning_camp.week_01.day1.arrays;

/**
 * 移除数组中的重复元素；
 */
public class Solution_remove_duplicates_01 {
    public static void main(String[] args) {
//        int[] nums = {1, 1, 2};
        int[] nums = {1, 1, 1, 2, 2, 3, 4, 5}; // 题设：原始数组中的元素是有序的

        // 移除数组中的重复元素；
        int res = removeDuplicates(nums);

        printSubArr(nums, res);
    }

    /**
     * 移除数组中的重复元素；
     */
    private static int removeDuplicates(int[] nums) {
        // 鲁棒性代码
        if(nums.length == 0) return 0;

        /* 找出有序数组中的重复元素，并进行新元素的重新排列 */
        // 手段：使用两个指针来比较相邻的元素
        // 指针i在前
        int i = 0;
        // 指针j在后
        for (int j = 1; j < nums.length; j++) {
            // 如果当前元素与指针i指向的元素相等，说明还没有遇到新的元素...
            if(nums[i] == nums[j])
                // 则把j指针向后移动
                continue;
            else { // 否则说明遇到了新的元素nums[j]...
                // 则把新的元素存放到预期的位置（i指针指向位置的下一个位置）
                i++;
                nums[i] = nums[j];
            }
        } // 找到新的元素 + 绑定到正确的位置

        // 返回长度，所以索引值+1
        return i+1;
    }

    private static void printSubArr(int[] nums, int i) {
        int length = nums.length;

        for (int j = 0; j < length; j++) {
            if (j < i) {
                System.out.print(nums[j] + " ");
            } else {
                break;
            }
        }
    }
}
// [1, 1, 1, 2, 2, 3, 4, 5]
// 排序后的数组：[1, 2, 3, 4, 5, 3, 4, 5]