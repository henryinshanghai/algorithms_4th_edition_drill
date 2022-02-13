package com.henry.sort_chapter_02.priority_queue_04.pq_primary_implement_02.unordered_array_01;

/**
 * 作用：没有保存插入顺序的优先队列
 * 手段：1 用压入栈的方式进行存储； 2 在删除最大值操作时，再想办法找到最大值去删除
 * @param <Key>
 */
public class UnorderedArrayMaxPQ_drill03<Key extends Comparable<Key>>{
    private Key[] pq;
    private int n;

    public UnorderedArrayMaxPQ_drill03(int capacity) {
        pq = (Key[]) new Comparable[capacity];
        n = 0;
    }

    // API
    public boolean isEmpty(){
        return n == 0;
    }

    public int size(){
        return n;
    }

    public void insert(Key item) {
        pq[n++] = item;
    }

    public Key delMax(){
        int max = 0;
        for (int i = 1; i < n; i++) {
            // 这里要看你想怎么实现less了
            if (less(max, i)) {
                max = i;
            }
        }

        exch(max, n - 1);

//        return pq[--n];
        Key key = pq[n - 1];
        pq[n - 1] = null;
        n--;

        return key;
    }

    private void exch(int max, int i) {
        Key temp = pq[max];
        pq[max] = pq[i];
        pq[i] = temp;
    }

    private boolean less(int i, int j) {
        return pq[i].compareTo(pq[j]) < 0;
    }

    public static void main(String[] args) {
        UnorderedArrayMaxPQ_drill03<String> pq = new UnorderedArrayMaxPQ_drill03<>(10);

        pq.insert("this");
        pq.insert("is");
        pq.insert("a");
        pq.insert("test");

        while (!pq.isEmpty()) {
            // 删除并打印最大值
            System.out.println(pq.delMax());
        }
    }
}
