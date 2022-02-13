package com.henry.leetcode_traning_camp.week_03.day06;

import java.util.*;

public class Solution_solveNQueens_03_method03_liweiwei {
    public static void main(String[] args) {
        int n = 4;
        List<List<String>> res = solveNQueens(n);
        for (int i = 0; i < res.size(); i++) {
            for (int j = 0; j < res.get(i).size(); j++) {
                System.out.println(res.get(i).get(j)); // 这里打印的是一行棋盘
            }

            System.out.println();
        }
    }

    private static int n;
    /**
     * 记录某一列是否放置了皇后
     */
    private static boolean[] col;
    /**
     * 记录主对角线上的单元格是否放置了皇后
     */
    private static boolean[] main;
    /**
     * 记录了副对角线上的单元格是否放置了皇后
     */
    private static boolean[] sub;
    private static List<List<String>> res;

    public static List<List<String>> solveNQueens(int n) {
        res = new ArrayList<>();
        if (n == 0) {
            return res;
        }

        // 设置成员变量，减少参数传递，具体作为方法参数还是作为成员变量，请参考团队开发规范
        Solution_solveNQueens_03_method03_liweiwei.n = n;
        col = new boolean[n];
        main = new boolean[2 * n - 1];
        sub = new boolean[2 * n - 1];
        Deque<Integer> path = new ArrayDeque<>();
        dfs(0, path);
        return res;
    }

    private static void dfs(int row, Deque<Integer> path) {
        if (row == n) {
            // 深度优先遍历到下标为 n，表示 [0.. n - 1] 已经填完，得到了一个结果
            List<String> board = convert2board(path);
            res.add(board);
            return;
        }

        // 针对下标为 row 的每一列，尝试是否可以放置
        for (int j = 0; j < n; j++) {
            if (!col[j] && !main[row - j + n - 1] && !sub[row + j]) {
                path.addLast(j);
                col[j] = true;
                main[row - j + n - 1] = true;
                sub[row + j] = true;


                dfs(row + 1, path);
                sub[row + j] = false;
                main[row - j + n - 1] = false;
                col[j] = false;
                path.removeLast();
            }
        }
    }

    /**
     * 从获取到的有效路径打印出棋盘来
     * @param path 每一行中皇后的有效列位置
     * @return
     */
    private static List<String> convert2board(Deque<Integer> path) {
        List<String> board = new ArrayList<>();
        // 遍历每一行/每一个列位置
        for (Integer num : path) {
            // 准备一个动态字符串
            StringBuilder row = new StringBuilder();
//            row.append(".".repeat(Math.max(0, n)));
            // 先为当前行添加n个'.'字符
            row.append(String.join("", Collections.nCopies(Math.max(0, n), ".")));
            // 然后根据列位置num，把对应的位置的.替换成为Q
            row.replace(num, num + 1, "Q"); // [num, num+1) 左闭右开区间

            // 把当前行的结果存储在字符串列表中
            board.add(row.toString());
        }
        return board;
    }
} // 这种方法同样不是很简洁 但是它打印出了完成的二维数组棋盘 可以学习一下根据路径打印棋盘的方法/代码
