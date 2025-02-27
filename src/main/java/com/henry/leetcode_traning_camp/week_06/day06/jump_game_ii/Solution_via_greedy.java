package com.henry.leetcode_traning_camp.week_06.day06.jump_game_ii;

public class Solution_via_greedy {
    public static void main(String[] args) {
        int[] currentSpotToItsMaxJumpDistance = {2, 3, 1, 1, 4};

        int minRequiredJumps = getMinRequiredJumpsFrom(currentSpotToItsMaxJumpDistance);

        System.out.println("所需要的最少跳跃次数为：" + minRequiredJumps);
    }

    private static int getMinRequiredJumpsFrom(int[] currentSpotToItsMaxJumpDistance) {
        // 当前跳跃所能到达的最远位置
        int currentJumpMaxReachSpot = 0;
        // 下一次跳跃所能到达的最远位置
        int nextJumpMaxReachSpot = 0;
        // 统共所需要的跳跃次数
        int minRequiredJumps = 0;

        for (int currentCursorSpot = 0; currentCursorSpot < currentSpotToItsMaxJumpDistance.length; currentCursorSpot++) {
            // 计算 下一次跳跃所能到达的最远位置
            int maxJumpDistanceOfCurrentSpot = currentSpotToItsMaxJumpDistance[currentCursorSpot];

            if (currentCursorSpot + maxJumpDistanceOfCurrentSpot > nextJumpMaxReachSpot) {
                nextJumpMaxReachSpot = currentCursorSpot + maxJumpDistanceOfCurrentSpot;
            }

            // 如果当前游标位置 等于 当前跳跃所能到达的最远位置，说明 可能需要进行下一次跳跃，则：
            if (currentCursorSpot == currentJumpMaxReachSpot) {
                // #1 如果 当前跳跃所能到达的最远位置 不等于 序列的最后一个位置，说明 真的需要下一次跳跃，则：
                if (currentJumpMaxReachSpot != currentSpotToItsMaxJumpDistance.length - 1) {
                    // 进行下一次跳跃
                    minRequiredJumps++;
                    // 使用 当前“下一次跳跃所能到达的最远位置” 来 更新 “当前跳跃所能到达的最远位置” - 为下一轮跳跃做准备
                    currentJumpMaxReachSpot = nextJumpMaxReachSpot;
                } else { // #2 如果 等于 序列的最后一个位置，说明 不需要下一次跳跃了，则：
                    // 直接跳出循环
                    break;
                }
            }
        }

        return minRequiredJumps;

    }
}
