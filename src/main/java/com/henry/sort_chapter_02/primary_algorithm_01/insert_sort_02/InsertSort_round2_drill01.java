package com.henry.sort_chapter_02.primary_algorithm_01.insert_sort_02;

/*
    算法简要描述：
        把当前元素 插入到一个有序区域中，并保持有序区域的元素有序性。
    算法流程：
        1 把第一个元素视为一个 只有一个元素的有序区域；
        2 获取到有序区域后面的一个元素，把它插入到 当前的有序区域中；
            手段：在有序区域中，使用一个从后往前的游标指针。通过不断地比较与交换，把待插入元素交换到正确的预期位置中(不是最终的排定位置)
            - 1 比较有序区域的最后一个元素 与 当前待插入的元素，如果当前待插入的元素更小，就交换它们；
            - 2 比较有序区域的倒数第二个元素 与 当前待插入的元素，如果当前待插入元素更小，就交换它们；
            如此往复，直到 待插入元素已经到达预期位置 或者 待插入元素已经到达数组的起始位置
        3 对每一个 新的待插入元素，做2中同样的操作

    有意义的变量名：
        N - itemAmount;
        currentBoundary - cursorOfItemToInsert
        backwardsCursor - backwardsCursor

    基础操作：插入 = 连续比较 + 连续交换
 */

import edu.princeton.cs.algs4.In;

public class InsertSort_round2_drill01 {

    public static void sort(Comparable[] a) {
        int itemAmount = a.length;

        // 插入 = 连续比较 + 连续交换
        for (int cursorOfItemToInsert = 1; cursorOfItemToInsert < itemAmount; cursorOfItemToInsert++) {
            // 在有序区间中，从后往前遍历 - 找到当前待处理元素的预期位置，并一步一步地把它交换过去
            /*
                手段：比较 当前元素 与 当前元素的前一个元素，更小则执行交换。否则，就已经到达预期位置
             */
            for (int backwardCursor = cursorOfItemToInsert;
                 backwardCursor > 0 && less(a[backwardCursor], a[backwardCursor - 1]);
                 backwardCursor--) {
                exch(a, backwardCursor, backwardCursor - 1);
            }

            assert isSorted(a, 0, cursorOfItemToInsert);
        }

        assert isSorted(a);
    }

    private static boolean isSorted(Comparable[] a) {
        return isSorted(a, 0, a.length - 1);
    }

    private static boolean isSorted(Comparable[] a, int leftBar, int rightBar) {
        for (int cursor = leftBar + 1; cursor <= rightBar; cursor++) {
            if (less(a[cursor], a[cursor - 1])) return false;
        }

        return true;
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
        // 从标准输入中读取字符串，然后把它们排序输出
        String[] a = In.readStrings();
//        String[] a = {"S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"};
        sort(a);

        printItems(a);
    }
}
