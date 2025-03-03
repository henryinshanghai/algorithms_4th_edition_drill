package com.henry.leetcode_traning_camp.week_06.preview.longest_common_substr.arr_basics;

// 验证#1：数组的length属性 就是 初始化数组时所指定的长度参数的值；
// 验证#2：使用 arr[index]的方式 来 索引数组元素时，index的取值范围是 [0, length-1];
// 因为 索引下标是 从0开始的
public class ArrPropertiesDemo {
    public static void main(String[] args) {
        int[] a = new int[5];

        System.out.println(a.length); // 数组的长度 就是初始化时 所指定的长度

        // 索引数组元素时的有效下标：0, 1, 2, 3, 4
        for (int validIndex = 0; validIndex < a.length; validIndex++) {
            System.out.println("索引" + validIndex + "所对应的数组元素为：" + a[validIndex]);
        }
        // 而如果使用 a[5]来引用数组元素，就会有 运行时报错”数组索引越界异常“
        System.out.println(a[5]);
    }
}
