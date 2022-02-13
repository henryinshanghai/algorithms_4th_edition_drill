package com.henry.leetcode_traning_camp.week_01.day1.arrays;


public class Solution_Max_Bucket_02 {
    public static void main(String[] args) {
        int[] heights = new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7};

        int sMax = maxArea(heights);

        System.out.println("数组元素所能围成的面积的最大值为： " + sMax);
    }

    private static int maxArea(int[] heights) {
        // 准备两个指针：lefty初始化指向左边界，righty初始化指向右边界
        int lefty = 0;
        int righty = heights.length - 1;

        // 准备一个sMax的整数
        int sMax = 0;

        // 准备一个循环，用来更新sMax与指针
        while(lefty < righty){
            if (heights[lefty] < heights[righty]) {
                sMax = Math.max(sMax, (righty - lefty) * heights[lefty++]);
            } else {
                sMax = Math.max(sMax, (righty - lefty) * heights[righty--]);
            }
        }

        return sMax;
    }
}
