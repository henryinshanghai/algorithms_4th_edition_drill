package com.henry.leetcode_traning_camp.week_01.day5.holding_water;

import java.util.Stack;

// 验证：利用单调栈的特性（能够找到目标元素左侧与右侧第一个比它大/小的元素）能够 以纵向切分的思路 来 解决接雨水问题
public class Solution_trap_water_in_pillars_via_monoIncreaseStack {
    public static void main(String[] args) {
        int[] barsHeight = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};

        int trappedWater = trap(barsHeight);

        System.out.println("柱状图所能容纳的水量为： " + trappedWater); // expect: 6
    }

    private static int trap(int[] barSpotToItsHeight) {
        // #0 鲁棒性代码
        if (barSpotToItsHeight == null || barSpotToItsHeight.length < 3) return 0;

        // #1 准备一个栈 维护为单调栈（从栈顶到栈底所对应的元素单调递增） 用于 得到栈口元素左侧与右侧的第一个更大的元素
        Stack<Integer> monoStack = new Stack<>();

        // #2 准备动态变量
        int totalWaterVolume = 0; // 储水量
        int currentBarSpot = 0; // 当前柱体位置

        // #3 借助单调栈 来 得到柱体所能储存的水的总量
        while (currentBarSpot < barSpotToItsHeight.length) {
            // 如果当前柱体的高度 小于 栈口元素对应的柱体的高度，说明 当前柱体的位置入栈后 栈中元素对应的柱体，从栈口到栈底 仍旧保持单调递增
            if (monoStack.isEmpty() || barSpotToItsHeight[currentBarSpot] <= barSpotToItsHeight[monoStack.peek()]) {
                // 则把当前柱体的位置 入栈
                monoStack.push(currentBarSpot);
                currentBarSpot++;
            } else { // 否则，说明 当前柱体 比起 栈口位置对应的柱体 更高，则
                // 对于栈口位置对应的柱体来说，它的左边第一个比它高的柱体 与 右边第一个比它高的柱体 都已经找到了。可以计算 此柱体所能带来的储水量了
                // 计算此柱体 与 其左右更高柱体 所围成的凹槽的储水量👇
                int targetBarSpot = monoStack.pop();

                if (!monoStack.isEmpty()) {
                    // 得到 左边第一个更高的柱体 与 右边第一个更高的柱体，并取其中的较小者👇
                    Integer firstHigherBarSpotOnLeft = monoStack.peek();
                    int firstHigherBarHeightOnLeft = barSpotToItsHeight[firstHigherBarSpotOnLeft];
                    int firstHigherBarHeightOnRight = barSpotToItsHeight[currentBarSpot];
                    int minBoundaryHeight = Math.min(firstHigherBarHeightOnLeft, firstHigherBarHeightOnRight);

                    // 计算出容水空间的高度 与 宽度，并进一步计算出 容水的面积/体积
                    int deltaHeight = minBoundaryHeight - barSpotToItsHeight[targetBarSpot];
                    int deltaWidth = currentBarSpot - firstHigherBarSpotOnLeft - 1;
                    int volumeOfTargetBar = deltaHeight * deltaWidth;

                    // 把当前目标柱体所提供的容积 计入到 总容积中
                    totalWaterVolume += volumeOfTargetBar;
                }
            }
        }

        // #4 返回总容积
        return totalWaterVolume;
    }
}