package com.henry.leetcode_traning_camp.week_04.day06;

public class Solution_jumpGame_ii_02_method03_dp_by_IDeserve_drill01 {
    public static void main(String[] args) {
        int[] steps = {2,3,1,1,4};
//        int[] steps = {3,2,1,0,4};

        int minimumStepsToEnd = jump(steps);

        System.out.println("按照当前的数字序列 到达数组末尾所需要的最小步数为： " + minimumStepsToEnd);

    }

    private static int jump(int[] A) {
        // 〇 对总的台阶数进行判空
        if (A.length <= 0) {
            return 0;
        }

        // Ⅰ 准备两个变量
        // 记录当前所使用的梯子   当前位置 + 扩展位置
        int ladder = A[0];
        // 记录当前梯子中的台阶数量 扩展得到的台阶数量
        int stairs = A[0];

        // Ⅱ 准备一个变量
            // 用于记录进行跳跃的数量 初始值为1（因为从初始位置不管怎么跳都要跳一步）
        int jumps = 1; // not understanding

        // Ⅲ 准备一个level指针来遍历所有的台阶
        for (int level = 0; level < A.length; level++) {
            // 1 判断当前台阶是不是已经到了楼顶
            if (level == A.length - 1) {
                return jumps;
            }

            // 2 在移动指针的过程中，时时地更新/存储当前台阶能够连接到最远位置的梯子
            if (level + A[level] > ladder) {
                ladder = level + A[level];
            }

            // 3 每次向前移动指针，当前梯子中剩余的台阶就会减1
            stairs--;

            // 4 如果当前梯子的台阶已经用完了，则：
            if (stairs == 0) {
                jumps++;
                stairs = ladder - level;
            }
        }

        return jumps;
    }
}
