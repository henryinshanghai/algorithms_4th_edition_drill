package com.henry.leetcode_traning_camp.week_03.day04;

import java.util.*;

public class Solution_buildAllArrangeOfArr_02_method01_dfs {
    public static void main(String[] args) {
        int[] nums = new int[]{1, 2, 3};

        List<List<Integer>> res = permute(nums);
        System.out.println(res);
    }

    private static List<List<Integer>> permute(int[] nums) {
        // 准备元素为列表的列表   用于存储每一个全排列结果
        List<List<Integer>> res = new ArrayList<>();

        int len = nums.length;

        if (len == 0) {
            return res;
        }

        // 准备一些个状态变量
        // 1 当前在递归状态树中的层数 这个是一个简单的int变量，直接放在dfs()方法的参数中就可以了
        // 2 序列中的数字是不是已经被使用过来构造全排列了
        boolean[] used = new boolean[len];

        // 用于存储当前得到的全排列结果
        Deque<Integer> path = new ArrayDeque<Integer>();

        // 调用dfs()  向res中添加所有可能的全排列
        dfs(nums, len, 0, used, path, res);

        return res;

    }

    private static void dfs(int[] nums, int len, int depth, boolean[] used, Deque<Integer> path,
                            List<List<Integer>> res) {
        // 1 递归终结条件 构造出了一个完整的全排列
        if (path.size() == len) {
            res.add(new ArrayList<>(path));
            return;
        }

        // 2 遍历序列中的每一个数字
        for (int i = 0; i < len; i++) {
            if (!used[i]) {
                path.addLast(nums[i]);
                used[i] = true;

                dfs(nums, len, i+1, used, path, res); // 这个depth参数到底应该绑定什么值？
//                dfs(nums, len, depth+1, used, path, res); // 这个depth参数到底应该绑定什么值？

                // 还原现场
                path.removeLast();
                used[i] = false;
            }
        }
    }
}
