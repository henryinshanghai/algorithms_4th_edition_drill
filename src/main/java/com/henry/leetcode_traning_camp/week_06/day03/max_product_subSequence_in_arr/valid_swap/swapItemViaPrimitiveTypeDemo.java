package com.henry.leetcode_traning_camp.week_06.day03.max_product_subSequence_in_arr.valid_swap;

// 验证：对于基本类型的参数，方法内的“重新赋值操作” 不会影响原始引用的值；
// 原理：Java中参数的传递类型是“值传递”；
// 结论：Java 中无法直接通过方法 交换两个基本数据类型的变量，但可以 通过操作数组或对象 来 间接实现。
public class swapItemViaPrimitiveTypeDemo {
    public static void main(String[] args) {
        int[] a = {1, 2, 3, 4, 5};
        int[] b = {-1, -2, -3, -4, -5};

        swap(a[0], b[0]);

        // 两个数组的元素并没有交换成功！
        System.out.println("a[0]: " + a[0]); // 1
        System.out.println("b[0]: " + b[0]); // -1
    }

    private static void swap(int i, int j) {
        int temp = i;
        i = j;
        j = temp;
    }
}
