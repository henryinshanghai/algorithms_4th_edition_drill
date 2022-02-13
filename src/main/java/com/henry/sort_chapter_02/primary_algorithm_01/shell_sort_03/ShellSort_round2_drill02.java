package com.henry.sort_chapter_02.primary_algorithm_01.shell_sort_03;

/*
算法描述： 对原始数组进行 h排序， 直到h的值为1
{"S", "H", "E", "L", "L", "S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"}
 */
public class ShellSort_round2_drill02 {

    public static void sort(Comparable[] a) {
        int N = a.length;

        /* 动态生成span的序列 */
        int span = 1;
        while(span < N/3) span = span * 3 + 1;

        while (span >= 1) {
            int startPointOfDisorder = span;
            for (int currentBoundary = startPointOfDisorder; currentBoundary < N; currentBoundary++) {
                for (int backwardsCursor = currentBoundary;
                     (backwardsCursor >= span) && less(a[backwardsCursor], a[backwardsCursor - span]);
                     backwardsCursor -= span) {
                    exch(a, backwardsCursor, backwardsCursor - span);
                }
            }

            span = span / 3;
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

    public static void printItems(Comparable[] a) {
        int N = a.length;

        for (int i = 0; i < N; i++) {
            System.out.print(a[i] + " ");
        }

        System.out.println();
    }

    public static void main(String[] args) {
        String[] a = new String[]{"S", "H", "E", "L", "L", "S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"};

        sort(a);

        printItems(a);
    }
}
