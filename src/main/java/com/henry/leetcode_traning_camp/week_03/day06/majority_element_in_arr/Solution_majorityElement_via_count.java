package com.henry.leetcode_traning_camp.week_03.day06.majority_element_in_arr;

import java.util.HashMap;
import java.util.Map;

// 验证：可以使用 对元素序列中的元素的出现次数进行计数 的方式 来 找到元素序列中所存在的”出现次数超过一半的元素“(题设其必然存在)
// 🐖 对于统计出现次数的场景 getOrDefault()方法非常好用，它能够在元素不存在时，为元素设置其出现次数为0
public class Solution_majorityElement_via_count {
    public static void main(String[] args) {
        int[] itemSequence = {1, 1, 1, 2, 2, 2, 1, 5, 5, 5, 5, 5, 5, 5, 5};
        int resultItem = majorityElementOf(itemSequence);
        System.out.println("数组中出现次数的最多元素是： " + resultItem);
    }

    private static int majorityElementOf(int[] numSequence) {
        Map<Integer, Integer> numToItsCounterMap = new HashMap<>();

        // 对于元素序列中的当前元素...
        for (int currentNum : numSequence) {
            // #1 使用map 来 对其出现的次数进行计数
            numToItsCounterMap.put(currentNum, numToItsCounterMap.getOrDefault(currentNum, 0) + 1);

            // #2 如果“当前元素”的计数 超过了n/2，说明 它就是元素序列中的”出现次数最多的元素“，则：
            if (numToItsCounterMap.get(currentNum) > numSequence.length / 2) {
                // 返回该元素
                return currentNum;
            }
        }

        // 返回-1 来 表示元素序列中不存在有“出现次数超过一半的数字”
        return -1;
    }
}
