package com.henry.sort_chapter_02.merge_sort_algorithm_02.merge_01;

import edu.princeton.cs.algs4.StdOut;

public class Merge_drill01 {

    /**
     * 对数组中的元素进行排序
     *
     * @param a
     */
    public static void sort(Comparable[] a) {
        // 参考具体的算法实现
    }

    /**
     * 作用：对指定的数组中的元素进行排序；
     * 手段：
     * 1 借助辅助函数aux，把数组分成左右两个子数组；
     * 2 比较两个子数组对应位置的元素大小； 使用指针
     * 3 根据比较结果，把元素从小到大地覆盖回原始数组中
     *
     * @param a   指定的数组
     * @param lo  数组的开始位置
     * @param mid 数组的中间位置
     * @param hi  数组的结尾位置
     */
    public static void merge(Comparable[] a, int lo, int mid, int hi) {
        // 把a[lo...mid]与a[mid+1...hi]进行归并
        // 定义指针
        int i = lo;
        int j = mid + 1; // 指针会被不停地更新来适应需求
        int N = a.length;
        Comparable[] aux = new Comparable[N];

        // 1 拷贝原始数组到辅助数组中
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
        }

        // 2 为数组的每个位置按照预期绑定数据
        // 手段：比较两个子数组中对应位置元素的大小  具体方法：比较 & 移动指针
        for (int k = lo; k <= hi; k++) { // 外循环需要(hi - lo)次 因为数组有这么多的元素
            // 为数组的位置绑定正确的数据
            // 特殊情况率先处理
            if (i > mid) { // 左边的数组元素用完了...
                a[k] = aux[j++];
            } else if (j > hi) { // 右边的数组元素用完了...
                a[k] = aux[i++];
            }else if (less(aux[j], aux[i])) { // 找到两者中的较小值绑定到a[k]上
                a[k] = aux[j++];
            } else {
                a[k] = aux[i++];
            }
        }
        // 循环结束后，a[]数组中的元素就应该都是从小到大排列的了
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
//        sort(a);

        show(a);

        merge(a, 0, (a.length-1)/2, a.length-1);
        System.out.println("____ 调用merge()之后 ____");
        show(a);
        // 断言数组元素已经有序了
        assert isSorted(a);
    }

}
