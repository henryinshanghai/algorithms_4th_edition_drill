package com.henry.leetcode_traning_camp.week_01.day2;

import edu.princeton.cs.algs4.In;

import java.util.ArrayDeque;
import java.util.Deque;

public class Solution_biggest_square_03_drill01 {
    public static void main(String[] args) {
        int[] heights = {2,1,5,6,2,3};

        int Smax = biggestRectangleArea(heights);

        System.out.println("此柱状图所能围成的最大矩形面积为： " + Smax);
    }

    private static int biggestRectangleArea(int[] heights) {
        // 准备一个更容易处理的数组
        int[] tmp = new int[heights.length + 2];
        System.arraycopy(heights, 0, tmp, 1, heights.length);

        // 准备一个栈    作为单调栈使用
        // 特征：代码中更常使用deque对象来最为栈结构
        Deque<Integer> stack =  new ArrayDeque<>();

        // 准备一个int变量    用于存储面积的值
        int area = 0;

        // 准备一个循环，遍历柱状图中的每一个柱子
        // 1 计算当前柱子所能扩展出的矩形面积；  2 把合适的柱子入栈
        for (int i = 0; i < heights.length; i++) {
            // 如果当前柱子小于栈顶的柱子,说明：栈顶柱子的右边界找到了。则...
            while (!stack.isEmpty() && tmp[i] < stack.peek()) {
                // 更新栈顶元素，直到当前柱子不再小于栈顶柱子
                int h = tmp[stack.pop()];
                int width = (i - stack.peek() - 1);
                area = Math.max(area, h * width);
            }

            // 把当前柱子入栈
            stack.push(i);
        } // 循环结束后，area的值 = Max(每个柱子所能扩展出的矩形面积)

        return area;
    }
}
