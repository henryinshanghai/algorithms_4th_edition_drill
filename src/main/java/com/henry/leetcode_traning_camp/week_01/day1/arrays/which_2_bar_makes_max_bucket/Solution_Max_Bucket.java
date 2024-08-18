package com.henry.leetcode_traning_camp.week_01.day1.arrays.which_2_bar_makes_max_bucket;

// 验证：可以使用左右指针的方式 来 得到数组中两个元素值所能围成的最大面积的结果
public class Solution_Max_Bucket {
    public static void main(String[] args) {
        int[] heights = new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7};
        int maxArea = maxArea(heights);
        System.out.println("数组元素所能围成的面积的最大值为： " + maxArea);
    }

    private static int maxArea(int[] heights) {
        // 准备两个指针：lefty初始化指向左边界，righty初始化指向右边界
        int leftBar = 0;
        int rightBar = heights.length - 1;

        // 准备一个sMax的整数
        int maxArea = 0;

        // 准备一个循环，用来更新sMax与指针
        while(leftBar < rightBar){
            int currentWidth = rightBar - leftBar;

            if (heights[leftBar] < heights[rightBar]) {
                int currentValidHeight = heights[leftBar];
                maxArea = Math.max(maxArea, currentWidth * currentValidHeight);

                leftBar++;
            } else {
                int currentValidHeight = heights[rightBar];
                maxArea = Math.max(maxArea, currentWidth * currentValidHeight);

                rightBar--;
            }
        }

        return maxArea;
    }
}
