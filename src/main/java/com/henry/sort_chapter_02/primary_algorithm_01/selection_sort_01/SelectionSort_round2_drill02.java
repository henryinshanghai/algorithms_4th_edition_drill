package com.henry.sort_chapter_02.primary_algorithm_01.selection_sort_01;

import edu.princeton.cs.algs4.StdOut;
// 逐一排定数组中的元素 ———— 找到/选择到未排定区间中的最小元素，并排定它
// String[] a = new String[]{"S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"};
public class SelectionSort_round2_drill02 {

    public static void sort(Comparable[] a) {
        int N = a.length;

        for (int i = 0; i < N; i++) {
            int min = i;
            for (int j = i+1; j < N; j++) {
                if (less(a[j], a[min])) {
                    min = j;
                }
            }

            // 排定当前位置i上的元素
            exch(a, i, min);
        }
    }

    private static void exch(Comparable[] a, int i, int j) {
        Comparable temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    private static void show(Comparable[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + " ");
        }

        System.out.println();
    }

    public static void main(String[] args) {
        String[] a = {"S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"};

        sort(a);
        show(a);
    }
}
