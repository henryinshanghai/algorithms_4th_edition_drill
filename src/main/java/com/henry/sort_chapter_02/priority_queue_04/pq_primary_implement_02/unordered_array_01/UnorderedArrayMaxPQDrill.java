package com.henry.sort_chapter_02.priority_queue_04.pq_primary_implement_02.unordered_array_01;

import edu.princeton.cs.algs4.StdOut;

// #1 insert item to array's tail;
// #2 find the maxItem via keep updating the maxItemCursor, then delete maxItem;
// note - this is the first 'Data Structure' occurs.
public class UnorderedArrayMaxPQDrill<Key extends Comparable<Key>> {

    private int arrayCapacity;
    private int itemAmount;
    private Key[] itemArray;

    public UnorderedArrayMaxPQDrill(int arrayCapacity) {
        itemArray = (Key[]) new Comparable[arrayCapacity];
        itemAmount = 0;
        this.arrayCapacity = arrayCapacity;
    }

    // 实现为 实例方法
    public void insert(Key item) {
        itemArray[itemAmount++] = item;
    }

    public Key delMax() {
        // find the maxItem
        int maxItemCursor = 0;

        for (int currentCursor = 1; currentCursor < itemAmount; currentCursor++) {
            if (less(itemArray[maxItemCursor], itemArray[currentCursor])) {
                maxItemCursor = currentCursor;
            }
        }

        // 维持数组的连续性
        exch(itemArray, maxItemCursor, itemAmount - 1);
        Key maxItem = itemArray[itemAmount - 1];
        // 物理删除元素
        itemArray[itemAmount - 1] = null;
        itemAmount--;
        return maxItem;
    }

    public int size() {
        return itemAmount;
    }

    public boolean isEmpty() {
        return itemAmount == 0;
    }

    private void exch(Key[] itemArray, int i, int j) {
        Key temp = itemArray[i];
        itemArray[i] = itemArray[j];
        itemArray[j] = temp;
    }

    private boolean less(Key v, Key w) {
        return v.compareTo(w) < 0;
    }

    public static void main(String[] args) {
        UnorderedArrayMaxPQDrill<String> maxPQ = new UnorderedArrayMaxPQDrill<>(10);

        maxPQ.insert("Alicia");
        maxPQ.insert("Ben");
        maxPQ.insert("David");
        maxPQ.insert("Eva");
        maxPQ.insert("Floria");
        maxPQ.insert("Grace");
        maxPQ.insert("Joker");
        maxPQ.insert("Leo");
        maxPQ.insert("Monica");

        System.out.println(maxPQ.size());

        while (!maxPQ.isEmpty())
            StdOut.println(maxPQ.delMax());
    }
}
