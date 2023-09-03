package com.henry.sort_chapter_02.merge_sort_algorithm_02.marge_bottom_to_up_03;

public class MergeSortBottomUpDrill02 {
    public static void main(String[] args) {
        Comparable[] a = {"M", "E", "R", "G", "E", "S", "O", "R", "T", "B", "O", "T", "T", "O", "M", "U", "P", "E", "X", "A", "M", "P", "L", "E", };

        sort(a);

        printItems(a);
    }

    private static void sort(Comparable[] a) {
        int itemAmount = a.length;
        aux = new Comparable[itemAmount];

        for (int blockSize = 1; blockSize < itemAmount; blockSize *= 2) {
            for (int leftBar = 0; leftBar < itemAmount - blockSize; leftBar += (blockSize * 2)) {
                mergeBlocksInPair(a, leftBar, leftBar + blockSize - 1,
                        Math.min((leftBar + blockSize * 2) - 1, itemAmount - 1));
            }
        }
    }

    private static Comparable[] aux;

    private static void mergeBlocksInPair(Comparable[] a, int leftBarInPair, int middle, int rightBarInPair) {
        for (int cursor = leftBarInPair; cursor <= rightBarInPair; cursor++) {
            aux[cursor] = a[cursor];
        }

        int leftHalveCursor = leftBarInPair;
        int rightHalveCursor = middle + 1;

        for (int currentSpot = leftBarInPair; currentSpot <= rightBarInPair; currentSpot++) {
            if (leftHalveCursor > middle) a[currentSpot] = aux[rightHalveCursor++];
            else if (rightHalveCursor > rightBarInPair) a[currentSpot] = aux[leftHalveCursor++];
            else if (less(aux[leftHalveCursor], aux[rightHalveCursor])) a[currentSpot] = aux[leftHalveCursor++];
            else a[currentSpot] = aux[rightHalveCursor++];
        }
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
