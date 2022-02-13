package com.henry.sort_chapter_02.quick_sort_algorithm_03.basic_quick_sort_01;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Quick_recursive_drill02 {
    /**
     * 对数组中的元素进行排序
     * @param a
     */
    public static void sort(Comparable[] a){
        // 参考具体的算法实现
        StdRandom.shuffle(a);
        sort(a, 0, a.length-1);
    }

    /**
     * 对数组中的元素进行排序
     * 手段:快速排序；
     * 具体方法：1 xxx； 2 xxx 递归调用sort()方法； 3 xxx
     * @param a
     * @param lo
     * @param hi
     */
    private static void sort(Comparable[] a, int lo, int hi) {
        // 1 基本条件
        if (lo >= hi) {
            return;
        }

        // 2 缩小规模
        // 设置一个partition元素
        int bar = partition(a, lo, hi);

        sort(a, lo, bar - 1);
        sort(a, bar + 1, hi);
    }

    private static int partition(Comparable[] a, int lo, int hi) {
        int i = lo;
        int j = hi + 1;
        Comparable bar = a[lo];

        // 处理数组中的元素
        while (true) {
            // 从左边找到第一个比bar大的元素     手段：使用while来遍历查找
            while (less(a[++i], bar)) {
                if (i == hi) {
                    break;
                }
            }

            // 从右边找到第一个比bar小的元素     Mark:查找元素的位置
            while (less(bar, a[--j])) { // Mark:++与--的用法 & 指针的初始化值
                if (j == lo) {
                    break;
                }
            }

            if (i >= j) {
                break;
            }

            // 交换i、j位置上的元素
            exch(a, i, j);
        }

        // 把切分元素与左边最后一个元素互换位置
        exch(a, lo, j); // Mark：位置是j
        return j;
    }

    @SuppressWarnings("unchecked")
    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    /**
     * 交换i、j这两个位置的元素
     * @param a
     * @param i
     * @param j
     */
    private static void exch(Comparable[] a, int i, int j) {
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    private static void show(Comparable[] a){
        // 在单行中打印数组
        for (int i = 0; i < a.length; i++) {
            StdOut.print(a[i] + " ");
        }
        System.out.println();
    }

    public static boolean isSorted(Comparable[] a){
        // 测试数组中的元素是否有序
        for (int i = 0; i < a.length; i++) {
            if (less(a[i], a[i - 1])) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        // 写死数组
        String[] a = new String[]{"E", "E", "G", "M", "R", "A", "C", "E", "R", "T"}; // 10
        sort(a);

        // 断言数组元素已经有序了
        assert isSorted(a);
        show(a);
    }
}
