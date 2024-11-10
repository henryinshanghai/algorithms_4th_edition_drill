package com.henry.leetcode_traning_camp.week_04.day04.game_board;

public class Solution_updateBoard_02_method01_dfs_happygirllzt {
    public static void main(String[] args) {
        // 初始时，就只有 未挖出的空方快 + 未挖出的地雷[上帝视角]
        char[][] gameBoard = {
                {'E', 'E', 'E', 'E', 'E'},
                {'E', 'E', 'M', 'E', 'E'},
                {'E', 'E', 'E', 'E', 'E'},
                {'E', 'E', 'E', 'E', 'E'}
        };

        int[] clickedCoordination = {3, 0};
        char[][] updatedBoard = updateBoardOnClicking(gameBoard, clickedCoordination);
        print(updatedBoard);
    }

    private static void print(char[][] updatedBoard) {
        for (int currentRowCursor = 0; currentRowCursor < updatedBoard.length; currentRowCursor++) {
            System.out.println(updatedBoard[currentRowCursor]);
        }
    }

    private static int[][] allDirections = {
            {0, 1}, // 右
            {0, -1}, // 左
            {1, 0}, // 上
            {-1, 0}, // 下
            {-1, -1}, // 左下
            {-1, 1}, // 左上
            {1, -1}, // 右下
            {1, 1} // 右上
    };

    // 揭露指定位置上的方格，并递归地揭露其邻居方格
    private static char[][] updateBoardOnClicking(char[][] gameBoard, int[] clickedCoordination) {
        int rowCoordination = clickedCoordination[0];
        int colCoordination = clickedCoordination[1];

        // 添加经常使用的值作为局部变量
        int maxRow = gameBoard.length;
        int maxColumn = gameBoard[0].length;

        // 如果 点击到的当前位置 是一个 未挖出的地雷方格/已经挖出的地雷方格（方格的值为M或者X），则：
        if (gameBoard[rowCoordination][colCoordination] == 'M' ||
                gameBoard[rowCoordination][colCoordination] == 'X') {
            // 按照规则，把方格中的值设置为X
            gameBoard[rowCoordination][colCoordination] = 'X';
            // 返回棋盘，游戏结束
            return gameBoard;
        }

        // 如果 点击到的当前位置 是 其他类型的方格，则：查看 以当前方格作为中心方格地九宫格中 存在有多少个“地雷方格”
        int mineAmountInCurrentGrid = 0;
        // 对于所有的8个方向...
        for (int[] currentDirection : allDirections) {
            // 计算出当前方向上的邻居方格的坐标
            int neighborRowCoord = rowCoordination + currentDirection[0];
            int neighborColumnCoord = colCoordination + currentDirection[1];

            // 如果“该邻居”是一个“未挖出的地雷(M)”的话，说明 需要把此地雷邻居 计入到 当前方格的计数中，则：
            if (neighborRowCoord >= 0 && neighborColumnCoord >= 0 &&
                    neighborColumnCoord < maxColumn && neighborRowCoord < maxRow &&
                    gameBoard[neighborRowCoord][neighborColumnCoord] == 'M') {
                // 把“当前位置的 直接邻居地雷的计数”+1
                mineAmountInCurrentGrid++;
            }
        }

        // 如果“以当前点击坐标为中心的九宫格”中的地雷数量 不为0，说明 需要更新 当前点击坐标所显示的值，则：
        if (mineAmountInCurrentGrid > 0) {
            // 按照规则 来 更新当前方格的值 为 ”当前九宫格中的地雷数量“
            gameBoard[rowCoordination][colCoordination] = (char)(mineAmountInCurrentGrid - '0');
            return gameBoard;
        }

        // 如果“九宫格中的地雷数量”为0，说明 它应该显示成为一个 “已挖出的空方块”，则：
        // 按照规则 来 更新“所有能够被更新的方块”
        // ① 首先修改当前方块为‘B’
        gameBoard[rowCoordination][colCoordination] = 'B';
        // ② 再者，对于九宫格中所有的其他邻居方块...
        for (int[] currentDirection : allDirections) {
            // 计算出当前方向上的邻居坐标
            int neighborRowCoord = rowCoordination + currentDirection[0];
            int neighborColCoord = colCoordination + currentDirection[1];

            // 检验邻居坐标的有效性 如果邻居仍旧是一个“未挖出的空方块”的话,说明应该递归地揭露它，则：
            if (neighborRowCoord >= 0 && neighborColCoord >= 0
                    && neighborColCoord < maxColumn && neighborRowCoord < maxRow &&
                    gameBoard[neighborRowCoord][neighborColCoord] == 'E') {
                // 递归地调用方法本身 来 揭露此邻居的情况
                updateBoardOnClicking(gameBoard, new int[]{neighborRowCoord, neighborColCoord});
            }
        }

        return gameBoard;
    }
} // EXPR:由于代码从上到下执行，所以放个被修改为1后，递归调用就会返回 而不会无限扩展
