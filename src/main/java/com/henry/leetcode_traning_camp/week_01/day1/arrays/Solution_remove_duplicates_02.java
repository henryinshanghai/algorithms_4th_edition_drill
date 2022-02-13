package com.henry.leetcode_traning_camp.week_01.day1.arrays;

public class Solution_remove_duplicates_02 {
    public static void main(String[] args) {
        int[] nums = {1, 1, 1, 2, 2, 3, 4, 5};

        int newLength = removeDuplicates(nums, 1 );

        if (newLength == 2) {
            System.out.println("数组中的非重复元素排列成功！");
        }
    }

    private static int removeDuplicates(int[] nums, int k) {
        int i = 0;
        for (int n : nums) {
            // 先存入k个元素  手段：i < k & i++ & nums[i++] = n;
            // 之后每遍历一个元素就和第前k个元素比较是否相等  手段：比较当前元素n 与 当前元素往前数前k个元素
            if (i < k || n > nums[i - k]) {
                nums[i++] = n;
            }
        }

        return i;
    }
} // Fixme this ain't make sense to me, yet work fine.😳
// still does not make sense
// 操作前：[1, 1, 1, 2, 2, 3, 4, 5]
// 操作后：[1, 2, 3, 4, 5, 3, 4, 5]