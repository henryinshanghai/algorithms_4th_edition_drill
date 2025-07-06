package com.henry.leetcode_traning_camp.week_06.day02.climb_stairs;

// 验证：如果递推公式中，f(i)仅仅依赖于f(i-1),f(i-2)的话，可以使用 curr, prev1, prev2这三个不断前进的指针变量 来 替代dp[]数组本身
// dp[]数组的具体含义： currentStairToApproachAmountUpToIt[]
// 🐖 被更新的指针变量 应该写在=的左边
public class Solution_via_dp_with_optimal {
    public static void main(String[] args) {
        int stairAmount = 4; // 3??
//        int stairAmount = 3; // 3
        int approachesAmountToTopStair = getApproachAmountClimbingToTopStair(stairAmount);
        System.out.println("到达第" + stairAmount + "级台阶一共有" + approachesAmountToTopStair + "种走法");
    }

    private static int getApproachAmountClimbingToTopStair(int stairsAmount) {
        // 替代dp[]数组
//        int[] currentStairToApproachesAmountUpToIt = new int[stairsAmount + 1];
//
//        currentStairToApproachesAmountUpToIt[0] = 1;
//        currentStairToApproachesAmountUpToIt[1] = 1;

        int prev2 = 0;
        int prev1 = 1;

        for (int currentStair = 2; currentStair <= stairsAmount; currentStair++) {
//            currentStairToApproachesAmountUpToIt[currentStair]
//                    = currentStairToApproachesAmountUpToIt[currentStair - 1] // 到达前一级台阶的走法数量
//                    + currentStairToApproachesAmountUpToIt[currentStair - 2]; // 到达前两级台阶的走法数量
            // 把指针变量整体向前推进
            int approachAmountUpToCurr = prev1 + prev2;

            prev2 = prev1;
            prev1 = approachAmountUpToCurr;
        }

//        printArr(currentStairToApproachesAmountUpToIt);

        // 返回 到达最后一级台阶的走法数量
//        return currentStairToApproachesAmountUpToIt[stairsAmount];
        return prev1; // 最终的指针状态
    }

    private static void printArr(int[] currentStairToApproachesAmountUpToIt) {
        for (int currentStair = 0; currentStair < currentStairToApproachesAmountUpToIt.length; currentStair++) {
            System.out.print(currentStairToApproachesAmountUpToIt[currentStair] + " ");
        }

        System.out.println();
    }
}

