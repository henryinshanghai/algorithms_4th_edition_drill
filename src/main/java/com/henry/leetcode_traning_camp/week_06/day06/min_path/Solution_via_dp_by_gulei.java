package com.henry.leetcode_traning_camp.week_06.day06.min_path;

// 验证：对于 从矩形棋盘上计算出 按照特定规则从左上角到右下角的最小路径的问题，可以使用
// 从所有可能的选项中选择最小路径的方式 来 得到 “到达当前方格的最小路径”
// 最优子结构：到达当前方格的最小路径 = 到达“上一个方格”的最小路径 + 当前方格的value
// 🐖 这种方式 先拷贝元素到dp[][]数组中，这种做法会 #1 代码层面上，由于元素是本地操作，所以使用+=的操作； #2 更加耗时
public class Solution_via_dp_by_gulei {
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

        if (numberBoard == null || rowAmount == 0) {
            return 0;
        }

        // 准备合乎题意的dp[][]数组
        int[][] currentGridToMinPathSumReachIt = new int[rowAmount][columnAmount];

        // #0 先把 二维数组中的元素 拷贝到 dp[][]数组中，然后在此基础上 来 计算dp[][]数组正确的元素值
        fullCopy(numberBoard, currentGridToMinPathSumReachIt);

        // #1 初始化dp[][]数组第一列的元素
        for (int currentRow = 1; currentRow < currentGridToMinPathSumReachIt.length; currentRow++) {
            currentGridToMinPathSumReachIt[currentRow][0]
                    += currentGridToMinPathSumReachIt[currentRow - 1][0];
        }

        // #2 初始化dp[][]数组第一行的元素
        for (int currentColumn = 1; currentColumn < currentGridToMinPathSumReachIt[0].length; currentColumn++) {
            currentGridToMinPathSumReachIt[0][currentColumn] +=
                    currentGridToMinPathSumReachIt[0][currentColumn - 1];
        }

        // #3 计算并填充 dp[][]数组中的剩余元素
        for (int currentRow = 1; currentRow < currentGridToMinPathSumReachIt.length; currentRow++) {
            for (int currentColumn = 1; currentColumn < currentGridToMinPathSumReachIt[currentRow].length; currentColumn++) {

                currentGridToMinPathSumReachIt[currentRow][currentColumn] += // 当前元素
                        Math.min(currentGridToMinPathSumReachIt[currentRow - 1][currentColumn], // option1: 上一行同列的元素
                                currentGridToMinPathSumReachIt[currentRow][currentColumn - 1]); // option2: 同一行前一列的元素

            }
        }

        // 最后，返回 最后一个方格 => 到达此方格的最小路径sum
        // 🐖 数组的索引从0开始，因此 最后一个元素是 arr[length - 1]
        return currentGridToMinPathSumReachIt[rowAmount - 1][columnAmount - 1];
    }

    private static void fullCopy(int[][] numberBoard, int[][] currentCoordinationToItsMinPathSum) {
        for (int currentRow = 0; currentRow < numberBoard.length; currentRow++) {
            for (int currentColumn = 0; currentColumn < numberBoard[0].length; currentColumn++) {
                currentCoordinationToItsMinPathSum[currentRow][currentColumn] = numberBoard[currentRow][currentColumn];
            }
        }
    }
}

// reference: https://www.youtube.com/watch?v=ItjZdu6jEMs