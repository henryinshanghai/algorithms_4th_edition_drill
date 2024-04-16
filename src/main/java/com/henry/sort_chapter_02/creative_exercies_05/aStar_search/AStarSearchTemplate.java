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
     * @param startGrid
     * @param endGrid
     */
    public static Grid aStarSearch(Grid startGrid, Grid endGrid) {
        // #0 准备辅助列表对象 - 这里最好使用优先队列，执行起来会更快
        ArrayList<Grid> candidateGridsToChooseMinGridFrom = new ArrayList<Grid>();
        ArrayList<Grid> discoveredGrids = new ArrayList<Grid>();

        // #1 把 “起点方格” 加入到 candidateGridsToChooseMinGridFrom  中
        candidateGridsToChooseMinGridFrom.add(startGrid);

        // #2 从 “候选方格集合” 中选择 “估值最小的方格”处理 - 直到“候选方格集合”中没有方格
        while (candidateGridsToChooseMinGridFrom.size() > 0) {
            // 2-1 从候选方格中找到 “估值最小的方格” 来 作为“当前方格”
            Grid currentGrid = findMinGrid(candidateGridsToChooseMinGridFrom);
            // 2-2 把 “当前方格” 从 candidateGridsToChooseMinGridFrom 中移除
            candidateGridsToChooseMinGridFrom.remove(currentGrid);
            // 2-3 把 “当前格子” 添加到 discoveredGrids
            discoveredGrids.add(currentGrid);

            // 2-4 找到 当前方格 所有的“有效邻居方格”
            List<Grid> validNeighbors = findValidNeighbors(currentGrid, candidateGridsToChooseMinGridFrom, discoveredGrids);
            // 2-5 把所有的有效邻居方格 添加到 “候选方格集合中”
            addValidNeighborsInCandidates(validNeighbors, candidateGridsToChooseMinGridFrom, currentGrid, endGrid);

            // 2-6 如果 “终点方格” 在 candidateGridsToChooseMinGridFrom 中，说明下一步就能够到达终点。则：
            // 直接返回“候选方格集合”中的终点方格 （终点可以一步到达）
            if (findEndGridExistInCandidates(endGrid, candidateGridsToChooseMinGridFrom).isPresent()) {
                return findEndGridExistInCandidates(endGrid, candidateGridsToChooseMinGridFrom).get();
            }
        }

        // 2-7 candidateGridsToChooseMinGridFrom 用尽，仍旧没有找到终点 说明终点不可达，返回null
        return null;
    }

    private static void addValidNeighborsInCandidates(List<Grid> validNeighbors,
                                                      ArrayList<Grid> candidateGridsToChooseMinGridFrom,
                                                      Grid currentGrid,
                                                      Grid endGrid) {
        for (Grid neighbor : validNeighbors) {
            neighbor.initGrid(currentGrid, endGrid); // 在添加进集合之前，先 初始化 父方格 & 方格的估值信息
            candidateGridsToChooseMinGridFrom.add(neighbor);
        }
    }

    private static Optional<Grid> findEndGridExistInCandidates(Grid endGrid, ArrayList<Grid> candidateGridsToChooseMinGridFrom) {
        Grid findResult = null;
        for (Grid currentGrid : candidateGridsToChooseMinGridFrom) {
            if ((currentGrid.x == endGrid.x) && (currentGrid.y == endGrid.y)) {
                findResult =  currentGrid;
            }
        }
        // 这里返回的是 “候选方格集合”中 与结束方格坐标相同的那个方法 - 它包含有parentGrid的信息
        return Optional.ofNullable(findResult);
    }

    /**
     * 找到当前格子的所有临近格子，并添加到openList中
     *
     * @param currentGrid
     * @param accessibleGridsFromCurrentGrid
     * @param discoveredGrids
     * @return
     */
    private static List<Grid> findValidNeighbors(Grid currentGrid, ArrayList<Grid> accessibleGridsFromCurrentGrid,
                                                 ArrayList<Grid> discoveredGrids) {
        ArrayList<Grid> currentGridsNeighbors = new ArrayList<>();

        // 上下左右四个方向上的邻居
        if (isValidNeighborGrid(currentGrid.x, currentGrid.y - 1, accessibleGridsFromCurrentGrid, discoveredGrids)) {
            currentGridsNeighbors.add(new Grid(currentGrid.x, currentGrid.y - 1));
        }
        if (isValidNeighborGrid(currentGrid.x, currentGrid.y + 1, accessibleGridsFromCurrentGrid, discoveredGrids)) {
            currentGridsNeighbors.add(new Grid(currentGrid.x, currentGrid.y + 1));
        }

        if (isValidNeighborGrid(currentGrid.x - 1, currentGrid.y, accessibleGridsFromCurrentGrid, discoveredGrids)) {
            currentGridsNeighbors.add(new Grid(currentGrid.x - 1, currentGrid.y));
        }

        if (isValidNeighborGrid(currentGrid.x + 1, currentGrid.y, accessibleGridsFromCurrentGrid, discoveredGrids)) {
            currentGridsNeighbors.add(new Grid(currentGrid.x + 1, currentGrid.y));
        }

        return currentGridsNeighbors;
    }

    private static boolean isValidNeighborGrid(int x, int y, ArrayList<Grid> accessibleGridsFromCurrentGrid,
                                               ArrayList<Grid> discoveredGrids) {
        // #1 是否超过迷宫边界
        if (x < 0 || x >= MAZE.length || y < 0 || y >= MAZE[0].length) {
            return false;
        }

        // #2 是否是 障碍物方格
        if (MAZE[x][y] == 1) {
            return false;
        }

        // #3 是否已经在 accessibleGridsFromCurrentGrid 中了
        if (containGivenGrid(accessibleGridsFromCurrentGrid, x, y)) {
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
     * @param candidateGridsToChooseMinGridFrom
     * @return
     */
    private static Grid findMinGrid(ArrayList<Grid> candidateGridsToChooseMinGridFrom) {
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
    static class  Grid {
        // 方格在Maze中的坐标 （x, y）
        public int x;
        public int y;

        // 当前方格的估值
        public int movedStepsFromStartGrid; // 从起点到当前格子所需要的步数
        public int toMoveStepsToEndGridWithoutObstacles; // 在不考虑障碍的情况下，从当前格子到目标格子所需要的步数
        public int totalExpectStepsRouteViaCurrentGrid; // movedStepsFromStartGrid+h的和

        // 当前方格的 父方格 - 用于回溯出完整的路径（像链表一样）
        public Grid parent;

        public Grid(int x, int y) {
            this.x = x;
            this.y = y;
        }

        // 初始化完整的方格信息
        public void initGrid(Grid parentGrid, Grid endGrid) {
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