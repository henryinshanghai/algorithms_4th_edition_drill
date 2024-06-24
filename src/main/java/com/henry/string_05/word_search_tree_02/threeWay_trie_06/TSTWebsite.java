package com.henry.string_05.word_search_tree_02.threeWay_trie_06; /******************************************************************************
 *  Compilation:  javac TST.java
 *  Execution:    java TST < words.txt
 *  Dependencies: StdIn.java
 *  Data files:   https://algs4.cs.princeton.edu/52trie/shellsST.txt
 *
 *  Symbol table with string keys, implemented using a ternary search
 *  trie (TST).
 *
 *
 *  % java TST < shellsST.txt
 *  keys(""):
 *  by 4
 *  sea 6
 *  sells 1
 *  she 0
 *  shells 3
 *  shore 7
 *  the 5
 *
 *  longestPrefixOf("shellsort"):
 *  shells
 *
 *  keysWithPrefix("shor"):
 *  shore
 *
 *  keysThatMatch(".he.l."):
 *  shells
 *
 *  % java TST
 *  theory the now is the time for all good men
 *
 *  Remarks
 *  --------
 *    - can't use a key that is the empty string ""
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * The {@code TST} class represents a symbol table of key-value
 * pairs, with string keys and generic values.
 * It supports the usual <em>put</em>, <em>get</em>, <em>contains</em>,
 * <em>delete</em>, <em>size</em>, and <em>is-empty</em> methods.
 * It also provides character-based methods for finding the string
 * in the symbol table that is the <em>longest prefix</em> of a given prefix,
 * finding all strings in the symbol table that <em>start with</em> a given prefix,
 * and finding all strings in the symbol table that <em>match</em> a given pattern.
 * A symbol table implements the <em>associative array</em> abstraction:
 * when associating a value with a key that is already in the symbol table,
 * the convention is to replace the old value with the new value.
 * Unlike {@link java.util.Map}, this class uses the convention that
 * values cannot be {@code null}—setting the
 * value associated with a key to {@code null} is equivalent to deleting the key
 * from the symbol table.
 * <p>
 * This implementation uses a ternary search trie.
 * <p>
 * For additional documentation, see <a href="https://algs4.cs.princeton.edu/52trie">Section 5.2</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
public class TSTWebsite<Value> {
    private int keysAmount;              // size
    private Node<Value> root;   // root of TST

    private static class Node<Value> {
        private char character;                        // character
        private Node<Value> leftSubtree, midSubtree, rightSubtree;  // left, middle, and right subtries
        private Value value;                     // value associated with string
    }

    /**
     * Initializes an empty string symbol table.
     */
    public TSTWebsite() {
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     *
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return keysAmount;
    }

    /**
     * Does this symbol table contain the given key?
     *
     * @param key the key
     * @return {@code true} if this symbol table contains {@code key} and
     * {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        return get(key) != null;
    }

    // 返回trie中，传入的字符串 所关联的值
    public Value get(String passedStr) {
        if (passedStr == null) {
            throw new IllegalArgumentException("calls get() with null argument");
        }
        if (passedStr.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        Node<Value> foundNode = getNodeForLastCharacterOf(root, passedStr, 0);
        if (foundNode == null) return null;
        return foundNode.value;
    }

    // 返回 传入的字符串参数（的尾字符） 在trie中对应的结点
    private Node<Value> getNodeForLastCharacterOf(Node<Value> currentRootNode, String passedStr, int currentCharacterSpot) {
        if (currentRootNode == null) return null;
        if (passedStr.length() == 0) throw new IllegalArgumentException("key must have length >= 1");

        // 获取“查询字符串”的当前字符
        char currentCharacter = passedStr.charAt(currentCharacterSpot);

        // 比较 当前字符 与 树的根结点中存储的字符
        if (currentCharacter < currentRootNode.character)
            // 如果更小，说明 当前字符 只会出现在left sub trie中，则：在左子树中继续查找 该字符
            return getNodeForLastCharacterOf(currentRootNode.leftSubtree, passedStr, currentCharacterSpot);
        else if (currentCharacter > currentRootNode.character)
            // 如果更大，说明 当前字符 只会出现在right sub trie中，则：在右子树中继续查找 该字符
            return getNodeForLastCharacterOf(currentRootNode.rightSubtree, passedStr, currentCharacterSpot);
        else if (currentCharacterSpot < passedStr.length() - 1)
            // 如果相等，说明 当前字符 与 当前结点匹配成功；&& 如果 当前字符 不是“待查询字符串的尾字符”，说明 还没有找到 想要找到的结点
            // 则：在中子树中，继续查找 下一个字符
            return getNodeForLastCharacterOf(currentRootNode.midSubtree, passedStr, currentCharacterSpot + 1);
            // 如果当前字符 与 当前结点的value匹配 && 当前字符是待查询字符串的尾字符，说明 已经找到了 想要的结点，则：返回这个结点
        else return currentRootNode;
    }

    // 把键值对 插入到 符号表中，如果键已经存在于符号表中，则 覆盖旧的value
    // 如果传入的value是null，那么 这个操作就会 从符号表中把传入的key给删除掉
    public void put(String passedKey, Value associatedValue) {
        if (passedKey == null) {
            throw new IllegalArgumentException("calls put() with null key");
        }
        if (!contains(passedKey)) keysAmount++;
        else if (associatedValue == null) keysAmount--;       // delete existing key
        root = putNodesOfPathThatStartFrom(root, passedKey, associatedValue, 0);
    }

    private Node<Value> putNodesOfPathThatStartFrom(Node<Value> currentRootNode, String passedKey, Value associatedValue, int currentCharacterSpot) {
        // 获取“待插入字符串”的当前字符
        char currentCharacterOfPassedKey = passedKey.charAt(currentCharacterSpot);

        // 如果trie中的当前结点为null，说明trie中不存在有此字符，则：在trie树中，为当前字符创建新结点 and??
        if (currentRootNode == null) {
            currentRootNode = new Node<Value>();
            currentRootNode.character = currentCharacterOfPassedKey;
        }

        // 如果trie树中的当前结点不为null，说明结点中存在有一个字符，则：把结点中的字符 与 字符串中的当前字符 进行比较
        if (currentCharacterOfPassedKey < currentRootNode.character) {
            // 如果“当前字符”比起“根结点中的字符”更小，说明应该在left sub trie中插入当前字符，则：在左子树中继续执行插入
            currentRootNode.leftSubtree = putNodesOfPathThatStartFrom(currentRootNode.leftSubtree, passedKey, associatedValue, currentCharacterSpot);
        } else if (currentCharacterOfPassedKey > currentRootNode.character)
            // 如果“当前字符”比起“根结点中的字符”更大，说明应该在right sub trie中插入当前字符，则：在右子树中继续执行插入
            currentRootNode.rightSubtree = putNodesOfPathThatStartFrom(currentRootNode.rightSubtree, passedKey, associatedValue, currentCharacterSpot);
        else if (currentCharacterSpot < passedKey.length() - 1)
            // 如果“当前字符” 与 “根结点中的字符”相等，说明 当前字符在trie树中已经存在了
            // 并且如果“当前字符”不是“尾字符”，说明 字符串中的字符还没有完全添加到trie树中，则：在中子树中继续执行插入“下一个字符”
            currentRootNode.midSubtree = putNodesOfPathThatStartFrom(currentRootNode.midSubtree, passedKey, associatedValue, currentCharacterSpot + 1);
        else
            // 如果向trie树中添加到了 “尾字符”，说明 已经到达了 要绑定value的结点，则：为此结点绑定 associatedValue
            currentRootNode.value = associatedValue;

        /* #3 在向trie树中插入“字符串的每个字符”之后，返回三向单词查找树的根结点 */
        return currentRootNode;
    }

    // 返回符号表中存在的、作为指定 字符串的最长前缀的键。如果不存在，则返回null
    public String keyThatIsLongestPrefixOf(String passedStr) {
        // 传入的字符串对象是 null
        if (passedStr == null) {
            throw new IllegalArgumentException("calls longestPrefixOf() with null argument");
        }
        // 传入的字符串是 空字符串
        if (passedStr.length() == 0) return null;

        int longestPrefixLength = 0;
        Node<Value> currentNode = root;
        int currentCharacterSpot = 0;

        // 在trie树中，逐步深入地查找 前缀key，并最终得到 最长前缀key
        // 循环结束条件：#1 到达trie树的叶子结点; #2 字符位置到达结束位置
        while (currentNode != null && currentCharacterSpot < passedStr.length()) {
            // 获取到 当前字符
            char currentCharacter = passedStr.charAt(currentCharacterSpot);
            // 比较 当前字符 与 当前结点中的字符...
            if (currentCharacter < currentNode.character)
                currentNode = currentNode.leftSubtree;
            else if (currentCharacter > currentNode.character)
                currentNode = currentNode.rightSubtree;
            else { // 如果两个字符相等, 说明在trie树中匹配到了当前字符，则：继续匹配 字符串中的下一个字符
                currentCharacterSpot++;
                // 如果 当前结点的value不为null，说明 找到了一个有效的key，则：使用 当前字符位置 来 更新“最长前缀长度”
                if (currentNode.value != null) longestPrefixLength = currentCharacterSpot;
                // 找到有效的key之后，更新当前结点 以便 继续在trie树中找到 更长的前缀key
                currentNode = currentNode.midSubtree;
            }
        }

        // 使用得到的“最长前缀长度” 来 从字符串中截取得到 最长前缀键
        return passedStr.substring(0, longestPrefixLength);
    }

    /**
     * Returns all keys in the symbol table as an {@code Iterable}.
     * To iterate over all of the keys in the symbol table named {@code st},
     * use the foreach notation: {@code for (Key key : st.keys())}.
     *
     * @return all keys in the symbol table as an {@code Iterable}
     */
    public Iterable<String> keys() {
        Queue<String> queue = new Queue<String>();
        collect(root, new StringBuilder(), queue);
        return queue;
    }

    /**
     * Returns all of the keys in the set that start with {@code prefix}.
     *
     * @param prefix the prefix
     * @return all of the keys in the set that start with {@code prefix},
     * as an iterable
     * @throws IllegalArgumentException if {@code prefix} is {@code null}
     */
    public Iterable<String> keysWithPrefix(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("calls keysWithPrefix() with null argument");
        }
        Queue<String> queue = new Queue<String>();
        Node<Value> x = getNodeForLastCharacterOf(root, prefix, 0);
        if (x == null) return queue;
        if (x.value != null) queue.enqueue(prefix);
        collect(x.midSubtree, new StringBuilder(prefix), queue);
        return queue;
    }

    // all keys in subtrie rooted at x with given prefix
    private void collect(Node<Value> x, StringBuilder prefix, Queue<String> queue) {
        if (x == null) return;
        collect(x.leftSubtree, prefix, queue);
        if (x.value != null) queue.enqueue(prefix.toString() + x.character);
        collect(x.midSubtree, prefix.append(x.character), queue);
        prefix.deleteCharAt(prefix.length() - 1);
        collect(x.rightSubtree, prefix, queue);
    }


    /**
     * Returns all of the keys in the symbol table that match {@code pattern},
     * where the character '.' is interpreted as a wildcard character.
     *
     * @param pattern the pattern
     * @return all of the keys in the symbol table that match {@code pattern},
     * as an iterable, where . is treated as a wildcard character.
     */
    public Iterable<String> keysThatMatch(String pattern) {
        Queue<String> queue = new Queue<String>();
        collect(root, new StringBuilder(), 0, pattern, queue);
        return queue;
    }

    private void collect(Node<Value> x, StringBuilder prefix, int i, String pattern, Queue<String> queue) {
        if (x == null) return;
        char c = pattern.charAt(i);
        if (c == '.' || c < x.character) collect(x.leftSubtree, prefix, i, pattern, queue);
        if (c == '.' || c == x.character) {
            if (i == pattern.length() - 1 && x.value != null) queue.enqueue(prefix.toString() + x.character);
            if (i < pattern.length() - 1) {
                collect(x.midSubtree, prefix.append(x.character), i + 1, pattern, queue);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
        if (c == '.' || c > x.character) collect(x.rightSubtree, prefix, i, pattern, queue);
    }


    /**
     * Unit tests the {@code TST} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        // build symbol table from standard input
        TSTWebsite<Integer> st = new TSTWebsite<Integer>();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            st.put(key, i);
        }

        // print results
        if (st.size() < 100) {
            StdOut.println("keys(\"\"):");
            for (String key : st.keys()) {
                StdOut.println(key + " " + st.get(key));
            }
            StdOut.println();
        }

        StdOut.println("longestPrefixOf(\"shellsort\"):");
        StdOut.println(st.keyThatIsLongestPrefixOf("shellsort"));
        StdOut.println();

        StdOut.println("longestPrefixOf(\"shell\"):");
        StdOut.println(st.keyThatIsLongestPrefixOf("shell"));
        StdOut.println();

        StdOut.println("keysWithPrefix(\"shor\"):");
        for (String s : st.keysWithPrefix("shor"))
            StdOut.println(s);
        StdOut.println();

        StdOut.println("keysThatMatch(\".he.l.\"):");
        for (String s : st.keysThatMatch(".he.l."))
            StdOut.println(s);
    }
}