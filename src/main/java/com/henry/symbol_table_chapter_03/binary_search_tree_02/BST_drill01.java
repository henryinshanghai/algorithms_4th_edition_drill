package com.henry.symbol_table_chapter_03.binary_search_tree_02;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;

import java.util.NoSuchElementException;

/**
 * 符号表的实现
 * 注：使用二叉查找树作为底层数据结构来实现
 */
public class BST_drill01<Key extends Comparable<Key>, Value> {
    private Node root;

    class Node {
        private Key key;
        private Value val;
        private Node left;
        private Node right;
        private int size; // 树的节点数量

        public Node(Key key, Value val, int size) {
            this.key = key;
            this.val = val;
            this.size = size;
        }
    }

    // API
    public boolean isEmpty(){
        return size(root) == 0;
    }

    public int size(){
        return size(root);
    }

    private int size(Node x) {
//        return x.size;

        if (x == null) return 0;
        else return x.size;
    }

    public boolean contains(Key key) {
        if (key == null) throw new IllegalArgumentException("the key can not be null");
        return get(key) != null;
    }

    // 核心API
    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("the key can not be null");

        return get(root, key);
    }

    /**
     * 对二叉树这种递归结构来说，几乎所有的操作都需要通过递归来完成 OR 循环
     * @param x
     * @param key
     * @return
     */
    private Value get(Node x, Key key) {
        if (key == null) throw new IllegalArgumentException("the key can not be null");

        if (x == null) return null;

        int cmp = key.compareTo(x.key);
        if (cmp > 0) return get(x.right, key);
        else if(cmp < 0) return get(x.left, key);
        else return x.val;
    }

    public void put(Key key, Value val) {
        if (key == null) throw new IllegalArgumentException("the key can not be null");

        // 执行删除操作
        if (val == null) {
            delete(key);
            return;
        }

        // 执行更新/插入操作
        root = put(root, key, val);
    }

    /**
     * 向二叉树中插入/更新一个节点，并返回更新后的二叉树
     * @param x
     * @param key
     * @param val
     * @return
     */
    private Node put(Node x, Key key, Value val){
        if (key == null) throw new IllegalArgumentException("the key can not be null");

        if (x == null) return new Node(key, val, 1);

        int cmp = key.compareTo(x.key);
        if (cmp > 0) x.right = put(x.right, key, val);
        else if (cmp < 0) x.left = put(x.left, key, val);
        else x.val = val;

        x.size = size(x.left) + size(x.right) + 1;
        return x;

    }

    // 删除操作
    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("the key can not be null");
        root = delete(root, key);
    }
    
//    // 获取到二叉树所有的键集合     手段：1 遍历二叉树； 2 把当前节点添加到queue中
//    public Iterable<Key> keys(){
//        if (isEmpty()) return new Queue<Key>();
//        return keys(min(), max());
//    }
//
//    public Key max(){
//        if (isEmpty()) throw new NoSuchElementException("二叉树已经空了");
//
//        return max(root).key;
//    }
//
//    private Node max(Node x) {
//        if (x.right == null) return x;
//        else return max(x.right);
//    }
//
//
//    public Iterable<Key> keys(Key lo, Key hi) {
//        if (lo == null) throw new IllegalArgumentException("first argument to keys() is null");
//        if (hi == null) throw new IllegalArgumentException("second argument to keys() is null");
//
//        Queue<Key> queue = new Queue<Key>();
//        keys(root, queue, lo, hi);
//        return queue;
//    }
//
//    /**
//     * 获取到二叉树中指定区间内的键集合
//     * @param x
//     * @param queue
//     * @param lo
//     * @param hi
//     */
//    private void keys(Node x, Queue<Key> queue, Key lo, Key hi) {
//        if (x == null) return;
//
//        /* 判断区间的范围 */
//        // 比较左区间、右区间与节点key
//        int cmplo = lo.compareTo(x.key);
//        int cmphi = hi.compareTo(x.key);
//
//        // 1 区间横跨左子树
//        if (cmplo < 0) keys(x.left, queue, lo, hi);
//        // 2 区间横跨根节点
//        if (cmplo <= 0 && cmphi >= 0) queue.enqueue(x.key);
//        // 3 区间横跨右子树
//        if (cmphi > 0) keys(x.right, queue, lo, hi);
//    }


    public Iterable<Key> keys(){
        Queue<Key> queue = new Queue<>();
        keys(root, queue);
        return queue;
    }


    private void keys(Node x, Queue<Key> queue) {
        if (x == null) return;

        keys(x.left, queue);
        queue.enqueue(x.key);
        keys(x.right, queue);
    }

    /**
     * 从二叉树中删除指定节点
     * 注：删除是最难的操作
     * 算法：
     * 1 把指向预期被删除的节点的链接保存到t；
     * 2 设置一个x来指向successor；
     * 3 设置x的右链接（右链接指向所有key大于x.key的节点）连接到deleteMin(t.right)；
     * 4 设置x的左链接（曾经为null）连接到t.left(指向所有key小于x.key以及其继任者的key的键集合)
     * @param x
     * @param key
     * @return
     */
    private Node delete(Node x, Key key) {
        if (key == null) throw new IllegalArgumentException("the key can not be null");

        if (x == null) return null;

        int cmp = key.compareTo(x.key);
        if (cmp > 0) x.right = delete(x.right, key);
        else if (cmp < 0) x.left = delete(x.left, key);
        else { // 根节点就是要删除的节点,怎么办？
            Node t = x;
            x = min(t.right); // 获取到“预期被删除节点的successor”
            x.right = deleteMin(t.right); // 更新successor的右子树
            x.left = t.left; // 更新successor的左子树
        }

        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    public void deleteMin(){
        if (root == null) throw new NoSuchElementException("符号表underflow~");

        root = deleteMin(root);
    }

    /**
     * 删除二叉树中的最小节点，并返回新的二叉树
     * @param x
     * @return
     */
    private Node deleteMin(Node x) {
        if (x == null) return null;

        if (x.left == null) return x.right;
        // 二叉树 = 根节点 + 左子树 + 右子树
        x.left = deleteMin(x.left);

        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    /**
     * 获取到符号表中的最小key
     * @return
     */
    public Key min(){
        if (isEmpty()) throw new NoSuchElementException("符号表为空");

        return min(root).key;
    }
    /**
     * 获取到二叉树中的最小节点
     * @param x
     * @return
     */
    private Node min(Node x) {
        if (x == null) return null;

        if (x.left == null) return x;
        return min(x.left);
    }

    public static void main(String[] args) {
        BST_drill01<String, Integer> bst = new BST_drill01<>();

        for (int i = 0; !StdIn.isEmpty(); i++) {
            bst.put(StdIn.readString(), i);
        }

        for (String key : bst.keys()) {
            System.out.println(key + " : " + bst.get(key));
        }
    }
}
