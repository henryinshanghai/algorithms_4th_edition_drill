package com.henry.symbol_table_chapter_03.implementation.advanced.via_binary_search_tree; /******************************************************************************
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
 * For alternative implementations of the symbol table API, see {@link ST},
 * {@link BinarySearchST}, {@link SequentialSearchST}, {@link RedBlackBSTFromWebsite},
 * {@link SeparateChainingHashST}, and {@link LinearProbingHashST},
 * For additional documentation, see
 * <a href="https://algs4.cs.princeton.edu/32bst">Section 3.2</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class BinarySearchTreeSymbolTable<Key extends Comparable<Key>, Value> {
    private Node rootNode;             // rootNode of BinarySearchTreeSymbolTable

    private class Node {
        private Key key;           // sorted by key
        private Value value;         // associated data
        private Node leftSubTree, rightSubTree;  // leftSubTree and rightSubTree subtrees
        private int itsNodesAmount;          // number of nodes in subtree

        public Node(Key key, Value value, int nodesAmount) {
            this.key = key;
            this.value = value;
            this.itsNodesAmount = nodesAmount;
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

    // return number of key-value pairs in BinarySearchTreeSymbolTable rooted at x
    private int size(Node currentNode) {
        if (currentNode == null) return 0;
        else return currentNode.itsNodesAmount;
    }

    /**
     * Does this symbol table contain the given key?
     *
     * @param key the key
     * @return {@code true} if this symbol table contains {@code key} and
     * {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != null;
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

        // 递归的终结条件
        if (currentNode == null) return null;

        // 本级递归要做的事情：1 把树分解为根节点 + 左子树 + 右子树； 2 判断根节点是不是预期的节点； 3 如果不是，从左右子树上执行查找，并返回值
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

        rootNode = put(rootNode, passedKey, associatedValue);
        assert check();
    }

    // 🐖 插入的过程 与 查找的过程十分类似 - 插入前，需要先查找
    private Node put(Node currentNode, Key passedKey, Value associatedValue) {
        // 递归终结条件：查询结束于一个空结点/链接 则：为传入的键值对创建一个新结点，并返回 以 链接到父节点上（重置搜索路径上指向结点的链接）
        if (currentNode == null)
            return new Node(passedKey, associatedValue, 1);


        int result = passedKey.compareTo(currentNode.key);
        if (result < 0) // 向左子树中插入键值对，并使用插入后的结果 来 更新左子树
            currentNode.leftSubTree = put(currentNode.leftSubTree, passedKey, associatedValue);
        else if (result > 0) // 向右子树中插入键值对，并使用插入后的结果 来 更新右子树
            currentNode.rightSubTree = put(currentNode.rightSubTree, passedKey, associatedValue);
        else currentNode.value = associatedValue; // 根节点的key 与 传入的key相同，则：更新结点中的value

        // 更新搜索路径中每个结点的 计数器 - 如果新增了结点，则：搜索路径上的每个结点的结点计数器都要+1
        // 手段：使用一个通用的等式 👇
        currentNode.itsNodesAmount = 1 + size(currentNode.leftSubTree) + size(currentNode.rightSubTree);
        return currentNode;
    }


    /**
     * Removes the smallest key and associated value from the symbol table.
     *
     * @throws NoSuchElementException if the symbol table is empty
     */
    public void deleteMin() {
        if (isEmpty()) throw new NoSuchElementException("Symbol table underflow");
        rootNode = deleteMin(rootNode);
        assert check();
    }

    private Node deleteMin(Node x) {
        if (x.leftSubTree == null) return x.rightSubTree;
        x.leftSubTree = deleteMin(x.leftSubTree);
        x.itsNodesAmount = size(x.leftSubTree) + size(x.rightSubTree) + 1;
        return x;
    }

    /**
     * Removes the largest key and associated value from the symbol table.
     *
     * @throws NoSuchElementException if the symbol table is empty
     */
    public void deleteMax() {
        if (isEmpty()) throw new NoSuchElementException("Symbol table underflow");
        rootNode = deleteMax(rootNode);
        assert check();
    }

    private Node deleteMax(Node x) {
        if (x.rightSubTree == null) return x.leftSubTree;
        x.rightSubTree = deleteMax(x.rightSubTree);
        x.itsNodesAmount = size(x.leftSubTree) + size(x.rightSubTree) + 1;
        return x;
    }

    /**
     * Removes the specified key and its associated value from this symbol table
     * (if the key is in this symbol table).
     *
     * @param key the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("calls delete() with a null key");
        rootNode = delete(rootNode, key);
        assert check();
    }

    private Node delete(Node x, Key key) {
        if (x == null) return null;

        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.leftSubTree = delete(x.leftSubTree, key);
        else if (cmp > 0) x.rightSubTree = delete(x.rightSubTree, key);
        else {
            if (x.rightSubTree == null) return x.leftSubTree;
            if (x.leftSubTree == null) return x.rightSubTree;
            Node t = x;
            x = min(t.rightSubTree);
            x.rightSubTree = deleteMin(t.rightSubTree);
            x.leftSubTree = t.leftSubTree;
        }
        x.itsNodesAmount = size(x.leftSubTree) + size(x.rightSubTree) + 1;
        return x;
    }


    /**
     * Returns the smallest key in the symbol table.
     *
     * @return the smallest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
    public Key min() {
        if (isEmpty()) throw new NoSuchElementException("calls min() with empty symbol table");
        return min(rootNode).key;
    }

    private Node min(Node x) {
        if (x.leftSubTree == null) return x;
        else return min(x.leftSubTree);
    }

    /**
     * Returns the largest key in the symbol table.
     *
     * @return the largest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
    public Key max() {
        if (isEmpty()) throw new NoSuchElementException("calls max() with empty symbol table");
        return max(rootNode).key;
    }

    private Node max(Node x) {
        if (x.rightSubTree == null) return x;
        else return max(x.rightSubTree);
    }

    /**
     * Returns the largest key in the symbol table less than or equal to {@code key}.
     *
     * @param key the key
     * @return the largest key in the symbol table less than or equal to {@code key}
     * @throws NoSuchElementException   if there is no such key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Key floor(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to floor() is null");
        if (isEmpty()) throw new NoSuchElementException("calls floor() with empty symbol table");
        Node x = floor(rootNode, key);
        if (x == null) throw new NoSuchElementException("argument to floor() is too small");
        else return x.key;
    }

    private Node floor(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (cmp < 0) return floor(x.leftSubTree, key);
        Node t = floor(x.rightSubTree, key);
        if (t != null) return t;
        else return x;
    }

    public Key floor2(Key key) {
        Key x = floor2(rootNode, key, null);
        if (x == null) throw new NoSuchElementException("argument to floor() is too small");
        else return x;

    }

    private Key floor2(Node x, Key key, Key best) {
        if (x == null) return best;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) return floor2(x.leftSubTree, key, best);
        else if (cmp > 0) return floor2(x.rightSubTree, key, x.key);
        else return x.key;
    }

    /**
     * Returns the smallest key in the symbol table greater than or equal to {@code key}.
     *
     * @param key the key
     * @return the smallest key in the symbol table greater than or equal to {@code key}
     * @throws NoSuchElementException   if there is no such key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Key ceiling(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to ceiling() is null");
        if (isEmpty()) throw new NoSuchElementException("calls ceiling() with empty symbol table");
        Node x = ceiling(rootNode, key);
        if (x == null) throw new NoSuchElementException("argument to floor() is too large");
        else return x.key;
    }

    private Node ceiling(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (cmp < 0) {
            Node t = ceiling(x.leftSubTree, key);
            if (t != null) return t;
            else return x;
        }
        return ceiling(x.rightSubTree, key);
    }

    /**
     * Return the key in the symbol table of a given {@code rank}.
     * This key has the property that there are {@code rank} keys in
     * the symbol table that are smaller. In other words, this key is the
     * ({@code rank}+1)st smallest key in the symbol table.
     *
     * @param rank the order statistic
     * @return the key in the symbol table of given {@code rank}
     * @throws IllegalArgumentException unless {@code rank} is between 0 and
     *                                  <em>n</em>–1
     */
    public Key select(int rank) {
        if (rank < 0 || rank >= size()) {
            throw new IllegalArgumentException("argument to select() is invalid: " + rank);
        }
        return select(rootNode, rank);
    }

    // Return key in BinarySearchTreeSymbolTable rooted at x of given rank.
    // Precondition: rank is in legal range.
    private Key select(Node x, int rank) {
        if (x == null) return null;
        int leftSize = size(x.leftSubTree);
        if (leftSize > rank) return select(x.leftSubTree, rank);
        else if (leftSize < rank) return select(x.rightSubTree, rank - leftSize - 1);
        else return x.key;
    }

    /**
     * Return the number of keys in the symbol table strictly less than {@code key}.
     *
     * @param key the key
     * @return the number of keys in the symbol table strictly less than {@code key}
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public int rank(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to rank() is null");
        return rank(key, rootNode);
    }

    // Number of keys in the subtree less than key.
    private int rank(Key key, Node x) {
        if (x == null) return 0;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) return rank(key, x.leftSubTree);
        else if (cmp > 0) return 1 + size(x.leftSubTree) + rank(key, x.rightSubTree);
        else return size(x.leftSubTree);
    }

    /**
     * Returns all keys in the symbol table as an {@code Iterable}.
     * To iterate over all of the keys in the symbol table named {@code st},
     * use the foreach notation: {@code for (Key key : st.keys())}.
     *
     * @return all keys in the symbol table
     */
    public Iterable<Key> keys() {
        if (isEmpty()) return new Queue<Key>();
        return keys(min(), max());
    }

    /**
     * Returns all keys in the symbol table in the given range,
     * as an {@code Iterable}.
     *
     * @param lo minimum endpoint
     * @param hi maximum endpoint
     * @return all keys in the symbol table between {@code lo}
     * (inclusive) and {@code hi} (inclusive)
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *                                  is {@code null}
     */
    public Iterable<Key> keys(Key lo, Key hi) {
        if (lo == null) throw new IllegalArgumentException("first argument to keys() is null");
        if (hi == null) throw new IllegalArgumentException("second argument to keys() is null");

        Queue<Key> queue = new Queue<Key>();
        keys(rootNode, queue, lo, hi);
        return queue;
    }

    private void keys(Node x, Queue<Key> queue, Key lo, Key hi) {
        if (x == null) return;

        /* 判断区间的范围 */
        // 比较左区间、右区间与节点key
        int cmplo = lo.compareTo(x.key);
        int cmphi = hi.compareTo(x.key);

        // 1 区间横跨左子树
        if (cmplo < 0) keys(x.leftSubTree, queue, lo, hi);
        // 2 区间横跨根节点
        if (cmplo <= 0 && cmphi >= 0) queue.enqueue(x.key);
        // 3 区间横跨右子树
        if (cmphi > 0) keys(x.rightSubTree, queue, lo, hi);
    }

    /**
     * Returns the number of keys in the symbol table in the given range.
     *
     * @param lo minimum endpoint
     * @param hi maximum endpoint
     * @return the number of keys in the symbol table between {@code lo}
     * (inclusive) and {@code hi} (inclusive)
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *                                  is {@code null}
     */
    public int size(Key lo, Key hi) {
        if (lo == null) throw new IllegalArgumentException("first argument to itsNodesAmount() is null");
        if (hi == null) throw new IllegalArgumentException("second argument to itsNodesAmount() is null");

        if (lo.compareTo(hi) > 0) return 0;
        if (contains(hi)) return rank(hi) - rank(lo) + 1;
        else return rank(hi) - rank(lo);
    }

    /**
     * Returns the height of the BinarySearchTreeSymbolTable (for debugging).
     *
     * @return the height of the BinarySearchTreeSymbolTable (a 1-node tree has height 0)
     */
    public int height() {
        return height(rootNode);
    }

    private int height(Node x) {
        if (x == null) return -1;
        return 1 + Math.max(height(x.leftSubTree), height(x.rightSubTree));
    }

    /**
     * Returns the keys in the BinarySearchTreeSymbolTable in level order (for debugging).
     *
     * @return the keys in the BinarySearchTreeSymbolTable in level order traversal
     */
    public Iterable<Key> levelOrder() {
        Queue<Key> keys = new Queue<Key>();
        Queue<Node> queue = new Queue<Node>();
        queue.enqueue(rootNode);
        while (!queue.isEmpty()) {
            Node x = queue.dequeue();
            if (x == null) continue;
            keys.enqueue(x.key);
            queue.enqueue(x.leftSubTree);
            queue.enqueue(x.rightSubTree);
        }
        return keys;
    }

    /*************************************************************************
     *  Check integrity of BinarySearchTreeSymbolTable data structure.
     ***************************************************************************/
    private boolean check() {
        if (!isBSTFromWebsite()) StdOut.println("Not in symmetric order");
        if (!isSizeConsistent()) StdOut.println("Subtree counts not consistent");
        if (!isRankConsistent()) StdOut.println("Ranks not consistent");
        return isBSTFromWebsite() && isSizeConsistent() && isRankConsistent();
    }

    // does this binary tree satisfy symmetric order?
    // Note: this test also ensures that data structure is a binary tree since order is strict
    private boolean isBSTFromWebsite() {
        return isBSTFromWebsite(rootNode, null, null);
    }

    // is the tree rooted at x a BinarySearchTreeSymbolTable with all keys strictly between min and max
    // (if min or max is null, treat as empty constraint)
    // Credit: Bob Dondero's elegant solution
    private boolean isBSTFromWebsite(Node x, Key min, Key max) {
        if (x == null) return true;
        if (min != null && x.key.compareTo(min) <= 0) return false;
        if (max != null && x.key.compareTo(max) >= 0) return false;
        return isBSTFromWebsite(x.leftSubTree, min, x.key) && isBSTFromWebsite(x.rightSubTree, x.key, max);
    }

    // are the itsNodesAmount fields correct?
    private boolean isSizeConsistent() {
        return isSizeConsistent(rootNode);
    }

    private boolean isSizeConsistent(Node x) {
        if (x == null) return true;
        if (x.itsNodesAmount != size(x.leftSubTree) + size(x.rightSubTree) + 1) return false;
        return isSizeConsistent(x.leftSubTree) && isSizeConsistent(x.rightSubTree);
    }

    // check that ranks are consistent
    private boolean isRankConsistent() {
        for (int i = 0; i < size(); i++)
            if (i != rank(select(i))) return false;
        for (Key key : keys())
            if (key.compareTo(select(rank(key))) != 0) return false;
        return true;
    }


    /**
     * Unit tests the {@code BinarySearchTreeSymbolTable} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        BinarySearchTreeSymbolTable<String, Integer> st = new BinarySearchTreeSymbolTable<String, Integer>();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            st.put(key, i);
        }

//        for (String s : st.levelOrder())
//            StdOut.println(s + " " + st.get(s));

        StdOut.println();

        for (String s : st.keys())
            StdOut.println(s + " " + st.get(s));
    }
}