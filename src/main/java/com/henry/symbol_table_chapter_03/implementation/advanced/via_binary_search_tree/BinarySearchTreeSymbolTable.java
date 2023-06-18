package com.henry.symbol_table_chapter_03.implementation.advanced.via_binary_search_tree;
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

import java.util.NoSuchElementException;

/**
 * The {@code BinarySearchTreeSymbolTable} class represents an ordered symbol table of generic
 * key-value pairs.
 * It supports the usual <em>put</em>, <em>get</em>, <em>contains</em>,
 * <em>delete</em>, <em>itsNodesAmount</em>, and <em>is-empty</em> methods.
 * It also provides ordered methods for finding the <em>minimum</em>,
 * <em>maximum</em>, <em>floor</em>, <em>select</em>, <em>ceiling</em>.
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
 * This implementation uses an (unbalanced) <em>binary search tree</em>.
 * The <em>put</em>, <em>contains</em>, <em>remove</em>, <em>minimum</em>,
 * <em>maximum</em>, <em>ceiling</em>, <em>floor</em>, <em>select</em>, and
 * <em>rank</em>  operations each take &Theta;(<em>n</em>) time in the worst
 * case, where <em>n</em> is the number of key-value pairs.
 * The <em>itsNodesAmount</em> and <em>is-empty</em> operations take &Theta;(1) time.
 * The keys method takes &Theta;(<em>n</em>) time in the worst case.
 * Construction takes &Theta;(1) time.
 * <p>
 * For alternative implementations of the symbol table API,
 * see 符号表, 二叉查找符号表, 有序查找符号表, 红黑树符号表, 单独链条哈希符号表, 线性探测哈希符号表
 * For additional documentation, see
 * <a href="https://algs4.cs.princeton.edu/32bst">Section 3.2</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
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

    /**
     * 符号表是否为空
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * 返回符号表中键值对的数量
     */
    public int size() {
        return size(rootNode);
    }

    // 返回 二叉查找树中的结点数量（键值对数量）
    private int size(Node currentNode) {
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
    public boolean contains(Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(passedKey) != null;
    }

    /**
     * 在符号表中查找传入的键，并返回 其所关联的值。
     * 如果符号表中不存在传入的键，则：返回null
     *
     * @param passedKey
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Value get(Key passedKey) {
        return get(rootNode, passedKey);
    }

    /**
     * 在二叉查找树中，查找传入的key
     * 如果命中，则：返回key所对应的值。
     * 如果未命中，则：返回null
     */
    private Value get(Node currentNode, Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("calls get() with a null key");

        // 递归的终结条件 - 查找过程结束于一个空链接/空结点
        if (currentNode == null) return null;

        // 本级递归要做的事情：1 把树分解为根节点 + 左子树 + 右子树； 2 判断根节点是不是预期的节点； 3 如果不是，从左右子树上执行查找，并返回值
        // 根据 传入的key 与 当前二叉树的根结点的key的比较结果 来 返回关联的value(如果key相同) 或者 在对应的子树中继续递归查找
        int result = passedKey.compareTo(currentNode.key);
        if (result < 0) return get(currentNode.leftSubTree, passedKey);
        else if (result > 0) return get(currentNode.rightSubTree, passedKey);
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
    public void put(Key passedKey, Value associatedValue) {
        if (passedKey == null) throw new IllegalArgumentException("calls put() with a null key");
        // #case01 传入的value是null, 则：执行删除
        if (associatedValue == null) {
            delete(passedKey);
            return;
        }

        // 向二叉查找树rootNode 中插入 key-value pair
        rootNode = put(rootNode, passedKey, associatedValue);
        assert check();
    }

    // 🐖 插入的过程 与 查找的过程十分类似 - 插入前，需要先查找
    private Node put(Node currentNode, Key passedKey, Value associatedValue) {
        // 递归终结条件：查询结束于一个空结点/链接
        // 则：为传入的键值对创建一个新结点，并返回 以 链接到父节点上（重置搜索路径上指向结点的链接）
        if (currentNode == null)
            return new Node(passedKey, associatedValue, 1);

        // 重置 搜索路径上的所有的 父节点指向子节点的链接（aka 左右链接）
        // 手段：node.leftNode = xxx; node.rightNode = ooo;
        int result = passedKey.compareTo(currentNode.key);
        if (result < 0) // 向左子树中插入键值对，并使用插入后的子树 来 更新左子树
            currentNode.leftSubTree = put(currentNode.leftSubTree, passedKey, associatedValue);
        else if (result > 0) // 向右子树中插入键值对，并使用插入后的子树 来 更新右子树
            currentNode.rightSubTree = put(currentNode.rightSubTree, passedKey, associatedValue);
        else currentNode.value = associatedValue; // 如果根节点的key 与 传入的key相同，则：更新结点中的value

        // 更新搜索路径中每个结点的 计数器 - 🐖 如果新增了结点，则：搜索路径上的每个结点的结点计数器都要+1
        // 手段：使用一个通用的恒等式 👇
        currentNode.itsNodesAmount = 1 + size(currentNode.leftSubTree) + size(currentNode.rightSubTree);
        return currentNode;
    }


    /**
     * 从符号表中删除最小的key & 它所关联的值
     * <p>
     * 如果符号表为空，则：抛出 NoSuchElementException异常
     */
    public void deleteMin() {
        if (isEmpty()) throw new NoSuchElementException("Symbol table underflow");
        rootNode = deleteMin(rootNode);
        assert check();
    }

    private Node deleteMin(Node currentNode) {
        /* 原理：最小的key 在二叉查找树的左子树的左子节点中 */
        if (currentNode.leftSubTree == null) // 如果左子树为空，说明最小结点就是根节点。则：直接删除根结点
            return currentNode.rightSubTree; // 手段：返回二叉查找树的右子树
        // 从左子树中删除最小结点 & 使用删除结点后的子树 来 更新指向原始子树的链接
        currentNode.leftSubTree = deleteMin(currentNode.leftSubTree);
        // 更新当前二叉树中的 结点计数器
        currentNode.itsNodesAmount = size(currentNode.leftSubTree) + size(currentNode.rightSubTree) + 1;

        // 返回当前结点
        return currentNode;
    }

    /**
     * 从符号表中删除最大键 & 它所关联的值
     * 如果符号表为空，则 抛出 NoSuchElementException
     */
    public void deleteMax() {
        if (isEmpty()) throw new NoSuchElementException("Symbol table underflow");
        rootNode = deleteMax(rootNode);
        assert check();
    }

    private Node deleteMax(Node currentNode) {
        /* 原理：二叉查找树中的最大结点 在右子树的右子结点中 */
        // 如果右子树为空，说明最大结点就是根结点，则：删除根结点      手段：直接返回当前二叉树的左子树
        if (currentNode.rightSubTree == null) return currentNode.leftSubTree;
        // 从右子树中删除最大结点 & “使用删除结点后的子树 来 更新指向原始子树的链接”
        currentNode.rightSubTree = deleteMax(currentNode.rightSubTree);
        // 更新当前二叉树根结点中的 结点计数器
        currentNode.itsNodesAmount = size(currentNode.leftSubTree) + size(currentNode.rightSubTree) + 1;

        // 返回当前结点
        return currentNode;
    }

    /**
     * 从符号表中删除传入的key & 它所关联的value（如果key存在于符号表中的话）
     * <p>
     * 如果传入的key为null 则抛出 非法参数异常
     *
     * @param passedKey
     */
    public void delete(Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("calls delete() with a null key");
        rootNode = delete(rootNode, passedKey);
        assert check();
    }

    // 从二叉查找树中删除 传入的key
    private Node delete(Node currentNode, Key passedKey) {
        // 递归中终结条件 - 对传入的key的查询 结束于一个空结点(也就是没有找到它)，则：返回null 表示查询未命中
        if (currentNode == null) return null;

        // 比较 传入的key 与 当前二叉树的根结点中的key
        int result = passedKey.compareTo(currentNode.key);

        // 如果传入的key 比 当前二叉树的根结点中的key更小...
        if (result < 0)
            // 从左子树中删除结点 & “使用删除结点后的子树 来 更新指向原始子树的链接”
            currentNode.leftSubTree = delete(currentNode.leftSubTree, passedKey);
        else if // 右子树同理
        (result > 0) currentNode.rightSubTree = delete(currentNode.rightSubTree, passedKey);
        else { // 如果传入的key 与 当前结点的key相同，则：删除当前二叉树的根结点
            /* 当删除有两个子节点的结点时，会有两个链接无处attach。但是只会有一个空链接 available 怎么办？
                Hibbard提出的解决方案：使用 被删除结点的后继结点(successor) 来 填补/替换 被删除结点的位置
                原理：在二叉树中的任何一个结点，都会有一个指向它的链接 & 两个从它指出的链接
                比喻：挖东墙，补西墙。
                难点：选择的后继结点 替换 被删除的结点后，整棵二叉搜索树仍旧能够遵守 BST的约束。
                手段：这里选择的后继结点 是 待删除结点的右子树中的最小结点。
                    因为从BST数值约束的角度来说，它可以作为 待删除的原始结点的平替
                具体做法：
                    #1 把 successor结点 作为 当前结点；
                    #2 更新 当前结点的左右链接；
                    #3 返回 当前结点 来 更新“指向当前结点的链接”
            * */
            // 左、右子树为空的case
            if (currentNode.rightSubTree == null) return currentNode.leftSubTree;
            if (currentNode.leftSubTree == null) return currentNode.rightSubTree;

            // 为当前结点添加一个引用 - 用于记录原始结点，从而在需要的时候获取到原始结点的左右结点
            Node originalNode = currentNode;
            // 获取原始结点 右子树中的最小结点 & 并 将之作为当前结点
            currentNode = min(originalNode.rightSubTree);
            // 从原始结点的右子树中删除最小结点 & 使用删除最小结点后的右子树 来 更新“当前结点”的右链接
            currentNode.rightSubTree = deleteMin(originalNode.rightSubTree);
            currentNode.leftSubTree = originalNode.leftSubTree;
        }

        // 更新当前二叉树根结点中的 结点计数器
        currentNode.itsNodesAmount = size(currentNode.leftSubTree) + size(currentNode.rightSubTree) + 1;

        return currentNode;
    }


    /**
     * 返回符号表中的最小键
     * 当符号表为空时，抛出 NoSuchElementException
     */
    public Key min() {
        if (isEmpty()) throw new NoSuchElementException("calls min() with empty symbol table");
        return min(rootNode).key;
    }

    private Node min(Node currentNode) {
        // 原理：二叉查找树中的最小结点一定是左子树中的左子结点
        // 手段：一直递归查找 二叉树的左子树，直到遇到左链接为null的结点即可 - 它就是最小的结点
        if (currentNode.leftSubTree == null) return currentNode;
        else return min(currentNode.leftSubTree);
    }

    /**
     * 返回符号表中的最大key
     * 如果符号表为空，则：抛出 没有这样的元素的异常
     */
    public Key max() {
        if (isEmpty()) throw new NoSuchElementException("calls max() with empty symbol table");
        return max(rootNode).key;
    }

    private Node max(Node currentNode) { // 同理
        if (currentNode.rightSubTree == null) return currentNode;
        else return max(currentNode.rightSubTree);
    }

    /**
     * 返回符号表中 小于等于 传入key的最大的key
     * <p>
     * 如果传入的key不存在，则：抛出 元素不存在异常
     * 如果传入的key为null，则：抛出 非法参数异常
     *
     * @param passedKey
     */
    public Key floor(Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to floor() is null");
        if (isEmpty()) throw new NoSuchElementException("calls floor() with empty symbol table");

        Node x = floor(rootNode, passedKey);
        if (x == null) throw new NoSuchElementException("argument to floor() is too small");
        else return x.key;
    }

    // 在 当前二叉树中找到 “小于等于传入key的最大结点” 👇
    private Node floor(Node currentNode, Key passedKey) {
        // 递归终结条件 - 查找过程结束于空结点，说明 满足条件的结点在二叉树中不存在，返回null 来 表示“不存在”
        if (currentNode == null) return null;

        int result = passedKey.compareTo(currentNode.key);
        // 如果传入的key 刚好等于 当前二叉树的根结点key，则：根结点就是 小于等于传入key的最大结点
        if (result == 0) return currentNode;
        // 如果传入的key 比 当前二叉树的根结点key 更小，则：在左子树中继续查找，并返回查找结果
        if (result < 0) return floor(currentNode.leftSubTree, passedKey);
        // 如果传入的key 比 当前二叉树的根结点key 更大，则：在右子树中继续查找
        Node foundNode = floor(currentNode.rightSubTree, passedKey);
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
    public Key ceiling(Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to ceiling() is null");
        if (isEmpty()) throw new NoSuchElementException("calls ceiling() with empty symbol table");
        Node foundNode = ceiling(rootNode, passedKey);

        if (foundNode == null) throw new NoSuchElementException("argument to floor() is too large");
        else return foundNode.key;
    }

    // 在当前二叉树中，查找 大于等于传入key的最小结点
    private Node ceiling(Node currentNode, Key passedKey) {
        // 查找结束于空结点，说明没找见
        if (currentNode == null) return null;

        int result = passedKey.compareTo(currentNode.key);
        // 找到了 等于传入key的结点 - 它就是 “大于等于传入key”的最小结点
        if (result == 0) return currentNode;
        // 在左子树中查找 “大于等于传入key的结点”
        if (result < 0) {
            Node foundCeilingNode = ceiling(currentNode.leftSubTree, passedKey);
            // 如果在左子树中找到了“大于等于传入key的结点”，则：返回所找到的结点
            if (foundCeilingNode != null) return foundCeilingNode;
            else // 如果没有找到，说明 当前二叉树的根结点就是 “大于等于传入key的最大结点”，则：返回它
                return currentNode;
        }
        return ceiling(currentNode.rightSubTree, passedKey);
    }

    /**
     * 返回符号表中 传入的排名 所对应的键。
     * 这个key存在有如下性质：在符号表中存在有 rank个key都小于它。
     * 换句话说，这个key 是符号表中 第(rank+1)小的key
     *
     * @param passedRank the order statistic （排名）
     * @return 符号表中排名为rank的键
     * 如果传入的rank 不在 [0, n-1]之间，则 抛出 非法参数异常
     */
    public Key select(int passedRank) {
        if (passedRank < 0 || passedRank >= size()) {
            throw new IllegalArgumentException("argument to select() is invalid: " + passedRank);
        }
        return select(rootNode, passedRank);
    }

    // 返回二叉搜索树中，指定排名的键
    // 前提条件：排名在合法的范围
    private Key select(Node currentNode, int passedRank) {
        // 如果查找过程结束于空结点，说明 在二叉树中没有找到 传入的rank，则：返回null(约定)
        // 这个应该属于是防御性编程 - 如果传入的passedRank本身是合法的，则：应该总能找到对应的结点
        if (currentNode == null) return null;
        int leftTreeSize = size(currentNode.leftSubTree);
        if (leftTreeSize > passedRank) return select(currentNode.leftSubTree, passedRank);
        else if (leftTreeSize < passedRank) return select(currentNode.rightSubTree, passedRank - leftTreeSize - 1);
        else return currentNode.key;
    }

    /**
     * 返回 符号表中 所有严格小于 传入的key的键的数量
     *
     * @param passedKey
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public int rank(Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to rank() is null");
        return rank(passedKey, rootNode);
    }

    // Number of keys in the subtree less than key.
    private int rank(Key passedKey, Node currentNode) {
        if (currentNode == null) return 0;
        int result = passedKey.compareTo(currentNode.key);
        if (result < 0) return rank(passedKey, currentNode.leftSubTree);
        else if (result > 0) return 1 + size(currentNode.leftSubTree) + rank(passedKey, currentNode.rightSubTree);
        else return size(currentNode.leftSubTree);
    }

    /**
     * 以Iterable的方式 来 返回符号表中所有的key所组成的集合
     * <p>
     * 为了遍历 st符号表中所有的key，可以使用foreach标记语法： for(Key key : st.keys()) {...}
     *
     * @return all keys in the symbol table
     */
    public Iterable<Key> keys() {
        if (isEmpty()) return new Queue<>();
        return keys(min(), max());
    }

    /**
     * 以 Iterable的方式 来 返回符号表中所有在指定范围内的key 组成的集合。
     *
     * @param leftBarKey  minimum endpoint 左边界（包含）
     * @param rightBarKey maximum endpoint 右边界（包含）
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *                                  is {@code null}
     */
    public Iterable<Key> keys(Key leftBarKey, Key rightBarKey) {
        if (leftBarKey == null) throw new IllegalArgumentException("first argument to keys() is null");
        if (rightBarKey == null) throw new IllegalArgumentException("second argument to keys() is null");

        Queue<Key> queue = new Queue<>();
        keys(rootNode, queue, leftBarKey, rightBarKey);
        return queue;
    }

    // 使用一个队列 来 收集二叉树中在 [leftBarKey, rightBarKey]区间之间的所有的key
    // 收集顺序：左 - 根 - 右
    private void keys(Node currentNode, Queue<Key> queueToCollect, Key leftBarKey, Key rightBarKey) {
        if (currentNode == null) return;

        /* 判断区间的范围 */
        // 比较左区间、右区间与节点key
        int leftBarResult = leftBarKey.compareTo(currentNode.key);
        int rightBarResult = rightBarKey.compareTo(currentNode.key);

        // 1 如果区间横跨左子树，则：
        if (leftBarResult < 0) // 收集左子树中满足条件（在指定范围内）的key
            keys(currentNode.leftSubTree, queueToCollect, leftBarKey, rightBarKey);
        // 2 区间横跨根节点，则：
        if (leftBarResult <= 0 && rightBarResult >= 0)
            queueToCollect.enqueue(currentNode.key); // 收集根结点中的key
        // 3 区间横跨右子树，则：
        if (rightBarResult > 0) // 收集右子树中满足条件（在指定范围内）的key
            keys(currentNode.rightSubTree, queueToCollect, leftBarKey, rightBarKey);
    }

    /**
     * 返回符号表中，在指定区间/范围内的所有的key的数量
     *
     * @param leftBarKey  minimum endpoint 左边界（包含）
     * @param rightBarKey maximum endpoint 右边界（包含）
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *                                  is {@code null}
     */
    public int size(Key leftBarKey, Key rightBarKey) {
        if (leftBarKey == null) throw new IllegalArgumentException("first argument to size() is null");
        if (rightBarKey == null) throw new IllegalArgumentException("second argument to size() is null");

        if (leftBarKey.compareTo(rightBarKey) > 0) return 0;
        // 如果符号表中存在 与右边界相同的key，则：数量+1 todo why so? 缺少一个简洁合理的解释
        if (contains(rightBarKey)) return rank(rightBarKey) - rank(leftBarKey) + 1;
        else return rank(rightBarKey) - rank(leftBarKey);
    }

    /**
     * 返货 符号表所使用的二叉查找树的高度
     * 1-结点的树 的高度为0
     */
    public int height() {
        return height(rootNode);
    }

    private int height(Node currentNode) {
        if (currentNode == null) return -1;
        return 1 + Math.max(height(currentNode.leftSubTree), height(currentNode.rightSubTree));
    }

    /**
     * 出于调试目的，返回 符号表所使用的二叉查找树的 层序遍历产生的key序列
     *
     * @return the keys in the BinarySearchTreeSymbolTable in level order traversal（层序遍历）
     */
    public Iterable<Key> levelOrder() {
        Queue<Key> keys = new Queue<>();
        Queue<Node> nodeQueue = new Queue<>();
        nodeQueue.enqueue(rootNode);

        while (!nodeQueue.isEmpty()) {
            Node currentNode = nodeQueue.dequeue();
            if (currentNode == null) continue;
            keys.enqueue(currentNode.key);
            nodeQueue.enqueue(currentNode.leftSubTree);
            nodeQueue.enqueue(currentNode.rightSubTree);
        }
        return keys;
    }

    /*************************************************************************
     *  Check integrity of BinarySearchTreeSymbolTable data structure.
     *  检查 符号表数据结构的完整性
     ***************************************************************************/
    private boolean check() {
        if (!isBST()) StdOut.println("Not in symmetric order");
        if (!isSizeConsistent()) StdOut.println("Subtree counts not consistent");
        if (!isRankConsistent()) StdOut.println("Ranks not consistent");
        return isBST() && isSizeConsistent() && isRankConsistent();
    }

    // 这个二叉树是否满足 对称顺序(symmetric order)?
    // 由于顺序是严格大的，因此 这个test也能够保证数据结构是一个 二叉树
    private boolean isBST() {
        return isBST(rootNode, null, null);
    }

    // 判断 以currentNode作为根结点的树 是不是一个 (所有的key都严格在[min, max]之间)的BST?
    // 如果min、max为null，则：将它们视为空约束 也就是Optional的约束
    // 荣誉：Bob Dondero 优雅的解决方案
    private boolean isBST(Node currentNode, Key minKey, Key maxKey) {
        if (currentNode == null) return true;
        if (minKey != null && currentNode.key.compareTo(minKey) <= 0) return false;
        if (maxKey != null && currentNode.key.compareTo(maxKey) >= 0) return false;
        return isBST(currentNode.leftSubTree, minKey, currentNode.key)
                && isBST(currentNode.rightSubTree, currentNode.key, maxKey);
    }

    // are the itsNodesAmount fields correct?
    // size字段是否正确？
    private boolean isSizeConsistent() {
        return isSizeConsistent(rootNode);
    }

    private boolean isSizeConsistent(Node currentNode) {
        // 空结点也是一棵二叉搜索树
        if (currentNode == null) return true;
        // 对于非空的二叉搜索树，要求恒等式一直成立 - size(rootNode) = size(leftTree) + size(rightTree) + 1
        if (currentNode.itsNodesAmount != size(currentNode.leftSubTree) + size(currentNode.rightSubTree) + 1)
            return false;
        // 左右子树各自本身也满足相同的条件（递归）
        return isSizeConsistent(currentNode.leftSubTree) && isSizeConsistent(currentNode.rightSubTree);
    }

    // check that ranks are consistent
    // 检查排名是否正确
    private boolean isRankConsistent() {
        for (int currentRank = 0; currentRank < size(); currentRank++)
            if (currentRank != rank(select(currentRank))) return false;
        for (Key currentKey : keys())
            if (currentKey.compareTo(select(rank(currentKey))) != 0) return false;
        return true;
    }


    /**
     * BinarySearchTreeSymbolTable 数据类型的单元测试
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        BinarySearchTreeSymbolTable<String, Integer> symbolTable
                = new BinarySearchTreeSymbolTable<>();

        for (int currentSpot = 0; !StdIn.isEmpty(); currentSpot++) {
            String currentKey = StdIn.readString();
            symbolTable.put(currentKey, currentSpot);
        }

        // 对符号表底层的二叉查找树中的结点 做层序遍历（当前层：自左向右 不同层：自上而下）
        for (String currentNodeKey : symbolTable.levelOrder())
            StdOut.println(currentNodeKey + " " + symbolTable.get(currentNodeKey));

        StdOut.println("~~~");

        // 遍历符号表中的所有key - key的顺序：BST结点中的左 - 根 - 右
        for (String currentKey : symbolTable.keys())
            StdOut.println(currentKey + " " + symbolTable.get(currentKey));
    }
}