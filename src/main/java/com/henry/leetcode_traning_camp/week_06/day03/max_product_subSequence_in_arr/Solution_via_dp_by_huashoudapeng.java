package com.henry.leetcode_traning_camp.week_06.day03.max_product_subSequence_in_arr;

import java.util.Arrays;

// 验证：当动态规划的递推公式只与dp[]数组的相邻两项有关时，可以使用一个变量(imax, imin)来替换掉dp[]数组，从而节省空间；
// 递推公式：dp[i] = max(nums[i], nums[i] * dp[i-1]);
// 用于替换dp[]的变量 imax => 递推公式：imax = max(nums[i], nums[i] * imax);
public class Solution_via_dp_by_huashoudapeng {
    public static void main(String[] args) {
        int[] numArr = {2, 3, -2, 4};
//        int[] numArr = {-2, 0, -1};
        int maxProductOfSubArr = getProductOfMaxSubArrayIn(numArr);
        System.out.println("数组" + Arrays.toString(numArr) + "中存在的乘积最大的子数组的乘积值为： " + maxProductOfSubArr);

    }

    private static int getProductOfMaxSubArrayIn(int[] numArr) {
        int max = Integer.MIN_VALUE,
            imax = 1, imin = 1; // #1 变量的初始值为1，这样执行乘法后 就能得到”正确的值“

        // #2 这里currentSpot的起点为0
        for (int currentSpot = 0; currentSpot < numArr.length; currentSpot++) {
            if (numArr[currentSpot] < 0) {
                int tmp = imax;
                imax = imin;
                imin = tmp;
            }

            imax = Math.max(imax * numArr[currentSpot], numArr[currentSpot]);
            imin = Math.min(imin * numArr[currentSpot], numArr[currentSpot]);

            // tip: 在循环中，通过不断比较 来 得到最大值
            max = Math.max(max, imax);
        }

        return max;
    }
}
