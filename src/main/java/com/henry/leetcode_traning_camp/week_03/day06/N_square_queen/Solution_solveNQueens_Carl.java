package com.henry.leetcode_traning_camp.week_03.day06.N_square_queen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 验证：可以使用 选择当前行的一个位置 + 递归得到剩余的棋子放置方案 + 回溯所选择的 当前行位置 的方式 来 生成N皇后问题的所有方案
// 🐖 在一个位置上放置皇后之前，需要先判断此位置上放置皇后是否合法👇
// #0 当前位置的同行中是否存在皇后(按照生成规则 不可能) #1 当前位置的同列中是否存在皇后；#2 当前位置的左上角是否存在皇后； #3 当前位置的右上角是否存在皇后
public class Solution_solveNQueens_Carl {

    public static void main(String[] args) {
        int boardSize = 4;
        List<List<String>> boardLayoutResult = getNQueensLegitLayouts(boardSize);
        print(boardLayoutResult);
    }

    private static void print(List<List<String>> boardResults) {
        int legitBoardLayoutCount = 0;
        for (List<String> currentBoardResult : boardResults) {
            // 当前的棋盘结果
            System.out.println("合法的棋盘结果" + (++legitBoardLayoutCount) + "👇");
            for (String currentRow : currentBoardResult) {
                System.out.println(currentRow);
            }

            System.out.println();
        }

    }

    private static List<List<String>> validChessBoardLayouts = new ArrayList<>();

    public static List<List<String>> getNQueensLegitLayouts(int boardSize) {
        // #1 初始化空的棋盘
        char[][] chessboard = initEmptyChessboard(boardSize);

        // #2 使用回溯 来 找到所有有效的棋盘放置方案
        generateLegitChessLayoutsBasedOn(chessboard, boardSize, 0);

        // #3 返回 所有的棋盘放置方案
        return validChessBoardLayouts;
    }

    // 初始化 空的棋盘
    private static char[][] initEmptyChessboard(int boardSize) {
        char[][] chessboard = new char[boardSize][boardSize];

        for (char[] currentRow : chessboard) {
            // 把每一行 都用.来填满
            Arrays.fill(currentRow, '.');
        }

        return chessboard;
    }

    // #0 递归方法的作用：在指定的棋盘上，从当前行开始，得到一个有效的“皇后放置方案”。
    public static void generateLegitChessLayoutsBasedOn(char[][] chessboard, int boardSize, int currentRow) {
        // #1 递归终结条件
        // 如果当前行 == 棋盘的尺寸，说明 棋盘最后一行的皇后也已经被有效放置(得到了一个有效的皇后放置方案)，则：
        if (currentRow == boardSize) {
            // 把得到的“皇后放置方案”添加到结果集合中
            // 🐖 由于结果集合是List<String>类型，而“皇后放置方案”是一个char[][]类型，所以这里需要手动做转换
            validChessBoardLayouts.add(Array2List(chessboard));
            // 返回到上一级 以 继续尝试其他可能的方案
            return;
        }

        // 对于当前行的每一列...
        for (int currentColumn = 0; currentColumn < boardSize; ++currentColumn) {
            // 如果 在当前位置(currentRow, currentColumn)放置皇后之后不会违反对皇后的约束，说明这是一个有效的皇后布局，则：
            if (isValidLayoutAfterPlaceQueenOn(currentRow, currentColumn, boardSize, chessboard)) {
                // 把 皇后棋子 放置在当前位置上
                chessboard[currentRow][currentColumn] = 'Q';
                // Ⅱ 在“剩下的行”（规模更小的子问题）中，继续选择有效的位置放置皇后 来 得到最终的皇后棋子放置方案
                generateLegitChessLayoutsBasedOn(chessboard, boardSize, currentRow + 1);
                // 回溯 当前位置上的皇后棋子，以便 判断下一个位置能够得到一个有效的“皇后放置方案”
                chessboard[currentRow][currentColumn] = '.';
            }
        }

    }


    public static List Array2List(char[][] chessboard) {
        List<String> allRowsInList = new ArrayList<>();

        for (char[] currentRow : chessboard) {
            allRowsInList.add(String.copyValueOf(currentRow));
        }
        return allRowsInList;
    }


    public static boolean isValidLayoutAfterPlaceQueenOn(int providedRow, int providedColumn, int boardSize, char[][] chessboard) {
        // 检查“同列约束” - 同列的皇后会相互攻击
        if (existBreachOnColumn(providedRow, providedColumn, chessboard)) return false;

        if (existBreachDiagonally(providedRow, providedColumn, boardSize, chessboard)) return false;

        return true;
    }

    private static boolean existBreachDiagonally(int providedRow, int providedColumn, int boardSize, char[][] chessboard) {
        // 检查“45度对角线约束” - 出现在对角位置(情形#1)上的皇后会相互攻击
        if (existBreachOnUpperLeft(providedRow, providedColumn, chessboard)) return true;

        // 检查“135度对角线约束” - 出现在对角位置(情形#2)上的皇后会相互攻击
        if (existBreachOnUpperRight(providedRow, providedColumn, boardSize, chessboard)) return true;
        return false;
    }

    private static boolean existBreachOnUpperRight(int providedRow, int providedColumn, int boardSize, char[][] chessboard) {
        for (int precedingRow = providedRow - 1, followingColumn = providedColumn + 1;
             precedingRow >= 0 && followingColumn <= boardSize - 1;
             precedingRow--, followingColumn++) {
            // 如果135度对角线的前一个位置上 是皇后棋子，说明 在此位置上放置皇后 违反了约束，则：
            if (chessboard[precedingRow][followingColumn] == 'Q') {
                // 返回false 表示 此位置上不能用来放置皇后
                return true;
            }
        }
        return false;
    }

    private static boolean existBreachOnUpperLeft(int providedRow, int providedColumn, char[][] chessboard) {
        for (int precedingRow = providedRow - 1, precedingColumn = providedColumn - 1;
             precedingRow >= 0 && precedingColumn >= 0; precedingRow--, precedingColumn--) {
            // 如果45度对角线的前一个位置上是一个皇后棋子，说明 违反了约束，则：
            if (chessboard[precedingRow][precedingColumn] == 'Q') {
                // 返回false 表示 此位置上不能用来放置皇后
                return true;
            }
        }
        return false;
    }

    private static boolean existBreachOnColumn(int providedRow, int providedColumn, char[][] chessboard) {
        for (int precedingRow = 0; precedingRow < providedRow; precedingRow++) { // 相当于剪枝
            // 如果 先前的某一行相同列的位置上是皇后，说明 违反了约束，则：
            if (chessboard[precedingRow][providedColumn] == 'Q') {
                // 返回false 表示 此位置上不能用来放置皇后
                return true;
            }
        }

        return false;
    }
}
