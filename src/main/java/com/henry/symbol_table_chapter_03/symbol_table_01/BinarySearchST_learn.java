package com.henry.symbol_table_chapter_03.symbol_table_01;


import edu.princeton.cs.algs4.Queue;
import sun.font.FontRunIterator;

public class BinarySearchST_learn<Key extends Comparable<Key>, Value> {
    // 实例变量
    private Key[] keys;
    private Value[] vals;
    private int N;

    public BinarySearchST_learn(int capacity) {
        /* 调整数组的大小 */

        keys = (Key[]) new Comparable[capacity];
        vals = (Value[]) new Object[capacity];

    }

    // 快捷API
    public int size(){
        return N;
    }

    public boolean isEmpty(){
        return N == 0;
    }

    // 核心API
    public Value get(Key key) {
        if(isEmpty()) return null;
        int i = rank(key);

        if(i < N && keys[i].compareTo(key) == 0) return vals[i];
        else return null;
    }

    /**
     * 作用：返回数组/符号表中小于给定键key的键数量
     * @param key
     * @return
     */
    public int rank(Key key) {
        int lo = 0, hi = N-1;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            int cmp = key.compareTo(keys[mid]);
            if(cmp < 0){
                hi = mid - 1;
            } else if (cmp > 0) {
                lo = mid + 1;
            } else {
                return mid;
            }
        }

        return lo;
    }

    public void put(Key key, Value val) {
        // 查找键：找到则进行更新，没找到就添加新元素
        int i = rank(key);
        if (i < N && keys[i].compareTo(key) == 0) {
            vals[i] = val;
            return;
        }

        // 把位置j后面的元素都往后移动一位 a[N-1] -> a[N]
        for (int j = N; j > i; j--) {
            keys[j] = keys[j - 1];
            vals[j] = vals[j - 1];
        }

        // 把新添的键值对存储到数组中
        keys[i] = key;
        vals[i] = val;

        N++;
    }

    public void delete(Key key) {
        // 自己实现的版本
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] == key) {
                vals[i] = null;
            }
        }
    }

    // 扩展API————由于key有序而扩展的其他操作
    public Key min(){
        return keys[0];
    }

    public Key max(){
        return keys[N - 1];
    }

    /**
     * 选择/获取到指定位置的键
     * @param k
     * @return
     */
    public Key select(int k) {
        return keys[k];
    }


    /**
     * 获取到大于等于指定key的最小键
     * @param key
     * @return
     */
    public Key ceiling(Key key) {
        // 数组：a[0 1 2 3 5 7]
        // 查找的元素：6 小于6的键数量 = 5; 按照定义ceiling(6) = 7;  a[5] = 7 aka a[rank(key)] = ceiling(key)
        // 查找的元素：7 小于7的键数量/元素7的位置 = 5； 按照定义ceiling(6) = 7; aka a[rank(key)] = ceiling(key);
        // 1 获取到小于key的键的数量（键的位置）
        if (key == null) throw new IllegalArgumentException("search table's key can not be null");
        int i = rank(key);
        if (i == N) { // 用户传入的key大于keys[]中的任何元素
            return null;
        }
        return keys[i]; // 原理：小于key的键数量 总是= ceiling的下标
    }

    /**
     * 获取到小于/等于指定key的最大值
     * a[] {0, 1, 2, 3, 5, 7} key=6 rank(6) = 5 floor(6) = 5  a[4] = 5 => a[rank(key)-1] = floor(key)
     * 原理：wait up!
     * @param key
     * @return
     */
    public Key floor(Key key){
        int i = rank(key);
        return keys[i - 1];
    }


    /**
     * 获取到符号表中指定区间的键集合
     */
    public Iterable<Key> keys(Key lo, Key hi) {
        Queue<Key> q = new Queue<>();

        for (int i = rank(lo); i < rank(hi); i++) { // 1 指定循环执行的次数； 2 为循环体提供局部变量
            q.enqueue(keys[i]);
        }

        // 对hi进行单独处理
        if (contains(hi)) {
            q.enqueue(keys[rank(hi)]);
        }

        return q;

    }

    private boolean contains(Key key) {
        if (key == null) throw new IllegalArgumentException("符号表中的键不能为null");

        return vals[rank(key)] != null;
    }
}
