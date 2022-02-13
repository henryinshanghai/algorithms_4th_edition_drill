package com.henry.leetcode_traning_camp.week_04.day06;

public class Solution_jumpGame_ii_02_method01_greedy {
    public static void main(String[] args) {
//        int[] arr = {2,3,1,1,4};
        int[] arr = {3,2,1,0,4};

        int minSteps = minimumJumpSteps(arr);

        System.out.println("按照当前数组跳到数组末尾所需要的最小步数为： " + minSteps);

    }

    private static int minimumJumpSteps(int[] nums) {
        // 这种需要维护多个变量的手段总是不容易理解 因为变量太多
        int furthestPosition = 0;
        int minJumpSteps = 0;
        int currEnd = 0;

        // only watch the state ahead nums.length-1 position
        for (int i = 0; i < nums.length - 1; i++) {
            // 更新furthestPosition
            if (i + nums[i] > furthestPosition) {
                furthestPosition = i + nums[i];
            }

            // 更新
            if (i == currEnd) {
                minJumpSteps++;
                currEnd = furthestPosition;
            }
        }

        return minJumpSteps;
    }
} // 这种解法不容易理解 skip
// 思路：利用最远可达距离来做
// 这种算法如果提供了一个不正确的数组（根本到达不了终点位置），那么方法就会给出错误的结果
