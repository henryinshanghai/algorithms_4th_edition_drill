package com.henry.sort_chapter_02.quick_sort_algorithm_03.basic_quick_sort_01;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Quick_recursive_drill01 {

    /**
     * 对数组中的元素进行排序
     * @param a
     */
    public static void sort(Comparable[] a){
        // 快速排序
        StdRandom.shuffle(a);
        sort(a, 0, a.length-1);
    }


    /**
     * 对数组中的元素进行排序
     * 快速排序
     *
     * @param a
     */
    public static void sort(Comparable[] a, int lo, int hi) {
        // 1 打乱原始数组
        // 为什么不能在sort()方法中打乱数组  答：因为sort()方法会被递归调用，这会导致每次调用时，数组a都是不同的数组
//        StdRandom.shuffle(a); // todo it is wrong to shuffle the array inside of sort, figure out why later

        // 2 指定基线条件
        if (lo >= hi) {
            return;
        }

        // 3 指定切分元素并交换到需要的位置
        int bar = partition(a, lo, hi);

        // 4 对子数组应用sort()方法
        sort(a, lo, bar - 1);
        sort(a, bar + 1, hi);
    }

    /**
     * 指定切分元素并交换到需要的位置
     *
     * @param a
     * @param lo
     * @param hi
     */
    private static int partition(Comparable[] a, int lo, int hi) {
        int i = lo;
        int j = hi + 1;
        Comparable bar = a[lo];

        while (true) {

            // 获取到左边起第一个大于基准元素的元素位置
            while (less(a[++i], bar)) {
                if (i == hi) {
                    break;
                }
            }

            // 获取到右边起第一个小于基准元素的元素位置
            while (less(bar, a[--j])) {
                if (j == lo) {
                    break;
                }
            }

            if (i >= j) {
                break;
            }

            exch(a, i, j);
        }

        exch(a, lo, j); // 必须是j
        return j;
    }

    @SuppressWarnings("unchecked")
    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    /**
     * 交换i、j这两个位置的元素
     *
     * @param a
     * @param i
     * @param j
     */
    private static void exch(Comparable[] a, int i, int j) {
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    private static void show(Comparable[] a) {
        // 在单行中打印数组
        for (int i = 0; i < a.length; i++) {
            StdOut.print(a[i] + " ");
        }
        System.out.println();
    }

    public static boolean isSorted(Comparable[] a) {
        // 测试数组中的元素是否有序
        for (int i = 0; i < a.length; i++) {
            if (less(a[i], a[i - 1])) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        // 从标准输入中读取字符串，然后把它们排序输出
//        String[] a = In.readStrings();
        String[] a = new String[]{"E", "E", "G", "M", "R", "A", "C", "E", "R", "T"}; // 10
//        sort(a, 0, a.length - 1);

        sort(a);
        // 断言数组元素已经有序了
        assert isSorted(a);
        show(a);
    }
}
