package com.henry.leetcode_traning_camp.week_02.day4.aboutHeap;

import java.util.Arrays;

public class Solution_getLeastNumbers_01_method01_sortAndPick {
    public static void main(String[] args) {
//        int[] originalArr = {3,2,1};
        int[] originalArr = {0,1,2,1};

        int k = 2;

        int[] leastKNumbers = getLeastNumbers(originalArr, k);

        for (int i = 0; i < leastKNumbers.length; i++) {
            System.out.print(leastKNumbers[i] + ", ");
        }
    }

    /**
     * 获取到指定数组中最小的k个数字
     * @param originalArr
     * @return
     */
    private static int[] getLeastNumbers(int[] originalArr, int k) {
        // 准备数组
        int[] leastKNumber = new int[k];
        
        // 排序
        Arrays.sort(originalArr);
        
        // 绑定值
        System.arraycopy(originalArr, 0, leastKNumber, 0, k);

        return leastKNumber;
    }
}
