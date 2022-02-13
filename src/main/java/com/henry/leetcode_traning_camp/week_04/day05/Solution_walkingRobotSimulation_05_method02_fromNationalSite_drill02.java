package com.henry.leetcode_traning_camp.week_04.day05;

import java.util.HashSet;
import java.util.Set;

public class Solution_walkingRobotSimulation_05_method02_fromNationalSite_drill02 {
    public static void main(String[] args) {

        int[] commandSequence = {4, -1, 4, -2, 4};
        int[][] obstacles = {{2, 4}};

        int maxDistance = robotSim(commandSequence, obstacles);

        System.out.println("机器人执行指令过程中距离原点的最大欧式距离为： " + maxDistance);
    }

    private static int robotSim(int[] commandSequence, int[][] obstacles) {
        /* 〇 准备一个集合对象，并把所有的障碍物的坐标存储到该对象中（以字符串的形式）*/
        Set<String> set = new HashSet<>();
        for (int[] obs : obstacles) {
            set.add(obs[0] + " " + obs[1]);
        }

        /* Ⅰ 定义机器人前进的四个方向（使用int[][]数组） */
        int[][] directions = {
                {0, 1},
                {1, 0},
                {0, -1},
                {-1, 0}
        }; // use index to indicate direction


        /* Ⅱ 定义并初始化机器人的当前状态：位置坐标、方向(d=0)、欧氏距离结果 */
        int x = 0, y = 0;
        int currDir = 0;
        int distance = 0;

        /* Ⅲ 遍历指令，开始执行 */
        for (int command : commandSequence) {
            if (command == -1) { // 右转 具体场景：从向北转到向东
                currDir++;

                if (currDir == 4) {
                    currDir = 0;
                }
            } else if (command == -2) { // 左转   具体场景：从向北转到向西
                // 手段：在方向数组中移动一个位置
                currDir--;

                // 如果移动的位置超出边界，就回到最开始的位置
                if (currDir == -1) {
                    currDir = 3;
                }
            } else { // 前进N步
                while (command-- > 0
                        &&
                        !set.contains(
                                (x + directions[currDir][0]) + " " + (y + directions[currDir][1])
                        )) {
                    x += directions[currDir][0];
                    y += directions[currDir][1];
                }
            }

            distance = Math.max(distance, x * x + y * y);
        }

        return distance;
    }
}
/*
EXPR
    1 表示四个方向的方式：一个二维数组；
        int[][] = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    2 记录障碍物坐标的位置；
        手段：
            1 使用一个String类型的集合对象；
            2 坐标使用字符串的方式来存储；

    3 把整型数据转化成为字符串；
        手段：使用+来连接整型数字 与 " ";
        注：在转变成为字符串之前，整形数据需要先进行正确的计算；
*/

