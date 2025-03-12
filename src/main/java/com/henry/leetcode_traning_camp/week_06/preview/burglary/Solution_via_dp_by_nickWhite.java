package com.henry.leetcode_traning_camp.week_06.preview.burglary;

import java.util.Arrays;

// dp[]数组的含义：当前位置i => 以当前位置作为结束位置(包含)的房子序列 所能抢劫到的最大金额；
// 最优子结构/子问题：dp[i] = max(dp[i-1], dp[i-2] + current_house_value)
public class Solution_via_dp_by_nickWhite {
    public static void main(String[] args) {
        int[] housesValueSequence = {2, 7, 9, 3, 1};

        int maxRobberyMoney = getMaxMoneyRobberFrom(housesValueSequence);

        System.out.println("从房子序列中：" + Arrays.toString(housesValueSequence) + "所能抢到的最大金额为：" + maxRobberyMoney);
    }

    private static int getMaxMoneyRobberFrom(int[] houseValueSequence) {
        /* 〇 对给定的房子价值序列进行判空  */
        if (houseValueSequence.length == 0) return 0;

        /* Ⅰ dp[]数组的含义：抢劫到当前房子（包含）所能抢到的最大金额 */
        int[] currentHouseToItsMaxRobberyMoneyEndWithIt = new int[houseValueSequence.length + 1]; // 这里指定的值是dpTable的容量，而不是下标

        /* Ⅱ dp[]数组 起始元素的初始化 - 用作为递推的基础 */
        // 🐖 这里房子的序号 从1开始，因此 dp[0] = 0
        currentHouseToItsMaxRobberyMoneyEndWithIt[0] = 0; // 抢 0个房子
        currentHouseToItsMaxRobberyMoneyEndWithIt[1] = houseValueSequence[0]; // 抢1个房子

        /* Ⅲ 遍历房子价值的序列，并进行递推 */
        for (int currentHouseSpot = 1; currentHouseSpot < houseValueSequence.length; currentHouseSpot++) { // template3: currentHouseSpot = 1
            // 两种抢劫的选择：当前的房子抢 OR 不抢
            /*
                如果抢当前房子，说明 不能抢当前房子的前一个房子，则：dp[i] = dp[i-2] + current_house_value
                如果不抢当前房子，说明 当前房子不会提供任何的钱/收益，则：dp[i] = dp[i-1]
                    综上：dp[i] = max(dp[i-1], dp[i-2] + current_house_value)
                🐖 这里初始化了 dp[0]与dp[1], 因此我们从 dp[2]开始计算起 dp[]数组的元素值 👇
             */
            int currentHouseValue = houseValueSequence[currentHouseSpot];

            currentHouseToItsMaxRobberyMoneyEndWithIt[currentHouseSpot + 1]
                    = Math.max(currentHouseToItsMaxRobberyMoneyEndWithIt[currentHouseSpot], // option1: 不抢劫当前房子
                        currentHouseToItsMaxRobberyMoneyEndWithIt[currentHouseSpot - 1] + currentHouseValue); // option2: 抢劫当前房子

        }

        /* currentHouseSpot => its max money */
        return currentHouseToItsMaxRobberyMoneyEndWithIt[houseValueSequence.length];
    }
}