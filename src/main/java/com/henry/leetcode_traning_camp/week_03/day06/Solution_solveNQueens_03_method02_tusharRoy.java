package com.henry.leetcode_traning_camp.week_03.day06;

import java.util.List;

public class Solution_solveNQueens_03_method02_tusharRoy {
    public static void main(String[] args) {
        int n = 4;
        Position[] res = solveQueenOneSolution(n);
        for (int i = 0; i < res.length; i++) {
            System.out.print("皇后放在第 " + res[i].row + "行，第" + res[i].col + "列");
            System.out.println();
        }
    }

    // 准备一个内部类，记录放置皇后的行信息与列信息
    static class Position {
        int row, col;

        Position(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    // 方法返回一个Position类型的数组  表示棋盘上皇后的排定结果
    private static Position[] solveQueenOneSolution(int n) {
        // 初始化数组的大小（使用棋盘的大小）
        Position[] positions = new Position[n];

        // 回溯方法返回了一个布尔值？
        boolean hasSolution = solveQueenOneSolutionUtil(n, 0, positions);

        // 如果为true，说明找到了满足条件的摆放方式。则：返回position数组
        if (hasSolution) {
            return positions;
        } else {
            return new Position[0];
        }

    }

    private static boolean solveQueenOneSolutionUtil(int n, int row, Position[] positions) {
        if (n == row) {
            return true;
        }

        int col;

        for (col = 0; col < n; col++) {
            boolean foundSafe = true;

            // check if this row and col is not under attack from any previous queen.
            for (int queen = 0; queen < row; queen++) {
                if (positions[queen].col == col ||
                    positions[queen].row - positions[queen].col == row - col ||
                    positions[queen].row + positions[queen].col == row + col) {
                    foundSafe = false;
                    break;
                }
            }

            if (foundSafe) {
                positions[row] = new Position(row, col);

                if (solveQueenOneSolutionUtil(n, row + 1, positions)) {
                    return true;
                }
            }
        }

        return false;
    }
} // 注： 这种方式不容易理解，这次黑人小哥胜出
// 参考：https://www.youtube.com/watch?v=xouin83ebxE
