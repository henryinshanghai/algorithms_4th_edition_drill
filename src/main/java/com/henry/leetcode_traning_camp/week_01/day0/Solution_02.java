package com.henry.leetcode_traning_camp.week_01.day0;

public class Solution_02 {
    public static void main(String[] args) {
        int[] arr = new int[]{2, 1, 4, 0, 5, 0, 0, 7};

        moveZeros(arr);

        printArr(arr);
    }

    /**
     * 移动数组中的零到特定的位置
     * @param arr
     */
    private static void moveZeros(int[] arr) {
        // 准备两个指针
        // explorer指针用于找到非零元素
        // anchor指针用于：指向预期被交换的元素；   特征：在交换位置后，anchor的位置会向前移动一个位置
        int anchor = 0;

        for (int explorer = 0; explorer < arr.length; explorer++) {
            // 特征：如果数组中所有的元素都不为0，那么会出现很多次不需要的交换（这个应该是可以进行优化的）
            if (arr[explorer] != 0) {
                int temp = arr[explorer];
                arr[explorer] = arr[anchor];
                arr[anchor] = temp;

                anchor++;
            }
        }
    }

    private static void printArr(int[] arr) {
        int length = arr.length;

        for (int i = 0; i < length; i++) {
            System.out.print(arr[i] + " ");
        }
    }
}
