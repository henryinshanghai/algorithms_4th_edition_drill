package com.henry.leetcode_traning_camp.week_01.day0;

// 验证：可以通过 #1 把非零元素移动到数组开头； #2 把剩余位置的元素置零的方式 来 实现对元素0的移动；
public class Move0TowardsEnd_01 {

    public static void main(String[] args) {
        int[] arr = new int[]{2, 1, 0, 3, 0, 9};

        System.out.print("original array: ");
        printArr(arr);

        removeZeros(arr);

        printArr(arr);
    }

    /**
     * 移动数组中的零到数组末尾
     * 思路：把非零元素 按照顺序覆盖到 正确的位置
     * 特征：这种做法 会改变数组中的原始元素
     * @param arr
     */
    private static void removeZeros(int[] arr) {
        // 1 准备一个指针
        int nonZeroCursor = 0;

        // 2 遍历数组中的每一个元素  作用：把非零元素按照顺序覆盖到正确的位置
        for (int currentSpot = 0; currentSpot < arr.length; currentSpot++) {
            if (arr[currentSpot] != 0) {
                arr[nonZeroCursor++] = arr[currentSpot];
            }

            printArr(arr);
        }

        // 3 把j指针指向的位置 以及 数组中后面的位置上的元素都设置为0
        for (int currentCursor = nonZeroCursor; currentCursor < arr.length; currentCursor++) {
            arr[currentCursor] = 0;
        }
    }

    private static void printArr(int[] arr) {
        int length = arr.length;

        for (int currentSpot = 0; currentSpot < length; currentSpot++) {
            System.out.print(arr[currentSpot] + " ");
        }

        System.out.println();
    }
}
