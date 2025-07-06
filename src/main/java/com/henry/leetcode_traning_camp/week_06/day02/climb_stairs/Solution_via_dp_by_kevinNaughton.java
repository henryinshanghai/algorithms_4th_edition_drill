package com.henry.leetcode_traning_camp.week_06.day02.climb_stairs;

// 验证：对于爬楼梯有多少种走法这样的方案总数问题，可以使用 动态规划的手段 来 得到结果；
// 最优子结构：到达第N级台阶的方案数量 包含有 到达第(N-i)级台阶的方案数量
// dp[]数组的具体含义： currentStairToApproachAmountUpToIt[]
public class Solution_via_dp_by_kevinNaughton {
    public static void main(String[] args) {
        int stairAmount = 4; // 3??
//        int stairAmount = 3; // 3
        int approachesAmountToTopStair = getApproachAmountClimbingToTopStair(stairAmount);
        System.out.println("到达第" + stairAmount + "级台阶一共有" + approachesAmountToTopStair + "种走法");
    }

    private static int getApproachAmountClimbingToTopStair(int stairsAmount) {
        int[] currentStairToApproachesAmountUpToIt = new int[stairsAmount + 1]; // +1是为了得到自然数的映射关系

        currentStairToApproachesAmountUpToIt[0] = 1; // 🐖 这里要初始化为1，才能在计算dp[2]的时候得到正确的值
        currentStairToApproachesAmountUpToIt[1] = 1;

        for (int currentStair = 2; currentStair <= stairsAmount; currentStair++) {
            currentStairToApproachesAmountUpToIt[currentStair]
                    = currentStairToApproachesAmountUpToIt[currentStair - 1] // 到达前一级台阶的走法数量
                    + currentStairToApproachesAmountUpToIt[currentStair - 2]; // 到达前两级台阶的走法数量
        }

        printArr(currentStairToApproachesAmountUpToIt);

        // 返回 到达最后一级台阶的走法数量
        // 🐖 这里 currentStair => its amount的映射关系中，currStair是自然数
        return currentStairToApproachesAmountUpToIt[stairsAmount];
    }

    private static void printArr(int[] currentStairToApproachesAmountUpToIt) {
        for (int currentStair = 0; currentStair < currentStairToApproachesAmountUpToIt.length; currentStair++) {
            System.out.print(currentStairToApproachesAmountUpToIt[currentStair] + " ");
        }

        System.out.println();
    }
}
/*
参考：https://www.youtube.com/watch?v=uHAToNgAPaM
 */
