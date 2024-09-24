package com.henry.leetcode_traning_camp.week_02.day3.NthThree.sum_to_target;

import java.util.HashMap;
import java.util.Map;

// 验证：对于 从整数数组中找到加和结果为targetNum的两个数的下标 这样的任务，可以
// 使用map 来 #1 记录num -> spot的映射； #2 判断 targetNum-currentNum 在map中是否已经存在；
public class Solution_twoSum {
    public static void main(String[] args) {
        int[] numSequence = {2, 3, 11, 15};
        int target = 13;

        int[] legitPairIndex = twoSum(numSequence, target);
        System.out.println("满足题设的两个元素的下标为： " + legitPairIndex[0] + " & " + legitPairIndex[1]);
    }

    private static int[] twoSum(int[] numberSequence, int targetNum) {
        if (numberSequence == null || numberSequence.length == 1)
            return new int[2];

        // 准备一个哈希表对象
        Map<Integer, Integer> numberToItsSpotMap = new HashMap<>();

        // 准备一个循环
        for (int currentNumSpot = 0; currentNumSpot < numberSequence.length; currentNumSpot++) {
            int currentNumber = numberSequence[currentNumSpot];

            // 如果map中已经包含有 差值的key，说明 成功找到了满足条件的两个元素，则：
            int wantedNum = targetNum - currentNumber;
            if (numberToItsSpotMap.containsKey(wantedNum)) {
                // 使用两个元素的spot 组成数组返回
                Integer wantedNumSpot = numberToItsSpotMap.get(wantedNum);
                return new int[]{currentNumSpot, wantedNumSpot};
            } else { // 如果没有包含 差值的key，则：把当前元素 -> 当前元素的位置 添加到map中
                numberToItsSpotMap.put(currentNumber, currentNumSpot);
            }
        }

        return new int[2];
    }
}

/*
expr:
    1 对于数组来说，使用索引可以方便地获取到对应的值。所以使用哈希对象来存储值 -> 索引的映射关系很好用
    2 根据存储位置的不同，可以使用对应的API：containsKey(certainKey)、containsValue(certainValue)
    3 先判断，再添加的套路对于哈希表无往而不利😄
 */
