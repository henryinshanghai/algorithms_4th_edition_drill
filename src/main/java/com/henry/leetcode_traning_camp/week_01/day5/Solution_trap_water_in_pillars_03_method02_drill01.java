package com.henry.leetcode_traning_camp.week_01.day5;

public class Solution_trap_water_in_pillars_03_method02_drill01 {
    public static void main(String[] args) {
        int[] height = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};

        int trappedWater = trap(height);

        System.out.println("积水的总量为： " + trappedWater); // 预期：6 实际：
    }

    private static int trap(int[] height) {
        if (height == null || height.length < 3) return 0;

        // 1 计算最高柱子的索引
        int peak_index = 0;
        for (int i = 0; i < height.length; i++) {
            if (height[i] > height[peak_index]) {
                peak_index = i;
            }
        }
        
        // 2 计算左侧部分的积水量
        // 准备一个指针   指向左边界
        int leftMostBar = 0;
        int water = 0;

        for (int i = 0; i < peak_index; i++) {
            if (height[i] > height[leftMostBar]) {
                leftMostBar = i;
            } else {
                water += (height[leftMostBar] - height[i]);
            }
        }

        // 3 计算右侧部分的积水量
        int rightMostBar = height.length - 1;
        for (int i = height.length - 1; i > peak_index ; i--) { // 可以不引用变量的地方就不引用变量，除非使用变量能够使得代码更可读
            if (height[i] > height[rightMostBar]) {
                rightMostBar = i;
            } else {
                water += (height[rightMostBar] - height[i]);
            }
        }

        return water;
    }
}
