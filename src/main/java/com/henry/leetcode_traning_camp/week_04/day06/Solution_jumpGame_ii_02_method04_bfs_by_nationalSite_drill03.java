package com.henry.leetcode_traning_camp.week_04.day06;

public class Solution_jumpGame_ii_02_method04_bfs_by_nationalSite_drill03 {
    public static void main(String[] args) {
//        int[] steps = {2,3,1,1,4};
        int[] steps = {3,2,1,0,4};
//        int[] steps = {1, 4, 3, 7, 1, 2, 6, 7, 6, 10};

        int minimumStepsToEnd = jump(steps);

        System.out.println("按照当前的数字序列 到达数组末尾所需要的最小步数为： " + minimumStepsToEnd);
    }

    private static int jump(int[] steps) {
        // Ⅰ
        int currRangeEnd = 0;
        int furthestNextRangeEnd = 0;
        int jumps = 0;

        for (int cursor = 0; cursor < steps.length; cursor++) { // PATTERN-1
            // 1 special condition
            if (cursor > furthestNextRangeEnd) {
                return -1;
            }

            // 2
            furthestNextRangeEnd = Math.max(furthestNextRangeEnd, cursor + steps[cursor]);

            // 3
            if (cursor == currRangeEnd) {
                jumps++;
                currRangeEnd = furthestNextRangeEnd;
            }

            // 4 PATTERN-2
            if (currRangeEnd >= steps.length - 1) {
                break;
            }
        }

        return jumps;
    }
}
