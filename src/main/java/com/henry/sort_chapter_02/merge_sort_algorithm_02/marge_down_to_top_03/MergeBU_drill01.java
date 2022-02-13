package com.henry.sort_chapter_02.merge_sort_algorithm_02.marge_down_to_top_03;

import edu.princeton.cs.algs4.StdOut;

/**
 * 归并排序
 * 自底向上
 * 特征：
 * 1 没有使用递归；
 * 2 方法参数很多
 */
public class MergeBU_drill01 {

    private static Comparable[] aux;

    /**
     * 对数组中的元素进行排序
     *
     * @param a
     */
    public static void sort(Comparable[] a) {
        // 使用循环调用merge()来逐步归并
        int N = a.length;


        for (int unit = 1; unit < N; unit *=2 ) { // 每次划分数组时，单元所包含的元素数都翻倍
            // 内循环的作用：从数组头部开始，顺序地合并“数组划分后得到的每一个分组”
            // 一个子数组 = 2 * 单元
            for (int lo = 0; lo < N - unit; lo += 2 * unit) { // 跳转到下一个分组
                int mid = lo + unit - 1;
                int hi = Math.min((lo + 2 * unit - 1), N-1);
                // 执行归并操作
                merge(a, lo, mid, hi);
            }
        }
    }

    /**
     * 对数组中的元素进行排序
     * 手段：归并
     *
     * @param a
     * @param lo
     * @param mid
     * @param hi
     */
    private static void merge(Comparable[] a, int lo, int mid, int hi) {
        // 1 定义比较指针
        int i = lo;
        int j = mid + 1;

        aux = new Comparable[a.length];

        // 2 拷贝元素到辅助数组
        for (int k = 0; k < a.length; k++) {
            aux[k] = a[k];
        }

        // 3 为a[]的每个位置绑定/覆盖正确的元素
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
        String[] a = new String[]{"E", "E", "G", "M", "R", "A", "C", "E", "R", "T"}; // 10
//        sort(a, 0, a.length - 1);
        sort(a);

        // 断言数组元素已经有序了
        assert isSorted(a);
        show(a);
    }

}
