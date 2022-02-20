package com.henry.sort_chapter_02.merge_sort_algorithm_02.marge_down_to_top_03;

/*
    分治思想的方式：用更小规模的问题的解 来解决原始问题。

    自顶向下： 大问题 -> 拆解成为小问题 -> 解决小问题 -> 聚合小问题的解，以解决大问题
    自底向上： 小问题 -> 聚合小问题的解，解决大问题。

    自底向上的算法描述：
        先归并这些个微型数组，然后再 成对地归并所得到的子数组；

    难点：如何区分这些个微型数组？

    // 1 定义分组的大小
       创建不同的size, 并根据size来对原始数组进行 分组归并 -
       分组大小从1开始，逐渐变大; 下一组的分组大小比起上一组翻倍

    // 2 定义归并操作的范围 与 游标
        不断向右移动游标，完成对当前所有分组的归并
        游标的下一个位置H：当前位置 + 分组大小*2   rightBar的位置：H - 1  注：这种计算方式可能会导致游标超界限，所以需要取较小值
        middle的位置： leftBar + size - 1
 */
public class MergeBU_round02_drill02 {
    private static Comparable[] aux;

    public static void sort(Comparable[] a) {
        int N = a.length;
        aux = new Comparable[N];

        sort(a, 0, a.length - 1);
    }

    private static void sort(Comparable[] a, int leftBar, int rightBar) {
        int N = a.length;

        // 定义分组的大小
        for (int groupSize = 1; groupSize <= N / 2; groupSize *= 2) {
            // 定义归并操作的范围 与 游标
            for (int leftBarCursor = 0; leftBarCursor < N - groupSize; leftBarCursor+=groupSize*2) {
                merge(a, leftBarCursor, leftBarCursor + (groupSize - 1), Math.min(leftBarCursor + groupSize * 2 - 1, N - 1));

            }
        }

    }

    private static void merge(Comparable[] a, int leftBar, int middle, int rightBar) {
        int leftHalfCursor = leftBar;
        int rightHalfCursor = middle + 1;

        for (int cursor = leftBar; cursor <= rightBar; cursor++) {
            aux[cursor] = a[cursor];
        }

        for (int cursor = leftBar; cursor <= rightBar; cursor++) {
            if(leftHalfCursor > middle) a[cursor] = aux[rightHalfCursor++];
            else if(rightHalfCursor > rightBar) a[cursor] = aux[leftHalfCursor++];
            else if(less(aux[leftHalfCursor], aux[rightHalfCursor])) a[cursor] = aux[leftHalfCursor++];
            else a[cursor] = aux[rightHalfCursor++];
        }
    }

    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    public static void printItems(Comparable[] a) {
        int N = a.length;

        for (int cursor = 0; cursor < N; cursor++) {
            System.out.print(a[cursor] + " ");
        }

        System.out.println();
    }

    public static void main(String[] args) {
        String[] a = new String[]{"M", "E", "R", "G", "E", "S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"};

        sort(a);

        printItems(a);
    }
}
