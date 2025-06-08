package com.henry.leetcode_traning_camp.week_04.day06.jump_game;

// 验证：对于 是否能够跳到终点的问题，可以 使用逆向指针遍历所有终点前的位置 + 更新当前所需到达位置的方式 来 判断
// 概念：终点位置、当前所需要到达的位置、当前位置
// 原理：按此方式回溯，如果 能够跳转到终点，则 第一个“当前需要到达的位置” 一定会是位置0
public class Solution_jumpGame_via_backwards {
    public static void main(String[] args) {
        int[] currentSpotToItsMaxJumpDistance = {2,3,1,1,4};
//        int[] currentSpotToItsMaxJumpDistance = {3, 2, 1, 0, 4};
        boolean yesOrNo = IsAbleToReachEnd(currentSpotToItsMaxJumpDistance);
        System.out.println("按照当前数组能否跳到数组末尾？" + yesOrNo);
    }

    private static boolean IsAbleToReachEnd(int[] currentSpotToItsMaxJumpDistance) {
        // 〇 准备一个变量 用于记录当前需要到达的位置
        int spotNeedToReach = currentSpotToItsMaxJumpDistance.length - 1;

        // Ⅰ 从后往前遍历数组   更新“当前需要到达的位置”
        // 对于 目标位置 之前的每一个位置，判断 由它能否跳跃到达到 “需要到达的位置”/“目标位置”
        for (int backwardsCursor = currentSpotToItsMaxJumpDistance.length - 2; backwardsCursor >= 0; backwardsCursor--) {
            int maxJumpDistanceOnCurrentSpot = currentSpotToItsMaxJumpDistance[backwardsCursor];
            // 如果 当前位置 + 当前位置上所能跳出的最大距离 大于等于 目标位置，说明 能够 由当前位置 到达 目标位置，则：
            if (backwardsCursor + maxJumpDistanceOnCurrentSpot >= spotNeedToReach) {
                // 使用 当前位置 来 更新“需要到达的位置”/“目标位置”
                spotNeedToReach = backwardsCursor;
            }
        }

        // Ⅱ 判断最终“反向需要到达的位置” 是不是 落到了“起点位置”
        // 原理：我们唯一能够确定的是 - 起点是无条件可达的
        return spotNeedToReach == 0;
    }
}
