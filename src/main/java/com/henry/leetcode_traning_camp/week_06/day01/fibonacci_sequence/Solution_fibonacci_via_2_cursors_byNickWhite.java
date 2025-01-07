package com.henry.leetcode_traning_camp.week_06.day01.fibonacci_sequence;

// 验证：对于 求斐波那契序列的第N项元素的数值 的问题，可以使用 while循环 + 双指针的方式 来 得到结果
// 特征：如果要计算第N项 斐波那契数字，则 需要进行(N-1)次循环 => 由此决定while()循环条件的写法
public class Solution_fibonacci_via_2_cursors_byNickWhite {
    public static void main(String[] args) {
        int ordinalNumber = 2;
        int fibonacciItem = fibonacciItemOn(ordinalNumber);
        System.out.println("斐波那契数列的第" + ordinalNumber + "项的元素值为：" + fibonacciItem); // 6765
    }

    private static int fibonacciItemOn(int ordinalNumber) {
        if (ordinalNumber == 0) return 0;
        if (ordinalNumber == 1) return 1;

        /* 准备需要用到的指针,并初始化指针的指向 */
        int previousTwoItem = 0;
        int previousOneItem = 1;
        int currentItem = previousTwoItem + previousOneItem;

        /* 使用递归的方式求出 F(N) */
        int counter = 0;
        while (ordinalNumber > 1) {
            // 求出 fibonacci数列当前元素的数值
            currentItem = previousTwoItem + previousOneItem;
            System.out.println("第" + (++counter) + "次计算斐波那契数字，结果为： " + currentItem);

            // 更新 前两项的指针所指向的元素
            previousTwoItem = previousOneItem;
            // 更新 前一项的指针所指向的元素
            previousOneItem = currentItem;

            // 把序号-1，因为当前项已经计算完成 - 下一次循环计算下一项
            ordinalNumber--;
        }
        System.out.println();

        // 循环结束后，currentItem的值就是 第N项斐波那契数字的数值。返回它
        return currentItem;
    }
}
/*
时间复杂度为线性
属于一种 semi-DP的解法
 */
