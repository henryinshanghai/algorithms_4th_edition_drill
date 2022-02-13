package com.henry.sort_chapter_02.primary_algorithm_01.selection_sort_01;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class SelectionSort_01 {

    /**
     * 对数组中的元素进行排序
     * @param a
     */
    public static void sort(Comparable[] a){
        // 参考具体的算法实现
        int N = a.length;
        for (int i = 0; i < N; i++) {
            // 逐一排定数组中的元素 ———— 找到/选择到未排定区间中的最小元素，并排定它
            // 手段：并将之与当前元素进行交换
            // 特征：每次遍历，都会有一个元素被放到了正确的位置
            /*
                手段：
                    1 遍历未排定区间中的所有元素；
                    2 判断有没有比“当前最小值”更小的元素
                    3 如果有的话，更新最小值所在的位置min
             */
            int min = i; // 记录最小值的位置
            for (int j = i+1; j < N; j++) {
                if (less(a[j], a[min])){
                    min = j;
                }
            }

            exch(a, i, min); // 把最小值元素与当前元素换个位置
        }
    }

    @SuppressWarnings("unchecked")
    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    /**
     * 交换i、j这两个位置的元素
     * 手段： 借助中间元素
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
        String[] a = In.readStrings();
        sort(a);

        // 断言数组元素已经有序了
        assert isSorted(a);
        show(a);
    }

}
