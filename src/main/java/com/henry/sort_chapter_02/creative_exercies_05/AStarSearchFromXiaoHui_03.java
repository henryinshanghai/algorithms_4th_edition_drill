package com.henry.sort_chapter_02.creative_exercies_05;

import java.util.ArrayList;
import java.util.List;

public class AStarSearchFromXiaoHui_03 {
    // 迷宫地图
    public static final int[][] MAZE = {
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0}
    };

    /**
     * A*寻路的主要逻辑
     */
    public static Grid aStarSearch(Grid start, Grid end) {
        ArrayList<Grid> openList = new ArrayList<Grid>();
        ArrayList<Grid> closeList = new ArrayList<Grid>();

        // 把起点加入到openList中
        openList.add(start);

        // 主循环 每一轮检查一个当前格子节点
        while (openList.size() > 0) {
            Grid currentGrid = findMinGrid(openList);
            // 把当前格子从openList中移除
            openList.remove(currentGrid);
            // 把当前格子添加到closeList
            closeList.add(currentGrid);
            // 找到所有的临近节点
            List<Grid> neighbors = findNeighbors(currentGrid, openList, closeList);

            for (Grid grid :
                    neighbors) {
                if (!openList.contains(grid)) { // 邻近节点不在openList中...
                    // 初始化当前节点
                    grid.initGrid(currentGrid, end);
                    openList.add(grid);
                }
            }

            // 如果终点在openList中，直接返回终点格子 （终点可以一步到达）
            for (Grid grid : openList) {
                if ((grid.x == end.x) && (grid.y == end.y)) {
                    return grid;
                }
            }
        }

        // openList用尽，仍旧没有找到终点 说明终点不可达，返回null
        return null;
    }

    /**
     * 找到当前格子的所有临近格子，并添加到openList中
     *
     * @param grid
     * @param openList
     * @param closeList
     * @return
     */
    private static List<Grid> findNeighbors(Grid grid, ArrayList<Grid> openList, ArrayList<Grid> closeList) {
        ArrayList<Grid> gridList = new ArrayList<>();
        if (isValidGrid(grid.x, grid.y - 1, openList, closeList)) {
            gridList.add(new Grid(grid.x, grid.y - 1));
        }
        if (isValidGrid(grid.x, grid.y + 1, openList, closeList)) {
            gridList.add(new Grid(grid.x, grid.y + 1));
        }

        if (isValidGrid(grid.x - 1, grid.y, openList, closeList)) {
            gridList.add(new Grid(grid.x - 1, grid.y));
        }

        if (isValidGrid(grid.x + 1, grid.y, openList, closeList)) {
            gridList.add(new Grid(grid.x + 1, grid.y));
        }

        return gridList;
    }

    private static boolean isValidGrid(int x, int y, ArrayList<Grid> openList, ArrayList<Grid> closeList) {
        // 是否超过边界
        if (x < 0 || x >= MAZE.length || y < 0 || y >= MAZE[0].length) {
            return false;
        }

        // 是否有障碍物
        if (MAZE[x][y] == 1) {
            return false;
        }

        // 是否已经在openList中了
        if (containGrid(openList, x, y)) {
            return false;
        }

        if (containGrid(closeList, x, y)) {
            return false;
        }

        return true;
    }

    /**
     * 找到当前openList中F值最小的格子
     * @param openList
     * @return
     */
    private static Grid findMinGrid(ArrayList<Grid> openList) {
        Grid minGrid = openList.get(0);
        for (Grid grid : openList) {
            if (grid.f < minGrid.f) {
                minGrid = grid;
            }
        }

        return minGrid;
    }

    /**
     * 判断格子集合中是否包含指定的格子
     * @param grids
     * @param x
     * @param y
     * @return
     */
    private static boolean containGrid(List<Grid> grids, int x, int y) {
        for (Grid n : grids) {
            if ((n.x == x) && (n.y == y)) {
                return true;
            }
        }
        return false;
    }

    static class Grid {
        public int x;
        public int y;

        public int f; // g+h的和
        public int g; // 从起点到当前格子所需要的步数
        public int h; // 在不考虑障碍的情况下，从当前格子到目标格子所需要的步数

        public Grid parent;

        public Grid(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void initGrid(Grid parent, Grid end) {
            this.parent = parent;
            if (parent != null) {
                this.g = parent.g + 1;
            } else {
                this.g = 1;
            }

            this.h = Math.abs(this.x - end.x) + Math.abs(this.y - end.y);

            this.f = this.g + this.h;
        }
    }

    // 测试用例
    public static void main(String[] args) {
        // 设置起点与终点
        Grid startGrid = new Grid(2, 1);
        Grid endGrid = new Grid(2, 5);

        // 搜索迷宫终点
        Grid resultGrid = aStarSearch(startGrid, endGrid);

        // 回溯迷宫路径
        ArrayList<Grid> path = new ArrayList<>();
        while (resultGrid != null) {
            path.add(new Grid(resultGrid.x, resultGrid.y));
            resultGrid = resultGrid.parent;
        }

        // 输出迷宫和路径，路径使用*表示
        for (int i = 0; i < MAZE.length; i++) { // 二维数组 = 特殊的一维数组 + 数组中的每一个元素都是一个一维数组；
            // 内循环作用：打印二维数组的一行
            for (int j = 0; j < MAZE[0].length; j++) { // 第一个元素的长度
                if (containGrid(path, i, j)) { // 如果下标数对包含在数组中...
                    System.out.print("*, "); // 打印*号
                } else {
                    System.out.print(MAZE[i][j] + ", "); // 否则打印二维数组的初始值
                }
            }
            // 换行
            System.out.println();
        }
    }
}
/*
0, 0, *, *, *, *, 0,
0, 0, *, 1, 0, *, 0,
0, *, *, 1, 0, *, 0,
0, 0, 0, 1, 0, 0, 0,
0, 0, 0, 0, 0, 0, 0,
 */