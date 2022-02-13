package com.henry.leetcode_traning_camp.week_01.day2;

import java.util.ArrayDeque;
import java.util.Deque;

public class Solution_biggest_square_03_drill02 {
    public static void main(String[] args) {
        int[] heights = {2,1,5,6,2,3};

        int AreaMax = biggestRectangleArea(heights);

        System.out.println("柱状图中围成的矩形面积的最大值为： " + AreaMax);
    }

    private static int biggestRectangleArea(int[] heights) {
        // Ⅰ 准备一个新数组 以便更好地使用单调栈
        int[] temp = new int[heights.length + 2];
        System.arraycopy(heights, 0, temp, 1, heights.length);

        // Ⅱ 准备一个栈  用作单调栈
        // 作用：升序记录下所有的柱体，用于计算当前柱体所能围成的面积
        Deque<Integer> stack =  new ArrayDeque();

        // 准备一个area整型变量
        int area = 0;

        // 准备一个循环 用于遍历数组中的每一个柱体，求出柱体能够扩展到的最大矩形面积
        // 手段：单调栈 + 栈顶元素（当前柱体）与左右边界之间的关系
        for (int i = 0; i < heights.length; i++) {
            // 如果当前柱体小于栈顶柱体...
            while (!stack.isEmpty() && temp[i] < stack.peek()) {
                int h = temp[stack.pop()]; // 单调栈中存储的是数组索引值
                int width = (i - stack.peek() - 1);
                area = Math.max(area, h * width);
            } // 循环结束后，当前柱体一定大于栈顶柱体了

            // 把当前柱体(的索引)添加到单调栈中
            stack.push(i);
        }

        return area;
    }
}
