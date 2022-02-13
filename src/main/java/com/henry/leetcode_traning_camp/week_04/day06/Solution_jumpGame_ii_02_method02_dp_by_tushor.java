package com.henry.leetcode_traning_camp.week_04.day06;

public class Solution_jumpGame_ii_02_method02_dp_by_tushor {
    public static void main(String[] args) {
        int[] arr = {2,3,1,1,4};
//        int[] arr = {3,2,1,0,4};

        int minSteps = minimumJumpSteps(arr);

        System.out.println("按照当前数组跳到数组末尾所需要的最小步数为： " + minSteps);
    }

    private static int minimumJumpSteps(int[] nums) {
        /* 〇 判断数组的长度 如果只有一个元素，所需要的步数就是0步 */
        if (nums.length == 1) {
            return 0;
        }

        /*Ⅰ 定义并初始化两个int类型的变量 */
        int count = 0; // 用于计数当前所走的步数
        int i = 0; // 用于记录当前所在的位置索引

        /*Ⅱ 准备一个while循环   用来更新到达终点位置所需要的最小步数 */
        while (i + nums[i] < nums.length - 1) { // 循环条件：从当前位置还跳不过终点位置
            // 准备两个局部变量
            int maxVal = 0; // 记录从当前位置能够跳到的最远位置
            int maxValIndex = 0; // 记录

            //
            for (int j = 1; j <= nums[i]; j++) {
                if (nums[j + i] + j > maxVal) {
                    maxVal = nums[j + i] + j;
                    maxValIndex = i + j;
                }
            }
            i = maxValIndex;
            count++;
        }
        return count + 1;
    }
} // 这套代码在给定数组无法到达终点位置的情况下不会给出错误结果，但是运行情况类似死循环
// this is another dead end for now, switch to another one
