package com.henry.symbol_table_chapter_03.implementation_02.advanced.via_balanced_search_tree.to_implement_23tree_neatly_02.when_insert;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

/******************************************************************************
 *  Compilation:  javac RedBlackTreeLiteSymbolTable.java
 *  Execution:    java RedBlackTreeLiteSymbolTable < input.txt
 *  Dependencies: StdIn.java StdOut.java  
 *  Data files:   https://algs4.cs.princeton.edu/33balanced/tinyST.txt  
 *
 *  A symbol table implemented using a leftSubNode-leaning red-black BST.
 *  This is the 2-3 version.
 *
 *  This implementation implements only put, get, and contains.
 *  See RedBlackTreeSymbolTable.java for a full implementation including delete.
 *
 *
 *  % more tinyST.txt
 *  S E A R C H E X A M P L E
 *
 *  % java RedBlackTreeLiteSymbolTable < tinyST.txt
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

public class RedBlackTreeLiteSymbolTable<Key extends Comparable<Key>, Value> {

    private static final boolean RED = true;
    private static final boolean BLACK = false; // 表示结点颜色的常量

    private Node rootNode;     // 二叉查找树中的根结点
    private int pairsAmount;         // 二叉查找树中的键值对数量

    // BST helper node data type
    private class Node {
        private Key key;           // 结点中的键
        private Value value;         // 结点中 键所关联的值
        private Node leftSubNode, rightSubNode;  // 当前结点的左子结点、右子结点
        private boolean color;     // 结点的颜色（指向当前结点的链接的颜色）

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

    // 返回传入的键 所关联的值, 如果传入的键不存在的话，则：返回null
    public Value get(Key passedKey) {
        return get(rootNode, passedKey);
    }

    public Value get(Node currentNode, Key passedKey) {
        while (currentNode != null) {
            // 比较 传入的键 与 根结点中的键
            int result = passedKey.compareTo(currentNode.key);
            // 如果更小，则：使用左子结点 来 更新当前节点
            if (result < 0) currentNode = currentNode.leftSubNode;
            // 如果更大，则：使用右子结点 来 更新当前节点
            else if (result > 0) currentNode = currentNode.rightSubNode;
            // 如果相等，则：返回根结点中 的值
            else return currentNode.value;
        }

        // 循环如果结束，说明查找结束于空结点 aka BST中没有找到传入的键。这种情况下返回null
        return null;
    }

    // 在符号表中 是否存在 与传入的key相等的键（&值）
    public boolean contains(Key passedKey) {
        return get(passedKey) != null;
    }


    /***************************************************************************
     *  红黑树的插入算法.
     **************************************************************************
     * @param passedKey
     * @param associatedValue*/

    public void put(Key passedKey, Value associatedValue) {
        // 查找key 找到则更新其值，否则为它创建一个节点
        rootNode = insert(rootNode, passedKey, associatedValue);

        // 约定：红黑树中根结点总是黑色的 - 为了遵守 红色左链接 <-> 3-结点的约定
        rootNode.color = BLACK;
        assert check();
    }

    private Node insert(Node currentNode, Key passedKey, Value associatedValue) {
        if (currentNode == null) { // 如果查找操作结束于一个空结点 说明BST中不存在 与passedKey相等的键(&值)，则...
            pairsAmount++;
            // 将传入的键值作为新结点添加到树的底部
            // 🐖 插入新结点时，使用红链接 将之和父节点之间相连
            return new Node(passedKey, associatedValue, RED); // 插入的结点总是红色的
        }

        // 为了保证“对称有序性”，按照与根结点的比较结果，在对应的子树中递归地插入结点
        int result = passedKey.compareTo(currentNode.key);
        if (result < 0)
            currentNode.leftSubNode = insert(currentNode.leftSubNode, passedKey, associatedValue); // 在左子树中插入
        else if (result > 0)
            currentNode.rightSubNode = insert(currentNode.rightSubNode, passedKey, associatedValue); // 在右子树中插入
        else
            currentNode.value = associatedValue; // 更新结点的value

        // 插入结点后，维护“合法的红黑树(黑链接平衡&&红链接约束)”     原理：参考 implement_insertion_code_wise_04
        // 手段：树中的局部变换 {左旋转、右旋转、颜色翻转}
        // 具体实现：插入结点后，在查找路径中的每一个结点（从下往上）上，根据需要来进行适当的局部变换
        // 🐖 红黑树中插入新结点是，5中具体情形(2-结点的插入&3-结点的插入)归约后得到如下3种情形👇
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
        /* 左旋转的操作统共需要5步来完成 👇 */
        /* 结构上的变更 */
        Node replacerNode = currentNode.rightSubNode;  // #1 获取当前结点的右子结点，作为“替换结点”
        currentNode.rightSubNode = replacerNode.leftSubNode; // #2 获取“替换结点”的左子树，并绑定为当前节点的右子树
        replacerNode.leftSubNode = currentNode; // #3 把“当前节点”绑定为“替换节点”的左子树

        /* 颜色上的变更 */
        replacerNode.color = currentNode.color; // #4 更新“替换节点”的颜色 为 当前节点的颜色
        currentNode.color = RED; // #5 更新“当前节点的颜色（指向当前结点的链接的颜色）”为红色 - 合法的红色左链接

        return replacerNode; // 返回更新后的树的根节点
    }

    // 前提条件： 两个子节点是红色的，父结点是黑色的
    // 后置条件：方法执行后，两个子节点是黑色的，父结点是红色的
    private void flipColors(Node currentNode) {
        // 防御性编程？ 先断言前置条件成立
        assert !isRed(currentNode) && isRed(currentNode.leftSubNode) && isRed(currentNode.rightSubNode);

        // 手动改变到预期的状态
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
     *  Test client.
     ***************************************************************************/
    public static void main(String[] args) {

        String letterSequence = "S E A R C H E X A M P L E";
        String[] letterArray = letterSequence.split(" ");

        RedBlackTreeLiteSymbolTable<String, Integer> letterToSpotSymbolTable
                = new RedBlackTreeLiteSymbolTable<String, Integer>();

        for (int spot = 0; spot < letterArray.length; spot++)
            letterToSpotSymbolTable.put(letterArray[spot], spot);

        StdOut.println("size = " + letterToSpotSymbolTable.size());
        StdOut.println("min  = " + letterToSpotSymbolTable.minKey());
        StdOut.println("max  = " + letterToSpotSymbolTable.maxKey());
        StdOut.println();

        // print letterArray in order using allKeys()
        StdOut.println("Testing keys() API");
        StdOut.println("--------------------------------");
        for (String letter : letterToSpotSymbolTable.keys())
            StdOut.println(letter + " " + letterToSpotSymbolTable.get(letter));
        StdOut.println();

        // 如果提供了一个命令行参数，则：按照顺序插入N个元素
        if (args.length == 0) return;
        int pairsAmount = Integer.parseInt(args[0]);
        RedBlackTreeLiteSymbolTable<Integer, Integer> symbolTable
                = new RedBlackTreeLiteSymbolTable<Integer, Integer>();

        for (int currentSpot = 0; currentSpot < pairsAmount; currentSpot++) {
            symbolTable.put(currentSpot, currentSpot);
            int redBlackTreesHeight = symbolTable.height();
            StdOut.println("currentSpot = " + currentSpot + ", height = " + redBlackTreesHeight + ", size = " + symbolTable.size());
        }

        StdOut.println("size = " + symbolTable.size());
    }
}