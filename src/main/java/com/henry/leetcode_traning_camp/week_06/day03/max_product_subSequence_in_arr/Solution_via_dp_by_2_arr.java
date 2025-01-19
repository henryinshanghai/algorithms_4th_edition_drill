package com.henry.leetcode_traning_camp.week_06.day03.max_product_subSequence_in_arr;

import java.util.Arrays;

// 验证：可以使用 维护两个dp[]数组的方式 来 求得数组中乘积最大的子数组的maxProduct
// 特征：由于 原始数组中可能存在有负数，所以 #1 需要维护一个minProduct[]; #2 在currentItem为负数时，需要交换 minProduct 与 maxProduct
// 目标结果并不是maxProduct[]数组的最后一个元素，而是 maxProduct[]数组的最大元素（使用遍历+更新的方式得到）
public class Solution_via_dp_by_2_arr {
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

        // 准备两个dp[]数组：dpA[] 用于记录 以当前位置作为结束位置的最大乘积，dpB[] 用于记录 以当前位置作为结束位置的最小成绩
        int[] currentSpotToMaxProductUpToIt = new int[numAmount + 1];
        int[] currentSpotToMinProductUpToIt = new int[numAmount + 1];

        // 初始化 dp[]数组的首元素，用以 正确地驱动递推公式
        currentSpotToMaxProductUpToIt[0] = numArr[0]; // 位置0的maxProduct为 元素本身
        currentSpotToMinProductUpToIt[0] = numArr[0]; // 位置0的minProduct为 元素本身

        for (int currentSpot = 1; currentSpot < numAmount; currentSpot++) {
            // 如果 当前数组元素 是一个负数，说明 与它相乘后，最大值会变成最小值&最小值会变成最大值，则👇
            // #1 为了得到最大乘积值，我们应该 使用“当前最小的乘积值”来与它相乘 - 这样才能保证 乘积结果在数轴中尽可能地靠右(也就是尽可能地大)
            // #2 为了得到“最小乘积值”，我们应该 使用“当前最大的乘积值”来与它相乘 - 这样才能保证 乘积结果尽可能地小
            // 为此，我们在相乘之前，先 交换 最大乘积值 与 最小乘积值
            if (numArr[currentSpot] < 0) {
                swap(currentSpotToMaxProductUpToIt[currentSpot], currentSpotToMinProductUpToIt[currentSpot]);
            }

            // 与加和一样，当前元素 * 当前数组元素乘积后得到的结果可能会变大或变小；
            // 如果变大，说明 需要 把当前元素包含进 当前子数组，则：maxProductOfCurrentSpot 取 乘积后的结果
            // 如果变小，说明 需要 以当前元素作为起点 重开子数组，则：maxProductOfCurrentSpot 取 当前元素
            // 总之，取 两者中的较大者 来 更新 以当前位置作为结束位置的所有子数组中的最大乘积
            currentSpotToMaxProductUpToIt[currentSpot]
                    = Math.max(numArr[currentSpot], numArr[currentSpot] * currentSpotToMaxProductUpToIt[currentSpot - 1]);
            // 同理，维护 minProduct的变量值，取 当前元素 与 当前min*乘上当前元素的结果 两者中的较小者
            currentSpotToMinProductUpToIt[currentSpot]
                    = Math.max(numArr[currentSpot], numArr[currentSpot] * currentSpotToMinProductUpToIt[currentSpot - 1]);

            // 尝试使用 当前位置的max 来 更新全局max
            maxProductOfAllSpot = Math.max(maxProductOfAllSpot, currentSpotToMaxProductUpToIt[currentSpot]);
        }

        return maxProductOfAllSpot;
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
