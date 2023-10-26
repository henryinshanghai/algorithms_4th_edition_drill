package com.henry.sort_chapter_02.primary_algorithm_01.shell_sort_03;

public class ShellSortDrill {
    public static void main(String[] args) {
        Comparable[] a = {"S", "H", "E", "L", "L", "S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E",};

        sort(a);

        printItems(a);
    }

    private static void printItems(Comparable[] a) {
        for (Comparable item : a) {
            System.out.print(item + " ");
        }
    }

    private static void sort(Comparable[] a) {

        int itemAmount = a.length;
        int blockSize = initBlockSize(itemAmount);

        while (blockSize >= 1) {
            // 对于闭区间[a[blockSize], a[itemAmount -1]]中的每个元素，逐一执行插入操作
            int startPointOfDisorder = blockSize;
            for (int currentItemToInsert = startPointOfDisorder; currentItemToInsert < itemAmount; currentItemToInsert++) {
                insertItemWithStepPitch(a, currentItemToInsert, blockSize);
            }

            System.out.println("当前的blockSize为： " + blockSize);
            System.out.print("闭区间中的所有元素，插入操作执行完成后的数组为：");
            show(a);
            System.out.println("~~~~~~~~~~~~~~~~~~");

            blockSize /= 3;
        }

    }

    private static void show(Comparable[] a) {
        for (Comparable item : a) {
            System.out.print(item + " ");
        }

        System.out.println();
    }

    private static void insertItemWithStepPitch(Comparable[] a, int itemToInsert, int stepPitch) {
        for (int backwardCursor = itemToInsert; backwardCursor >= stepPitch; backwardCursor -= stepPitch) {
            if (less(a[backwardCursor], a[backwardCursor - stepPitch])) {
                exch(a, backwardCursor, backwardCursor - stepPitch);
            }
        }
    }

    private static int initBlockSize(int itemAmount) {
        int blockSize = 1;
        while (blockSize < itemAmount / 3) {
            blockSize = blockSize * 3 + 1;
        }
        return blockSize;
    }

    private static void exch(Comparable[] a, int i, int j) {
        Comparable temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }
}
