package com.henry.string_05.word_search_tree_02.threeWay_trie_06.execution; /******************************************************************************
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
                if (currentNode.value != null) {
                    longestPrefixLength = currentCharacterSpot;
                }
                // 找到有效的key之后，更新当前结点 以便 继续在trie树中找到 更长的前缀key
                currentNode = currentNode.midSubtree;
            }
        }

        // 使用得到的“最长前缀长度” 来 从字符串中截取得到 最长前缀键
        return passedStr.substring(0, longestPrefixLength);
    }

    // 以一个可迭代的对象 来 返回符号表中所有的keys
    // 迭代符号表st中所有key的方式 - 使用foreach语法： for (Key key : st.keys())
    public Iterable<String> keys() {
        Queue<String> keysQueue = new Queue<String>();
        collectKeysInto(root, new StringBuilder(), keysQueue);
        return keysQueue;
    }

    /**
     * Returns all of the keys in the set that start with {@code prefix}.
     *
     * @param passedStr the prefix
     * @return all of the keys in the set that start with {@code prefix},
     * as an iterable
     * @throws IllegalArgumentException if {@code prefix} is {@code null}
     */
    public Iterable<String> keysWithPrefix(String passedStr) {
        if (passedStr == null) {
            throw new IllegalArgumentException("calls keysWithPrefix() with null argument");
        }
        Queue<String> keysQueue = new Queue<String>();
        // 获取到 传入的字符串的最后一个字符 在3-way trie树中所对应的结点
        Node<Value> nodeForLastCharacter = getNodeForLastCharacterOf(root, passedStr, 0);

        // 如果 传入的字符串的最后一个字符 在3-way trie中所对应的结点是null，说明 前缀字符串在3-way trie中不存在，则：
        if (nodeForLastCharacter == null) {
            // 直接返回空的queue
            return keysQueue;
        }

        // 如果 传入的字符串的最后一个字符 在3-way trie树中所对应的结点 是一个key结点，说明 传入的字符串本身就是一个key，则：
        if (nodeForLastCharacter.value != null) {
            // 把前缀字符串添加到 keysQueue中...
            keysQueue.enqueue(passedStr);
        }

        // 获取到 前缀字符串的最后一个字符 在trie中对应的结点 的中子树 - 作为前缀，字符串必然是匹配的。所以key中剩下的字符 需要在中子树中继续匹配
        Node<Value> subTreeToKeepMatching = nodeForLastCharacter.midSubtree;
        StringBuilder givenPrefixSB = new StringBuilder(passedStr);

        // 在subTrie中，查找以指定字符串作为前缀的key，并收集到keysQueue集合中
        collectKeysInto(subTreeToKeepMatching, givenPrefixSB, keysQueue);

        // 最终返回收集到的所有key
        return keysQueue;
    }

    // all keys in subtrie rooted at x with given prefix
    // 以x作为根结点的子树中存在的、以指定字符串作为前缀的所有key
    private void collectKeysInto(Node<Value> currentRootNode, StringBuilder currentAttemptStr, Queue<String> keysQueue) {
        // 递归遍历3-way trie结点的过程中，如果结点为null，说明此分支已经探索完毕，则：直接返回 以继续探索其他分支
        if (currentRootNode == null) return;

        // #1 遍历左子树中的结点，来收集key
        // 说明：如果使用左子树，说明 没有选用“当前结点的字符”来组成 attemptStr，因此：前缀字符串保持不变
        collectKeysInto(currentRootNode.leftSubtree, currentAttemptStr, keysQueue);

        // 递归遍历3-way trie结点的过程中，如果结点的value不为null，说明找到了keyStr的尾字符对应的结点，则：
        if (currentRootNode.value != null) {
            // ① 组装出 key字符串
            String keyStr = currentAttemptStr.toString() + currentRootNode.character;
            // ② 把组装出的key字符串 添加到keysQueue中
            keysQueue.enqueue(keyStr);
            /* 🐖 这里不会return，因为后继的路径中仍旧可能存在有 有效的key。只有查找到null时，才会return */
        }

        // #2 遍历中子树中的结点，来收集key
        // 说明：如果使用中子树，说明 选用了“当前结点的字符”来组成 attemptStr，因此：把当前结点中的字符 追加到 attemptStr中 - 用于最终拼凑出key字符串
        collectKeysInto(currentRootNode.midSubtree, currentAttemptStr.append(currentRootNode.character), keysQueue);

        // 在继续尝试在right sub-trie中查找之前，移除上一步中添加的字符
        currentAttemptStr.deleteCharAt(currentAttemptStr.length() - 1);

        // #3 遍历右子树中的结点，来收集key
        // 说明：如果使用右子树，说明 没有选用“当前结点的字符”来组成 attemptStr，因此：前缀字符串保持不变
        collectKeysInto(currentRootNode.rightSubtree, currentAttemptStr, keysQueue);
    }


    // 返回符号表中 匹配模式字符串的所有key，其中字符.会被解释成为一个 通配字符
    public Iterable<String> keysThatMatch(String patternStr) {
        Queue<String> keysQueue = new Queue<String>();
        collectKeysStartWithPrefixInto(root, new StringBuilder(), patternStr, 0, keysQueue);
        return keysQueue;
    }

    // TBD
    private void collectKeysStartWithPrefixInto(Node<Value> currentRootNode, StringBuilder currentPrefix, String patternStr, int currentCharacterSpot, Queue<String> keysQueue) {
        if (currentRootNode == null) return;
        char currentPatternCharacter = patternStr.charAt(currentCharacterSpot);

        if (currentPatternCharacter == '.' || currentPatternCharacter < currentRootNode.character)
            collectKeysStartWithPrefixInto(currentRootNode.leftSubtree, currentPrefix, patternStr, currentCharacterSpot, keysQueue);

        if (currentPatternCharacter == '.' || currentPatternCharacter == currentRootNode.character) {
            // 找到了key结点
            if (currentCharacterSpot == patternStr.length() - 1 && currentRootNode.value != null)
                keysQueue.enqueue(currentPrefix.toString() + currentRootNode.character);

            // 当前结点 匹配到了 当前模式字符
            if (currentCharacterSpot < patternStr.length() - 1) {
                // 在中子树中继续进行匹配与收集
                collectKeysStartWithPrefixInto(currentRootNode.midSubtree, currentPrefix.append(currentRootNode.character), patternStr, currentCharacterSpot + 1, keysQueue);
                currentPrefix.deleteCharAt(currentPrefix.length() - 1);
            }
        }

        if (currentPatternCharacter == '.' || currentPatternCharacter > currentRootNode.character)
            collectKeysStartWithPrefixInto(currentRootNode.rightSubtree, currentPrefix, patternStr, currentCharacterSpot, keysQueue);
    }


    /**
     * Unit tests the {@code TST} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        // build symbol table from standard input
        TSTWebsite<Integer> symbolTable = new TSTWebsite<Integer>();

        for (int currentSpot = 0; !StdIn.isEmpty(); currentSpot++) {
            String currentKey = StdIn.readString();
            symbolTable.put(currentKey, currentSpot);
        }

        // print results
        if (symbolTable.size() < 100) {
            StdOut.println("keys(\"\"):");
            for (String key : symbolTable.keys()) {
                StdOut.println(key + " " + symbolTable.get(key));
            }
            StdOut.println();
        }

        StdOut.println("longestPrefixOf(\"shellsort\"):");
        StdOut.println(symbolTable.keyThatIsLongestPrefixOf("shellsort"));
        StdOut.println();

        StdOut.println("longestPrefixOf(\"shell\"):");
        StdOut.println(symbolTable.keyThatIsLongestPrefixOf("shell"));
        StdOut.println();

        StdOut.println("keysWithPrefix(\"shor\"):");
        for (String s : symbolTable.keysWithPrefix("shor"))
            StdOut.println(s);
        StdOut.println();

        StdOut.println("keysThatMatch(\".he.l.\"):");
        for (String s : symbolTable.keysThatMatch(".he.l."))
            StdOut.println(s);
    }
}