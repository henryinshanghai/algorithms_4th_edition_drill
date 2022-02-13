package com.henry.sort_chapter_02.priority_queue_04.pq_primary_implement_02.ordered_array_02;

/**
 * 实现优先队列
 * API：1 向优先队列中插入元素； 2 从优先队列中删除最大元素；
 * @param <Key>
 */
public class OrderedArrayMaxPQ_drill01<Key extends Comparable<Key>> { // 泛型的类型参数Key
    // 实例变量
    private Key[] pq; // Key类型的数组
    private int n; // 队列中的元素个数

    public OrderedArrayMaxPQ_drill01(int capacity) {
        pq = (Key[]) new Comparable[capacity]; // 强制类型转换会导致检查性的异常
        n = 0;
    }

    // APIs
    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    /**
     * 删除队列中的最大元素
     * @return
     */
    public Key delMax() {
        // 手段：返回数组中的最后一个元素；    更新数组中的元素个数
        // 原理：数组需要是一个有序数组，这样才能保证最后一个元素就是最大元素
        return pq[--n]; // 一句话，两个功能 good or bad?
    }

    /**
     * 向优先队列中插入一个元素
     * 手段：把插入的元素放在数组中合适的位置
     * 具体方法：把插入的item从后往前与数组中的元素逐个比较，直到找到合适的插入位置
     * @param item
     */
    public void insert(Key item) {
        // 把item与数组中的当前元素从后向前比较     比较次数未知
        int i = n - 1; // 一个指向数组末尾元素的指针

        // 找到合适的插入位置
        while (i >= 0 && less(item, pq[i])) { // 极限条件：i=0
            pq[i + 1] = pq[i];
            i--; // i<0时就会跳出循环
        }

        // 把元素绑定到对应位置
        pq[i + 1] = item;
        // 更新队列中的元素个数
        n++;
    }

    private boolean less(Key v, Key w) {
        return v.compareTo(w) < 0;
    }

    public static void main(String[] args) {
        OrderedArrayMaxPQ_drill01<String> orderedPQ = new OrderedArrayMaxPQ_drill01<>(10);

        orderedPQ.insert("this");
        orderedPQ.insert("is");
        orderedPQ.insert("a");
        orderedPQ.insert("test");

        while (!orderedPQ.isEmpty()) {
            System.out.println(orderedPQ.delMax());
        }
    }
}
