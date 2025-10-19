package com.henry.sort_chapter_02.advanced_algorithm.via_data_structure.priority_queue.pq_primary_implement_02.ordered_array_02;

/**
 * 数组元素 可不可以是 null？
 * 答：可以的
 * 所以通过 把 元素值 设置为 null 应该是 无法 删除数组元素的
 */
public class ArrayTestNull {
    public static void main(String[] args) {
        Integer[] integers = new Integer[10];
        // 把 数组元素 赋值为 null
        for (int i = 0; i < integers.length; i++) {
            integers[i] = null;
        }

        for (Integer integer : integers) {
            System.out.println(integer);
        }
    }
}
