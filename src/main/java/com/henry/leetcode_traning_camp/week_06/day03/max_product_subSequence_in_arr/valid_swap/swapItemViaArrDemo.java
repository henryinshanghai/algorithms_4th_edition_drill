package com.henry.leetcode_traning_camp.week_06.day03.max_product_subSequence_in_arr.valid_swap;

// 结论：想要交换数组中的两个元素，必须基于数组操作，而不能基于元素操作
public class swapItemViaArrDemo {
    public static void main(String[] args) {
        int[] a = {1, 10, 100, 1000};

        swap(a, 0, a.length - 1);

        // Bingo! 交换成功
        System.out.println(a[0]); // 1000
        System.out.println(a[a.length - 1]); // 1
    }

    private static void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
}
