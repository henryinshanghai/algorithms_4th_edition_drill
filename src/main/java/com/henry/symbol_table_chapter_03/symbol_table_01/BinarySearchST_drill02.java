package com.henry.symbol_table_chapter_03.symbol_table_01;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;

public class BinarySearchST_drill02<Key extends Comparable<Key>, Value>{

    private Key[] keys;
    private Value[] vals;
    private int n;

    public BinarySearchST_drill02(int capacity) {
        keys = (Key[]) new Comparable[capacity];
        vals = (Value[]) new Object[capacity];
        n = 0;
    }

    // 辅助API
    public boolean isEmpty(){
        return n == 0;
    }

    public int size(){
        return n;
    }

    public boolean contains(Key key) {
        if(key == null) throw new IllegalArgumentException("key值不能为null");
        return get(key) != null;
    }

    // 核心API
    public Value get(Key key) {
        if(key == null) throw new IllegalArgumentException("key值不能为null");

        if (isEmpty()) return null;

        int i = rank(key);
        if (i < n && keys[i].compareTo(key) == 0) { // key存在于keys[]中
            return vals[i];
        }

        return null; // 不存在
    }

    public void put(Key key, Value val){
        if(key == null) throw new IllegalArgumentException("key值不能为null");

        // 实现删除————当val==null时，尝试删除
        if (val == null) {
            // 在delete中判断key是否存在于keys[]中
            delete(key);
            return;
        }

        // 更新操作
        int i = rank(key);
        if (i < n && keys[i].compareTo(key) == 0) {
            vals[i] = val;
            return;
        }

        // 插入操作 这个本来是需要分类讨论的，但是通过代码避免了烦人的if/else
        if (n == keys.length) resize(keys.length * 2);
        // 从后往前遍历，向后移动一个位置 a[i+1] = a[i]
        for (int j = n; j > i ; j--) {
            keys[j] = keys[j - 1];
            vals[j] = vals[j - 1];
        }

        keys[i] = key;
        vals[i] = val; // 插入操作本身使得存储的key是有序的

        n++;
    }

    /**
     * 删除指定的key以及其关联的值
     * @param key
     */
    private void delete(Key key) {
        if(key == null) throw new IllegalArgumentException("key值不能为null");

        int i = rank(key);
        // key不存在...
        if (i == n ||  keys[i].compareTo(key) != 0) {
            return;
        }

        // 从数组中删除元素：从前往后遍历，从后往前覆盖
        for (int j = i; j < n-1; j++) { // a[n-2] = a[n-1]
            keys[j] = keys[j + 1];
            vals[j] = vals[j + 1];
        }

        n--;
        keys[n] = null;
        vals[n] = null;

        if (n > 0 && n == keys.length / 4) resize(keys.length / 2);
    }

    private void resize(int newCapacity) {
        Key[] tempKey = (Key[]) new Comparable[newCapacity];
        Value[] tempVal = (Value[]) new Object[newCapacity];

        for (int i = 0; i < newCapacity; i++) {
            tempKey[i] = keys[i];
            tempVal[i] = vals[i];
        }

        keys = tempKey;
        vals = tempVal;
    }

    /**
     * 计算出指定的key在keys[]中预期出现的位置
     * @param key
     * @return
     */
    private int rank(Key key) {
        if(key == null) throw new IllegalArgumentException("key值不能为null");

        int lo = 0;
        int hi = n - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int cmp = key.compareTo(keys[mid]);
            if (cmp > 0) lo = mid + 1;
            else if(cmp < 0) hi = mid - 1;
            else return mid;
        }

        return lo;
    }

    public Iterable<Key> keys(){
        Queue<Key> queue = new Queue<Key>();

        for (int i = 0; i < n; i++) {
            queue.enqueue(keys[i]);
        }

        return queue;
    }

    public static void main(String[] args) {
        BinarySearchST_drill02<String, Integer> st = new BinarySearchST_drill02<>(10);

        for (int i = 0; !StdIn.isEmpty(); i++) {
            st.put(StdIn.readString(), i);
        }

        for (String key : st.keys()) {
            System.out.println(key + ":" + st.get(key));
        }
    }
}
// 大不了就分类讨论嘛