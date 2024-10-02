package com.henry.leetcode_traning_camp.week_02.day4.aboutHeap.smallest_K_item.via_sort;

import java.util.Arrays;

// 验证：可以使用“排序 + 从指定位置拷贝指定长度的元素”的方式 来 解决“元素序列中最小的K个元素”的问题
// 特征：由于“原始数组”变成了一个“有序序列”，因此 得到的K个最小元素 也会是有序的
public class Solution_getLeastKNumbers_via_sort {
    public static void main(String[] args) {
        int[] itemSequence = {3, 2, 1, 6, 8, 10, 6, 9, 3, 5, 11};
        int wantedItemAmount = 5;

        int[] leastKNumbers = getLeastNumbers(itemSequence, wantedItemAmount);

        print(leastKNumbers);
    }

    private static void print(int[] leastKNumbers) {
        for (int currentSpot = 0; currentSpot < leastKNumbers.length; currentSpot++) {
            System.out.print(leastKNumbers[currentSpot] + ", ");
        }
    }

    /**
     * 获取到指定数组中最小的k个数字
     *
     * @param itemSequence 原始的元素序列
     * @param wantedAmount 期望得到的元素数量K
     * @return
     */
    private static int[] getLeastNumbers(int[] itemSequence, int wantedAmount) {
        // 为“最小的K个元素”所准备的数组
        int[] leastKItemSequence = new int[wantedAmount];

        // #1 对原始数组进行排序 - itemSequence会变成 有序的元素序列
        Arrays.sort(itemSequence);

        // #2 从“有序序列”的指定位置开始，拷贝 “指定数量”的元素 到“目标数组”的指定位置
        System.arraycopy(itemSequence, 0, leastKItemSequence, 0, wantedAmount);

        // #3 返回“目标数组”
        return leastKItemSequence;
    }
}
