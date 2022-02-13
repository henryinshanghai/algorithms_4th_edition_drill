package com.henry.leetcode_traning_camp.week_03.day06;

import java.util.ArrayList;
import java.util.List;

public class Solution_solveNQueens_03_method01_backToBack {
    public static void main(String[] args) {
        int size = 4;
        List<List<Integer>> res = NQueens(size);

        for (List<Integer> curr : res) {
            System.out.println(curr);
        }
    }

    private static List<List<Integer>> NQueens(int n) {
        List<List<Integer>> res = new ArrayList<>();
        solveNQueens(n, 0, new ArrayList<Integer>(), res);
        return res;
    }

    private static void solveNQueens(int n, int row, List<Integer> colPlacements, List<List<Integer>> res) {
        // our goal
        if (row == n) {
            res.add(new ArrayList<>(colPlacements));
        } else {
            // traverse all the choices
            for (int col = 0; col < n; col++) {
                // make a choice
                colPlacements.add(col);

                // certain constraints
                if (isValid(colPlacements)) {
                    solveNQueens(n, row+1, colPlacements, res);
                }
                colPlacements.remove(colPlacements.size() - 1);
            }
        }
    }

    private static boolean isValid(List<Integer> colPlacements) {
        int rowId = colPlacements.size() - 1;

        for (int i = 0; i < rowId; i++) {
            /* 最后添加进来的皇后所在的列位置 与 先前摆放的皇后的位置 会不会引起攻击？ */
            // 计算(当前皇后所在的列, 最后添加的皇后所在的列)差值的绝对值
            int diff = Math.abs(colPlacements.get(i) - colPlacements.get(rowId));
            // 如果结果为0 说明最后添加的皇后与当前皇后在相同的列，则：会产生攻击，返回false
            // 如果结果为(rowId - i) aka 行数间的差值，说明 最后添加的皇后与当前皇后处在对角线的位置上，则：会产生攻击，返回false
            if (diff == 0 || diff == rowId - i) {
                return false;
            }
        }

        return true;
    } // 这种写法得到的结果是：每一行的第X列放置皇后的数组；并不是题目预期的结果
}
