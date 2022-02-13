package com.henry.symbol_table_chapter_03.symbol_table_01;

import edu.princeton.cs.algs4.Queue;

/**
 * 使用数组的方式实现有序符号表
 * 特征：查找时使用rank()操作
 * 为什么突然会需要rank()方法？顺序查找的符号表就不需要这个呀
 * 那是因为顺序查找的符号表不是键有序的，所以无法执行rank操作
 */
public class BinarySearchST_drill04<Key extends Comparable<Key>, Value> {
    private Key[] keys;
    private Value[] vals;
    private int n;

    public BinarySearchST_drill04(int capacity) {
        keys = (Key[]) new Comparable[capacity];
        vals = (Value[]) new Object[capacity];
        n = 0;
    }

    // 快捷API
    public boolean isEmpty() {
        return n == 0;
    }

    public int size(){
        return n;
    }

    public boolean contains(Key key) {
        if(key == null) throw new IllegalArgumentException("the key can not be null");
        return get(key) != null;
    }

    // 核心API
    public Value get(Key key) {
        if(key == null) throw new IllegalArgumentException("the key can not be null");

        int i = rank(key);
        if (i < n && key.compareTo(keys[i]) == 0) {
            return vals[i];
        }

        return null;
    }


    public void put(Key key, Value val){
        if(key == null) throw new IllegalArgumentException("the key can not be null");

        // 删除操作
        if (val == null) {
            delete(key);
            return;
        }

        // 更新操作
        int i = rank(key);
        if (i < n && key.compareTo(keys[i]) == 0) {
            vals[i] = val;
            return;
        }

        // 插入操作
        if (n == keys.length) resize(keys.length * 2);
        // 从后往前遍历 从前往后移动
        for (int j = n; j > i; j--) { // a[i+1] =  a[i]  a[n] = a[n-1]
            keys[j] = keys[j - 1];
            vals[j] = vals[j - 1];
        }

        keys[i] = key;
        vals[i] = val;
        n++;
    }

    public Iterable<Key> keys(){
        Queue<Key> queue = new Queue<Key>();
        for (int i = 0; i < n; i++) {
            queue.enqueue(keys[i]);
        }

        return queue;
    }

    private void delete(Key key) {
        if(key == null) throw new IllegalArgumentException("the key can not be null");

        int i = rank(key);
        if (i == n || key.compareTo(keys[i]) != 0) {
            return;
        }

        // 从前往后遍历，从后往前覆盖
        for (int j = i; j < n-1; j++) { // a[i] = a[i+1] a[n-2] = a[n-1]
            keys[j] = keys[j + 1];
            vals[j] = vals[j + 1];
        }
        n--;
        keys[n] = null;
        vals[n] = null;

        if (n == keys.length / 4) resize(keys.length / 2);
    }

    private void resize(int newCapacity) {
        Key[] tempKey = (Key[]) new Comparable[newCapacity];
        Value[] tempVal = (Value[]) new Object[newCapacity];

        for (int i = 0; i < n; i++) {
            tempKey[i] = keys[i];
            tempVal[i] = vals[i];
        }

        keys = tempKey;
        vals = tempVal;
    }

    /**
     * 计算出key在keys[]中预期的位置
     * @param key
     * @return
     */
    private int rank(Key key) {
        int lo = 0, hi = n-1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int cmp = key.compareTo(keys[mid]);
            if(cmp > 0) lo = mid + 1;
            else if(cmp < 0) hi = mid - 1;
            else return mid;
        }

        return lo;
    }

    public static void main(String[] args) {
        BinarySearchST_drill04<String, Integer> st = new BinarySearchST_drill04<>(10);

        String[] strings = {"S", "E", "A", "R", "C", "H", "E", "X", "A", "M", "P", "L", "E"};
        for (int i = 0; i < strings.length; i++) {
            st.put(strings[i], i);
        }

        for (String key : st.keys()) {
            System.out.println(key + " : " + st.get(key));
        }
    }
}
// 共计耗时21分钟