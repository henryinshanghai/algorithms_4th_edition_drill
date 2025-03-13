package com.henry.leetcode_traning_camp.week_06.day06.min_path;

// 🐖 这里从0开始 构建dp[][]数组的元素值
public class Solution_via_dp_by_KevinNaughton {
    public static void main(String[] args) {
        int[][] numberBoard = {
                {1, 3, 1},
                {1, 5, 1},
                {4, 2, 1}
        };

        int pathSum = getMinimumPathSumOf(numberBoard);

        System.out.println("矩阵中从左上角到右下角的最小路径的加和值等于： " + pathSum);

    }

    private static int getMinimumPathSumOf(int[][] numberBoard) {
        if (numberBoard == null || numberBoard.length == 0) {
            return 0;
        }

        // 准备dp[][]数组
        int[][] currentGridToMinPathSumReachIt = new int[numberBoard.length][numberBoard[0].length];

        for (int currentRow = 0; currentRow < currentGridToMinPathSumReachIt.length; currentRow++) {
            for (int currentColumn = 0; currentColumn < currentGridToMinPathSumReachIt[0].length; currentColumn++) {
                // 当前路径的sum 一定会包含 当前方格的value
                currentGridToMinPathSumReachIt[currentRow][currentColumn] += numberBoard[currentRow][currentColumn];

                // 当前路径的sum 除了 当前方格的value外，还可能包含哪些部分呢? 🐖 计算方向：从左往右 × 从上往下
                if (currentRow > 0 && currentColumn > 0) {
                    currentGridToMinPathSumReachIt[currentRow][currentColumn] +=
                            Math.min(currentGridToMinPathSumReachIt[currentRow - 1][currentColumn],
                                    currentGridToMinPathSumReachIt[currentRow][currentColumn - 1]);
                } else if (currentRow > 0) {
                    currentGridToMinPathSumReachIt[currentRow][currentColumn] +=
                            currentGridToMinPathSumReachIt[currentRow - 1][currentColumn];
                } else if (currentColumn > 0) {
                    currentGridToMinPathSumReachIt[currentRow][currentColumn] +=
                            currentGridToMinPathSumReachIt[currentRow][currentColumn - 1];
                } else {
                    continue;
                }
                // 🐖 这里 if/else if/else if 的结构，会不会漏掉一些情况呢?? 比如 dp[0][0]的情况
                // 答：不会，因为 dp[0][0] = numberBoard[0][0]
            }
        }

        return currentGridToMinPathSumReachIt[numberBoard.length - 1][numberBoard[0].length - 1];
    }
}
