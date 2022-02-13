package com.henry.leetcode_traning_camp.week_04.day06;

public class Solution_jumpGame_ii_02_method04_bfs_by_nationalSite_drill01 {
    public static void main(String[] args) {
        int[] steps = {2,3,1,1,4};
//        int[] steps = {3,2,1,0,4};

        int minimumStepsToEnd = jump(steps);

        System.out.println("按照当前的数字序列 到达数组末尾所需要的最小步数为： " + minimumStepsToEnd);
    }

    private static int jump(int[] steps) {
        int currRangeEnd = 0;
        int nextFurthestRangeEnd = 0;
        int jumps = 0;

        // DIFF: - 1
        for (int cursor = 0; cursor < steps.length; cursor++) { // EXPR：为了到达最后一个位置 不需要考虑最后一个位置能跳到哪儿去
//            if (cursor > nextFurthestRangeEnd) {
//                return -1;
//            }

            nextFurthestRangeEnd = Math.max(nextFurthestRangeEnd, cursor + steps[cursor]);

            if (cursor == currRangeEnd) {
                jumps++;
                currRangeEnd = nextFurthestRangeEnd;
            }

            // 如果 当前范围的终点位置 已经超过了 数组的终点位置 则：当前步就能够到达终点了，不需要再跳一步
            if (currRangeEnd >= steps.length - 1) {
                // 直接break
                break;
            }
        }

        return jumps;
    }
}
