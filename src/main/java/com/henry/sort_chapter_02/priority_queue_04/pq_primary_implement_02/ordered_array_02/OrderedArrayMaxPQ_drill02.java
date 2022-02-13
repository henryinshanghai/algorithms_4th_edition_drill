package com.henry.sort_chapter_02.priority_queue_04.pq_primary_implement_02.ordered_array_02;

public class OrderedArrayMaxPQ_drill02<Key extends Comparable<Key>> {
    private Key[] pq;
    private int n;

    public OrderedArrayMaxPQ_drill02(int capacity) {
        pq = (Key[]) new Comparable[capacity];
        n = 0;
    }

    // API
    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public Key delMax() {
        return pq[--n]; // 1 获取到pq[n-1]; 2 把n--
    }

    public void insert(Key item) {
        // 一个指针
        int i = n - 1;
        // 遍历比较，并根据结果移动元素
        while (i >= 0 && less(item, pq[i])) { // 考虑极端情况
            pq[i + 1] = pq[i]; // 把元素向后移动一格
            i--; // 指针-1 处理下一个元素
        }

        // 把指针向后移动一格    并把item绑定到指针指向的新位置   有没有必要改变指针的值？
        pq[++i] = item;
        n++;
    }

    // 移动元素的两种方式：交换 & 覆盖    这里没有用到交换操作

    private boolean less(Key item, Key key) {
        return item.compareTo(key) < 0;
    }

    public static void main(String[] args) {
        OrderedArrayMaxPQ_drill02<String> orderedPQ = new OrderedArrayMaxPQ_drill02<>(10);

        orderedPQ.insert("this");
        orderedPQ.insert("is");
        orderedPQ.insert("a");
        orderedPQ.insert("test");

        while (!orderedPQ.isEmpty()) {
            System.out.println(orderedPQ.delMax());
        }
    }
}
