package com.henry.sort_chapter_02.priority_queue_04.pq_primary_implement_02.ordered_array_02;

import edu.princeton.cs.algs4.StdOut;

// 结论：可以使用有序数组来实现优先队列(#1 添加元素; #2 从中删除最大元素)；
// 步骤：#1 添加元素时，维护一个有序的元素数组； #2 删除最大元素时，直接删除；
public class OrderedArrayMaxPQTemplate<Key extends Comparable<Key>> {
    private Key[] itemArray;
    private int itemAmount;

    public OrderedArrayMaxPQTemplate(int capacity) {
        itemArray = (Key[]) (new Comparable[capacity]);
        itemAmount = 0;
    }

    public boolean isEmpty() { return itemAmount == 0;  }
    public int size()        { return itemAmount;       }

    public Key delMax()      { return itemArray[--itemAmount]; }

    // 在有序数组中，插入元素key
    // 手段：从后往前遍历数组元素，一边遍历，一边决定是插入元素还是移除元素
    public void insert(Key key) {
        // 指针初始位置指向最后一个元素
        int backwardsCursor = itemAmount -1;

        // 排定“待插入元素”
        while (backwardsCursor >= 0 && less(key, itemArray[backwardsCursor])) { // #1 比较当前元素 与 待插入元素
            // 如果当前元素更大，则：把当前元素往后移动一个位置
            itemArray[backwardsCursor+1] = itemArray[backwardsCursor];
            backwardsCursor--;
        }

        // 如果当前元素更小，说明找到了插入位置。则：在当前位置的下一位置上插入元素
        itemArray[backwardsCursor+1] = key;

        // 更新元素数量
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