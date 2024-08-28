package com.henry.leetcode_traning_camp.week_01.day5.holding_water;

public class Solution_trap_water_in_pillars_via_peakBar {
    public static void main(String[] args) {
        int[] itemSequence = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};

        int trappedWater = trap(itemSequence);
        System.out.println("柱状图能够存水的量为： " + trappedWater); // 6

    }

    private static int trap(int[] barsSpotToItsHeight) {
        if (barsSpotToItsHeight == null || barsSpotToItsHeight.length < 3) return 0;

        // #1 找到最高的柱子的位置
        int peakBarSpot = 0;
        for (int currentSpot = 1; currentSpot < barsSpotToItsHeight.length; currentSpot++) {
            int currentBarsHeight = barsSpotToItsHeight[currentSpot];
            if (currentBarsHeight > barsSpotToItsHeight[peakBarSpot]) {
                peakBarSpot = currentSpot;
            }
        }

        // #2 累计最高柱子左侧部分的柱体 所提供的积水量
        int waterTotalAmount = 0;
        // 准备一个指针
        int leftMostBar = 0;

        for (int currentSpot = 0; currentSpot < peakBarSpot; currentSpot++) {
            int currentBarHeight = barsSpotToItsHeight[currentSpot];
            int highestBarOnLeft = barsSpotToItsHeight[leftMostBar];

            // 如果 当前柱体的高度 比起 当前左侧最高柱体的高度 要大，说明我们在左侧找到了一个更高点，它无法储水 但是可以帮助比它更低的柱体储水
            if (currentBarHeight > highestBarOnLeft) {
                // 则：更新 左侧最高柱体的指针位置
                leftMostBar = currentSpot;
            } else { // 如果 当前柱体的高度 比起 当前左侧最高柱体的高度 要小，说明出现了一个低洼处，能够储水
                // 则：计算出储水量 = 高点高度 - 低洼处高度
                int holdingWaterAmount = highestBarOnLeft - currentBarHeight;
                // 累计到储水总量中
                waterTotalAmount += holdingWaterAmount;
            }
        }

        // #3 累计最高柱子右侧部分的柱体 所提供的积水量
        int rightMostBar = 0;
        for (int backwardsSpot = barsSpotToItsHeight.length - 1; backwardsSpot > peakBarSpot; backwardsSpot--) { // 确定循环次数
            int currentBarHeight = barsSpotToItsHeight[backwardsSpot];
            int highestBarOnRight = barsSpotToItsHeight[rightMostBar];

            // 如果出现了更高的高地
            if (currentBarHeight > highestBarOnRight) {
                // 更新指向高地的指针的位置
                rightMostBar = backwardsSpot;
            } else { // 如果出现了低洼
                // 计算储水量
                int holdingWaterAmount = highestBarOnRight - currentBarHeight;
                // 累计到储水总量中
                waterTotalAmount += holdingWaterAmount;
            }
        }

        // 返回最终的储水总量
        return waterTotalAmount;
    }
}
