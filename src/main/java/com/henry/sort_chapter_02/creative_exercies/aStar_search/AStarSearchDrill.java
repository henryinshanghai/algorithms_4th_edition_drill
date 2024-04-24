package com.henry.sort_chapter_02.creative_exercies.aStar_search;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AStarSearchDrill {

    private static int[][] MAZE = {
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0}
    };

    static class Grid {

        public int xCoordinate;
        public int yCoordinate;

        public int totalExpectStepsThroughCurrentGrid;
        public int movedStepsFromStartGrid;
        public int toMoveStepsToEndGridWithoutObstacles;

        private Grid parentGrid;

        public Grid(int xCoordinate, int yCoordinate) {
            this.xCoordinate = xCoordinate;
            this.yCoordinate = yCoordinate;
        }

        // 这里为Grid添加一个方法 - 用于计算当前方格的估值（这个估值会用于判断 “最小方格”）
        public void completeGridInfoWith(Grid parentGrid, Grid endGrid) {
            this.parentGrid = parentGrid;

            // 父方格的 “移动步数” + 1
            this.movedStepsFromStartGrid = parentGrid.movedStepsFromStartGrid + 1;
            // “当前方格”与“终点方格”的 相差步数
            this.toMoveStepsToEndGridWithoutObstacles = Math.abs(endGrid.xCoordinate - this.xCoordinate) + Math.abs(endGrid.yCoordinate - this.yCoordinate);
            // 累计和 即为 当前方格的估值
            this.totalExpectStepsThroughCurrentGrid = this.movedStepsFromStartGrid + this.toMoveStepsToEndGridWithoutObstacles;
        }
    }

    // 方法的返回值只是一个“终点方格” - 但是方格中封装了 parentGrid的信息，可以用来反溯得到 整个搜索路径
    public static Grid aStarSearch(Grid startGrid, Grid endGrid) {
        ArrayList<Grid> candidateGrids = new ArrayList<>();
        ArrayList<Grid> discoveredGrids = new ArrayList<>();

        candidateGrids.add(startGrid);

        while (candidateGrids.size() > 0) {
            Grid currentChoseGird = findMinGridFrom(candidateGrids);

            update(discoveredGrids, candidateGrids, currentChoseGird, endGrid);

            // 判断“终点方格” 是不是已经出现在 “候选方格集合”中了
            Optional<Grid> searchResult = searchEndGridInCandidates(candidateGrids, endGrid);
            if (searchResult.isPresent()) {
                return searchResult.get();
            }
        }

        return null;
    }

    private static void update(ArrayList<Grid> discoveredGrids, ArrayList<Grid> candidateGrids, Grid currentChoseGird, Grid endGrid) {
        candidateGrids.remove(currentChoseGird);
        discoveredGrids.add(currentChoseGird);

        // 找到邻居方格
        List<Grid> validNeighborGrids = findValidNeighbors(currentChoseGird, candidateGrids, discoveredGrids);
        // 把邻居方格（包含评估值） 添加到 候选方格列表中
        addNeighborsInCandidateGrids(validNeighborGrids, candidateGrids, currentChoseGird, endGrid);
    }

    // #1 把方法的返回值类型修改为Optional<T>
    private static Optional<Grid> searchEndGridInCandidates(ArrayList<Grid> candidateGrids, Grid endGrid) {
        // #3 添加一个局部变量，用于 按需修改变量的值 && 在返回点返回
        Grid searchResult = null;

        for (Grid candidateGrid : candidateGrids) {
            if (candidateGrid.xCoordinate == endGrid.xCoordinate && candidateGrid.yCoordinate == endGrid.yCoordinate) {
                searchResult = candidateGrid;
            }
        }
        // #2 在方法中只保留一个返回点, 用于返回Optional对象 - 作用：显式声明 此对象可能是一个null；
        // 用法：把 表示原始对象的变量 作为参数，传递给 Optional的ofNullable()方法。 可以理解为 使用ofNullable()方法，封装了 原始变量
        return Optional.ofNullable(searchResult);
    }

    private static void addNeighborsInCandidateGrids(List<Grid> validNeighbors,
                                                     ArrayList<Grid> candidateGrids,
                                                     Grid currentChoseGird,
                                                     Grid endGrid) {
        for (Grid validNeighborGrid : validNeighbors) {
            validNeighborGrid.completeGridInfoWith(currentChoseGird, endGrid);
            candidateGrids.add(validNeighborGrid);
        }
    }

    private static List<Grid> findValidNeighbors(Grid currentChoseGird, ArrayList<Grid> candidateGridsToChooseMinGridFrom, ArrayList<Grid> discoveredGrids) {
        ArrayList<Grid> validNeighbors = new ArrayList<>();
        int x = currentChoseGird.xCoordinate;
        int y = currentChoseGird.yCoordinate;

        if (isValidNeighbor(x - 1, y, candidateGridsToChooseMinGridFrom, discoveredGrids)) {
            validNeighbors.add(new Grid(x - 1, y));
        }

        if (isValidNeighbor(x, y + 1, candidateGridsToChooseMinGridFrom, discoveredGrids)) {
            validNeighbors.add(new Grid(x, y + 1));
        }

        if (isValidNeighbor(x + 1, y, candidateGridsToChooseMinGridFrom, discoveredGrids)) {
            validNeighbors.add(new Grid(x + 1, y));
        }

        if (isValidNeighbor(x, y - 1, candidateGridsToChooseMinGridFrom, discoveredGrids)) {
            validNeighbors.add(new Grid(x, y - 1));
        }

        return validNeighbors;
    }

    private static boolean isValidNeighbor(int xCoordinate, int yCoordinate, ArrayList<Grid> candidateGrids, ArrayList<Grid> discoveredGrids) {
        if (xCoordinate < 0 || xCoordinate >= MAZE.length || yCoordinate < 0 || yCoordinate >= MAZE[0].length) {
            return false;
        }

        // 判断是不是 “障碍方格”
        if (MAZE[xCoordinate][yCoordinate] == 1) {
            return false;
        }

        if (containsGivenGird(candidateGrids, xCoordinate, yCoordinate)) {
            return false;
        }

        if (containsGivenGird(discoveredGrids, xCoordinate, yCoordinate)) {
            return false;
        }

        return true;
    }

    private static boolean containsGivenGird(ArrayList<Grid> gridCollection, int xCoordinate, int yCoordinate) {
        for (Grid currentGrid : gridCollection) {
            if (currentGrid.xCoordinate == xCoordinate && currentGrid.yCoordinate == yCoordinate) {
                return true;
            }
        }

        return false;
    }

    private static Grid findMinGridFrom(ArrayList<Grid> candidateGrids) {
        Grid minGrid = candidateGrids.get(0);

        for (int currentCursor = 1; currentCursor < candidateGrids.size(); currentCursor++) {
            Grid currentCandidateGrid = candidateGrids.get(currentCursor);
            if (currentCandidateGrid.totalExpectStepsThroughCurrentGrid < minGrid.totalExpectStepsThroughCurrentGrid) {
                minGrid = currentCandidateGrid;
            }
        }

        return minGrid;
    }

    // 在迷宫中绘制出 a*寻路算法所找到的路径(最短吗??)
    private static void printPathInMaze(ArrayList<Grid> pathToEndGrid) {
        int rows = MAZE.length;
        int columns = MAZE[0].length;

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                // 如果当前坐标出现在 路径方格的集合中
                if (containsGivenGird(pathToEndGrid, row, column)) {
                    // 则：打印*
                    System.out.print("*, ");
                } else { // 否则：打印原始的数组元素
                    System.out.print(MAZE[row][column] + ", ");
                }
            }

            System.out.println();
        }
    }

    private static ArrayList<Grid> generatePathFrom(Grid finalGrid) {
        ArrayList<Grid> gridsInPath = new ArrayList<>();

        Grid currentGrid = finalGrid;
        while (currentGrid != null) {
            gridsInPath.add(currentGrid);
            currentGrid = currentGrid.parentGrid;
        }

        return gridsInPath;
    }

    public static void main(String[] args) {
        Grid startGrid = new Grid(2, 1);
        Grid endGrid = new Grid(2, 5);

        Grid finalGrid = aStarSearch(startGrid, endGrid);

        // 🐖 这里得到的path其实是逆序的。但 printPathInMaze()中只是把它当成一个bag集合来使用，不关心方格的顺序
        ArrayList<Grid> pathToEndGrid = generatePathFrom(finalGrid);

        printPathInMaze(pathToEndGrid);
    }
}
