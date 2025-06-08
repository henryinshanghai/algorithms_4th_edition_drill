package com.henry.leetcode_traning_camp.week_04.day06.jump_game2;

// 对于 求跳跃楼梯所需要的最小跳跃次数 这样的问题，可以使用 向前推进的动态指针 的方式 来 得到结果
// 概念：#0 第X次跳跃 所能达到的最远位置；
// #1 跳跃作为基本动作(一次跳跃可以 跳xx单位的距离)、
// #2 当前所能达到的最远位置 currentMaxReachSpot、
// #3 经下一次跳跃后 所能到达的最远位置 maxReachSpotViaNextJump
// 原理：#1 从起点开始跳跃，每次跳跃都有其能够到达的最远位置；
// #2 当游标指针 移动到此最远位置后，就需要开始下一次跳跃。
// 🐖 使用添加的打印语句 来 帮助理解算法的执行过程
public class Solution_jumpGame_ii_via_greedy {
    public static void main(String[] args) {
        int[] currentSpotToItsMaxJumpDistance = {2, 3, 1, 1, 4};
//        int[] currentSpotToItsMaxJumpDistance = {3, 2, 1, 0, 4}; // 这是一个不满足题意的跳跃方式
        int requiredMinJumps = minJumpAmountToReachEnd(currentSpotToItsMaxJumpDistance);
        System.out.println("按照当前数组跳到数组末尾所需要的最小步数为： " + requiredMinJumps);

    }

    private static int minJumpAmountToReachEnd(int[] currentSpotToItsMaxJumpDistance) {
        int currentJumpMaxReachSpot = 0; // 当前跳跃所能够到达的最远位置
        int nextJumpMaxReachSpot = 0; // 通过下一次跳跃所能到达的最远位置
        int requiredMinJumpSteps = 0; // 到达终点位置所需要的跳跃数量

        // 🐖 作为一个动态的过程，以下两个if发生的时机并不相同
        for (int currentCursorSpot = 0; currentCursorSpot < currentSpotToItsMaxJumpDistance.length - 1; currentCursorSpot++) {
            /* #1 获取到 由当前位置 经过一步跳跃所能跳出的最远距离 */
            int maxJumpDistanceOfCurrentSpot = currentSpotToItsMaxJumpDistance[currentCursorSpot];

            /* #2 计算 从当前跳跃所能到达的各个位置上开始，再经过一次跳跃(下一次跳跃)所能到达的最大位置👇 */
            // 如果 (当前位置 + 由此位置所能跳出的最远距离) 大于 当前记录的"下一次跳跃所能到达的最大位置",
            // 说明 在所有“当前跳跃所能够跳到的位置“中，我们找到了一个 ”更好的起跳位置“（因为可以跳得更远）
            if (currentCursorSpot + maxJumpDistanceOfCurrentSpot > nextJumpMaxReachSpot) {
                // 则：使用 它 来 更新 “下一次跳跃所能到达的最大位置”
                nextJumpMaxReachSpot = currentCursorSpot + maxJumpDistanceOfCurrentSpot;
            }

            /* #3 更新 ”当前所能到达的最大位置“ */
            // 如果 随着当前位置的推进，当前游标位置 已经到达 “当前跳跃所能到达的最远位置”，说明 需要开始新的跳跃了，则：
            if (currentCursorSpot == currentJumpMaxReachSpot) {
                // 把 跳跃的步数+1 来 执行跳跃
                requiredMinJumpSteps++;
                // 使用 当前“下一次跳跃所能到达的最远位置” 来 更新 “当前跳跃所能到达的最远位置” - 为下一轮跳跃做准备
                currentJumpMaxReachSpot = nextJumpMaxReachSpot;
            }
        }

        // 返回 最终的跳跃步数
        return requiredMinJumpSteps;
    }
}
// 思路：利用最远可达距离来做
// 这种算法如果提供了一个不正确的数组（根本到达不了终点位置），那么方法就会给出错误的结果
