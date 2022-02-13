package com.henry.symbol_table_chapter_03.balance_search_tree_03.delete_02; /******************************************************************************
 *  Compilation:  javac RedBlackBST.java
 *  Execution:    java RedBlackBST < input.txt
 *  Dependencies: StdIn.java StdOut.java  
 *  Data files:   https://algs4.cs.princeton.edu/33balanced/tinyST.txt  
 *
 *  A symbol table implemented using a left-leaning red-black BST.
 *  This is the 2-3 version.
 *
 *  Note: commented out assertions because DrJava now enables assertions
 *        by default.
 *
 *  % more tinyST.txt
 *  S E A R C H E X A M P L E
 *
 *  % java RedBlackBST < tinyST.txt
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
 *  The {@code BST} class represents an ordered symbol table of generic
 *  key-value pairs.
 *  It supports the usual <em>put</em>, <em>get</em>, <em>contains</em>,
 *  <em>delete</em>, <em>size</em>, and <em>is-empty</em> methods.
 *  It also provides ordered methods for finding the <em>minimum</em>,
 *  <em>maximum</em>, <em>floor</em>, and <em>ceiling</em>.
 *  It also provides a <em>keys</em> method for iterating over all of the keys.
 *  A symbol table implements the <em>associative array</em> abstraction:
 *  when associating a value with a key that is already in the symbol table,
 *  the convention is to replace the old value with the new value.
 *  Unlike {@link java.util.Map}, this class uses the convention that
 *  values cannot be {@code null}—setting the
 *  value associated with a key to {@code null} is equivalent to deleting the key
 *  from the symbol table.
 *  <p>
 *  It requires that
 *  the key type implements the {@code Comparable} interface and calls the
 *  {@code compareTo()} and method to compare two keys. It does not call either
 *  {@code equals()} or {@code hashCode()}.
 *  <p>
 *  This implementation uses a <em>left-leaning red-black BST</em>. 
 *  The <em>put</em>, <em>get</em>, <em>contains</em>, <em>remove</em>,
 *  <em>minimum</em>, <em>maximum</em>, <em>ceiling</em>, <em>floor</em>,
 *  <em>rank</em>, and <em>select</em> operations each take
 *  &Theta;(log <em>n</em>) time in the worst case, where <em>n</em> is the
 *  number of key-value pairs in the symbol table.
 *  The <em>size</em>, and <em>is-empty</em> operations take &Theta;(1) time.
 *  The <em>keys</em> methods take
 *  <em>O</em>(log <em>n</em> + <em>m</em>) time, where <em>m</em> is
 *  the number of keys returned by the iterator.
 *  Construction takes &Theta;(1) time.
 *  <p>
 *  For alternative implementations of the symbol table API, see {@link ST},
 *  {@link BinarySearchST}, {@link SequentialSearchST}, {@link BST},
 *  {@link SeparateChainingHashST}, {@link LinearProbingHashST}, and
 *  {@link AVLTreeST}.
 *  For additional documentation, see
 *  <a href="https://algs4.cs.princeton.edu/33balanced">Section 3.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */

public class RedBlackBST<Key extends Comparable<Key>, Value> {

    private static final boolean RED   = true;
    private static final boolean BLACK = false;

    private Node root;     // root of the BST

    // BST helper node data type
    private class Node {
        private Key key;           // key
        private Value val;         // associated data
        private Node left, right;  // links to left and right subtrees
        private boolean color;     // color of parent link
        private int size;          // subtree count

        public Node(Key key, Value val, boolean color, int size) {
            this.key = key;
            this.val = val;
            this.color = color;
            this.size = size;
        }
    }

    /**
     * Initializes an empty symbol table.
     */
    public RedBlackBST() {
    }

    /***************************************************************************
     *  Node helper methods.
     ***************************************************************************/
    // is node x red; false if x is null ?
    private boolean isRed(Node x) {
        if (x == null) return false;
        return x.color == RED;
    }

    // number of node in subtree rooted at x; 0 if x is null
    private int size(Node x) {
        if (x == null) return 0;
        return x.size;
    }


    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return size(root);
    }

    /**
     * Is this symbol table empty?
     * @return {@code true} if this symbol table is empty and {@code false} otherwise
     */
    public boolean isEmpty() {
        return root == null;
    }


    /***************************************************************************
     *  Standard BST search.
     ***************************************************************************/

    /**
     * Returns the value associated with the given key.
     * @param key the key
     * @return the value associated with the given key if the key is in the symbol table
     *     and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        return get(root, key);
    }

    // value associated with the given key in subtree rooted at x; null if no such key
    private Value get(Node x, Key key) {
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if      (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else              return x.val;
        }
        return null;
    }

    /**
     * Does this symbol table contain the given key?
     * @param key the key
     * @return {@code true} if this symbol table contains {@code key} and
     *     {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(Key key) {
        return get(key) != null;
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
     * @param key the key
     * @param val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(Key key, Value val) {
        if (key == null) throw new IllegalArgumentException("first argument to put() is null");
        if (val == null) {
            delete(key);
            return;
        }

        root = put(root, key, val);
        root.color = BLACK;
        // assert check();
    }

    // insert the key-value pair in the subtree rooted at h
    private Node put(Node h, Key key, Value val) {
        if (h == null) return new Node(key, val, RED, 1);

        int cmp = key.compareTo(h.key);
        if      (cmp < 0) h.left  = put(h.left,  key, val);
        else if (cmp > 0) h.right = put(h.right, key, val);
        else              h.val   = val;

        // fix-up any right-leaning links
        if (isRed(h.right) && !isRed(h.left))      h = rotateLeft(h);
        if (isRed(h.left)  &&  isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left)  &&  isRed(h.right))     flipColors(h);
        h.size = size(h.left) + size(h.right) + 1;

        return h;
    }

    /***************************************************************************
     *  Red-black tree deletion.
     ***************************************************************************/

    /**
     * Removes the smallest key and associated value from the symbol table.
     * @throws NoSuchElementException if the symbol table is empty
     * 通过保持与文本中给出的转换的对应关系来实现RedBlackBST.java的deleteMin（）操作；
     * 作用：1 以使树的左脊向下移动；
     * 2 同时保持不变————即当前节点不是2节点。
     * Implement the deleteMin() operation for RedBlackBST.java
     *  by maintaining the correspondence with the transformations given in the text
     *  for moving down the left spine of the tree
     *  while maintaining the invariant that the current node is not a 2-node.
     */
    public void deleteMin() {
        if (isEmpty()) throw new NoSuchElementException("BST underflow");

        // if both children of root are black, set root to red
        // 如果根节点的两个子节点都是黑色，就设置根节点的颜色为红色
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;

        // 把删除最小节点后的树重新绑定给root
        root = deleteMin(root);
        if (!isEmpty()) root.color = BLACK;
        // assert check();
    }

    // 在h树中删除最小键的key-value对
    // delete the key-value pair with the minimum key rooted at h
    /**
     * 通过保持与文本描述中给出的转换的对应关系来实现RedBlackBST.java的deleteMin（）操作，以使树的左脊向下移动，同时保持不变，即当前节点不是2节点。
     * 递归方法作用：从树中删除最小键的键值对，并返回更新后的树
     * @param h
     * @return
     */
    private Node deleteMin(Node h) {
        // 递归终结条件：左子树为null时，为什么返回的是null?
        // 这与2-3树的生成方式有关
        // 当有两个键值对时，就会构造出一个3-节点
        // 把一棵仅由一个3-节点组成的2-3树转化为一个红黑树（根节点 + 左子节点）
        // 因此，如果根节点的左子节点为null 说明当前2-3树中就只有一个节点  这个节点就是最小节点 删除之后，就只有一棵空树了 所以return null
        if (h.left == null)
            return null;

        // 如果左子树不是红色的，而且左子树的左子树也不是红色的
        // 这说明了什么？
        // 1 节点的左子树不是红色的，说明节点不是一个3-节点
        // 2 节点的左子节点也不是3-节点
        // Ⅰ 递归前，对当前节点进行处理————避免删除路径的当前节点是一个2-节点（这样就不会从2-节点中删除键值对）
        if (!isRed(h.left) && !isRed(h.left.left)) // 删除路径上出现了两个连续的2-节点
            // 假设h是红色的，而h.left、h.left.left都是黑色的，使h.left或者它的某个子节点变成红色
            h = moveRedLeft(h);

        // Ⅱ 执行删除操作，并把删除最小节点后的左子树重新绑定到根结点上    注：删除最小节点后可能会导致红黑树颜色失衡
        h.left = deleteMin(h.left);

        // Ⅲ 回溯时，对树进行平衡操作
        return balance(h);
    }


    /**
     * Removes the largest key and associated value from the symbol table.
     * @throws NoSuchElementException if the symbol table is empty
     *
     *  Implement the deleteMax() operation for RedBlackBST.java.
     *  Note that the transformations involved
     *      differ slightly from those in the previous exercise
     *      because red links are left-leaning.
     *  请注意，涉及的转换与上一个练习中的转换略有不同，因为红色链接向左倾斜。
     */
    public void deleteMax() {
        if (isEmpty()) throw new NoSuchElementException("BST underflow");

        // if both children of root are black, set root to red
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;

        root = deleteMax(root);
        if (!isEmpty()) root.color = BLACK;
        // assert check();
    }

    // delete the key-value pair with the maximum key rooted at h
    private Node deleteMax(Node h) {
        // Ⅰ 递归调用之前做一些事情    作用：避免当前节点是一个2-节点
        if (isRed(h.left)) // 1 左子树为红节点   这说明右子树是一个黑节点（2-节点）  这不是预期的，所以需要更新当前树来避免查找路径上的2-节点
            // 作用：更新当前节点    手段：使一个左倾的链接转向右倾
            // 更新后，在查找路径上得到一个3-节点
            h = rotateRight(h);

        if (h.right == null) // 2 右子树为空     这说明树中就只有一个节点
            // 作用：？？？   手段：返回null       如果右子树为空，则根据性质5 左子树中只能有两种情况：1 左子树为空 2 左子树中只包含红节点（这种情况已经在第一个if()中处理，它会得到一个包含右节点的树 所以2不存在）
            // 当右子树为null时，说明树中就只有一个节点
            return null;

        if (!isRed(h.right) && !isRed(h.right.left)) // 右子树为黑节点&右子树的左子树为黑节点     判断查找路径上的当前节点是否为2-节点 这里的h.right.left是因为红链接是左倾的
            // 更新当前节点   作用：避免当前节点（的右子节点）是一个2-节点
            h = moveRedRight(h);

        h.right = deleteMax(h.right);

        return balance(h);
    }

    /**
     * Removes the specified key and its associated value from this symbol table     
     * (if the key is in this symbol table).    
     *
     * @param  key the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to delete() is null");
        if (!contains(key)) return;

        // if both children of root are black, set root to red
        // 如果root节点的两个子节点都是黑色，就设置根节点为红色
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;

        root = delete(root, key);
        if (!isEmpty()) root.color = BLACK;
        // assert check();
    }

    // delete the key-value pair with the given key rooted at h
    // 为RedBlackBST.java实现delete（）操作，将前两个练习的方法与BST的delete（）操作结合起来。
    // 如果是删除随机的节点，如何能确定查找路径呢？
    private Node delete(Node h, Key key) {
        // assert get(h, key) != null;


        if (key.compareTo(h.key) < 0)  { // 预期删除的节点在左子树中
            // 更新当前节点 确保它不是2-节点
            if (!isRed(h.left) && !isRed(h.left.left)) // 如何判断一个节点是不是2-节点呢？
                h = moveRedLeft(h);
            // 从左子树中删除预期节点
            h.left = delete(h.left, key);
        }
        else { // 预期删除的节点在右子树中  任务：避免当前节点是一个2-节点
            // condition1 左节点为红色
            if (isRed(h.left))
                h = rotateRight(h); // 把左子节点作为新的根节点，旧的根节点会进入到右子树中

            // condition2 根节点即为预期删除的节点，并且右节点为空
            if (key.compareTo(h.key) == 0 && (h.right == null))
                return null; // 为什么返回空？ can not make sense

            // condition3 右子节点为2-节点 并且右子节点的左子节点也是2-节点
            if (!isRed(h.right) && !isRed(h.right.left)) // this seems like the former condition in deleteMin()
                // 执行相应操作，避免当前节点为2-节点
                h = moveRedRight(h);

            // condition4 如果根节点刚好是预期删除的节点
            if (key.compareTo(h.key) == 0) { // this is like delete in BST
                // 先覆盖预期删除的节点 使用它的successor
                Node x = min(h.right);
                h.key = x.key;
                h.val = x.val;
                // h.val = get(h.right, min(h.right).key);
                // h.key = min(h.right).key;
                h.right = deleteMin(h.right); // 再使用删除节点后的树更新右子树
            }

            // 如果不是上面的任何一种情况 直接更新右子树 THIS is make no sense too. why there is so many if?
            else h.right = delete(h.right, key);
        }
        return balance(h);
    }

    /***************************************************************************
     *  Red-black tree helper functions.
     ***************************************************************************/

    // make a left-leaning link lean to the right
    // 使一个左倾的链接转向右倾
    private Node rotateRight(Node h) {
        // assert (h != null) && isRed(h.left);
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = x.right.color;
        x.right.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }

    // make a right-leaning link lean to the left
    private Node rotateLeft(Node h) {
        // assert (h != null) && isRed(h.right);
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = x.left.color;
        x.left.color = RED;

        // 新增了设置size的代码
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }

    // flip the colors of a node and its two children
    private void flipColors(Node h) {
        // h must have opposite color of its two children
        // assert (h != null) && (h.left != null) && (h.right != null);
        // assert (!isRed(h) &&  isRed(h.left) &&  isRed(h.right))
        //    || (isRed(h)  && !isRed(h.left) && !isRed(h.right));
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    // Assuming that h is red and both h.left and h.left.left
    // are black, make h.left or one of its children red.
    // 假设h是红色的，而h.left、h.left.left都是黑色的，使h.left或者它的某个子节点变成红色
    private Node moveRedLeft(Node h) {
        // assert (h != null);
        // assert isRed(h) && !isRed(h.left) && !isRed(h.left.left);

        flipColors(h); // Ⅰ 对当前节点的颜色进行翻转
        if (isRed(h.right.left)) { // Ⅱ 如果右子树的左节点是红色的...    从右子树中借一个2-节点
            // ① 对右子树进行右旋转操作
            h.right = rotateRight(h.right);
            // ② 对当前节点进行左旋转操作
            h = rotateLeft(h);
            // ③ 翻转当前节点的颜色
            flipColors(h);
        }
        return h; // somehow,当前节点就不会再是2-节点了
    }

    // Assuming that h is red and both h.right and h.right.left
    // are black, make h.right or one of its children red.
    // 假设说h是红节点，而h.right与h.right.left都是黑节点,以下代码使得h.right或者h.right的一个子节点变成红色
    private Node moveRedRight(Node h) { // 当前h是一个红子树
        // assert (h != null);
        // assert isRed(h) && !isRed(h.right) && !isRed(h.right.left);

        flipColors(h); // 1 翻转节点（为红色节点）的链接颜色
        if (isRed(h.left.left)) { // 2 判断节点的左子节点的左子节点是否为红色————这决定了是case2 OR case3
            // 如果是case3
            h = rotateRight(h); // ① 对节点执行右旋操作 相当于从左子树借了一个节点
            flipColors(h); // ② 翻转颜色    恢复根节点的初始颜色
        }

        // 如果是case2 只做颜色翻转即可 规则：会把3个2-节点合并成一个4-节点
        return h;
    }

    // restore red-black tree invariant
    // 恢复红黑树的不变性 这是一套固定流程
    private Node balance(Node h) {
        // assert (h != null);

        // 需要颜色平衡的三种情况
        if (isRed(h.right))                      h = rotateLeft(h); // 红色右链接
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h); // 两个连续的红色左链接
        if (isRed(h.left) && isRed(h.right))     flipColors(h); // 同一个节点连接左右红链接

        h.size = size(h.left) + size(h.right) + 1;
        return h;
    }


    /***************************************************************************
     *  Utility functions.
     ***************************************************************************/

    /**
     * Returns the height of the BST (for debugging).
     * @return the height of the BST (a 1-node tree has height 0)
     */
    public int height() {
        return height(root);
    }
    private int height(Node x) {
        if (x == null) return -1;
        return 1 + Math.max(height(x.left), height(x.right));
    }

    /***************************************************************************
     *  Ordered symbol table methods.
     ***************************************************************************/

    /**
     * Returns the smallest key in the symbol table.
     * @return the smallest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
    public Key min() {
        if (isEmpty()) throw new NoSuchElementException("calls min() with empty symbol table");
        return min(root).key;
    }

    // the smallest key in subtree rooted at x; null if no such key
    private Node min(Node x) {
        // assert x != null;
        if (x.left == null) return x;
        else                return min(x.left);
    }

    /**
     * Returns the largest key in the symbol table.
     * @return the largest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
    public Key max() {
        if (isEmpty()) throw new NoSuchElementException("calls max() with empty symbol table");
        return max(root).key;
    }

    // the largest key in the subtree rooted at x; null if no such key
    private Node max(Node x) {
        // assert x != null;
        if (x.right == null) return x;
        else                 return max(x.right);
    }


    /**
     * Returns the largest key in the symbol table less than or equal to {@code key}.
     * @param key the key
     * @return the largest key in the symbol table less than or equal to {@code key}
     * @throws NoSuchElementException if there is no such key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Key floor(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to floor() is null");
        if (isEmpty()) throw new NoSuchElementException("calls floor() with empty symbol table");
        Node x = floor(root, key);
        if (x == null) throw new NoSuchElementException("argument to floor() is too small");
        else           return x.key;
    }

    // the largest key in the subtree rooted at x less than or equal to the given key
    private Node floor(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (cmp < 0)  return floor(x.left, key);
        Node t = floor(x.right, key);
        if (t != null) return t;
        else           return x;
    }

    /**
     * Returns the smallest key in the symbol table greater than or equal to {@code key}.
     * @param key the key
     * @return the smallest key in the symbol table greater than or equal to {@code key}
     * @throws NoSuchElementException if there is no such key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Key ceiling(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to ceiling() is null");
        if (isEmpty()) throw new NoSuchElementException("calls ceiling() with empty symbol table");
        Node x = ceiling(root, key);
        if (x == null) throw new NoSuchElementException("argument to ceiling() is too small");
        else           return x.key;
    }

    // the smallest key in the subtree rooted at x greater than or equal to the given key
    private Node ceiling(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (cmp > 0)  return ceiling(x.right, key);
        Node t = ceiling(x.left, key);
        if (t != null) return t;
        else           return x;
    }

    /**
     * Return the key in the symbol table of a given {@code rank}.
     * This key has the property that there are {@code rank} keys in
     * the symbol table that are smaller. In other words, this key is the
     * ({@code rank}+1)st smallest key in the symbol table.
     *
     * @param  rank the order statistic
     * @return the key in the symbol table of given {@code rank}
     * @throws IllegalArgumentException unless {@code rank} is between 0 and
     *        <em>n</em>–1
     */
    public Key select(int rank) {
        if (rank < 0 || rank >= size()) {
            throw new IllegalArgumentException("argument to select() is invalid: " + rank);
        }
        return select(root, rank);
    }

    // Return key in BST rooted at x of given rank.
    // Precondition: rank is in legal range.
    private Key select(Node x, int rank) {
        if (x == null) return null;
        int leftSize = size(x.left);
        if      (leftSize > rank) return select(x.left,  rank);
        else if (leftSize < rank) return select(x.right, rank - leftSize - 1);
        else                      return x.key;
    }

    /**
     * Return the number of keys in the symbol table strictly less than {@code key}.
     * @param key the key
     * @return the number of keys in the symbol table strictly less than {@code key}
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public int rank(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to rank() is null");
        return rank(key, root);
    }

    // number of keys less than key in the subtree rooted at x
    private int rank(Key key, Node x) {
        if (x == null) return 0;
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) return rank(key, x.left);
        else if (cmp > 0) return 1 + size(x.left) + rank(key, x.right);
        else              return size(x.left);
    }

    /***************************************************************************
     *  Range count and range search.
     ***************************************************************************/

    /**
     * Returns all keys in the symbol table as an {@code Iterable}.
     * To iterate over all of the keys in the symbol table named {@code st},
     * use the foreach notation: {@code for (Key key : st.keys())}.
     * @return all keys in the symbol table as an {@code Iterable}
     */
    public Iterable<Key> keys() {
        if (isEmpty()) return new Queue<Key>();
        return keys(min(), max());
    }

    /**
     * Returns all keys in the symbol table in the given range,
     * as an {@code Iterable}.
     *
     * @param  lo minimum endpoint
     * @param  hi maximum endpoint
     * @return all keys in the symbol table between {@code lo}
     *    (inclusive) and {@code hi} (inclusive) as an {@code Iterable}
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *    is {@code null}
     */
    public Iterable<Key> keys(Key lo, Key hi) {
        if (lo == null) throw new IllegalArgumentException("first argument to keys() is null");
        if (hi == null) throw new IllegalArgumentException("second argument to keys() is null");

        Queue<Key> queue = new Queue<Key>();
        // if (isEmpty() || lo.compareTo(hi) > 0) return queue;
        keys(root, queue, lo, hi);
        return queue;
    }

    // add the keys between lo and hi in the subtree rooted at x
    // to the queue
    private void keys(Node x, Queue<Key> queue, Key lo, Key hi) {
        if (x == null) return;
        int cmplo = lo.compareTo(x.key);
        int cmphi = hi.compareTo(x.key);
        if (cmplo < 0) keys(x.left, queue, lo, hi);
        if (cmplo <= 0 && cmphi >= 0) queue.enqueue(x.key);
        if (cmphi > 0) keys(x.right, queue, lo, hi);
    }

    /**
     * Returns the number of keys in the symbol table in the given range.
     *
     * @param  lo minimum endpoint
     * @param  hi maximum endpoint
     * @return the number of keys in the symbol table between {@code lo}
     *    (inclusive) and {@code hi} (inclusive)
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *    is {@code null}
     */
    public int size(Key lo, Key hi) {
        if (lo == null) throw new IllegalArgumentException("first argument to size() is null");
        if (hi == null) throw new IllegalArgumentException("second argument to size() is null");

        if (lo.compareTo(hi) > 0) return 0;
        if (contains(hi)) return rank(hi) - rank(lo) + 1;
        else              return rank(hi) - rank(lo);
    }


    /***************************************************************************
     *  Check integrity of red-black tree data structure.
     ***************************************************************************/
    private boolean check() {
        if (!isBST())            StdOut.println("Not in symmetric order");
        if (!isSizeConsistent()) StdOut.println("Subtree counts not consistent");
        if (!isRankConsistent()) StdOut.println("Ranks not consistent");
        if (!is23())             StdOut.println("Not a 2-3 tree");
        if (!isBalanced())       StdOut.println("Not balanced");
        return isBST() && isSizeConsistent() && isRankConsistent() && is23() && isBalanced();
    }

    // does this binary tree satisfy symmetric order?
    // Note: this test also ensures that data structure is a binary tree since order is strict
    private boolean isBST() {
        return isBST(root, null, null);
    }

    // is the tree rooted at x a BST with all keys strictly between min and max
    // (if min or max is null, treat as empty constraint)
    // Credit: Bob Dondero's elegant solution
    private boolean isBST(Node x, Key min, Key max) {
        if (x == null) return true;
        if (min != null && x.key.compareTo(min) <= 0) return false;
        if (max != null && x.key.compareTo(max) >= 0) return false;
        return isBST(x.left, min, x.key) && isBST(x.right, x.key, max);
    }

    // are the size fields correct?
    private boolean isSizeConsistent() { return isSizeConsistent(root); }
    private boolean isSizeConsistent(Node x) {
        if (x == null) return true;
        if (x.size != size(x.left) + size(x.right) + 1) return false;
        return isSizeConsistent(x.left) && isSizeConsistent(x.right);
    }

    // check that ranks are consistent
    private boolean isRankConsistent() {
        for (int i = 0; i < size(); i++)
            if (i != rank(select(i))) return false;
        for (Key key : keys())
            if (key.compareTo(select(rank(key))) != 0) return false;
        return true;
    }

    // Does the tree have no red right links, and at most one (left)
    // red links in a row on any path?
    private boolean is23() { return is23(root); }
    private boolean is23(Node x) {
        if (x == null) return true;
        if (isRed(x.right)) return false;
        if (x != root && isRed(x) && isRed(x.left))
            return false;
        return is23(x.left) && is23(x.right);
    }

    // do all paths from root to leaf have same number of black edges?
    private boolean isBalanced() {
        int black = 0;     // number of black links on path from root to min
        Node x = root;
        while (x != null) {
            if (!isRed(x)) black++;
            x = x.left;
        }
        return isBalanced(root, black);
    }

    // does every path from the root to a leaf have the given number of black links?
    private boolean isBalanced(Node x, int black) {
        if (x == null) return black == 0;
        if (!isRed(x)) black--;
        return isBalanced(x.left, black) && isBalanced(x.right, black);
    }


    /**
     * Unit tests the {@code RedBlackBST} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        RedBlackBST<String, Integer> st = new RedBlackBST<String, Integer>();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            st.put(key, i);
        }
        StdOut.println();
        for (String s : st.keys())
            StdOut.println(s + " " + st.get(s));
        StdOut.println();
    }
}