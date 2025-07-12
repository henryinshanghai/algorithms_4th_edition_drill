package com.henry.leetcode_traning_camp.week_06.day04.burglary;

import java.util.Arrays;


public class Solution_via_separate_ranges {
    public static void main(String[] args) {
        int[] housesValues = {1, 2, 3, 1}; // 4
//        int[] housesValues = {2,3,2}; // 3
//        int[] housesValues = {0}; // 0
//        int[] housesValues = {1}; // 1

        int maxMoney = maxMoneyCanStoleFromHouses(housesValues);

        System.out.println("从给定的房子序列" + Arrays.toString(housesValues) + "所能偷到的最大金额为：" + maxMoney);
    }

    private static int maxMoneyCanStoleFromHouses(int[] housesValues) {
        if (housesValues == null || housesValues.length == 0) {
        return 0;
    }
        int totalHouses = housesValues.length;
        if (totalHouses == 1) {
            return housesValues[0];
        }

        // 情况1：偷窃第一间到倒数第二间（排除最后一间）  区间为左右闭区间
        int maxMoneyWithoutLastHouse = robLinearRange(housesValues, 0, totalHouses - 2);
        // 情况2：偷窃第二间到最后一间（排除第一间）    区间为左右闭区间
        int maxMoneyWithoutFirstHouse = robLinearRange(housesValues, 1, totalHouses - 1);

        // 取两种情况的最大值
        return Math.max(maxMoneyWithoutLastHouse, maxMoneyWithoutFirstHouse);
    }

    /**
     * 计算从 给定的房子序列 中的特定区间中所能抢到的最大金额
     *
     * @param housesValues 给定的房子序列
     * @param startHouseIndex    抢劫区间的起始位置
     * @param endHouseIndex      抢劫区间的终止位置 之所以没有出现下标越界，是因为这里代码for循环中没有使用 = 符号
     * @return
     */
    private static int robLinearRange(int[] housesValues, int startHouseIndex, int endHouseIndex) {
        if (startHouseIndex > endHouseIndex) {
            return 0;
        }

        // 计算区间的大小
        int rangeSize = endHouseIndex - startHouseIndex + 1;
        // dp[i] 表示偷窃到第 i 间房屋时的最大金额
        int[] currentHouseToMaxRobberyMoneyUpToIt = new int[rangeSize + 1];

        currentHouseToMaxRobberyMoneyUpToIt[0] = 0; // 没有房屋时金额为 0
        currentHouseToMaxRobberyMoneyUpToIt[1] = housesValues[startHouseIndex]; // 只有一间房屋时直接偷

        // 🐖 动态规划的索引是解决问题的步数，不是原始数据的直接映射！
        for (int currentHouseIndex = 2; currentHouseIndex <= rangeSize; currentHouseIndex++) {
            // 考察houseValues 来 获取到正确的“当前房子价值”
            int currentHouseValue = housesValues[startHouseIndex + currentHouseIndex - 1];

            // 状态转移：偷当前房屋（+前前房屋的金额）或不偷（保持前一个房屋的金额）
            currentHouseToMaxRobberyMoneyUpToIt[currentHouseIndex] =
                    Math.max(
                        currentHouseToMaxRobberyMoneyUpToIt[currentHouseIndex - 1],
                        currentHouseToMaxRobberyMoneyUpToIt[currentHouseIndex - 2] + currentHouseValue
            );
        }

        // 返回 最后一个被计算的dp[]元素
        return currentHouseToMaxRobberyMoneyUpToIt[rangeSize];
    }
}
