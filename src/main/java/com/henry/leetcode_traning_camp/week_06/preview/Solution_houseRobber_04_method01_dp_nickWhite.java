package com.henry.leetcode_traning_camp.week_06.preview;

import java.util.Arrays;

public class Solution_houseRobber_04_method01_dp_nickWhite {
    public static void main(String[] args) {
        int[] housesValue = {2, 7, 9, 3, 1};

        int maxValue = robber(housesValue);

        System.out.println("从如下房子中：" + Arrays.toString(housesValue) + "所能抢到的最大金额为：" + maxValue);
    }

    private static int robber(int[] nums) {
        /* 〇 对给定的房子价值序列进行判空  */
        if (nums.length == 0) return 0;

        /* Ⅰ/template1 准备一个比房子价值序列大一个位置的数组 作为dpTable */
        int[] dpTable = new int[nums.length + 1]; // 这里指定的值是dpTable的容量，而不是下标

        /* Ⅱ/template2 准备dpTable的前两个初始元素 用作为递推的基础 */
        dpTable[0] = 0; // 抢 0个房子
        dpTable[1] = nums[0]; // 抢1个房子

        /* Ⅲ 遍历房子价值的序列，并进行递推 */
        for (int i = 1; i < nums.length; i++) { // template3: i = 1
            // 两种抢劫的选择：当前的房子抢 OR 不抢
            /*
                如果抢当前房子，就不能抢当前房子的前一个房子
                如果不抢当前房子，那么 以当前房子为结束位置的房子序列中能够抢到的钱 = 以当前房子的前一个房子为结束位置的房子序列所能抢到的钱；
                    因为当前房子没有被抢，所以不存在额外收益，而只是用掉了一个房子

             */
            dpTable[i + 1] = Math.max(dpTable[i],
                    dpTable[i - 1] + nums[i]);

        }

        /* Ⅳ/template4 返回dpTable[]数组中满足题设条件的项 */
        return dpTable[nums.length];
    }
}
/*
① dp[i]表示的是什么？ aka 状态转移中的状态是什么
② 找出递推方程；

 */
