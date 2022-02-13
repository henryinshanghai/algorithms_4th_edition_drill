package com.henry.leetcode_traning_camp.week_01.day5;

import java.util.Stack;

// use the monotone stack to record the left and right boundary, so that you can calculate sth further
public class Solution_trap_water_in_pillars_03_method01_drill01 {
    public static void main(String[] args) {
        int[] arr = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};

        int trappedWater = trap(arr);

        System.out.println("柱状图所能容纳的水量为： " + trappedWater); // expect: 6
    }

    private static int trap(int[] heights) {
        // 0 鲁棒性代码
        if (heights == null || heights.length < 3) return 0;

        // 1 准备一个栈以及需要用到的变量
        Stack<Integer> stack = new Stack<>(); // 特征：1 存储的是索引；   2 栈中的元素单调递减；

        int water = 0;
        int cursor = 0;

        // 2 准备一个循环     作用：1 维护一个单调递减的栈； 2 使用单调栈中的元素来求出当前柱子的存水量
        while (cursor < heights.length) {
            if (stack.isEmpty() || heights[cursor] <= heights[stack.peek()]) {
                stack.push(cursor);
                cursor++;
            } else { // 说明找到了当前柱子的右边界
                int curr = stack.pop();

                if (!stack.isEmpty()) {
                    int minBoundary = Math.min(heights[cursor], heights[stack.peek()]);

//                    water += (minBoundary - heights[curr]) * 1;
                    int width = cursor - stack.peek() - 1;
                    int waterFromCurr = (minBoundary - heights[curr]) * width;
                    water += waterFromCurr;
                }
            }
        }

        return water;
    }
}
