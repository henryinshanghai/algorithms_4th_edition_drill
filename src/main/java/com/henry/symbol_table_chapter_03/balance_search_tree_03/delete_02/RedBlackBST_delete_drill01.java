package com.henry.symbol_table_chapter_03.balance_search_tree_03.delete_02;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class RedBlackBST_delete_drill01<Key extends Comparable<Key>, Value> {


    private Node root;
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    class Node {
        private Key key;
        private Value val;
        private Node left, right;
        private int size;
        private boolean color;

        public Node(Key key, Value val, boolean color, int size) {
            this.key = key;
            this.val = val;
            this.size = size;
            this.color = color;
        }
    }

    public boolean isEmpty(){
        return size() == 0;
    }

    public int size(){
        if (root == null) return 0;
        return size(root);
    }

    private int size(Node x) {
        return x.size;
    }

    public boolean contains(Key key) {
        if (key == null) throw new IllegalArgumentException("the key can not be null");
        return get(key) != null;
    }

    // core api
    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("the key can not be null");

        return get(root, key);
    }

    private Value get(Node x, Key key) {
        if (x == null) return null;

        int cmp = key.compareTo(x.key);
        if (cmp > 0) return get(x.right, key);
        else if(cmp < 0) return get(x.left, key);
        else return x.val;
    }

    // put
    public void put(Key key, Value val){
        if (key == null) throw new IllegalArgumentException("the key can not be null");

        if (val == null) {
            delete(key);
        }

        root = put(root, key, val);
    }

    private void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("the key can not be null");

        // 初始化树的根节点颜色
        if (!isRed(root.left) && !isRed(root.right)) {
            root.color = RED;
        }

        root = delete(root, key);

        if (!isEmpty()) {
            root.color = BLACK;
        }
    }

    // how to delete a node in red-black tree???
    private Node delete(Node x, Key key) {
        // 1 对左子树的处理
        if (key.compareTo(x.key) < 0) {
            // 对当前节点进行调整-避免是2-节点
            if (!isRed(x.left) && !isRed(x.left.left)) {
                // 作用：把当前节点的左子节点变成3-节点/4-节点   手段:把红链接移动到左子树
                // 使用手段命名方法
                x = moveRedLeft(x);
            }

            // 从左子树中删除节点
            x.left = delete(x.left, key);
        }
        else { // 2 对根节点/右子树进行处理
            if (isRed(x.left)) {
                x = rightRotate(x);
            }

            if (key.compareTo(x.key) == 0 && x.right == null) {
                return null;
            }

            if (!isRed(x.right) && !isRed(x.right.left)) {
                x = moveRedToRight(x);
            }

            if (key.compareTo(x.key) == 0) {
                // 1 获取到successor
                Node h = min(x.right);
                // 2 覆盖待删除的节点
                x.key = h.key;
                x.val = h.val;

                // 3 删除Replacement/successor
                x.right = deleteMin(x.right);
            }

            else { x.right =  delete(x.right, key);}
        }

        return rebalance(x);
    }

    // delete the smallest node in red-black tree
    private Node deleteMin(Node x) {
        if (x.left == null) return null;

        // 1 in the path to find deleted node, make sure current node is not 2- node
        if (!isRed(x.left) && !isRed(x.left.left)) {
            x = moveRedLeft(x);
        }

        // 2 do the deletion. use recursive method
        x.left = deleteMin(x.left);

        // 3 rebalance the red-black tree's property
        return rebalance(x);
    }

    public Node min(Node x) {
        if (x.left == null) return x;
        else return min(x.left);
    }

    private Node moveRedToRight(Node x) {
        flipColor(x);
        if (isRed(x.left.left)) {
            x = rightRotate(x);
            flipColor(x);
        }

        return x;
    }

    /**
     * 假设h是红色的，而h.left、h.left.left都是黑色的，使h.left或者它的某个子节点变成红色
     * 手段：把红链接移动到左子树
     * @param x
     * @return
     */
    private Node moveRedLeft(Node x) {
        // case2 & case3的通用步骤:翻转颜色
        flipColor(x);
        // 2 判断左子节点的兄弟节点是否是红节点 - 决定能否借一个键值对/把红链接移到左子树中
        if (isRed(x.right.left)) {
            x.right = rightRotate(x.right);
            x = leftRotate(x);
            flipColor(x);
        }
        return x;
    }

    private Node put(Node x, Key key, Value val) {
        if (x == null) {
            return new Node(key, val, RED, 1);
        }

        // do something before recursive call
        int cmp = key.compareTo(x.key);
        if (cmp > 0) x.right = put(x.right, key, val);
        else if(cmp <0) x.left = put(x.left, key, val);
        else x.val = val;

        // after the insertion of red node, we need to re-balance the tree's color
        return rebalance(x);
    }

    private Node rebalance(Node x) {
        if (isRed(x.right)) x = leftRotate(x);
        if(isRed(x.left) && isRed(x.left.left)) x = rightRotate(x);
        if (isRed(x.left) && isRed(x.right)) flipColor(x);

        return x;
    }

    private void flipColor(Node x) {
        x.color = !x.color;
        x.left.color = !x.left.color;
        x.right.color = !x.right.color;
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
        return x.color == RED;
    }

    //
    public Iterable<Key> keys() {
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


    // 如何能够验证：在所有这些操作之后，红黑树仍旧是满足那5条性质的呢？

    public static void main(String[] args) {
        RedBlackBST_delete_drill01<String, Integer> st = new RedBlackBST_delete_drill01<String, Integer>();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            st.put(key, i);
        }
        StdOut.println();
        for (String s : st.keys())
            StdOut.println(s + " " + st.get(s));
        StdOut.println();
    }
}

