package com.henry.leetcode_traning_camp.week_01.day3_dp_theme.smallest_path_in_grid.via_recursion;

// 验证：对于 在矩形方格中找最小路径 && 走法限定在向下与向右 的问题，可以使用 递归的方式 来 找到 到达终点的最小路径
public class Solution_smallest_path_sum_via_recursion_topToDown {
    public static void main(String[] args) {
//        [
//          [1,3,1],
//          [1,5,1],
//          [4,2,1]
//        ]
        int[][] grid = {{1, 3, 1}, {1, 5, 1}, {4, 2, 1}};

        int minPathSum = minPathSum(grid);

        System.out.println("给定二维数组中满足条件的路径的元素加和值为： " + minPathSum);

    }

    // 作用：找到给定二维数组中满足条件的路径，并返回路径的元素加和值
    private static int minPathSum(int[][] grid) {
        if (grid == null) return 0;

        int rowAmount = grid.length - 1;
        int columnAmount = grid[0].length - 1;

        return minPathSumToGivenDest(grid, rowAmount, columnAmount);
    }

    private static int minPathSumToGivenDest(int[][] grid, int destRowCoord, int destColCoord) {
        // #1 递归终结条件 行与列的值都是0
        if (destRowCoord == 0 && destColCoord == 0) return grid[0][0];

        // #2 使用规模更小的子问题的结果 来 帮助解决原始问题
        int minPathSum = -1;
        int valueOfCurrentPosition = grid[destRowCoord][destColCoord];

        /* 分类讨论 */
        // #2-1 如果目标方格的行坐标是0，说明只能通过 向右前进一格 的方式 到达此位置，则
        if(destRowCoord == 0) {
            // 路径的value = 前一个位置的路径value + 当前方格的value
            minPathSum = valueOfCurrentPosition + minPathSumToGivenDest(grid, destRowCoord, destColCoord - 1);
        }
        // #2-2 如果目标方格的列坐标是0，说明只能通过 向下前进一格 的方式 到达此位置，则
        else if (destColCoord == 0) {
            // 路径的value = 前一个位置的路径value + 当前方格的value
            minPathSum = valueOfCurrentPosition + minPathSumToGivenDest(grid, destRowCoord - 1, destColCoord);
        }
        else { // #2-3 如果目标方格的坐标是其他任意位置，说明 可能通过两种方式到达此位置，则
            // 要么通过 向右前进一格的方式，要么通过 向下前进一格的方式，我们取其中的较小者
            minPathSum = valueOfCurrentPosition +
                    Math.min(minPathSumToGivenDest(grid, destRowCoord - 1, destColCoord),
                            minPathSumToGivenDest(grid, destRowCoord, destColCoord - 1)
                    );
        } // 🐖：为了使各个语句块能够并列，这里必须使用if... else if ... else的语法————这是使用变量minPathSum所需要做出的代码调整

        // #3 返回原始问题的答案
        return minPathSum;
    }
} // 由于递归方式中有很多重复的计算（参考递归树），因此这种方法其实会超时（time limited）
