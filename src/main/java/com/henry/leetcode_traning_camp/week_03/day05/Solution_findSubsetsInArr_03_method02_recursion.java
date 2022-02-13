package com.henry.leetcode_traning_camp.week_03.day05;

import java.util.ArrayList;
import java.util.List;

public class Solution_findSubsetsInArr_03_method02_recursion {
    public static void main(String[] args) {
        int[] nums = {1, 2, 3};

        List<List<Integer>> res = subSets(nums);
        System.out.println(res);

    }

    private static List<List<Integer>> subSets(int[] nums) {
        // 预处理：与左括号、右括号排列的问题本质是一样的
        List<List<Integer>> res = new ArrayList<>();
        if(nums == null) return res;

        dfs(res, nums, new ArrayList<Integer>(), 0);
        return res;
    }

    private static void dfs(List<List<Integer>> res, int[] nums, List<Integer> list, int index) {
        // terminator
        // 如果是第 index 层 == nums.length 说明就已经走到递归树的最后一层了（aka 数组的最末尾 aka 得到了满足条件的子集）。
        if (index == nums.length) {
            // 则：把这个子集添加到结果res中
            res.add(new ArrayList<>(list));
            return;
        }

        // 不选这个index表示的数
        dfs(res, nums, list, index + 1);

        // 选择这个index表示的数，则：更新list后再去调用xxx
        list.add(nums[index]);
        dfs(res, nums, list, index + 1);

        // reverse the current state    为什么需要reverse？因为list变量是一个递归函数中的变量，它的值的变化会影响它的上下层调用
        list.remove(list.size() - 1);
    }
} // 问题转化：给定n个格子，每个格子中的元素可选可不选。求所有选择的情况
