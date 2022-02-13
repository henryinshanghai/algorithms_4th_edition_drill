package com.henry.sort_chapter_02.primary_algorithm_01.selection_sort_01;

// 逐一排定数组中的元素 ———— 找到/选择到未排定区间中的最小元素，并排定它
// String[] a = new String[]{"S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"};
public class SelectionSort_round2_drill03 {

    public static void sort(Comparable[] a) {
        int N = a.length;

        for (int currAnchor = 0; currAnchor < N; currAnchor++) {
            int min = currAnchor;

            for (int cursor = currAnchor+1; cursor < N; cursor++) {
                if (less(a[cursor], a[min])) {
                    min = cursor;
                }
            }

            exch(a, currAnchor, min);
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

    private static void print(Comparable[] a) {
        int N = a.length;

        for (int i = 0; i < N; i++) {
            System.out.print(a[i] + " ");
        }
    }

    public static void main(String[] args) {
        String[] a = {"S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"};
        sort(a);

        print(a);
    }
}
