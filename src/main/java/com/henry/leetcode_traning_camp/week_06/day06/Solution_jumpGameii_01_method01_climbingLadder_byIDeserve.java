package com.henry.leetcode_traning_camp.week_06.day06;

import java.util.Arrays;

public class Solution_jumpGameii_01_method01_climbingLadder_byIDeserve {
    public static void main(String[] args) {
        int[] jumpLengthThroughPositions = {2,3,1,1,4};
        int minSteps= minJump(jumpLengthThroughPositions); // 2
        System.out.println("如果跳跃距离序列为： " + Arrays.toString(jumpLengthThroughPositions));
        System.out.println("则，从序列开始位置跳到序列结束位置所需要的跳跃次数为： " + minSteps);
    }

    private static int minJump(int[] jumpLengthThroughPositions) {
        int positions = jumpLengthThroughPositions.length;
        if (positions <= 1) { // 如果就只有一个位置，则：不用跳就已经到结束位置了
            return 0;
        }

        // 记录并追踪当前所拥有的最长梯子  梯子的长度是 jumpLengthThroughPositions[i]
        int ladder = jumpLengthThroughPositions[0];
        // 记录并追踪当前梯子的台阶/位置数量
        int stairs = jumpLengthThroughPositions[0]; // 开始时 最长的梯子 = 当前的梯子
        // 记录并追踪跳跃的次数
        int jump = 1; // 初始值为什么是1？ 因为必须从第一个位置跳出去，否则根本到不了最后的位置

        for (int pos = 1; pos < positions; pos++) {
            // case 1: 已经到达 数组的最后一个位置
            if (pos == positions - 1) { // 当前位置 已经到达 数组的最后一个位置
                return jump;
            }

            // case 2:如果当前位置pos + 当前位置所能跳跃的长度 > 最长梯子的长度
            if (pos + jumpLengthThroughPositions[pos] > ladder) {
                // 则：换梯子
                ladder = pos + jumpLengthThroughPositions[pos]; // 这种计法多计入了一些长度/位置
            }

            // case 3：沿着当前梯子向前走一个位置
            stairs--; // 当前梯子的可用台阶数量
            if (stairs == 0) { // 如果当前梯子已经没有台阶可用...
                // 跳到下一个“最长的ladder”
                jump++;
                // 计算“最长ladder中可用的台阶数量/位置数量”
                stairs = ladder - pos; // 其实就是扣除多计入的位置/台阶
            }

        }

        // 返回整个过程最终的跳跃数
        return jump;
    }
}
