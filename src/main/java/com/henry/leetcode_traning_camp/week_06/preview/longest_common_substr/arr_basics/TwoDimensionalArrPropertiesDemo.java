package com.henry.leetcode_traning_camp.week_06.preview.longest_common_substr.arr_basics;

// 验证#1：初始化二维数组arr[i][j]时，i表示的是二维数组的行数，j表示的是二维数组的列数
// 验证#2：初始化二维数组中的某一行时，不能超过 初始化时所指定的列数，否则会在运行阶段报错
public class TwoDimensionalArrPropertiesDemo {
    public static void main(String[] args) {
        int[][] a = new int[2][3]; // 行索引为0,1 列索引为0, 1, 2

        // 行数 & 列数
        System.out.println("二维数组a的行数为： " + a.length);
        System.out.println("二维数组a的列数为： " + a[0].length);

        // 如果给第一行 初始化一个 长度超长的数组，编译时不会报错
        a[0] = new int[]{1, 2, 3, 4, 5};
        // 但在运行时会报错
//        printArrItems(a);
    }

    private static void printArrItems(int[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                System.out.print(a[i][j] + " ");
            }
            System.out.println();
        }
    }
}
