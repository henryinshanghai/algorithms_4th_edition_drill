package com.henry.leetcode_traning_camp.week_02.day3.NthThree.sum_to_target;

// 验证：int类型的数组，其元素初始值会默认为0
public class ArrayDemo {
    public static void main(String[] args) {
        int[] demoArr = new int[2];

        print(demoArr);

        // 在创建数组的同时 进行元素初始化的语法 - new int[]{xxx, ooo}
        int[] demoArr2 = new int[]{1, 2, 3, 4, 5};
        print(demoArr2);
    }

    private static void print(int[] demoArr) {
        for (int currentItem : demoArr) {
            System.out.print(currentItem + ", ");
        }
        System.out.println();
    }
}
