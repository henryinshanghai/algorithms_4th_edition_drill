package com.henry.sort_chapter_02.priority_queue_04.pq_indexed_item_implement_04;

import java.util.NoSuchElementException;

/**
 * 带着问题去阅读：如何实现 pq[对象排序后的序号] = 索引值
 * aka 如何把指定的索引值存放在pq中正确的位置？
 * 手段：1 把pq处理成为一个优先队列；  为啥优先队列能够保证索引能放到正确的位置呢？ not sure
 * 区分：索引本身 = 索引值    索引关联的对象 = 对象本身
 * @param <Key>
 */
public class IndexMinPQ_learn_01<Key extends Comparable<? super Key>> { // implements Iterable<Integer>
    /**
     * 索引优先队列,保存对象在数组中的位置，按索引值(即keys[i],i为索引)进行小堆排序
     */
    private int[] pq;
    /**
     * pq的逆序,保存对象索引在pq中的位置
     */
    private int[] qp;
    /**
     * 队列最大元素数
     */
    private int maxSize;
    /**
     * 当前队列元素数量
     */
    private int currentSize;

    /**
     * 具体元素
     */
    private Key[] keys;

    /**
     * @param maxSize 表示索引优先队列中预期能够存储元素的最大容量
     */
    public IndexMinPQ_learn_01(int maxSize) {
        if (maxSize < 0) {
            throw new IllegalArgumentException("参数非法");
        }
        this.maxSize = maxSize;
        pq = new int[maxSize + 1];
        qp = new int[maxSize + 1]; // 把pq实现成为一个二叉堆 特征：第一个位置不存放内容
        keys = (Key[]) new Comparable[maxSize + 1];
        currentSize = 0;

        // 对qp数组进行初始化 作用：根据索引值来获取“对象排序后的序号”
        for (int i = 0; i < maxSize + 1; i++) {
            //qp 初始化为-1 ，表示没有索引关联的对象
            qp[i] = -1;
        }
    }

    public Key keyOf(int i){
        checkIndex(i);
        if (!contains(i))throw new NoSuchElementException("不存在该索引");
        return keys[i];
    }

    /**
     * 返回最小的索引，即索引队列pq[1]对应的索引对象
     * @return
     */
    public int minIndex(){
        if (isEmpty()){
            throw new NoSuchElementException("队列为空");
        }
        return pq[1];
    }

    private boolean isEmpty() {
        return  currentSize == 0;
    }

    public boolean contains(int index){
        checkIndex(index);
        //如果存在，则qp[index]一定不为-1，且指向 该对象index在 索引队列pq中的位置
        return qp[index] != -1;
    }
    private void checkIndex(int index){
        if (index<0 || index>= maxSize){
            throw new IndexOutOfBoundsException("参数越界");
        }
    }

    // 核心API
    /**
     * 插入一对值
     * @param index 索引
     * @param key 索引关联的值
     */
    public void  insert(int index,Key key){
        checkIndex(index);
        if (contains(index)){
            throw new IllegalArgumentException("索引"+index+"已存在");
        }
        //增加元素数
        currentSize++;
        //当前对象的索引为队列尾部
        qp[index]=currentSize;
        //该索引指向的对象在key中的位置为index
        pq[currentSize]=index;
        keys[index] = key;
        //对尾部元素上滤
        percolateUp(currentSize);
    }

    /**
     * 上滤
     * @param n
     */
    private void percolateUp(int n) {
        //将n和n/2（n的父亲）比较，如果父亲大，则交换两个节点
        for (; n>1&&compareTo(n,n/2)<0  ; n/=2) {
            swap(n,n/2);
        }
    }

    /**
     * 比较索引队列i和j上的对应的key值大小
     * @param i
     * @param j
     * @return
     */
    private int compareTo(int i,int j){
        return keys[pq[i]].compareTo(keys[pq[j]]);
    }

    /**
     * 交换pq[i]和pq[j]的元素
     * 并更新qp,qp[pq[i]]=i;
     * @param i
     * @param j
     */
    private void swap(int i, int j) {
        int temp= pq[i];
        pq[i] = pq[j];
        pq[j] = temp;

        qp[pq[i]] = i;
        qp[pq[j]] = j;
    }

    // 核心API
    /**
     * 删除最小键并返回其关联的索引。
     * @return 关联的索引 即pq[1]
     */
    public int delMin(){
        if (currentSize == 0) {
            return -1;
        }

        int minIndex=pq[1];
        swap(1,currentSize--);
        percolateDown(1);
        //删除当前对象，即pq中不存在该对象了
        qp[minIndex] = -1;

        pq[currentSize+1]=-1;//不是必须的
        keys[minIndex]=null;
        return minIndex;
    }

    /**
     * 下滤
     * @param k
     */
    private void percolateDown(int k) {
        int child;
        for (; k*2 <= currentSize ; k =child) {
            child= 2 * k;
            if (child < currentSize &&compareTo(child+1,child)<0){
                child++;
            }
            if (compareTo(child,k)<0){
                swap(child,k);
            }else {
                break;
            }
        }

    }

    // 其他APIs

}
