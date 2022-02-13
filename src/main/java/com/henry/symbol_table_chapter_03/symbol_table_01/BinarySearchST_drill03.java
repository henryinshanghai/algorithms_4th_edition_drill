package com.henry.symbol_table_chapter_03.symbol_table_01;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;

public class BinarySearchST_drill03<Key extends Comparable<Key>, Value> {

    private Key[] keys;
    private Value[] vals;
    private int n;

    public BinarySearchST_drill03(int capacity) {
        keys = (Key[]) new Comparable[capacity];
        vals = (Value[]) new Object[capacity];
        n = 0;
    }

    // API
    public boolean isEmpty() {
        return n == 0;
    }

    public int size(){
        return n;
    }

    public boolean contains(Key key) {
        if(key == null) throw new IllegalArgumentException("something wrong about your key");
        return get(key) != null;
    }

    // 核心API
    public Value get(Key key) {
        if(key == null) throw new IllegalArgumentException("something wrong about your key");

        int i = rank(key);
        if (i < n && key.compareTo(keys[i]) == 0) {
            return vals[i];
        }

        return null;
    }

    private int rank(Key key) {
        int lo = 0, hi = n - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int cmp = key.compareTo(keys[mid]);
            if (cmp > 0) lo = mid + 1;
            else if(cmp < 0) hi = mid - 1;
            else return mid;
        }

        return lo;
    }

    public void put(Key key, Value val){
        if(key == null) throw new IllegalArgumentException("something wrong about your key");

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
        // 从后往前遍历，从前往后移动 注意边界
        for (int j = n; j > i ; j--) { // a[n] = a[n-1]  a[i+1] = a[i]
            keys[j] = keys[j - 1];
            vals[j] = vals[j - 1];
        }

        keys[i] = key;
        vals[i] = val;

        n++;
    }

    private void delete(Key key) {
        if(key == null) throw new IllegalArgumentException("something wrong about your key");

        int i = rank(key);
        if (i == n || key.compareTo(keys[i]) != 0) return;

        // 从数组中删除一个元素
        // 从前往后遍历，从后往前覆盖
        for (int j = i; j < n-1; j++) { // a[i] = a[i+1] a[n-2] = a[n-1]
            keys[j] = keys[j + 1];
            vals[j] = vals[j + 1];
        }

        n--;
        keys[n] = null;
        vals[n] = null;

        if (n > 0 && n == keys.length / 4 ) resize(keys.length / 2);
    }

    private void resize(int newCapacity) {
        Key[] tempKey = (Key[]) new Comparable[newCapacity];
        Value[] tempValue = (Value[]) new Object[newCapacity];

        for (int i = 0; i < n; i++) {
            tempKey[i] = keys[i];
            tempValue[i] = vals[i];
        }

        keys = tempKey;
        vals = tempValue;
    }

    public Iterable<Key> keys(){
        Queue<Key> queue = new Queue();
        for (int i = 0; i < n; i++) {
            queue.enqueue(keys[i]);
        }

        return queue;
    }

    public static void main(String[] args) {
        BinarySearchST_drill03<String, Integer> st = new BinarySearchST_drill03<>(10);

        for (int i = 0; !StdIn.isEmpty(); i++) {
            st.put(StdIn.readString(), i);
        }

        for (String key : st.keys()) {
            System.out.println(key + " :" + st.get(key));
        }
    }
}
// OMG!