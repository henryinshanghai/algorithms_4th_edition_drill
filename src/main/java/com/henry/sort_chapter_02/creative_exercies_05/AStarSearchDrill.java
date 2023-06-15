package com.henry.sort_chapter_02.creative_exercies_05;

import java.util.ArrayList;
import java.util.List;

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

        public int totalExpectStepsViaCurrentGrid;
        public int movedStepsFromStartGrid;
        public int toMoveStepsToEndGridWithoutObstacles;

        private Grid parentGrid;

        public Grid(int xCoordinate, int yCoordinate) {
            this.xCoordinate = xCoordinate;
            this.yCoordinate = yCoordinate;
        }

        // 这里为Grid添加一个方法 - 用于计算当前方格的估值（这个估值会用于判断 “最小方格”）
        public void initGridWithAssessment(Grid parentGrid, Grid endGrid) {
            this.parentGrid = parentGrid;

            // 父方格的 “移动步数” + 1
            this.movedStepsFromStartGrid = parentGrid.movedStepsFromStartGrid + 1;
            // “当前方格”与“终点方格”的 相差步数
            this.toMoveStepsToEndGridWithoutObstacles = Math.abs(endGrid.xCoordinate - this.xCoordinate) + Math.abs(endGrid.yCoordinate - this.yCoordinate);
            // 累计和 即为 当前方格的估值
            this.totalExpectStepsViaCurrentGrid = this.movedStepsFromStartGrid + this.toMoveStepsToEndGridWithoutObstacles;
        }
    }

    public static Grid aStarSearch(Grid startGrid, Grid endGrid) {

        ArrayList<Grid> candidateGridsToChooseMinGridFrom = new ArrayList<>();
        ArrayList<Grid> discoveredGrids = new ArrayList<>();

        candidateGridsToChooseMinGridFrom.add(startGrid);

        while (candidateGridsToChooseMinGridFrom.size() > 0) {
            Grid currentChoseGird = findMinGrid(candidateGridsToChooseMinGridFrom);
            candidateGridsToChooseMinGridFrom.remove(currentChoseGird);
            discoveredGrids.add(currentChoseGird);

            // 找到邻居方格
            List<Grid> validNeighbors = findValidNeighbors(currentChoseGird, candidateGridsToChooseMinGridFrom, discoveredGrids);
            // 把邻居方格（包含评估值） 添加到 候选方格列表中
            addNeighborsInCandidateGrids(validNeighbors, candidateGridsToChooseMinGridFrom, currentChoseGird, endGrid);

            // 判断“终点方格” 是不是已经出现在 “候选方格集合”中了
            Grid finalGrid = findEndGridInCandidates(candidateGridsToChooseMinGridFrom, endGrid);
            if (finalGrid != null) {
                return finalGrid;
            }
        }

        return null;
    }

    private static Grid findEndGridInCandidates(ArrayList<Grid> candidateGrids, Grid endGrid) {
        for (Grid candidateGrid : candidateGrids) {
            if (candidateGrid.xCoordinate == endGrid.xCoordinate && candidateGrid.yCoordinate == endGrid.yCoordinate) {
                return candidateGrid;
            }
        }
        return null;
    }

    private static void addNeighborsInCandidateGrids(List<Grid> validNeighbors,
                                                     ArrayList<Grid> candidateGrids,
                                                     Grid currentChoseGird,
                                                     Grid endGrid) {
        for (Grid validNeighborGrid : validNeighbors) {
            validNeighborGrid.initGridWithAssessment(currentChoseGird, endGrid);
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

        if (containsGird(candidateGrids, xCoordinate, yCoordinate)) {
            return false;
        }

        if (containsGird(discoveredGrids, xCoordinate, yCoordinate)) {
            return false;
        }

        return true;
    }

    private static boolean containsGird(ArrayList<Grid> candidateGrids, int xCoordinate, int yCoordinate) {
        for (Grid currentCandidate : candidateGrids) {
            if (currentCandidate.xCoordinate == xCoordinate && currentCandidate.yCoordinate == yCoordinate) {
                return true;
            }
        }

        return false;
    }

    private static Grid findMinGrid(ArrayList<Grid> candidateGrids) {
        Grid minGrid = candidateGrids.get(0);

        for (int currentCursor = 1; currentCursor < candidateGrids.size(); currentCursor++) {
            Grid currentCandidateGrid = candidateGrids.get(currentCursor);
            if (currentCandidateGrid.totalExpectStepsViaCurrentGrid < minGrid.totalExpectStepsViaCurrentGrid) {
                minGrid = currentCandidateGrid;
            }
        }

        return minGrid;
    }

    public static void main(String[] args) {
        Grid startGrid = new Grid(2, 1);
        Grid endGrid = new Grid(2, 5);

        Grid finalGrid = aStarSearch(startGrid, endGrid);

        ArrayList<Grid> pathToEndGrid = generatePathFrom(finalGrid);

        printPathInMaze(pathToEndGrid);
    }

    private static void printPathInMaze(ArrayList<Grid> pathToEndGrid) {
        int rows = MAZE.length;
        int columns = MAZE[0].length;

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (containsGird(pathToEndGrid, row, column)) {
                    System.out.print("*, ");
                } else {
                    System.out.print(MAZE[row][column] + ", ");
                }
            }

            System.out.println();
        }
    }

    private static ArrayList<Grid> generatePathFrom(Grid finalGrid) {
        ArrayList<Grid> path = new ArrayList<>();

        Grid currentGrid = finalGrid;
        while (currentGrid != null) {
            path.add(currentGrid);
            currentGrid = currentGrid.parentGrid;
        }

        return path;
    }
}
