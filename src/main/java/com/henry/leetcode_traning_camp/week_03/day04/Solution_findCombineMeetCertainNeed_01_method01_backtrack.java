package com.henry.leetcode_traning_camp.week_03.day04;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Solution_findCombineMeetCertainNeed_01_method01_backtrack {
    public static void main(String[] args) {
        int n = 4; // 原始集合的大小
        int k = 2; // 满足条件的组合的大小

        List<List<Integer>> res = combine(n, k);
        for (int i = 0; i < res.size(); i++) {
            for (int j = 0; j < res.get(i).size(); j++) {
                System.out.print(res.get(i).get(j) + "->");
            }

            System.out.println();
        }
    }

    private static List<List<Integer>> combine(int n, int k) {
        // 准备一个元素为列表的列表 作用：用于封装最终结果(组合的列表)进行返回
        List<List<Integer>> res = new ArrayList<>();

        // 0
        if (k <= 0 || k > n) {
            return res;
        }

        Deque<Integer> path = new LinkedList<>();

        // 1
        dfs(n, k, 1, path, res);

        return res;
    }

    private static void dfs(int n, int k, int anchorItem, Deque<Integer> path, List<List<Integer>> res) {
        // 1 递归终结条件 & 递归终结时需要做的事情
        if (path.size() == k) {
            res.add(new ArrayList<>(path));
            return;
        }

        // 2
        for (int i = anchorItem; i <= n; i++) {
            // 2-1 处理当前元素
            path.addLast(i);
            // 2-2 递归到下一层 处理子树
            dfs(n, k, i + 1, path, res); // 注意这里的代码时i + 1,而不是anchorItem + 1
            // 2-3 回溯当前层的选择
            path.removeLast();
        }
    }
}
