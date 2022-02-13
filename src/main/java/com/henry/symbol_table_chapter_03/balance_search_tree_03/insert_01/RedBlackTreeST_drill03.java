package com.henry.symbol_table_chapter_03.balance_search_tree_03.insert_01;

import edu.princeton.cs.algs4.Queue;

public class RedBlackTreeST_drill03<Key extends Comparable<Key>, Value> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private Node root;
    private int n;

    class Node {
        private Key key;
        private Value val;
        private Node left, right;
        private boolean color;

        public Node(Key key, Value val, boolean color) {
            this.key = key;
            this.val = val;
            this.color = color;
        }
    }

    // quick api
    public boolean isEmpty(){
        return size() == 0;
    }


    public int size(){
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        return n;
    }

    public boolean contains(Key key) {
        if (key == null) throw new IllegalArgumentException("the key can not be null");

        return contains(root, key);
    }

    private boolean contains(Node x, Key key) {
        if (x == null) return false;

        int cmp = key.compareTo(x.key);
        if(cmp > 0) return contains(x.right, key);
        else if(cmp < 0) return contains(x.left, key);
        else return true;
    }

    // core api
    public void put(Key key, Value val){
        if (key == null) throw new IllegalArgumentException("the key can not be null");

        if (val == null) {
            System.out.println("trying to delete the key-value pair");
        }

        root = put(root, key, val);
    }

    /**
     * add given key-value into given tree
     * @param x
     * @param key
     * @param val
     * @return
     */
    private Node put(Node x, Key key, Value val) {
        if(x == null) {
            n++;
            return new Node(key, val, RED);
        }

        int cmp = key.compareTo(x.key);
        if(cmp > 0) x.right = put(x.right, key, val);
        else if(cmp  < 0) x.left = put(x.left, key, val);
        else x.val = val;

        // re-balance the red links in tree
        if (!isRed(x.left) && isRed(x.right)) x = leftRotate(x);
        if(isRed(x.left) && isRed(x.left.left)) x = rightRotate(x);
        if(isRed(x.left) && isRed(x.right)) x = flipColor(x); // todo verify if this is correct

        return x;
    }

    private Node flipColor(Node x) {
        x.left.color = BLACK;
        x.right.color = BLACK;
        x.color = RED;

        return x;
    }

    private Node rightRotate(Node x) {
        Node h = x.left;
        x.left = h.right;

        h.right = x;
        h.color = x.color;

        x.color = RED;

        return h;
    }

    private Node leftRotate(Node x) {
        Node h = x.right;
        x.right = h.left;

        h.left = x;
        h.color = x.color;

        x.color = RED;
        return h;
    }

    private boolean isRed(Node x) {
        if (x == null) return false;
        return x.color;
    }

    //
    public Iterable<Key> keys(){
        Queue<Key> queue = new Queue<Key>();

        keys(root, queue);
        return queue;
    }

    private void keys(Node x, Queue<Key> queue) {
        if (x == null) return;

        keys(x.left, queue);
        queue.enqueue(x.key);
        keys(x.right, queue);
    }

    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException(" the key can not be null");

        return get(root, key);
    }

    private Value get(Node x, Key key) {
        if (x == null) return null;

        int cmp = key.compareTo(x.key);
        if (cmp > 0) return get(x.right, key);
        else if(cmp < 0) return get(x.left, key);
        else return x.val;
    }

    public static void main(String[] args) {
        String test = "S E A R C H E X A M P L E";
        String[] keys = test.split(" ");

        RedBlackTreeST_drill03<String, Integer> st = new RedBlackTreeST_drill03<>();
        for (int i = 0; i < keys.length; i++) {
            st.put(keys[i], i);
        }

        for (String key : st.keys()) {
            System.out.println(key + " : " + st.get(key));
        }
    }

}
