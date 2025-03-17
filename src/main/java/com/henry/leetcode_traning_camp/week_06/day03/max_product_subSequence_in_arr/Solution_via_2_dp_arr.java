package com.henry.leetcode_traning_camp.week_06.day03.max_product_subSequence_in_arr;

import java.util.Arrays;

// 验证：可以使用 维护两个dp[]数组的方式 来 求得数组中乘积最大的子数组的maxProduct
// 特征：由于 原始数组中可能存在有负数，所以 #1 需要维护一个minProduct[];
// #2 在currentItem为负数时，需要交换 minProduct 与 maxProduct
// dp[]数组的具体含义：currentSpotToMaxProductEndWithIt[] + currentSpotToMinProductEndWithIt[]
// 获取目标值：由于每个currentSpot上记录的maxProduct之间是相互独立的，因此
// 目标结果并不是maxProduct[]数组的最后一个元素，而是 maxProduct[]数组的最大元素（使用遍历+更新的方式得到）
public class Solution_via_2_dp_arr {
    public static void main(String[] args) {
        int[] numArr = {2, 3, -2, 4};
//        int[] numArr = {-2, 0, -1};
        int maxProductOfSubArr = getProductOfMaxSubArrayIn(numArr);
        System.out.println("数组" + Arrays.toString(numArr) + "中存在的乘积最大的子数组的乘积值为： " + maxProductOfSubArr);

    }

    private static int getProductOfMaxSubArrayIn(int[] numArr) {
        if (numArr.length == 0) return -1;
        int numAmount = numArr.length;

        // 用于存储当前找到的最大值，初始化为 数组的第一个元素
        int maxProductOfAllSpot = Integer.MIN_VALUE; // numArr[0]

        // 准备两个dp[]数组：dpA[] 用于记录 以当前位置作为结束位置的所有子数组中 乘积最大的子数组的乘积值，
        // dpB[] 用于记录 以当前位置作为结束位置的所有子数组中 乘积最小的子数组的乘积值
        int[] currentSpotToMaxProductEndWithIt = new int[numAmount + 1];
        int[] currentSpotToMinProductEndWithIt = new int[numAmount + 1];

        // 初始化 dp[]数组的首元素，用以 正确地驱动递推公式
        currentSpotToMaxProductEndWithIt[0] = numArr[0]; // 位置0的maxProduct为 元素本身
        currentSpotToMinProductEndWithIt[0] = numArr[0]; // 位置0的minProduct为 元素本身

        for (int currentSpot = 1; currentSpot < numAmount; currentSpot++) {
            // #1 如果 当前数组元素 是一个负数，说明 与它相乘后，最大值会变成最小值&最小值会变成最大值，则👇
            // #1-1 为了得到“最大乘积值”，我们应该 使用“当前最小的乘积值”来与它相乘 - 这样才能保证 乘积结果在数轴中尽可能地靠右(也就是尽可能地大)
            // #1-2 为了得到“最小乘积值”，我们应该 使用“当前最大的乘积值”来与它相乘 - 这样才能保证 乘积结果尽可能地小
            // 为此，对于“当前数组元素为负数”的情况，我们在 乘进当前子数组元素 之前，要先 交换 “当前最大乘积值” 与 “当前最小乘积值”
            if (numArr[currentSpot] < 0) {
                swap(currentSpotToMaxProductEndWithIt[currentSpot], currentSpotToMinProductEndWithIt[currentSpot]);
            }

            // #2 与加和一样，当前元素 * 当前数组元素乘积后得到的结果可能会变大或变小；
            // 如果变大，说明 需要 把当前元素 包含进当前子数组，则：maxProductOfCurrentSpot 取 相乘后的结果
            // 如果变小，说明 需要 以当前元素作为起点 重开子数组，则：maxProductOfCurrentSpot 取 当前元素
            // 总之，取 两者中的较大者 来 更新 以当前位置作为结束位置的所有子数组中的最大乘积
            currentSpotToMaxProductEndWithIt[currentSpot]
                    = Math.max(numArr[currentSpot], numArr[currentSpot] * currentSpotToMaxProductEndWithIt[currentSpot - 1]);
            // 同理，维护 minProduct的变量值，取 当前元素 与 当前min*乘上当前元素的结果 两者中的较小者
            currentSpotToMinProductEndWithIt[currentSpot]
                    = Math.min(numArr[currentSpot], numArr[currentSpot] * currentSpotToMinProductEndWithIt[currentSpot - 1]);

            // #3 尝试使用 当前位置的max 来 更新全局max
            // 原因：由于每个 currentSpot都独立地记录了 以其为结束位置的所有子数组中的最大乘积，因此所有的maxProduct之间是相互独立的。需要遍历它们得到 max of maxProduct
            maxProductOfAllSpot = Math.max(maxProductOfAllSpot, currentSpotToMaxProductEndWithIt[currentSpot]);

            System.out.println("原始数组为👇");
            printArr(numArr);

            System.out.println("当前currentSpotToMaxProductEndWithIt[]数组为👇");
            printArr(currentSpotToMaxProductEndWithIt);

            System.out.println("当前currentSpotToMinProductEndWithIt[]数组为👇");
            printArr(currentSpotToMinProductEndWithIt);

            System.out.println("当前的maxProductOfAllSpot为👇");
            System.out.println(maxProductOfAllSpot);
            System.out.println();
        }

        return maxProductOfAllSpot;
    }

    private static void printArr(int[] currentSpotToMaxProductUpToIt) {
        for (int currentSpot_i = 0; currentSpot_i < currentSpotToMaxProductUpToIt.length; currentSpot_i++) {
            System.out.print(currentSpotToMaxProductUpToIt[currentSpot_i] + " ");
        }

        System.out.println();
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
    从先前任意位置到“当前位置” 的数组集合中的 最大乘积的子数组的乘积值；
    从先前任意位置到“当前位置” 的数组集合中的 最小乘积的子数组的乘积值；
如果能够为每一个索引都存储这两个值（比如 maxProduct[i]、minProduct[i]），那么就能够
更容易地看出来问题本身的DP结构

在每一个新item上，你有两种选择：
    1 把新的item 乘到 既有的乘积上；
    2 或者从新的item开始来重新计算 乘积 aka 清除掉先前的乘积结果；
这就是为什么会有两个 Math.max()行；

如果我们看到一个负数值, 那么 为了得到“最大值的候选者”，就应该使用“现在的最小乘积值”；
因为一个 比较大的数字 * 一个负数，会得到一个比较小的结果。
所以这里需要swap()

参考：
https://leetcode.com/problems/maximum-product-subarray/discuss/48230/Possibly-simplest-solution-with-O(n)-time-complexity

 */