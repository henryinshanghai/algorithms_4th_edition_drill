package com.henry.sort_chapter_02.primary_algorithm_01.selection_sort_01;

public class SelectionSort_01_drill02 {
    // 不需要任何实例变量
    public void sort(Comparable[] a){
        // 使用选择排序实现     为什么叫选择排序？ 因为每次都要从数组的剩余部分选择最小元素与无序区的第一个元素交换位置
        int N = a.length;
        for (int i = 0; i < N; i++) {
            // 任务1：对应于当前元素，找到数组剩余部分中的最小元素；
            /*
                手段：1 设置一个min变量标识最小元素的索引值；
                    2 把当前元素视为最小元素 aka 把当前元素的索引绑定到min上
                    3 逐一比较数组中剩余部分的每一个元素与当前的最小元素，并根据比较结果来更新最小元素的索引值
                结果：得到数组剩余部分中最小元素的索引值————用这个索引值就能实现与当前元素a[i]交换位置的操作
              */
            int min = i;
            for (int j = i+1; j < N; j++) {
                if (less(a[j], a[min])) {
                    min = j;
                }
            }

            // 任务2： 此时min就是数组剩余元素最小值的索引 使用它完成元素交换
            exch(a, i, min);
        }
    }

    // 辅助API
    /**
     * 比较v元素是不是小于w元素
     * 如果小于，返回true
     * @param v
     * @param w
     * @return
     */
    public boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    /**
     * 交换数组中指定的两个位置的元素
     */
    public void exch(Comparable[] a, int i, int j) {
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    /**
     * 打印数组中的元素
     */
    public void show(Comparable[] a){
        int N = a.length;
        for (int i = 0; i < N; i++) {
            System.out.println(a[i]);
        }
    }

    /**
     * 判断数组元素是否已经有序(升序)
     * 注：这种方式并不保险
     */
    public boolean isSorted(Comparable[] a){
        int N = a.length;
        for (int i = 0; i < N; i++) {
            if (less(a[i+1], a[i])) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        // 读取用户从标准输入中输入的元素，并存入一个数组中

        // 对数组进行排序

        // 断言数组已经是有序的了

        // 打印数组内容
    }
}
/*
启示：即便是最简单的排序算法，理解+转换成为代码也不是易事
 */