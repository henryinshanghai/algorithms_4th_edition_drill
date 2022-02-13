package com.henry.leetcode_traning_camp.week_06.preview;

import java.util.Arrays;

public class Solution_houseRobber_04_method01_dp_nickWhite_drill01 {
    public static void main(String[] args) {
        int[] housesValue = {2, 7, 9, 3, 1};

        int maxValue = robber(housesValue);

        System.out.println("从如下房子中：" + Arrays.toString(housesValue) + "所能抢到的最大金额为：" + maxValue);
    }

    private static int robber(int[] housesValue) {
        /* 〇 对给定的房子价值序列进行判空  */
        if (housesValue.length == 0) {
            return 0;
        }

        /* Ⅰ/template1 准备一个比房子价值序列大一个位置的数组 作为dpTable */
        int[] dpTable = new int[housesValue.length + 1];

        /* Ⅱ/template2 准备dpTable的前两个初始元素 用作为递推的基础 */
        // dpTable[i]表示 以第i-th房子作为结束位置的房子序列所能抢劫到的最大收益
        dpTable[0] = 0;
        dpTable[1] = housesValue[0]; // EXPR1:从第一个房子中所能抢到的最大收益

        /* Ⅲ 遍历房子价值的序列，并进行递推 */
        for (int i = 1; i < housesValue.length; i++) {
            dpTable[i + 1] = Math.max(
                    dpTable[i],
                    dpTable[i - 1] + housesValue[i]
            );
        }

        /* Ⅳ/template4 返回dpTable[]数组中满足题设条件的项 */
        return dpTable[housesValue.length];
    }
}
