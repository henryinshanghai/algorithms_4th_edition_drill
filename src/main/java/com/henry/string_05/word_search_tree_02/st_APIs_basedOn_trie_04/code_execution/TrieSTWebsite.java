package com.henry.string_05.word_search_tree_02.st_APIs_basedOn_trie_04.code_execution;

/******************************************************************************
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
    private static final int characterOptionsAmount = 256;        // 扩展后的ASCII码表的 可选字符的数量

    private Node rootNode;      // trie树的根结点
    private int keysAmount;          // trie树中存储的key的数量

    // R向单词查找树的结点
    private static class Node {
        private Object value; // 结点所绑定的value(optional)
        private Node[] characterToItsSubNode = new Node[characterOptionsAmount]; // 结点 所链接到的 所有后继结点的集合
    }

    /**
     * 初始化一个空的 字符串符号表
     */
    public TrieSTWebsite() {
    }


    /**
     * 返回 指定的键 所关联的value
     *
     * @param passedKey 指定的键
     *                  如果 指定的键 在符号表中存在，则 返回其所关联的value；如果不存在，则 返回null
     *                  如果 传入的key是null的话，则 抛出异常
     */
    public Value getAssociatedValueOf(String passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to get() is null");

        Node lastNodeInPath = getLastNodeOfPathThatStartFrom(rootNode, passedKey, 0);

        if (lastNodeInPath == null) return null;
        return (Value) lastNodeInPath.value;
    }

    /**
     * 符号表中 包含有 指定的键 吗？
     *
     * @param passedKey 指定的键
     * @return 如果 包含有 指定的键的话，则 返回true； 如果不包含，则 返回false；
     * 如果传入的key是null，则 抛出 非法参数异常
     */
    public boolean contains(String passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to contains() is null");
        return getAssociatedValueOf(passedKey) != null;
    }

    /**
     * 获取到 指定key字符串 从指定位置开始 的子字符串 在 当前trie树中的路径 的最后一个节点
     *
     * @param currentRootNode           当前的trie树
     * @param passedKey                 指定的key字符串
     * @param currentStartCharacterSpot 当前子字符串的首字符 在原始字符串中 的位置
     * @return 返回 路径中的最后一个节点
     */
    private Node getLastNodeOfPathThatStartFrom(Node currentRootNode,
                                                String passedKey,
                                                int currentStartCharacterSpot) {
        /* 〇 递归终结条件 */
        // 如果 当前字符 所对应的trie结点 为null，说明 trie树的路径中 不存在有 对应的字符，则：
        if (currentRootNode == null) {
            // 返回null 表示 trie中 不存在有 预期的路径
            return null;
        }
        // 如果 “对路径的查询” 进行到了 键字符串的最后一个字符，说明 trie树中 存在有 预期的路径，则：
        if (currentStartCharacterSpot == passedKey.length()) {
            // 直接返回 当前结点（aka 路径中的最后一个结点）
            return currentRootNode;
        }

        /* Ⅰ 如果 trie树 中 存在有 当前字符的话，继续 在trie子树中 查找 剩余的子路径 👇 */
        // 获取 键字符串 ”在 当前起始位置 上的字符“
        char currentCharacter = passedKey.charAt(currentStartCharacterSpot);
        // 获取到 该字符所链接到 的”后继结点“
        Node correspondingSubNode = currentRootNode.characterToItsSubNode[currentCharacter];

        // 在 子树 中，继续查询 ”新的子字符串“ 所对应的路径的 最后一个节点
        return getLastNodeOfPathThatStartFrom(correspondingSubNode, passedKey, currentStartCharacterSpot + 1);
    }

    /**
     * 向 符号表 中 插入 指定的键值对
     * 如果 传入的键 在符号表中已经存在，则 覆盖旧的值；
     * 如果 传入的value 是null的话，则 从符号表中 删除 该传入的key
     *
     * @param passedKey       键值对中的 键
     * @param associatedValue 键值对中 值
     * @throws IllegalArgumentException 如果传入的key 是 null，则 抛出异常
     */
    public void putInPairOf(String passedKey, Value associatedValue) {
        if (passedKey == null) throw new IllegalArgumentException("first argument to put() is null");

        if (associatedValue == null) {
            deletePairOf(passedKey); // 执行 删除操作
        } else { // 执行 插入操作
            rootNode = putInNodesOfPathThatStartFrom(rootNode, passedKey, associatedValue, 0);
        }
    }

    /**
     * 把 指定的key字符串 从指定位置开始的子字符串，添加到 当前的trie树中。并 在 路径的尾节点 上 绑定value
     *
     * @param currentRootNode           当前的trie树 （参数命名为current 暗示着这会是一个递归方法）
     * @param passedKey                 传入的key
     * @param associatedValue           key所绑定的value
     * @param currentStartCharacterSpot 当前子字符串的首字符 在原始字符串中 的位置
     * @return 插入了 键所对应的路径 后的 trie树
     */
    private Node putInNodesOfPathThatStartFrom(Node currentRootNode,
                                               String passedKey,
                                               Value associatedValue,
                                               int currentStartCharacterSpot) {
        // 如果 对路径的查询 终止于null，说明 trie树中 不存在有 预期的路径，则：
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

        /* 如果 trie中 存在有 当前字符的话，继续 在trie子树中 查找 剩余的路径 👇 */
        char currentCharacter = passedKey.charAt(currentStartCharacterSpot);
        Node successorNodeForCharacter = currentRootNode.characterToItsSubNode[currentCharacter];

        // 把更新后的trie子树 绑定回到 原始的trie树上
        currentRootNode.characterToItsSubNode[currentCharacter] = putInNodesOfPathThatStartFrom(successorNodeForCharacter, passedKey, associatedValue, currentStartCharacterSpot + 1);
        // 返回原始trie树的引用
        return currentRootNode;
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     * 符号表中键值对的数量
     *
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return keysAmount;
    }

    /**
     * Is this symbol table empty?
     * 符号表是否为空??
     *
     * @return {@code true} if this symbol table is empty and {@code false} otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * 以 可迭代对象的方式 来 返回 符号表中的所有key
     * 用法：客户端如果想要 遍历 st符号表中所有的key，可以使用foreach语法 Key key : st.keys()
     * 以可迭代集合的方式 返回 符号表中的所有key
     */
    public Iterable<String> getAllKeysInIterable() {
        // 获取到 符号表中存在的 所有 以空字符串作为前缀的 key - 也就是 符号表中 存在的 所有key
        return keysWhosePrefixIs("");
    }

    /**
     * 返回 符号表中所存在的 所有 以 指定字符串 作为前缀的 key的集合
     *
     * @param passedStr 前缀字符串
     * @return 以 可迭代集合的方式 来 返回 所有 以指定前缀开头的 所有key
     */
    public Iterable<String> keysWhosePrefixIs(String passedStr) {
        Queue<String> keysCollection = new Queue<String>();
        // 获取到 在 trie树 中，“前缀字符串” 所对应的路径 中的 最后一个结点
        Node lastNodeOfPrefixStr = getLastNodeOfPathThatStartFrom(rootNode, passedStr, 0);
        // 在 以 “前缀字符串的最后一个字符 所对应的 结点” 为 根结点 的 trie子树 中，查询 有效的key，并 把 它们 添加到 keysCollection中去
        collectKeysWhosePrefixIs(lastNodeOfPrefixStr, new StringBuilder(passedStr), keysCollection);

        return keysCollection;
    }

    /**
     * 找到 当前trie树中 所有 拥有 指定前缀 的key，并 把所有满足条件的key 添加到一个集合中
     * ① 怎么找到 trie树中的key?   因为 key字符串 在trie树中的路径的 最后一个节点 会绑定value，可以 以此来找到key
     * ② 怎么 把路径上各个节点中的字符 拼接得到 字符串？  使用 当前前缀字符串 不断地 拼接上 路径上当前节点中的字符
     * ③ 怎么 把拼接得到的字符串key 给收集起来？     一个容器类型的变量   这里为什么作为方法参数呢？为啥不是成员变量呢?
     *
     * @param currentRootNode 当前的trie树
     * @param currentPrefix   指定的前缀字符串
     * @param keysCollection  满足条件的 所有key 所组成的集合
     */
    private void collectKeysWhosePrefixIs(Node currentRootNode,
                                          StringBuilder currentPrefix,
                                          Queue<String> keysCollection) {
        /* 〇 递归终结条件：查询 执行到了 trie树的叶子节点 or 字符所对应的节点 在trie树中 不存在 */
        // 如果 路径上的当前字符 不存在 其对应的结点(aka 对 路径的查询 结束于null)，说明 trie树中 不存在有 预期的路径(符号表中有效的key)，则：
        if (currentRootNode == null) {
            // 直接返回 表示 此路 查询未命中
            return;
        }

        /* Ⅰ本级调用的任务：在 当前trie树 中 尝试找到 满足前缀条件的key 具体手段：① + ② */
        // ① 判断 当前路径 是不是 一个有效的key
        // 如果 路径上的当前节点 是一个 “value不为null的结点”，说明 该路径代表的key 满足前缀条件，找到了 有效的key，则：
        if (currentRootNode.value != null) {
            // 获取到 该路径 所对应的key
            String currentKey = currentPrefix.toString();
            // 并 把 该key 添加到 key的集合中
            keysCollection.enqueue(currentKey);
            /* 🐖 这里 不能够 直接return，因为 后面的路径中 仍旧可能存在有 有效的key。只有 查找到null 时，才能够 return */
        }

        /* ② 扩展路径（尝试所有路径的可能性）以 找到 满足前缀条件的key */
        // 手段：在 当前prefix字符串的路径 下，通过 添加新的可能节点(字符选项) 来 扩展路径
        // 对于 字母表中 所有可能的 字符选项...
        for (char currentCharacterOption = 0; currentCharacterOption < characterOptionsAmount; currentCharacterOption++) {
            // #1 尝试 使用 当前字符选项(option) 与 当前prefix 进行拼接 来 扩展路径（得到potential key）
            currentPrefix.append(currentCharacterOption);

            // #2 在 扩展了路径 之后，继续 递归地 ① 判断路径 + ② 扩展路径；直到 遇到null 为止（也就是 到了 trie树的叶子节点）
            // 获取到 当前字符选项 在完整trie树中 所对应的 子节点/子trie树
            Node subNodeForCurrentCharacterOption = currentRootNode.characterToItsSubNode[currentCharacterOption];
            // 在 子trie树 中，继续递归地 进行①+②
            collectKeysWhosePrefixIs(subNodeForCurrentCharacterOption, currentPrefix, keysCollection);

            // #3 移除(remove) 尝试路径中 "当前所选择的字符" - 这样才能够 在 原始的prefix 的基础上，重新选择 新的字符 来 构造出新的扩展路径/potential key
            // 🐖 这个过程 有点像 尝试不同的路径：anchorNode/anchorPrefix + dynamicNode
            currentPrefix.deleteCharAt(currentPrefix.length() - 1);
        }
    }

    /**
     * 返回 符号表中 与模式字符串相匹配的 所有的key，其中 模式字符串中的. 会被解释成为 一个通配符
     *
     * @param patternStr 模式字符串
     *                   以 可迭代集合的形式 返回 符号表中 所有匹配 指定的模式字符串的 键的集合，其中 .符号 被视为一个 通配符号
     */
    public Iterable<String> keysThatMatch(String patternStr) {
        Queue<String> matchedKeys = new Queue<String>();
        findAndCollectMatchedKeys(rootNode,
                new StringBuilder(),
                patternStr,
                matchedKeys);
        return matchedKeys;
    }

    /**
     * 在 R向trie树 中，收集所有 与指定的模式字符串 相匹配(字符匹配&长度匹配) 的所有键
     *
     * @param currentRootNode     当前的trie树（的根节点） 用于???
     * @param currentPrefixStr    当前的前缀字符串     用于???
     * @param patternStr          模式字符串   用于 说明 有效key 所需要遵守的规则
     * @param validKeysCollection 匹配 模式字符串 的所有key 所组成的集合
     */
    private void findAndCollectMatchedKeys(Node currentRootNode,
                                           StringBuilder currentPrefixStr,
                                           String patternStr,
                                           Queue<String> validKeysCollection) {
        /* 〇 递归终结条件：查询 执行到了 trie树的叶子节点 or 字符所对应的节点 在trie树中 不存在 */
        // 如果 路径上的当前字符 不存在 其对应的结点(aka 对路径的查询 结束于 null)，说明 没有找到 匹配条件的key，则：
        if (currentRootNode == null) {
            // 直接返回 表示 此路 查询未命中
            return;
        }

        /* Ⅰ本级调用的任务：在 当前trie树 中 尝试找到 满足“匹配条件”的key 具体手段：① + ② */
        // ① 判断 当前路径 是不是 一个有效的key
        int prefixStrLength = currentPrefixStr.length();
        if (lengthMatched(patternStr, prefixStrLength) && isAKeysEndNode(currentRootNode)) { // 如果 当前路径 是一个key，则：
            // 把 满足条件的key 添加到 集合中
            validKeysCollection.enqueue(currentPrefixStr.toString());
        }

        if (prefixStrLength == patternStr.length()) { // 尾节点没有value，说明 当前路径 不是一个key，则：
            // 直接返回，不再 继续查询 剩下的路径
            return;
        }

        /* ② 根据 当前的模式字符 来 扩展路径 以 找到 满足匹配条件的key */
        // 获取到 当前模式字符
        char currentCharacterOfPatternStr = patternStr.charAt(prefixStrLength);

        // 如果 当前模式字符 是 一个通配字符, 说明 其 在 单词查找树 中 能够 与 任意字符选项 匹配成功，则：
        if (currentCharacterOfPatternStr == '.') {
            // 对于 字母表中 所有可能的 字符选项...
            for (char currentCharacterOption = 0; currentCharacterOption < characterOptionsAmount; currentCharacterOption++) {
                // #1 尝试 使用 当前字符选项 与 当前prefixStr 进行拼接 来 扩展路径（构造potential key）
                currentPrefixStr.append(currentCharacterOption);

                // #2 在 扩展了路径 之后，继续 递归地 ① 判断路径 + ② 扩展路径；直到 遇到null为止（也就是 到了 trie树的叶子节点）
                // 获取到 当前字符选项 在完整trie树中 所对应的 子trie树/子节点
                Node subNodeForCurrentCharacterOption = currentRootNode.characterToItsSubNode[currentCharacterOption];
                // 在 子trie树 中，继续递归地 进行①+②
                findAndCollectMatchedKeys(subNodeForCurrentCharacterOption, currentPrefixStr, patternStr, validKeysCollection);

                // #3 移除(remove) 尝试路径中 "当前所选择的字符" - 这样才能够 在 原始的prefix 的基础上，重新选择 新的字符 来 构造出新的扩展路径/potential key
                currentPrefixStr.deleteCharAt(currentPrefixStr.length() - 1);
            }
        } else { // 如果 当前匹配字符 不是 通配字符的话，说明 我们已经知道 需要 在哪一个具体的子树中 进行”查找与收集“，则：
            // #1 尝试使用 当前匹配字符 与 prefixStr拼接 来 扩展路径（构造出一个potential key）
            currentPrefixStr.append(currentCharacterOfPatternStr);
            // #2 在 扩展了路径 之后，继续 递归地 ① 判断路径 + ② 扩展路径；直到 遇到null为止（也就是 到了 trie树的叶子节点）
            Node subNodeForCurrentCharacterOption = currentRootNode.characterToItsSubNode[currentCharacterOfPatternStr];
            findAndCollectMatchedKeys(subNodeForCurrentCharacterOption, currentPrefixStr, patternStr, validKeysCollection);

            // #3 移除 ”当前选择的字符“ 来 尝试其他可能的 potential key
            currentPrefixStr.deleteCharAt(currentPrefixStr.length() - 1);
        }
    }

    // trie树中的指定节点 是不是一个 key的尾节点
    private boolean isAKeysEndNode(Node currentRootNode) {
        // 手段：查看 该节点的value属性 是否为null
        return currentRootNode.value != null;
    }

    // 判断 得到的key字符串 与 模式字符串 在长度上是不是匹配的
    private boolean lengthMatched(String patternStr, int prefixStrLength) {
        return prefixStrLength == patternStr.length();
    }

    /**
     * 返回 符号表中所存在的、能够作为 指定的查询字符串 的前缀的 最长的key
     * 如果 不存在 这样的key，则 返回null
     *
     * @param passedStr 指定的查询字符串
     *                  如果 传入的查询字符串为null，则 抛出 非法参数异常
     */
    public String longestKeyThatIsPrefixOf(String passedStr) {
        if (passedStr == null) throw new IllegalArgumentException("argument to longestPrefixOf() is null");
        int keyStrLength =
                longestKeysLengthThatIsPrefixOf(rootNode,
                        passedStr,
                        0,
                        -1);

        // 如果 返回值 为 -1，说明 不存在 满足条件的key字符串，则：
        if (keyStrLength == -1) {
            // 返回 null，表示 不存在 满足条件的key
            return null;
        } else { // 如果 存在 满足条件的key字符串，则：
            // 从 传入的字符串 中 截取出 最长的“键字符串”
            return passedStr.substring(0, keyStrLength);
        }
    }

    /**
     * 获取到 符号表中所存在的、可以作为 指定的查询字符串 的前缀的 最长key的长度
     * 怎么得到 key？      如果 路径中的节点 不为null，则：出现了 一个key
     * 怎么保证 最长？     在路径中 持续地 获取key，并 使用 后来的key的长度 不断维护 longestKeysLength的值
     * 怎么保证 key 是 查询字符串的前缀?     在trie树中，沿着 查询字符串所指定的路径 来 查找key
     *
     * @param currentRootNode          当前trie树
     * @param passedStr                传入的查询字符串
     * @param currentCharacterSpot     查询字符串的 当前字符 指针
     * @param currentLongestKeysLength 当前最长key的长度
     * @return 满足条件的最长key的长度
     */
    private int longestKeysLengthThatIsPrefixOf(Node currentRootNode,
                                                String passedStr,
                                                int currentCharacterSpot,
                                                int currentLongestKeysLength) {
        /* 〇 递归终结条件：查询 执行到了 trie树的叶子节点 or 字符所对应的节点 在trie树中 不存在 */
        // 如果 路径中的当前字符 不存在 其对应的节点(aka 对路径的查询 结束于 null)，说明 trie树中 不存在有 当前结点/字符，则：
        if (currentRootNode == null) {
            // 对路径的探索 结束，直接返回 当前最长键的长度
            return currentLongestKeysLength;
        }

        /* Ⅰ本级调用的任务：在 当前trie树 中 尝试找到 满足“最长前缀条件”的key 具体手段：① + ② */
        // ① 判断 路径扩展到 当前位置时，有没有 得到一个key
        if (isAKeysEndNode(currentRootNode)) { // 如果 路径扩展到当前位置 是一个key，则：
            // 维护（初始化/更新） 表示 最长key的length 的变量
            currentLongestKeysLength = currentCharacterSpot;
        }
        // 如果 当前字符在路径中的位置 == 传入字符串的长度，说明 对路径的探索 已经结束，则：
        if (reachTheEndNode(passedStr, currentCharacterSpot)) { // 如果 路径扩展到末尾位置，说明 变量longestKeysLength的当前值 就是 最长键的长度，则：
            // 直接返回 该变量
            return currentLongestKeysLength;
        }

        /* ② 沿着 既定路径 继续扩展 来 接着查找 路径中的key，进而 得到 最长的key */
        // #1 获取到 当前查询字符
        char currentCharacterInPassedStr = passedStr.charAt(currentCharacterSpot);
        // #2 获取到 该字符 在完整trie树中 所对应的 trie子树
        Node charactersSubTree = currentRootNode.characterToItsSubNode[currentCharacterInPassedStr];
        // #3 在 子trie树 中，继续递归地 进行①+②
        return longestKeysLengthThatIsPrefixOf(charactersSubTree,
                passedStr,
                currentCharacterSpot + 1, // 指针 向后移动 一个位置
                currentLongestKeysLength);
    }

    // 判断 字符指针的位置 是否 到达字符串的末尾
    private boolean reachTheEndNode(String passedStr, int currentCharacterSpot) {
        // 这里使用length() 是因为 spot在调用时 已经有了 +1的操作
        return currentCharacterSpot == passedStr.length();
    }

    /**
     * 如果 指定的key 在符号表中存在的话，移除这个key
     *
     * @param passedKey 指定的key
     * @throws IllegalArgumentException 如果 传入的key 为null，则 抛出异常
     */
    public void deletePairOf(String passedKey) {
        if (passedKey == null) {
            throw new IllegalArgumentException("argument to delete() is null");
        }
        // 一个参数 变成了 三个参数，为啥？
        rootNode = deleteNodesOfPathThatStartFrom(rootNode, passedKey, 0);
    }

    /**
     * 从 当前trie树 中，删除 指定的key 所对应的节点 及 其所关联的value
     * 怎么 删除 节点所关联的值？   把value设置为null
     * 怎么 找到 key的尾节点？   把key字符串 当作trie树中的path，逐个节点(字符)地递归查找
     * 怎么 在trie树中 删除节点？
     * 如果 节点 存在有 一个或多个指向其他节点的链接，说明 它还 被用于构造 其他的key，则：只删除value即可，不能删除节点
     * 如果 节点 不存在有 任何指向其他节点的链接，说明 它 没有被用于构造 其他的key，则：不仅要删除value，还要 对节点 进行递归地删除
     *
     * @param currentRootNode           当前trie树的根节点
     * @param passedKey                 传入的key字符串
     * @param currentStartCharacterSpot ???
     * @return
     */
    private Node deleteNodesOfPathThatStartFrom(Node currentRootNode,
                                                String passedKey,
                                                int currentStartCharacterSpot) {
        /* 〇 递归终结条件：查询 执行到了 trie树的叶子节点 or 字符所对应的节点 在trie树中 不存在 */
        // 如果 路径中的当前结点 为 null,说明 字符所对应的节点 在trie树中 不存在，则：
        if (currentRootNode == null) {
            // 返回 null 表示 删除失败，因为 传入的key 不存在
            return null;
        }

        /* Ⅰ本级调用的任务：在 当前trie树 中 尝试删除 指定key的尾节点 具体手段：① + ② */
        // ① 删除 指定key的 尾结点的value值
        if (currentStartCharacterSpot == passedKey.length()) { // 如果 当前结点 是 路径中的最后一个结点，说明 在tire树中 找到了 待删除的节点，则：
            // （特殊情况）如果 key 在符号表中 存在，说明 执行删除后 符号表中键的数量 会-1，则：
            if (isAKeysEndNode(currentRootNode)) {
                // 把 符号表中的 key的数量（成员变量） - 1
                keysAmount--;
            }
            // （一般情况）无论key 在符号表中 是否存在，都需要 对节点上的value 进行 物理删除
            currentRootNode.value = null;
        } else { // 如果 当前结点 不是 路径中的最后一个结点，说明 查询 尚未执行到 最后一个节点，则：
            // 在 子trie树 中 继续 递归地删除key
            char currentCharacter = passedKey.charAt(currentStartCharacterSpot);
            Node successorNodeForCharacter = currentRootNode.characterToItsSubNode[currentCharacter];

            // 把 “删除了指定key之后”的 trie子树，绑定回去 原始子树的引用上
            currentRootNode.characterToItsSubNode[currentCharacter] =
                    deleteNodesOfPathThatStartFrom(successorNodeForCharacter,
                            passedKey,
                            currentStartCharacterSpot + 1);
        }

        /* ② 在 回溯的过程(会持续进行 直到根节点) 中，按需删除 当前节点 👇 */
        // 如果 当前节点 是某个key的尾节点，说明 它在trie树中 有实际作用 不能删除它，则：
        if (isAKeysEndNode(currentRootNode)) {
            // 返回 该节点 给上一级调用，表示 保留该节点，继续回溯过程
            return currentRootNode;
        }

        // 如果 当前节点 存在有 某个“非空链接”，说明 当前节点 会在某个key中发挥作用，则：
        for (int currentCharacterOfAlphabet = 0; currentCharacterOfAlphabet < characterOptionsAmount; currentCharacterOfAlphabet++)
            // 如果 当前字符 对应的子节点 不为null，说明 其存在有 非空链接，因而 会在某个key中发挥作用，则：
            if (currentRootNode.characterToItsSubNode[currentCharacterOfAlphabet] != null) {
                // 返回 该节点 给上一级调用，表示 保留该节点，继续回溯过程
                return currentRootNode;
            }

        // 如果 当前节点 既 没有绑定value，又没有 非空子链接，说明 它现在已经是一个无用的节点了，则：
        // 返回null 来 从 单词查找树 中 物理删除 当前结点
        return null;
    }

    /**
     * 由trie树实现的符号表 数据类型 的 单元测试
     *
     * @param args 命令行参数    🐖 方法中 并没有使用到 命令行参数，而是 从标准输入中 获取到input
     */
    public static void main(String[] args) {
        TrieSTWebsite<Integer> st = new TrieSTWebsite<Integer>();
        // 读取标准输入，并 使用输入 来 构造符号表
        for (int currentSpot = 0; !StdIn.isEmpty(); currentSpot++) {
            String keyOnCurrentSpot = StdIn.readString();
            st.putInPairOf(keyOnCurrentSpot, currentSpot);
        }

        // 打印 符号表中的条目
        if (st.size() < 100) {
            StdOut.println("keys(\"\"):");
            for (String currentKey : st.getAllKeysInIterable()) {
                // key -> value
                StdOut.println(currentKey + " -> " + st.getAssociatedValueOf(currentKey));
            }
            StdOut.println();
        }

        // 获取到 符号表中 能够作为指定字符串的前缀 的 最长键
        StdOut.println("longestPrefixOf(\"shellsort\"):");
        StdOut.println(st.longestKeyThatIsPrefixOf("shellsort"));
        StdOut.println();

        StdOut.println("longestPrefixOf(\"quicksort\"):");
        StdOut.println(st.longestKeyThatIsPrefixOf("quicksort"));
        StdOut.println();

        // 获取到 符号表中 以指定字符串作为前缀的 所有键
        StdOut.println("keysWithPrefix(\"shor\"):");
        for (String s : st.keysWhosePrefixIs("shor"))
            StdOut.println(s);
        StdOut.println();

        // 获取到 符号表中 能够成功匹配模式字符串的 所有键
        StdOut.println("keysThatMatch(\".he.l.\"):");
        for (String s : st.keysThatMatch(".he.l."))
            StdOut.println(s);
    }
}