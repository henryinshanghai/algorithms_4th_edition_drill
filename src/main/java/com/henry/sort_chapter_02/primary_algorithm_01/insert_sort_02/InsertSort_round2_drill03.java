package com.henry.sort_chapter_02.primary_algorithm_01.insert_sort_02;

// 通过插入操作，不断扩展有序区间
// 插入 = 连续比较 + 连续交换
public class InsertSort_round2_drill03 {

    public static void sort(Comparable[] a) {
        int N = a.length;

        for (int currBoundary = 1; currBoundary < N; currBoundary++) {
            for (int backwardsCursor = currBoundary; backwardsCursor > 0 && less(a[backwardsCursor], a[backwardsCursor - 1]) ; backwardsCursor--) {
                exch(a, backwardsCursor, backwardsCursor - 1);
            }
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
        String[] a = new String[]{"S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"};

        sort(a);

        printItems(a);
    }


}
