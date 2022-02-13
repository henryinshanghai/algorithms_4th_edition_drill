package com.henry.leetcode_traning_camp.week_01.day1.arrays;

public class Solution_remove_duplicates_01_drill01 {
    public static void main(String[] args) {
        int[] arr = new int[]{1, 1, 1, 2, 2, 3, 4, 5};

        int subArrPointer = removeDuplicates(arr);

        printSubArr(arr, subArrPointer);
    }

    private static void printSubArr(int[] arr, int subArrPointer) {
        for (int i = 0; i < subArrPointer; i++) {
            System.out.print(arr[i] + " ");
        }
    }

    /**
     * 删除数组中的重复元素
     * @param arr
     * @return
     */
    private static int removeDuplicates(int[] arr) {
        // 鲁棒性代码
        if(arr == null) return 0;

        /* 找到数组中出现的新元素 & 把找到的新元素绑定到正确的位置 */
        int anchor = 0;
        for (int explorer = 1; explorer < arr.length; explorer++) {
            if (arr[explorer] == arr[anchor]) {
                continue;
            } else {
                anchor++;
                arr[anchor] = arr[explorer];
            }
        }

        return anchor + 1; // 返回有序子数组的长度
    }
}
