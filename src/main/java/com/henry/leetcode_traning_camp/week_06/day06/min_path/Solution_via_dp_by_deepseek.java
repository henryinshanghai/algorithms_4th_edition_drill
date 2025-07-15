package com.henry.leetcode_traning_camp.week_06.day06.min_path;

// 不把原元素拷贝到dp[]数组中的做法
// 这种做法可能会省一些时间
public class Solution_via_dp_by_deepseek {
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
        // 边界条件
        int rowAmount = numberBoard.length;
        int columnAmount = numberBoard[0].length;

        if (numberBoard == null || rowAmount == 0 || columnAmount == 0) {
            return 0;
        }

        // 准备合乎题意的dp[][]数组
        int[][] currentGridToMinPathSumReachIt = new int[rowAmount][columnAmount];

        /* 初始化dp[][]元素 */
        currentGridToMinPathSumReachIt[0][0] = numberBoard[0][0];

        // #1 初始化dp[][]数组第一列的元素
        for (int currentRow = 1; currentRow < rowAmount; currentRow++) {
            currentGridToMinPathSumReachIt[currentRow][0]
                    = currentGridToMinPathSumReachIt[currentRow - 1][0] + numberBoard[currentRow][0];
        }

        // #2 初始化dp[][]数组第一行的元素
        for (int currentColumn = 1; currentColumn < columnAmount; currentColumn++) {
            currentGridToMinPathSumReachIt[0][currentColumn] =
                    currentGridToMinPathSumReachIt[0][currentColumn - 1] + numberBoard[0][currentColumn];
        }

        // #3 计算并填充 dp[][]数组中的剩余元素
        for (int currentRow = 1; currentRow < rowAmount; currentRow++) {
            for (int currentColumn = 1; currentColumn < columnAmount; currentColumn++) {
                int numberOnCurrentGrid = numberBoard[currentRow][currentColumn];

                currentGridToMinPathSumReachIt[currentRow][currentColumn] =
                        numberOnCurrentGrid + // 当前元素
                            Math.min(currentGridToMinPathSumReachIt[currentRow - 1][currentColumn], // option1: 上一行同列的元素
                                currentGridToMinPathSumReachIt[currentRow][currentColumn - 1]); // option2: 同一行前一列的元素

            }
        }

        // 最后，返回 最后一个方格 => 到达此方格的最小路径sum
        // 🐖 数组的索引从0开始，因此 最后一个元素是 arr[length - 1]
        return currentGridToMinPathSumReachIt[rowAmount - 1][columnAmount - 1];
    }
}