package com.henry.sort_chapter_02.creative_exercies_05.aStar_search;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
A*算法步骤：
    1 把起始状态放到优先队列中；
    2 重复以下方法，直到达到目的地：
        1 删掉优先级最高的状态；
        2 把 从1中的状态，在一步之内能够达到的所有状态 全部添加到优先队列中(除去1的状态本身)

 */
public class AStarSearchTemplate {
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
     * 找到从 startGrid -> endGrid 的一个可行的最短路径
     *
     * @param startGrid
     * @param endGrid
     */
    public static Grid aStarSearch(Grid startGrid, Grid endGrid) {
        // #0 准备辅助列表对象 - 这里最好使用优先队列，执行起来会更快
        ArrayList<Grid> candidateGrids = new ArrayList<Grid>(); // 用于从中选择 其估值最小的方格 来 作为路径中的当前方格
        ArrayList<Grid> discoveredGrids = new ArrayList<Grid>();

        // #1 把 “起点方格” 加入到 candidateGrids中
        candidateGrids.add(startGrid);

        // 从 “候选方格集合” 中选择 “估值最小的方格”进行处理 - 直到“候选方格集合”中没有方格
        while (candidateGrids.size() > 0) {
            // #2 从候选方格集合中找到 “估值最小的方格” 来 作为“当前方格”
            Grid currentGrid = findMinGridFrom(candidateGrids);

            // #3 更新 “候选方格集合” 更新流程：currentGrid -> (discoveredGrids, candidateGrids) -> validNeighborGrids -> candidateGrids
            update(candidateGrids, discoveredGrids, currentGrid, endGrid);

            // #4 如果 “终点方格” 在 candidateGrids 中，说明下一步就能够到达终点。则：
            Optional<Grid> searchResult = searchEndGridExistInCandidates(endGrid, candidateGrids);
            if (searchResult.isPresent()) {
                // 直接返回“候选方格集合”中的终点方格 （终点可以一步到达）
                return searchResult.get();
            }
        }

        // 如果 candidateGrids 用尽，仍旧没有找到终点 说明终点不可达，返回null
        return null;
    }

    private static void update(ArrayList<Grid> candidateGrids, ArrayList<Grid> discoveredGrids, Grid currentGrid, Grid endGrid) {
        // #1 把 “当前方格” 从 candidateGridsToChooseMinGridFrom 中移除
        candidateGrids.remove(currentGrid);
        // #2 把 “当前格子” 添加到 discoveredGrids
        discoveredGrids.add(currentGrid);
        // #3 找到 当前方格 所有的“有效邻居方格”
        List<Grid> validNeighborGrids = findValidNeighborGridsOf(currentGrid, candidateGrids, discoveredGrids);
        // #4 把所有的有效邻居方格 添加到 “候选方格集合中”
        addValidNeighborsIntoCandidates(validNeighborGrids, candidateGrids, currentGrid, endGrid);
    }

    private static void addValidNeighborsIntoCandidates(List<Grid> validNeighborGrids,
                                                        ArrayList<Grid> candidateGrids,
                                                        Grid currentGrid,
                                                        Grid endGrid) {
        for (Grid neighborGrid : validNeighborGrids) {
            // 在添加进 candidateGrids 之前，先 完善方格信息 - #1 添加 父方格信息（用于回溯出方格路径）； #2 添加 方格的估值信息（用于从candidateGrids中获取下一个方格时，评估最小方格）
            neighborGrid.completeGridInfoWith(currentGrid, endGrid);
            candidateGrids.add(neighborGrid);
        }
    }

    private static Optional<Grid> searchEndGridExistInCandidates(Grid endGrid, ArrayList<Grid> candidateGridsToChooseMinGridFrom) {
        Grid findResult = null;
        for (Grid currentGrid : candidateGridsToChooseMinGridFrom) {
            if ((currentGrid.x == endGrid.x) && (currentGrid.y == endGrid.y)) {
                findResult = currentGrid;
            }
        }
        // 这里返回的是 “候选方格集合”中 与结束方格坐标相同的那个方法 - 它包含有parentGrid的信息
        return Optional.ofNullable(findResult);
    }

    /**
     * 找到当前格子的所有临近格子，并添加到openList中
     *
     * @param currentGrid
     * @param candidateGrids
     * @param discoveredGrids
     * @return
     */
    private static List<Grid> findValidNeighborGridsOf(Grid currentGrid, ArrayList<Grid> candidateGrids,
                                                       ArrayList<Grid> discoveredGrids) {
        ArrayList<Grid> currentGridsNeighbors = new ArrayList<>();

        /* 上下左右四个方向上的邻居，各自判断它们是不是“有效邻居” */
        // 如果是"有效的邻居"(其坐标满足条件)，则...
        if (isAValidNeighbor(currentGrid.x, currentGrid.y - 1, candidateGrids, discoveredGrids)) {
            // 为此邻居创建一个Grid，并添加到 有效邻居方格集合中
            currentGridsNeighbors.add(new Grid(currentGrid.x, currentGrid.y - 1));
        }
        if (isAValidNeighbor(currentGrid.x, currentGrid.y + 1, candidateGrids, discoveredGrids)) {
            currentGridsNeighbors.add(new Grid(currentGrid.x, currentGrid.y + 1));
        }

        if (isAValidNeighbor(currentGrid.x - 1, currentGrid.y, candidateGrids, discoveredGrids)) {
            currentGridsNeighbors.add(new Grid(currentGrid.x - 1, currentGrid.y));
        }

        if (isAValidNeighbor(currentGrid.x + 1, currentGrid.y, candidateGrids, discoveredGrids)) {
            currentGridsNeighbors.add(new Grid(currentGrid.x + 1, currentGrid.y));
        }

        return currentGridsNeighbors;
    }

    // 判断 指定的邻居 是不是有效邻居 - 其坐标需要满足4个条件：#1 在迷宫范围内； #2 不是障碍方格； #3 不存在于candidateGrids中； #4 不存在于 discoveredGrids中；
    private static boolean isAValidNeighbor(int x, int y, ArrayList<Grid> candidateGrids,
                                            ArrayList<Grid> discoveredGrids) {
        // #1 是否超过迷宫边界
        if (x < 0 || x >= MAZE.length || y < 0 || y >= MAZE[0].length) {
            return false;
        }

        // #2 是否是 障碍物方格
        if (MAZE[x][y] == 1) {
            return false;
        }

        // #3 是否已经在 candidateGrids 中了
        if (containGivenGrid(candidateGrids, x, y)) {
            return false;
        }

        // #4 是否已经在 discoveredGrids 中了
        if (containGivenGrid(discoveredGrids, x, y)) {
            return false;
        }

        return true;
    }

    /**
     * 找到当前openList中F值最小的格子
     *
     * @param candidateGridsToChooseMinGridFrom
     * @return
     */
    private static Grid findMinGridFrom(ArrayList<Grid> candidateGridsToChooseMinGridFrom) {
        Grid minGrid = candidateGridsToChooseMinGridFrom.get(0);

        for (Grid currentGrid : candidateGridsToChooseMinGridFrom) {
            if (currentGrid.totalExpectStepsRouteViaCurrentGrid < minGrid.totalExpectStepsRouteViaCurrentGrid) {
                minGrid = currentGrid;
            }
        }

        return minGrid;
    }

    // 判断格子集合中是否包含指定坐标的格子
    private static boolean containGivenGrid(List<Grid> grids, int x, int y) {
        for (Grid currentGrid : grids) {
            if ((currentGrid.x == x) && (currentGrid.y == y)) {
                return true;
            }
        }
        return false;
    }

    // 方格内部类
    static class Grid {
        // 方格在Maze中的坐标 （x, y）
        public int x;
        public int y;

        // 当前方格的估值
        public int movedStepsFromStartGrid; // 从起点到当前格子所需要的步数
        public int toMoveStepsToEndGridWithoutObstacles; // 在不考虑障碍的情况下，从当前格子到目标格子所需要的步数
        public int totalExpectStepsRouteViaCurrentGrid; // movedStepsFromStartGrid + toMoveStepsToEndGridWithoutObstacles 的和

        // 当前方格的 父方格 - 用于回溯出完整的路径（像链表一样）
        public Grid parent;

        public Grid(int x, int y) {
            this.x = x;
            this.y = y;
        }

        // 为当前方格添加完整的方格信息
        public void completeGridInfoWith(Grid parentGrid, Grid endGrid) {
            // #1 父方格
            this.parent = parentGrid;

            // #2 当前方格的估值 {movedStepsFromStartGrid, toMoveStepsToEndGridWithoutObstacles, totalExpectStepsRouteViaCurrentGrid}
            // 2-1 movedStepsFromStartGrid 从起始方格到当前方格的步数
            if (parentGrid != null) {
                this.movedStepsFromStartGrid = parentGrid.movedStepsFromStartGrid + 1;
            } else {
                this.movedStepsFromStartGrid = 1; // 邻居方格 相对于 初始方格的步数是 1
            }

            // 2-2 toMoveStepsToEndGridWithoutObstacles 在不考虑障碍的情况下，从当前方格移动到终点方格所需要的步数
            this.toMoveStepsToEndGridWithoutObstacles = Math.abs(this.x - endGrid.x) + Math.abs(this.y - endGrid.y);

            // 2-3 totalExpectStepsRouteViaCurrentGrid 如果选择当前方格，估计移动到终点方格所需要的总步数
            this.totalExpectStepsRouteViaCurrentGrid = this.movedStepsFromStartGrid + this.toMoveStepsToEndGridWithoutObstacles;
        }
    }


    private static void printThePathInMaze(ArrayList<Grid> path) {
        int rows = MAZE.length;
        int columns = MAZE[0].length;
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (containGivenGrid(path, row, column)) {
                    System.out.print("*, "); // 打印*号
                } else {
                    System.out.print(MAZE[row][column] + ", ");
                }
            }
            // 换行
            System.out.println();
        }
    }

    // 从终点方格中回溯出整个路径
    // 手段：在每个方格中，都会记录它的父方格(打哪儿来)
    private static ArrayList<Grid> generateThePath(Grid finalGrid) {
        ArrayList<Grid> path = new ArrayList<>();
        Grid currentGrid = finalGrid;
        // 起始方格没有父方格
        while (currentGrid != null) {
            path.add(new Grid(currentGrid.x, currentGrid.y));
            currentGrid = currentGrid.parent;
        }
        return path;
    }

    // 测试用例
    public static void main(String[] args) {
        // 设置起点与终点
        Grid startGrid = new Grid(2, 1);
        Grid endGrid = new Grid(2, 5);

        // 搜索迷宫终点
        Grid finalGrid = aStarSearch(startGrid, endGrid);

        // 回溯迷宫路径
        ArrayList<Grid> path = generateThePath(finalGrid);

        // 输出迷宫和路径，路径使用*表示
        printThePathInMaze(path);
    }
}
/*
0, 0, *, *, *, *, 0,
0, 0, *, 1, 0, *, 0,
0, *, *, 1, 0, *, 0,
0, 0, 0, 1, 0, 0, 0,
0, 0, 0, 0, 0, 0, 0,
 */