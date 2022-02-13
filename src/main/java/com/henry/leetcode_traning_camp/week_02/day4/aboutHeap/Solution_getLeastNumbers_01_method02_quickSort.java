package com.henry.leetcode_traning_camp.week_02.day4.aboutHeap;

import java.util.Arrays;

public class Solution_getLeastNumbers_01_method02_quickSort {
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
     * 特征：获取到最小的k个数字并不需要对整个数组进行排序，也不要求这k个数字就是有序的；
     * 手段：利用快速排序的SOP
     * @param arr
     * @param k
     * @return
     */
    private static int[] getLeastNumbers(int[] arr, int k) {
        if (k == 0 || arr.length == 0) {
            return new int[0];
        }
        
        // 最后一个参数表示我们要找的是下标为k-1的数
        // 特征：相比于快排模板，多了一个参数k
        return quickSearch(arr, 0, arr.length - 1, k - 1); // 对数组进行快速排序的方法
    }

    /**
     * 对指定数组进行快速排序
     * 手段：1 找到切分元素，并把切分元素放在正确的位置； 2 在子数组上递归调用函数本身对子数组进行排序
     * @param nums 待排序的数组
     * @param lo 本级调用的左边界
     * @param hi 本机调用的右边界
     * @param k 预期切分元素的位置
     * @return
     */
    private static int[] quickSearch(int[] nums, int lo, int hi, int k) {
        // 每快排切分1次，找到排序后下标为j的元素，如果j恰好等于k就返回j以及j左边所有的数；
        int j = partition(nums, lo, hi);

        // 当切分元素的位置 == k时，说明此时数组中的前k个元素就已经是"最小的k个值"了
        if (j == k) {
            // 则：把数组中的前k个元素拷贝出来
            return Arrays.copyOf(nums, j + 1); // 这里需要的参数是长度，所以为(索引 + 1)
        }
        // 否则根据下标j与k的大小关系来决定继续切分左段还是右段。
        // 如果切分元素的位置大于k，说明预期的位置在左区间中。则：对数组的左区间进行快排操作
        // 如果切分元素的位置小于k，说明预期的位置在右区间中。则：对数组的右区间进行快排操作
        return j > k? quickSearch(nums, lo, j - 1, k): quickSearch(nums, j + 1, hi, k);
    }

    /**
     * 为特定数组选择一个切分元素，调整数组，使得：
     *  1 切分元素左边的元素序列都小于切分元素 & 切分元素右边的元素序列都大于切分元素
     * 并返回切分元素最终的位置
     * @param nums
     * @param lo
     * @param hi
     * @return
     */
    private static int partition(int[] nums, int lo, int hi) {
        // 1 随机选择一个元素作为“切分元素”
        int v = nums[lo];

        // 2 准备两个指针i、j，分别指向数组的头元素与尾元素
        int i = lo, j = hi + 1;

        /*
            3 准备一个循环，在循环中：
                3-1 从左区间找到第一个比切分元素更大的元素
                3-2 从右区间中找到第一个比切分元素更小的元素
                3-3 交换1、2中找到的这两个元素，直到左右指针相遇
         */
        while (true) {
            while (++i <= hi && nums[i] < v);
            while (--j >= lo && nums[j] > v);

            if (i >= j) { // 如果左指针 >= 右指针，说明：剩余的数组元素序列已经按照预期进行排列了。则：
                // 不需要执行任何的交换操作 直接跳出循环
                break;
            }

            int t = nums[j];
            nums[j] = nums[i];
            nums[i] = t;
        }

        // 4 把基准元素与指针j指向的元素交换位置————这样基准元素就被会被排定到最终的位置上
        nums[lo] = nums[j];
        nums[j] = v;

        // 5 返回基准元素所在的位置
        return j;
    }
}
