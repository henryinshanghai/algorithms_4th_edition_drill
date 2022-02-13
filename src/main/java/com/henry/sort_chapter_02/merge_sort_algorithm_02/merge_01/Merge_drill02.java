package com.henry.sort_chapter_02.merge_sort_algorithm_02.merge_01;

import edu.princeton.cs.algs4.StdOut;

public class Merge_drill02 {
    /**
     * 对数组中的元素进行排序
     * @param a
     */
    public static void sort(Comparable[] a){
        // 参考具体的算法实现
    }

    /**
     * 对数组中的元素进行排序————归并方法
     * merge(a, 0, (a.length - 1) / 2, a.length-1);
     */
    public static void merge(Comparable[] a, int lo, int mid, int hi) { // 传入的参数是不变的
        // 用于比较元素的指针 指针是会变化的
        int i = lo;
        int j = mid + 1; // 预期mid为数组的中间位置 

        int N = a.length;
        Comparable[] aux = new Comparable[N]; // 辅助数组
        
        // 1 把原始数组的元素拷贝到辅助数组中
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
        }

        // 2 为原始数组中的所有位置都绑定正确的数据    for()中的变量，作用域仅限于for{}语句块中
        for (int k = lo; k <= hi; k++) {
            if (i > mid) {
                a[k] = aux[j++];
            } else if (j > hi) {
                a[k] = aux[i++];
            } else if (less(aux[i], aux[j])) {
                a[k] = aux[i++];
            } else {
                a[k] = aux[j++];
            }
        }
        // 循环结束时，数据就已经有序了
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

        show(a); // 未排序时的数组

        merge(a, 0, (a.length - 1) / 2, a.length-1);

        // 断言数组元素已经有序了
        assert isSorted(a);
        show(a);
    }
}
