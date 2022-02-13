package com.henry.leetcode_traning_camp.week_06.preview;

import java.util.Arrays;

public class Solution_maxSubArray_03_method02_dp_spaceOptimal_drill01 {
    public static void main(String[] args) {
        int[] nums = {-2,1,-3,4,-1,2,1,-5,4};

        int maxRes = maxSubArray(nums);

        System.out.println("在当前数组：" + Arrays.toString(nums) + "中加和值最大的连续子数组的sum为：" + maxRes);
    }

    private static int maxSubArray(int[] nums) {
        // Ⅰ
        int maxSubArraySum_endWithCurrItem = nums[0];

        // Ⅱ
        int maxSubArraySum_ofAllItems = nums[0];

        // Ⅲ
        for (int num : nums) {
            if (maxSubArraySum_endWithCurrItem > 0) {
                maxSubArraySum_endWithCurrItem += num;
            } else {
                maxSubArraySum_endWithCurrItem = num;
            }

            maxSubArraySum_ofAllItems = (maxSubArraySum_ofAllItems > maxSubArraySum_endWithCurrItem)
                    ? maxSubArraySum_ofAllItems : maxSubArraySum_endWithCurrItem;
        }

        return maxSubArraySum_ofAllItems;
    }
}
