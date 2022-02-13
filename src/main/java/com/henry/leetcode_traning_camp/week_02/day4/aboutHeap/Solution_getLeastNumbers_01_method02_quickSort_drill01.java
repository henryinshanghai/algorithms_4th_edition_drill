package com.henry.leetcode_traning_camp.week_02.day4.aboutHeap;

import java.util.Arrays;

public class Solution_getLeastNumbers_01_method02_quickSort_drill01 {
    public static void main(String[] args) {
        int[] originalArr = {3,2,1};
//        int[] originalArr = {0,1,2,1};

        int k = 2;

        int[] leastKNumbers = getLeastNumbers(originalArr, k);

        for (int i = 0; i < leastKNumbers.length; i++) {
            System.out.print(leastKNumbers[i] + ", ");
        }
    }

    /**
     * 获取到指定数组中最小的k个数字
     * @param arr
     * @param k
     * @return
     */
    private static int[] getLeastNumbers(int[] arr, int k) {
        // 0
        if (arr.length == 0 || k == 0) {
            return new int[0];
        }

        // 1 借助快速排序的原理，获取到经过快速排序处理后数组中最小的k个数字
        return quickSortSearch(arr, 0, arr.length - 1, k - 1);
    }

    /**
     * 获取到指定数组中，最小的k个数字所组成的数组；
     * @param arr   指定的数组
     * @param lo    进行快排操作的区间左边界
     * @param hi    进行快排操作的区间右边界
     * @param k     预期获取到的数组的元素个数
     * @return
     */
    private static int[] quickSortSearch(int[] arr, int lo, int hi, int k) {
        // 1 排定数组中的一个切分元素
        int arranged = partition(arr, lo, hi);

        if (arranged == k) {
            return Arrays.copyOf(arr, arranged + 1);
        }

        return arranged > k
                ? quickSortSearch(arr, lo, arranged - 1, k)
                : quickSortSearch(arr, arranged + 1, hi, k);
    }

    /**
     * 排定数组中的一个元素，并返回所排定元素的下标
     * @param arr 待排序的数组
     * @param lo 本级排序区间的左边界
     * @param hi 本级排序区间的右边界
     * @return
     */
    private static int partition(int[] arr, int lo, int hi) {
        // 1
        int bar = arr[lo];

        // 2
        int left = lo;
        int right = hi + 1; // 尾元素的下一个位置

        // 3
        while (true) {
            // 3-1 找到待交换的左元素:第一个大于切分元素的元素
            while(++left <= hi && arr[left] < bar);

            // 3-2 找到待交换的右元素：第一个小于切分元素的元素
            while(--right >= lo && arr[right] > bar);

            // 3-3 判断除了基准元素之外的其他数组元素是否已经符合预期：不是排序状态，而是xxx
            if (left >= right) break;

            // 3-4 交换找到的两个元素
            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
        }

        // 4 把基准元素交换到正确的位置（right指针）上
        arr[lo] = arr[right];
        arr[right] = bar; // expr:这里需要使用的是right指针

        // 5
        return right;
    }


}
