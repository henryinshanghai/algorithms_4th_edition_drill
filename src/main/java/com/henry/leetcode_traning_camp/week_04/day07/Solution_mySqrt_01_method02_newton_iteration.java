package com.henry.leetcode_traning_camp.week_04.day07;

public class Solution_mySqrt_01_method02_newton_iteration {
    public static void main(String[] args) {
        int x = 8;
        int res = mySqrt(x);
        System.out.println(x + "的平方根的整数部分为：" + res);
    }

    private static int mySqrt(int x) {
        if (x == 0) return 0;


        /*
            原理参考：
                1 https://leetcode-cn.com/problems/sqrtx/solution/niu-dun-die-dai-fa-by-loafer/
                2 https://oi-wiki.org/math/newton/
            牛顿迭代法公式：
                更近似的解x1 =  当前近似解x0 - （x0对应的函数值f(x0)）/函数在自变量等于x0时的导数值
                公式化简：
                    更近似的解x1 = 1/2 * （当前近似解x0 + N/当前近似解x0）
         */
        // Ⅰ 任意取一个数字作为当前的近似解————这里的x其实就是x0
        long i = x;
        while (i > x / i) { // Ⅲ 判断计算得到的最新的近似解x1 是不是 比精确平方根 更大, 如果是，就继续找更精确的近似解
            // Ⅱ 使用牛顿迭代法来计算出更精确的近似解x1
            i = (i + x / i) / 2;
        }

        // Ⅳ 循环结束后，会得到一个近似解————取这个近似解的整数部分即可
        return (int) i;
    }
}
