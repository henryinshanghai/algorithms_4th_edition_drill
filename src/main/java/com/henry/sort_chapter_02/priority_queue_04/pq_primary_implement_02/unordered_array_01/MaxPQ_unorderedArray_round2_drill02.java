package com.henry.sort_chapter_02.priority_queue_04.pq_primary_implement_02.unordered_array_01;

/*
算法描述：
    使用无序数组 来实现 一个优先队列；（能够插入元素 + 删除队列中的最大值）

底层数据结构：数组
泛型： 继承自Comparable的Key
    泛型应该要什么时候添加呢？
区分： 队列的容量 与 队列中的元素个数

 */
public class MaxPQ_unorderedArray_round2_drill02<Key extends Comparable<Key>>{

    private Key[] elements;
    private int itemAmount;

    public MaxPQ_unorderedArray_round2_drill02(int capacity) {
        elements = (Key[]) new Comparable[capacity];
    }

    // 核心 API
    public void insert(Key item) {
        elements[itemAmount++] =item;
    }

    public Key delMax() {
        // find the biggest item in array
        int cursor_for_max = 0;
        for (int cursor = 0; cursor < itemAmount; cursor++) {
            if (less(cursor_for_max, cursor)) {
                cursor_for_max = cursor;
            }
        }

        exch(cursor_for_max, itemAmount - 1);

        Key maxItem = elements[--itemAmount];
        elements[itemAmount + 1] = null;
        return maxItem;
    }

    public boolean isEmpty() {
        return itemAmount == 0;
    }

    private void exch(int i, int j) {
        Key temp = elements[i];
        elements[i] = elements[j];
        elements[j] = temp;
    }

    private boolean less(int i, int j) {
        return elements[i].compareTo(elements[j]) < 0;
    }

    public static void main(String[] args) {
        MaxPQ_unorderedArray_round2_drill02<String> maxPQ = new MaxPQ_unorderedArray_round2_drill02<>(10);

        maxPQ.insert("Ada");
        maxPQ.insert("Alicia");
        maxPQ.insert("Henry");
        maxPQ.insert("Ben");
        maxPQ.insert("Quinta");
        maxPQ.insert("Kelly");
        maxPQ.insert("Annie");

        while (!maxPQ.isEmpty()) {
            String maxItem = maxPQ.delMax();
            System.out.println("current max String: " + maxItem);
        }
    }

}
