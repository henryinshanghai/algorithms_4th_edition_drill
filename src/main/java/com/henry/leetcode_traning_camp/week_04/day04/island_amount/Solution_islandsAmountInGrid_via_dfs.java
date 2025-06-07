package com.henry.leetcode_traning_camp.week_04.day04.island_amount;

// 验证：可以在网格中使用 把“陆地单元”标记为“水单元” + 向四周搜索的方式 来 找到网格中的“岛屿”数量
public class Solution_islandsAmountInGrid_via_dfs {
    public static void main(String[] args) {
        char[][] unitsGrid = {
                {'1', '1', '1', '1', '0'},
                {'1', '1', '0', '1', '0'},
                {'1', '1', '0', '0', '0'},
                {'0', '0', '0', '0', '0'}
        };

        int islandAmount = numOfIslandsIn(unitsGrid);
        System.out.println("给定的二维数组中有 " + islandAmount + "个岛屿");
    }

    private static int numOfIslandsIn(char[][] unitsGrid) {
        int islandAmount = 0;

        for (int currentRowCursor = 0; currentRowCursor < unitsGrid.length; currentRowCursor++) {
            for (int currentColumnCursor = 0; currentColumnCursor < unitsGrid[0].length; currentColumnCursor++) {
                // 获取到 网格中“当前位置上的单元”的值
                char currentUnit = unitsGrid[currentRowCursor][currentColumnCursor];

                // 如果“当前网格单元”是陆地，说明 它可能是某个新的岛屿的一部分，则：
                if (currentUnit == '1') {
                    // #1 先把岛屿数量+1
                    islandAmount++;
                    // #2 把从“当前位置”出发，把 所有由当前位置所能够到达的“陆地单元”(“当前岛屿”上所有的陆地单元)，都标记成“水单元”👇
                    // 用于 ① 避免重复累计岛屿的数量； ② 能够在下次遇到“陆地单元”时，自信地判定 它是某个新的岛屿的一部分
                    markAllAccessibleGridInIsland(unitsGrid, currentRowCursor, currentColumnCursor);
                }
            }
        }

        return islandAmount;
    }

    // 作用：在二位网格中，把所有由当前位置可以到达的“陆地单元” 都标记成为“水单元”
    public static void markAllAccessibleGridInIsland(char[][] gridArr, int currentRow, int currentColumn) {
        // #1 递归终结条件
        // 如果 搜索范围超过了索引越界 或者 当前网格不是陆地，说明 对当前岛屿的搜索已经结束，则：
        if ((currentRow < 0) || (currentColumn < 0)
                || (currentRow > gridArr.length)
                || (currentColumn > gridArr[0].length) // 搜索范围超过了索引越界
                || (gridArr[currentRow][currentColumn] != '1')) { // 当前网格不是陆地
            // 结束当前递归，返回上一级递归
            return;
        }

        /* #2 本级递归要做的事情 */
        // #2-1 把当前位置 标记为 “水单元”
        gridArr[currentRow][currentColumn] = '*';
        // #2-2 从当前位置，沿着所有可能的方向 继续搜索陆地
        markAllAccessibleGridInIsland(gridArr, currentRow, currentColumn + 1); // 向右一格
        markAllAccessibleGridInIsland(gridArr, currentRow, currentColumn - 1); // 向左一格
        markAllAccessibleGridInIsland(gridArr, currentRow + 1, currentColumn); // 向下一格
        markAllAccessibleGridInIsland(gridArr, currentRow - 1, currentColumn); // 向上一格
    }
}
