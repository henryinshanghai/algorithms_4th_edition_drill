package com.henry.leetcode_traning_camp.week_04.day05;

import java.util.HashSet;
import java.util.Set;

public class Solution_walkingRobotSimulation_05_method02_fromNationalSite_drill01 {
    public static void main(String[] args) {
        int[] commandSequence = {4, -1, 4, -2, 4};
        int[][] obstacles = {{2, 4}};

        int maxDistance = robotSim(commandSequence, obstacles);

        System.out.println("机器人执行指令过程中距离原点的最大欧式距离为： " + maxDistance);
    }

    private static int robotSim(int[] commandSequence, int[][] obstacles) {

        /* 〇 准备一个集合对象，并把所有的障碍物的坐标存储到该对象中（以字符串的形式）*/
        Set<String> obstaclesSet = new HashSet<>();
        for (int[] obs : obstacles) {
            obstaclesSet.add(obs[0] + " " + obs[1]);
        }

        /* Ⅰ 定义机器人前进的四个方向（使用int[][]数组） 从y轴开始顺时针 */
        int[][] directions = new int[][]{
                {0, 1},
                {1, 0},
                {0, -1},
                {-1, 0}
        }; // 二维数组中的每一个元素表示一个方向

        /* Ⅱ 定义并初始化机器人的当前状态：位置坐标、方向、欧氏距离结果 */
        int x = 0, y = 0;
        int currDirection = 0; // 初始方向向北:0-th
        int result = 0; // 准备一个变量存储最大的欧氏距离

        /* Ⅲ 遍历指令，开始执行 */
        // 每次执行完成指令后，使用当前机器人的位置来更新一次result
        for (int currCommand : commandSequence) {
            if (currCommand == -1) {
                // 调整方向：从当前位置右转90度 aka 顺时针转到下一个方向
                currDirection++;

                // 判断是不是已经超出了方向数组的下标范围
                if (currDirection == 4) { // 如果超下标...
                    currDirection = 0; // 马上归零
                }
            } else if (currCommand == -2) { // -2：向左转 90 度
                currDirection--;

                if (currDirection == -1) {
                    currDirection = 3;
                }
            } else {
                while (currCommand-- > 0 &&
                        // EXPR:如果这里的contains()中的字符串不添加小括号的话，就会导致结果为80 而不是 65？！
                        !obstaclesSet.contains(
                                (x + directions[currDirection][0]) + " " + (y + directions[currDirection][1])
                        )) { // 这里是先把证书相加 然后再转化成为字符串
                    x += directions[currDirection][0];
                    y += directions[currDirection][1];
                }
            }

            result = Math.max(result, x * x + y * y);
        }

        return result;
    }
}
