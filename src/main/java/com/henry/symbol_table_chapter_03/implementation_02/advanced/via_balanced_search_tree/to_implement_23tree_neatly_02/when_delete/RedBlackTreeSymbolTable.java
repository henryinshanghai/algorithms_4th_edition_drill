package com.henry.symbol_table_chapter_03.implementation_02.advanced.via_balanced_search_tree.to_implement_23tree_neatly_02.when_delete;

/******************************************************************************
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

// 验证：可以使用红黑树 作为底层数据结构 来 实现符号表
// 手段：使用红黑树中的节点 来 封装 #1 key -> value的映射 & #2 结点的颜色 - 用于使用2-结点 来 表示3-结点
// 性能特征：作为平衡树，红黑树能够保证 - 在最坏的情况下，各种操作的算法增长数量级都是logN
// put()操作特征：#1 插入新结点时，始终插入红色的新结点; #2 插入新结点后，通过{左旋转、右旋转、颜色翻转} 来 维护“合法的红黑树”
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
    private int pairAmountOf(Node currentNode) {
        if (currentNode == null) return 0;
        return currentNode.itsNodesAmount;
    }


    /**
     * Returns the number of key-value pairs in this symbol table.
     *
     * @return the number of key-value pairs in this symbol table
     */
    public int pairAmount() {
        return pairAmountOf(rootNode);
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
    public Value getAssociatedValueOf(Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to get() is null");
        return getAssociatedValueFrom(rootNode, passedKey);
    }

    // 使用循环的方式 在树中查找传入的key
    private Value getAssociatedValueFrom(Node currentNode, Key passedKey) {
        while (currentNode != null) {
            int result = passedKey.compareTo(currentNode.key);
            if (result < 0) currentNode = currentNode.leftSubNode;
            else if (result > 0) currentNode = currentNode.rightSubNode;
            else return currentNode.value;
        }
        return null;
    }

    /**
     * 符号表中是否包含有 传入的key?
     *
     * @param passedKey 传入的key
     * @return 如果符号表中 包含有 该key，返回true。否则 返回false
     * @throws IllegalArgumentException 如果传入的key为null
     */
    public boolean doesContains(Key passedKey) {
        return getAssociatedValueOf(passedKey) != null;
    }

    /***************************************************************************
     *  Red-black tree insertion.
     ***************************************************************************/

    /**
     * 向符号表中插入指定的键值对
     * 如果符号表中存在有传入的键，则：覆写其所对应的值
     * 如果传入的值是null，则：删除指定的键（及 与之关联的value）
     *
     * @param passedKey       指定的key
     * @param associatedValue 其所关联的value
     * @throws IllegalArgumentException 如果传入的key是null
     */
    public void putInPairOf(Key passedKey, Value associatedValue) {
        if (passedKey == null)
            throw new IllegalArgumentException("first argument to put() is null");

        // 如果传入的value是null，则：执行删除
        if (associatedValue == null) {
            deletePairOf(passedKey);
            return;
        }

        // 向指定的树（初始是rootNode）中，添加键值对
        rootNode = putPairInto(rootNode, passedKey, associatedValue);

        // 把根结点的颜色 设置回黑色
        rootNode.color = BLACK;
        // assert check();
    }

    // 向 根节点为currentNode的树中，插入键值对
    private Node putPairInto(Node currentNode, Key passedKey, Value associatedValue) {
        /* 递归终结条件：对传入key的查找终止于一个null结点（说明不存在这样的结点）， */
        if (currentNode == null)
            // 则：需要创建一个新结点，并插入到树中
            return new Node(passedKey, associatedValue, RED, 1);

        /* 根据传入的key 与 当前树的根结点key之间的大小关系，决定具体的行为 👇 */
        int result = passedKey.compareTo(currentNode.key);
        if (result < 0) // 如果更小，则：递归地在左子树中执行插入操作
            currentNode.leftSubNode = putPairInto(currentNode.leftSubNode, passedKey, associatedValue);
        else if (result > 0) // 如果更大，则：递归地在右子树中执行插入操作
            currentNode.rightSubNode = putPairInto(currentNode.rightSubNode, passedKey, associatedValue);
        else // 如果相等，则：更新当前结点的value
            currentNode.value = associatedValue;

        /* 在插入结点（红节点）后，处理树中所有可能出现的breach；   🐖：这是一个从下往上的过程，因此放在递归调用的代码之后 */
        // #1 插入结果：红色的右链接（手段：左旋转当前结点）；
        // 对应的插入情形：① 2-节点的右链接插入；② 3-节点的中链接插入
        if (isRed(currentNode.rightSubNode) && !isRed(currentNode.leftSubNode))
            currentNode = rotateItsRedSubLinkToLeft(currentNode);
        // #2 插入结果②：连续的红色链接（右旋转当前结点 OR 翻转颜色）；
        // 对应的插入情形：③ 3-节点的左链接插入
        if (isRed(currentNode.leftSubNode) && isRed(currentNode.leftSubNode.leftSubNode))
            currentNode = rotateItsRedSubLinkToRight(currentNode);
        // #3 插入结果③：红色的左链接 && 红色的右链接
        // 对应的插入情形：④ 3-节点的右链接插入 🐖 这是一个基础情形，可以通过翻转颜色 来 把红链接沿着路径向上传递
        if (isRed(currentNode.leftSubNode) && isRed(currentNode.rightSubNode))
            flipColors(currentNode);

        // 维护节点中的 树节点数量属性
        currentNode.itsNodesAmount =
                pairAmountOf(currentNode.leftSubNode) + pairAmountOf(currentNode.rightSubNode) + 1;

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
    public void deletePairOfMaxKey() {
        if (isEmpty()) throw new NoSuchElementException("BST underflow");

        // 如果 查询路径上的第一个链接 不是 红链接（根结点的左右子节点 都是黑色的），说明 根结点 是一个2-结点。则：
        // 把 根结点 改变成为 一个红节点 - 后继才能 把这个红链接 往下推
        if (rootNodeHas2BlackChild())
            rootNode.color = RED;

        rootNode = deletePairOfMaxKeyFrom(rootNode);

        // 删除完成后，把 根结点 强制设置为 黑色（红黑树的定义）
        if (!isEmpty()) rootNode.color = BLACK;
        // assert check();
    }

    // 判断根结点 是不是 一个2-结点   手段：判断根结点的左子结点、右子结点 是不是都是 黑色结点
    private boolean rootNodeHas2BlackChild() {
        return !isRed(rootNode.leftSubNode) && !isRed(rootNode.rightSubNode);
    }

    // 删除符号表中的最大键 及 其所关联的value
    // 整体的不变性 - 当前结点不是2-结点  手段：在左倾红黑树中，可以通过结点&左子结点是否为红色 来 判断结点是不是2-结点
    // 具体的不变性 - 在查询路径中，保证 当前节点 或者 当前节点的右子结点为红色
    private Node deletePairOfMaxKeyFrom(Node currentNode) {
        // Ⅰ 递归调用之前（沿着树从上往下）在查询路径中，引入红链接👇
        // Ⅰ-①：如果 当前结点 存在有一个红色的左链接，说明 它是3-节点的上半部分，则：
        if (isTheUpperNodeOf3Node(currentNode))
            // 把左链接右旋转 来 为maxPath中引入红链接
            currentNode = rotateItsRedSubLinkToRight(currentNode);

        // Ⅱ 执行删除操作
        // Ⅱ-① 如果调用沿着右脊 找到了最大结点 aka 递归 执行到了 树的底部...
        if (reachToBottomOnRightSpine(currentNode))
            // 则：删除 最大结点（红节点/叶子节点）
            return performDeletion();

        // Ⅰ-②：如果maxPath的incomingNode 是一个2-结点，说明 需要 为maxPath中引入红链接
        if (incomingNodeIsA2NodeInRightSpine(currentNode))
            // 则：在查询路径中 引入红链接，使之不再是 一个2-结点
            // 手段：使用 moveRedRight() 来 把 红链接 沿着查找路径 往下推
            currentNode = introduceRedLinkIntoMaxPath(currentNode);

        // Ⅱ-② 如果还没有达到最大结点，则 执行删除操作，并把 “删除了最大节点后的右子树” 重新绑定到 “当前结点的右子树” 上
        // 🐖 经过Ⅰ的调整后，我们可以确保 删除动作 会发生在一个 不是2-结点的结点中
        currentNode.rightSubNode = deletePairOfMaxKeyFrom(currentNode.rightSubNode);

        // Ⅲ 对 执行了删除操作后的树 恢复约束，得到 符合左倾红黑树所有约束的 树（aka 红黑树）
        // 🐖 这是一个 从叶子节点到根结点 的过程
        return fixMightBreaches(currentNode);
    }


    // 沿着 查询最大结点的路径 达到 右脊的底部
    private boolean reachToBottomOnRightSpine(Node currentNode) {
        return currentNode.rightSubNode == null;
    }


    // 判断 红黑树的当前节点 在其对应的2-3树中 是否为一个3-结点
    private boolean isTheUpperNodeOf3Node(Node currentNode) {
        return isRed(currentNode.leftSubNode);
    }

    /**
     * 从符号表中 删除最小键（及 其所关联的值）
     * 通过保持 与文本中给出的转换的对应关系 来 实现RedBlackBST.java的deleteMin（）操作；
     * 作用：1 使 树的左脊 向下移动；
     * 2 同时保持树的不变性————即当前节点不是2节点。
     */
    public void deleteNodeOfMinKey() {
        if (isEmpty()) throw new NoSuchElementException("BST underflow");

        // 如果 查询路径上的第一个链接 不是 红链接（根结点的左右子节点都是黑色的），说明 根结点 是一个2-结点。则：
        // 把 根结点 改变成为 一个红节点 - 后继才能 把这个红链接 往下推
        if (rootNodeHas2BlackChild())
            rootNode.color = RED;

        rootNode = deleteNodeOfMinKeyFrom(rootNode);

        if (!isEmpty()) rootNode.color = BLACK;
        // assert check();
    }


    // 删除 当前符号表中的最小键 && 与其关联的值
    // 整体的不变性 - 即当前节点不（会）是2节点
    // 具体的不变性 - 在查询路径中，保持 当前节点 为红色 或者 当前节点的左子结点 为红色
    private Node deleteNodeOfMinKeyFrom(Node currentRootNode) {
        /* Ⅰ 执行 物理删除操作 */
        // 如果调用 沿着左脊 执行到了 最小节点，说明 已经找到了 minKey，
        if (reachToBottomOnLeftSpine(currentRootNode))
            // 则：return null; 来 直接执行物理删除
            return performDeletion();

        /* Ⅱ 在 递归调用 之前（也就是 沿着树从上往下），先在 查询路径 中，引入红链接👇 */
        // 如果 minPath的incomingNode 是一个2-结点，说明 minPath中需要引入一个红链接 来 保持不变性，
        if (incomingNodeIsA2NodeInLeftSpine(currentRootNode))
            // 则: 为minPath中 引入红链接, 使之 不再是 一个2-结点
            currentRootNode = introduceRedLinkIntoMinPath(currentRootNode);

        /* Ⅲ（在 确保路径中的当前节点 不是2-结点 之后）👇 */
        // 在左子树中 递归地继续执行 删除最小键的操作，并 把 “删除了最小节点后的左子树” 重新绑定回到 “当前结点的左子树”上
        currentRootNode.leftSubNode = deleteNodeOfMinKeyFrom(currentRootNode.leftSubNode);

        // Ⅳ 对 执行了删除操作后的树 恢复“红黑树约束”，得到 “遵守 左倾红黑树所有约束 的树”（aka 红黑树）
        // 🐖 这是一个 从叶子节点到根结点 的过程，因此 放在递归调用代码的后面
        return fixMightBreaches(currentRootNode);
    }


    // 判断 沿着左脊的查询路径上 的下一个结点 是不是2-结点
    private boolean incomingNodeIsA2NodeInLeftSpine(Node currentNode) {
        // 获取到 查询路径上的下一个结点
        Node incomingNode = currentNode.leftSubNode;
        // 判断 该节点 是不是一个 2-结点
        // 手段：只要它 不属于3-结点（”由红色的左链接所连接“的 两个物理结点），就可以证明 它是2-结点
        return !isRed(incomingNode) && !isRed(incomingNode.leftSubNode);
    }

    // 判断 沿着右脊的查询路径上 的下一个结点 是不是2-结点
    private boolean incomingNodeIsA2NodeInRightSpine(Node currentNode) {
        // 获取到 查询路径上的下一个结点
        Node incomingNode = currentNode.rightSubNode;
        // 判断 该红黑树中的当前节点 是不是 一个2-3树中的一个2-结点
        // 手段：在2-3树中，只要 指向它的链接 不是红色 & 它的左子链接 不是红色，就可以证明 它是2-3树中的2-结点
        return !isRed(incomingNode) && !isRed(incomingNode.leftSubNode);
    }

    // 删除掉 最小结点（红节点/叶子节点/3-结点or4-结点的一个内部节点）
    private Node performDeletion() {
        // 手段：返回null / 使用null 关联到 父结点上
        return null;
    }

    // 沿着 查询最小节点的路径 到达 左脊的底部
    private boolean reachToBottomOnLeftSpine(Node currentNode) {
        return currentNode.leftSubNode == null;
    }

    // 恢复红黑树的不变性 🐖 这是一套固定流程
    private Node fixMightBreaches(Node currentNode) {
        // assert (toMoveStepsToEndGridWithoutObstacles != null);

        // 需要被修复的三种情况：#1 红色右链接（左旋转）; #2 连续的红色左链接(右旋转); #3 红色的左右子结点（反转颜色）
        // 如果出现了 红色右链接...
        if (redRightSublinkExist(currentNode))
            // 则：把 红链接 移动到左边
            currentNode = rotateItsRedSubLinkToLeft(currentNode);
        // 如果出现了 连续的红色左链接...
        if (consecutiveRedLeftSublinksExist(currentNode))
            // 则：把 红链接 移动到右边
            currentNode = rotateItsRedSubLinkToRight(currentNode);
        // 如果出现了 红色的左链接 与 红色的右链接
        if (twoRedSubLinkExist(currentNode))
            // 则：翻转 结点的颜色
            flipColors(currentNode);

        // 维护“根结点中所记录的 ‘以此结点作为根结点’的 树 中的结点数量”
        currentNode.itsNodesAmount = pairAmountOf(currentNode.leftSubNode) + pairAmountOf(currentNode.rightSubNode) + 1;

        return currentNode;
    }

    private boolean twoRedSubLinkExist(Node currentNode) {
        return isRed(currentNode.leftSubNode) && isRed(currentNode.rightSubNode);
    }

    private boolean consecutiveRedLeftSublinksExist(Node currentNode) {
        return isRed(currentNode.leftSubNode) && isRed(currentNode.leftSubNode.leftSubNode);
    }

    private boolean redRightSublinkExist(Node currentNode) {
        return isRed(currentNode.rightSubNode)
                && !isRed(currentNode.leftSubNode); // 这个条件是Optional的吗？
    }

    // #1 把 当前结点上的红链接 沿着查询路径 向下移动（default approach）
    // #2 或者 把 红链接 从右孙子 移动到 左孙子（如果有的话）
    private Node introduceRedLinkIntoMinPath(Node currentNode) { // moveRedLeft
        // 手段#1（默认操作）：翻转 当前节点 及 其子节点的颜色 来 为minPath中引入红链接
        // 🐖 由于所维护的不变性，因此 当前节点h 必然是 红节点。
        defaultApproach(currentNode);

        // 手段#2：如果可能，从 incoming结点 的兄弟结点中，借一个结点，得到一个 3-结点
        // ① 获取 minPath路径上的 incoming结点的兄弟结点 aka “当前节点的右子结点”;
        Node siblingNodeOfIncomingNode = currentNode.rightSubNode;
        if (isNot2Node(siblingNodeOfIncomingNode)) {
            // 如果 后继节点的兄弟节点 是一个 非2-结点, 说明 兄弟节点处存在有一个 红色的左链接，
            // 则：把 该3-节点的红色左链接 给借到 minPath中 来 维护 路径中当前节点不会是2-节点 的不变性
            currentNode = borrowRedFromSiblingToMinPath(currentNode);
        }

        // 返回 “按需移动红链接”后的当前节点
        return currentNode;
    }

    private Node borrowRedFromSiblingToMinPath(Node currentNode) {
        return moveRedLinkToLeftSpine(currentNode);
    }

    // 🐖 这是 为路径中引入红链接 的默认手段
    private void defaultApproach(Node currentNode) {
        // 做法：翻转 当前节点的颜色；
        // 作用：维护了 "查询路径上的当前节点 不会是2-结点" 的不变性
        // 副作用：引入了 红色的右链接 - 这在LLRBTree中是不合法的
        flipColors(currentNode);
    }

    // 把当前子树 右脊中的红链接 移动到 左脊上
    private Node moveRedLinkToLeftSpine(Node currentNode) {
        // #1 右旋转 当前结点的右子结点 来 在右脊上产生 连续的红色链接; / -> \
        currentNode.rightSubNode = rotateItsRedSubLinkToRight(currentNode.rightSubNode);
        // #2 左旋转 当前节点 来 在左脊上产生 连续的红色链接;
        currentNode = rotateItsRedSubLinkToLeft(currentNode);
        // #3 翻转 当前节点的颜色 来 只保留 左脊上第二层的红链接 & 恢复 当前结点的颜色 为红色；
        flipColors(currentNode);

        // 从结果上看，相当于把 右孙子的红链接 移动到 左孙子上（从sibling借红链接）
        return currentNode;
    }


    // 为maxPath 引入 红链接
    private Node introduceRedLinkIntoMaxPath(Node currentNode) {
        // 手段#1（默认操作）：翻转 当前节点的颜色
        // 作用：强行 为maxPath路径中 引入红链接
        // 副作用：导致了 红色的右链接
        defaultApproach(currentNode);

        // 手段#2：根据需要，从 incoming结点的兄弟结点中，借一个结点，得到一个3-结点
        // ① 获取 maxPath路径上的 incoming结点的兄弟结点 aka “当前节点的左子结点”;
        Node siblingNodeOfIncomingNode = currentNode.leftSubNode;
        // ② 如果 该兄弟结点是一个非2-结点, 说明 存在有红色的左链接可以借用，
        if (isTheUpperNodeOf3Node(siblingNodeOfIncomingNode)) {
            // 则：把该左链接 借用到 maxPath中
            // 从2-3树的角度来说，相当于 从sibling node中借一个键值，得到一个3-结点 - 从而维护了 “路径中的当前结点不是2-结点”的不变性
            currentNode = borrowRedFromSiblingToMaxPath(currentNode);
        }

        // 返回 “按需移动红链接”后 的当前节点
        return currentNode;
    }

    private Node borrowRedFromSiblingToMaxPath(Node currentNode) {
        // 把 兄弟结点的左子红链接 移动到 incoming结点的右子链接上
        return moveRedLinkToRightSpine(currentNode);
    }

    private Node moveRedLinkToRightSpine(Node currentNode) {
        // step#1 右旋转当前结点 来 在右脊上产生连续的红色链接;
        currentNode = rotateItsRedSubLinkToRight(currentNode);

        // step#2 翻转当前结点的颜色 来 只保留右脊上第二层的红链接 + 恢复当前结点的颜色为红色;
        flipColors(currentNode);

        // 🐖 从结果上看（在查询路径上产生了一个右链接 在2-3-4树中，等同于一个3-结点）
        // 相当于把 左孙子的红链接 移动到 右孙子上（从sibling借红链接）
        return currentNode;
    }

    // 判断 “查询路径中下一个结点的sibling结点” 是不是一个非2-结点
    private boolean isNot2Node(Node currentNode) {
        // 判断其左链接(左子结点)是不是红色 - 如果是，则为非2-结点。如果不是，则为2-结点
        return isRed(currentNode.leftSubNode);
    }

    /**
     * 从符号表中删除传入的key 及 其所关联的value
     *
     * @param passedKey the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void deletePairOf(Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to delete() is null");
        if (!doesContains(passedKey)) return;

        // 根据需要（下一级结点没有红节点），在查询路径中，手动引入一个红节点
        // 手段：把根结点设置为红色
        if (rootNodeHas2BlackChild())
            rootNode.color = RED;

        // 从 当前树中 删除 传入的key, 并把 删除后的结果 绑定回到 当前结点上
        rootNode = deleteNodeFrom(rootNode, passedKey);

        // 强制 把 根结点的颜色 设置为黑色
        if (!isEmpty()) rootNode.color = BLACK;
        // assert check();
    }

    // 如果是删除 随机的节点，如何能确定 查找路径呢？
    // 不变性 - 在查找路径中，不会出现2-结点
    private Node deleteNodeFrom(Node currentNode, Key passedKey) {

        // 如果 预期删除的节点 在左子树中，说明 删除操作会发生在左子树中, 则：执行 左子树中的递归删除 aka 删除最小结点的算法
        if (wantedNodeInLeftSpine(currentNode, passedKey)) {
            /* Ⅰ 为 minPath 引入 红链接 */
            // 如果 minPath上 出现了 2-结点，说明 我们需要 想办法为路径中 引入红链接，
            if (incomingNodeIsA2NodeInLeftSpine(currentNode))
                // 则：为 当前查询路径minPath 引入红链接
                currentNode = introduceRedLinkIntoMinPath(currentNode);

            /* Ⅱ 在minPath路径上 引入了红链接之后，声明式地删除（递归）“待删除结点” */
            // 手段：从 当前子树(左子树) 中 删除预期节点，并 把 删除结点后的左子树 重新绑定回到 左子树上
            currentNode.leftSubNode = deleteNodeFrom(currentNode.leftSubNode, passedKey);
        } else { // 如果 预期删除的结点 在右子树中 或者 与当前结点相等，说明 删除操作 发生在 当前节点 或 右子树中，
            // 则：执行 右子树中的递归删除 或者 对当前节点的删除

            /* Ⅰ 保证不变性：向下查找过程中，路径的当前节点不会是 2-结点；*/
            // Ⅰ-①： 如果 当前节点 是 红黑树中标准的3-结点，说明 存在有红色的左链接 可以引入到 当前路径中，
            if (isTheUpperNodeOf3Node(currentNode))
                // 则：把 红色的左链接 推到右边 - 具体做法：右旋转 当前节点
                // 原因👆：避免删除了一个黑节点 这会导致黑高的失衡
                currentNode = rotateItsRedSubLinkToRight(currentNode);

            /* Ⅱ 删除结点 */
            // Ⅱ-①：在 继续 “在查询路径中引入红链接” 之前，先判断 查询是不是已经到了 树的底部
            // 如果 在此找到了 预期删除的结点，并且 目标节点的右子树为null（特殊的节点位置），说明 可以直接删除（而不用 借助后继节点进行删除），
            if (findTheTargetWithoutRightChild(currentNode, passedKey))
                // 则：返回null 来 直接“物理删除”结点
                // 🐖 这里的删除 不同于BST中同情形下的删除(返回左子树)，因此 它会留下一个断链，等待 fixBreaches()对其进行修复
                return performDeletion();

            // Ⅰ-②： 如果 在查询路径上 出现了2-节点，说明 我们需要想办法 为路径中引入红链接，
            if (incomingNodeIsA2NodeInRightSpine(currentNode))
                // 则：为 当前查询路径maxPath 引入红链接
                currentNode = introduceRedLinkIntoMaxPath(currentNode);

            // Ⅱ-②（相对于deleteFromMax()是新增的步骤）：在继续 “递归地在当前子树（右子树）中查找” 之前，先判断 当前结点的key 与 传入的key 是否相等
            // 如果 当前节点 就是 待删除的结点（一般性的位置），说明 需要借助后继节点进行删除
            if (findTheTarget(currentNode, passedKey)) {
                // 则：借助“后继结点的方式” 来 实现物理删除; - 类似于BST中的删除
                deleteViaReplaceWithSuccessor(currentNode);
            }

            // Ⅱ-③：如果 当前节点 并不是 待删除的结点，说明 待删除节点 存在于右子树中，
            // 则：在 当前子树（右子树）中 来 继续查询 并 “声明式删除”预期的结点
            else currentNode.rightSubNode = deleteNodeFrom(currentNode.rightSubNode, passedKey);
        }

        // Ⅲ 删除结点后，修复 所有可能引入的 ① 红色右链接 + ② 4-结点
        return fixMightBreaches(currentNode);
    }

    // 在红黑树中 删除 任意位置的结点 - 手段：简化为 “删除最小结点”的操作
    private void deleteViaReplaceWithSuccessor(Node currentNode) {
        // Ⅰ 找到 当前节点 右子树中的最小结点，作为 ”后继结点“
        Node successorNode = findNodeWithMinKey(currentNode.rightSubNode);
        // Ⅱ 使用 后继结点的键值 来 更新当前节点(它的左右子链接&指向它的链接 都没有变化)
        currentNode.key = successorNode.key;
        currentNode.value = successorNode.value;
        // Ⅲ 从右子树中 删除最小结点（后继结点）， 并 把 删除结点后的子树 重新绑定到 当前节点的右子树上
        currentNode.rightSubNode = deleteNodeOfMinKeyFrom(currentNode.rightSubNode);
    }

    private boolean findTheTargetWithoutRightChild(Node currentNode, Key passedKey) {
        return findTheTarget(currentNode, passedKey)
                && reachToBottomOnRightSpine(currentNode);
    }

    private boolean findTheTarget(Node currentNode, Key passedKey) {
        return passedKey.compareTo(currentNode.key) == 0;
    }

    private boolean wantedNodeInLeftSpine(Node currentNode, Key passedKey) {
        return passedKey.compareTo(currentNode.key) < 0;
    }

    /***************************************************************************
     *  Red-black tree helper functions.
     *************************************************************************
     * @return*/

    // 使一个 右倾的链接 变成 左倾  aka 左旋转 右倾红链接
    private Node rotateItsRedSubLinkToLeft(Node currentNode) {
        assert (currentNode != null) && isRed(currentNode.rightSubNode);

        // #1 结构上的变更：
        Node replacerNode = currentNode.rightSubNode; // 获取到替换结点
        currentNode.rightSubNode = replacerNode.leftSubNode; // 更新 当前节点的右链接（先断开，再连接）
        replacerNode.leftSubNode = currentNode; // 更新替换结点的左链接（先断开，再连接）

        // #2 颜色上的变更：替换结点（更新为当前结点的颜色） & 当前结点（更新为红色）
        replacerNode.color = currentNode.color;
        currentNode.color = RED;

        // #3 子树中的结点数量的变更
        replacerNode.itsNodesAmount = currentNode.itsNodesAmount;
        currentNode.itsNodesAmount = pairAmountOf(currentNode.leftSubNode) + pairAmountOf(currentNode.rightSubNode) + 1;

        return replacerNode;
    }

    // 使一个 左倾的链接 变成右倾 - 右旋转 左倾红链接
    private Node rotateItsRedSubLinkToRight(Node currentNode) {
        assert (currentNode != null) && isRed(currentNode.leftSubNode);

        // #1 结构上的变更
        // 获取到 用于替换当前节点位置的节点
        Node replacerNode = currentNode.leftSubNode;
        // 重建 当前节点的左链接 与 替换节点的右链接
        currentNode.leftSubNode = replacerNode.rightSubNode;
        replacerNode.rightSubNode = currentNode;

        // #2 颜色上的变更
        // 替换节点的颜色 保留为 当前节点的颜色
        replacerNode.color = currentNode.color;
        // 旋转后，当前节点仍旧是一个红节点，只是倾斜方向变化
        currentNode.color = RED;

        // #3 子树中结点数量的变更（替换节点&当前结点）
        replacerNode.itsNodesAmount = currentNode.itsNodesAmount; // “替换结点”子树中的结点数量 与 “当前结点”中的结点数量相同
        currentNode.itsNodesAmount = pairAmountOf(currentNode.leftSubNode) + pairAmountOf(currentNode.rightSubNode) + 1;

        // 返回替换节点
        return replacerNode;
    }

    // 反转 当前结点&它的左右子节点 的颜色
    private void flipColors(Node currentNode) {
        // 把 当前结点 & 它的左右子节点 的颜色 变更为 “与当前颜色不同的另一种颜色”
        currentNode.color = !currentNode.color;
        currentNode.leftSubNode.color = !currentNode.leftSubNode.color;
        currentNode.rightSubNode.color = !currentNode.rightSubNode.color;
    }


    /***************************************************************************
     *  Utility functions.
     ***************************************************************************/

    // 返回二叉搜索树的高度 - 1-结点构成的树高度为0
    public int heightOfRBTree() {
        return heightOf(rootNode);
    }

    private int heightOf(Node currentNode) {
        if (currentNode == null) return -1;
        return 1 + Math.max(heightOf(currentNode.leftSubNode), heightOf(currentNode.rightSubNode));
    }

    /***************************************************************************
     *  Ordered symbol table methods. 与有序性相关的符号表方法
     ***************************************************************************/

    /**
     * 返回符号表中最小的键
     *
     * @return 符号表中最小的key
     * @throws NoSuchElementException 如果符号表为空
     */
    public Key getMinKey() {
        if (isEmpty()) throw new NoSuchElementException("calls min() with empty symbol table");
        return findNodeWithMinKey(rootNode).key;
    }

    // 返回 当前树中的最小结点 手段：沿着左脊 一路向下查找，直到 某个结点的leftSubNode 为null（即找到了最小结点）
    private Node findNodeWithMinKey(Node currentNode) {
        // assert x != null;
        if (currentNode.leftSubNode == null) return currentNode;
        else return findNodeWithMinKey(currentNode.leftSubNode);
    }

    /**
     * 返回符号表中的最大键
     *
     * @return 符号表中最大的key
     * @throws NoSuchElementException 如果符号表为空
     */
    public Key getMaxKey() {
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
     * 返回符号表中 小于等于 传入的key的最大的key
     *
     * @param passedKey 指定的key
     * @return 符号表中小于或等于 指定key的最大key
     * @throws NoSuchElementException   如果该key不存在
     * @throws IllegalArgumentException 如果传入的key是null
     */
    public Key getFlooredKeyOf(Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to floor() is null");
        if (isEmpty()) throw new NoSuchElementException("calls floor() with empty symbol table");

        // 从 当前的结点树 中 查找 满足条件的结点
        Node flooredNode = getNodeOfFlooredKeyFrom(rootNode, passedKey);

        // 根据 具体的查询结果 决定返回值 或者 抛出异常
        if (flooredNode == null) throw new NoSuchElementException("argument to floor() is too small");
        else return flooredNode.key;
    }

    // 返回当前结点树（以当前节点作为根结点的树）中，小于等于传入key的最大key
    private Node getNodeOfFlooredKeyFrom(Node currentNode, Key passedKey) {
        // 如果查询过程递归到叶子节点，说明没有找到满足条件的结点，则：返回null 表示查找未成功
        if (currentNode == null) return null;

        int result = passedKey.compareTo(currentNode.key);
        if (result == 0) return currentNode;
        if (result < 0) return getNodeOfFlooredKeyFrom(currentNode.leftSubNode, passedKey);

        // 如果 passedKey 大于 当前节点中的key，则有两种可能：#1 满足条件的结点再右子树中； #2 满足条件的结点就是当前节点
        Node flooredNode = getNodeOfFlooredKeyFrom(currentNode.rightSubNode, passedKey);
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
     * @throws NoSuchElementException   如果符号表中不存在该key
     * @throws IllegalArgumentException 如果传入的key为null
     */
    public Key getCeilingKeyOf(Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to ceiling() is null");
        if (isEmpty()) throw new NoSuchElementException("calls ceiling() with empty symbol table");

        // 从结点树中，查找到大于等于passedKey的最小结点
        Node ceiledNode = getNodeOfCeilingKeyFrom(rootNode, passedKey);

        if (ceiledNode == null) throw new NoSuchElementException("argument to ceiling() is too small");
        else return ceiledNode.key;
    }

    // 返回结点树中 大于等于passedKey的最小结点
    private Node getNodeOfCeilingKeyFrom(Node currentNode, Key passedKey) {
        // 如果查询持续到了叶子节点，说明不存在满足条件的结点，则：返回null - 表示没有查询到满足条件的结点
        if (currentNode == null) return null;

        // 比较passedKey 与 当前节点中的key
        int result = passedKey.compareTo(currentNode.key);
        if (result == 0) return currentNode;
        if (result > 0) return getNodeOfCeilingKeyFrom(currentNode.rightSubNode, passedKey);

        // 如果passedKey小于currentNode.key，有两种情况：
        Node ceiledNode = getNodeOfCeilingKeyFrom(currentNode.leftSubNode, passedKey);

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
     * @param passedRanking 指定的排名次序
     * @return the key in the symbol table of given {@code rank} 在符号表中指定排名的key
     * @throws IllegalArgumentException 如果传入的rank 不在[0, n-1]区间内
     */
    public Key selectOutKeyOf(int passedRanking) {
        if (passedRanking < 0 || passedRanking >= pairAmount()) {
            throw new IllegalArgumentException("argument to select() is invalid: " + passedRanking);
        }

        // 在结点树中，找到指定排名的结点
        return selectOutKeyFrom(rootNode, passedRanking);
    }

    // 返回结点树中，指定排名的结点的key
    // 先决条件：传入的排名在一个合法的范围内 - 🐖 排名从0开始 0th
    private Key selectOutKeyFrom(Node currentNode, int passedRanking) {
        // 如果查找过程递归到了叶子节点，说明查找满足条件的结点失败，则：返回null
        if (currentNode == null) return null;

        // 获取左子树中的结点数量 - 原理：二叉查找树中结点的有序性
        int leftSize = pairAmountOf(currentNode.leftSubNode);
        // 如果 左子树中的结点数量 比起 传入的排名更大，则：满足条件的结点必然在左子树中 递归地在左子树中继续查找满足条件的结点
        if (leftSize > passedRanking) return selectOutKeyFrom(currentNode.leftSubNode, passedRanking);
            // 如果 左子树中的结点数量 比起 传入的排名小，则：满足条件的结点必然在右子树中 递归地在右子树中继续查找满足条件的结点
            // 🐖 由于左子树与根结点都已经占据了排名，所以在右子树中需要查找的是 排名为 passedRanking - leftSize -1 的键
        else if (leftSize < passedRanking)
            return selectOutKeyFrom(currentNode.rightSubNode, passedRanking - leftSize - 1);
        else return currentNode.key;
    }

    /**
     * 返回符号表中，所有严格小于 passedKey的键的总数量
     *
     * @param passedKey 传入的key
     * @return 符号表中 严格小于 指定的key的 键的数量
     * @throws IllegalArgumentException 如果传入的key是null
     */
    public int rankingOf(Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to rank() is null");

        // 获取到在结点树中，passedKey的排名 🐖 排名从0开始 0-th
        return rankingIn(passedKey, rootNode);
    }

    // 在结点树中，小于passedKey的结点的数量
    private int rankingIn(Key passedKey, Node currentNode) {
        // 如果没有查找到满足条件的结点，则：返回0 表示不存在这样的结点
        if (currentNode == null) return 0;

        // 比较 passedKey 与 currentNode.key
        int result = passedKey.compareTo(currentNode.key);
        // 如果 passedKey更小，说明它一定在左子树的范围中，则：在左子树中递归地查找并返回它的排名
        if (result < 0) return rankingIn(passedKey, currentNode.leftSubNode);
            // 如果更大，说明它在右子树的范围中，则：在右子树中递归地查找并返回它的排名
        else if (result > 0)
            return 1 + pairAmountOf(currentNode.leftSubNode) + rankingIn(passedKey, currentNode.rightSubNode);
            // 如果相等，则：左子树中结点的数量 就是 它的排名 - 排名从0-th开始
        else return pairAmountOf(currentNode.leftSubNode);
    }

    /***************************************************************************
     *  Range count and range search.
     ***************************************************************************/

    /**
     * 以一个Iterable的形式 来 返回符号表中所有的key
     * 如果想要遍历st符号表中的所有的键，可以使用 foreach的标记语法  for (Key key : st.keys())
     *
     * @return 以一个可迭代的形式返回 符号表中所有的key
     */
    public Iterable<Key> getIterableKeys() {
        if (isEmpty()) return new Queue<Key>();
        return getIterableKeysBetween(getMinKey(), getMaxKey());
    }

    /**
     * 以Iterable的方式 来 返回符号表中 指定范围（左右闭区间）内的键
     *
     * @param leftBarKey  最小端点
     * @param rightBarKey 最大端点
     * @return 以一个可迭代的形式 来 返回符号表中 [lo, hi]区间内 所有的key
     * @throws IllegalArgumentException 如果lo或者hi中的任意一个为null
     */
    public Iterable<Key> getIterableKeysBetween(Key leftBarKey, Key rightBarKey) {
        if (leftBarKey == null) throw new IllegalArgumentException("first argument to keys() is null");
        if (rightBarKey == null) throw new IllegalArgumentException("second argument to keys() is null");

        // 🐖：这里只需要一个可迭代的集合类型，不一定要是队列
        Queue<Key> keysQueue = new Queue<Key>();
        // if (isEmpty() || lo.compareTo(hi) > 0) return keysQueue;
        collectKeysBetweenRangeInto(rootNode, keysQueue, leftBarKey, rightBarKey);

        return keysQueue;
    }

    // 把结点树中，在[leftBarKey, rightBarKey]区间内的所有键 都添加到队列中
    private void collectKeysBetweenRangeInto(Node currentNode, Queue<Key> keysQueue, Key leftBarKey, Key rightBarKey) {
        // 查询过程结果，直接return
        if (currentNode == null) return;

        // 比较左边界 与 当前节点中的key
        int leftResult = leftBarKey.compareTo(currentNode.key);
        // 比较右边界 与 当前结点中的key
        int rightResult = rightBarKey.compareTo(currentNode.key);

        // 如果左边界小于根结点（说明区间覆盖左子树），则：把左子树中满足条件的key添加到队列中
        if (leftResult < 0) collectKeysBetweenRangeInto(currentNode.leftSubNode, keysQueue, leftBarKey, rightBarKey);
        // 如果左边界小于等于根结点&右边界大于等于根结点(说明区间包含当前节点)，则：把当前结点添加到队列中
        if (leftResult <= 0 && rightResult >= 0) keysQueue.enqueue(currentNode.key);
        // 如果右边界大于根结点(说明区间覆盖右子树)，则：把当前结点添加到队列中
        if (rightResult > 0) collectKeysBetweenRangeInto(currentNode.rightSubNode, keysQueue, leftBarKey, rightBarKey);
    }

    /**
     * 返回符号表中指定范围内（左右闭区间）所有键的总数量
     *
     * @param leftBarKey  最小端点
     * @param rightBarKey 最大端点
     * @return 符号表区间[lo, hi]之间的所有键的数量
     * @throws IllegalArgumentException 如果lo或者hi中的任一个为null
     */
    public int pairAmountBetween(Key leftBarKey, Key rightBarKey) {
        if (leftBarKey == null) throw new IllegalArgumentException("first argument to itsNodesAmount() is null");
        if (rightBarKey == null) throw new IllegalArgumentException("second argument to itsNodesAmount() is null");

        // 如果区间无效，则：返回0 表示此区间内不存在满足条件的键
        if (leftBarKey.compareTo(rightBarKey) > 0) return 0;
        // 公式： 区间中所包含的键 = 右边界的排名 - 左边界的排名 (+1)
        // 🐖 如果右边界在树中存在的话，则：满足条件的键的数量 + 1
        if (doesContains(rightBarKey)) return rankingOf(rightBarKey) - rankingOf(leftBarKey) + 1;
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
        if (currentNode.itsNodesAmount != pairAmountOf(currentNode.leftSubNode) + pairAmountOf(currentNode.rightSubNode) + 1)
            return false;
        return isSizeConsistent(currentNode.leftSubNode) && isSizeConsistent(currentNode.rightSubNode);
    }

    // 检查 排名是否是consistent的？
    private boolean isRankConsistent() {
        // 选择出指定排名的元素，其在树中的排名 恒等于 最开始选择的排名 （循环成立）
        for (int currentRanking = 0; currentRanking < pairAmount(); currentRanking++)
            if (currentRanking != rankingOf(selectOutKeyOf(currentRanking))) return false;

        // 使用指定键在树中的排名，来在树中选择相同排名的元素。 得到的必然是 最开始指定的元素（循环成立）
        for (Key currentKey : getIterableKeys())
            if (currentKey.compareTo(selectOutKeyOf(rankingOf(currentKey))) != 0) return false;

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
     * 红黑树符号表的单元测试
     *
     * @param args the command-line arguments 命令行参数
     * 问题：对main()函数来说，函数体中 并没有使用到 args这个参数，为什么还要设置 这个形式参数呢？
     */
    public static void main(String[] args) {
        // 创建一个符号表对象（红黑树只是实现方式，符号表才是最终目的）
        RedBlackTreeSymbolTable<String, Integer> symbolTable = new RedBlackTreeSymbolTable<String, Integer>();

        // 从输入流中读取字符串作为key，并在符号表中 建立起 currentKey -> currentSpot（从0开始）的映射
        for (int currentSpot = 0; !StdIn.isEmpty(); currentSpot++) {
            String currentKey = StdIn.readString();
            symbolTable.putInPairOf(currentKey, currentSpot);
        }

        // 打印空行
        StdOut.println();

        // 遍历并打印出 符号表中所有的 键值对
        for (String currentKey : symbolTable.getIterableKeys())
            StdOut.println(currentKey + " " + symbolTable.getAssociatedValueOf(currentKey));

        StdOut.println();
    }
}