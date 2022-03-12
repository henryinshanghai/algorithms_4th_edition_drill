package com.henry.sort_chapter_02.priority_queue_04.pq_primary_implement_02.unordered_array_01;

import edu.princeton.cs.algs4.StdOut;

/*
算法描述：
    使用无序数组 来实现 一个优先队列；（能够插入元素 + 删除队列中的最大值）

底层数据结构：数组
泛型： 继承自Comparable的Key
区分： 队列的容量 与 队列中的元素个数

 */
public class MaxPQ_unorderedArray_round2_drill01<Key extends Comparable<Key>> {

    // 实例变量
    private Key[] elements;      // elements

    // why do we need this anyway? to support multiple APIs
    private int itemAmount;         // number of elements

    // set initial size of heap to hold size elements
    public MaxPQ_unorderedArray_round2_drill01 (int capacity) {
        elements = (Key[]) new Comparable[capacity]; // 不能直接创建泛型数组，只能先创建通用类型的数组，然后再进行强制类型转换
        itemAmount = 0;
    }

    // APIs
    public boolean isEmpty()   { return itemAmount == 0; }
    public int size()          { return itemAmount;      }

    // to insert an item into Heap, just add the item into the elements array
    /*
        1 add the item into the element array;  elements[itemAmount] = x;
        2 update the amount of items.   itemAmount++;
        you can jam them into one sentence~
     */
    public void insert(Key x)  { elements[itemAmount++] = x;   }

    // to delete the max item in the heap, you need to delete the max item in the array
    /*
        1 find the biggest item in the elements array - since it is unordered, you need to pull some string
        2 then, to avoid there's empty spot in there. just fill the spot with the last item.
     */
    public Key delMax() {
        // find the biggest item in the array - note all the action is based on index.
        int max = 0; // the pointer of the biggest item
        for (int i = 1; i < itemAmount; i++)
            if (less(max, i)) max = i; // keep updating the pointer

        // since we are gonna delete the biggest item. swap it with the last item
        exch(max, itemAmount -1);

        // let's delete the biggest item(it's in the last position now), by forsaking it
        return elements[--itemAmount]; // this seems not forsaking anything. just ignore the item logically
    }


    /***************************************************************************
     * Helper functions. 辅助函数 we are not using the array itself as the parameter
     ***************************************************************************/
    private boolean less(int i, int j) { // pass in the positions
        return elements[i].compareTo(elements[j]) < 0;
    }

    private void exch(int i, int j) {
        Key swap = elements[i];
        elements[i] = elements[j];
        elements[j] = swap;
    }


    /***************************************************************************
     * Test routine. 测试用例
     ***************************************************************************/
    public static void main(String[] args) {
        // initialize the heap
        UnorderedArrayMaxPQFromWebsite<String> pq = new UnorderedArrayMaxPQFromWebsite<String>(10);

        // insert some items into the heap
        pq.insert("this");
        pq.insert("is");
        pq.insert("a");
        pq.insert("test");

        // retrieve the biggest item at the heap
        while (!pq.isEmpty())
            StdOut.println(pq.delMax());
    }


}
