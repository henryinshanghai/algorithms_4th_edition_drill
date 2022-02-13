package com.henry.leetcode_traning_camp.week_02.day4.aboutHeap;

import java.util.Arrays;

public class Solution_getLeastNumbers_01_method02_quickSort_drill02 {
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
     * 从数组中获取到最小的k个数字
     * @param arr
     * @param k
     * @return
     */
    private static int[] getLeastNumbers(int[] arr, int k) {
        if (arr == null || arr.length == 0 || k == 0) return new int[0];

        return quickSortSearch(arr, 0, arr.length - 1, k - 1);
    }

    /**
     * 使用数组中最小的k个数字组成新数组并返回
     * @param arr   待处理的数组
     * @param lo    本机递归处理的区间左边界
     * @param hi    本机递归处理的区间右边界
     * @param k     expr4: 预期获取到的元素在数组中的下标
     * @return
     */
    private static int[] quickSortSearch(int[] arr, int lo, int hi, int k) {
        // 先排定数组中的一个元素
        int arranged = partition(arr, lo, hi);

        if (arranged == k) {
            return Arrays.copyOf(arr, k + 1);
        }

        return arranged > k
                ? quickSortSearch(arr, lo, arranged - 1, k)
                : quickSortSearch(arr, arranged + 1, hi, k);
    }

    private static int partition(int[] arr, int lo, int hi) {
        int to_be_arrange = arr[lo];

        int left = lo;
        int right = hi + 1;

        while (true) {
            while(++left <= hi && arr[left] < to_be_arrange); // expr3：包含等号

            while(--right >= lo && arr[right] > to_be_arrange);

            if(left >= right) break; // expr2：包含等号

            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
        }

        arr[lo] = arr[right];
        arr[right] = to_be_arrange;

        return right; // expr:这里是right指针
    }
}
