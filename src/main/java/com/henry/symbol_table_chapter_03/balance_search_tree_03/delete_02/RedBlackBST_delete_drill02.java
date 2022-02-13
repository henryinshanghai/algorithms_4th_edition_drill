package com.henry.symbol_table_chapter_03.balance_search_tree_03.delete_02;

public class RedBlackBST_delete_drill02<Key extends Comparable<Key>, Value> {
    private Node root;

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    class Node{
        private Key key;
        private Value val;
        private Node left, right;
        private boolean color;
        private int size;

        public Node(Key key, Value val, boolean color, int size) {
            this.key = key;
            this.val = val;
            this.color = color;
            this.size = size;
        }
    }

    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("key can not be null");

        if (!contains(key)) return;

        if (!isRed(root.left) && !isRed(root.right)) {
            root.color = RED;
        }

        root = delete(root, key);

        if (!isEmpty()){
            root.color = BLACK;
        }
    }

    /**
     * 递归方法
     * 作用：从树中删除指定的节点，并返回新的树
     * 递归方法的组成：左子树 + 根节点 + 右子树
     * @param x
     * @param key
     * @return
     */
    private Node delete(Node x, Key key) {
        // 1 左子树 phrase1
        if (key.compareTo(x.key) < 0) {
            // 判断当前节点及其左子节点是否为红节点 作用：判断它是不是3-节点
            if (!isRed(x.left) && !isRed(x.left.left)) {
                // 把节点变换成3-节点/4-节点      手段：把右子树中的红链接移动到左子树中
                x = moveRedToLeft(x);
            }

            x.left = delete(x.left, key);
        }
        else { // 右子树&根节点
            if (isRed(x.left)) {
                x = rightRotate(x);
            }

            // 循环终结条件   删除的最简情况：树中就只有一个节点，它刚好就是待删除的节点
            // 注意这是一个红黑树    aka 右子树为null时，左子树肯定不会有黑节点 而且左子树也不会有红节点（1） aka 左子树也为空
            if (key.compareTo(x.key) == 0 && x.right == null) {
                return null;
            }

            if (!isRed(x.right) && !isRed(x.right.left)) {
                x = moveRedToRight(x);
            }

            // phrase2 根节点刚好是预期删除的节点：直接执行删除
            if (key.compareTo(x.key) == 0) {
                Node h = min(x.right);
                x.key = h.key;
                x.val = h.val;
                x.right = deleteMin(x);
            }

            else { // phrase3 从右子树中执行删除
                // recursively call it
                x.right = delete(x.right, key);
            }
        }

        return rebalance(x);
    }

    // delete the smallest node of the tree and return a new tree
    private Node deleteMin(Node x) {
        if (x.left == null) return null;

        if (!isRed(x.left) && !isRed(x.left.left)) {
            x = moveRedToLeft(x);
        }

        x.left = deleteMin(x.left);

        return rebalance(x);
    }

    private Node rebalance(Node x) {
        if (isRed(x.right)) x = leftRotate(x);
        if (isRed(x.left) && isRed(x.left.left)) x = rightRotate(x);
        if (isRed(x.left) && isRed(x.right)) flipColor(x);

        return x;
    }

    // find and return the smallest node in the tree
    private Node min(Node x) {
        if (x.left == null) return x;
        return min(x.left);
    }

    // 把红链接移动到右子树中
    private Node moveRedToRight(Node x) {
        flipColor(x);
        if (isRed(x.left.left)) {
            x = rightRotate(x);
            flipColor(x);
        }

        return x;
    }

    private Node moveRedToLeft(Node x) {
        flipColor(x);
        if (isRed(x.right.left)) {
            x.right = rightRotate(x.right);
            x = leftRotate(x);
            flipColor(x);
        }

        return x;
    }

    private Node leftRotate(Node x) {
        Node h = x.right;
        x.right = h.left;

        h.left = x;
        h.color = x.color;

        x.color = RED;
        return h;
    }

    private Node rightRotate(Node x) {
        Node h = x.left;
        x.left = h.right;

        h.right = x;
        h.color = x.color;

        x.color = RED;
        return h;
    }

    private void flipColor(Node x) {
        x.color = !x.color;
        x.left.color = !x.left.color;
        x.right.color = !x.right.color;
    }

    public boolean isEmpty(){
        return size() == 0;
    }

    public int size(){
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        return x.size;
    }

    private boolean isRed(Node x) {
        if (x == null) return false;
        else return x.color == RED;
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
        if (x == null) return null;

        int cmp = key.compareTo(x.key);
        if (cmp > 0) return get(x.right, key);
        else if(cmp < 0) return get(x.left, key);
        else return x.val;
    }
}
