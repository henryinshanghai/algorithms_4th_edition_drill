package com.henry.leetcode_traning_camp.week_06.day03;

import java.util.Arrays;

public class Solution_maxProduct_01_method01_dp_byNickWhite_drill01 {
    public static void main(String[] args) {
        int[] arr = {2,3,-2,4};
        int maxProduct = maxProduct(arr);
        System.out.println("数组" + Arrays.toString(arr) + "中存在的乘积最大的子数组的乘积值为： " + maxProduct);
    }

    private static int maxProduct(int[] nums) {
        if (nums.length == 0) return -1;

        // 准备一些递推的初始值
        int maxProduct_endWith_currItem = nums[0];
        int minProduct_endWith_currItem = nums[0];
        int maxProduct_inWholeNums = nums[0];

        // 准备循环 开始进行递推/更新
        for (int i = 1; i < nums.length; i++) {
            // 更新 max
            maxProduct_endWith_currItem = Math.max(
                    Math.max(maxProduct_endWith_currItem * nums[i],
                            minProduct_endWith_currItem * nums[i]),
                    nums[i]
            );

            // 更新 min   这里需要使用到 max 但是发现max的值刚刚被更新了
            minProduct_endWith_currItem = Math.min(
                    Math.min(minProduct_endWith_currItem * nums[i],
                            minProduct_endWith_currItem * nums[i]),
                    nums[i]
            );

            // 更新 max_of_all
            maxProduct_inWholeNums = Math.max(maxProduct_endWith_currItem, maxProduct_inWholeNums);

        }

        return maxProduct_inWholeNums;
    }


}
