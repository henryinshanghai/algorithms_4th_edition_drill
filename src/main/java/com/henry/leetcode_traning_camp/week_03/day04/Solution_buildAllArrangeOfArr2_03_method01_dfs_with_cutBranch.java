package com.henry.leetcode_traning_camp.week_03.day04;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Solution_buildAllArrangeOfArr2_03_method01_dfs_with_cutBranch {
    public static void main(String[] args) {
        int[] nums = new int[]{1,1,2};

        List<List<Integer>> res = permuteUnique(nums);
        System.out.println(res);
    }

    private static List<List<Integer>> permuteUnique(int[] nums) {
        // 准备一个元素为列表的列表 用于存放全排列的集合
        List<List<Integer>> res = new ArrayList<>();

        int len = nums.length;
        if (len == 0) {
            return res;
        }

        boolean[] used = new boolean[len];
        Deque<Integer> path =  new LinkedList<>();

        dfs(nums, len, 0, used, path, res);

        return res;
    }

    private static void dfs(int[] nums, int len, int depth, boolean[] used, Deque<Integer> path, List<List<Integer>> res) {
        if (path.size() == len) {
            res.add(new ArrayList<>(path));
            return;
        }

        for (int i = 0; i < len; i++) {
            if (used[i]) {
                continue;
            }

            // 新增的剪枝条件
            if (i > 0 && nums[i] == nums[i - 1] && used[i - 1] == false) {
                continue;
            }

            path.addLast(nums[i]);
            used[i] = true;

            dfs(nums, len, i + 1, used, path, res); // EXPR：这里的depth参数绑定 depth+1 或者是 i+1都没啥关系
//            dfs(nums, len, depth + 1, used, path, res);

            path.removeLast();
            used[i] = false;
        }
    }
}
