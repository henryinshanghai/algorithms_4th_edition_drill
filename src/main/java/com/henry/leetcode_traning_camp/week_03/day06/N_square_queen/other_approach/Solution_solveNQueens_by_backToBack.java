package com.henry.leetcode_traning_camp.week_03.day06.N_square_queen.other_approach;

import java.util.ArrayList;
import java.util.List;

public class Solution_solveNQueens_by_backToBack {
    public static void main(String[] args) {
        int boardSize = 4;
        List<List<Integer>> validLayouts = getValidNQueenLayouts(boardSize);

        for (List<Integer> currentValidLayout : validLayouts) {
            System.out.println(currentValidLayout);
        }
    }

    private static List<List<Integer>> getValidNQueenLayouts(int boardSize) {
        List<List<Integer>> allValidLayouts = new ArrayList<>();
        findAndCollectValidQueensLayouts(boardSize, 0, new ArrayList<Integer>(), allValidLayouts);
        return allValidLayouts;
    }

    private static void findAndCollectValidQueensLayouts(int boardSize, int currentRow,
                                                         List<Integer> currentRowToItsQueenColumn,
                                                         List<List<Integer>> allValidLayouts) {
        // our goal
        if (currentRow == boardSize) {
            allValidLayouts.add(new ArrayList<>(currentRowToItsQueenColumn));
        } else {
            // traverse all the choices
            for (int currentColumn = 0; currentColumn < boardSize; currentColumn++) {
                // make a choice
                currentRowToItsQueenColumn.add(currentColumn);

                // certain constraints
                if (isAValidLayout(currentRowToItsQueenColumn)) {
                    findAndCollectValidQueensLayouts(boardSize, currentRow + 1, currentRowToItsQueenColumn, allValidLayouts);
                }

                // revert the choice
                currentRowToItsQueenColumn.remove(currentRowToItsQueenColumn.size() - 1);
            }
        }
    }

    private static boolean isAValidLayout(List<Integer> currentRowToItsQueenColumn) {
        int totalRowAmount = currentRowToItsQueenColumn.size() - 1;

        for (int currentRow = 0; currentRow < totalRowAmount; currentRow++) {
            /* 最后添加进来的皇后所在的列位置 与 先前摆放的皇后的位置 会不会引起攻击？ */
            // 计算(当前皇后所在的列, 最后添加的皇后所在的列)差值的绝对值
            Integer currentRowsQueensColumn = currentRowToItsQueenColumn.get(currentRow);
            Integer finalRowsQueenColumn = currentRowToItsQueenColumn.get(totalRowAmount);

            int columnDifference = Math.abs(currentRowsQueensColumn - finalRowsQueenColumn);
            // #1 如果结果为0 说明最后添加的皇后与当前皇后在相同的列，则：会产生攻击，返回false
            // #2 如果结果为(totalRowAmount - currentRow) aka 行数间的差值，说明 最后添加的皇后与当前皇后处在对角线的位置上，则：会产生攻击，返回false
            if (sameColumnBreach(columnDifference) || diagonalBreach(totalRowAmount, currentRow, columnDifference)) {
                return false;
            }
        }

        return true;
    } // 这种写法得到的结果是：第X行的皇后应该放在第list[X]的列位置

    // 对角线攻击
    private static boolean diagonalBreach(int totalRowAmount, int currentRow, int diff) {
        return diff == totalRowAmount - currentRow;
    }

    // 相同列攻击
    private static boolean sameColumnBreach(int diff) {
        return diff == 0;
    }
}
