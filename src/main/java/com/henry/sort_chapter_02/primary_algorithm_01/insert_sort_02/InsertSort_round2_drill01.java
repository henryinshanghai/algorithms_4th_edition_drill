package com.henry.sort_chapter_02.primary_algorithm_01.insert_sort_02;

// 通过插入操作，不断扩展有序区间
// 插入 = 连续比较 + 连续交换
public class InsertSort_round2_drill01 {

    public static void sort(Comparable[] a) {
        int N = a.length;

        // 插入 = 连续比较 + 连续交换
        for (int currBoundary = 1; currBoundary < N; currBoundary++) {
            for (int backwardCursor = currBoundary; backwardCursor > 0 && less(a[backwardCursor], a[backwardCursor - 1]); backwardCursor--) {
                exch(a, backwardCursor, backwardCursor - 1);
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

    private static void printItems(Comparable[] a) {
        int N = a.length;

        for (int cursor = 0; cursor < N; cursor++) {
            System.out.print(a[cursor] + " ");
        }

        System.out.println();
    }

    public static void main(String[] args) {
        String[] a = {"S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"};
        sort(a);

        printItems(a);
    }
}
