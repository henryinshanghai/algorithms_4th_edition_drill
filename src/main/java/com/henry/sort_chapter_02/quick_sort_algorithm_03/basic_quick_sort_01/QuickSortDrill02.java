package com.henry.sort_chapter_02.quick_sort_algorithm_03.basic_quick_sort_01;

public class QuickSortDrill02 {
    public static void main(String[] args) {
        Comparable[] a = {"Q", "U", "I", "C", "K", "S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"};

        sort(a);

        printItems(a);
    }

    private static void sort(Comparable[] a) {
        int itemAmount = a.length;

        sort(a, 0, itemAmount - 1);
    }

    private static void sort(Comparable[] a, int leftBar, int rightBar) {
        if (leftBar >= rightBar) return;

        int arrangeSpot = arrangePivotViaPartition(a, leftBar, rightBar);
        sort(a, leftBar, arrangeSpot - 1); // [leftBar, arrangeSpot - 1]
        sort(a, arrangeSpot + 1, rightBar);
    }

    private static int arrangePivotViaPartition(Comparable[] a, int leftBar, int rightBar) {
        Comparable pivotItem = a[leftBar]; //  使用 a[leftBar] 而不是 a[0]

        int lessZoneBoundary = leftBar;
        int greaterZoneBoundary = rightBar + 1;

        while (true) {
            while(less(a[++lessZoneBoundary], pivotItem)) if (lessZoneBoundary >= rightBar) break;
            while(less(pivotItem, a[--greaterZoneBoundary])) if (greaterZoneBoundary <= leftBar) break;
            if (lessZoneBoundary >= greaterZoneBoundary) break;
            exch(a, lessZoneBoundary, greaterZoneBoundary);
        }

        exch(a, leftBar, greaterZoneBoundary); // 使用leftBar 而不是0

        return greaterZoneBoundary; // 使用 greaterZoneBoundary
    }

    public static void exch(Comparable[] a, int i, int j) {
        Comparable temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    private static void printItems(Comparable[] a) {
        for (Comparable item : a) {
            System.out.print(item + " ");
        }
    }
}
