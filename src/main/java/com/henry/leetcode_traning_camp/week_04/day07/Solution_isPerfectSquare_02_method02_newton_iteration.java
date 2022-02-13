package com.henry.leetcode_traning_camp.week_04.day07;

public class Solution_isPerfectSquare_02_method02_newton_iteration {
    public static void main(String[] args) {
//        int num = 16;
        int num = 14;
        boolean res = isPerfectSquare(num);

        System.out.println("num" + "是一个完全平方数？" + res);
    }

    private static boolean isPerfectSquare(int num) {
        // 牛顿迭代法不是用来求近似解的吗？要怎么转化为“判断是否为平方根”的作用的？
        /*
            1 使用牛顿迭代法求出 num的近似平方根；
            2 如果num是一个完全平方数，那么求得的近似平方根肯定是一个整数。则：
                对此结果求平方，肯定能够得到原始数值
              而如果num不是一个完全平方数，那么求得的近似平方根就会有损耗。则：
                对这个根求平方，得到的结果不等于原始值
         */
        if (num == 0 || num == 1) {
            return true;
        }

        long currApproxiAnswer = num;
        while (currApproxiAnswer > num / currApproxiAnswer) {
            currApproxiAnswer = (currApproxiAnswer + num / currApproxiAnswer) / 2;
        }

        return currApproxiAnswer * currApproxiAnswer == num;

    }
}
