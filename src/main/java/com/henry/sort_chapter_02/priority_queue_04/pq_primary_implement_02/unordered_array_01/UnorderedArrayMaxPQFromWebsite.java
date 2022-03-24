package com.henry.sort_chapter_02.priority_queue_04.pq_primary_implement_02.unordered_array_01;

import edu.princeton.cs.algs4.StdOut;

/******************************************************************************
 *  Compilation:  javac UnorderedArrayMaxPQFromWebsite.java
 *  Execution:    java UnorderedArrayMaxPQFromWebsite
 *  Dependencies: StdOut.java 
 *
 *  Priority queue implementation with an unsorted array.
 *
 *  Limitations
 *  -----------
 *   - no array resizing
 *   - does not check for overflow or underflow.
 *
 ******************************************************************************/

public class UnorderedArrayMaxPQFromWebsite<Key extends Comparable<Key>> {
    // 实例变量
    private Key[] itemArray;      // elements
    private int itemAmount;         // number of elements

    // set initial size of heap to hold size elements
    public UnorderedArrayMaxPQFromWebsite(int capacity) {
        itemArray = (Key[]) new Comparable[capacity]; // 不能直接创建泛型数组，只能先创建通用类型的数组，然后再进行强制类型转换
        itemAmount = 0;
    }

    // APIs
    public boolean isEmpty()   { return itemAmount == 0; }

    // 优先队列中的元素个数
    public int size()          { return itemAmount;      }

    // 向“优先队列” 中插入一个元素 - 由于这里使用无序数组实现 PQ，所以直接把元素添加到 数组末尾即可
    public void insert(Key x)  { itemArray[itemAmount++] = x;   }

    // 从“优先队列”中删除最大元素 - 由于底层的数组是无序的，所以找到最大值要费点力气
    public Key delMax() {
        int cursorToMaxItem = 0;
        for (int dynamicCursor = 1; dynamicCursor < itemAmount; dynamicCursor++)
            if (less(cursorToMaxItem, dynamicCursor)) cursorToMaxItem = dynamicCursor;

        // 把数组中的最大元素 交换到数组的末尾
        exch(cursorToMaxItem, itemAmount -1);

        // 返回数组末尾的元素(aka 最大值元素) 并 把队列中的元素数量-1 注：这相当于 逻辑上删除了 队列中的元素
        return itemArray[--itemAmount]; // 直接返回最后一个元素的值
    }


    /***************************************************************************
     * Helper functions. 辅助函数
     ***************************************************************************/
    private boolean less(int i, int j) {
        return itemArray[i].compareTo(itemArray[j]) < 0;
    }

    private void exch(int i, int j) {
        Key swap = itemArray[i];
        itemArray[i] = itemArray[j];
        itemArray[j] = swap;
    }


    /***************************************************************************
     * Test routine. 测试用例
     ***************************************************************************/
    public static void main(String[] args) {
        UnorderedArrayMaxPQFromWebsite<String> pq = new UnorderedArrayMaxPQFromWebsite<String>(10);
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