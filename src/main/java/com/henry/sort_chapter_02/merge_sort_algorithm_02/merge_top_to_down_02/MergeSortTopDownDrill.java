package com.henry.sort_chapter_02.merge_sort_algorithm_02.merge_top_to_down_02;

public class MergeSortTopDownDrill {

    public static void main(String[] args) {
        Comparable[] a = {"M", "E", "R", "G", "E", "S", "O", "R", "T", "T", "O", "P", "D", "O", "W", "N", "E", "X", "A", "M", "P", "L", "E",};

        sort(a);

        printItems(a);
    }

    private static void sort(Comparable[] a) {
        int itemAmount = a.length;
        aux = new Comparable[itemAmount];

        sort(a, 0, itemAmount - 1);
    }

    private static void sort(Comparable[] a, int leftBar, int rightBar) {
        if (leftBar >= rightBar) return;

        int middle = leftBar + (rightBar - leftBar) / 2;

        sort(a, leftBar, middle);
        sort(a, middle + 1, rightBar);

        mergeSortedRange(a, leftBar, middle, rightBar);
    }

    private static Comparable[] aux;

    private static void mergeSortedRange(Comparable[] a, int leftBar, int middle, int rightBar) {
        for (int cursor = leftBar; cursor <= rightBar; cursor++) {
            aux[cursor] = a[cursor];
        }

        int leftHalveCursor = leftBar;
        int rightHalveCursor = middle + 1;

        for (int currentSpot = leftBar; currentSpot <= rightBar; currentSpot++) {
            if (leftHalveCursor > middle) a[currentSpot] = aux[rightHalveCursor++];
            else if (rightHalveCursor > rightBar) a[currentSpot] = aux[leftHalveCursor++];
            else if (less(aux[leftHalveCursor], aux[rightHalveCursor])) a[currentSpot] = aux[leftHalveCursor++];
            else a[currentSpot] = aux[rightHalveCursor++];

        }
    }

    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    private static void printItems(Comparable[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + " ");
        }
    }
}
