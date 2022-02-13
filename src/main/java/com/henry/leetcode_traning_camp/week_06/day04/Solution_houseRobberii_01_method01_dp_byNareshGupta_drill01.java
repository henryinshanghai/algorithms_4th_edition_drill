package com.henry.leetcode_traning_camp.week_06.day04;

import java.util.Arrays;

public class Solution_houseRobberii_01_method01_dp_byNareshGupta_drill01 {
    public static void main(String[] args) {

        int[] housesValues = {1,2,3,1}; // 4
//        int[] housesValues = {2,3,2}; // 3
//        int[] housesValues = {0}; // 0
//        int[] housesValues = {1}; // 1

        int maxMoney = maxMoneyCanStoleFromHouses(housesValues);

        System.out.println("从给定的房子序列" + Arrays.toString(housesValues) + "所能偷到的最大金额为：" + maxMoney);
    }

    private static int maxMoneyCanStoleFromHouses(int[] housesValues) {
        if (housesValues.length == 1) return housesValues[0];

        return Math.max(
                robberFromRange(housesValues, 0, housesValues.length - 1),
                robberFromRange(housesValues, 1, housesValues.length)
        );
    }

    private static int robberFromRange(int[] housesValues, int start, int end) {
        // ① 准备两个额外的变量prevOne、prevTwo & 一个变量max
        int prevOne = 0, prevTwo = 0, max = 0;

        // ② 开始遍历当前区间中的每一个位置，并累加max
        // 注意：这里需要为 防止出现IndexOutOfBoundary 额外留心 比如循环变量i的范围
        for (int i = start; i < end; i++) {
            // 是否抢劫当前房子：1 抢； 2 不抢； 取两者中的较大值绑定到max
            max = Math.max(prevTwo + housesValues[i], prevOne); // prevOne与prevTwo用于存储临时的max（用于计算当前的max） max用于记录与更新 当前所抢劫到的最大金额

            // 更新变量的值 用于计算下一个max
            prevTwo = prevOne;
            prevOne = max;
        }
        // ③ 返回循环结束后求取到的max
        return max;
    }
}
