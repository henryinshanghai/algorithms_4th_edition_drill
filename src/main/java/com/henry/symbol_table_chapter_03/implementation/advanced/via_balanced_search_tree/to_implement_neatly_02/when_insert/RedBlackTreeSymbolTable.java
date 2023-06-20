package com.henry.symbol_table_chapter_03.implementation.advanced.via_balanced_search_tree.to_implement_neatly_02.when_insert;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

/******************************************************************************
 *  Compilation:  javac RedBlackTreeSymbolTable.java
 *  Execution:    java RedBlackTreeSymbolTable < input.txt
 *  Dependencies: StdIn.java StdOut.java  
 *  Data files:   https://algs4.cs.princeton.edu/33balanced/tinyST.txt  
 *
 *  A symbol table implemented using a leftSubNode-leaning red-black BST.
 *  This is the 2-3 version.
 *
 *  This implementation implements only put, get, and contains.
 *  See RedBlackBST.java for a full implementation including delete.
 *
 *
 *  % more tinyST.txt
 *  S E A R C H E X A M P L E
 *
 *  % java RedBlackTreeSymbolTable < tinyST.txt
 *  A 8
 *  C 4
 *  E 12
 *  H 5
 *  L 11
 *  M 9
 *  P 10
 *  R 3
 *  S 0
 *  X 7
 *
 ******************************************************************************/

public class RedBlackTreeSymbolTable<Key extends Comparable<Key>, Value> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private Node rootNode;     // rootNode of the BST
    private int pairsAmount;         // number of key-value pairs in BST

    // BST helper node data type
    private class Node {
        private Key key;           // key
        private Value value;         // associated data
        private Node leftSubNode, rightSubNode;  // links to leftSubNode and rightSubNode subtrees
        private boolean color;     // 指向当前结点的链接的颜色 - color of parent link

        public Node(Key key, Value value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    /***************************************************************************
     *  Standard BST search.
     **************************************************************************
     * @param passedKey*/

    // return value associated with the given key, or null if no such key exists
    public Value get(Key passedKey) {
        return get(rootNode, passedKey);
    }

    public Value get(Node currentNode, Key passedKey) {
        while (currentNode != null) {
            int result = passedKey.compareTo(currentNode.key);
            if (result < 0) currentNode = currentNode.leftSubNode;
            else if (result > 0) currentNode = currentNode.rightSubNode;
            else return currentNode.value;
        }
        return null;
    }

    // is there a key-value pair in the symbol table with the given key?
    public boolean contains(Key key) {
        return get(key) != null;
    }


    /***************************************************************************
     *  Red-black tree insertion.
     **************************************************************************
     * @param passedKey
     * @param associatedValue*/

    public void put(Key passedKey, Value associatedValue) {
        // 查找key 找到则更新其值，否则为它创建一个节点
        rootNode = insert(rootNode, passedKey, associatedValue);
        rootNode.color = BLACK;
        assert check();
    }

    private Node insert(Node currentNode, Key passedKey, Value associatedValue) {
        // 标准的插入操作，和父节点之间使用红链接相连
        if (currentNode == null) {
            pairsAmount++; // 这里对树中的键值对个数进行了更新
            return new Node(passedKey, associatedValue, RED);
        }

        int result = passedKey.compareTo(currentNode.key);
        if (result < 0) currentNode.leftSubNode = insert(currentNode.leftSubNode, passedKey, associatedValue);
        else if (result > 0) currentNode.rightSubNode = insert(currentNode.rightSubNode, passedKey, associatedValue);
        else currentNode.value = associatedValue;

        // 原理：参考 recap_03 line31
        // 实现手段 - 在递归调用之后添加这些代码：在查找路径中的每一个结点（从下往上）上，经过局部变换(左旋转、右旋转、颜色变换) 来 维护“红黑树的平衡性”
        // 红黑树中插入新结点归约后的3中情形👇
        if (isRed(currentNode.rightSubNode) && !isRed(currentNode.leftSubNode)) // #1 右子结点为红色，而左子结点为黑色
            currentNode = rotateLeft(currentNode); // 对当前结点左旋转
        if (isRed(currentNode.leftSubNode) && isRed(currentNode.leftSubNode.leftSubNode)) // #2 左子结点为红色，左子结点的左子结点也为红色，
            currentNode = rotateRight(currentNode); // 对上层链接进行右旋转
        if (isRed(currentNode.leftSubNode) && isRed(currentNode.rightSubNode)) // #3 左子结点为红色，且右子结点也为红色
            flipColors(currentNode); // 进行颜色转换 来 #1 消除breach； #2 把红链接向上传递（维持与2-3树的等价性）

        return currentNode;
    }

    /***************************************************************************
     *  Red-black tree helper functions.
     **************************************************************************
     * @param currentNode*/

    // is node currentNode red (and non-null) ?
    private boolean isRed(Node currentNode) {
        if (currentNode == null) return false;
        return currentNode.color == RED;
    }

    // rotate rightSubNode
    private Node rotateRight(Node currentNode) {
        assert (currentNode != null) && isRed(currentNode.leftSubNode);
        Node replacerNode = currentNode.leftSubNode;
        currentNode.leftSubNode = replacerNode.rightSubNode;
        replacerNode.rightSubNode = currentNode;
        replacerNode.color = currentNode.color;
        currentNode.color = RED;
        return replacerNode;
    }

    // rotate leftSubNode
    private Node rotateLeft(Node currentNode) {
        assert (currentNode != null) && isRed(currentNode.rightSubNode);
        Node replacerNode = currentNode.rightSubNode;  // 获取右子树x
        currentNode.rightSubNode = replacerNode.leftSubNode; // 获取x的左子树，并绑定为当前节点的右子树
        replacerNode.leftSubNode = currentNode; // 把当前节点作为x的左子树
        replacerNode.color = currentNode.color; // 更新x的颜色（用当前节点的颜色）
        currentNode.color = RED; // 更新当前节点的颜色为红色
        return replacerNode; // 返回更新后的树的根节点
    }

    // precondition: two children are red, node is black
    // postcondition: two children are black, node is red
    private void flipColors(Node currentNode) {
        assert !isRed(currentNode) && isRed(currentNode.leftSubNode) && isRed(currentNode.rightSubNode);
        currentNode.color = RED;
        currentNode.leftSubNode.color = BLACK;
        currentNode.rightSubNode.color = BLACK;
    }


    /***************************************************************************
     *  Utility functions.
     ***************************************************************************/
    // return number of key-value pairs in symbol table
    public int size() {
        return pairsAmount;
    }

    // is the symbol table empty?
    public boolean isEmpty() {
        return pairsAmount == 0;
    }

    // height of tree (1-node tree has height 0)
    public int height() {
        return height(rootNode);
    }

    private int height(Node currentNode) {
        if (currentNode == null) return -1;
        return 1 + Math.max(height(currentNode.leftSubNode), height(currentNode.rightSubNode));
    }

    // return the smallest key; null if no such key
    public Key minKey() {
        return minKey(rootNode);
    }

    private Key minKey(Node currentNode) {
        Key key = null;
        while (currentNode != null) {
            key = currentNode.key;
            currentNode = currentNode.leftSubNode;
        }
        return key;
    }

    // return the largest key; null if no such key
    public Key maxKey() {
        return maxKey(rootNode);
    }

    private Key maxKey(Node currentNode) {
        Key key = null;
        while (currentNode != null) {
            key = currentNode.key;
            currentNode = currentNode.rightSubNode;
        }
        return key;
    }


    /***************************************************************************
     *  Iterate using an inorder traversal.
     *  Iterating through N elements takes O(N) time.
     ***************************************************************************/
    public Iterable<Key> keys() {
        Queue<Key> queue = new Queue<Key>();
        keys(rootNode, queue);
        return queue;
    }

    private void keys(Node currentNode, Queue<Key> keysQueue) {
        if (currentNode == null) return;
        keys(currentNode.leftSubNode, keysQueue);
        keysQueue.enqueue(currentNode.key);
        keys(currentNode.rightSubNode, keysQueue);
    }


    /***************************************************************************
     *  Check integrity of red-black tree data structure.
     ***************************************************************************/
    private boolean check() {
        if (!isBST()) StdOut.println("Not in symmetric order");
        if (!is23()) StdOut.println("Not a 2-3 tree");
        if (!isBalanced()) StdOut.println("Not balanced");
        return isBST() && is23() && isBalanced();
    }

    // does this binary tree satisfy symmetric order?
    // Note: this test also ensures that data structure is a binary tree since order is strict
    private boolean isBST() {
        return isBST(rootNode, null, null);
    }

    // is the tree rooted at x a BST with all keys strictly between min and max
    // (if min or max is null, treat as empty constraint)
    // Credit: Bob Dondero's elegant solution
    private boolean isBST(Node currentNode, Key minKey, Key maxKey) {
        if (currentNode == null) return true;
        if (minKey != null && currentNode.key.compareTo(minKey) <= 0) return false;
        if (maxKey != null && currentNode.key.compareTo(maxKey) >= 0) return false;
        return isBST(currentNode.leftSubNode, minKey, currentNode.key) && isBST(currentNode.rightSubNode, currentNode.key, maxKey);
    }

    // Does the tree have no red rightSubNode links, and at most one (leftSubNode)
    // red links in a row on any path?
    private boolean is23() {
        return is23(rootNode);
    }

    private boolean is23(Node currentNode) {
        if (currentNode == null) return true;
        if (isRed(currentNode.rightSubNode)) return false;
        if (currentNode != rootNode && isRed(currentNode) && isRed(currentNode.leftSubNode))
            return false;
        return is23(currentNode.leftSubNode) && is23(currentNode.rightSubNode);
    }

    // do all paths from rootNode to leaf have same number of black edges?
    private boolean isBalanced() {
        int blackLinkAmount = 0;     // number of black links on path from rootNode to min
        Node currentNode = rootNode;
        while (currentNode != null) {
            if (!isRed(currentNode)) blackLinkAmount++;
            currentNode = currentNode.leftSubNode;
        }
        return isBalanced(rootNode, blackLinkAmount);
    }

    // does every path from the rootNode to a leaf have the given number of black links?
    private boolean isBalanced(Node currentNode, int blackLinkAmount) {
        if (currentNode == null) return blackLinkAmount == 0;
        if (!isRed(currentNode)) blackLinkAmount--;
        return isBalanced(currentNode.leftSubNode, blackLinkAmount) && isBalanced(currentNode.rightSubNode, blackLinkAmount);
    }


    /***************************************************************************
     *  Test client. TODO: understand this unit test.
     ***************************************************************************/
    public static void main(String[] args) {

        String test = "S E A R C H E X A M P L E";
        String[] keys = test.split(" ");
        RedBlackTreeSymbolTable<String, Integer> st = new RedBlackTreeSymbolTable<String, Integer>();
        for (int i = 0; i < keys.length; i++)
            st.put(keys[i], i);

        StdOut.println("size = " + st.size());
        StdOut.println("min  = " + st.minKey());
        StdOut.println("max  = " + st.maxKey());
        StdOut.println();


        // print keys in order using allKeys()
        StdOut.println("Testing keys()");
        StdOut.println("--------------------------------");
        for (String s : st.keys())
            StdOut.println(s + " " + st.get(s));
        StdOut.println();

        // insert N elements in order if one command-line argument supplied
//        if (args.length == 0) return;
//        int pairsAmount = Integer.parseInt(args[0]);
//        RedBlackTreeSymbolTable<Integer, Integer> st2 = new RedBlackTreeSymbolTable<Integer, Integer>();
//        for (int i = 0; i < pairsAmount; i++) {
//            st2.put(i, i);
//            int toMoveStepsToEndGridWithoutObstacles = st2.height();
//            StdOut.println("i = " + i + ", height = " + toMoveStepsToEndGridWithoutObstacles + ", size = " + st2.size());
//        }
//
//
//        StdOut.println("size = " + st2.size());
    }
}