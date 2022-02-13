package com.henry.leetcode_traning_camp.week_04.day06;

public class Solution_jumpGame_i_01_method01_greedy {
    public static void main(String[] args) {
//        int[] arr = {2,3,1,1,4};
        int[] arr = {3,2,1,0,4};

        boolean canOrNot = canJump(arr);

        System.out.println("按照当前数组能够跳到数组末尾？" + canOrNot);
    }

    private static boolean canJump(int[] nums) {
        // 〇 准备一个变量 用于记录当前需要到达的位置
        int positionNeedToGo = nums.length - 1;

        // Ⅰ 从后往前遍历数组   更新“当前需要到达的位置”
        for (int i = nums.length - 2; i >= 0; i--) {
            if (i + nums[i] >= positionNeedToGo) {
                positionNeedToGo = i;
            }
        }

        // Ⅱ 判断最终“需要到达的位置”是不是落到了起点位置
        return positionNeedToGo == 0;
    }
}
