package com.henry.leetcode_traning_camp.week_06.day04;

import java.util.Arrays;

public class Solution_houseRobberii_01_method01_dp_byNareshGupta {
    public static void main(String[] args) {
//        int[] housesValues = {1,2,3,1}; // 4
//        int[] housesValues = {2,3,2}; // 3
//        int[] housesValues = {0}; // 0
        int[] housesValues = {1}; // 1

        int maxMoney = maxMoneyCanStoleFromHouses(housesValues);

        System.out.println("从给定的房子序列" + Arrays.toString(housesValues) + "所能偷到的最大金额为：" + maxMoney);
    }

    private static int maxMoneyCanStoleFromHouses(int[] housesValues) {
        // Ⅰ 处理边界情况：如果房子序列中就只有一所房子，则：最大金额就是偷这所房子
        if(housesValues.length == 1) return housesValues[0];

        // Ⅱ 如果房子序列中有多个房子，则：
        /*
            人为地把房子序列分解成两个子序列：
                序列A: [0, houses.length - 1];
                序列B: [1, house.length]; // 这样下标不会越界吗？
         */
        return Math.max(robber(housesValues, 0, housesValues.length - 1),
                robber(housesValues, 1, housesValues.length));
    }

    /**
     * 计算从 给定的房子序列 中的特定区间中所能抢到的最大金额
     * @param housesValues  给定的房子序列
     * @param start 抢劫区间的起始位置
     * @param end 抢劫区间的终止位置 之所以没有出现下标越界，是因为这里代码for循环中没有使用 = 符号
     * @return
     */
    private static int robber(int[] housesValues, int start, int end) {
        // ① 准备两个额外的变量prevOne、prevTwo & 一个变量max
        int prevTwo = 0, prevOne = 0, max = 0; // prevOne与prevTwo用于存储临时的max（用于计算当前的max） max用于记录与更新 当前所抢劫到的最大金额

        // ② 开始遍历当前区间中的每一个位置，并累加max
        // 注意：这里需要为 防止出现IndexOutOfBoundary 额外留心 比如循环变量i的范围
        for (int i = start; i < end; i++) {
            // 是否抢劫当前房子：1 抢； 2 不抢； 取两者中的较大值绑定到max
            max = Math.max(prevTwo + housesValues[i], prevOne);

            // 更新变量的值 用于计算下一个max
            prevTwo = prevOne;
            prevOne = max;
        }

        // ③ 返回循环结束后求取到的max
        return max;
    }
}
