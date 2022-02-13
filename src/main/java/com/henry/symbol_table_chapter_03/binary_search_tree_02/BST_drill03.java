package com.henry.symbol_table_chapter_03.binary_search_tree_02;

import edu.princeton.cs.algs4.Queue;

import java.util.NoSuchElementException;

public class BST_drill03<Key extends Comparable<Key>, Value> {

    private Node root;

    class Node{
        private Key key;
        private Value val;
        private Node left, right;
        private int size;

        public Node(Key key, Value val, int size) {
            this.key = key;
            this.val = val;
            this.size = size;
        }
    }

    // API
    public boolean isEmpty(){
        return size() == 0;
    }

    public int size(){
        return size(root);
    }

    /**
     * size()的实现中必须处理二叉树为空的情况 否则在x.size可能回导致空指针
     * @param x
     * @return
     */
    private int size(Node x) {
        if (x == null) return 0;
        else return x.size;
//        return x.size;
    }

    public boolean contains(Key key) {
        if(key == null) throw new IllegalArgumentException("the key can not be null");

        return get(key) != null;
    }

    public Value get(Key key) {
        if(key == null) throw new IllegalArgumentException("the key can not be null");

        return get(root, key);
    }

    private Value get(Node x, Key key) {
        if (x == null) throw new NoSuchElementException("the symbol table is empty now");

        int cmp = key.compareTo(x.key);
        if (cmp > 0) return get(x.right, key);
        else if(cmp < 0) return get(x.left, key);
        else return x.val;
    }

    public void put(Key key, Value val){
        if(key == null) throw new IllegalArgumentException("the key can not be null");

        if (val == null) {
            delete(key);
            return;
        }

        // insert a key-value pair or update
        root = put(root, key, val);

    }

    private Node put(Node x, Key key, Value val) {
        if (x == null) return new Node(key, val, 1);

        int cmp = key.compareTo(x.key);
        if (cmp > 0) x.right = put(x.right, key, val);
        else if(cmp < 0) x.left = put(x.left, key, val);
        else x.val = val;

        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    private void delete(Key key) {
        if(key == null) throw new IllegalArgumentException("the key can not be null");

        root = delete(root, key);
    }

    private Node delete(Node x, Key key) {
//        if (x == null) throw new NoSuchElementException("the symbol table is empty now");

        if(x == null) return null;

        int cmp = key.compareTo(x.key);
        if (cmp > 0) delete(x.right, key);
        else if (cmp < 0) delete(x.left, key);
        else {
            // 新增
            if (x.right == null) return x.left;
            if (x.left  == null) return x.right;

            Node t = x;
            x = min(t.right);
            x.right = deleteMin(t.right);
            x.left = t.left;
        }

        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    // delete the smallest node in the tree, and return the renewed tree.
    private Node deleteMin(Node x) {
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);

        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    // find the smallest node in the tree
    private Node min(Node x) {
//        if (x == null) throw new NoSuchElementException("the symbol table is empty now");
        if (x.left == null) return x;

        return min(x.left);
    }

    public Iterable<Key> keys() {
//        if(root == null) throw new NoSuchElementException("the symbol table is empty now");
        if(root == null) return new Queue<Key>();

        Queue<Key> queue = new Queue<>();
        keys(root, queue);
        return queue;
    }

    private void keys(Node x, Queue<Key> queue) {
        if (x == null) return;

        // one way to traversal in tree
        keys(x.left, queue);
        queue.enqueue(x.key);
        keys(x.right, queue);
    }

    public static void main(String[] args) {
        // 这里的字符串不能包含空字符 否则会被视为不同的字符串 alt + shift + a进行多列操作
        String[] strings = {"S", "E", "A", "R", "C", "H", "E", "X", "A", "M", "P", "L", "E"};

        BST_drill03<String, Integer> bst = new BST_drill03<>();
        for (int i = 0; i < strings.length; i++) {
            bst.put(strings[i], i);
        }

        for (String key : bst.keys()) {
            System.out.println(key + " : " + bst.get(key));
        }
    }
}
/*
启示：
1 size()的实现中必须处理二叉树为空的情况 否则在x.size可能回导致空指针
2 // 这里的字符串不能包含空字符 否则会被视为不同的字符串 alt + shift + a进行多列操作
 */