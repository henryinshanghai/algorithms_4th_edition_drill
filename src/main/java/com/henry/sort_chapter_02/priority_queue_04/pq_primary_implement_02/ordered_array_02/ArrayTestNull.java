package com.henry.sort_chapter_02.priority_queue_04.pq_primary_implement_02.ordered_array_02;

/**
 * 数组元素可不可以是null？
 * 答：可以的
 * 所以通过把元素值设置为null 应该是无法删除数组元素的
 */
public class ArrayTestNull {
    public static void main(String[] args) {
        Integer[] integers = new Integer[10];
        for (int i = 0; i < integers.length; i++) {
            integers[i] = null;
        }

        for (Integer integer : integers) {
            System.out.println(integer);
        }
    }
}
