package com.henry.leetcode_traning_camp.week_01.day5;

import java.util.Stack;

public class Solution_trap_water_in_pillars_03_method01_drill02 {
    public static void main(String[] args) {
        int[] arr = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};

        int trappedWater = trap(arr);
        System.out.println("柱状图能够存水的量为： " + trappedWater); // 6
    }

    private static int trap(int[] heights) {
        if(heights == null || heights.length < 3) return 0;

        Stack<Integer> stack = new Stack<>();
        int water = 0;
        int cursor = 0;

        while (cursor < heights.length) {
            if (stack.isEmpty() || heights[cursor] <= heights[stack.peek()]) {
                stack.push(cursor);
                cursor++;
            } else {
                int curr = stack.pop();

                if (!stack.isEmpty()) {
                    int minBoundary = Math.min(heights[cursor], heights[stack.peek()]);
                    int width = (cursor - stack.peek() - 1);
                    int waterFromCurr = (minBoundary - heights[curr]) * width;

                    water += waterFromCurr;
                }
            }
        }

        return water;
    }
}
