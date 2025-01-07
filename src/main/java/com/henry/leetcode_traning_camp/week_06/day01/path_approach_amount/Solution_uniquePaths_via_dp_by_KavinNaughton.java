package com.henry.leetcode_traning_camp.week_06.day01.path_approach_amount;

// 验证：对于 在一个矩形棋盘上 按照指定的规则，从左上角走到右下角 存在有多少中走法 这样的问题，可以使用 动态规划的技巧 来 得出大答案；
// 判断一个问题是否可以用动态规划求解的依据：#1 在求解的过程中是否存在有“重叠子问题”； #2 子问题的最优解 能否用于推出 原始问题的最优解（最优子结构）；
// 本题中，到达左边一个方格(上面一个方格)的路径数量 就能够用于计算出 到达当前方格的路径数量，因此可以使用 DP
public class Solution_uniquePaths_via_dp_by_KavinNaughton {
    public static void main(String[] args) {
        int rows = 3;
        int columns = 7;
        int pathAmount = pathAmountsTowardTargetGrid(rows, columns);

        System.out.println("在一个" + rows + "*" + columns + "的二维数组中，从" +
                "左上角方格到右下角方格的路径一共有" + pathAmount + "种");

    }

    private static int pathAmountsTowardTargetGrid(int boardRows, int boardColumns) {
        /* 明确dp[][]数组的含义，并尽可能给它提供一个有意义的名字 */
        // pathAmountTowardsCurrentPosition[i][j]的含义/状态定义：从左上角到达当前位置(i, j)一共有多少种走法
        int[][] pathAmountTowardsCurrentPosition = new int[boardRows][boardColumns];

        /* Ⅰ 初始化dpTable中的第一行与第一列元素的值为1————为递推求出其他元素的值做准备 */
        // 填充第一列的元素
        for (int currentRow = 0; currentRow < pathAmountTowardsCurrentPosition.length; currentRow++) {
            pathAmountTowardsCurrentPosition[currentRow][0] = 1;
        }

        // 填充第一行的元素
        for (int currentColumn = 0; currentColumn < pathAmountTowardsCurrentPosition[0].length; currentColumn++) {
            pathAmountTowardsCurrentPosition[0][currentColumn] = 1;
        }

        /* Ⅱ 按照题设规则进行递推，使用子问题的解 来 求出 pathAmountTowardsCurrentPosition[]中其他元素的值 */
        for (int currentRow = 1; currentRow < pathAmountTowardsCurrentPosition.length; currentRow++) {
            for (int currentColumn = 1; currentColumn < pathAmountTowardsCurrentPosition[currentRow].length; currentColumn++) {
                // 受限于题设的行走规则，因此有：到达当前方格的走法 = 到达其左边一个方格的走法 + 到达其上面一个方格的走法
                pathAmountTowardsCurrentPosition[currentRow][currentColumn]
                        = pathAmountTowardsCurrentPosition[currentRow - 1][currentColumn] // 上面一个方格的值
                        + pathAmountTowardsCurrentPosition[currentRow][currentColumn - 1]; // 左边一个方格的值
            }

        }

        // 打印二维数组中的元素值
        printItemsIn(pathAmountTowardsCurrentPosition);

        /* Ⅲ 返回 pathAmountTowardsCurrentPosition[]中，满足题意的元素值(二维数组右下角的元素) */
        return pathAmountTowardsCurrentPosition[boardRows - 1][boardColumns - 1];
    }

    private static void printItemsIn(int[][] pathAmountTowardsCurrentPosition) {
        for (int currentRow = 0; currentRow < pathAmountTowardsCurrentPosition.length; currentRow++) {
            for (int currentColumn = 0; currentColumn < pathAmountTowardsCurrentPosition[currentRow].length; currentColumn++) {
                System.out.print(pathAmountTowardsCurrentPosition[currentRow][currentColumn] + "    ");
            }

            System.out.println();
        }
    }
}
