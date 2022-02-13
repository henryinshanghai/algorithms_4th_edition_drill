package com.henry.leetcode_traning_camp.week_04.day06;

public class Solution_jumpGame_ii_02_method04_bfs_by_nationalSite {
    private static int jump(int[] A) {
        /* 〇 准备一些变量*/
        // 当前跳跃的步数
        int jumps = 0;
        // 当前可达的范围终点位置
        int currEnd = 0;
        // 当前可达的范围内所能扩展出的最远位置————这个位置会成为新的区间终点位置
        int currFarthest = 0;

        /* Ⅰ 遍历数组中的每一个元素 */
        for (int i = 0; i < A.length; i++) {
            /* 如果给定的数组并不能到达数组末尾，则：返回-1 */
            // 手段：判断当前指针的位置i 与 当前可达范围中所能扩展出的最远距离currFarthest 预期：最远位置应该大于当前位置
            // 因为最远位置一直在更新，所以如果出现上述情况 说明终点是不可达的
            if (i > currFarthest) return -1;

            // 2 更新当前范围内所能达到的最远位置
            currFarthest = Math.max(currFarthest, i + A[i]);

            // 3 判断是不是已经走到当前范围的终点，如果是，说明该跳一步 到达下一个范围中了
            if (i == currEnd) {
                // 跳一步
                jumps++;
                // 更新当前范围的终点位置
                currEnd = currFarthest;
            }

            // 4 如果当前范围的终点位置 大于 数组的终点位置
            // 说明数组的终点位置已经在当前范围内了   aka 不会需要再跨一步了
            if (currEnd >= A.length - 1) {
                break; // 则：直接break
            }
        }

        /* Ⅱ 返回跳到数组终点位置所需要的步数 */
        return jumps;
    }

    public static void main(String[] args) {

//        int[] steps = {2,3,1,1,4};
        int[] steps = {3,2,1,0,4};

        int minimumStepsToEnd = jump(steps);

        System.out.println("按照当前的数字序列 到达数组末尾所需要的最小步数为： " + minimumStepsToEnd);

    }
}
/*
This is an implicit bfs solution.
    i == curEnd means you visited all the items on the current level.
    Incrementing jumps++ is like incrementing the level you are on.
    And curEnd = curFarthest is like getting the queue size (level size) for the next level you are traversing.
thinking: at each step, you have multiple options. that's where BFS come from

early jump out:
    if (currEnd >= A.length - 1) {
        break; // 则：直接break
    }

judge if it is an good array:
// 手段：判断当前指针的位置i 与 当前可达范围中所能扩展出的最远距离currFarthest 预期：最远位置应该大于当前位置
// 因为最远位置一直在更新，所以如果出现上述情况 说明终点是不可达的
            if (i > currFarthest) return -1;

 */
