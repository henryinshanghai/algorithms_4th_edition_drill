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

    // 使用循环的方式 在树中查找传入的key
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
     * 符号表中是否包含有 传入的key?
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
     * 向符号表中插入指定的键值对
     * 如果符号表中存在有传入的键，则：覆写其所对应的值
     * 如果传入的值是null，则：删除指定的键（及 与之关联的value）
     * @param passedKey       the key
     * @param associatedValue the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(Key passedKey, Value associatedValue) {
        if (passedKey == null)
            throw new IllegalArgumentException("first argument to put() is null");

        // 如果传入的value是null，则：执行删除
        if (associatedValue == null) {
            delete(passedKey);
            return;
        }

        // 向指定的树（初始是rootNode）中，添加键值对
        rootNode = put(rootNode, passedKey, associatedValue);

        // 把根结点的颜色 设置回黑色
        rootNode.color = BLACK;
        // assert check();
    }

    // 向 根节点为currentNode的树中，插入键值对
    private Node put(Node currentNode, Key passedKey, Value associatedValue) {
        /* 递归终结条件：对传入key的查找终止于一个null结点（说明不存在这样的结点），则：需要创建一个新结点，并插入到树中 */
        if (currentNode == null) return new Node(passedKey, associatedValue, RED, 1);

        /* 根据传入的key 与 当前树的根结点key之间的大小关系，决定具体的行为 👇 */
        int result = passedKey.compareTo(currentNode.key);
        if (result < 0) // 如果更小，则：递归地在左子树中执行插入操作
            currentNode.leftSubNode = put(currentNode.leftSubNode, passedKey, associatedValue);
        else if (result > 0) // 如果更大，则：递归地在右子树中执行插入操作
            currentNode.rightSubNode = put(currentNode.rightSubNode, passedKey, associatedValue);
        else // 如果相等，则：更新当前结点的value
            currentNode.value = associatedValue;

        // 在插入结点（红节点）后，处理树中可能出现的breach - #1 红色的右链接（手段：左旋转当前结点）； #2 连续的红色链接（右旋转当前结点 | 翻转颜色）；
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
     * 移除符号表中的最大key 及其所关联的value
     * 请注意，涉及的转换与上一个练习中的转换略有不同，因为红色链接向左倾斜。
     * 不变性：向下查询的过程中，当前结点总是红色的
     */
    public void deleteMax() {
        if (isEmpty()) throw new NoSuchElementException("BST underflow");

        // 如果查询路径上的第一个链接不是红链接（根结点的左右子节点都是黑色的），说明根结点是一个2-结点。则：
        // 把根结点改变成为一个红节点 - 后继才能把这个红链接往下推
        if (rootNodeIsA2Node())
            rootNode.color = RED;

        rootNode = deleteMax(rootNode);

        // 删除完成后，把根结点强制设置为黑色（红黑树的定义）
        if (!isEmpty()) rootNode.color = BLACK;
        // assert check();
    }

    private boolean rootNodeIsA2Node() {
        return !isRed(rootNode.leftSubNode) && !isRed(rootNode.rightSubNode);
    }

    // 删除符号表中的最大键 及 其所关联的value
    // 整体的不变性 - 当前结点不是2-结点  手段：在左倾红黑树中，可以通过结点的左链接是否为红色 来 判断结点是不是2-结点
    // 具体的不变性 - 在查询路径中，保证 当前节点 或者 当前节点的右子结点为红色
    private Node deleteMax(Node currentNode) {
        // Ⅰ 递归调用之前做一些事情
        /* 在查询路径中，引入一个红节点👇 */
        // 手段：如果当前节点的左子结点是红节点(2-3-4树中的3-结点)，则 右旋转当前结点 来 得到红色的右链接（2-3-4树中的3-结点）
        if (isA3Node(currentNode))
            currentNode = leanRedLinkToRight(currentNode);

        // 如果调用沿着右脊 递归执行到了树的底部...
        if (reachToBottomOnRightSpine(currentNode))
            // 则：删除最大结点（红节点/叶子节点）
            return performDeletion();

        // 在沿着树的右脊向下递归查找的过程中，判断查询路径中的下一个结点(当前节点的右子结点) 是否为2-3-4树中的2-结点...
        // 如果是一个2-结点...
        if (incomingNodeIsA2NodeInRightSpine(currentNode))
            // 则：在查询路径中引入红链接，使之不再是一个2-结点
            // 手段：使用 moveRedRight() 来 把红链接沿着查找路径往下推
            currentNode = introduceRedLinkIntoMaxPath(currentNode);

        // Ⅱ 执行删除操作，并把 “删除了最大节点后的右子树” 重新绑定到“当前结点的右子树”上
        // 🐖 经过Ⅰ的调整后，我们可以确保 删除动作发生在一个 不是2-结点的结点中
        currentNode.rightSubNode = deleteMax(currentNode.rightSubNode);

        // 对执行了删除操作后的树恢复约束，得到符合左倾红黑树所有约束的树（aka 红黑树）
        // 🐖 这是一个从叶子节点到根结点的过程
        return fixMightBreaches(currentNode);
    }

    // 判断沿着右脊的查询路径上的下一个结点 是不是2-结点
    private boolean incomingNodeIsA2NodeInRightSpine(Node currentNode) {
        // 获取到查询路径上的下一个结点
        Node incomingNode = currentNode.rightSubNode;
        // 判断该节点是不是一个2-结点   手段：只要它不属于3-结点（由红色的左链接所连接的两个物理结点），就可以证明它是2-结点
        return !isRed(incomingNode) && !isRed(incomingNode.leftSubNode);
    }

    // 沿着查询最大结点的路径达到右脊的底部
    private boolean reachToBottomOnRightSpine(Node currentNode) {
        return currentNode.rightSubNode == null;
    }

    // 把红链接右倾
    private Node leanRedLinkToRight(Node currentNode) {
        return rotateRight(currentNode);
    }

    // 判断红黑树的当前节点 在其对应的2-3树中是否为一个3-结点
    private boolean isA3Node(Node currentNode) {
        return isRed(currentNode.leftSubNode);
    }

    /**
     * 从符号表中删除最小键（及其所关联的值）
     * 通过保持与文本中给出的转换的对应关系来实现RedBlackBST.java的deleteMin（）操作；
     * 作用：1 以使树的左脊向下移动；
     *      2 同时保持树的不变性————即当前节点不是2节点。
     */
    public void deleteMin() {
        if (isEmpty()) throw new NoSuchElementException("BST underflow");

        // 如果查询路径上的第一个链接不是红链接（根结点的左右子节点都是黑色的），说明根结点是一个2-结点。则：
        // 把根结点改变成为一个红节点 - 后继才能把这个红链接往下推
        if (rootNodeIsA2Node())
            rootNode.color = RED;

        rootNode = deleteMin(rootNode);

        if (!isEmpty()) rootNode.color = BLACK;
        // assert check();
    }


    // 删除当前符号表中的最小键&与其关联的值
    // 整体的不变性 - 即当前节点不是2节点
    // 具体的不变性 - 在查询路径中，保持当前节点为红色 或者 当前节点的左子结点为红色
    private Node deleteMin(Node currentNode) {
        // 如果调用沿着左脊 递归执行到了树的底部...
        if (reachToBottomOnLeftSpine(currentNode))
            return performDeletion();

        // 在沿着树的左脊向下递归查找的过程中，判断查询路径上的下一个节点（当前节点的左子结点）是否为2-3-4树中的2-结点...
        // 如果是一个2-结点...
        if (incomingNodeIsA2NodeInLeftSpine(currentNode))
            // 在查询路径中引入红链接，使之不再是一个2-结点
            currentNode = introduceRedLinkInMinPath(currentNode);

        // 在确保路径中的当前节点不是2-结点之后
        // 在左子树中递归地执行删除操作，并把 “删除了最小节点后的右子树” 重新绑定到“当前结点的左子树”上
        currentNode.leftSubNode = deleteMin(currentNode.leftSubNode);

        // 对执行了删除操作后的树恢复约束，得到符合左倾红黑树所有约束的树（aka 红黑树）
        // 🐖 这是一个从叶子节点到根结点的过程
        return fixMightBreaches(currentNode);
    }


    // 判断沿着左脊的查询路径上的下一个结点 是不是2-结点
    private boolean incomingNodeIsA2NodeInLeftSpine(Node currentNode) {
        // 获取到查询路径上的下一个结点
        Node incomingNode = currentNode.leftSubNode;
        // 判断该节点是不是一个2-结点   手段：只要它不属于3-结点（由红色的左链接所连接的两个物理结点），就可以证明它是2-结点
        return !isRed(incomingNode) && !isRed(incomingNode.leftSubNode);
    }

    // 删除掉最小结点（红节点/叶子节点/3-结点or4-结点的一个内部节点）
    private Node performDeletion() {
        // 手段：返回null / 使用null关联到父结点上
        return null;
    }

    // 沿着查询最小节点的路径到达左脊的底部
    private boolean reachToBottomOnLeftSpine(Node currentNode) {
        return currentNode.leftSubNode == null;
    }

    // 恢复红黑树的不变性 🐖 这是一套固定流程
    private Node fixMightBreaches(Node currentNode) {
        // assert (toMoveStepsToEndGridWithoutObstacles != null);

        // 需要被修复的三种情况：#1 红色右链接（左旋转）; #2 连续的红色左链接(右旋转); #3 红色的左右子结点（反转颜色）
        if (isRed(currentNode.rightSubNode))
            currentNode = rotateLeft(currentNode); // 红色右链接
        if (isRed(currentNode.leftSubNode) && isRed(currentNode.leftSubNode.leftSubNode))
            currentNode = rotateRight(currentNode); // 两个连续的红色左链接
        if (isRed(currentNode.leftSubNode) && isRed(currentNode.rightSubNode))
            flipColors(currentNode); // 同一个节点连接左右红链接

        currentNode.itsNodesAmount = size(currentNode.leftSubNode) + size(currentNode.rightSubNode) + 1;
        return currentNode;
    }

    // 把当前结点上的红链接 沿着查询路径向下移动
    // 或者，把红链接从右孙子 移动到左孙子
    private Node introduceRedLinkInMinPath(Node currentNode) { // moveRedLeft
        // 🐖 由于所维护的不变性，因此当前节点必然是红节点。
        defaultApproach(currentNode);

        // 判断查询路径上下一个结点的sibling结点 是不是一个 非2-结点
        if (siblingNodeIsNot2Node(currentNode)) {
            // 如果 是一个非2-结点, 则：为了维护“查询路径上的当前结点不是2-结点的不变性”
            // 从2-3-4树的角度来说，我们需要从sibling node中借一个结点
            // 手段：把红链接移动到左脊上
            currentNode = moveRedLinkToLeft(currentNode);
        }

        // 返回 “按需移动红链接”后的当前节点
        return currentNode;
    }

    // 判断查询路径上当前节点的下一个节点的sibling node是不是一个非2-结点
    private boolean siblingNodeIsNot2Node(Node currentNode) {
        // #1 获取到sibling node（currentNode.rightSubNode）;
        Node siblingNode = currentNode.rightSubNode;
        // #2 判断sibling结点 是不是一个非2-结点；
        // 手段：查看它的左链接是否为红色？ 如果是，则：为非2-结点。否则，为2-结点
        return isRed(siblingNode.leftSubNode);
    }

    // 引入红链接的默认手段
    private void defaultApproach(Node currentNode) {
        // 翻转当前节点的颜色：从2-3-4树的角度来看，是 与sibling结点相结合，得到了一个4-结点
        // 作用：维护了 "查询路径上当前节点不是2-结点" 的不变性
        flipColors(currentNode);
    }

    // 把当前子树 右脊中的红链接 移动到 左脊上
    private Node moveRedLinkToLeft(Node currentNode) {
        // #1 右旋转当前结点的右子结点(在右脊上产生连续的红色链接);
        currentNode.rightSubNode = rotateRight(currentNode.rightSubNode);
        // #2 左旋转当前节点（在左脊上产生连续的红色链接）;
        currentNode = rotateLeft(currentNode);
        // #3 翻转当前节点的颜色（只保留左脊上第二层的红链接）
        flipColors(currentNode); // 从结果上看，相当于把 右孙子的红链接移动到左孙子上（从sibling借红链接）

        return currentNode;
    }


    // 把当前节点的红链接 沿着查找路径 向下移动 👇
    // 或者，把红链接 从左孙子 移动到右孙子
    private Node introduceRedLinkIntoMaxPath(Node currentNode) {
        // 默认操作：翻转当前节点的颜色
        // 🐖 由于所维护的不变性，因此当前节点必然是红节点。
        // 翻转当前节点的颜色：从2-3-4树的角度来看，是 与sibling结点相结合，得到了一个4-结点 从而 维护了 当前节点不是2-结点的不变性
        defaultApproach(currentNode);

        if (siblingNodeIsNot2NodeInMaxPath(currentNode)) {
            // 如果 是一个非2-结点, 则：为了维护当前结点不是2-结点的不变性
            // 从2-3-4树的角度来说，我们需要从sibling node中借一个结点
            // 借的手段：通过右旋转把 根结点移动到右脊上
            currentNode = moveRedLinkToRight(currentNode);
        }

        // 返回 “按需移动红链接”后的当前节点
        return currentNode;
    }

    private Node moveRedLinkToRight(Node currentNode) {
        // #1 右旋转当前结点(在右脊上产生连续的红色链接);
        currentNode = rotateRight(currentNode);
        // #2 翻转当前结点的颜色（只保留右脊上第二层的红链接）
        flipColors(currentNode); // 从结果上看（产生了一个右链接 在2-3-4树中，等同于一个3-结点），相当于把左孙子的红链接移动到右孙子上（从sibling借红链接）

        return currentNode;
    }

    // 判断 “查询路径中下一个结点的sibling结点” 是不是一个非2-结点
    private boolean siblingNodeIsNot2NodeInMaxPath(Node currentNode) {
        // 手段：#1 获取“当前节点的左子结点”;
        Node siblingNode = currentNode.leftSubNode;
        // #2 判断其左链接(左子结点)是不是红色 - 如果是，则为非2-结点。如果不是，则为2-结点
        return isRed(siblingNode.leftSubNode);
    }

    /**
     * 从符号表中删除传入的key 及 其所关联的value
     * @param passedKey the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void delete(Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to delete() is null");
        if (!contains(passedKey)) return;

        // 根据需要（下一级结点没有红节点），在查询路径中，手动引入一个红节点
        // 手段：把根结点设置为红色
        if (rootNodeIsA2Node())
            rootNode.color = RED;

        // 从当前树中删除传入的key, 并把删除后的结果绑定回到 当前结点上
        rootNode = delete(rootNode, passedKey);

        // 强制把根结点的颜色设置为黑色
        if (!isEmpty()) rootNode.color = BLACK;
        // assert check();
    }

    // 如果是删除随机的节点，如何能确定查找路径呢？
    private Node delete(Node currentNode, Key passedKey) {

        if (wantedNodeInLeftSpine(currentNode, passedKey)) { // 如果预期删除的节点在左子树中，则：
            // 如果需要，则：为当前查询路径引入红链接
            if (incomingNodeIsA2NodeInLeftSpine(currentNode))
                currentNode = introduceRedLinkInMinPath(currentNode);

            // 递归地从左子树中删除预期节点， 并把删除结点后的树 重新绑定回到 左子树上
            currentNode.leftSubNode = delete(currentNode.leftSubNode, passedKey);
        } else { // 预期删除的结点 在右子树中 或者是 根结点
            // 把红色的左链接推到右边 手段：右旋转当前节点
            if (isA3Node(currentNode))
                currentNode = leanRedLinkToRight(currentNode);

            // 如果在树的叶子节点处找到预期删除的结点，
            if (findTheWantedInRightSpine(currentNode, passedKey))
                // 则：直接删除结点 返回null
                return performDeletion(); // return null to delete the wanted node

            // 如果在查询路径上缺少红链接，则：把红链接移动到查询路径中
            if (incomingNodeIsA2NodeInRightSpine(currentNode))
                currentNode = introduceRedLinkIntoMaxPath(currentNode);

            // 如果当前节点就是待删除的结点...
            if (findWantedNode(currentNode, passedKey)) { // this is like delete in BST
                // 则：借助后继结点的方式 来 实现删除
                deleteViaReplaceWithSuccessor(currentNode);
            }

            // 否则，没有找到预期删除的结点。则：在右子树中继续递归 来 寻找并删除预期的结点
            else currentNode.rightSubNode = delete(currentNode.rightSubNode, passedKey);
        }

        // 修复引入的红色右链接 + 4-结点
        return fixMightBreaches(currentNode);
    }

    // 在红黑树中删除任意位置的结点 - 手段：归约到“删除最小结点”的操作
    private void deleteViaReplaceWithSuccessor(Node currentNode) {
        // 找到当前节点右子树中的最小结点，作为 后继结点
        Node successorNode = findNodeWithMinKey(currentNode.rightSubNode);
        // 使用后继结点的key、value 来 更新当前节点(它的左右子链接没有变化)
        currentNode.key = successorNode.key;
        currentNode.value = successorNode.value;
        // 从右子树中删除最小结点（后继结点）， 并 把删除结点后的子树 重新绑定到 当前节点的右子树上
        currentNode.rightSubNode = deleteMin(currentNode.rightSubNode);
    }

    private boolean findTheWantedInRightSpine(Node currentNode, Key passedKey) {
        return findWantedNode(currentNode, passedKey)
                && reachToBottomOnRightSpine(currentNode);
    }

    private boolean findWantedNode(Node currentNode, Key passedKey) {
        return passedKey.compareTo(currentNode.key) == 0;
    }

    private boolean wantedNodeInLeftSpine(Node currentNode, Key passedKey) {
        return passedKey.compareTo(currentNode.key) < 0;
    }

    /***************************************************************************
     *  Red-black tree helper functions.
     **************************************************************************/

    // 使一个右倾的链接变成左倾
    private Node rotateLeft(Node currentNode) {
        assert (currentNode != null) && isRed(currentNode.rightSubNode);

        // #1 结构上的变更：
        Node replacerNode = currentNode.rightSubNode; // 获取到替换结点
        currentNode.rightSubNode = replacerNode.leftSubNode; // 更新当前节点的右子树（先断开，再连接）
        replacerNode.leftSubNode = currentNode; // 更新替换结点的左子树（先断开，再连接）

        // #2 颜色上的变更：替换结点（更新为当前结点的颜色） & 当前结点（更新为红色）
        replacerNode.color = currentNode.color;
        currentNode.color = RED;

        // #3 子树中的结点数量的变更
        replacerNode.itsNodesAmount = currentNode.itsNodesAmount;
        currentNode.itsNodesAmount = size(currentNode.leftSubNode) + size(currentNode.rightSubNode) + 1;

        return replacerNode;
    }

    // 使一个左倾的链接变成右倾 - 右旋转左链接
    private Node rotateRight(Node currentNode) {
        assert (currentNode != null) && isRed(currentNode.leftSubNode);

        // #1 结构上的变更
        Node replacerNode = currentNode.leftSubNode;
        currentNode.leftSubNode = replacerNode.rightSubNode;
        replacerNode.rightSubNode = currentNode;

        // #2 颜色上的变更
        replacerNode.color = currentNode.color;
        currentNode.color = RED; // 旋转后，当前节点仍旧是一个红节点，只是倾斜方向变化

        // #3 子树中结点数量的变更（替换节点&当前结点）
        replacerNode.itsNodesAmount = currentNode.itsNodesAmount; // “替换结点”子树中的结点数量 与 “当前结点”中的结点数量相同
        currentNode.itsNodesAmount = size(currentNode.leftSubNode) + size(currentNode.rightSubNode) + 1;

        // 返回替换节点
        return replacerNode;
    }

    // 反转当前结点&它的左右子节点的颜色
    private void flipColors(Node currentNode) {
        // 把当前结点 & 它的左右子节点 的颜色变更为 “与当前颜色不同的另一种颜色”
        currentNode.color = !currentNode.color;
        currentNode.leftSubNode.color = !currentNode.leftSubNode.color;
        currentNode.rightSubNode.color = !currentNode.rightSubNode.color;
    }


    /***************************************************************************
     *  Utility functions.
     ***************************************************************************/

    // 返回二叉搜索树的高度 - 1-结点构成的树高度为0
    public int height() {
        return height(rootNode);
    }

    private int height(Node currentNode) {
        if (currentNode == null) return -1;
        return 1 + Math.max(height(currentNode.leftSubNode), height(currentNode.rightSubNode));
    }

    /***************************************************************************
     *  Ordered symbol table methods. 与有序性相关的符号表方法
     ***************************************************************************/

    /**
     * 返回符号表中最小的键
     *
     * @return the smallest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
    public Key minKey() {
        if (isEmpty()) throw new NoSuchElementException("calls min() with empty symbol table");
        return findNodeWithMinKey(rootNode).key;
    }

    // 返回当前树中的最小结点
    private Node findNodeWithMinKey(Node currentNode) {
        // assert x != null;
        if (currentNode.leftSubNode == null) return currentNode;
        else return findNodeWithMinKey(currentNode.leftSubNode);
    }

    /**
     * 返回符号表中的最大键
     *
     * @return the largest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
    public Key maxKey() {
        if (isEmpty()) throw new NoSuchElementException("calls max() with empty symbol table");
        return findNodeWithMaxKey(rootNode).key;
    }

    // 返回当前树中的最大结点
    private Node findNodeWithMaxKey(Node currentNode) {
        // assert x != null;
        if (currentNode.rightSubNode == null) return currentNode;
        else return findNodeWithMaxKey(currentNode.rightSubNode);
    }


    /**
     * 返回符号表中小于等于 传入的key的最大的key
     * @param passedKey the key
     * @return the largest key in the symbol table less than or equal to {@code key}
     * @throws NoSuchElementException   if there is no such key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Key floor(Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to floor() is null");
        if (isEmpty()) throw new NoSuchElementException("calls floor() with empty symbol table");

        // 从当前的结点树中查找满足条件的结点
        Node flooredNode = floor(rootNode, passedKey);

        // 根据具体的查询结果决定返回值 或者 抛出异常
        if (flooredNode == null) throw new NoSuchElementException("argument to floor() is too small");
        else return flooredNode.key;
    }

    // 返回当前结点树（以当前节点作为根结点的树）中，小于等于传入key的最大key
    private Node floor(Node currentNode, Key passedKey) {
        // 如果查询过程递归到叶子节点，说明没有找到满足条件的结点，则：返回null 表示查找未成功
        if (currentNode == null) return null;

        int result = passedKey.compareTo(currentNode.key);
        if (result == 0) return currentNode;
        if (result < 0) return floor(currentNode.leftSubNode, passedKey);

        // 如果 passedKey 大于 当前节点中的key，则有两种可能：#1 满足条件的结点再右子树中； #2 满足条件的结点就是当前节点
        Node flooredNode = floor(currentNode.rightSubNode, passedKey);
        // #1 如果找到了满足条件的key，则：返回其结点
        if (flooredNode != null) return flooredNode;
        // #2 右子树中不存在比passedKey更小的键，因此 currentNode就是 flooredNode
        else return currentNode;
    }

    /**
     * 返回符号表中大于等于 passedKey的最小键
     *
     * @param passedKey 传入的key
     * @return 符号表中大于等于 传入的key的最小key
     * @throws NoSuchElementException   if there is no such key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Key ceiling(Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to ceiling() is null");
        if (isEmpty()) throw new NoSuchElementException("calls ceiling() with empty symbol table");

        // 从结点树中，查找到大于等于passedKey的最小结点
        Node ceiledNode = ceiling(rootNode, passedKey);

        if (ceiledNode == null) throw new NoSuchElementException("argument to ceiling() is too small");
        else return ceiledNode.key;
    }

    // 返回结点树中 大于等于passedKey的最小结点
    private Node ceiling(Node currentNode, Key passedKey) {
        // 如果查询持续到了叶子节点，说明不存在满足条件的结点，则：返回null - 表示没有查询到满足条件的结点
        if (currentNode == null) return null;

        // 比较passedKey 与 当前节点中的key
        int result = passedKey.compareTo(currentNode.key);
        if (result == 0) return currentNode;
        if (result > 0) return ceiling(currentNode.rightSubNode, passedKey);

        // 如果passedKey小于currentNode.key，有两种情况：
        Node ceiledNode = ceiling(currentNode.leftSubNode, passedKey);

        // #1 满足条件的结点在左子树中(如果存在的话);
        if (ceiledNode != null) return ceiledNode;
        // #2 满足条件的结点是currentNode
        else return currentNode;
    }

    /**
     * 返回符号表中指定排名(ranking)的键
     * 特征：符号表中有 ranking个比它更小的键。
     * 换句话说，这个key是符号表中 第 (rank+1) 小的key
     *
     * @param passedRanking the order statistic
     * @return the key in the symbol table of given {@code rank}
     * @throws IllegalArgumentException unless {@code rank} is between 0 and （n-1）
     */
    public Key selectOut(int passedRanking) {
        if (passedRanking < 0 || passedRanking >= size()) {
            throw new IllegalArgumentException("argument to select() is invalid: " + passedRanking);
        }

        // 在结点树中，找到指定排名的结点
        return selectOut(rootNode, passedRanking);
    }

    // 返回结点树中，指定排名的结点的key
    // 先决条件：传入的排名在一个合法的范围内 - 🐖 排名从0开始 0th
    private Key selectOut(Node currentNode, int passedRanking) {
        // 如果查找过程递归到了叶子节点，说明查找满足条件的结点失败，则：返回null
        if (currentNode == null) return null;

        // 获取左子树中的结点数量 - 原理：二叉查找树中结点的有序性
        int leftSize = size(currentNode.leftSubNode);
        // 如果 左子树中的结点数量 比起 传入的排名更大，则：满足条件的结点必然在左子树中 递归地在左子树中继续查找满足条件的结点
        if (leftSize > passedRanking) return selectOut(currentNode.leftSubNode, passedRanking);
        // 如果 左子树中的结点数量 比起 传入的排名小，则：满足条件的结点必然在右子树中 递归地在右子树中继续查找满足条件的结点
        // 🐖 由于左子树与根结点都已经占据了排名，所以在右子树中需要查找的是 排名为 passedRanking - leftSize -1 的键
        else if (leftSize < passedRanking) return selectOut(currentNode.rightSubNode, passedRanking - leftSize - 1);
        else return currentNode.key;
    }

    /**
     * 返回符号表中，所有严格小于 passedKey的键的总数量
     * @param passedKey the key
     * @return the number of keys in the symbol table strictly less than {@code key}
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public int rankingOf(Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to rank() is null");

        // 获取到在结点树中，passedKey的排名 🐖 排名从0开始 0-th
        return rankingOf(passedKey, rootNode);
    }

    // 在结点树中，小于passedKey的结点的数量
    private int rankingOf(Key passedKey, Node currentNode) {
        // 如果没有查找到满足条件的结点，则：返回0 表示不存在这样的结点
        if (currentNode == null) return 0;

        // 比较 passedKey 与 currentNode.key
        int result = passedKey.compareTo(currentNode.key);
        // 如果 passedKey更小，说明它一定在左子树的范围中，则：在左子树中递归地查找并返回它的排名
        if (result < 0) return rankingOf(passedKey, currentNode.leftSubNode);
        // 如果更大，说明它在右子树的范围中，则：在右子树中递归地查找并返回它的排名
        else if (result > 0) return 1 + size(currentNode.leftSubNode) + rankingOf(passedKey, currentNode.rightSubNode);
        // 如果相等，则：左子树中结点的数量 就是 它的排名 - 排名从0-th开始
        else return size(currentNode.leftSubNode);
    }

    /***************************************************************************
     *  Range count and range search.
     ***************************************************************************/

    /**
     * 以一个Iterable的形式 来 返回符号表中所有的key
     * 如果想要遍历st符号表中的所有的键，可以使用 foreach的标记语法  for (Key key : st.keys())
     *
     * @return all keys in the symbol table as an {@code Iterable}
     */
    public Iterable<Key> keys() {
        if (isEmpty()) return new Queue<Key>();
        return keys(minKey(), maxKey());
    }

    /**
     * 以Iterable的方式 来 返回符号表中指定范围（左右闭区间）内的键
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

    // 把结点树中，在[leftBarKey, rightBarKey]区间内的所有键 都添加到队列中
    private void keys(Node currentNode, Queue<Key> keysQueue, Key leftBarKey, Key rightBarKey) {
        // 查询过程结果，直接return
        if (currentNode == null) return;

        // 比较左边界 与 当前节点中的key
        int leftResult = leftBarKey.compareTo(currentNode.key);
        // 比较右边界 与 当前结点中的key
        int rightResult = rightBarKey.compareTo(currentNode.key);

        // 如果左边界小于根结点（说明区间覆盖左子树），则：把左子树中满足条件的key添加到队列中
        if (leftResult < 0) keys(currentNode.leftSubNode, keysQueue, leftBarKey, rightBarKey);
        // 如果左边界小于等于根结点&右边界大于等于根结点(说明区间包含当前节点)，则：把当前结点添加到队列中
        if (leftResult <= 0 && rightResult >= 0) keysQueue.enqueue(currentNode.key);
        // 如果右边界大于根结点(说明区间覆盖右子树)，则：把当前结点添加到队列中
        if (rightResult > 0) keys(currentNode.rightSubNode, keysQueue, leftBarKey, rightBarKey);
    }

    /**
     * 返回符号表中指定范围内（左右闭区间）所有键的总数量
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

        // 如果区间无效，则：返回0 表示此区间内不存在满足条件的键
        if (leftBarKey.compareTo(rightBarKey) > 0) return 0;
        // 公式： 区间中所包含的键 = 右边界的排名 - 左边界的排名 (+1)
        // 🐖 如果右边界在树中存在的话，则：满足条件的键的数量 + 1
        if (contains(rightBarKey)) return rankingOf(rightBarKey) - rankingOf(leftBarKey) + 1;
        else return rankingOf(rightBarKey) - rankingOf(leftBarKey);
    }


    /***************************************************************************
     *  Check integrity of red-black tree data structure. 检查红黑树结构的完整性
     ***************************************************************************/
    private boolean checkIfRedBlackTree() {
        // 是二叉查找树
        if (!isBST()) StdOut.println("Not in symmetric order");
        // 当前节点树的节点数量 恒等于 左子树的节点数量 + 右子树的结点数量 + 1（递归成立）
        if (!isSizeConsistent()) StdOut.println("Subtree counts not consistent");
        // 选择指定排名的元素，其在树中的排名 恒等于 最开始选择的排名 （循环成立）
        // 使用指定键在树中的排名，来在树中选择相同排名的元素。 得到的必然是 最开始指定的元素（循环成立）
        if (!isRankConsistent()) StdOut.println("Ranks not consistent");
        // 是一个严格意义上的2-3树
        if (!is23Tree()) StdOut.println("Not a 2-3 tree");
        // 严格遵守平衡性
        if (!isRedBlackTreeBalanced()) StdOut.println("Not balanced");
        // 红黑树既是...又是...还是...而且是...
        return isBST() && isSizeConsistent() && isRankConsistent() && is23Tree() && isRedBlackTreeBalanced();
    }

    // 这个二叉树满足 对称有序性吗？
    // 🐖 这个测试也能够保证数据结构是二叉树（因为顺序是严格的）
    private boolean isBST() {
        return isBST(rootNode, null, null);
    }

    // 判断以currentNode作为根的结点树中的所有键是否严格在 [minKeyBar, maxKeyBar]之间
    // 🐖 如果 minKeyBar 或者 maxKeyBar传入的值为null。则：视为没有约束
    // 荣誉：Bob Dondero's elegant solution
    private boolean isBST(Node currentNode, Key minKeyBar, Key maxKeyBar) {
        if (currentNode == null) return true;

        // 小于左边界
        if (minKeyBar != null && currentNode.key.compareTo(minKeyBar) <= 0) return false;
        // 大于右边界
        if (maxKeyBar != null && currentNode.key.compareTo(maxKeyBar) >= 0) return false;

        // 要求左子树 & 右子树也都是BST
        return isBST(currentNode.leftSubNode, minKeyBar, currentNode.key) && isBST(currentNode.rightSubNode, currentNode.key, maxKeyBar);
    }

    // 结点中的nodesAmount的数值 维护地是否正确
    private boolean isSizeConsistent() {
        return isSizeConsistent(rootNode);
    }

    private boolean isSizeConsistent(Node currentNode) {
        if (currentNode == null) return true;
        if (currentNode.itsNodesAmount != size(currentNode.leftSubNode) + size(currentNode.rightSubNode) + 1)
            return false;
        return isSizeConsistent(currentNode.leftSubNode) && isSizeConsistent(currentNode.rightSubNode);
    }

    // 检查 排名是否是consistent的？
    private boolean isRankConsistent() {
        // 选择出指定排名的元素，其在树中的排名 恒等于 最开始选择的排名 （循环成立）
        for (int currentRanking = 0; currentRanking < size(); currentRanking++)
            if (currentRanking != rankingOf(selectOut(currentRanking))) return false;

        // 使用指定键在树中的排名，来在树中选择相同排名的元素。 得到的必然是 最开始指定的元素（循环成立）
        for (Key currentKey : keys())
            if (currentKey.compareTo(selectOut(rankingOf(currentKey))) != 0) return false;

        // 通过检查
        return true;
    }

    // 判断树中是不是 #1 在任何路径中都不存在红色的右链接， 以及 #2 在任何路径中都不存在连续的(in a row)红色左链接
    private boolean is23Tree() {
        return is23Tree(rootNode);
    }

    private boolean is23Tree(Node currentNode) {
        // 路径中所有的结点都通过检查，则：返回true 表示的确是一个23树
        if (currentNode == null) return true;

        // 如果存在红色的右链接，则：返回false 表示没有完整对应到一个2-3树
        if (isRed(currentNode.rightSubNode)) return false;

        // 如果当前节点是红色的，并且当前节点的左链接也是红色的。说明存在连续的红链接...
        if (currentNode != rootNode && isRed(currentNode) && isRed(currentNode.leftSubNode))
            return false;

        // 左右子树也要递归地满足相同的约束
        return is23Tree(currentNode.leftSubNode) && is23Tree(currentNode.rightSubNode);
    }

    // 从根结点到叶子节点的所有路径中 都包含有相同数量的黑色边吗？
    private boolean isRedBlackTreeBalanced() {
        int blackLinkAmount = 0;     // number of black links on path from rootNode to min
        Node currentNode = rootNode;

        // 沿着左脊，统计出左脊上所有黑链接的总数量
        while (currentNode != null) {
            if (!isRed(currentNode)) blackLinkAmount++;
            currentNode = currentNode.leftSubNode;
        }

        return isBalanced(rootNode, blackLinkAmount);
    }

    // 判断是否每一个从根结点到叶子节点的路径中，都包含有相同数量的黑链接
    private boolean isBalanced(Node currentNode, int blackLinkAmount) {
        // 当递归执行到叶子节点的时候，预期 blackLinkAmount的值为0
        if (currentNode == null) return blackLinkAmount == 0;
        // 从根结点出发，每次遇到一个黑节点。就把 blackLinkAmount值减一
        if (!isRed(currentNode)) blackLinkAmount--;

        // 在左子树与右子树中递归地 验证黑链接数量的平衡
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