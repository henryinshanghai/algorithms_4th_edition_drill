package com.henry.leetcode_traning_camp.week_01.day3_dp_theme;

public class Solution_biggest_subArr_sum_01_method01_iteration {
    public static void main(String[] args) {
        int[] arr = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        int biggest_sum = maxSubArray(arr);

        System.out.println("原始数组中所能找到的 <元素值加和最大的子数组的加和值为>: " + biggest_sum);
    }

    private static int maxSubArray(int[] nums) {
        // 0 鲁棒性代码
        if (nums == null || nums.length == 0) return 0;

        // 1 准备一个int变量，用于记录当前子数组的元素加和值
        int curr_subArr_sum = nums[0]; // 初始化为数组的第一个元素

        // 2 准备一个int变量，用于实时更新子数组的元素加和值的最大值
        int subArr_sum_max = curr_subArr_sum; // 初始化为当前子数组的元素加和值

        // 3 准备一个循环，迭代数组中的每一个元素
        // 作用：1 查找以当前元素作为起始元素的所有有效子数组； 2 使用找到的有效子数组来更新sum_max的值
        for (int i = 1; i < nums.length; i++) { // 从第二个元素开始，尝试把当前元素累加到子数组中...
            // 计算子数组的元素加和值
            /*
                规则：如果当前子数组的和大于0，就继续计入下一个元素；
                如果当前子数组的和小于0，则使用当前元素来重置子数组
             */
            curr_subArr_sum = (curr_subArr_sum > 0) ? curr_subArr_sum + nums[i] : nums[i];

            // 尝试用当前子数组的加和值来 更新子数组元素加和值的最大值
            subArr_sum_max = Math.max(subArr_sum_max, curr_subArr_sum);
        }

        // 4 返回老大哥
        return subArr_sum_max;
    }
} // 这种方法的时间复杂度是O(N)，因为它用来一层循环来处理数组中的节点
