package com.henry.sort_chapter_02.priority_queue_04.pq_primary_implement_02.unordered_array_01;

public class GenericTypeArrayTest {

    public static void main(String[] args) {
        Comparable[] array = new Comparable[5];

        array[0] = "Henry";
        array[1] = 2;
        array[2] = "Alicia";

        // 只要元素类型实现了Comparable接口 就可以是为 Comparable类型的元素
        MyType myItem = new MyType();
        array[3] = myItem;

        for (int cursor = 0; cursor < array.length; cursor++) {
            System.out.println("- " + array[cursor] + " -");
        }
    }

}


class MyType implements Comparable {

    @Override
    public int compareTo(Object o) {
        return -1;
    }

    @Override
    public String toString() {
        return "this is my type";
    }
}