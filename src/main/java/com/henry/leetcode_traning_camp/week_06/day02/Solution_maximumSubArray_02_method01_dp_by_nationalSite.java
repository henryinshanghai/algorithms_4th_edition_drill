package com.henry.leetcode_traning_camp.week_06.day02;

import java.util.Arrays;

public class Solution_maximumSubArray_02_method01_dp_by_nationalSite {
    public static void main(String[] args) {
        int[] nums = {-2,1,-3,4,-1,2,1,-5,4};

        int maxRes = maxSubArray(nums);

        System.out.println("在当前数组：" + Arrays.toString(nums) + "中加和值最大的连续子数组的sum为：" + maxRes);
    }

    private static int maxSubArray(int[] nums) {
        /* 〇 准备一个dpTable */
        int[] dpTable = new int[nums.length];

        /* Ⅰ 初始化dpTable[]中的一些数据，以便进行递推 */
        dpTable[0] = nums[0];
        int max = nums[0];

        /* Ⅱ 遍历原始数组arr中的每一个元素
            按照给定的规则，使用递推的方式来计算dpTable[]中剩余元素的值 */
        for (int i = 1; i < nums.length; i++) {
//            dpTable[i] = dpTable[i - 1] > 0 ? dpTable[i - 1] + nums[i] : nums[i];
            dpTable[i] = nums[i] + (dpTable[i - 1] > 0 ? dpTable[i - 1] : 0); // 所以A[i]肯定是有的，主要看 先前扩展出来的子数组还要不要了

            max = Math.max(max, dpTable[i]);
        }

        /* Ⅲ 循环结束后，返回max的值 */
        return max;
    }
}
/*
特征：在dpTable[]中递推式，其实值使用到了相邻两项的元素
这种情况可以使用两个变量/指针来代替数组————空间优化

事实上，由于dpTable[i]只依赖于dpTable[i-1]。所以我们可以只是用一个变量
通过不断更新变量的值来记录需要的结果（而不用存储每一个中间结果）
 */
