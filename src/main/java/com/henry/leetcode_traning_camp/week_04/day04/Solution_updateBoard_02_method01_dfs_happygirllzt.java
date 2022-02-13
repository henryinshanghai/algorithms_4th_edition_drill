package com.henry.leetcode_traning_camp.week_04.day04;

public class Solution_updateBoard_02_method01_dfs_happygirllzt {
    public static void main(String[] args) {
        char[][] board = {
                {'E', 'E', 'E', 'E', 'E'},
                {'E', 'E', 'M', 'E', 'E'},
                {'E', 'E', 'E', 'E', 'E'},
                {'E', 'E', 'E', 'E', 'E'}
        };

        int[] click = {3, 0};

        char[][] updatedBoard = updateBoard(board, click);

        for (int i = 0; i < updatedBoard.length; i++) {
            System.out.println(updatedBoard[i]);
        }
    }

    private static int[][] dirs = {
            {0, 1},
            {0, -1},
            {1, 0},
            {-1, 0},
            {-1, -1},
            {-1, 1},
            {1, -1},
            {1, 1}
    };

    private static char[][] updateBoard(char[][] board, int[] click) {
        int row = click[0];
        int col = click[1];

        // 添加经常使用的值作为局部变量
        int m = board.length;
        int n = board[0].length;

        // 点击到了M或者X方格 aka 暴雷情况
        if (board[row][col] == 'M' ||
                board[row][col] == 'X') {
            board[row][col] = 'X';
            return board;
        }

        // 对于其他任意位置 开始找当前方格有几个地雷邻居
        int num = 0;
        for (int[] dir : dirs) {
            // 计算出当前方向上的邻居坐标
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            // 检验邻居坐标的有效性 如果邻居是一个地雷的话...
            if (newRow >= 0 && newCol >= 0
                    && newCol < n && newRow < m &&
                    board[newRow][newCol] == 'M') {
                num++;
            }
        }

        // 如果点击的位置周边有地雷邻居，按照规则，更新当前方格的表示方式
        if (num > 0) {
            board[row][col] = (char)(num - '0');
            return board;
        }

        // 如果点击的位置周围没有地雷邻居，按照规则：递归地更新所有邻居的表示方式
        board[row][col] = 'B';
        for (int[] dir : dirs) {
            // 计算出当前方向上的邻居坐标
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            // 检验邻居坐标的有效性 如果邻居是一个空方块的话...
            if (newRow >= 0 && newCol >= 0
                    && newCol < n && newRow < m &&
                    board[newRow][newCol] == 'E') { // E表示空方块...
                // 递归地调用方法本身来更新邻居方块的表示方式    aka 点击它
                updateBoard(board, new int[]{newRow, newCol});
            }
        }

        return board;
    }
} // EXPR:由于代码从上到下执行，所以放个被修改为1后，递归调用就会返回 而不会无限扩展
