package com.henry.sort_chapter_02.merge_sort_algorithm_02.merge_top_to_down_02;

import edu.princeton.cs.algs4.StdOut;

public class Merge_recursive_drill03 {
    private static Comparable[] aux;

    /**
     * 对数组中的元素进行排序
     * @param a
     */
    public static void sort(Comparable[] a, int lo, int hi){ // 需要哪些参数？
        // 归并排序
        if (lo >= hi) {
            return;
        }

        int mid = lo + (hi - lo)/2; // mid = hi / 2
        sort(a, lo, mid);
        sort(a, mid + 1, hi);

        merge(a, lo, mid, hi);
    }

    /**
     * 通过归并操作进行排序
     * @param a
     * @param lo
     * @param mid
     * @param hi
     */
    private static void merge(Comparable[] a, int lo, int mid, int hi) {
        int N = a.length;
        aux = new Comparable[N];

        int i = lo;
        int j = mid + 1;


//        for (int k = 0; k < N; k++) { // 这里如果写作[0, N)的话，不会报错 但会有无效的循环 所以会使sort()耗时更久    注：这个结论站不住脚，纯属实验误差
//            aux[k] = a[k];
//        }

        for (int k = lo; k <= hi; k++) { // this is recommended
            aux[k] = a[k];
        }

        // 由于递归调用时，每次传进来的参数值都是不一样的 aka 每次进行的循环次数也会不同    如果写作N的话，就会把循环次数写死为数组长度，导致下标越界
        for (int k = lo; k <= hi; k++) { // todo 未解之谜 已解开  是因为递归调用吗？ 果然是因为递归调用
            if (i > mid) {
                a[k] = aux[j++];
            } else if (j > hi) {
                a[k] = aux[i++];
            } else if (less(aux[i], aux[j])) {
                a[k] = aux[i++]; // 不要忘了写++
            } else {
                a[k] = aux[j++];
            }
        }
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
//        sort(a);
        long startTime = System.nanoTime();
        sort(a, 0, a.length-1);
        System.out.println("耗时：" + (System.nanoTime() - startTime));
        // 断言数组元素已经有序了
        assert isSorted(a);
        show(a);
    }
}
