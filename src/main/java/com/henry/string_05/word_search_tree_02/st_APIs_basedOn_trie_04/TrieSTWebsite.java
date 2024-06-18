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
    private static final int characterOptionsAmount = 256;        // 扩展后的ASCII码表 的可选字符大小


    private Node rootNode;      // trie树的根结点
    private int keysAmount;          // trie树中存储的key的数量

    // R向单词查找树的结点
    private static class Node {
        private Object value; // 结点所绑定的value(optional)
        private Node[] characterToSuccessorNode = new Node[characterOptionsAmount]; // 结点所链接到的后继结点集合
    }

    /**
     * Initializes an empty string symbol table.
     */
    public TrieSTWebsite() {
    }


    /**
     * Returns the value associated with the given key.
     *
     * @param passedKey the key
     * @return the value associated with the given key if the key is in the symbol table
     * and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Value getAssociatedValueOf(String passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to get() is null");

        Node lastNodeInPath = getLastNodeOfPathThatStartFrom(rootNode, passedKey, 0);

        if (lastNodeInPath == null) return null;
        return (Value) lastNodeInPath.value;
    }

    /**
     * Does this symbol table contain the given key?
     *
     * @param passedKey the key
     * @return {@code true} if this symbol table contains {@code key} and
     * {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(String passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to contains() is null");
        return getAssociatedValueOf(passedKey) != null;
    }

    private Node getLastNodeOfPathThatStartFrom(Node currentRootNode, String passedKey, int currentStartCharacterSpot) {
        // 当前字符对应的trie结点为null，说明 trie树中不存在有对应的字符，则：返回null 表示trie中不存在有预期的路径
        if (currentRootNode == null) return null;
        // 如果“对路径的查询”进行到了 键字符串的最后一个字符，说明 trie树中存在有 预期的路径，则：直接返回 当前结点（aka 路径中的最后一个结点）
        if (currentStartCharacterSpot == passedKey.length())
            return currentRootNode;

        /* 如果trie中存在有 当前字符的话，继续在trie子树中查找剩余的路径 👇 */
        // 获取 键字符串”在当前起始位置上的字符“
        char currentCharacter = passedKey.charAt(currentStartCharacterSpot);
        // 获取到 该字符所链接到的”后继结点“
        Node successorNodeForCharacter = currentRootNode.characterToSuccessorNode[currentCharacter];

        // 在子树中，继续查询”新的子字符串“所对应的路径
        return getLastNodeOfPathThatStartFrom(successorNodeForCharacter, passedKey, currentStartCharacterSpot + 1);
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
    public void putInPairOf(String passedKey, Value associatedValue) {
        if (passedKey == null) throw new IllegalArgumentException("first argument to put() is null");

        if (associatedValue == null) deletePairOf(passedKey);
        else rootNode = putInNodesOfPathThatStartFrom(rootNode, passedKey, associatedValue, 0);
    }

    private Node putInNodesOfPathThatStartFrom(Node currentRootNode, String passedKey, Value associatedValue, int currentStartCharacterSpot) {
        // 对路径的查询 终止于null，说明 trie树中不存在有 预期的路径
        if (currentRootNode == null)
            currentRootNode = new Node();

        // 对路径的查询 进行到了 keyStr的最后一个字符，说明 在trie树中找到了 预期的路径，即 符号表中存在有对应的keyStr...
        if (currentStartCharacterSpot == passedKey.length()) {
            // 添加的操作
            if (currentRootNode.value == null)
                keysAmount++; // 把key的计数器+1

            // 添加 OR 更新
            currentRootNode.value = associatedValue;
            // 返回 当前结点（路径中的最后一个结点）
            return currentRootNode;
        }

        /* 如果trie中存在有 当前字符的话，继续在trie子树中查找剩余的路径 👇 */
        char currentCharacter = passedKey.charAt(currentStartCharacterSpot);
        Node successorNodeForCharacter = currentRootNode.characterToSuccessorNode[currentCharacter];

        // 把更新后的trie子树 绑定回到 原始的trie树上
        currentRootNode.characterToSuccessorNode[currentCharacter] = putInNodesOfPathThatStartFrom(successorNodeForCharacter, passedKey, associatedValue, currentStartCharacterSpot + 1);
        // 返回原始trie树的引用
        return currentRootNode;
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     * 符号表中键值对的数量
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return keysAmount;
    }

    /**
     * Is this symbol table empty?
     * 符号表是否为空??
     * @return {@code true} if this symbol table is empty and {@code false} otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns all keys in the symbol table as an {@code Iterable}.
     * To iterate over all of the keys in the symbol table named {@code st},
     * use the foreach notation: {@code for (Key key : st.keys())}.
     * 以 可迭代对象的方式 来 返回 符号表中的所有key
     * 用法：客户端如果想要 遍历 st符号表中所有的key，可以使用foreach语法 Key key : st.keys()
     * @return all keys in the symbol table as an {@code Iterable}
     */
    public Iterable<String> getIterableKeys() {
        // 获取到符号表中存在的 所有 以空字符串作为前缀的 key - 也就是 符号表中存在的所有key
        return keysWithPrefixEqualsTo("");
    }

    /**
     * Returns all of the keys in the set that start with {@code prefix}.
     * 返回 符号表中存在的 所有 以指定字符串作为前缀的key的集合
     * @param passedPrefix the prefix
     * @return all of the keys in the set that start with {@code prefix},
     * as an iterable
     */
    public Iterable<String> keysWithPrefixEqualsTo(String passedPrefix) {
        Queue<String> keysCollection = new Queue<String>();
        // 获取到 在trie树中，“前缀字符串”所对应的路径中的 最后一个结点
        Node lastNodeOfPrefixStr = getLastNodeOfPathThatStartFrom(rootNode, passedPrefix, 0);
        // 在 以前缀字符串的最后一个字符对应的结点为根结点 的trie子树中，查询有效的key，并把它们添加到 keysCollection中去
        collectKeysStartWithPrefixInto(lastNodeOfPrefixStr, new StringBuilder(passedPrefix), keysCollection);
        return keysCollection;
    }

    private void collectKeysStartWithPrefixInto(Node currentRootNode, StringBuilder currentPrefix, Queue<String> keysCollection) {
        // 如果路径上的当前字符不存在其对应的结点 / 对路径的查询结束于null，说明 trie树中不存在有 预期的路径(符号表中有效的key)，则：直接返回 表示此路无果
        if (currentRootNode == null) return;

        // 如果对路径的查询结束于一个 value不为null的结点，说明 trie树中存在有 预期的路径，则：把路径对应的key 添加到 key的集合中
        if (currentRootNode.value != null) {
            String currentKey = currentPrefix.toString();
            keysCollection.enqueue(currentKey);
            /* 🐖 这里不会return，因为后继的路径中仍旧可能存在有 有效的key。只有查找到null时，才会return */
        }

        // 如果“路径上的当前字符” 存在有 其对应的结点，则：在trie树中继续尝试各种可能的R种路径...
        for (char currentAlphabetCharacter = 0; currentAlphabetCharacter < characterOptionsAmount; currentAlphabetCharacter++) {
            // #1 选择(pick)当前字符选项(option) 来 与当前prefix进行拼接，从而进一步构造 potential key/预期路径
            currentPrefix.append(currentAlphabetCharacter);

            // #2 查询 “当前拼接出的potential key” 是不是一个 “valid key”，如果是的话，则：把它添加到keys集合中
            Node successorNodeForCharacter = currentRootNode.characterToSuccessorNode[currentAlphabetCharacter];
            collectKeysStartWithPrefixInto(successorNodeForCharacter, currentPrefix, keysCollection);

            // #3 移除(remove) 路径中的"当前所选择的字符" - 这样才能够在 原始的prefix的基础上，重新选择新的字符 来 构造出新的potential key/预期路径
            // 🐖 这个过程有点像 尝试不同的路径：anchorNode/anchorPrefix + dynamicNode
            currentPrefix.deleteCharAt(currentPrefix.length() - 1);
        }
    }

    /**
     * Returns all of the keys in the symbol table that match {@code pattern},
     * where the character '.' is interpreted as a wildcard character.
     * 返回 符号表中 与模式字符串相匹配的所有的key，模式字符串中的.会被解释成为一个通配符
     *
     * @param patternStr the pattern
     * @return all of the keys in the symbol table that match {@code pattern},
     * as an iterable, where . is treated as a wildcard character.
     */
    public Iterable<String> keysThatMatch(String patternStr) {
        Queue<String> validKeysCollection = new Queue<String>();
        collectKeysStartWithPrefixThatMatchingPatternInto(rootNode, new StringBuilder(), patternStr, validKeysCollection);
        return validKeysCollection;
    }

    // 原始任务：在单词查找树中，收集所有以 “指定的前缀字符串”(生成自“指定的模式字符串”) 作为前缀 而与“模式字符串”长度相等（匹配）的键
    // 匹配“指定模式字符串” 的键 <->
    private void collectKeysStartWithPrefixThatMatchingPatternInto(Node currentRootNode, StringBuilder currentPrefixStr, String patternStr, Queue<String> validKeysCollection) {
        // #1 如果路径上的当前字符不存在其对应的结点 / 对路径的查询结束于null，说明 没有找到匹配条件的key，则：直接返回 表示此路无果
        if (currentRootNode == null) return;

        // #2 如果对路径的查询已经进行到 patternStr的最后一个字符，并且 这个字符对应的结点上有值，说明 找到了满足条件的key，则：把key添加到集合中
        int prefixStrLength = currentPrefixStr.length();
        if (prefixStrLength == patternStr.length() && currentRootNode.value != null)
            validKeysCollection.enqueue(currentPrefixStr.toString());

        // #3 如果对路径的查询已经进行到 patternStr的最后一个字符，但 字符对应的结点上没有值，说明 单词查找树中虽然存在所有字符，但没有满足条件的key，则：直接返回
        if (prefixStrLength == patternStr.length())
            return;

        // 获取到 patternStr的当前字符
        char currentCharacterOfPatternStr = patternStr.charAt(prefixStrLength);

        // 与书上提供的代码不一样 👇
        // 如果当前模式字符是 一个通配字符, 说明 当前字符在单词查找树中能够与任意字符匹配成功，则：
        if (currentCharacterOfPatternStr == '.') {
            for (char currentCharacterOfAlphabet = 0; currentCharacterOfAlphabet < characterOptionsAmount; currentCharacterOfAlphabet++) {
                // 把字母表中的当前字符，追加到 prefixStr上 来 构造potential key
                currentPrefixStr.append(currentCharacterOfAlphabet);
                // 子问题：在（每一个）子树中，收集匹配模式字符串的key
                Node successorNodeForCharacter = currentRootNode.characterToSuccessorNode[currentCharacterOfAlphabet];
                collectKeysStartWithPrefixThatMatchingPatternInto(successorNodeForCharacter, currentPrefixStr, patternStr, validKeysCollection);
                // 从当前前缀字符串中移除”当前选择的字符“ - 这样才能够在 原始的prefix的基础上，使用新的字符 来 构造出新的potential key
                currentPrefixStr.deleteCharAt(currentPrefixStr.length() - 1);
            }
        } else { // 如果不是通配字符的话，说明 我们已经知道 需要在哪一个具体的子树中进行”查找与收集“，则：
            // 把 patternStr的当前字符 直接追加到 prefixStr的后面 来 构造出一个potential key
            currentPrefixStr.append(currentCharacterOfPatternStr);
            // 子问题：在（特定的）子树中，收集匹配模式字符串的key
            Node successorNodeForCharacter = currentRootNode.characterToSuccessorNode[currentCharacterOfPatternStr];
            collectKeysStartWithPrefixThatMatchingPatternInto(successorNodeForCharacter, currentPrefixStr, patternStr, validKeysCollection);

            // 移除”当前选择的字符“ 来 尝试其他的 potential key
            currentPrefixStr.deleteCharAt(currentPrefixStr.length() - 1);
        }
    }

    /**
     * Returns the string in the symbol table that is the longest prefix of {@code query},
     * or {@code null}, if no such string.
     * 返回符号表中存在的、能够作为 查询字符串的前缀的 最长的key
     * 如果不存在这样的key，则 返回null
     *
     * @param passedStr the query string 查询字符串
     * @return the string in the symbol table that is the longest prefix of {@code query},
     * or {@code null} if no such string
     * @throws IllegalArgumentException if {@code query} is {@code null}
     */
    public String longestKeyThatIsPrefixOf(String passedStr) {
        if (passedStr == null) throw new IllegalArgumentException("argument to longestPrefixOf() is null");
        int keyStrLength = longestKeysLengthThatIsPrefixOf(rootNode, passedStr, 0, -1);

        // 返回-1，表示 不存在 满足条件的键字符串
        if (keyStrLength == -1) return null;
            // 如果存在满足条件的键字符串，则：从传入的字符串中切取出 最长的“键字符串”
        else return passedStr.substring(0, keyStrLength);
    }

    // 返回 以x（x是一个查询字符串的前缀）作为根结点的子树中的 最长字符串键的长度
    // 假设前d个字符匹配，并且我们已经 找到了与给定长度相匹配的前缀（如果没有匹配的前缀，返回-1）
    private int longestKeysLengthThatIsPrefixOf(Node currentRootNode, String passedStr, int currentCharacterSpot, int currentLongestKeysLength) {
        // 路径中的当前结点 如果结点为null，说明 trie树中不存在有当前结点/字符，则：对路径的探索结束，直接返回当前的 最长键的长度
        if (currentRootNode == null) return currentLongestKeysLength;

        // 如果 路径中的当前结点 是一个键结点，说明 路径对应于一个key，则：初始化/更新 length的值 - 在路径上找到的最后一个key结点 会用来更新length的值
        if (currentRootNode.value != null) currentLongestKeysLength = currentCharacterSpot;
        // 如果 当前字符在路径中的位置 == 传入字符串的长度，说明 对路径已经探索结束，则：当前的keysLength 就是此路径能够找到的最长前缀key的长度
        if (currentCharacterSpot == passedStr.length()) return currentLongestKeysLength;

        // 获取路径中的下一个结点/字符
        char currentCharacterInPassedStr = passedStr.charAt(currentCharacterSpot);
        // 获取到 字符所对应的trie子树
        Node charactersSubTree = currentRootNode.characterToSuccessorNode[currentCharacterInPassedStr];
        // 在trie子树中，递归地继续 获取最长前缀key的长度
        return longestKeysLengthThatIsPrefixOf(charactersSubTree, passedStr, currentCharacterSpot + 1, currentLongestKeysLength);
    }

    /**
     * Removes the key from the set if the key is present.
     *
     * @param passedKey the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void deletePairOf(String passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to delete() is null");
        rootNode = deleteNodesOfPathThatStartFrom(rootNode, passedKey, 0);
    }

    private Node deleteNodesOfPathThatStartFrom(Node currentRootNode, String passedKey, int currentStartCharacterSpot) {
        if (currentRootNode == null) return null;

        /* #1 把“键字符串的尾字符”所对应的结点的value 设置为null */
        // 如果当前结点 是 键字符串尾字符所对应的结点，则：把结点的value 设置为null
        if (currentStartCharacterSpot == passedKey.length()) {
            if (currentRootNode.value != null)
                keysAmount--;
            currentRootNode.value = null;
        } else { // 如果还不是“尾字符结点”的话，则：递归地在树中查找下一个字符所对应的结点
            char currentCharacter = passedKey.charAt(currentStartCharacterSpot);
            Node successorNodeForCharacter = currentRootNode.characterToSuccessorNode[currentCharacter];

            currentRootNode.characterToSuccessorNode[currentCharacter] = deleteNodesOfPathThatStartFrom(successorNodeForCharacter, passedKey, currentStartCharacterSpot + 1);
        }

        /* #2 如果当前节点 既没有value，又没有子链接，则：物理删除当前结点（返回null） */
        // 如果当前节点 “非空值”，则：保留当前结点
        if (currentRootNode.value != null) return currentRootNode;
        // 如果当前节点 存在“非空链接”，则：保留当前结点
        for (int currentCharacterOfAlphabet = 0; currentCharacterOfAlphabet < characterOptionsAmount; currentCharacterOfAlphabet++)
            if (currentRootNode.characterToSuccessorNode[currentCharacterOfAlphabet] != null)
                return currentRootNode;

        // 对于其他情况，返回null 来 从单词查找树中物理删除当前结点
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
            st.putInPairOf(key, i);
        }

        // print results
        if (st.size() < 100) {
            StdOut.println("keys(\"\"):");
            for (String key : st.getIterableKeys()) {
                StdOut.println(key + " " + st.getAssociatedValueOf(key));
            }
            StdOut.println();
        }

        StdOut.println("longestPrefixOf(\"shellsort\"):");
        StdOut.println(st.longestKeyThatIsPrefixOf("shellsort"));
        StdOut.println();

        StdOut.println("longestPrefixOf(\"quicksort\"):");
        StdOut.println(st.longestKeyThatIsPrefixOf("quicksort"));
        StdOut.println();

        StdOut.println("keysWithPrefix(\"shor\"):");
        for (String s : st.keysWithPrefixEqualsTo("shor"))
            StdOut.println(s);
        StdOut.println();

        StdOut.println("keysThatMatch(\".he.l.\"):");
        for (String s : st.keysThatMatch(".he.l."))
            StdOut.println(s);
    }
}