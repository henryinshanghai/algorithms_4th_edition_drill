package com.henry.leetcode_traning_camp.week_04.day05;

import java.util.HashSet;
import java.util.Set;

public class Solution_walkingRobotSimulation_05_method02_fromNationalSite {
    public static void main(String[] args) {
        int[] commandSequence = {4, -1, 4, -2, 4};
        int[][] obstacles = {{2, 4}};

        int maxDistance = robotSim(commandSequence, obstacles);

        System.out.println("机器人执行指令过程中距离原点的最大欧式距离为： " + maxDistance);
    }

    private static int robotSim(int[] commands, int[][] obstacles) {
        /* 〇 准备一个集合对象，并把所有的障碍物的坐标存储到该对象中（以字符串的形式）*/
        // 准备一个集合
        Set<String> set = new HashSet<>();

        // 把所有的障碍物添加到集合中
        for (int[] obs : obstacles) {
            set.add(obs[0] + " " + obs[1]); // 以字符串的形式添加到集合中
        }

        /* Ⅰ 定义机器人前进的四个方向（使用int[][]数组） */
        // 定义机器人前进的四个方向     手段：一个二维数组
        int[][] dirs = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

        /* Ⅱ 定义并初始化机器人的当前状态：位置坐标、方向、欧氏距离结果 */
        int d = 0, x = 0, y = 0, result = 0;

        /* Ⅲ 遍历指令，开始执行 */
        for (int c : commands) {
            // 如果指令为-1
            if (c == -1) {
                // 调整方向
                d++;
                if (d == 4) {
                    d = 0;
                }
            } else if (c == -2) { // 如果指令为-2
                // 调整方向
                d--;
                if (d == -1) {
                    d = 3;
                }
            } else { // 否则，移动机器人
                // 判断移动时是否会碰到障碍物，如果遇到就停下
                // 手段：一个while循环
                while (c-- > 0 &&
                        !set.contains((x + dirs[d][0]) + " " + (y + dirs[d][1]))) {
                    x += dirs[d][0];
                    y += dirs[d][1];
                }
            }

            // 每次执行完成指令后，使用当前机器人的位置来更新一次result
            result = Math.max(result, x * x + y * y);
        }

        /* Ⅳ 返回最终的result */
        return result;
    }

}
// COMMENTS：这个版本其实更好，结构很清楚
