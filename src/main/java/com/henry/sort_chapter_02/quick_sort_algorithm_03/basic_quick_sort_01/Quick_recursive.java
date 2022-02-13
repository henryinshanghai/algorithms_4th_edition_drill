package com.henry.sort_chapter_02.quick_sort_algorithm_03.basic_quick_sort_01;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Quick_recursive {
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
     * 对数组进行排序
     * 手段：快速排序
     * @param a
     * @param lo
     * @param hi
     */
    private static void sort(Comparable[] a, int lo, int hi) {
        // 1 递归调用的基线条件
        if (hi <= lo) {
            return;
        }

        // 2 选择一个切分元素，并把它放到正确的位置
        int j = partition(a, lo, hi); // 选取一个元素作为切分元素————把该元素放到正确的位置上

        // 3 递归调用sort()对子数组进行排序
        sort(a, lo, j-1); // 对左半部分进行排序 a[lo...j-1]
        sort(a, j+1, hi); // 对右半部分进行排序 a[j+1...hi]
    }

    /**
     * 选择一个元素作为切分元素，并返回元素排序后的位置
     * @param a
     * @param lo
     * @param hi
     * @return
     */
    private static int partition(Comparable[] a, int lo, int hi) {
        int i = lo;
        int j = hi + 1;
        Comparable v = a[lo];

        // 这时候用while比用for就省事多了 不存在步长什么的
        while (true) {
            // 从左边查找比基准元素更大的元素所在的位置
            while (less(a[++i], v)) { // a[i] < v时，循环表达式成立 当a[i] > v（基准元素）时，循环终止
                if (i == hi) {
                    break;
                }
            }

            // 从右边查找比基准元素更小的元素所在的位置
            while (less(v, a[--j])) {
                if (j == lo) {
                    break;
                }
            }

            // 到这里就已经得到所需要的i、j
            // 判断i、j是否已经碰头/交叉       说明：左边数组的最后一个元素的位置是j，而不是i
            // 因为最后一次元素交换导致i与j的值发生了变化————现在j指向左侧数组的最后一个元素
            if (i >= j) {
                break;
            }

            // 交换更大元素与更小元素的位置
            exch(a, i, j);
        }

        // 最后，把基准元素与左边数组的最后一个元素交换位置
        exch(a, lo, j);

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
        // 从标准输入中读取字符串，然后把它们排序输出
//        String[] a = In.readStrings();
        String[] a = new String[]{"E", "E", "G", "M", "R", "A", "C", "E", "R", "T"}; // 10
        sort(a);

        // 断言数组元素已经有序了
        assert isSorted(a);
        show(a);
    }
}
