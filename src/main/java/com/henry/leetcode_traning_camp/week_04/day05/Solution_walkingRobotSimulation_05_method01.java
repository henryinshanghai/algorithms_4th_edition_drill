package com.henry.leetcode_traning_camp.week_04.day05;

import javafx.util.Pair;

import java.util.HashSet;
import java.util.Set;

public class Solution_walkingRobotSimulation_05_method01 {
    public static void main(String[] args) {

        int[] commandSequence = {4, -1, 4, -2, 4};
        int[][] obstacles = {{2, 4}};

        int maxDistance = robotSim(commandSequence, obstacles);

        System.out.println("机器人执行指令过程中距离原点的最大欧式距离为： " + maxDistance);

    }

    private static int robotSim(int[] commandSequence, int[][] obstacles) {
        // 定义左右方向
        int[] direx = {0, 1, 0, -1};
        // 定义上下方向
        int[] direy = {1, 0, -1, 0};

        // 初始化机器人的当前坐标
        int curx = 0, cury = 0;
        // 初始化机器人的当前方向
        int curdire = 0;

        // 获取到指令序列中指令的个数
        int comLen = commandSequence.length;
        // 准备一个int类型的变量     用于：？？？
        int ans = 0;
        // 准备一个set对象    用于存储障碍物
        Set<Pair<Integer, Integer>> obstacleSet = new HashSet<>();

        // 把障碍物的坐标添加到set对象中
        for (int i = 0; i < obstacles.length; i++) {
            obstacleSet.add(new Pair<>(obstacles[i][0], obstacles[i][1]));
        }

        // 遍历指令序列中的指令，并执行之
        for (int i = 0; i < comLen; i++) {
            /* 列举额所有可能的指令 */
            // 指令-1表示右转90度
            if (commandSequence[i] == -1) {
                // 更新机器人的当前方向
                curdire = (curdire + 1) % 4;
            } else if (commandSequence[i] == -2) { // -2表示左转90度
                // 更新机器人的当前方向
                curdire = (curdire + 3) % 4;
            } else { // 否则，机器人需要前进指定的步数
                for (int j = 0; j < commandSequence[i]; j++) {
                    // 计算机器人移动一步之后的坐标
                    int nx = curx + direx[curdire];
                    int ny = cury + direy[curdire];

                    // 如果移动后的坐标出现在了obstacle集合中，说明没有遇到障碍物...
                    if (!obstacleSet.contains(new Pair<>(nx, ny))) {
                        // 记录下当前的位置，并计算出此时的欧氏距离以更新ans
                        curx = nx;
                        cury = ny;
                        ans = Math.max(ans, curx * curx + cury * cury);
                    } else { // 如果遇到了障碍物，停止前进 终止本条指令，准备执行下一条指令
                        break;
                    }
                }
            }
        }
        return ans;

    }

}
