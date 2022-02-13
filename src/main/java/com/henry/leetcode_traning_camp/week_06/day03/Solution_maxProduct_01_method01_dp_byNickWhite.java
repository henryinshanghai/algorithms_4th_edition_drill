package com.henry.leetcode_traning_camp.week_06.day03;

import java.util.Arrays;

public class Solution_maxProduct_01_method01_dp_byNickWhite {
    public static void main(String[] args) {
        int[] arr = {2,3,-2,4};
        int maxProduct = maxProduct(arr);
        System.out.println("数组" + Arrays.toString(arr) + "中存在的乘积最大的子数组的乘积值为： " + maxProduct);
    }

    private static int maxProduct(int[] nums) {
        /* 〇 */
        if (nums.length == 0) return -1;

        /* Ⅰ 准备递推过程的初始变量 */
        int current_max = nums[0]; // 以当前元素作为结束位置的 最大乘积
        int current_min = nums[0]; // 以当前元素作为结束位置的 最小乘积

        int final_max = nums[0]; // 当前所计算出的所有位置的最大乘积 中的最大值

        /* Ⅱ 开始遍历数组，进行递推 */
        for (int i = 1; i < nums.length; i++) {
            // 1 由于 current_max：1 会被更新； 2 在后续的计算中还会被用到； 所以在这里使用一个变量来记录下它的当前值
            int temp = current_max;

            // 2 记录当前 乘积最大的子数组 的乘积值
            current_max = Math.max(
                        Math.max(current_max * nums[i], current_min * nums[i])
                        , nums[i]); // 三种选择：使用当前的最大值product * item、当前的最小值product * item、当前元素item

            // 3 记录下当前 乘积最小的子数组 的乘积值
            current_min = Math.min(
                    Math.min(temp * nums[i], current_min * nums[i]),
                    nums[i]
            );

            // 4 更新最大乘积值 fina_max
            if (current_max > final_max) {
                final_max = current_max;
            }
        }

        /* Ⅲ 返回最大乘积值final_max */
        return final_max;
    }
}
