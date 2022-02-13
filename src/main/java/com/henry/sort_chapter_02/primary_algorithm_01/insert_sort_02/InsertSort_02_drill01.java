package com.henry.sort_chapter_02.primary_algorithm_01.insert_sort_02;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class InsertSort_02_drill01 {
    /**
     * 对数组中的元素进行排序
     * @param a
     */
    public static void sort(Comparable[] a) {
        // 插入排序
        int N = a.length;
        for (int i = 0; i < N; i++) { // 获取到当前元素
            // 考虑i=0的情况，也要求j>0才能执行比较操作  手段：&&的短路特性
            for (int j = i; j > 0 && less(a[j], a[j-1]); j--) { // 把比较的操作向前推进
                exch(a, j, j - 1);
            }

        }
    }

    @SuppressWarnings("unchecked")
    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    /**
     * 交换i、j这两个位置的元素
     * @param a
     * @param i
     * @param j
     */
    private static void exch(Comparable[] a, int i, int j) {
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    private static void show(Comparable[] a){
        // 在单行中打印数组
        for (int i = 0; i < a.length; i++) {
            StdOut.print(a[i] + " ");
        }
        System.out.println();
    }

    public static boolean isSorted(Comparable[] a){
        // 测试数组中的元素是否有序
        for (int i = 0; i < a.length; i++) {
            if (less(a[i], a[i - 1])) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        // 从标准输入中读取字符串，然后把它们排序输出
        String[] a = In.readStrings();
        sort(a);

        // 断言数组元素已经有序了
        assert isSorted(a);
        show(a);
    }
}
