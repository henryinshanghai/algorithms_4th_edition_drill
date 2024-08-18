package com.henry.leetcode_traning_camp.week_01.day0;

// 验证：#1 可以使用双指针的方式（指针1总是前进、指针2在某种条件下才会前进）来 定位到0元素与非零元素；
// #2 通过交换零元素 与 非零元素，可以把0元素聚集到一起（以便后继截断）
public class Move0TowardsEnd_02 {
    public static void main(String[] args) {
        int[] arr = new int[]{2, 1, 4, 0, 5, 0, 0, 7};

        printArr(arr);

        moveZeros(arr);

        printArr(arr);
    }

    /**
     * 移动数组中的零到特定的位置
     * 思路：把非零元素 交换到 正确的位置上
     * 特征：这种思路 会保持 数组中的原始元素
     *
     * @param arr
     */
    private static void moveZeros(int[] arr) {
        // 准备两个指针
        // explorer指针用于找到非零元素
        // anchor指针用于：指向预期被交换的元素；   特征：在交换位置后，anchor的位置会向前移动一个位置
        int anchor = 0;

        for (int explorer = 0; explorer < arr.length; explorer++) {
            // 特征：如果数组中所有的元素都不为0，那么会出现很多次不需要的交换（这个应该是可以进行优化的）
            // anchor会停留在 值为0的元素上，并用于与非零元素进行交换
            System.out.println("anchor is: " + anchor + ", and explorer is: " + explorer);

            // 遇到非零元素时，交换 0元素 与 非零元素
            if (arr[explorer] != 0) {
                int temp = arr[explorer];
                arr[explorer] = arr[anchor];
                arr[anchor] = temp;

                printArr(arr);
                anchor++;
            }
        }
    }

    private static void printArr(int[] arr) {
        int length = arr.length;

        for (int i = 0; i < length; i++) {
            System.out.print(arr[i] + " ");
        }

        System.out.println();
    }
}
