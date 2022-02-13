package com.henry.sort_chapter_02.priority_queue_04.pq_primary_implement_02.unordered_array_01;

import edu.princeton.cs.algs4.StdOut;

/**
 * 先按照自己的想法来实现，再对比书上提供的实现方式有什么不同
 */
public class UnorderedArrayMaxPQ_drill01 {
    // 实例变量
    private Comparable[] pq;
    private int N; // 队列中的元素个数  也相当于是指针

    // 构造方法 创建指定容量大小的优先队列
    public UnorderedArrayMaxPQ_drill01(int Max) {
        pq = new Comparable[Max];
    }

    // APIs
    public int size() {
        return N;
    }

    public boolean isEmpty(){
        return N == 0;
    }

    // 两个核心的APIs
    public void insert(Comparable item) {
        pq[N++] = item;
    }

    /**
     * 删除最大元素
     * @return
     */
    public Comparable delMax() {
        int max = 0;
        for (int i = 1; i < N; i++) {
            // 如果出现了比max还要大的元素，就更新max的值
            if (less(pq[max], pq[i])) {
                max = i;
            }
        }

        // 在交换之前获取到最大值
//        Comparable maxItem = pq[max]; // todo:this can be better
        exch(pq, max, N-1);

        // 在交换之后获取到最大值  注：在交换之后获取最大值更方便些 因为这时候是从N中获取 可以直接执行-1操作
        Comparable maxItem = pq[--N];
        pq[N - 1] = null; // 数组删除元素
//        N--;
        return maxItem;
    }

    // 两个辅助函数
    public boolean less(Comparable i, Comparable j) {
        return i.compareTo(j) < 0;
    }

    public void exch(Comparable[] pq, int i, int j) {
        Comparable temp = pq[i];
        pq[i] = pq[j];
        pq[j] = temp;
    }

    /***************************************************************************
     * Test routine. 测试用例
     ***************************************************************************/
    public static void main(String[] args) {
        UnorderedArrayMaxPQFromWebsite<String> pq = new UnorderedArrayMaxPQFromWebsite<String>(10);
        pq.insert("this");
        pq.insert("is");
        pq.insert("a");
        pq.insert("test");
        while (!pq.isEmpty())
            StdOut.println(pq.delMax());
    }
}
