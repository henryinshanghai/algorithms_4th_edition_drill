package com.henry.symbol_table_chapter_03.symbol_table_01;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;

public class BinarySearchST_drill01<Key extends Comparable<Key>, Value> {
    // 底层数据结构
    private Key[] keys;
    private Value[] values;
    private int N;

    public BinarySearchST_drill01(int capacity) {
        keys = (Key[])new Comparable[capacity];
        values = (Value[]) new Object[capacity];
        N = 0;
    }

    // 快捷API
    public boolean isEmpty(){
        return N == 0;
    }

    public int size(){
        return N;
    }

    public boolean contains(Key key) {
        if(key == null) throw new IllegalArgumentException("符号表中不存在以null作为键的键值对");
        return get(key) != null;
    }

    // 核心API
    public Value get(Key key) {
        if(key == null) throw new IllegalArgumentException("符号表中不存在以null作为键的键值对");

        // Mark: 特殊情况：符号表已经空掉了
        if (isEmpty()) return null;

        // key在keys[]中存不存在
        int expectPos = rank(key);
        if (expectPos < N && keys[expectPos].compareTo(key) == 0) { // 存在...
            return values[expectPos];
        }

        return null; // 不存在
    }

    public void put(Key key, Value value) {
        if(key == null) throw new IllegalArgumentException("符号表中不存在以null作为键的键值对");

        // 删除操作
        if (contains(key) && value == null) {
            delete(key);
            return;
        }

        // 更新操作
        // 判断key是不是存在于keys[]中
        // 手段1：contains(key) 这会执行一个get()————其中有多个不需要的步骤
        // 手段2：通过rank(key) + keys[rank(key)]的方式————1 rank(key)的结果能在下面接着用到 2 keys[]是底层操作，没有多余的步骤
        if (contains(key)) { // 虽然都能得到预期的效果，但是不同的实现性能肯定会有些差异
            int i = rank(key);
            values[i] = value;
            return;
        }

        if (N == keys.length) {
            resize(keys.length * 2);
        }

        // 插入操作 比起链表来说要复杂些
        int i = rank(key); // 计算了两次，感觉性能要差一些
        // 遍历，从后往前地 向后移动一个位置
        for (int j = N; j > i; j--) { // 这里可以引用a[n]是因为n并不是a.length; 而是a中所包含的元素个数
            keys[j] = keys[j - 1];
            values[j] = values[j - 1];
        }
        keys[i] = key;
        values[i] = value;

        N++;
    }

    /**
     * 从符号表中删除指定key的键值对
     * @param key
     */
    private void delete(Key key) {
        if(key == null) throw new IllegalArgumentException("符号表中不存在以null作为键的键值对");
        if(isEmpty()) return;

        // key不在keys[]中 等价于 key不在符号表中 等价于 通过get()得到的结果为null
//        if (!contains(key)) {
//            return;
//        }

        int i = rank(key);

        // 如果key不在keys[]中 则：return; key not in table
        if (i == N || keys[i].compareTo(key) != 0) {
            return;
        }

        // 如果在keys[]中 则：遍历，从前向后地进行覆盖
        for (int j = i; j < N-1; j++) {
            keys[j] = keys[j + 1];
            values[j] = values[j + 1];
        }

        N--;
        // 物理删除
        keys[N] = null;
        values[N] = null;

        // 判断是否需要缩容
        if (N > 0 && N == keys.length / 4) {
            resize(keys.length / 2);
        }

    }

    /**
     * 对数组的容量大小进行调整
     * @param newCapacity
     */
    private void resize(int newCapacity) {
        Key[] tempKeys = (Key[]) new Comparable[newCapacity];
        Value[] tempVals = (Value[])new Object[newCapacity];

        for (int i = 0; i < N; i++) {
            tempKeys[i] = keys[i];
            tempVals[i] = values[i];
        }

        keys = tempKeys;
        values = tempVals;
    }

    /**
     * 作用：找到keys[]中比key更小的元素的个数
     * 手段：使用二分查找的方式；
     * 条件：keys[]是一个排序后的数组
     * @param key
     * @return
     */
    private int rank(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to rank() is null");

        int lo = 0;
        int hi = N-1; // Mark:这里是用N，而不是keys.length
        while (lo <= hi) { //
            int mid = lo + (hi - lo) / 2;
            int cmp = key.compareTo(keys[mid]);
            if (cmp > 0) lo = mid + 1;
            else if(cmp < 0) hi = mid - 1;
            else return mid;
        }

        return lo;
    }

    // 其他一些个方法

    /**
     * keys[]中大于等于key的最小值
     * @param key
     * @return
     */
    public Key ceiling(Key key) {
        if (key == null) throw new IllegalArgumentException("something wrong!");
        int i = rank(key);
        if (i == N) return null;
        return keys[i];
    }

    /**
     * keys[]中小于等于指定key的最大值
     */
    public Key floor(Key key) {
        if (key == null) throw new IllegalArgumentException("something wrong!");

        int i = rank(key);
        if (i == 0) return null;

        if (i < N && keys[i].compareTo(key) == 0) {
            return keys[i];
        } else {
            return keys[i - 1];
        }
    }

    /**
     * 返回指定区间内的keys[]中的元素数量
     */
    public int size(Key lo, Key hi) {
        if (lo == null) throw new IllegalArgumentException("something wrong!");
        if (hi == null) throw new IllegalArgumentException("something wrong!");

        if (lo.compareTo(hi) >0) return 0;

        if (contains(hi)) {
            return rank(hi) - rank(lo) + 1;
        } else {
            return rank(hi) - rank(lo);
        }
    }

    public Iterable<Key> keys(){
        Queue<Key> queue = new Queue<>();
        for (int i = 0; i < N; i++) {
            queue.enqueue(keys[i]);
        }

        return queue;
    }

    public static void main(String[] args) {
        BinarySearchST_drill01<String, Integer> st = new BinarySearchST_drill01<>(10);

        for (int i = 0; !StdIn.isEmpty(); i++) {
            st.put(StdIn.readString(), i);
        }

        for (String key : st.keys()) {
            System.out.println(key + " : " + st.get(key));
        }
    }
}
