package com.henry.sort_chapter_02.priority_queue_04.pq_primary_implement_02.ordered_array_02;

import edu.princeton.cs.algs4.StdOut;

/******************************************************************************
 *  Compilation:  javac OrderedArrayMaxPQFromWebsite.java
 *  Execution:    java OrderedArrayMaxPQFromWebsite
 *  Dependencies: StdOut.java 
 *
 *  Priority queue implementation with an ordered array.
 *
 *  Limitations
 *  -----------
 *   - no array resizing 
 *   - does not check for overflow or underflow.
 *
 * 特征：在插入元素时就保证最后一个元素始终是最大值；    这样删除最大值时直接删除最后一个元素即可；
 ******************************************************************************/

public class OrderedArrayMaxPQFromWebsite<Key extends Comparable<Key>> {
    private Key[] elementArray;          // elements
    private int itemAmount;             // number of elements

    // set initial size of heap to hold size elements
    public OrderedArrayMaxPQFromWebsite(int capacity) {
        elementArray = (Key[]) (new Comparable[capacity]);
        itemAmount = 0;
    }

    public boolean isEmpty() { return itemAmount == 0;  }
    public int size()        { return itemAmount;       }

    // 由于是有序数组， 所以删除最大元素的操作非常简单
    public Key delMax()      { return elementArray[--itemAmount]; }

    // 由于要维护一个有序的数组（这样可以方便地找到最大值），所以 insert操作 会有点费劲
    // 手段：找到 待插入元素需要放置的位置
    public void insert(Key item) {
        int backwardsCursor = itemAmount -1;
        while (backwardsCursor >= 0 && less(item, elementArray[backwardsCursor])) {
            // 把 指针指向的当前元素 向后移动一个位置
            elementArray[backwardsCursor+1] = elementArray[backwardsCursor];
            backwardsCursor--;
        }

        // 当指针指向的元素 小于 待插入的元素时，在指针的下一个位置上插入 待插入元素
        elementArray[backwardsCursor+1] = item;
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
        OrderedArrayMaxPQFromWebsite<String> pq = new OrderedArrayMaxPQFromWebsite<String>(10);

        pq.insert("Do");
        pq.insert("Or");
        pq.insert("Do Not");
        pq.insert("There's");
        pq.insert("No");
        pq.insert("Try");

        while (!pq.isEmpty())
            StdOut.println(pq.delMax());
    }

}