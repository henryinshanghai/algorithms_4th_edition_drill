package com.henry.leetcode_traning_camp.week_01.day2;

import java.util.ArrayDeque;
import java.util.Deque;

public class Solution_biggest_square_03 {
    public static void main(String[] args) {
        int[] heights = {2,1,5,6,2,3};

        int Smax = biggestRectangleArea(heights);

        System.out.println("此柱状图所能围成的最大矩形面积为： " + Smax);
    }

    // 作用：计算柱状图中所能勾勒出来的矩形的最大面积；
    private static int biggestRectangleArea(int[] heights) {
        // 为了简化操作：准备一个新的数组，数组在原始数组左右都添加了元素0；
        int[] tmp = new int[heights.length + 2];

        // 拷贝原始数组到目标数组
        System.arraycopy(heights, 0, tmp, 1, heights.length);

        // 准备一个栈 作为单调栈（单调栈是不是一种数据结构 还是说只是栈的一种特殊用法罢了）
        // 单调栈就是 栈内元素单调递增或者单调递减 的栈，单调栈只能在栈顶操作。
        Deque<Integer> stack = new ArrayDeque<>();

        // 准备一个int类型的变量 用于记录围成的矩形的面积大小
        int area = 0;

        // 准备一个循环     遍历数组中的每一个柱体，以...
        for (int i = 0; i < tmp.length; i++) {
            // 对栈中柱体来说，栈中的上一个柱体就是其「左边第一个小于自身的柱体」；
            // 边界意味着不能借墙，aka 此时就能计算此棒子所能提供的Smax

            // 若当前柱体 i 的高度 小于 栈顶柱体的高度，说明 i 是栈顶柱体的「右边第一个小于栈顶柱体的柱体」 aka 当前柱体的右边界
            // 因此以 栈顶柱体 为高的矩形的左右宽度边界就确定了，可以计算面积🌶️ ～
            while (!stack.isEmpty() && tmp[i] < tmp[stack.peek()]) { // 栈顶柱体的右边界找到了（左边界就是上一个柱体）
                // 计算当前柱体所能提供的矩形的面积
                int h = tmp[stack.pop()]; // 当前柱体的高度h
                area = Math.max(area, (i - stack.peek() - 1) * h);   // 计算当前柱体所能围成的最大面积 S = h * (right_bound -
                // left_bound - 1)
            }

            // 栈中需要处理的栈顶元素都处理完成后，会继续把当前柱体的索引入栈————入栈后，栈中的元素从下往上是递增的 aka 栈顶元素的右边界未知（等待破局者）
            stack.push(i);
        }

        return area;
    } // 时间复杂度为O(N)
}
