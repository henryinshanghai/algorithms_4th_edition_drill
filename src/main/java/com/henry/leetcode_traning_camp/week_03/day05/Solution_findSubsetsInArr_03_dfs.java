package com.henry.leetcode_traning_camp.week_03.day05;

import java.util.ArrayList;
import java.util.List;

public class Solution_findSubsetsInArr_03_dfs {
    public static void main(String[] args) {
        int[] nums = {1, 2, 3};

        List<List<Integer>> res = subSets(nums);
        System.out.println(res);
    }

    private static List<List<Integer>> subSets(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        int len = nums.length;

        if(len == 0) return res;

        dfs(0, nums, res, new ArrayList<>());

        return res;
    }

    // 这种回溯的算法不是很容易理解
    private static void dfs(int start, int[] nums, List<List<Integer>> res, ArrayList<Integer> currSet) {
        if (currSet.size() > nums.length) {
            return;
        }

        // 把当前子集添加到res中
        res.add(new ArrayList<>(currSet));

        // 从当前子集开始分叉
        for (int i = start; i < nums.length; i++) { // 每次分叉都从当前调用所传入的start索引位置开始————所以能够不重不漏
            // 把当前索引的元素添加到当前子集中
            currSet.add(nums[i]);
            // 递归地调用dfs：找到新的子集，并把找到的子集添加到res中
            dfs(i + 1, nums, res, currSet);
            // 当前索引元素的所有分支完全生成后，则：回溯当前索引元素，然后从下一个元素处开始新的分叉
            currSet.remove(currSet.size() - 1);
        }
    }
}
