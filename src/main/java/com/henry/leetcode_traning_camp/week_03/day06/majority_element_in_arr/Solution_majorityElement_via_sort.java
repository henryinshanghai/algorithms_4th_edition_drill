package com.henry.leetcode_traning_camp.week_03.day06.majority_element_in_arr;

import java.util.Arrays;

// 验证：可以借助 排序 + 获取指定位置上的元素 的手段 来 找到数组中存在的”出现次数超过一半的元素“
// 🐖 排序可以直接使用库函数，它只是辅助步骤
public class Solution_majorityElement_via_sort {
    public static void main(String[] args) {
        int[] itemSequence = {1, 1, 1, 2, 2, 2, 1};
        int resultItem = majorityElement(itemSequence);
        System.out.println("元素序列中的出现次数最多元素是： " + resultItem);
    }

    private static int majorityElement(int[] itemSequence) {
        // 先对数组进行排序
        Arrays.sort(itemSequence);

        // 然后返回排序后的数组中间位置的元素
        return itemSequence[itemSequence.length / 2];
    }
}
