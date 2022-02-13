package com.henry.leetcode_traning_camp.week_03.day06;

import java.util.ArrayList;
import java.util.List;

public class Solution_solveNQueens_03_method01_backToBack_drill01 {
    public static void main(String[] args) {
        int n = 4;
        List<List<Integer>> res = NQueen(n);
        for (List<Integer> curr :
                res) {
            System.out.println(curr);
        }

    }

    private static List<List<Integer>> NQueen(int n) {
        // 准备一个元素为列表的列表
        List<List<Integer>> res = new ArrayList<>();
        // 准备一个列表   用于存放每一行放置皇后的列数
        List<Integer> colPlacements = new ArrayList<>();

        solveNQueens(res, colPlacements, 0, n);

        return res;
    }

    private static void solveNQueens(List<List<Integer>> res, List<Integer> colPlacements, int row, int n) {
        // our goal
        if (row == n) { // colPlacements.size()
            res.add(new ArrayList<>(colPlacements));
        } else {
            // traverse all options
            for (int i = 0; i < n; i++) {
                // make a choice
                colPlacements.add(i);

                // use constraint before another call
                if (isValid(colPlacements)) {
                    solveNQueens(res, colPlacements, row+1, n);
                }

                // remove the choice make earlier
                colPlacements.remove(colPlacements.size() - 1);
            }
        }
    }

    /**
     * 判断当前的列位置数组是不是有效的
     * @param colPlacements
     * @return
     */
    private static boolean isValid(List<Integer> colPlacements) {
        int lastRow = colPlacements.size() - 1;

        for (int currRow = 0; currRow < lastRow; currRow++) {
            int diff = Math.abs(colPlacements.get(currRow) - colPlacements.get(lastRow));

            if (diff == 0 || diff == (lastRow - currRow)) {
                return false;
            }
        }

        return true;
    }
}
