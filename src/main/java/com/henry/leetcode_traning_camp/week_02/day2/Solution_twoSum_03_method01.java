package com.henry.leetcode_traning_camp.week_02.day2;

import java.util.HashMap;
import java.util.Map;

public class Solution_twoSum_03_method01 {
    public static void main(String[] args) {
        int[] nums = {2, 7, 11, 15};
        int target = 9;

        int[] res = twoSum(nums, target);
        System.out.println("满足题设的两个元素的下标为： " + res[0] + "&" + res[1]);
    }

    private static int[] twoSum(int[] nums, int target) {
        if (nums == null || nums.length == 1) return new int[0];

        // 准备一个哈希表对象
        Map<Integer, Integer> mapItemToIndex = new HashMap<>();

        // 准备一个循环
        for (int i = 0; i < nums.length; i++) {
            if (mapItemToIndex.containsKey(target - nums[i])) {
                return new int[]{i, mapItemToIndex.get(target - nums[i])};
            } else {
                mapItemToIndex.put(nums[i], i);
            }
        }

        return new int[0];
    }
}

/*
expr:
    1 对于数组来说，使用索引可以方便地获取到对应的值。所以使用哈希对象来存储值 -> 索引的映射关系很好用
    2 根据存储位置的不同，可以使用对应的API：containsKey(certainKey)、containsValue(certainValue)
    3 先判断，再添加的套路对于哈希表无往而不利😄
 */
