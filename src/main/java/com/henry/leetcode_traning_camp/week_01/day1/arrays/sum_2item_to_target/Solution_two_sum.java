package com.henry.leetcode_traning_camp.week_01.day1.arrays.sum_2item_to_target;

// 验证：对于有序数组来说，我们可以通过前进指针和后退指针 来 有效利用数组的有序性
public class Solution_two_sum {
    public static void main(String[] args) {
        // 准备两个参数
        int[] numbers = {2, 7, 11, 15};
        int target = 9;

        int[] itemSpots = which2SpotsItemSumTo(numbers, target);

        System.out.println("数组中相加和为" + target + "的两个数的位置为：" + "(" + itemSpots[0] + "," + itemSpots[1] + ")");
    }

    private static int[] which2SpotsItemSumTo(int[] orderedSequence, int targetValue) {
        // 准备两个指针
        int forwardCursor = 0;
        int backwardCursor = orderedSequence.length - 1;

        // 准备一个循环   在循环中找到满足条件的数字对
        while (forwardCursor < backwardCursor) {
            int num1 = orderedSequence[forwardCursor];
            int num2 = orderedSequence[backwardCursor];
            int sum = num1 + num2;

            if (sum == targetValue) {
                // 使用两个指针 创建出一个数组，作为满足条件的两个元素的位置
                return new int[]{forwardCursor + 1, backwardCursor + 1};
            } else if (sum < targetValue) {
                // 前进指针 前进一格
                forwardCursor++;
            } else {
                // 后退指针 后退一格
                backwardCursor--;
            }
        }

        return new int[]{0, 0};
    }
}
