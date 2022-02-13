package com.henry.sort_chapter_02.merge_sort_algorithm_02.merge_top_to_down_02;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Merge {
    // 实例变量
    private static Comparable[] aux; // 归并操作所需要使用的辅助数组

    // 公共方法     为使用者提供便利
    public static void sort(Comparable[] a) {
        aux = new Comparable[a.length];
        sort(a, 0, a.length - 1);
    }

    // 私有方法     在类中处理细节

    /**
     * 既然merge()方法已经能够实现：对数组中的元素进行排序； 为什么还需要再写一个sort()方法呢？
     * 答：merge()方法只把原始数组分成了两个子数组。对于大数组来说，速度很慢
     * sort()方法使用递归来解决这个问题~
     *
     * 作用：对数组a中的元素进行排序；
     * 递归调用的两个要点：1 看功能，不看细节； 2 更新参数，缩小规模
     * @param a
     * @param lo
     * @param hi
     */
    private static void sort(Comparable[] a, int lo, int hi) {
        // 对数组a进行排序 aka a[lo...hi]
        // 1 描述递归的基线条件 改条件下调用开始返回
        if (hi <= lo) { // 特殊情况率先处理
            return; // 基线条件：两个指针相等了     因为每次调用都会更新hi/lo的值，让它们不断接近   最终会得到一个不需要排序的子数组
        }

        // 2 更新方法调用时的参数
        int mid = lo + (hi - lo) / 2; // 计算mid的值 会在每次调用时更新mid吗？会的，因为每次调用时传入的lo/hi都会是新值

        // 3 调用自身 分别对当前数组的两个半边进行排序  手段：参数传入更新后的值
        sort(a, lo, mid); // 左半边    更新hi参数的值为：mid
        sort(a, mid + 1, hi); // 右半边 更新lo参数的值为：mid+1

        // 4 每次调用，都归并两个排序好的子数组（左半边 + 右半边）来对整个数组排序
        merge(a, lo, mid, hi);
    }

    /**
     * 对数组中的元素进行排序————归并方法
     */
    public static void merge(Comparable[] a, int lo, int mid, int hi) {
        // 用于比较元素的指针
        int i = lo;
        int j = mid + 1; // 预期mid为数组的中间位置

        int N = a.length;
        aux = new Comparable[N]; // 辅助数组    把merge从一个局部变量修改成为实例变量————这样在其他方法中也就能使用这个变量了

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
        String[] a = new String[]{"E", "E", "G", "M", "R", "A", "C", "E", "R", "T"}; // 10
        sort(a);

        // 断言数组元素已经有序了
        assert isSorted(a);
        show(a);
    }
}
