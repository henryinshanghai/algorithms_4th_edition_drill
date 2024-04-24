package com.henry.sort_chapter_02.advanced_algorithm.via_data_structure.priority_queue.heap_sort_05;

// 验证：arr.length 是实例化数组时所指定的长度
public class ArrayTest {
    public static void main(String[] args) {
        // 实例化时，数组元素就已经被赋值0了
        Integer[] ints = new Integer[10];

        ints[0] = 10;
        ints[1] = 20;
        ints[2] = 30;
        ints[3] = 40;

        System.out.println(ints.length); // 10 length是实例化数组时所指定的长度

        for (int i = 0; i < ints.length; i++) {
            System.out.print(ints[i] + " ");
        }
        System.out.println();

        for (int anInt : ints) {
            System.out.print(anInt + " ");
        }
    }
}
