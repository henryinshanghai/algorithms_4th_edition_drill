package com.henry.leetcode_traning_camp.week_06.day05.perfect_square;

import java.util.Arrays;

// 验证：对于 使用平方数凑出目标数字的问题，可以使用
// dp[]:currentNumberToItsMinRequiredSquareAmount 与 递推公式 dp[givenNumber - <current_square>] + 1
// 的动态规划 方法 来 求出所需的最少数字数量
// 本质上是  使用最少数量的可选硬币 凑出目标金额的 问题
public class Solution_via_dp_by_happygirl {
    public static void main(String[] args) {
        int givenNumber = 12;
        int minSquareAmount = minRequiredSquareAmountToMakeUp(givenNumber); // 3
        System.out.println("凑出正整数 " + givenNumber + "所需要的完全平方数的最少个数为：" + minSquareAmount);
    }

    private static int minRequiredSquareAmountToMakeUp(int givenNumber) {
        // 1 准备dp[]数组 currentNumber -> its min required square amount
        int[] currentNumberToItsMinRequiredSquareAmount = new int[givenNumber + 1];

        // 2 初始化dp[]数组的所有元素 以方便后记对元素的更新
        Arrays.fill(currentNumberToItsMinRequiredSquareAmount, givenNumber); // 15 = 15 * 1

        // 3 初始化dp[0]与dp[1]元素 来 给递推一个正确的开始
        currentNumberToItsMinRequiredSquareAmount[0] = 0;
        currentNumberToItsMinRequiredSquareAmount[1] = 1;

        // 4 递推计算出 dp[]数组所有元素正确的值
        for (int currentNumber = 1; currentNumber <= givenNumber; currentNumber++) {
            // 事实：dp[]数组中前面的元素 不会影响到 后继的元素??
            // currentNumberToItsMinRequiredSquareAmount[4]的意思是 凑出数值4 最少需要 currentNumberToItsMinRequiredSquareAmount[4]个完全平方数
            // 在计算 dp[5]的时候，dp[4]的结果不会被影响到。dp[]数组中的元素值 是独立的???

            // 对于每一个子问题，我们都尝试 使用可选的硬币 来 凑出它。但要怎么表示 可选的硬币(平方数)呢?
            for (int baseNumber = 1; baseNumber * baseNumber <= currentNumber; baseNumber++) {
                currentNumberToItsMinRequiredSquareAmount[currentNumber]
                        = Math.min(currentNumberToItsMinRequiredSquareAmount[currentNumber], // option01: 当前 “凑出指定数字”所需要的最少square数量 aka 它本身
                        currentNumberToItsMinRequiredSquareAmount[currentNumber - baseNumber * baseNumber] + 1); // option02: 刨去一个当前平方数后 所需要的最少平方数数量(最优子结构) + 1（刨去的这个平方数）
            }
        }

        // 5 返回 经计算后的dp[]数组中，givenNumber => its min required square amount.
        return currentNumberToItsMinRequiredSquareAmount[givenNumber];
    }
}
