package com.henry.symbol_table_chapter_03.binary_search_tree_02;

import edu.princeton.cs.algs4.Queue;

import java.util.NoSuchElementException;

/**
 * 使用二叉查找树来实现符号表
 * 特征：多用递归，找准套路
 */
public class BST_drill02<Key extends Comparable<Key>, Value> {
    private Node root;

    class Node{
        private Key key;
        private Value value;
        private Node left, right;
        private int size;

        public Node(Key key, Value value, int size) {
            this.key = key;
            this.value = value;
            this.size = size;
        }
    }

    // API
    public boolean isEmpty(){
        return size() == 0;
    }

    private int size() {
        return size(root);
    }

    private int size(Node x) {
        if(x == null) return 0;
        return x.size;
    }

    public boolean contains(Key key) {
        if (key == null) throw new IllegalArgumentException("key can not be null");
        return get(key) != null;
    }

    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("key can not be null");

        return get(root, key);
    }

    private Value get(Node x, Key key) {
        if (x == null) throw new NoSuchElementException("the symbol table is empty!");

        int cmp = key.compareTo(x.key);
        if (cmp > 0) return get(x.right, key);
        else if(cmp < 0) return get(x.left, key);
        else return x.value;
    }

    public void put(Key key, Value val){
        if (key == null) throw new IllegalArgumentException("key can not be null");

        if (val == null) {
            delete(key);
            return;
        }

        // insert OR update via one method————cos they all get return a new tree/node
        root = put(root, key, val);
    }

    private Node put(Node x, Key key, Value val) {
        // end condition - the tree is empty
        if (x == null) return new Node(key, val, 1);

        int cmp = key.compareTo(x.key);
        if (cmp > 0) x.right = put(x.right, key, val);
        else if(cmp < 0) x.left = put(x.left, key, val);
        else x.value = val;

        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    private void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("key can not be null");

        root = delete(root, key);
    }

    private Node delete(Node x, Key key) {
        if (x == null) throw new NoSuchElementException("the symbol table now is empty");

        int cmp = key.compareTo(x.key);
        if (cmp > 0) x.right = delete(x.right, key);
        else if(cmp < 0) x.left = delete(x.left, key);
        else {
            Node t = x;
            x = min(t.right);
            x.right = deleteMin(t.right);
            x.left = t.left;
        }

        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    // delete the smallest node in tree, then return a renewed tree
    private Node deleteMin(Node x) {
        if (x.left == null) return x.right;

        x.left = deleteMin(x.left);
        x.size = size(x.left) + size(x.right) + 1;

        return x;
    }


    private Node min(Node x) {
        // this is part of recursive process- the end condition
        if (x == null) throw new NoSuchElementException("the symbol table is empty now!");

        return min(x.left);
    }


    // keys
    public Iterable<Key> keys(){
        if (root == null) return new Queue<Key>();

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

    public static void main(String[] args) {
        BST_drill02<String, Integer> bst = new BST_drill02<>();

        String[] strings ={"S", "E", "A", "R", "C", "H", "E", "X", "A", "M", "P", "L", "E"};
        for (int i = 0; i < strings.length; i++) {
            bst.put(strings[i], i);
        }

        for (String key : bst.keys()) {
            System.out.println(key + " :" + bst.get(key));
        }
    }

}
