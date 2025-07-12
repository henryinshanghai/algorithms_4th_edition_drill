package com.henry.leetcode_traning_camp.week_06.day04.burglary;

import java.util.Arrays;

// 验证：对于 沿着一个圈来打劫的问题，可以使用 currentHouseToMaxRobMoneyUpToIt[] 的dp数组 来 得到其最大可能的抢劫数额
// 最优子结构/递推公式: dp[i] = max(dp[i-1], dp[i-2] + current_house_value)
// 由于递推公式中，当前的dp[]值的计算 只与其前两项相关，因此 可以使用三个连续的指针变量 来 代替dp[]数组，以此节约空间
// 🐖 双指针的做法 可以避免对下标越界情况的处理
public class Solution_via_dp_optimal_byNareshGupta {
    public static void main(String[] args) {
        int[] housesValues = {1, 2, 3, 1}; // 4
//        int[] housesValues = {2,3,2}; // 3
//        int[] housesValues = {0}; // 0
//        int[] housesValues = {1}; // 1

        int maxMoney = maxMoneyCanStoleFromHouses(housesValues);

        System.out.println("从给定的房子序列" + Arrays.toString(housesValues) + "所能偷到的最大金额为：" + maxMoney);
    }

    private static int maxMoneyCanStoleFromHouses(int[] housesValues) {
        // Ⅰ 处理边界情况：如果房子序列中就只有一所房子，则：最大金额就是偷这所房子
        if (housesValues.length == 1) return housesValues[0];

        // Ⅱ 由于房子序列组成了一个圆圈，因此我们可以：
        /*
            人为地/逻辑上地 把房子序列分解成两个可抢的房子子序列：
                序列A: [0, houses.length - 1);
                序列B: [1, house.length); // 实际区间是左闭右开，因此不会越界
         */
        return Math.max(
                getMaxBurglaryValueFromRange(housesValues, 0, housesValues.length - 1),
                getMaxBurglaryValueFromRange(housesValues, 1, housesValues.length));
    }

    /**
     * 计算从 给定的房子序列 中的特定区间中所能抢到的最大金额
     *
     * @param housesValues 给定的房子序列
     * @param startSpot    抢劫区间的起始位置
     * @param endSpot      抢劫区间的终止位置 之所以没有出现下标越界，是因为这里代码for循环中没有使用 = 符号
     * @return
     */
    private static int getMaxBurglaryValueFromRange(int[] housesValues, int startSpot, int endSpot) {
        // ① 准备两个额外的变量prevOne、maxRobberyMoneyUpToPrevTwo & 一个变量max
        int maxRobberyMoneyUpToPrevTwo = 0, maxRobberyMoneyUpToPrevOne = 0, // prevOne与prevTwo用于存储临时的max（用于计算当前的max）
            maxRobberyMoneyUpToCurrent = 0; //  max用于记录与更新 当前所抢劫到的最大金额

        // ② 开始遍历当前区间中的每一个位置，并累加max
        for (int currentSpot = startSpot; currentSpot < endSpot; currentSpot++) { // 🐖 由于选择了左闭右开区间，所以这里是<
            // 获取到 当前房子的价值
            int currentHouseValue = housesValues[currentSpot];

            /* 计算出 抢劫当前房子的情况下，所能得到的金额 */
            // 如果抢劫当前的房子，说明不抢劫 前一个房子，则：
            // 抢劫到的金额 = 到前两个房子为止所抢到的金额 + 当前房子的价值
            int robberyMoneyWhenRobCurrentHouse = maxRobberyMoneyUpToPrevTwo + currentHouseValue;

            // 从 “抢劫当前房子” 与 “不抢劫当前房子” 的抢劫结果中，选择出 最大值 来作为“抢劫到此房子为止所能抢到的最大金额”
            maxRobberyMoneyUpToCurrent = Math.max(robberyMoneyWhenRobCurrentHouse, // 抢劫当前房子
                    maxRobberyMoneyUpToPrevOne); // 不抢劫当前房子

            // 更新 prevOne与prevTwo变量的值 来 用于计算下一个位置的max
            maxRobberyMoneyUpToPrevTwo = maxRobberyMoneyUpToPrevOne;
            maxRobberyMoneyUpToPrevOne = maxRobberyMoneyUpToCurrent;
        }

        // ③ 返回循环结束后 最终求取到的max aka 抢劫到最后一个房子为止所能抢到的最大金额
        return maxRobberyMoneyUpToCurrent;
    }
}
