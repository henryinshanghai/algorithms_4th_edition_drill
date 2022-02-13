package com.henry.leetcode_traning_camp.week_06.day03;

import java.util.Arrays;

public class Solution_maxProduct_01_method02_dp_byNationalSite {
    public static void main(String[] args) {
        int[] arr = {2,3,-2,4};
        int maxProduct = maxProduct(arr);
        System.out.println("数组" + Arrays.toString(arr) + "中存在的乘积最大的子数组的乘积值为： " + maxProduct);

    }

    private static int maxProduct(int[] nums) {
        if (nums.length == 0) return -1;

        int n = nums.length;
        // 存储当前找到的最大值
        int r = nums[0];

        // imax/imin 存储 以当前数值nums[i]作为结束位置的 所有子数组集合中 乘积最大/最小的结果
        for (int i = 1, imax = r, imin = r; i < n; i++) {
            // 当乘以一个负数时，会使 大的原始值得到小的结果。而小的原始值得到大的结果
            // 所以我们通过交换它们 来重新定义极端值
            if (nums[i] < 0) {
                swap(imax, imin);
            }

            // 与加和一样，以当前元素为结束的子数组集合的最大乘积 要么是当前元素自己
            // 要么是前一个元素 乘以 当前元素
            imax = Math.max(nums[i], imax * nums[i]);
            imin = Math.min(nums[i], imin * nums[i]);

            // 最新计算出的max的值 是我们全局结果的一个候选者
            r = Math.max(r, imax);
        }

        return r;
    }

    private static void swap(int a, int b) {
        int temp = a;
        a = b;
        b = temp;
    }
}
/*
全球站解法的说明：
这个题目与“加和值最大的连续子数组”问题非常类似。
这里你需要记录两个值：
    从先前任意位置到当前位置 的数组集合中的 最大乘积的子数组的乘积值；
    从先前位置到当前位置 的数组集合中的 最小乘积的子数组的乘积值；
如果能够为每一个索引都存储这两个值（比如 maxProduct[i]、minProduct[i]），那么就能够
更容易地看出来问题本身的DP结构

在每一个新item上，你有两种选择：
    1 把新的item 乘到 既有的乘积上；
    2 或者从新的item开始来重新计算 乘积 aka 清除掉先前的乘积结果；
这就是为什么会有两个 Math.max()行；

如果我们看到一个负数值, 那么 最大值的候选者就应该是先前的 最小乘积；
因为一个 比较大的数字 * 一个负数，会得到一个比较小的结果。
所以这里需要swap()

参考：https://leetcode.com/problems/maximum-product-subarray/discuss/48230/Possibly-simplest-solution-with-O(n)-time-complexity

 */
/*
与 Nick的解法的比较：
1 我不太习惯这种把变量初始化写在for循环中的做法，但其实这里的初始化工作只会被执行一次
2 其实就只是循环体中的代码有点子不一样而已
    全球站上的做法：
        1 判断当前item的正负情况，如果当前item为负，则：交换当前imax 与 imin的值；
        2 然后从 当前item 与 当前item * 旧的乘积值 中，选择较大值/较小值来更新 imax、imin
        3 使用当前计算出的imax 来更新 global_max

    Nick的做法
    // 1 由于 current_max：1 会被更新； 2 在后续的计算中还会被用到； 所以在这里使用一个变量来记录下它的当前值
    // 2 记录当前 乘积最大的子数组 的乘积值
    // 3 记录下当前 乘积最小的子数组 的乘积值
    // 4 更新最大乘积值 fina_max
 */
