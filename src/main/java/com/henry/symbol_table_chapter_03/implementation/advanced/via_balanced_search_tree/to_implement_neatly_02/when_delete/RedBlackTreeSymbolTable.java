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

    // 删除符号表中的最大键 及 其所关联的value
    // 整体的不变性 - 当前结点不是2-结点  手段：在左倾红黑树中，可以通过结点的左链接是否为红色 来 判断结点是不是2-结点
    // 具体的不变性 - 在查询路径中，保证 当前节点 或者 当前节点的右子结点为红色
    private Node deleteMax(Node currentNode) {
        // Ⅰ 递归调用之前做一些事情
        /* 在查询路径中，引入一个红节点👇 */
        // 手段：如果当前节点的左子结点是红节点(2-3-4树中的3-结点)，则 右旋转当前结点 来 得到红色的右链接（2-3-4树中的3-结点）
        if (isA3Node(currentNode))
            currentNode = leanRedLinkToRight(currentNode);

        // 如果调用沿着右脊执行到了树的底部...
        if (reachToBottomOnRightSpine(currentNode))
            // 则：删除最大结点（红节点/叶子节点）
            return performDeletion();

        // 判断查询路径中的下一个结点(当前节点的右子结点) 是否为2-3-4树中的2-结点...
        // 如果是一个2-结点...
        if (incomingNodeIsA2NodeInRightSpine(currentNode))
            // 则：在查询路径中引入红链接，使之不再是一个2-结点
            // 手段：使用 moveRedRight() 来 把红链接沿着查找路径往下推
            currentNode = introduceRedLinkIntoMaxPath(currentNode);

        // Ⅱ 执行删除操作，并把 “删除了最大节点后的右子树” 重新绑定到“当前结点”上
        // 🐖 经过Ⅰ的调整后，我们可以确保 删除动作发生在一个 不是2-结点的结点中
        currentNode.rightSubNode = deleteMax(currentNode.rightSubNode);

        // 删除结点后，在向上的过程中，修复红色右链接 & 4-结点
        return fixMightBreaches(currentNode);
    }

    // 判断查询路径上的下一个结点是不是2-结点
    private boolean incomingNodeIsA2NodeInRightSpine(Node currentNode) {
        // 查询路径上的下一个结点
        Node incomingNode = currentNode.rightSubNode;
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

    private boolean rootNodeIsA2Node() {
        return !isRed(rootNode.leftSubNode) && !isRed(rootNode.rightSubNode);
    }

    // 删除当前符号表中的最小键&与其关联的值
    // 整体的不变性 - 即当前节点不是2节点
    // 具体的不变性 - 在查询路径中，保持当前节点为红色 或者 当前节点的左子结点为红色
    private Node deleteMin(Node currentNode) {
        // 查询最小key的过程会沿着树的左脊递归下去，直到遇到最小结点
        if (reachToBottom(currentNode))
            return performDeletion();

        // 在沿着树向下递归查找的过程中，判断查询路径上的下一个节点（当前节点的左子结点）是不是一个2-结点
        if (incomingNodeIsA2Node(currentNode))
            // 在查询路径中引入红链接，使之不再是一个2-结点
            currentNode = introduceRedLinkInMinPath(currentNode);

        // 在确保路径中的当前节点不是2-结点之后，在左子树中递归地执行删除操作
        currentNode.leftSubNode = deleteMin(currentNode.leftSubNode);

        // 对执行了删除操作后的树恢复平衡，得到符合所有约束的树（aka 红黑树）
        return fixMightBreaches(currentNode);
    }


    // 判断当前节点的下一个结点是不是2-结点
    private boolean incomingNodeIsA2Node(Node currentNode) {
        // 手段：#1 获取到当查询路径上的下一个结点 currentNode.leftSubNode；
        Node incomingNode = currentNode.leftSubNode;
        // #2 判断指向该结点的链接是否为红色(2-3-4树中的3-结点①的左链接就是红色的);
        // #3 判断其左链接是不是为红色（2-3-4树中的3-结点①与4-结点的左链接都是红色的）
        // 如果都不是。则：说明查询路径中出现了2-结点
        return !isRed(incomingNode) && !isRed(incomingNode.leftSubNode);
    }

    // 删除掉最小结点（红节点/叶子节点/3-结点or4-结点的一个内部节点）
    private Node performDeletion() {
        // 手段：返回null / 使用null关联到父结点上
        return null;
    }

    private boolean reachToBottom(Node currentNode) {
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

    // 使一个左倾的链接变成右倾 - 右旋转左链接
    private Node rotateRight(Node currentNode) {
        assert (currentNode != null) && isRed(currentNode.leftSubNode);

        // #1 结构上的变更
        Node replacerNode = currentNode.leftSubNode; // 找到替换结点（aka 当前节点的左子结点）
        currentNode.leftSubNode = replacerNode.rightSubNode; // 用替换结点的右子树 来 作为当前结点的左子树
        replacerNode.rightSubNode = currentNode; // 使用当前节点 来 作为替换节点的右子树

        // #2 颜色上的变更
        replacerNode.color = currentNode.color;
        currentNode.color = RED; // 旋转后，当前节点仍旧是一个红节点

        // #3 子树中结点数量的变更（替换节点&当前结点）
        replacerNode.itsNodesAmount = currentNode.itsNodesAmount; // “替换结点”子树中的结点数量 与 “当前结点”中的结点数量相同
        currentNode.itsNodesAmount = size(currentNode.leftSubNode) + size(currentNode.rightSubNode) + 1;

        // 返回替换节点
        return replacerNode;
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
            if (incomingNodeIsA2Node(currentNode))
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
        return findNodeWithMinKey(rootNode).key;
    }

    // the smallest key in subtree rooted at x; null if no such key
    private Node findNodeWithMinKey(Node currentNode) {
        // assert x != null;
        if (currentNode.leftSubNode == null) return currentNode;
        else return findNodeWithMinKey(currentNode.leftSubNode);
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