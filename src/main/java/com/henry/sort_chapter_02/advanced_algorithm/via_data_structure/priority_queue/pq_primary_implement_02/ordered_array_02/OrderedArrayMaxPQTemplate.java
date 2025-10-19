package com.henry.sort_chapter_02.advanced_algorithm.via_data_structure.priority_queue.pq_primary_implement_02.ordered_array_02;

import edu.princeton.cs.algs4.StdOut;

// 结论：可以使用 有序数组 来 实现优先队列(#1 添加元素; #2 从中删除最大元素)；
// 步骤：#1 添加元素 时，维护一个 有序的元素数组； #2 删除最大元素 时，直接删除；
public class OrderedArrayMaxPQTemplate<Key extends Comparable<Key>> {
    private Key[] itemArray;
    private int itemAmount;

    public OrderedArrayMaxPQTemplate(int capacity) {
        itemArray = (Key[]) (new Comparable[capacity]);
        itemAmount = 0;
    }

    public boolean isEmpty() {
        return itemAmount == 0;
    }

    public int size() {
        return itemAmount;
    }

    public Key delMax() {
        return itemArray[--itemAmount];
    }

    /**
     * 在有序数组中，插入元素key
     * 手段：从后往前 遍历数组元素，一边遍历，一边决定 是 插入元素 还是 移除元素
     */
    public void insert(Key key) {
        // 游标指针 初始指向 最后一个元素
        int backwardsCursor = itemAmount - 1;

        // 排定 “待插入元素”
        while (backwardsCursor >= 0 && less(key, itemArray[backwardsCursor])) { // #1 比较当前元素 与 待插入元素
            // 如果 “指针所指向的元素” 更大，说明 当前指针位置 大于 应该插入的位置，则：
            // #1 把 当前元素 往后移动 一个位置 🐖 这也解释了 为什么需要 从后往前地遍历指针，是 为了避免 arr[i+1] 被覆盖掉
            itemArray[backwardsCursor + 1] = itemArray[backwardsCursor];
            // #2 把 指针 向前移动 一个位置
            backwardsCursor--;
        }

        // 如果 当前元素 更小，说明 找到了 应该插入的位置。则：
        // 在 “当前位置的下一位置” 上 插入元素
        itemArray[backwardsCursor + 1] = key;

        // 维护 元素数量
        itemAmount++;
    }


    /***************************************************************************
     * Helper functions.
     ***************************************************************************/
    private boolean less(Key v, Key w) {
        return v.compareTo(w) < 0;
    }

    /***************************************************************************
     * Test routine.
     ***************************************************************************/
    public static void main(String[] args) {
        OrderedArrayMaxPQTemplate<String> maxPQ = new OrderedArrayMaxPQTemplate<String>(10);

        maxPQ.insert("Ada");
        maxPQ.insert("Alicia");
        maxPQ.insert("Henry");
        maxPQ.insert("Ben");
        maxPQ.insert("Quinta");
        maxPQ.insert("Kelly");
        maxPQ.insert("Annie");

        while (!maxPQ.isEmpty())
            StdOut.println(maxPQ.delMax());
    }

}