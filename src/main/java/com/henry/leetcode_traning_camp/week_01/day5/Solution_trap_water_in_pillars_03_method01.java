package com.henry.leetcode_traning_camp.week_01.day5;

import java.util.Stack;

public class Solution_trap_water_in_pillars_03_method01 {
    public static void main(String[] args) {
        int[] arr = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};

        int trappedWater = trap(arr);

        System.out.println("柱状图所能容纳的水量为： " + trappedWater); // expect: 6
    }

    /*
        // 0 鲁棒性代码
        // 1 准备一个栈，用作单调递减栈（monotone stack）	用于记录数组元素的左右边界
        // 2 准备一个int类型的变量，表示存水量
        // 3 准备一个int类型的指针，表示索引指针
        // 4 准备一个循环
            作用：4-1 尝试把当前数组元素的索引添加到单调栈中；
                  4-2 根据单调栈的栈顶索引所对应的元素 与 当前数组元素之间的关系来计算water存水量

            // Ⅰ 如果栈为空 或者 当前i索引的数组元素 小于等于 单调栈的栈顶数值所索引的数组元素

            // Ⅱ 否则（栈不为空 && i索引的数组元素 大于 单调栈的栈顶数值所索引的数组元素）,说明把索引i入栈会破坏单调递减栈的单调性，则：
                // 计算当前数组元素/柱子的存水量并累加到总雨水量中

     */
    private static int trap(int[] arr) {
        // 0 鲁棒性代码
        if (arr == null || arr.length < 3) return 0;

        // 1 准备一个栈
        Stack<Integer> stack = new Stack<>();

        // 2
        int water = 0;
        int i = 0;

        // 4
        while (i < arr.length) {
            if (stack.isEmpty() || arr[i] <= arr[stack.peek()]) {
                stack.push(i);
                i++;
            } else {
                // calculate the water
                int curr = stack.pop();

                if (!stack.isEmpty()) {
                    int minBoundary = Math.min(arr[stack.peek()], arr[i]);
                    water += (minBoundary - arr[curr]) * (i - stack.peek() - 1);
                }
            }
        }

        return water;
    }
}