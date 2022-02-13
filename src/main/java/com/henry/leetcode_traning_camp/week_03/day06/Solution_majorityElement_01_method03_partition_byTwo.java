package com.henry.leetcode_traning_camp.week_03.day06;

public class Solution_majorityElement_01_method03_partition_byTwo {
    public static void main(String[] args) {
        int[] arr = {1, 1, 1, 2, 2, 2, 1};
        int res = majorityElement(arr);
        System.out.println("数组中的最多元素是： " + res);
    }

    private static int majorityElement(int[] arr) {
        return quickSearch(arr, 0, arr.length - 1, arr.length / 2);
    }

    private static int quickSearch(int[] nums, int lo, int hi, int k) {
        // 每快排切分1次，找到排序后下标为j的元素，如果j恰好等于n/2就返回；
        int j = partition(nums, lo, hi);
        if (j == k) {
            return nums[j];
        }
        // 否则根据下标j与k的大小关系来决定继续切分左段还是右段。
        return j > k? quickSearch(nums, lo, j - 1, k): quickSearch(nums, j + 1, hi, k);
    }

    /**
     * 找到一个切分元素，并把切分元素放在正确的位置
     * @param nums
     * @param lo
     * @param hi
     * @return
     */
    private static int partition(int[] nums, int lo, int hi) {
        // 1
        int v = nums[lo];
        int i = lo, j = hi + 1;

        // 2
        while (true) {
            while (++i <= hi && nums[i] < v);
            while (--j >= lo && nums[j] > v);
            if (i >= j) {
                break;
            }
            int t = nums[j];
            nums[j] = nums[i];
            nums[i] = t;
        }

        // 3 再做一次交换
        nums[lo] = nums[j];
        nums[j] = v;

        // 4 返回交换后的切分元素的索引
        return j;
    }

}
