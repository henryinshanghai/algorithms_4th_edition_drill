package com.henry.sort_chapter_02.primary_algorithm_01.insert_sort_02;

import edu.princeton.cs.algs4.StdOut;

public class InsertSort_02_drill03 {
    /**
     * 对数组中的元素进行排序
     * @param a
     */
    public static void sort(Comparable[] a){
        // 插入排序
        int N = a.length;
        for (int i = 0; i < N; i++) {
            // 内循环作用：把当前元素交换到有序区正确的位置上      手段：把当前元素与它的前一个元素进行比较&交换操作
//            for (int j = i; j < N; j--) {
//                if (j == 0) { // 修改1： j>0时才允许进入循环
//                    break;
//                }
//                if (less(a[j], a[j - 1])) { // 修改2：把if()中的条件作为循环条件————这样能够及时地终止循环
//                    exch(a, j, j - 1);
//                }
//             }

            for (int j = i; (j>0) && less(a[j], a[j-1]); j--) { // j--用于在数组中移动当前区间
                exch(a, j, j - 1);
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
        String[] a = new String[]{"E", "E", "G", "M", "R", "A", "C", "E", "R", "T"}; // 10
        sort(a);

        // 断言数组元素已经有序了
        assert isSorted(a);
        show(a);
    }
}
