package com.henry.sort_chapter_02.merge_sort_algorithm_02.merge_01;

// #1 sort leftHalfZone, rightHalfZone;
// #2 using leftHalfCursor, rightHalfCursor to compare item in left and right half of aux, then use the smaller one to fill original array.
public class MergeSortDrill {
    private static Comparable[] aux;

    public static void sort(Comparable[] a) {
        aux = new String[a.length];
        sort(a, 0, a.length - 1);
    }

    private static void sort(Comparable[] a, int leftBar, int rightBar) {
        if (leftBar >= rightBar) return;

        int middle = leftBar + (rightBar - leftBar) / 2;
        sort(a, leftBar, middle);
        sort(a, middle+1, rightBar);

        merge(a, leftBar, middle, rightBar);
    }

    // merge the sorted subArray in closed scope.
    private static void merge(Comparable[] a, int leftBar, int middle, int rightBar) {
        for (int cursor = leftBar; cursor <= rightBar; cursor++) {
            aux[cursor] = a[cursor];
        }

        int leftHalfCursor = leftBar;
        int rightHalfCursor = middle + 1;

        for (int cursor = leftBar; cursor <= rightBar; cursor++) {

            if (leftHalfCursor > middle) a[cursor] = aux[rightHalfCursor++];
            else if(rightHalfCursor > rightBar) a[cursor] = aux[leftHalfCursor++];
            else if(less(a[leftHalfCursor], a[rightHalfCursor])) a[cursor] = aux[leftHalfCursor++];
            else a[cursor] = aux[rightHalfCursor++];
        }

    }

    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    private static void show(Comparable[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + " ");
        }
    }

    public static void main(String[] args) {
        String[] a = {"I", "L", "O", "V", "E", "A", "L", "I", "C", "I", "A"};

        sort(a);
        show(a);
    }
}