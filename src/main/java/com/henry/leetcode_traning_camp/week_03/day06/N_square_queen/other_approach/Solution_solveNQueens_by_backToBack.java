package com.henry.leetcode_traning_camp.week_03.day06.N_square_queen.other_approach;

import java.util.ArrayList;
import java.util.List;

// 棋盘放置方案的简单表示：当前行 -> 放置皇后的列 currentRowToItsQueenColumn[]
public class Solution_solveNQueens_by_backToBack {
    public static void main(String[] args) {
        int boardSize = 4;
        List<List<Integer>> validLayouts = getValidNQueenLayouts(boardSize);

        for (List<Integer> currentValidLayout : validLayouts) {
            System.out.println(currentValidLayout);
        }
        /*
            [1, 3, 0, 2]
            [2, 0, 3, 1]
         */
    }

    private static List<List<Integer>> allValidLayouts = new ArrayList<>();

    private static List<List<Integer>> getValidNQueenLayouts(int boardSize) {
        List<Integer> currentRowToItsQueenColumn = new ArrayList<>();
        findAndCollectValidQueensLayouts(boardSize, 0, currentRowToItsQueenColumn);

        return allValidLayouts;
    }


    private static void findAndCollectValidQueensLayouts(int boardSize, int currentRow,
                                                         List<Integer> currentRowToItsQueenColumn) {
        // 如果 当前行数 == 棋盘的尺寸，说明 已经找到了一个 皇后的放置方案，则：
        if (currentRow == boardSize) {
            // 收集此方案 到 方案集合中
            allValidLayouts.add(new ArrayList<>(currentRowToItsQueenColumn));
        } else {
            // 对于棋盘中的每一列...
            for (int currentColumn = 0; currentColumn < boardSize; currentColumn++) {
                // #1 在当前行的该列上放置皇后
                currentRowToItsQueenColumn.add(currentColumn);

                // #2 判断防止皇后之后的棋盘布局是不是有效的，如果是，则：
                if (isAValidLayout(currentRowToItsQueenColumn)) {
                    // 继续尝试放置皇后，得到 有效的皇后放置方案并收集之
                    findAndCollectValidQueensLayouts(boardSize, currentRow + 1, currentRowToItsQueenColumn);
                }

                // #3 回退#1中所放置的皇后，以便继续尝试其他方案
                currentRowToItsQueenColumn.remove(currentRowToItsQueenColumn.size() - 1);
            }
        }
    }

    // 判断当前棋盘上的皇后布局是不是一个“有效的布局”
    // 棋盘上皇后布局的表示手段：当前行 -> 当前行上的皇后的位置
    private static boolean isAValidLayout(List<Integer> currentRowToItsQueenColumn) {
        int totalRowAmount = currentRowToItsQueenColumn.size() - 1;
        // 得到 最后一行的皇后的位置
        Integer finalRowsQueenColumn = currentRowToItsQueenColumn.get(totalRowAmount);

        // 对于棋盘中的每一行...
        /* 查看 最后一行的皇后 与 当前行的皇后 会不会相互攻击？ */
        for (int currentRow = 0; currentRow < totalRowAmount; currentRow++) {
            // 得到 当前行的皇后的位置
            Integer currentRowsQueensColumn = currentRowToItsQueenColumn.get(currentRow);

            // 计算(当前皇后所在的列, 最后添加的皇后所在的列)差值的绝对值
            int columnDifference = Math.abs(currentRowsQueensColumn - finalRowsQueenColumn);
            // #1 如果结果为0 说明最后添加的皇后与当前皇后在相同的列，则：会产生攻击，返回false
            // #2 如果 列之间的差值 = 行数间的差值，说明 最后一行的皇后 与 当前皇后处在对角线的位置上，则：会产生攻击，返回false
            if (sameColumnBreach(columnDifference)
                    || diagonalBreach(totalRowAmount, currentRow, columnDifference)) {
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
