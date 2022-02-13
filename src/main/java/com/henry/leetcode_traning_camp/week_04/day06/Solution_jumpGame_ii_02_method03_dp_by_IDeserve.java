package com.henry.leetcode_traning_camp.week_04.day06;

public class Solution_jumpGame_ii_02_method03_dp_by_IDeserve {
    public static void main(String[] args) {
//        int[] steps = {2,3,1,1,4};
        int[] steps = {3,2,1,0,4};

        int minimumStepsToEnd = jump(steps);

        System.out.println("按照当前的数字序列 到达数组末尾所需要的最小步数为： " + minimumStepsToEnd);

    }

    /**
     * 线性时间复杂度来解决跳楼梯问题
     * @param A
     * @return
     */
    private static int jump(int[] A) {
        // 〇 对总的台阶数进行判空
        if (A.length <= -1) {
            return 0;
        }

        // Ⅰ 准备两个变量
        // 记录当前所使用的梯子   当前位置 + 扩展位置
        int ladder = A[0]; // keep track of the largest ladder that you have
        // 记录当前梯子中的台阶数量 扩展得到的台阶数量
        int stairs = A[0]; // keep track of the stairs in the current ladder

        // Ⅱ 准备一个变量 用于记录进行跳跃的数量 初始值为1（因为从初始位置不管怎么跳都要跳一步）
        int jump = 1;

        // Ⅲ 准备一个level指针来遍历所有的台阶
        for (int level = 1; level < A.length; level++) {
            // 1 判断当前台阶是不是已经到了楼顶
            if (level == A.length - 1) {
                return jump;
            }

            // 2 在移动指针的过程中，时时地更新/存储当前台阶能够连接到最远位置的梯子
            if (level + A[level] > ladder) {
                ladder = level + A[level]; // build up the ladder
            }

            // 3 每次向前移动指针，当前梯子中剩余的台阶就会减1
            stairs--; // use up the stairs

            // 4 如果当前梯子的台阶已经用完了，则：
            if (stairs == 0) {
                // 1 跳到下一个梯子
                jump++; // no stairs left, now do a jump
                // 2 更新当前梯子所能前进的台阶数量
                stairs = ladder - level; // now get new set of stairs
            }
        }

        // Ⅳ 返回跳跃的总次数
        return jump;
    }
}
/*
沿着梯子上台阶：
    如果当前楼梯能够搭出更长的梯子，就存储下这个更长的梯子；
        注：存储了更长的梯子后，仍旧在当前梯子的台阶上继续前进。

    如果把当前梯子的所有台阶都走完了，则：
        1 跳到下一个最长的梯子；
        2 更新这个梯子所包含的台阶数量
参考：https://www.youtube.com/watch?v=vBdo7wtwlXs
 */
