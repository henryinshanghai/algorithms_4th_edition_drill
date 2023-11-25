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

    /**
     * Returns the value associated with the given key.
     *
     * @param passedKey the key
     * @return the value associated with the given key if the key is in the symbol table
     * and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Value get(String passedKey) {
        if (passedKey == null) {
            throw new IllegalArgumentException("calls get() with null argument");
        }
        if (passedKey.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        Node<Value> x = getNodeFrom(root, passedKey, 0);
        if (x == null) return null;
        return x.value;
    }

    // return subtrie corresponding to given key
    private Node<Value> getNodeFrom(Node<Value> currentNode, String passedKey, int currentCharacterSpot) {
        if (currentNode == null) return null;
        if (passedKey.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        char currentCharacterOfPassedKey = passedKey.charAt(currentCharacterSpot);
        if (currentCharacterOfPassedKey < currentNode.character)
            return getNodeFrom(currentNode.leftSubtree, passedKey, currentCharacterSpot);
        else if (currentCharacterOfPassedKey > currentNode.character)
            return getNodeFrom(currentNode.rightSubtree, passedKey, currentCharacterSpot);
        else if (currentCharacterSpot < passedKey.length() - 1)
            return getNodeFrom(currentNode.midSubtree, passedKey, currentCharacterSpot + 1);
        else return currentNode;
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is {@code null}, this effectively deletes the key from the symbol table.
     *
     * @param passedKey       the key
     * @param associatedValue the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(String passedKey, Value associatedValue) {
        if (passedKey == null) {
            throw new IllegalArgumentException("calls put() with null key");
        }
        if (!contains(passedKey)) keysAmount++;
        else if (associatedValue == null) keysAmount--;       // delete existing key
        root = putNodeInto(root, passedKey, associatedValue, 0);
    }

    private Node<Value> putNodeInto(Node<Value> currentNode, String passedKey, Value associatedValue, int currentCharacterSpot) {
        char currentCharacterOfPassedKey = passedKey.charAt(currentCharacterSpot);
        if (currentNode == null) {
            currentNode = new Node<Value>();
            currentNode.character = currentCharacterOfPassedKey;
        }
        if (currentCharacterOfPassedKey < currentNode.character)
            currentNode.leftSubtree = putNodeInto(currentNode.leftSubtree, passedKey, associatedValue, currentCharacterSpot);
        else if (currentCharacterOfPassedKey > currentNode.character)
            currentNode.rightSubtree = putNodeInto(currentNode.rightSubtree, passedKey, associatedValue, currentCharacterSpot);
        else if (currentCharacterSpot < passedKey.length() - 1)
            currentNode.midSubtree = putNodeInto(currentNode.midSubtree, passedKey, associatedValue, currentCharacterSpot + 1);
        else currentNode.value = associatedValue;
        return currentNode;
    }

    /**
     * Returns the string in the symbol table that is the longest prefix of {@code query},
     * or {@code null}, if no such string.
     *
     * @param query the query string
     * @return the string in the symbol table that is the longest prefix of {@code query},
     * or {@code null} if no such string
     * @throws IllegalArgumentException if {@code query} is {@code null}
     */
    public String longestPrefixOf(String query) {
        if (query == null) {
            throw new IllegalArgumentException("calls longestPrefixOf() with null argument");
        }
        if (query.length() == 0) return null;
        int length = 0;
        Node<Value> x = root;
        int i = 0;
        while (x != null && i < query.length()) {
            char c = query.charAt(i);
            if (c < x.character) x = x.leftSubtree;
            else if (c > x.character) x = x.rightSubtree;
            else {
                i++;
                if (x.value != null) length = i;
                x = x.midSubtree;
            }
        }
        return query.substring(0, length);
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
        Node<Value> x = getNodeFrom(root, prefix, 0);
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
        StdOut.println(st.longestPrefixOf("shellsort"));
        StdOut.println();

        StdOut.println("longestPrefixOf(\"shell\"):");
        StdOut.println(st.longestPrefixOf("shell"));
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