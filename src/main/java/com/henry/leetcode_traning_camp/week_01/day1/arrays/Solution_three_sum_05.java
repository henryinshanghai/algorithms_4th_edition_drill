package com.henry.leetcode_traning_camp.week_01.day1.arrays;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Solution_three_sum_05 {
    public static void main(String[] args) {
        // 准备数组...
        int[] nums = {-1, 0, 1, 2, -1, -4};

        // 获取到数组中所有满足条件的(a,b,c)元组的集合
        List<List<Integer>> res = threeSum(nums);

        // 遍历集合中的每一个元组，并打印元组中的数字
        for (int i = 0; i < res.size(); i++) {
            System.out.println("满足（a+b+c）的三个数字为： " + res.get(i).get(0) + "," +
                    res.get(i).get(1) + "," + res.get(i).get(2));
        }
    }

    private static List<List<Integer>> threeSum(int[] nums) {
        // 0 鲁棒性代码
        List<List<Integer>> res = new LinkedList<>();
        if(nums == null || nums.length < 3) return res;

        // 1 对数组元素进行排序
        Arrays.sort(nums);

        // 2 定指针 + 两个动指针的方式
        for (int anchor = 0; anchor < nums.length - 2; anchor++) {
            // 对特殊情况进行处理
            if(nums[anchor] > 0) break;
            // 当 k > 0且nums[k] == nums[k - 1]时即跳过此元素nums[k]：
            // 因为已经将 nums[k - 1] 的所有组合加入到结果中，本次双指针搜索只会得到重复组合。
            if(anchor > 0 && nums[anchor] == nums[anchor - 1]) continue; // 直到遇到新的元素

            // 准备两个动指针
            int front = anchor + 1;
            int back = nums.length - 1;

            // 在子数组中查找满足条件的(b+c)
            while (front < back) {
                int sum = nums[anchor] + nums[front] + nums[back];

                if (sum == 0) {
                    res.add(Arrays.asList(nums[anchor], nums[front], nums[back]));

                    // expr：这里的两个并列条件
                    while(front < back && nums[front] == nums[front+1]) front++;

                    while(front < back && nums[back] == nums[back-1]) back--;

                    front++;
                    back--;
                } else if (sum < 0) {
                    // 跳过重复出现的元素，到达一个新的元素
                    // 这种写法其实性能差了一些
//                    while(front < back && nums[front] == nums[++front]); // 执行++操作，直到相邻的两个元素不再相等
                    front++;
                } else {
//                    while(front < back && nums[back] == nums[--back]);
                    back--;
                }
            }
        }

        return res;
    }
}
