package com.henry.symbol_table_chapter_03.balance_search_tree_03.insert_01;

import edu.princeton.cs.algs4.Queue;

public class RedBlackTreeST_drill02<Key extends Comparable<Key>, Value>{
    private static final boolean BLACK = false;
    private static final boolean RED = true;

    private Node root;
    private int n;

    class Node{
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

    // quick apis
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
        if(key == null) throw new IllegalArgumentException("the key cannot be null");

        return contains(root, key);
    }

    private boolean contains(Node x, Key key) {
        int cmp = key.compareTo(x.key);
        if(x == null) return false;

        if (cmp > 0) return contains(x.right, key);
        else if(cmp < 0) return contains(x.left, key);
        else return true;
    }

    // 核心API
    public void put(Key key, Value val){
        if(key == null) throw new IllegalArgumentException("the key cannot be null");

        if(val == null){
            System.out.println("delete method");
        }

        root = put(root, key, val);
    }

    /**
     * add a key-value pair into given tree/node, and return the new tree / node
     * @param x given node/tree
     * @param key
     * @param val
     * @return
     */
    private Node put(Node x, Key key, Value val) {
        if(x == null) {
            n++;
            return new Node(key, val, RED);
        }

        // right position
        int cmp = key.compareTo(x.key);
        if(cmp > 0) x.right = put(x.right, key, val);
        else if(cmp < 0) x.left = put(x.left, key, val);
        else x.val = val;

        // right-ruled tree
        // for red-black tree, after the insert-operation, tree will out of rule
        // thus, we need do something extra to get it in rule
        // method:different oper with different situation
        if(!isRed(x.left) && isRed(x.right)) x =  leftRotate(x);
        if(isRed(x.left) && isRed(x.left.left)) x =  rightRotate(x);
        if (isRed(x.left) && isRed(x.right)) flipColor(x);

        return x;
    }

    /**
     * do flip-color operation for given node, so that we tune it in paper red/black links.
     * @param x
     */
    private void flipColor(Node x) {
        x.left.color = BLACK;
        x.right.color = RED;
        x.color = RED;
    }

    /**
     * do right rotate operation for given node, so that we tune it in proper red links.
     * the thing we need to deal with is: current node + its left child node
     * @param x
     * @return
     */
    private Node rightRotate(Node x) {
        Node h = x.left;
        x.left = h.right;

        h.right = x;
        h.color = x.color; // update the link

        x.color = RED;

        return h;
    }

    /**
     * do left rotate operation for given node, so that we tune it in proper red links
     * note that it's object restrain into 2 object:the current node + its right child node.
     * @param x
     */
    private Node leftRotate(Node x) {
        // rearrange the child tree
        Node h = x.right;
        x.right = h.left;

        // rearrange the link from father
        h.left = x;
        h.color = x.color;

        // rearrange link for the child
        x.color = RED;

        // return current father node
        return h;
    }

    /**
     * judge a given node is a red node or not
     * @param x
     * @return
     */
    private boolean isRed(Node x) {
        if (x == null) return false;
        return x.color;
    }

    // another key api:get
    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("the key can not be null");

        return get(root, key);
    }

    private Value get(Node x, Key key) {
        if(x == null) return null;

        int cmp = key.compareTo(x.key);
        if(cmp > 0) return get(x.right, key);
        else if(cmp <0) return get(x.left, key);
        else return x.val;
    }

    // other api for case code
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

    public static void main(String[] args) {
        String test = "S E A R C H E X A M P L E";
        String[] keys = test.split(" ");

        RedBlackTreeST_drill02<String, Integer> st = new RedBlackTreeST_drill02<>();
        for (int i = 0; i < keys.length; i++) {
            st.put(keys[i], i);
        }

        for (String key : st.keys()) {
            System.out.println(key  + ":" + st.get(key));
        }
    }
}
