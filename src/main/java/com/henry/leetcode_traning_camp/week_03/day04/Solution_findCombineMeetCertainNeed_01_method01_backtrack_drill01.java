package com.henry.leetcode_traning_camp.week_03.day04;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Solution_findCombineMeetCertainNeed_01_method01_backtrack_drill01 {
    public static void main(String[] args) {
        int n = 4;
        int k = 2;

        List<List<Integer>> res = combine(n, k);
//        for (int i = 0; i < res.size(); i++) {
//            for (int j = 0; j < res.get(i).size(); j++) {
//                System.out.print(res.get(i).get(j) + "->");
//            }
//
//            System.out.println();
//        }
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

        for (int i = anchorItem; i <= n; i++) {
            // 处理当前元素
            path.addLast(i);
            System.out.println("递归之前 => " + path);
            // 递归调用
            dfs(n, k, i + 1, path, res);

            // 回溯当前元素
            path.removeLast();
            System.out.println("递归之后 => " + path);
        }
    }
}
