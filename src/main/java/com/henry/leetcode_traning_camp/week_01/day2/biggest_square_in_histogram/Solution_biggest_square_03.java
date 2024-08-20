package com.henry.leetcode_traning_camp.week_01.day2.biggest_square_in_histogram;

import java.util.ArrayDeque;
import java.util.Deque;

// 验证：使用单调栈存储“基准柱体”的方式 能够得到 该“基准柱体”在柱体数组中所能勾勒出的最大面积
// 单调栈的特征：栈中的元素(从栈顶到栈底)是单调递增 / 单调递减
// 单调栈的应用：找到 数组中，基准元素右侧的第一个小于它的元素 与 基准元素左侧的第一个小于它的元素
public class Solution_biggest_square_03 {
    public static void main(String[] args) {
        int[] heights = {2, 1, 5, 6, 2, 3};

        int biggestRectangleArea = biggestRectangleArea(heights);
        System.out.println("此柱状图所能围成的最大矩形面积为： " + biggestRectangleArea);
    }

    // 作用：计算柱状图中所能勾勒出来的矩形的最大面积；
    private static int biggestRectangleArea(int[] heights) {
        // 为了简化操作：准备一个新的数组，数组在原始数组左右都添加了元素0；
        int[] extendHeights = new int[heights.length + 2];

        // 把原始数组的指定起点 指定长度的元素 拷贝到 目标数组的指定起点
        System.arraycopy(heights, 0, extendHeights, 1, heights.length);

        // 准备一个栈 作为单调栈（单调栈不是一种数据结构）
        // 单调栈就是 栈内元素单调递增或者单调递减 的栈，单调栈只能在栈顶操作。
        Deque<Integer> spotStack = new ArrayDeque<>();

        // 准备一个int类型的变量 用于记录围成的矩形的面积大小
        int squareArea = 0;
        // 准备一个循环     遍历数组中的每一个柱体，以...
        for (int currentSpot = 0; currentSpot < extendHeights.length; currentSpot++) {
            // 对栈中柱体来说，栈中的上一个柱体就是其「左边第一个小于自身的柱体」；
            // 边界意味着不能借墙，aka 此时就能计算此棒子所能提供的Smax

            /* 判断当前柱体 是否能够用于 确定“基准柱体”所能勾勒的最大面积 👇 */
            // 若当前柱体 currentSpot 的高度 小于 栈顶柱体的高度，
            // 说明 currentSpot 是栈顶柱体的「右边第一个小于栈顶柱体的柱体」 aka 当前柱体的右边界
            // 因此以 栈顶柱体 为高的矩形的左右宽度边界就确定了，可以计算面积🌶️ ～
            while (!spotStack.isEmpty() && extendHeights[currentSpot] < extendHeights[spotStack.peek()]) { // 栈顶柱体的右边界找到了（左边界就是上一个柱体）
                // 计算当前柱体所能提供的矩形的面积
                Integer determinedBarSpot = spotStack.pop();
                int determinedBarHeight = extendHeights[determinedBarSpot]; // 栈顶柱体的高度

                int firstLessBarOnRight = currentSpot;
                Integer firstLessBarOnLeft = spotStack.peek();
                int areaWidth = firstLessBarOnRight - firstLessBarOnLeft - 1; // 所选区域的宽度

                squareArea = Math.max(squareArea, areaWidth * determinedBarHeight);   // 计算当前柱体所能得到的最大面积
            }

            // 把当前位置 添加到栈中，作为“待确定其所能勾勒的矩形最大面积 的基准元素” - 这个元素等待着遇到 “其右侧的第一个小于它的元素” 来 确定其所能勾勒的矩形最大面积
            spotStack.push(currentSpot);
        }

        return squareArea;
    } // 时间复杂度为O(N)
}
