package com.henry.sort_chapter_02.primary_algorithm_01.selection_sort_01;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class SelectionSort_01_drill01 {
    // 实例变量完全不需要，一切都依赖于方法的参数

    /**
     * 对数组中的元素进行排序
     * @param a
     */
    public static void sort(Comparable[] a){
        int N = a.length;
        for (int i = 0; i < N; i++) {
            int min = i; // min：记录最小值的位置    把当前元素视为最小值，所以min = i
            for (int j = i+1; j < N; j++) {
                if (less(a[j], a[min])) {
                    min = j; // 更新最小值所在的位置  这里并没有改变数组的任何东西
                }
            }

            exch(a, i, min);
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
