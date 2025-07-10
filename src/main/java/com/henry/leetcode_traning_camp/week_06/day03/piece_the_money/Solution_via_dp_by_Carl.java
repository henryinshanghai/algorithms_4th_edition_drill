package com.henry.leetcode_traning_camp.week_06.day03.piece_the_money;

import java.util.Arrays;

// 验证：对于 使用给定的有限可选元素（任意多个） 来 凑出目标数字的 最少数量问题，
// 可以使用 动态规划 来 得到问题的答案
// dp[]数组：当前金额 => 凑出当前金额所需要的最少硬币数量
// 最优子结构/递推公式：dp[i] = min(dp[i], dp[i-coin_option] + 1);
// 这是一个“完全背包”类的问题，因此for循环遍历时，先遍历物品（可选项），再遍历背包（目标金额）
// 🐖 这里有一个小技巧：计算dp[i]需要依赖“计算过的dp[i-coin_option]”，因此可以 先判断dp[i-coin_option]是不是已经被计算过了
// 如果不是，说明 凑不出(i-coin_option)这个目标金额，则：也就凑不出i这个金额，可以跳过没必要的计算
public class Solution_via_dp_by_Carl {
    public static void main(String[] args) {
        int[] coinOptions = {1, 2, 5}; // 可选的硬币面额
        int wantedMoneyAmount = 11; // 想要凑出的金额

        // 求出 凑出“目标金额” 所需要的最少硬币数量
        int minRequiredCoinAmount = calculateMinCoinAmount(coinOptions, wantedMoneyAmount); // 3
        System.out.println("凑出金额 " + wantedMoneyAmount + " 所需要的最小硬币数量：" + minRequiredCoinAmount);
    }

    public static int calculateMinCoinAmount(int[] coinOptions, int wantedMoneyAmount) {
        int[] currentMoneyAmountToItsRequiredMinCoins = new int[wantedMoneyAmount + 1];
        // 🐖 这里使用Integer类型的最大值作为初始值，那对应地 计算dp[i]时，就需要先判断dp[i-coin]是否为最大值，否则就会有数值溢出的风险
        int maxInitValue = Integer.MAX_VALUE;

        // 初始化dp数组中的元素为 整数最大值 - 这样在后续 使用min来更新元素值时，计算的值 就不会被原始值给覆盖掉
        Arrays.fill(currentMoneyAmountToItsRequiredMinCoins, maxInitValue);

        // 当金额为0时需要的硬币数目为0
        currentMoneyAmountToItsRequiredMinCoins[0] = 0;

        // 外层遍历物品
        for (int currentCoinSpot = 0; currentCoinSpot < coinOptions.length; currentCoinSpot++) {
            // 内层遍历背包 - 这里使用正序遍历，因为 完全背包中，硬币可以被选择多次
            for (int currentMoneyAmount = coinOptions[currentCoinSpot]; currentMoneyAmount <= wantedMoneyAmount; currentMoneyAmount++) {
                // 最优子结构：刨去当前硬币，凑出“剩余金额”所需要的 最少硬币数量
                // 先计算出 剩余金额
                int restMoneyAmount = currentMoneyAmount - coinOptions[currentCoinSpot];

                // 如果“剩余金额”所需要的 最少硬币数量 不等于 maxInitValue，说明 针对此金额已经有“最少硬币方案”了，则：
                // 🐖 这里的if判断是重要的，否则就可能会产生 数值溢出的错误
                if (currentMoneyAmountToItsRequiredMinCoins[restMoneyAmount] != maxInitValue) {
                    // 确定 “当前金额”的最少硬币方案
                    currentMoneyAmountToItsRequiredMinCoins[currentMoneyAmount]
                            = Math.min(currentMoneyAmountToItsRequiredMinCoins[currentMoneyAmount], // option01：当前方案 所需要的硬币数量
                            currentMoneyAmountToItsRequiredMinCoins[restMoneyAmount] + 1); // option02：选择当前硬币时所需的硬币数量：剩余金额所需要的硬币数量 + 当前硬币的数量1
                }

                // 如果 等于 maxInitValue，说明 针对此“剩余金额”不存在“最少硬币方案”，则：无法凑出当前金额的“最少硬币方案”
            }
        }

        // 最终dp[]数组元素 只有可能是两种情况：#1 保持原始值； #2 经过计算后的值
        return currentMoneyAmountToItsRequiredMinCoins[wantedMoneyAmount] == maxInitValue // 如果dp[]数组的元素值仍旧是初始值，说明 没有凑出这个金额的“最少硬币方案”
                ? -1 // 则：返回-1 来 表示这种情况
                : currentMoneyAmountToItsRequiredMinCoins[wantedMoneyAmount]; // 否则，返回 计算元素值：“凑出此金额所需要的最少硬币数量”
    }
}
