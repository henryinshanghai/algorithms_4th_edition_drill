package com.henry.sort_chapter_02.primary_algorithm_01.selection_sort_01;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class SelectionSort_round2_drill01 {

    // 逐一排定数组中的元素 ———— 找到/选择到未排定区间中的最小元素，并排定它
    public static void sort(Comparable[] a) {
        int N = a.length;

        for (int i = 0; i < N; i++) {
            int min = i;

            for (int j = i+1; j < N; j++) {
                if (less(a[j], a[min])) {
                    min = j;
                }
            }

            exch(a, i, min);
        }
    }

    private static void exch(Comparable[] a, int i, int j) {
        Comparable temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private static boolean less(Comparable v1, Comparable v2) {
        return v1.compareTo(v2) < 0;
    }

    private static void show(Comparable[] a){
        // 在单行中打印数组
        for (int i = 0; i < a.length; i++) {
            StdOut.print(a[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        // 从标准输入中读取字符串，然后把它们排序输出
//        String[] a = In.readStrings();
//        String[] a = new String[]{"E", "E", "G", "M", "R", "A", "C", "E", "R", "T"}; // 10
        String[] a = new String[]{"S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"}; // 10
        sort(a);

        // 断言数组元素已经有序了
//        assert isSorted(a);
        show(a);
    }
}
