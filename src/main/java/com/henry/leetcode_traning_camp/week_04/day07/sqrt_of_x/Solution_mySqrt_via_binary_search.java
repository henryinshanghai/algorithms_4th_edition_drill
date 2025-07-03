package com.henry.leetcode_traning_camp.week_04.day07;

// 验证：可以使用二分查找的方式 来 查找/计算出一个给定整数的平方根的整数部分
// 概念：左闭右闭区间、平方根值、平方根的整数部分、平方根值的下一个整数:ceiling(平方根值)
// 原理：
// #1 按照 {#1 mid偏大则调整右区间； #2 否则调整左区间}的区间调整规则，区间的左右边界 一定会在 “平方根值的下一个整数”处相遇
// #2 平方根的整数部分 = 平方根值的下一个整数 - 1；
public class Solution_mySqrt_via_binary_search {
    public static void main(String[] args) {
//        int givenIntegerNumber = 8; // 2
//        int givenIntegerNumber = 9; // 3
//        int givenIntegerNumber = 10; // 3
        int givenIntegerNumber = 2147395599;
        int integerPartOfSqrtResult = mySqrtOf(givenIntegerNumber);

        System.out.println(givenIntegerNumber + "的平方根结果的整数部分为：" + integerPartOfSqrtResult);
    }

    private static int mySqrtOf(int givenIntegerNumber) {
        // 〇 参数判断
        if (givenIntegerNumber == 0 || givenIntegerNumber == 1) {
            return givenIntegerNumber;
        }

        // Ⅰ 初始化 区间的边界位置(左闭右闭区间) 与 区间的中间位置
        long leftBarNum = 1, rightBarNum = givenIntegerNumber;
        long midNum = 1; // EXPR1：mid的值初始化为多少都无所谓，因为它的值都会被更新

        // Ⅱ 进行循环，不断缩小查找区间————直到左边界与右边界相遇
        int counter = 0;
        // 🐖 我们想要计算的并不是真正的平方根值，而是平方根值的整数部分
        // 如果区间的左右边界相遇，说明二分查找 已经粗略定位到了 平方根值，则：
        // 此时有：ceiling(平方根值) = leftBarNum = rightBarNum = midNum, 也就是 循环会再执行一次
        // 🐖 由于当前midNum比起平方根值更大，因此 此次循环一定会 把rightBarNum-1，(rightBarNum-1)就是 我们想要得到的平方根值的整数部分
        while (leftBarNum <= rightBarNum) { // EXPR2：这里是 <= 符号————这会产生一个现象：leftBarNum = right时，循环还会继续执行

            // 计算出 当前区间的中间位置数字
            midNum = leftBarNum + (rightBarNum - leftBarNum) / 2;
            System.out.println("当前第" + (++counter) + "次循环中，leftBarNum = " + leftBarNum +
                    ", rightBarNum = " + rightBarNum +
                    ", midNum = " + midNum);

            // 如果 midNum的平方结果 大于 给定的整数数字，说明 该midNum 比起 平方根的值 更大，则：
            if (midNum * midNum > givenIntegerNumber) {
                // 更新 当前区间的右边界 来 #1 缩小搜索区间； #2 确保区间 仍旧包含 平方根的值
                System.out.println("midNum = " + midNum + " 的平方结果 大于 给定的数字 " + givenIntegerNumber + " ，更新 区间的右边界");
                rightBarNum = midNum - 1;
            } else { // 否则，说明 该midNum 小于或等于 平方根的值，则：
                // 更新 当前区间的左边界 来 #1 缩小搜索区间； #2 确保区间 仍旧包含平方根的值
                System.out.println("midNum = " + midNum + " 的平方结果 小于或等于 给定的数字 " + givenIntegerNumber +" ，更新 区间的左边界");
                leftBarNum = midNum + 1;
            }
            // 按照以上调整区间的规则，区间的左右边界 必然会在 大于平方根的下一个整数处:ceiling(平方根)（对于10来说，就是数字4）相遇👆

            System.out.println("当前第" + (counter) +"次循环结束后，leftBarNum = " + leftBarNum +
                    ", rightBarNum = " + rightBarNum +
                    ", midNum = " + midNum);
            System.out.println();
        }

        // Ⅲ 返回跳出循环前范围的某个边界————具体是左边界还是右边界需要进行调试，因为循环结束时，left与right指针的位置顺序可能已经发生变化
        // EXPR3: 这里必须要返回 区间的右边界数字的整数部分即可(题目要求)
        System.out.println("while循环结束后，leftBarNum = " + leftBarNum +
                ", rightBarNum = " + rightBarNum +
                ", midNum = " + midNum);
        return (int) rightBarNum;
    }
} // 不断更新区间，直到区间左右边界 在ceiling(平方根)的位置上相遇/交叉，然后拿 ceiling(平方根)-1 就能得到 平方根的整数部分结果
