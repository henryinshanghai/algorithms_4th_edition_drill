package com.henry.leetcode_traning_camp.week_04.day05.simulate_the_robot;

import java.util.HashSet;
import java.util.Set;

// 验证：对于 机器人在 有障碍的无限大网格上 按照指令行走的问题，可以
// #1 使用 二维数组 来 表示障碍物的坐标；
// #2 使用 数字序列 来 表示指令序列；
// #3 使用 二维数组 来 表示在东南西北四个方向上 前进一步；
// 对于 东南西北这四个方向，可以使用[0-3]来表示方向值。并在持续转向时，像时钟一样 得到“有效的方向值”
// 概念：有效的方向值、指令序列、机器人的当前位置、距离原点的欧氏距离
public class Solution_simulate_walking_robot_in_a_grid {
    public static void main(String[] args) {
        /* 规则：#1 -2：向左转 90 度; #2 -1：向右转 90 度; #3 1<= x <=9：向前移动 x 个单位长度; */
        int[] commandSequence = {4, -1, 4, -2, 4};
        int[][] obstaclesCoord = {{2, 4}};

        int maxDistance = robotSim(commandSequence, obstaclesCoord);

        System.out.println("机器人执行指令过程中距离原点的最大欧式距离为： " + maxDistance);
    }

    private static int robotSim(int[] commandSequence, int[][] obstaclesCoord) {
        /* 〇 准备一个集合对象，并把所有的障碍物的坐标存储到该对象中（以字符串的形式）*/
        // 准备一个集合
        Set<String> obstacleCoordSet = new HashSet<>();

        // 把所有的障碍物添加到集合中
        for (int[] currentObstacleCoord : obstaclesCoord) {
            int obstacleCoordX = currentObstacleCoord[0];
            int obstacleCoordY = currentObstacleCoord[1];

            obstacleCoordSet.add(obstacleCoordX + " " + obstacleCoordY); // 以字符串"X Y"的形式添加到集合中
        }

        /* Ⅰ 定义机器人前进的四个方向（使用int[][]数组） */
        // 定义机器人前进的四个方向     手段：一个二维数组
        int[][] allDirections = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

        /* Ⅱ 定义并初始化 机器人的相关变量 */
        int currentDirection = 0, // 初始方向为 向北，把向北的情况 定义为0
                currentCoordX = 0, currentCoordY = 0, // 位置坐标
                currentEuclidDistance = 0; // 欧氏距离结果

        /* Ⅲ 遍历指令，开始执行 */
        for (int currentReceivedCommand : commandSequence) {
            //  规则：#1 -2：向左转 90 度; #2 -1：向右转 90 度; #3 1<= x <=9：向前移动 x 个单位长度;
            // 如果指令为-1, 也就是想让机器人 向右转90度，则：
            if (currentReceivedCommand == -1) {
                // 把当前方向值+1
                // 每次右转，都把方向值+1 方向值的有效范围应该是 [0-3] [北-东-南-西]，因此 +1操作有超出有效范围的风险
                currentDirection++;

                // 如果 当前方向的值 等于4，说明 方向值已经超出有效范围, 则：把方向值更改到正确的值(想象 只有四个刻度的钟表指针)
                if (currentDirection == 4) {
                    currentDirection = 0;
                }
            } else if (currentReceivedCommand == -2) { // 如果指令为-2，也就是要把机器人 向左转90度，
                // 把当前方向值-1 -
                // 每次左转，都把方向值-1 方向值的有效范围应该是 [0-3] [北-东-南-西]，因此 -1操作有超出有效范围的风险
                currentDirection--;

                // 如果当前方向的值 等于-1，说明 方向值已经低于有效范围, 则：把方向值更改到正确的值(想象 只有四个刻度的钟表指针)
                if (currentDirection == -1) {
                    currentDirection = 3;
                }
            } else { // 如果指令不是-1或-2，说明得到了一个移动的指令，则：
                // 尝试按照指令指示，一直向前移动 直到 #1 移动了需要的步数 或者 #2 遇到了障碍物
                // 手段：一个while循环
                while (currentReceivedCommand-- > 0 && // 步数还没有移动完成..
                        !obstacleCoordSet.contains((currentCoordX + allDirections[currentDirection][0]) + " " +
                                                    (currentCoordY + allDirections[currentDirection][1]))) { // 下一步不是一个障碍方格
                    // 更新当前位置的坐标
                    currentCoordX += allDirections[currentDirection][0];
                    currentCoordY += allDirections[currentDirection][1];
                }
            }

            // 每次执行完成指令后，使用 机器人的当前位置 来 尝试更新“最大的欧氏距离”
            currentEuclidDistance = Math.max(currentEuclidDistance,
                    currentCoordX * currentCoordX + currentCoordY * currentCoordY);
        }

        /* Ⅳ 返回最终的 最大的欧氏距离结果 */
        return currentEuclidDistance;
    }

}
// COMMENTS：这个版本其实更好，结构很清楚
