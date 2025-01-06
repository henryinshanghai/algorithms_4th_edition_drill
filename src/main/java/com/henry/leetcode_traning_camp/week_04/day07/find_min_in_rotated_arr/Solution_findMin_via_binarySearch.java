package com.henry.leetcode_traning_camp.week_04.day07.find_min_in_rotated_arr;

import java.util.Arrays;

// 验证：对于在旋转数组中找到最小元素的问题，可以使用二分查找 + 一些情形判断 来 得到最小元素
// 原理：旋转后的数组，其实是两个升序序列；
// 特征：两个升序序列中，会出现一个骤降 - 因此第二个升序序列的首元素 就是最小元素
public class Solution_findMin_via_binarySearch {
    public static void main(String[] args) {
        int[] itemSequence = {4, 5, 6, 7, 9, 1, 2};
        int minItem = getMinItemIn(itemSequence);
        System.out.println(Arrays.toString(itemSequence) + "数组中的最小值为： " + minItem);
    }

    private static int getMinItemIn(int[] itemSequence) {
        /* 〇 corner case */
        if (itemSequence.length == 1) return 0;

        if (itemSequence[0] < itemSequence[itemSequence.length - 1]) return itemSequence.length - 1;

        /* Ⅰ 初始化BS需要用到的一些变量/指针 */
        int leftBar = 0, rightBar = itemSequence.length - 1;
        int middle_position = 0;

        /* Ⅱ 准备循环，找到原始数组中出现的相邻降序对 */
        while (leftBar <= rightBar) {
            // 1 计算当前区间的中间位置
            middle_position = leftBar + (rightBar - leftBar) / 2;

            // 如果出现了降序对，说明 找到了原始数组中的最小元素
            if (itemSequence[middle_position] > itemSequence[middle_position + 1]) {
                // 则：返回 数组在此降序位置上的元素
                return itemSequence[middle_position + 1];
            } else if (itemSequence[leftBar] <= itemSequence[middle_position]) { // 如果 中间位置上的元素 相比于 左边界上的元素更大，
                // 说明 目标值在右半区间中，则：更新 左边界
                leftBar = middle_position + 1;
            } else {
                // 否则，说明 目标值在右半区间中，则：更新 右边界
                rightBar = middle_position - 1;
            }
        }

        /* Ⅲ 返回一个标识，表示查找失败 */
        return 0;

    }
}
