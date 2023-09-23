package com.henry.symbol_table_chapter_03.implementation_02.advanced.via_binary_search_tree;
/******************************************************************************
 *  Compilation:  javac BinarySearchTreeSymbolTable.java
 *  Execution:    java BinarySearchTreeSymbolTable
 *  Dependencies: StdIn.java StdOut.java Queue.java
 *  Data files:   https://algs4.cs.princeton.edu/32bst/tinyST.txt  
 *
 *  A symbol table implemented with a binary search tree.
 *
 *  % more tinyST.txt
 *  S E A R C H E X A M P L E
 *
 *  % java BinarySearchTreeSymbolTable < tinyST.txt
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

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

// 验证：可以使用二叉搜索树 作为底层数据结构 来 实现符号表
// 手段：使用BST中的节点 来 封装key -> value的映射
// 特征：使用BST能够得到较好的 get(), put()操作性能
public class BinarySearchTreeSymbolTable<Key extends Comparable<Key>, Value> {
    private Node rootNode;             // 二叉查找树的根结点

    private class Node {
        private Key key;           // 结点的key - 二叉查找树的排序依据就是结点中的key
        private Value value;         // 与key相关联的值
        private Node leftSubTree, rightSubTree;  // 当前结点的左右子节点/子树
        private int itsNodesAmount;          // 以当前节点为根结点的二叉树中的结点数量

        public Node(Key passedKey, Value associatedValue, int itsNodesAmount) {
            this.key = passedKey;
            this.value = associatedValue;
            this.itsNodesAmount = itsNodesAmount;
        }
    }

    /**
     * 初始化一个空的符号表
     */
    public BinarySearchTreeSymbolTable() {
    }

    // ~~ GET, PUT, DELETE ~~
    /**
     * 在符号表中查找传入的键，并返回 其所关联的值。
     * 如果符号表中不存在传入的键，则：返回null
     *
     * @param passedKey
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Value getAssociatedValueOf(Key passedKey) {
        return getAssociatedValueFrom(rootNode, passedKey);
    }

    /**
     * 在二叉查找树中，查找传入的key
     * 如果命中，则：返回key所对应的值。
     * 如果未命中，则：返回null
     */
    private Value getAssociatedValueFrom(Node currentNode, Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("calls get() with a null key");

        // 递归的终结条件 - 查找过程结束于一个空链接/空结点
        if (currentNode == null) return null;

        // 本级递归要做的事情：1 把树分解为根节点 + 左子树 + 右子树； 2 判断根节点是不是预期的节点； 3 如果不是，从左右子树上执行查找，并返回值
        // 根据 传入的key 与 当前二叉树的根结点的key的比较结果 来 返回关联的value(如果key相同) 或者 在对应的子树中继续递归查找
        int result = passedKey.compareTo(currentNode.key);
        if (result < 0) return getAssociatedValueFrom(currentNode.leftSubTree, passedKey);
        else if (result > 0) return getAssociatedValueFrom(currentNode.rightSubTree, passedKey);
        else return currentNode.value;
    }

    /**
     * 向符号表中插入传入的键值对
     * 如果符号表中存在有相同大的键，则：覆盖其所对应的值
     * 如果传入的值是null，则：从符号表中删除传入的键（以及关联的值）
     *
     * @param passedKey       the key
     * @param associatedValue the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void putInPairOf(Key passedKey, Value associatedValue) {
        if (passedKey == null) throw new IllegalArgumentException("calls put() with a null key");
        // #case01 传入的value是null, 则：执行删除
        if (associatedValue == null) {
            deletePairOf(passedKey);
            return;
        }

        // 向二叉查找树rootNode 中插入 key-value pair
        rootNode = putPairInto(rootNode, passedKey, associatedValue);
        assert checkIfUnderlyingMemberCorrect();
    }

    // 🐖 插入的过程 与 查找的过程十分类似 - 插入前，需要先查找
    private Node putPairInto(Node currentNode, Key passedKey, Value associatedValue) {
        // 递归终结条件：查询结束于一个空结点/链接
        // 则：为传入的键值对创建一个新结点，并返回 以 链接到父节点上（重置搜索路径上指向结点的链接）
        if (currentNode == null)
            return new Node(passedKey, associatedValue, 1);

        // 重置 搜索路径上的所有的 父节点指向子节点的链接（aka 左右链接）
        // 手段：node.leftNode = xxx; node.rightNode = ooo;
        int result = passedKey.compareTo(currentNode.key);
        if (result < 0) // 向左子树中插入键值对，并使用插入后的子树 来 更新左子树
            currentNode.leftSubTree = putPairInto(currentNode.leftSubTree, passedKey, associatedValue);
        else if (result > 0) // 向右子树中插入键值对，并使用插入后的子树 来 更新右子树
            currentNode.rightSubTree = putPairInto(currentNode.rightSubTree, passedKey, associatedValue);
        else currentNode.value = associatedValue; // 如果根节点的key 与 传入的key相同，则：更新结点中的value

        // 更新搜索路径中每个结点的 计数器 - 🐖 如果新增了结点，则：搜索路径上的每个结点的结点计数器都要+1
        // 手段：使用一个通用的恒等式 👇
        currentNode.itsNodesAmount = 1 + nodeAmountOf(currentNode.leftSubTree) + nodeAmountOf(currentNode.rightSubTree);
        return currentNode;
    }

    /**
     * 从符号表中删除传入的key & 它所关联的value（如果key存在于符号表中的话）
     * <p>
     * 如果传入的key为null 则抛出 非法参数异常
     *
     * @param passedKey
     */
    public void deletePairOf(Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("calls delete() with a null key");
        rootNode = deletePairFrom(rootNode, passedKey);
        assert checkIfUnderlyingMemberCorrect();
    }

    // 从二叉查找树中删除 传入的key
    private Node deletePairFrom(Node currentNode, Key passedKey) {
        // 递归中终结条件 - 对传入的key的查询 结束于一个空结点(也就是没有找到它)，则：返回null 表示查询未命中
        if (currentNode == null) return null;

        // 比较 传入的key 与 当前二叉树的根结点中的key
        int result = passedKey.compareTo(currentNode.key);

        // 如果传入的key 比 当前二叉树的根结点中的key更小，说明 传入的key在左子树中（假如存在的话）
        if (result < 0)
            // 则：从左子树中删除结点 & “使用删除结点后的子树 来 更新指向原始子树的链接”
            currentNode.leftSubTree = deletePairFrom(currentNode.leftSubTree, passedKey);
            // 如果更大，说明 传入的key在右子树中（假如存在的话）
        else if (result > 0)
            // 则：从右子树中删除节点 & 使用“删除节点后的子树” 来 更新指向原始子树的连接
            currentNode.rightSubTree = deletePairFrom(currentNode.rightSubTree, passedKey);
            // 如果相等，说明 传入的key 就是根结点中的key
        else {
            // 则：删除根结点(当前结点)
            /* 当根结点(当前结点)有两个子节点时，删除结点后，会有两个链接无处attach。但是父结点上只会有一个空链接可用 该怎么办？
                高层手段（Hibbard）：使用 被删除结点的后继结点(successor) 来 填补/替换 被删除结点的位置
                原理：在二叉树中的任何一个结点，都会有一个指向它的链接 & 两个从它指出的链接 - 比喻：挖东墙，补西墙。
                难点：选择的后继结点 替换 被删除的结点后，整棵二叉搜索树仍旧能够遵守 BST的数值约束。
                具体手段：这里选择的后继结点 是 “待删除结点的右子树中的最小结点”。
                    因为从BST数值约束的角度来说，它可以作为 待删除的原始结点的平替(replacement)
                具体做法：
                    #1 把 successor结点 作为 当前结点；
                    #2 更新 当前结点的左右链接；
                    #3 返回 当前结点 来 更新“指向当前结点的链接”
            * */
            // #case01 右子树为空
            if (currentNode.rightSubTree == null) return currentNode.leftSubTree;
            // #case02 左子树为空
            if (currentNode.leftSubTree == null) return currentNode.rightSubTree;

            // 为当前结点添加一个引用 - 用于记录原始结点，从而在需要的时候 用它来获取到原始结点的左右结点
            Node originalNode = currentNode;
            // #1 获取原始结点 右子树中的最小结点 & 并 将之作为当前结点
            currentNode = nodeOfMinKeyFrom(originalNode.rightSubTree);
            // #2 设置当前结点的左右子树
            // 手段：对于右子树，使用“删除结点后的右子树”来更新指向右子树的链接
            currentNode.rightSubTree = deletePairOfMinKeyFrom(originalNode.rightSubTree);
            // 对于左子树，使用“原始节点的左子树” 来 更新指向“当前结点左子树”的链接
            currentNode.leftSubTree = originalNode.leftSubTree;
        }

        // 更新当前二叉树根结点中的 结点计数器
        currentNode.itsNodesAmount = nodeAmountOf(currentNode.leftSubTree) + nodeAmountOf(currentNode.rightSubTree) + 1;

        // 返回“当前结点” 来 连接到 父结点上
        return currentNode;
    }


    // 返回 二叉查找树中的结点数量（键值对数量）
    private int nodeAmountOf(Node currentNode) {
        if (currentNode == null) return 0;
        else return currentNode.itsNodesAmount;
    }

    /**
     * 符号表中是否包含有传入的key？
     * <p>
     * 如果包含，则：返回true 否则返回false
     * 如果传入的key是null，则：抛出异常
     *
     * @param passedKey
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean doesContains(Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to contains() is null");
        return getAssociatedValueOf(passedKey) != null;
    }


    // ~~ MAX and MIN ~~
    /**
     * 从符号表中删除最小的key & 它所关联的值
     * <p>
     * 如果符号表为空，则：抛出 NoSuchElementException异常
     */
    public void deletePairOfMinKey() {
        if (isEmpty()) throw new NoSuchElementException("Symbol table underflow");
        rootNode = deletePairOfMinKeyFrom(rootNode);
        assert checkIfUnderlyingMemberCorrect();
    }

    private Node deletePairOfMinKeyFrom(Node currentNode) {
        /* 原理：最小的key 在二叉查找树的左子树的左子节点中 */
        // #case01 左子树为空 - 说明最小结点就是根节点...
        if (currentNode.leftSubTree == null)
            // 则：直接删除根结点 - 手段：返回二叉查找树的右子树
            return currentNode.rightSubTree;
        // #case02 左子树不为空 - 说明最小节点在左子树中...
        // 则：从左子树中删除最小结点 & 使用删除结点后的子树 来 更新指向原始子树的链接
        currentNode.leftSubTree = deletePairOfMinKeyFrom(currentNode.leftSubTree);

        // 更新当前二叉树根结点中的 结点计数器 - 手段：使用恒等式
        currentNode.itsNodesAmount = nodeAmountOf(currentNode.leftSubTree) + nodeAmountOf(currentNode.rightSubTree) + 1;

        // 返回当前结点
        return currentNode;
    }

    /**
     * 从符号表中删除最大键 & 它所关联的值
     * 如果符号表为空，则 抛出 NoSuchElementException
     */
    public void deletePairOfMaxKey() {
        if (isEmpty()) throw new NoSuchElementException("Symbol table underflow");
        rootNode = deletePairOfMaxKeyFrom(rootNode);
        assert checkIfUnderlyingMemberCorrect();
    }

    private Node deletePairOfMaxKeyFrom(Node currentNode) {
        /* 原理：二叉查找树中的最大结点 在右子树的右子结点中 */
        // #case01 右子树为空，说明最大结点就是根结点...
        if (currentNode.rightSubTree == null)
            // 则：删除根结点      手段：直接返回当前二叉树的左子树
            return currentNode.leftSubTree;

        // #case02 右子树不为空, 说明最大节点在右子树中
        // 则：从右子树中删除最大结点 & “使用删除结点后的子树 来 更新指向原始子树的链接”
        currentNode.rightSubTree = deletePairOfMaxKeyFrom(currentNode.rightSubTree);

        // 更新当前二叉树根结点中的 结点计数器 - 手段：使用恒等式
        currentNode.itsNodesAmount = nodeAmountOf(currentNode.leftSubTree) + nodeAmountOf(currentNode.rightSubTree) + 1;

        // 返回当前结点
        return currentNode;
    }

    /**
     * 返回符号表中的最小键
     * 当符号表为空时，抛出 NoSuchElementException
     */
    public Key getMinKey() {
        if (isEmpty()) throw new NoSuchElementException("calls min() with empty symbol table");
        return nodeOfMinKeyFrom(rootNode).key;
    }

    private Node nodeOfMinKeyFrom(Node currentNode) {
        // 原理：二叉查找树中的最小结点一定是左子树中的左子结点
        // 手段：一直递归查找 二叉树的左子树，直到遇到左链接为null的结点即可 - 它就是最小的结点
        if (currentNode.leftSubTree == null) return currentNode;
        else return nodeOfMinKeyFrom(currentNode.leftSubTree);
    }

    /**
     * 返回符号表中的最大key
     * 如果符号表为空，则：抛出 没有这样的元素的异常
     */
    public Key getMaxKey() {
        if (isEmpty()) throw new NoSuchElementException("calls max() with empty symbol table");
        return getPairOfMaxKeyFrom(rootNode).key;
    }

    private Node getPairOfMaxKeyFrom(Node currentNode) { // 同理
        if (currentNode.rightSubTree == null) return currentNode;
        else return getPairOfMaxKeyFrom(currentNode.rightSubTree);
    }

    // ~~ FLOOR AND CEILING ~~
    /**
     * 返回符号表中 小于等于 传入key的最大的key
     * <p>
     * 如果传入的key不存在，则：抛出 元素不存在异常
     * 如果传入的key为null，则：抛出 非法参数异常
     *
     * @param passedKey
     */
    public Key getFlooredKeyOf(Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to floor() is null");
        if (isEmpty()) throw new NoSuchElementException("calls floor() with empty symbol table");

        Node nodeOfFlooredKey = getNodeOfFlooredKeyAgainst(rootNode, passedKey);
        if (nodeOfFlooredKey == null) throw new NoSuchElementException("argument to floor() is too small");
        else return nodeOfFlooredKey.key;
    }

    // 在 当前二叉树中找到 “小于等于传入key的最大结点” 👇
    private Node getNodeOfFlooredKeyAgainst(Node currentNode, Key passedKey) {
        // 递归终结条件 - 查找过程结束于空结点，说明 满足条件的结点在二叉树中不存在，返回null 来 表示“不存在”
        if (currentNode == null) return null;

        int result = passedKey.compareTo(currentNode.key);
        // 如果传入的key 刚好等于 当前二叉树的根结点key，则：根结点就是 小于等于传入key的最大结点
        if (result == 0) return currentNode;
        // 如果传入的key 比 当前二叉树的根结点key 更小，则：在左子树中继续查找，并返回查找结果
        if (result < 0) return getNodeOfFlooredKeyAgainst(currentNode.leftSubTree, passedKey);
        // 如果传入的key 比 当前二叉树的根结点key 更大，则：在右子树中继续查找
        Node foundNode = getNodeOfFlooredKeyAgainst(currentNode.rightSubTree, passedKey);
        // 如果在右子树中 查找到了 小于等于传入key的结点，则：返回查找到的结果
        if (foundNode != null) return foundNode;
            // 否则，说明在右子树中 不存在“小于等于key的结点”，则：返回当前二叉树的根结点（因为它就是 小于等于key的最大结点）
        else return currentNode;
    }

    // TODO what is this for?
    public Key floor2(Key passedKey) {
        Key foundKey = floor2(rootNode, passedKey, null);
        if (foundKey == null) throw new NoSuchElementException("argument to floor() is too small");
        else return foundKey;

    }

    private Key floor2(Node currentNode, Key passedKey, Key best) {
        if (currentNode == null) return best;
        int result = passedKey.compareTo(currentNode.key);
        if (result < 0) return floor2(currentNode.leftSubTree, passedKey, best);
        else if (result > 0) return floor2(currentNode.rightSubTree, passedKey, currentNode.key);
        else return currentNode.key;
    }

    /**
     * 返回符号表中 大于等于传入key的最小key
     *
     * @param passedKey
     * @throws NoSuchElementException   if there is no such key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Key getCeilingKeyOf(Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to ceiling() is null");
        if (isEmpty()) throw new NoSuchElementException("calls ceiling() with empty symbol table");
        Node foundNode = getNodeOfCeilingKeyAgainst(rootNode, passedKey);

        if (foundNode == null) throw new NoSuchElementException("argument to floor() is too large");
        else return foundNode.key;
    }

    // 在当前二叉树中，查找 大于等于传入key的最小结点
    private Node getNodeOfCeilingKeyAgainst(Node currentNode, Key passedKey) {
        // 查找结束于空结点，说明没找见
        if (currentNode == null) return null;

        int result = passedKey.compareTo(currentNode.key);
        // 找到了 等于传入key的结点 - 它就是 “大于等于传入key”的最小结点
        if (result == 0) return currentNode;
        // 在左子树中查找 “大于等于传入key的结点”
        if (result < 0) {
            Node foundCeilingNode = getNodeOfCeilingKeyAgainst(currentNode.leftSubTree, passedKey);
            // 如果在左子树中找到了“大于等于传入key的结点”，则：返回所找到的结点
            if (foundCeilingNode != null) return foundCeilingNode;
            else // 如果没有找到，说明 当前二叉树的根结点就是 “大于等于传入key的最大结点”，则：返回它
                return currentNode;
        }
        return getNodeOfCeilingKeyAgainst(currentNode.rightSubTree, passedKey);
    }

    // ~~ SELECT & RANK ~~
    /**
     * 返回符号表中 传入的排名 所对应的键。
     * 这个key存在有如下性质：在符号表中存在有 rank个key都小于它。
     * 换句话说，这个key 是符号表中 第(rank+1)小的key
     *
     * @param passedRank the order statistic （排名）
     * @return 符号表中排名为rank的键
     * 如果传入的rank 不在 [0, n-1]之间，则 抛出 非法参数异常
     */
    public Key selectOutKeyOf(int passedRank) {
        if (passedRank < 0 || passedRank >= pairAmount()) {
            throw new IllegalArgumentException("argument to select() is invalid: " + passedRank);
        }
        return selectOutKeyFrom(rootNode, passedRank);
    }

    // 返回二叉搜索树中，指定排名的键
    // 前提条件：排名在合法的范围
    private Key selectOutKeyFrom(Node currentNode, int passedRank) {
        // 如果查找过程结束于空结点，说明 在二叉树中没有找到 传入的rank，则：返回null(约定)
        // 这个应该属于是防御性编程 - 如果传入的passedRank本身是合法的，则：应该总能找到对应的结点
        if (currentNode == null) return null;
        int leftTreeSize = nodeAmountOf(currentNode.leftSubTree);
        if (leftTreeSize > passedRank) return selectOutKeyFrom(currentNode.leftSubTree, passedRank);
        else if (leftTreeSize < passedRank) return selectOutKeyFrom(currentNode.rightSubTree, passedRank - leftTreeSize - 1);
        else return currentNode.key;
    }

    /**
     * 返回 符号表中 所有严格小于 传入的key的键的数量
     *
     * @param passedKey
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public int rankingOf(Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to rank() is null");
        return rankingIn(passedKey, rootNode);
    }

    // Number of keys in the subtree less than key.
    private int rankingIn(Key passedKey, Node currentNode) {
        if (currentNode == null) return 0;
        int result = passedKey.compareTo(currentNode.key);
        if (result < 0) return rankingIn(passedKey, currentNode.leftSubTree);
        else if (result > 0) return 1 + nodeAmountOf(currentNode.leftSubTree) + rankingIn(passedKey, currentNode.rightSubTree);
        else return nodeAmountOf(currentNode.leftSubTree);
    }

    // ~~ ITERABLE ~~
    /**
     * 以Iterable的方式 来 返回符号表中所有的key所组成的集合
     * 为了遍历 st符号表中所有的key，可以使用foreach标记语法： for(Key key : st.keys()) {...}
     * 特征：以 “左 - 根 - 右” 的次序 来 返回BST中的key
     * @return all keys in the symbol table
     */
    public Iterable<Key> getIterableKeys() {
        if (isEmpty()) return new Queue<>();
        return getIterableKeysBetween(getMinKey(), getMaxKey());
    }

    /**
     * 以 Iterable的方式 来 返回符号表中所有在指定范围内的key 组成的集合。
     *
     * @param leftBarKey  minimum endpoint 左边界（包含）
     * @param rightBarKey maximum endpoint 右边界（包含）
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *                                  is {@code null}
     */
    public Iterable<Key> getIterableKeysBetween(Key leftBarKey, Key rightBarKey) {
        if (leftBarKey == null) throw new IllegalArgumentException("first argument to keys() is null");
        if (rightBarKey == null) throw new IllegalArgumentException("second argument to keys() is null");

        // 🐖：这里只需要一个可迭代的集合类型，不一定要是队列
        Queue<Key> queue = new Queue<>();
        collectKeysBetweenRangeInto(rootNode, queue, leftBarKey, rightBarKey);
        return queue;
    }

    // RANGE
    // 使用一个队列 来 收集二叉树中在 [leftBarKey, rightBarKey]区间之间的所有的key
    // 收集顺序：左 - 根 - 右   手段：递归调用时，代码编写的顺序 左 - 根 - 右
    private void collectKeysBetweenRangeInto(Node currentNode, Queue<Key> queueToCollect, Key leftBarKey, Key rightBarKey) {
        if (currentNode == null) return;

        /* 判断区间的范围 */
        // 比较左边界key、右区间key 与 当前节点key的大小关系
        int leftBarResult = leftBarKey.compareTo(currentNode.key);
        int rightBarResult = rightBarKey.compareTo(currentNode.key);

        // 1 如果区间 与左子树有重叠，则：
        if (leftBarResult < 0) // 收集左子树中 满足条件（在指定范围内）的key
            collectKeysBetweenRangeInto(currentNode.leftSubTree, queueToCollect, leftBarKey, rightBarKey);
        // 2 区间 包含有 根节点，则：
        if (leftBarResult <= 0 && rightBarResult >= 0)
            queueToCollect.enqueue(currentNode.key); // 收集根结点中的key
        // 3 区间 与右子树有重叠，则：
        if (rightBarResult > 0) // 收集右子树中 满足条件（在指定范围内）的key
            collectKeysBetweenRangeInto(currentNode.rightSubTree, queueToCollect, leftBarKey, rightBarKey);
    }

    /**
     * 返回符号表中，在指定区间/范围内的所有的key的数量
     *
     * @param leftBarKey  minimum endpoint 左边界（包含）
     * @param rightBarKey maximum endpoint 右边界（包含）
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *                                  is {@code null}
     */
    public int pairAmountBetween(Key leftBarKey, Key rightBarKey) {
        if (leftBarKey == null) throw new IllegalArgumentException("first argument to size() is null");
        if (rightBarKey == null) throw new IllegalArgumentException("second argument to size() is null");

        if (leftBarKey.compareTo(rightBarKey) > 0) return 0;
        // 如果符号表中存在 与右边界相同的key，则：数量+1 todo why so? 缺少一个简洁合理的解释
        if (doesContains(rightBarKey))
            return rankingOf(rightBarKey) - rankingOf(leftBarKey) + 1;
        else
            return rankingOf(rightBarKey) - rankingOf(leftBarKey);
    }

    // ~~ VERIFY BST ~~
    /**
     * 返货 符号表所使用的二叉查找树的高度
     * 1-结点的树 的高度为0
     */
    public int heightOfBST() {
        return heightOf(rootNode);
    }

    private int heightOf(Node currentNode) {
        if (currentNode == null)
            return -1;

        return 1 + Math.max(heightOf(currentNode.leftSubTree), heightOf(currentNode.rightSubTree));
    }

    /**
     * 出于调试目的，返回 符号表所使用的二叉查找树的 层序遍历产生的key序列
     * 手段：
     *  #1 利用队列的先进先出特性 来 以“当前层：自左向右 不同层：自上而下”的顺序 遍历BST中的结点
     *  #2 把队列中的队首结点 添加到 keys简单集合中
     * @return the keys in the BinarySearchTreeSymbolTable in level order traversal（层序遍历）
     */
    public Iterable<Key> getIterableKeysInLevelOrder() {
        List<Key> keyCollection = new ArrayList<>();
        Queue<Node> nodeQueue = new Queue<>();
        // #1 入队 “初始结点”
        nodeQueue.enqueue(rootNode);

        while (!nodeQueue.isEmpty()) {
            // # 把结点队列的队首结点(动态变化)中的key 添加到 keys集合（一直增长）中
            Node currentNode = nodeQueue.dequeue();
            if (currentNode == null) // 判断队列中的结点是否已经用尽
                continue;
            keyCollection.add(currentNode.key);

            // #2 入队 “左结点”
            nodeQueue.enqueue(currentNode.leftSubTree);
            // #3 入队 “右结点”
            nodeQueue.enqueue(currentNode.rightSubTree);
        }

        // 返回已经 按照“根 - 左 - 右” 排序后的key集合
        return keyCollection;
    }

    /*************************************************************************
     *  Check integrity of BinarySearchTreeSymbolTable data structure.
     *  检查 符号表数据结构的完整性
     ***************************************************************************/
    private boolean checkIfUnderlyingMemberCorrect() {
        if (!isLegitBST()) StdOut.println("Not in symmetric order");
        if (!isNodesAmountConsistent()) StdOut.println("Subtree counts not consistent");
        if (!isRankingConsistent()) StdOut.println("Ranks not consistent");
        return isLegitBST() && isNodesAmountConsistent() && isRankingConsistent();
    }

    // 这个二叉树是否满足 对称顺序(symmetric order)?
    // 由于顺序是严格大的，因此 这个test也能够保证数据结构是一个 二叉树
    private boolean isLegitBST() {
        return isLegitBST(rootNode, null, null);
    }

    // 判断 以currentNode作为根结点的树 是不是一个 (所有的key都严格在[min, max]之间)的BST?
    // 如果min、max为null，则：将它们视为空约束 也就是Optional的约束
    // 荣誉：Bob Dondero 优雅的解决方案
    private boolean isLegitBST(Node currentNode, Key minKey, Key maxKey) {
        if (currentNode == null) return true;
        if (minKey != null && currentNode.key.compareTo(minKey) <= 0) return false;
        if (maxKey != null && currentNode.key.compareTo(maxKey) >= 0) return false;
        return isLegitBST(currentNode.leftSubTree, minKey, currentNode.key)
                && isLegitBST(currentNode.rightSubTree, currentNode.key, maxKey);
    }

    // are the itsNodesAmount fields correct?
    // size字段是否正确？
    private boolean isNodesAmountConsistent() {
        return isNodesAmountConsistentFor(rootNode);
    }

    private boolean isNodesAmountConsistentFor(Node currentNode) {
        // 空结点也是一棵二叉搜索树
        if (currentNode == null) return true;
        // 对于非空的二叉搜索树，要求恒等式一直成立 - size(rootNode) = size(leftTree) + size(rightTree) + 1
        if (currentNode.itsNodesAmount != nodeAmountOf(currentNode.leftSubTree) + nodeAmountOf(currentNode.rightSubTree) + 1)
            return false;
        // 左右子树各自本身也满足相同的条件（递归）
        return isNodesAmountConsistentFor(currentNode.leftSubTree) && isNodesAmountConsistentFor(currentNode.rightSubTree);
    }

    // check that ranks are consistent
    // 检查排名是否正确
    private boolean isRankingConsistent() {
        for (int currentRank = 0; currentRank < pairAmount(); currentRank++)
            if (currentRank != rankingOf(selectOutKeyOf(currentRank)))
                return false;

        for (Key currentKey : getIterableKeys())
            if (currentKey.compareTo(selectOutKeyOf(rankingOf(currentKey))) != 0)
                return false;
        return true;
    }

    // ~~ SIMPLE HELPER ~~
    /**
     * 符号表是否为空
     */
    public boolean isEmpty() {
        return pairAmount() == 0;
    }

    /**
     * 返回符号表中键值对的数量
     */
    public int pairAmount() {
        return nodeAmountOf(rootNode);
    }


    /**
     * BinarySearchTreeSymbolTable 数据类型的单元测试
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        BinarySearchTreeSymbolTable<String, Integer> mySymbolTable = new BinarySearchTreeSymbolTable<>();

        // 构造符号表
        for (int currentSpot = 0; !StdIn.isEmpty(); currentSpot++) {
            String currentKey = StdIn.readString();
            mySymbolTable.putInPairOf(currentKey, currentSpot);
        }

        // 对符号表底层的二叉查找树中的结点 做层序遍历（当前层：自左向右 不同层：自上而下）
        for (String currentNodeKey : mySymbolTable.getIterableKeysInLevelOrder())
            StdOut.println(currentNodeKey + " " + mySymbolTable.getAssociatedValueOf(currentNodeKey));

        StdOut.println("~~~");

        // 遍历符号表中的所有key - key的顺序：BST结点中的左 - 根 - 右
        for (String currentKey : mySymbolTable.getIterableKeys())
            StdOut.println(currentKey + " " + mySymbolTable.getAssociatedValueOf(currentKey));
    }
}