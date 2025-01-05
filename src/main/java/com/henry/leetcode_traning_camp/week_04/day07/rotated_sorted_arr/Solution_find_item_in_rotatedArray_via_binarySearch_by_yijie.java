package com.henry.leetcode_traning_camp.week_04.day07.rotated_sorted_arr;

import java.util.Arrays;

// 验证：对于 在旋转数组中找到目标元素并返回其索引值 的问题，可以 #1 使用二分查找先找到两段递增序列的最大元素的位置； #2 再在两段有序区间中分别进行二分查找。
// 原理：#1 原始有序的数组 旋转后 会得到两段 递增序列； #2 二分查找并不一定需要序列是完全有序的
// 概念：针对两段升序序列的二分查找、简单二分查找
public class Solution_find_item_in_rotatedArray_via_binarySearch_by_yijie {
    public static void main(String[] args) {
        // 题设信息
        int[] itemSequence = {4, 5, 6, 7, 0, 1, 2};
        int targetItem = 2;

        int targetItemsIndex = searchItemIndexIn(itemSequence, targetItem);
        System.out.println("目标元素 " + targetItem + "在数组" + Arrays.toString(itemSequence) + "中的下标位置为： " + targetItemsIndex);
    }

    public static int searchItemIndexIn(int[] itemSequence, int targetItem) {
        if (itemSequence == null || itemSequence.length == 0) {
            return -1;
        }

        // step1 找出原始数组中的最大元素————这个元素就是两个升序区间的交界/旋转点
        int maxItemsIndex = getMaxItemsIndexIn(itemSequence);

        // step2 在升序区间中，执行 “朴素版本的二分查找”
        // 如果 targetItem 在第一个/区间中，说明 需要在此区间内 进行二分查找，
        if (maxItemsIndex >= 0 && targetItem >= itemSequence[0] && targetItem <= itemSequence[maxItemsIndex]) {
            // 则：指定此区间的左右边界 以 进行朴素的二分查找
            return getTargetItemsIndexInRange(itemSequence, 0, maxItemsIndex, targetItem);
        } else { // 否则，说明 targetItem在第二个/区间中，
            // 则：指定此区间的左右边界 以 进行朴素的二分查找
            return getTargetItemsIndexInRange(itemSequence, maxItemsIndex + 1, itemSequence.length - 1, targetItem);
        }

    }

    /**
     * 找到 原始数组（翻转后的）中的最大元素的索引
     *
     * @param itemSequence
     * @return
     */
    private static int getMaxItemsIndexIn(int[] itemSequence) {
        /* 〇 corner case */
        if (itemSequence.length == 1) return 0;

        if (itemSequence[0] < itemSequence[itemSequence.length - 1]) {
            return itemSequence.length - 1;
        }

        /* Ⅰ 初始化BS需要用到的一些变量/指针 */
        int leftBar = 0, rightBar = itemSequence.length - 1;
        int middle_position = 0;

        /* Ⅱ 准备循环，找到原始数组中出现的相邻降序对 */
        while (leftBar <= rightBar) {
            // 1 计算出 当前区间的中间位置
            middle_position = leftBar + (rightBar - leftBar) / 2;

            // 如果 在中间位置上 出现了降序的情况(/ /)，说明 中间位置 刚好就是 元素最大的位置，
            if (itemSequence[middle_position] > itemSequence[middle_position + 1]) {
                // 则：返回 中间位置
                return middle_position;
            } else if (itemSequence[leftBar] <= itemSequence[middle_position]) { // 如果 左边界上的元素 小于 中间位置上的元素，
                // 说明 targetItem在 当前middle_position 之后
                // 则：更新区间的左边界 为 当前middle_position+1
                leftBar = middle_position + 1;
            } else { // 否则，说明 targetItem 在 当前middle_position之前
                // 则：更新区间的右边界 为 当前middle_position-1
                rightBar = middle_position - 1;
            }
        }

        /* Ⅲ 返回一个标识，表示查找失败 */
        return 0;
    }

    /**
     * 这就只是一个典型的二分查找
     * 但是在方法参数列表中指定了查找的区间边界
     *
     * @param itemSequence
     * @param leftBar
     * @param rightBar
     * @param targetItem
     * @return
     */
    public static int getTargetItemsIndexInRange(int[] itemSequence, int leftBar, int rightBar, int targetItem) {
        // 区间为 左闭右闭区间 [leftBar, rightBar]
        while (leftBar <= rightBar) { // EXPR1： <=
            // 1 计算中间位置
            int current_middle_position = leftBar + (rightBar - leftBar) / 2;

            // 2 比较 当前中间位置上的元素 与 targetItem
            // 如果相等，说明 找到了targetItem
            if (itemSequence[current_middle_position] == targetItem) {
                // 则：返回 current_middle_position
                return current_middle_position;
            } else if (itemSequence[current_middle_position] < targetItem) { // 如果中间位置上的元素 相比于 targetItem更小，说明 需要在右区间中继续查找
                // 则：更新区间的左边界
                leftBar = current_middle_position + 1;
            } else { // 否则，说明 需要在当前的左区间中继续查找
                // 则：更新区间的右边界
                rightBar = current_middle_position - 1;
            }
        }

        return -1;
    }
}
