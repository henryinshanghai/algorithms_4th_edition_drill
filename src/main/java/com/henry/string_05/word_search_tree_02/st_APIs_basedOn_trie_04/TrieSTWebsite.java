package com.henry.string_05.word_search_tree_02.st_APIs_basedOn_trie_04; /******************************************************************************
 *  Compilation:  javac TrieST.java
 *  Execution:    java TrieST < words.txt
 *  Dependencies: StdIn.java
 *  Data files:   https://algs4.cs.princeton.edu/52trie/shellsST.txt
 *
 *  A string symbol table for extended ASCII strings, implemented
 *  using a 256-way trie.
 *
 *  % java TrieST < shellsST.txt
 *  by 4
 *  sea 6
 *  sells 1
 *  she 0
 *  shells 3
 *  shore 7
 *  the 5
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * The {@code TrieST} class represents a symbol table of key-value
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
 * This implementation uses a 256-way trie.
 * The <em>put</em>, <em>contains</em>, <em>delete</em>, and
 * <em>longest prefix</em> operations take time proportional to the length
 * of the key (in the worst case). Construction takes constant time.
 * The <em>size</em>, and <em>is-empty</em> operations take constant time.
 * Construction takes constant time.
 * <p>
 * For additional documentation, see <a href="https://algs4.cs.princeton.edu/52trie">Section 5.2</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
public class TrieSTWebsite<Value> {
    private static final int R = 256;        // extended ASCII


    private Node root;      // root of trie
    private int n;          // number of keys in trie

    // R-way trie node
    private static class Node {
        private Object value;
        private Node[] characterToNodeArr = new Node[R]; // successorNodes
    }

    /**
     * Initializes an empty string symbol table.
     */
    public TrieSTWebsite() {
    }


    /**
     * Returns the value associated with the given key.
     *
     * @param key the key
     * @return the value associated with the given key if the key is in the symbol table
     * and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Value get(String key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        Node x = getLastNodeOfPathThatStartFrom(root, key, 0);
        if (x == null) return null;
        return (Value) x.value;
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
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != null;
    }

    private Node getLastNodeOfPathThatStartFrom(Node currentNode, String passedKey, int currentCharacterSpot) {
        if (currentNode == null) return null;
        if (currentCharacterSpot == passedKey.length()) return currentNode;
        char currentCharacter = passedKey.charAt(currentCharacterSpot);
        return getLastNodeOfPathThatStartFrom(currentNode.characterToNodeArr[currentCharacter], passedKey, currentCharacterSpot + 1);
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is {@code null}, this effectively deletes the key from the symbol table.
     *
     * @param key the key
     * @param val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(String key, Value val) {
        if (key == null) throw new IllegalArgumentException("first argument to put() is null");
        if (val == null) delete(key);
        else root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, Value val, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            if (x.value == null) n++;
            x.value = val;
            return x;
        }
        char c = key.charAt(d);
        x.characterToNodeArr[c] = put(x.characterToNodeArr[c], key, val, d + 1);
        return x;
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     *
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return n;
    }

    /**
     * Is this symbol table empty?
     *
     * @return {@code true} if this symbol table is empty and {@code false} otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns all keys in the symbol table as an {@code Iterable}.
     * To iterate over all of the keys in the symbol table named {@code st},
     * use the foreach notation: {@code for (Key key : st.keys())}.
     *
     * @return all keys in the symbol table as an {@code Iterable}
     */
    public Iterable<String> getIterableKeys() {
        return keysWithPrefixOf("");
    }

    /**
     * Returns all of the keys in the set that start with {@code prefix}.
     *
     * @param passedPrefix the prefix
     * @return all of the keys in the set that start with {@code prefix},
     * as an iterable
     */
    public Iterable<String> keysWithPrefixOf(String passedPrefix) {
        Queue<String> keysCollection = new Queue<String>();
        Node lastNodeOfPrefixStr = getLastNodeOfPathThatStartFrom(root, passedPrefix, 0);
        collectKeysStartWithPrefixInto(lastNodeOfPrefixStr, new StringBuilder(passedPrefix), keysCollection);
        return keysCollection;
    }

    private void collectKeysStartWithPrefixInto(Node currentNode, StringBuilder currentPrefix, Queue<String> keysCollection) {
        if (currentNode == null) return;
        if (currentNode.value != null) {
            String currentKey = currentPrefix.toString();
            keysCollection.enqueue(currentKey);
        }
        for (char currentAlphabetCharacter = 0; currentAlphabetCharacter < R; currentAlphabetCharacter++) {
            currentPrefix.append(currentAlphabetCharacter);
            collectKeysStartWithPrefixInto(currentNode.characterToNodeArr[currentAlphabetCharacter], currentPrefix, keysCollection);

            // ???
            currentPrefix.deleteCharAt(currentPrefix.length() - 1);
        }
    }

    /**
     * Returns all of the keys in the symbol table that match {@code pattern},
     * where the character '.' is interpreted as a wildcard character.
     *
     * @param patternStr the pattern
     * @return all of the keys in the symbol table that match {@code pattern},
     * as an iterable, where . is treated as a wildcard character.
     */
    public Iterable<String> keysThatMatch(String patternStr) {
        Queue<String> validKeysCollection = new Queue<String>();
        collectKeysStartWithPrefixThatMatchingPatternInto(root, new StringBuilder(), patternStr, validKeysCollection);
        return validKeysCollection;
    }

    // 原始任务：在单词查找树中，收集所有以 “指定的前缀字符串”(生成自“指定的模式字符串”) 作为前缀 而与“模式字符串”长度相等（匹配）的键
    // 匹配“指定模式字符串” 的键 <->
    private void collectKeysStartWithPrefixThatMatchingPatternInto(Node currentNode, StringBuilder currentPrefixStr, String patternStr, Queue<String> validKeysCollection) {
        // #1 如果已经到达叶子结点，说明 没有找到匹配条件的key，则：直接返回
        if (currentNode == null) return;
        int prefixStrLength = currentPrefixStr.length();
        // #2 如果已经查找到 patternStr的最后一个字符，并且 这个字符对应的结点上有值，说明 找到了满足条件的key，则：把key添加到集合中
        if (prefixStrLength == patternStr.length() && currentNode.value != null)
            validKeysCollection.enqueue(currentPrefixStr.toString());
        // #3 如果已经查找到了 patternStr的最后一个字符，但 字符对应的结点上没有值，说明 单词查找树中虽然存在所有字符，但没有满足条件的key，则：直接返回
        if (prefixStrLength == patternStr.length())
            return;

        // 获取到 patternStr的当前字符
        char currentCharacterOfPatternStr = patternStr.charAt(prefixStrLength);

        // 与书上提供的代码不一样 👇
        // 如果当前字符是 一个通配字符, 说明 当前字符在单词查找树中匹配成功，则：
        if (currentCharacterOfPatternStr == '.') {
            for (char currentCharacterOfAlphabet = 0; currentCharacterOfAlphabet < R; currentCharacterOfAlphabet++) {
                // 把字母表中的每一个字符，分别追加到 prefixStr上。
                currentPrefixStr.append(currentCharacterOfAlphabet);
                // 子问题：在（每一个）子树中，收集匹配模式字符串的key
                collectKeysStartWithPrefixThatMatchingPatternInto(currentNode.characterToNodeArr[currentCharacterOfAlphabet], currentPrefixStr, patternStr, validKeysCollection);

                currentPrefixStr.deleteCharAt(currentPrefixStr.length() - 1);
            }
        } else { // 如果不是通配字符的话，说明 我们已经知道 需要在哪一个具体的子树中查找与收集，则：
            // 把 patternStr的当前字符 直接追加到 prefixStr的后面，然后
            currentPrefixStr.append(currentCharacterOfPatternStr);
            // 子问题：在（特定的）子树中，收集匹配模式字符串的key
            collectKeysStartWithPrefixThatMatchingPatternInto(currentNode.characterToNodeArr[currentCharacterOfPatternStr], currentPrefixStr, patternStr, validKeysCollection);

            currentPrefixStr.deleteCharAt(currentPrefixStr.length() - 1);
        }
    }

    /**
     * Returns the string in the symbol table that is the longest prefix of {@code query},
     * or {@code null}, if no such string.
     *
     * @param passedStr the query string
     * @return the string in the symbol table that is the longest prefix of {@code query},
     * or {@code null} if no such string
     * @throws IllegalArgumentException if {@code query} is {@code null}
     */
    public String longestKeyThatPrefixOf(String passedStr) {
        if (passedStr == null) throw new IllegalArgumentException("argument to longestPrefixOf() is null");
        int keyStrLength = longestKeysLengthThatPrefixOf(root, passedStr, 0, -1);

        // 返回-1，表示 不存在 满足条件的键字符串
        if (keyStrLength == -1) return null;
        // 如果存在满足条件的键字符串，则：从传入的字符串中切取出 最长的“键字符串”
        else return passedStr.substring(0, keyStrLength);
    }

    // 返回 以x（x是一个查询字符串的前缀）作为根结点的子树中的 最长字符串键的长度
    // 假设前d个字符匹配，并且我们已经 找到了与给定长度相匹配的前缀（如果没有匹配的前缀，返回-1）
    private int longestKeysLengthThatPrefixOf(Node currentNode, String passedStr, int currentCharacterSpot, int keysLength) {
        // 原理：指定字符在单词查找树中是否存在 <-> 该字符对应的子查找树是否为null
        if (currentNode == null) return keysLength;

        // 当遇到键结点时，初始化/更新 length的值 - 在路径上找到的最后一个key结点 会用来更新length的值
        if (currentNode.value != null) keysLength = currentCharacterSpot;
        if (currentCharacterSpot == passedStr.length()) return keysLength;

        // 获取字符
        char currentCharacterInPassedStr = passedStr.charAt(currentCharacterSpot);
        // 字符对应的子查找树
        Node charactersSubTree = currentNode.characterToNodeArr[currentCharacterInPassedStr];
        return longestKeysLengthThatPrefixOf(charactersSubTree, passedStr, currentCharacterSpot + 1, keysLength);
    }

    /**
     * Removes the key from the set if the key is present.
     *
     * @param key the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void delete(String key) {
        if (key == null) throw new IllegalArgumentException("argument to delete() is null");
        root = delete(root, key, 0);
    }

    private Node delete(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) {
            if (x.value != null) n--;
            x.value = null;
        } else {
            char c = key.charAt(d);
            x.characterToNodeArr[c] = delete(x.characterToNodeArr[c], key, d + 1);
        }

        // remove subtrie rooted at x if it is completely empty
        if (x.value != null) return x;
        for (int c = 0; c < R; c++)
            if (x.characterToNodeArr[c] != null)
                return x;
        return null;
    }

    /**
     * Unit tests the {@code TrieST} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        // build symbol table from standard input
        TrieSTWebsite<Integer> st = new TrieSTWebsite<Integer>();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            st.put(key, i);
        }

        // print results
        if (st.size() < 100) {
            StdOut.println("keys(\"\"):");
            for (String key : st.getIterableKeys()) {
                StdOut.println(key + " " + st.get(key));
            }
            StdOut.println();
        }

        StdOut.println("longestPrefixOf(\"shellsort\"):");
        StdOut.println(st.longestKeyThatPrefixOf("shellsort"));
        StdOut.println();

        StdOut.println("longestPrefixOf(\"quicksort\"):");
        StdOut.println(st.longestKeyThatPrefixOf("quicksort"));
        StdOut.println();

        StdOut.println("keysWithPrefix(\"shor\"):");
        for (String s : st.keysWithPrefixOf("shor"))
            StdOut.println(s);
        StdOut.println();

        StdOut.println("keysThatMatch(\".he.l.\"):");
        for (String s : st.keysThatMatch(".he.l."))
            StdOut.println(s);
    }
}