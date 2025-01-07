package com.henry.leetcode_traning_camp.week_06.day01.fibonacci_sequence;

// 验证：对于 求fibonacci数列的第N项元素 的问题，可以用 准备一个ordinalNoToItsFibonacciItem[]数组来存储所有斐波那契数字的方式 来 避免递归过程中的重复计算
// 为了避免递归过程中的重复计算，可以使用一个 有意义的数组 来 记录下所有“已经计算出的fibo值”
public class Solution_fibonacci_via_recursion_with_memo {
    public static void main(String[] args) {
        int ordinalNumber = 20;
        int[] ordinalNumberToItsFibonacciItem = new int[ordinalNumber + 1];
        int fibonacciItem = getFibonacciItemOn(ordinalNumber, ordinalNumberToItsFibonacciItem);
        System.out.println("斐波那契数列的第" + ordinalNumber + "项的元素值为：" + fibonacciItem); // 6765
    }

    private static int getFibonacciItemOn(int ordinalNumber, int[] currentSpotToItsFibonacciItemArr) {
        if (ordinalNumber <= 1) {
            return ordinalNumber;
        }

        // 如果 当前序号在数组中对应的元素为0 ，说明当前位置/序号上的斐波那契数字 没有被计算过
        if (currentSpotToItsFibonacciItemArr[ordinalNumber] == 0) {
            // 则：计算出 当前序号/位置上的 斐波那契数字，并把它存到 currentSpotToItsFibonacciItemArr数组 对应的位置上
            // 作用：用作缓存，供之后的计算使用
            currentSpotToItsFibonacciItemArr[ordinalNumber]
                = getFibonacciItemOn(ordinalNumber - 1, currentSpotToItsFibonacciItemArr)
                    + getFibonacciItemOn(ordinalNumber - 2, currentSpotToItsFibonacciItemArr);
        }

        // 返回 数组在传入的位置上的数值 - 即为斐波那契序列在该位置上的数字
        return currentSpotToItsFibonacciItemArr[ordinalNumber];
    }
}
// 注：这种方式的时间复杂度是线性的
/*
优化：
    使用数组元素来逐步向后计算；
    自顶向下地计算，从较小值计算得到最大值

方法1：分治 + 记忆化；
方法2：自底向上进行递推
 */
