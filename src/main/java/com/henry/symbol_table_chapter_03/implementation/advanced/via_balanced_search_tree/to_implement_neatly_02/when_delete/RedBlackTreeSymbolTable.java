package com.henry.symbol_table_chapter_03.implementation.advanced.via_balanced_search_tree.to_implement_neatly_02.when_delete; /******************************************************************************
 *  Compilation:  javac RedBlackTreeSymbolTable.java
 *  Execution:    java RedBlackTreeSymbolTable < input.txt
 *  Dependencies: StdIn.java StdOut.java  
 *  Data files:   https://algs4.cs.princeton.edu/33balanced/tinyST.txt  
 *
 *  A symbol table implemented using a leftSubNode-leaning red-black BST.
 *  This is the 2-3 version.
 *
 *  Note: commented out assertions because DrJava now enables assertions
 *        by default.
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

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

/**
 * The {@code BST} class represents an ordered symbol table of generic
 * key-value pairs.
 * It supports the usual <em>put</em>, <em>get</em>, <em>contains</em>,
 * <em>delete</em>, <em>itsNodesAmount</em>, and <em>is-empty</em> methods.
 * It also provides ordered methods for finding the <em>minimum</em>,
 * <em>maximum</em>, <em>floor</em>, and <em>ceiling</em>.
 * It also provides a <em>keys</em> method for iterating over all of the keys.
 * A symbol table implements the <em>associative array</em> abstraction:
 * when associating a value with a key that is already in the symbol table,
 * the convention is to replace the old value with the new value.
 * Unlike {@link java.util.Map}, this class uses the convention that
 * values cannot be {@code null}—setting the
 * value associated with a key to {@code null} is equivalent to deleting the key
 * from the symbol table.
 * <p>
 * It requires that
 * the key type implements the {@code Comparable} interface and calls the
 * {@code compareTo()} and method to compare two keys. It does not call either
 * {@code equals()} or {@code hashCode()}.
 * <p>
 * This implementation uses a <em>leftSubNode-leaning red-black BST</em>.
 * The <em>put</em>, <em>get</em>, <em>contains</em>, <em>remove</em>,
 * <em>minimum</em>, <em>maximum</em>, <em>ceiling</em>, <em>floor</em>,
 * <em>rank</em>, and <em>select</em> operations each take
 * &Theta;(log <em>n</em>) time in the worst case, where <em>n</em> is the
 * number of key-value pairs in the symbol table.
 * The <em>itsNodesAmount</em>, and <em>is-empty</em> operations take &Theta;(1) time.
 * The <em>keys</em> methods take
 * <em>O</em>(log <em>n</em> + <em>m</em>) time, where <em>m</em> is
 * the number of keys returned by the iterator.
 * Construction takes &Theta;(1) time.
 * <p>
 * For alternative implementations of the symbol table API, see {@link ST},
 * {@link BinarySearchST}, {@link SequentialSearchST}, {@link BST},
 * {@link SeparateChainingHashST}, {@link LinearProbingHashST}, and
 * {@link AVLTreeST}.
 * For additional documentation, see
 * <a href="https://algs4.cs.princeton.edu/33balanced">Section 3.3</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */

public class RedBlackTreeSymbolTable<Key extends Comparable<Key>, Value> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private Node rootNode;     // rootNode of the BST

    // BST helper node data type
    private class Node {
        private Key key;           // key
        private Value value;         // associated data
        private Node leftSubNode, rightSubNode;  // links to leftSubNode and rightSubNode subtrees
        private boolean color;     // color of parent link
        private int itsNodesAmount;          // subtree count

        public Node(Key key, Value value, boolean color, int itsNodesAmount) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.itsNodesAmount = itsNodesAmount;
        }
    }

    /**
     * Initializes an empty symbol table.
     */
    public RedBlackTreeSymbolTable() {
    }

    /***************************************************************************
     *  Node helper methods.
     **************************************************************************
     * @param currentNode*/
    // is node x red; false if x is null ?
    private boolean isRed(Node currentNode) {
        if (currentNode == null) return false;
        return currentNode.color == RED;
    }

    // number of node in subtree rooted at x; 0 if x is null
    private int size(Node currentNode) {
        if (currentNode == null) return 0;
        return currentNode.itsNodesAmount;
    }


    /**
     * Returns the number of key-value pairs in this symbol table.
     *
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return size(rootNode);
    }

    /**
     * Is this symbol table empty?
     *
     * @return {@code true} if this symbol table is empty and {@code false} otherwise
     */
    public boolean isEmpty() {
        return rootNode == null;
    }


    /***************************************************************************
     *  Standard BST search.
     ***************************************************************************/

    /**
     * Returns the value associated with the given key.
     *
     * @param passedKey the key
     * @return the value associated with the given key if the key is in the symbol table
     * and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Value get(Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to get() is null");
        return get(rootNode, passedKey);
    }

    // value associated with the given key in subtree rooted at x; null if no such key
    private Value get(Node currentNode, Key passedKey) {
        while (currentNode != null) {
            int result = passedKey.compareTo(currentNode.key);
            if (result < 0) currentNode = currentNode.leftSubNode;
            else if (result > 0) currentNode = currentNode.rightSubNode;
            else return currentNode.value;
        }
        return null;
    }

    /**
     * Does this symbol table contain the given key?
     *
     * @param passedKey the key
     * @return {@code true} if this symbol table contains {@code key} and
     * {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(Key passedKey) {
        return get(passedKey) != null;
    }

    /***************************************************************************
     *  Red-black tree insertion.
     ***************************************************************************/

    /**
     * Inserts the specified key-value pair into the symbol table, overwriting the old
     * value with the new value if the symbol table already contains the specified key.
     * Deletes the specified key (and its associated value) from this symbol table
     * if the specified value is {@code null}.
     *
     * @param passedKey       the key
     * @param associatedValue the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(Key passedKey, Value associatedValue) {
        if (passedKey == null) throw new IllegalArgumentException("first argument to put() is null");
        if (associatedValue == null) {
            delete(passedKey);
            return;
        }

        rootNode = put(rootNode, passedKey, associatedValue);
        rootNode.color = BLACK;
        // assert check();
    }

    // insert the key-value pair in the subtree rooted at toMoveStepsToEndGridWithoutObstacles
    private Node put(Node currentNode, Key passedKey, Value associatedValue) {
        if (currentNode == null) return new Node(passedKey, associatedValue, RED, 1);

        int cmp = passedKey.compareTo(currentNode.key);
        if (cmp < 0) currentNode.leftSubNode = put(currentNode.leftSubNode, passedKey, associatedValue);
        else if (cmp > 0) currentNode.rightSubNode = put(currentNode.rightSubNode, passedKey, associatedValue);
        else currentNode.value = associatedValue;

        // fix-up any rightSubNode-leaning links
        if (isRed(currentNode.rightSubNode) && !isRed(currentNode.leftSubNode)) currentNode = rotateLeft(currentNode);
        if (isRed(currentNode.leftSubNode) && isRed(currentNode.leftSubNode.leftSubNode)) currentNode = rotateRight(currentNode);
        if (isRed(currentNode.leftSubNode) && isRed(currentNode.rightSubNode)) flipColors(currentNode);
        currentNode.itsNodesAmount = size(currentNode.leftSubNode) + size(currentNode.rightSubNode) + 1;

        return currentNode;
    }

    /***************************************************************************
     *  Red-black tree deletion.
     ***************************************************************************/

    /**
     * Removes the smallest key and associated value from the symbol table.
     *
     * @throws NoSuchElementException if the symbol table is empty
     *                                通过保持与文本中给出的转换的对应关系来实现RedBlackBST.java的deleteMin（）操作；
     *                                作用：1 以使树的左脊向下移动；
     *                                2 同时保持不变————即当前节点不是2节点。
     *                                Implement the deleteMin() operation for RedBlackTreeSymbolTable.java
     *                                by maintaining the correspondence with the transformations given in the text
     *                                for moving down the leftSubNode spine of the tree
     *                                while maintaining the invariant that the current node is not a 2-node.
     */
    public void deleteMin() {
        if (isEmpty()) throw new NoSuchElementException("BST underflow");

        // if both children of rootNode are black, set rootNode to red
        // 如果根节点的两个子节点都是黑色，就设置根节点的颜色为红色
        if (!isRed(rootNode.leftSubNode) && !isRed(rootNode.rightSubNode))
            rootNode.color = RED;

        // 把“删除最小节点后的树” 重新绑定给 root
        rootNode = deleteMin(rootNode);

        // 把根结点的颜色强制设置为黑色
        if (!isEmpty()) rootNode.color = BLACK;
        // assert check();
    }

    // 在h树中删除最小键的key-value对
    // delete the key-value pair with the minimum key rooted at toMoveStepsToEndGridWithoutObstacles

    /**
     * 通过保持与文本描述中给出的转换的对应关系来实现RedBlackBST.java的deleteMin（）操作，以使树的左脊向下移动，同时保持不变，即当前节点不是2节点。
     * 递归方法作用：从树中删除最小键的键值对，并返回更新后的树
     *
     * @param currentNode
     * @return
     */
    private Node deleteMin(Node currentNode) {
        // 查找最小键的过程会递归地沿着当前树的左链接进行，最终会遇到一个子链接为null的叶子结点👇
        // todo why null?
        /*
            这与2-3树的生成方式有关 - 当有两个键值对时，就会构造出一个3-节点？？？
            把一棵 “仅由一个3-节点组成的2-3树” 转化为一个红黑树（根节点 + 左子节点）
            因此，如果根节点的左子节点为null，说明当前2-3树中就只有一个节点，则：这个节点就是最小节点
            删除之后，就只有一棵空树了 所以return null
         */
        if (currentNode.leftSubNode == null)
            return null;

        // Ⅰ 递归前，沿着左链接对当前节点进行处理————确保当前节点不是一个2-节点（这样就不会从2-节点中删除键值对）
        // 如果左子树不是红色的（左子结点是一个2-结点） & 左子树的左子树也不是红色的（左子结点的左子结点也是一个2-结点）
        // 则：对当前结点执行 moveRedLeft()
        if (!isRed(currentNode.leftSubNode) && !isRed(currentNode.leftSubNode.leftSubNode))
            currentNode = moveRedLeft(currentNode);

        // Ⅱ 执行删除操作，并把 “删除了最小节点后的左子树” 重新绑定到“当前结点”上
        // 🐖 经过Ⅰ的调整后，我们可以确保 删除动作发生在一个 不是2-结点的结点中
        currentNode.leftSubNode = deleteMin(currentNode.leftSubNode);

        // Ⅲ 回溯时，对树进行平衡操作
        return balance(currentNode);
    }


    /**
     * Removes the largest key and associated value from the symbol table.
     *
     * @throws NoSuchElementException if the symbol table is empty
     *                                <p>
     *                                Implement the deleteMax() operation for RedBlackTreeSymbolTable.java.
     *                                Note that the transformations involved
     *                                differ slightly from those in the previous exercise
     *                                because red links are leftSubNode-leaning.
     *                                请注意，涉及的转换与上一个练习中的转换略有不同，因为红色链接向左倾斜。
     */
    public void deleteMax() {
        if (isEmpty()) throw new NoSuchElementException("BST underflow");

        // if both children of rootNode are black, set rootNode to red
        if (!isRed(rootNode.leftSubNode) && !isRed(rootNode.rightSubNode))
            rootNode.color = RED;

        rootNode = deleteMax(rootNode);
        if (!isEmpty()) rootNode.color = BLACK;
        // assert check();
    }

    // delete the key-value pair with the maximum key rooted at toMoveStepsToEndGridWithoutObstacles
    private Node deleteMax(Node h) {
        // Ⅰ 递归调用之前做一些事情    作用：避免当前节点是一个2-节点
        if (isRed(h.leftSubNode)) // 1 左子树为红节点   这说明右子树是一个黑节点（2-节点）  这不是预期的，所以需要更新当前树来避免查找路径上的2-节点
            // 作用：更新当前节点    手段：使一个左倾的链接转向右倾
            // 更新后，在查找路径上得到一个3-节点
            h = rotateRight(h);

        if (h.rightSubNode == null) // 2 右子树为空     这说明树中就只有一个节点
            // 作用：？？？   手段：返回null       如果右子树为空，则根据性质5 左子树中只能有两种情况：1 左子树为空 2 左子树中只包含红节点（这种情况已经在第一个if()中处理，它会得到一个包含右节点的树 所以2不存在）
            // 当右子树为null时，说明树中就只有一个节点
            return null;

        if (!isRed(h.rightSubNode) && !isRed(h.rightSubNode.leftSubNode)) // 右子树为黑节点&右子树的左子树为黑节点     判断查找路径上的当前节点是否为2-节点 这里的h.rightSubNode.left是因为红链接是左倾的
            // 更新当前节点   作用：避免当前节点（的右子节点）是一个2-节点
            h = moveRedRight(h);

        h.rightSubNode = deleteMax(h.rightSubNode);

        return balance(h);
    }

    /**
     * Removes the specified key and its associated value from this symbol table
     * (if the key is in this symbol table).
     *
     * @param passedKey the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void delete(Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to delete() is null");
        if (!contains(passedKey)) return;

        // if both children of rootNode are black, set rootNode to red
        // 如果root节点的两个子节点都是黑色，就设置根节点为红色
        if (!isRed(rootNode.leftSubNode) && !isRed(rootNode.rightSubNode))
            rootNode.color = RED;

        rootNode = delete(rootNode, passedKey);
        if (!isEmpty()) rootNode.color = BLACK;
        // assert check();
    }

    // delete the key-value pair with the given key rooted at toMoveStepsToEndGridWithoutObstacles
    // 为RedBlackBST.java实现delete（）操作，将前两个练习的方法与BST的delete（）操作结合起来。
    // 如果是删除随机的节点，如何能确定查找路径呢？
    private Node delete(Node currentNode, Key passedKey) {
        // assert get(toMoveStepsToEndGridWithoutObstacles, key) != null;

        if (passedKey.compareTo(currentNode.key) < 0) { // 预期删除的节点在左子树中
            // 更新当前节点 确保它不是2-节点
            if (!isRed(currentNode.leftSubNode) && !isRed(currentNode.leftSubNode.leftSubNode)) // 如何判断一个节点是不是2-节点呢？
                currentNode = moveRedLeft(currentNode);
            // 从左子树中删除预期节点
            currentNode.leftSubNode = delete(currentNode.leftSubNode, passedKey);
        } else { // 预期删除的节点在右子树中  任务：避免当前节点是一个2-节点
            // condition1 左节点为红色
            if (isRed(currentNode.leftSubNode))
                currentNode = rotateRight(currentNode); // 把左子节点作为新的根节点，旧的根节点会进入到右子树中

            // condition2 根节点即为预期删除的节点，并且右节点为空
            if (passedKey.compareTo(currentNode.key) == 0 && (currentNode.rightSubNode == null))
                return null; // 为什么返回空？ can not make sense

            // condition3 右子节点为2-节点 并且右子节点的左子节点也是2-节点
            if (!isRed(currentNode.rightSubNode) && !isRed(currentNode.rightSubNode.leftSubNode)) // this seems like the former condition in deleteMin()
                // 执行相应操作，避免当前节点为2-节点
                currentNode = moveRedRight(currentNode);

            // condition4 如果根节点刚好是预期删除的节点
            if (passedKey.compareTo(currentNode.key) == 0) { // this is like delete in BST
                // 先覆盖预期删除的节点 使用它的successor
                Node successorNode = minKey(currentNode.rightSubNode);
                currentNode.key = successorNode.key;
                currentNode.value = successorNode.value;
                // toMoveStepsToEndGridWithoutObstacles.value = get(toMoveStepsToEndGridWithoutObstacles.rightSubNode, min(toMoveStepsToEndGridWithoutObstacles.rightSubNode).key);
                // toMoveStepsToEndGridWithoutObstacles.key = min(toMoveStepsToEndGridWithoutObstacles.rightSubNode).key;
                currentNode.rightSubNode = deleteMin(currentNode.rightSubNode); // 再使用删除节点后的树更新右子树
            }

            // 如果不是上面的任何一种情况 直接更新右子树 THIS is make no sense too. why there is so many if?
            else currentNode.rightSubNode = delete(currentNode.rightSubNode, passedKey);
        }
        return balance(currentNode);
    }

    /***************************************************************************
     *  Red-black tree helper functions.
     **************************************************************************
     * @param currentNode
     * @return*/

    // make a leftSubNode-leaning link lean to the rightSubNode
    // 使一个左倾的链接变成右倾 - 右旋转左链接
    private Node rotateRight(Node currentNode) {
        assert (currentNode != null) && isRed(currentNode.leftSubNode);

        // #1 结构上的变更
        Node replacerNode = currentNode.leftSubNode; // 找到替换结点
        currentNode.leftSubNode = replacerNode.rightSubNode; // 用替换结点的右子树 来 作为当前结点的左子树
        replacerNode.rightSubNode = currentNode; // 使用当前节点 来 作为替换节点的右子树

        // #2 颜色上的变更
        replacerNode.color = currentNode.color;
        currentNode.color = RED; // 旋转后，得到的仍旧是一个红链接

        // #3 子树中结点数量的变更
        replacerNode.itsNodesAmount = currentNode.itsNodesAmount; // “替换结点”子树中的结点数量 与 “当前结点”中的结点数量相同
        currentNode.itsNodesAmount = size(currentNode.leftSubNode) + size(currentNode.rightSubNode) + 1;

        return replacerNode;
    }

    // 使一个右倾的链接变成左倾
    private Node rotateLeft(Node currentNode) {
        assert (currentNode != null) && isRed(currentNode.rightSubNode);

        // #1 结构上的变更：当前节点(的右子树) & 替换节点（的左子树）
        Node replacerNode = currentNode.rightSubNode;
        currentNode.rightSubNode = replacerNode.leftSubNode;
        replacerNode.leftSubNode = currentNode;

        // #2 颜色上的变更：替换结点（更新为当前结点的颜色） & 当前结点（更新为红色）
        replacerNode.color = currentNode.color;
        currentNode.color = RED;

        // #3 子树中的结点数量的变更
        replacerNode.itsNodesAmount = currentNode.itsNodesAmount;
        currentNode.itsNodesAmount = size(currentNode.leftSubNode) + size(currentNode.rightSubNode) + 1;

        return replacerNode;
    }

    // 反转当前结点&它的左右子节点的颜色
    private void flipColors(Node currentNode) {
        // 把当前结点 & 它的左右子节点 的颜色变更为 “与当前颜色不同的另一种颜色”
        currentNode.color = !currentNode.color;
        currentNode.leftSubNode.color = !currentNode.leftSubNode.color;
        currentNode.rightSubNode.color = !currentNode.rightSubNode.color;
    }

    // todo 这部分的代码和过程书上讲得非常不清楚，只靠自己没办法add them up. 明天用公司的网络在youtube上搜一下“红黑树的删除最小键的算法”
    // 假设 当前结点是红色的（3-结点的一部分）& 当前节点的左节点、当前节点左节点的左节点都是黑色的（必然有一个是2-结点），则：
    // #1 要么把 “当前节点的左节点”变红（成为一个3-结点 但是破坏了红黑树约束）
    // #2 要么 把“当前节点的左节点”的任意一个子节点 变红（成为3-结点的一部分）
    private Node moveRedLeft(Node currentNode) {
        flipColors(currentNode);

        if (isRed(currentNode.rightSubNode.leftSubNode)) {
            currentNode.rightSubNode = rotateRight(currentNode.rightSubNode);
            currentNode = rotateLeft(currentNode);
            flipColors(currentNode);
        }

        return currentNode;
    }

    // 假设 当前结点是红节点，而 它的右子结点 与 右子结点的左子结点 都是黑节点（2-结点）,则：
    // 把 当前结点的右子结点 或者 右子结点的某个子节点 变成红色
    private Node moveRedRight(Node currentNode) {
        // Ⅰ 翻转当前结点的链接颜色
        flipColors(currentNode);

        // Ⅱ 如果 “当前节点的左子节点”的左子节点为红色，则：
        if (isRed(currentNode.leftSubNode.leftSubNode)) {
            currentNode = rotateRight(currentNode);
            flipColors(currentNode);
        }

        return currentNode;
    }

    // restore red-black tree invariant
    // 恢复红黑树的不变性 这是一套固定流程
    private Node balance(Node currentNode) {
        // assert (toMoveStepsToEndGridWithoutObstacles != null);

        // 需要颜色平衡的三种情况
        if (isRed(currentNode.rightSubNode)) currentNode = rotateLeft(currentNode); // 红色右链接
        if (isRed(currentNode.leftSubNode) && isRed(currentNode.leftSubNode.leftSubNode))
            currentNode = rotateRight(currentNode); // 两个连续的红色左链接
        if (isRed(currentNode.leftSubNode) && isRed(currentNode.rightSubNode)) flipColors(currentNode); // 同一个节点连接左右红链接

        currentNode.itsNodesAmount = size(currentNode.leftSubNode) + size(currentNode.rightSubNode) + 1;
        return currentNode;
    }


    /***************************************************************************
     *  Utility functions.
     ***************************************************************************/

    /**
     * Returns the height of the BST (for debugging).
     *
     * @return the height of the BST (a 1-node tree has height 0)
     */
    public int height() {
        return height(rootNode);
    }

    private int height(Node currentNode) {
        if (currentNode == null) return -1;
        return 1 + Math.max(height(currentNode.leftSubNode), height(currentNode.rightSubNode));
    }

    /***************************************************************************
     *  Ordered symbol table methods.
     ***************************************************************************/

    /**
     * Returns the smallest key in the symbol table.
     *
     * @return the smallest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
    public Key minKey() {
        if (isEmpty()) throw new NoSuchElementException("calls min() with empty symbol table");
        return minKey(rootNode).key;
    }

    // the smallest key in subtree rooted at x; null if no such key
    private Node minKey(Node currentNode) {
        // assert x != null;
        if (currentNode.leftSubNode == null) return currentNode;
        else return minKey(currentNode.leftSubNode);
    }

    /**
     * Returns the largest key in the symbol table.
     *
     * @return the largest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
    public Key maxKey() {
        if (isEmpty()) throw new NoSuchElementException("calls max() with empty symbol table");
        return maxKey(rootNode).key;
    }

    // the largest key in the subtree rooted at x; null if no such key
    private Node maxKey(Node currentNode) {
        // assert x != null;
        if (currentNode.rightSubNode == null) return currentNode;
        else return maxKey(currentNode.rightSubNode);
    }


    /**
     * Returns the largest key in the symbol table less than or equal to {@code key}.
     *
     * @param passedKey the key
     * @return the largest key in the symbol table less than or equal to {@code key}
     * @throws NoSuchElementException   if there is no such key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Key floor(Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to floor() is null");
        if (isEmpty()) throw new NoSuchElementException("calls floor() with empty symbol table");
        Node flooredNode = floor(rootNode, passedKey);
        if (flooredNode == null) throw new NoSuchElementException("argument to floor() is too small");
        else return flooredNode.key;
    }

    // the largest key in the subtree rooted at x less than or equal to the given key
    private Node floor(Node currentNode, Key passedKey) {
        if (currentNode == null) return null;
        int result = passedKey.compareTo(currentNode.key);
        if (result == 0) return currentNode;
        if (result < 0) return floor(currentNode.leftSubNode, passedKey);
        Node flooredNode = floor(currentNode.rightSubNode, passedKey);
        if (flooredNode != null) return flooredNode;
        else return currentNode;
    }

    /**
     * Returns the smallest key in the symbol table greater than or equal to {@code key}.
     *
     * @param passedKey the key
     * @return the smallest key in the symbol table greater than or equal to {@code key}
     * @throws NoSuchElementException   if there is no such key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Key ceiling(Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to ceiling() is null");
        if (isEmpty()) throw new NoSuchElementException("calls ceiling() with empty symbol table");
        Node ceiledNode = ceiling(rootNode, passedKey);
        if (ceiledNode == null) throw new NoSuchElementException("argument to ceiling() is too small");
        else return ceiledNode.key;
    }

    // the smallest key in the subtree rooted at x greater than or equal to the given key
    private Node ceiling(Node currentNode, Key passedKey) {
        if (currentNode == null) return null;
        int result = passedKey.compareTo(currentNode.key);
        if (result == 0) return currentNode;
        if (result > 0) return ceiling(currentNode.rightSubNode, passedKey);

        Node ceiledNode = ceiling(currentNode.leftSubNode, passedKey);
        if (ceiledNode != null) return ceiledNode;
        else return currentNode;
    }

    /**
     * Return the key in the symbol table of a given {@code rank}.
     * This key has the property that there are {@code rank} keys in
     * the symbol table that are smaller. In other words, this key is the
     * ({@code rank}+1)st smallest key in the symbol table.
     *
     * @param passedRanking the order statistic
     * @return the key in the symbol table of given {@code rank}
     * @throws IllegalArgumentException unless {@code rank} is between 0 and
     *                                  <em>n</em>–1
     */
    public Key select(int passedRanking) {
        if (passedRanking < 0 || passedRanking >= size()) {
            throw new IllegalArgumentException("argument to select() is invalid: " + passedRanking);
        }
        return select(rootNode, passedRanking);
    }

    // Return key in BST rooted at x of given rank.
    // Precondition: rank is in legal range.
    private Key select(Node currentNode, int passedRanking) {
        if (currentNode == null) return null;
        int leftSize = size(currentNode.leftSubNode);
        if (leftSize > passedRanking) return select(currentNode.leftSubNode, passedRanking);
        else if (leftSize < passedRanking) return select(currentNode.rightSubNode, passedRanking - leftSize - 1);
        else return currentNode.key;
    }

    /**
     * Return the number of keys in the symbol table strictly less than {@code key}.
     *
     * @param passedKey the key
     * @return the number of keys in the symbol table strictly less than {@code key}
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public int rankingOf(Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to rank() is null");
        return rankingOf(passedKey, rootNode);
    }

    // number of keys less than key in the subtree rooted at x
    private int rankingOf(Key passedKey, Node currentNode) {
        if (currentNode == null) return 0;
        int result = passedKey.compareTo(currentNode.key);
        if (result < 0) return rankingOf(passedKey, currentNode.leftSubNode);
        else if (result > 0) return 1 + size(currentNode.leftSubNode) + rankingOf(passedKey, currentNode.rightSubNode);
        else return size(currentNode.leftSubNode);
    }

    /***************************************************************************
     *  Range count and range search.
     ***************************************************************************/

    /**
     * Returns all keys in the symbol table as an {@code Iterable}.
     * To iterate over all of the keys in the symbol table named {@code st},
     * use the foreach notation: {@code for (Key key : st.keys())}.
     *
     * @return all keys in the symbol table as an {@code Iterable}
     */
    public Iterable<Key> keys() {
        if (isEmpty()) return new Queue<Key>();
        return keys(minKey(), maxKey());
    }

    /**
     * Returns all keys in the symbol table in the given range,
     * as an {@code Iterable}.
     *
     * @param leftBarKey  minimum endpoint
     * @param rightBarKey maximum endpoint
     * @return all keys in the symbol table between {@code lo}
     * (inclusive) and {@code hi} (inclusive) as an {@code Iterable}
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *                                  is {@code null}
     */
    public Iterable<Key> keys(Key leftBarKey, Key rightBarKey) {
        if (leftBarKey == null) throw new IllegalArgumentException("first argument to keys() is null");
        if (rightBarKey == null) throw new IllegalArgumentException("second argument to keys() is null");

        Queue<Key> keysQueue = new Queue<Key>();
        // if (isEmpty() || lo.compareTo(hi) > 0) return keysQueue;
        keys(rootNode, keysQueue, leftBarKey, rightBarKey);
        return keysQueue;
    }

    // add the keys between lo and hi in the subtree rooted at x
    // to the queue
    private void keys(Node currentNode, Queue<Key> keysQueue, Key leftBarKey, Key rightBarKey) {
        if (currentNode == null) return;
        int leftResult = leftBarKey.compareTo(currentNode.key);
        int rightResult = rightBarKey.compareTo(currentNode.key);

        if (leftResult < 0) keys(currentNode.leftSubNode, keysQueue, leftBarKey, rightBarKey);
        if (leftResult <= 0 && rightResult >= 0) keysQueue.enqueue(currentNode.key);
        if (rightResult > 0) keys(currentNode.rightSubNode, keysQueue, leftBarKey, rightBarKey);
    }

    /**
     * Returns the number of keys in the symbol table in the given range.
     *
     * @param leftBarKey  minimum endpoint
     * @param rightBarKey maximum endpoint
     * @return the number of keys in the symbol table between {@code lo}
     * (inclusive) and {@code hi} (inclusive)
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *                                  is {@code null}
     */
    public int size(Key leftBarKey, Key rightBarKey) {
        if (leftBarKey == null) throw new IllegalArgumentException("first argument to itsNodesAmount() is null");
        if (rightBarKey == null) throw new IllegalArgumentException("second argument to itsNodesAmount() is null");

        if (leftBarKey.compareTo(rightBarKey) > 0) return 0;
        if (contains(rightBarKey)) return rankingOf(rightBarKey) - rankingOf(leftBarKey) + 1;
        else return rankingOf(rightBarKey) - rankingOf(leftBarKey);
    }


    /***************************************************************************
     *  Check integrity of red-black tree data structure.
     ***************************************************************************/
    private boolean checkIfRedBlackTree() {
        if (!isBST()) StdOut.println("Not in symmetric order");
        if (!isSizeConsistent()) StdOut.println("Subtree counts not consistent");
        if (!isRankConsistent()) StdOut.println("Ranks not consistent");
        if (!is23Tree()) StdOut.println("Not a 2-3 tree");
        if (!isRedBlackTreeBalanced()) StdOut.println("Not balanced");
        return isBST() && isSizeConsistent() && isRankConsistent() && is23Tree() && isRedBlackTreeBalanced();
    }

    // does this binary tree satisfy symmetric order?
    // Note: this test also ensures that data structure is a binary tree since order is strict
    private boolean isBST() {
        return isBST(rootNode, null, null);
    }

    // is the tree rooted at x a BST with all keys strictly between min and max
    // (if min or max is null, treat as empty constraint)
    // Credit: Bob Dondero's elegant solution
    private boolean isBST(Node x, Key min, Key max) {
        if (x == null) return true;
        if (min != null && x.key.compareTo(min) <= 0) return false;
        if (max != null && x.key.compareTo(max) >= 0) return false;
        return isBST(x.leftSubNode, min, x.key) && isBST(x.rightSubNode, x.key, max);
    }

    // 结点中的nodesAmount的数值 维护地是否正确?
    private boolean isSizeConsistent() {
        return isSizeConsistent(rootNode);
    }

    private boolean isSizeConsistent(Node currentNode) {
        if (currentNode == null) return true;
        if (currentNode.itsNodesAmount != size(currentNode.leftSubNode) + size(currentNode.rightSubNode) + 1)
            return false;
        return isSizeConsistent(currentNode.leftSubNode) && isSizeConsistent(currentNode.rightSubNode);
    }

    // check that ranks are consistent
    // 检查 排名是否是consistent的？
    private boolean isRankConsistent() {
        for (int currentRanking = 0; currentRanking < size(); currentRanking++)
            if (currentRanking != rankingOf(select(currentRanking))) return false;

        for (Key currentKey : keys())
            if (currentKey.compareTo(select(rankingOf(currentKey))) != 0) return false;

        return true;
    }

    // Does the tree have no red rightSubNode links, and at most one (leftSubNode)
    // red links in a row on any path?
    private boolean is23Tree() {
        return is23Tree(rootNode);
    }

    private boolean is23Tree(Node currentNode) {
        if (currentNode == null) return true;
        if (isRed(currentNode.rightSubNode)) return false;
        if (currentNode != rootNode && isRed(currentNode) && isRed(currentNode.leftSubNode))
            return false;

        return is23Tree(currentNode.leftSubNode) && is23Tree(currentNode.rightSubNode);
    }

    // do all paths from rootNode to leaf have same number of black edges?
    private boolean isRedBlackTreeBalanced() {
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

        return isBalanced(currentNode.leftSubNode, blackLinkAmount)
                && isBalanced(currentNode.rightSubNode, blackLinkAmount);
    }


    /**
     * Unit tests the {@code RedBlackTreeSymbolTable} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        RedBlackTreeSymbolTable<String, Integer> symbolTable = new RedBlackTreeSymbolTable<String, Integer>();
        for (int currentSpot = 0; !StdIn.isEmpty(); currentSpot++) {
            String currentKey = StdIn.readString();
            symbolTable.put(currentKey, currentSpot);
        }

        StdOut.println();

        for (String currentKey : symbolTable.keys())
            StdOut.println(currentKey + " " + symbolTable.get(currentKey));

        StdOut.println();
    }
}