package com.henry.leetcode_traning_camp.week_03.day04;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Solution_findCombineMeetCertainNeed_01_method02_backtrack_cutBranch {
    public static void main(String[] args) {
        int n = 4;
        int k = 2;

        List<List<Integer>> res = combine(n, k);

        System.out.println(res);
    }

    private static List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> res = new ArrayList<>();

        if (k <= 0 || k > n) {
            return res;
        }

        Deque<Integer> path = new LinkedList<>();

        dfs(n, k, 1, path, res);

        return res;
    }

    private static void dfs(int n, int k, int anchorItem, Deque<Integer> path, List<List<Integer>> res) {
        if (path.size() == k) {
            res.add(new ArrayList<>(path));
            return;
        }

        for (int i = anchorItem; i <= n - (k - path.size()) + 1; i++) { // 这是一个非常奇技淫巧的剪枝操作
            path.addLast(i);
            dfs(n, k, i+1, path, res);
            path.removeLast();
        }
    }
}
