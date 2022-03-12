package com.henry.sort_chapter_02.priority_queue_04.pq_primary_implement_02.ordered_array_02;

import edu.princeton.cs.algs4.StdOut;


/*
    算法描述：
        使用一个元素有序的数组 来实现 优先队列 - 核心操作 insert(item) + delMax()

    底层数据结构：
        elements array

    insert操作：
        为了保持 有序数组在插入元素后仍旧有序，这里的insert操作要求会复杂一些

    delMax 操作：
        既然数组已经是 元素有序的状态， 那么直接删除掉数组的最后一个元素就可以了

    less操作：
        不需要传入 数组本身 作为参数

    exch操作：
        没有用到
 */
public class MaxPQ_orderedArray_round2_drill01<Key extends Comparable<Key>> {
    private Key[] elements;          // elements
    private int itemAmount;             // number of elements

    // initialize the capacity of heap - via the elements array
    public MaxPQ_orderedArray_round2_drill01(int capacity) {
        elements = (Key[]) (new Comparable[capacity]);
        itemAmount = 0;
    }


    public boolean isEmpty() { return itemAmount == 0;  }
    public int size()        { return itemAmount;       }

    // to delete the biggest item from the heap, just delete the last item of elements array - since the array is item-ordered.
    /*
        1 delete the last item of elements array;   elements[itemAmount - 1]; again, this is logically ignore.
        2 update the amount of items.   itemAmount--;

        you can combine them into one statement like: elements[--itemAmount];
     */
    public Key delMax()      { return elements[--itemAmount]; }

    // to insert an item in heap, you need find the right place to put in the new item. because you want to keep it item-ordered for insert action.
    /*
        1 loop through each element from the begining, compare it with the newly added item
        2 add the new item in;
        3 move the rest forward for one spot.

        this can actually be a lot easier, since the original elements array is item-ordered.
        1 trace the items backward;
        2 move the bigger item to its next spot;
        3 until find the right spot for the newly added item.
     */
    public void insert(Key key) {
        // 记录最后一个有元素的位置
        int i = itemAmount -1;
        // 因为循环需要进行的具体次数未知 这里使用while循环
        /*
            循环条件：
                1 检索指针的位置 >= 0；
                2 当前元素 大于 待插入的元素
         */
        while (i >= 0 && less(key, elements[i])) {
            /*
                循环体：
                    1 把当前元素后移一格
                    2 把指针向前移动一个 - 处理前一个元素
             */
            elements[i+1] = elements[i];
            i--;
        }

        // 在指针指向的当前位置的下一个spot插入新的元素
        elements[i+1] = key;

        // 更新元素数量
        itemAmount++;
    }

    // 甚至没有用到交换操作


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
        MaxPQ_orderedArray_round2_drill01<String> pq = new MaxPQ_orderedArray_round2_drill01<String>(10);
        pq.insert("this");
        pq.insert("is");
        pq.insert("a");
        pq.insert("test");
        while (!pq.isEmpty())
            StdOut.println(pq.delMax());
    }

}