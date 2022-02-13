package com.henry.leetcode_traning_camp.week_04.day06;

public class Solution_jumpGame_ii_02_method04_bfs_by_nationalSite_drill02 {
    public static void main(String[] args) {
        int[] steps = {2,3,1,1,4};
//        int[] steps = {3,2,1,0,4};
//        int[] steps = {1, 4, 3, 7, 1, 2, 6, 7, 6, 10};

        int minimumStepsToEnd = jump(steps);

        System.out.println("按照当前的数字序列 到达数组末尾所需要的最小步数为： " + minimumStepsToEnd);
    }

    private static int jump(int[] steps) {
        // Ⅰ
        int currRangeEnd = 0;
        int furthestNextRangeEnd = 0;
        int jumps = 0;

        // Ⅱ 遍历每一个需要的当前位置 查看每个位置能够跳到的最远距离 EXPR:不需要考虑最后一个位置
        // 这种方式有自己的问题：当考虑最后一个位置时，你会得到一个错误的结果
        for (int cursor = 0; cursor < steps.length; cursor++) {
            // EXPR: this is only working when you consider every item in step arr.
            if (cursor > furthestNextRangeEnd) {
                return -1;
            }

            furthestNextRangeEnd = Math.max(furthestNextRangeEnd, cursor + steps[cursor]);

            if (cursor == currRangeEnd) {
                jumps++;
                currRangeEnd = furthestNextRangeEnd;
            }

            // 如果没有及时跳出的话，得到的结果值会为 3 ————这是一个错误的结果
            // 添加此判断后，结果为：2
            if (currRangeEnd >= steps.length - 1) {
                break;
            } // EXPR:this is important when you consider every item in the steps arr.

        }

        return jumps;
    }
}
