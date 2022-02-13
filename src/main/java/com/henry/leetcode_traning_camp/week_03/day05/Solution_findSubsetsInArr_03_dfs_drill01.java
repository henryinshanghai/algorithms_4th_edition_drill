package com.henry.leetcode_traning_camp.week_03.day05;

import java.util.ArrayList;
import java.util.List;

public class Solution_findSubsetsInArr_03_dfs_drill01 {
    public static void main(String[] args) {
        int[] nums = {1, 2, 3};
        List<List<Integer>> res = subSets(nums);
        System.out.println(res);
    }

    private static List<List<Integer>> subSets(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        int len = nums.length;
        if (len == 0) {
            return res;
        }

        dfs(res, new ArrayList<>(), 0, nums);

        return res;
    }

    private static void dfs(List<List<Integer>> res, List<Integer> currSet, int p, int[] nums) {
        if (currSet.size() > nums.length) {
            return;
        }

        res.add(new ArrayList<>(currSet));

        for (int i = p; i < nums.length; i++) {
            currSet.add(nums[i]);

            dfs(res, currSet, i + 1, nums); // 这里新的p = i+1 而不是p+1 这会导致当前分叉产生完全不同的结果

            currSet.remove(currSet.size() - 1);
        }
    }
}
