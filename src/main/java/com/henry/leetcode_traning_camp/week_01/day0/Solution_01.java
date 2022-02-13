package com.henry.leetcode_traning_camp.week_01.day0;

public class Solution_01 {

    public static void main(String[] args) {
        int[] arr = new int[]{2, 1, 0, 3, 0, 9};

        removeZeros(arr);

        printArr(arr);
    }

    /**
     * 移动数组中的零到数组末尾
     * @param arr
     */
    private static void removeZeros(int[] arr) {
        // 1 准备一个指针
        int j = 0;

        // 2 遍历数组中的每一个元素  作用：把非零元素按照顺序覆盖到正确的位置
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != 0) {
                arr[j++] = arr[i];
            }
        }

        // 3 把j指针指向的位置以及数组中后面的位置都绑定0
        for(int i=j; i < arr.length; i++){
            arr[i] = 0;
        }
    }

    private static void printArr(int[] arr) {
        int length = arr.length;

        for (int i = 0; i < length; i++) {
            System.out.print(arr[i] + " ");
        }
    }
}
