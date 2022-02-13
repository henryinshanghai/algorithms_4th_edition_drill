package com.henry.leetcode_traning_camp.week_01.day5;

public class Solution_trap_water_in_pillars_03_method02 {
    public static void main(String[] args) {
        int[] arr = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};

        int trappedWater = trap(arr);
        System.out.println("柱状图能够存水的量为： " + trappedWater); // 6

    }

    private static int trap(int[] height) {
        if (height == null || height.length < 3) return 0;

        // 1 找到最高的柱子
        int peak_index = 0;
        for (int i = 1; i < height.length; i++) {
            if (height[i] > height[peak_index]) {
                peak_index = i;
            }
        }

        // 2 累计最高柱子左侧部分的积水量
        // 准备一个指针
        int leftMostBar = 0;
        int water = 0;

        for (int i = 0; i < peak_index; i++) {
            if (height[i] > height[leftMostBar]) { // expr2: 既然xxxMostBar表示的是柱子的高度，这里就应该直接拿来比较————保持统一即可
                leftMostBar = i; // expr1: 这边应该绑定柱子的高度
            } else {
                water += (height[leftMostBar] - height[i]);
            }
        }

        // 3 累计最高柱子右侧部分的积水量
        int rightMostBar = 0;
        for (int i = height.length - 1; i > peak_index; i--) { // 确定循环次数
            if (height[i] > height[rightMostBar]) {
                rightMostBar = i;
            } else {
                water += (height[rightMostBar] - height[i]);
            }
        }

        return water;
    }
}
