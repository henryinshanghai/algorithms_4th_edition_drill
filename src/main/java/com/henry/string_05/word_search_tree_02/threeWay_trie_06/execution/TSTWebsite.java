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
        // 对传入的字符串参数 进行校验
        if (passedStr == null) {
            throw new IllegalArgumentException("calls get() with null argument");
        }
        if (passedStr.length() == 0)
            throw new IllegalArgumentException("key must have length >= 1");

        // 获取到 传入的字符串的最后一个字符 在trie树中所对应的结点
        Node<Value> foundNode = getNodeForLastCharacterOf(root, passedStr, 0);

        if (foundNode == null) return null;
        return foundNode.value;
    }

    // 返回 传入的字符串参数（的尾字符） 在trie中对应的结点
    private Node<Value> getNodeForLastCharacterOf(Node<Value> currentRootNode, String passedStr, int currentCharacterSpot) {
        // 如果当前结点为null，说明 #1 查询进行到了trie树的null结点，aka 查询结束 || #2 当前字符对应的结点在tire中不存在，则：
        if (currentRootNode == null) {
            // 返回null 来 表示trie中不存在 passedStr所对应的结点
            return null;
        }
        if (passedStr.length() == 0) throw new IllegalArgumentException("key must have length >= 1");

        // 获取“查询字符串”的当前字符
        char currentCharacter = passedStr.charAt(currentCharacterSpot);

        // 比较 当前字符 与 树的根结点中存储的字符
        if (currentCharacter < currentRootNode.character)
            return getNodeForLastCharacterOf(currentRootNode.leftSubtree, passedStr, currentCharacterSpot);
        else if (currentCharacter > currentRootNode.character)
            // [递归地执行任务]
            return getNodeForLastCharacterOf(currentRootNode.rightSubtree, passedStr, currentCharacterSpot);
        else if (currentCharacterSpot < passedStr.length() - 1)
            // 如果相等，说明 当前字符 与 当前结点匹配成功；&&
            // 如果 当前字符 不是“待查询字符串的尾字符”，说明 还没有找到 想要找到的结点
            // 则：在中子树中，继续查找 下一个字符
            return getNodeForLastCharacterOf(currentRootNode.midSubtree, passedStr, currentCharacterSpot + 1);
        else
            // 如果当前字符 与 当前结点的value匹配 && 当前字符是待查询字符串的尾字符，说明 已经找到了 想要的结点，
            // 则：返回这个结点[具体地执行任务]
            return currentRootNode;
    }

    // 把键值对 插入到 符号表中，如果键已经存在于符号表中，则 覆盖旧的value
    // 如果传入的value是null，那么 这个操作就会 从符号表中把传入的key给删除掉
    public void put(String passedKey, Value associatedValue) {
        if (passedKey == null) {
            throw new IllegalArgumentException("calls put() with null key");
        }

        // 如果传入的key是一个新的key，则：
        if (!contains(passedKey)) {
            // 把trie中的key计数+1
            keysAmount++;
        } else if (associatedValue == null) {
            // 如果传入的value为null，说明使用方想要删除此键值对，则：
            // 把trie中的key计数器-1
            keysAmount--; // delete existing key
        }

        // 向trie树中，插入passedStr的各个字符所对应的结点
        root = putNodesOfPathThatStartFrom(root, passedKey, associatedValue, 0);
    }

    private Node<Value> putNodesOfPathThatStartFrom(Node<Value> currentRootNode, String passedKey, Value associatedValue, int currentCharacterSpot) {
        // 获取“待插入字符串”的当前字符
        char currentCharacterOfPassedKey = passedKey.charAt(currentCharacterSpot);

        // #1 如果trie中的当前结点为null，说明trie中不存在有此字符，则：[具体地执行任务]
        if (currentRootNode == null) {
            // ① 在trie树中，为当前字符创建新结点 and
            currentRootNode = new Node<Value>();
            // ② 为创建的结点 绑定当前字符
            currentRootNode.character = currentCharacterOfPassedKey;
        }

        // #2 根据待插入字符 与 当前结点中的字符的比较结果 来 选择正确的子树插入字符
        if (currentCharacterOfPassedKey < currentRootNode.character) {
            currentRootNode.leftSubtree = putNodesOfPathThatStartFrom(currentRootNode.leftSubtree, passedKey, associatedValue, currentCharacterSpot);
        } else if (currentCharacterOfPassedKey > currentRootNode.character)
            currentRootNode.rightSubtree = putNodesOfPathThatStartFrom(currentRootNode.rightSubtree, passedKey, associatedValue, currentCharacterSpot);
        else if (currentCharacterSpot < passedKey.length() - 1)
            // 如果相等，说明  “当前字符” 在trie树中已经存在了； &&
            // 并且如果“当前字符”不是“待查询字符串的尾字符”，说明 字符串中的字符还没有完全添加到trie树中
            // 则：在中子树中，继续插入“下一个字符”[递归地执行任务]
            currentRootNode.midSubtree = putNodesOfPathThatStartFrom(currentRootNode.midSubtree, passedKey, associatedValue, currentCharacterSpot + 1);
        else
            // 如果向trie树中添加到了 “尾字符”，说明 已经到达了 要绑定value的结点，
            // 则：为此结点绑定 associatedValue
            currentRootNode.value = associatedValue;

        // #3 在向trie树中插入“字符串的每个字符”之后，返回三向单词查找树的根结点
        return currentRootNode;
    }

    // 返回符号表中存在的、作为指定 字符串的最长前缀的键。如果不存在，则返回null
    public String keyWhoIsLongestPrefixOf(String passedStr) {
        // #1 校验传入的字符串参数 - {① 传入的字符串对象是 null； ② 传入的字符串参数是 空字符串}
        if (passedStr == null) {
            throw new IllegalArgumentException("calls longestPrefixOf() with null argument");
        }
        if (passedStr.length() == 0)
            return null; // 返回null来表示：trie中不存在满足条件的key

        // #2 在trie中，查找这个唯一存在的key
        // ① 准备一些 在查找过程中需要被动态更新的变量
        int currentKeysLength = 0; // 当前key的长度
        Node<Value> currentNode = root; // trie树中的当前结点
        int currentCharacterSpot = 0; // 指向字符串中当前字符的指针 - 用于遍历字符串中的字符

        // 在trie树中，按照passedStr的guide 来 逐步深入地查找 key（找的key必然是其前缀），并最终得到 最长的key(这意味着一个比较&&更新的过程)
        // 循环结束条件：#1 到达trie树的叶子结点; || #2 字符位置到达结束位置
        while (currentNode != null && currentCharacterSpot < passedStr.length()) {
            // 获取到 传入字符串中的当前字符
            char currentCharacterInPassedStr = passedStr.charAt(currentCharacterSpot);

            // 根据 当前字符 与 当前结点中的字符的比较结果 来 导航到正确的子树中 继续查找
            if (currentCharacterInPassedStr < currentNode.character)
                // 更新当前结点 以 在正确的子树中继续查找
                currentNode = currentNode.leftSubtree;
            else if (currentCharacterInPassedStr > currentNode.character)
                // 更新当前结点 以 在正确的子树中继续查找
                currentNode = currentNode.rightSubtree;
            else { // 如果两个字符相等, 说明在trie树中匹配到了当前字符，则：在中子树中 继续查找 字符串中的下一个字符
                // 字符串的下一个字符    手段：更新字符指针到下一个位置
                currentCharacterSpot++;

                // 如果 当前结点的value不为null，说明 找到了一个有效的key，则：
                if (currentNode.value != null) {
                    // 更新“当前key的长度” - 手段：使用 当前字符位置
                    currentKeysLength = currentCharacterSpot;
                }

                // 更新当前结点 以便 继续在trie树中找到 更长的key
                currentNode = currentNode.midSubtree;
            }
        }

        // 从字符串中截取得到 最长前缀键 - 手段：使用得到的“最长前缀长度”
        return passedStr.substring(0, currentKeysLength);
    }

    // 以一个可迭代的对象 来 返回符号表中所有的keys
    // 迭代符号表st中所有key的方式 - 使用foreach语法： for (Key key : st.keys())
    // 原理：为了得到trie中所有的key，必须要遍历root trie中的所有结点
    public Iterable<String> getAllKeysInIterable() {
        Queue<String> keysQueue = new Queue<String>();
        collectKeysWhosePrefixIsInto(root, new StringBuilder(), keysQueue);
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
    public Iterable<String> keysWhosePrefixIs(String passedStr) {
        if (passedStr == null) {
            throw new IllegalArgumentException("calls keysWithPrefix() with null argument");
        }
        Queue<String> keysQueue = new Queue<String>();
        // #1 获取到 传入的字符串的最后一个字符 在3-way trie树中所对应的结点
        Node<Value> nodeForLastCharacter = getNodeForLastCharacterOf(root, passedStr, 0);

        /* #2 前缀字符串在trie中不存在的情况 */
        // 如果 其所对应的结点是null，说明 传入的前缀字符串 在3-way trie中不存在，则：
        if (nodeForLastCharacter == null) {
            // 直接返回空的queue
            return keysQueue;
        }

        /* #3 前缀字符串在trie中存在的情况 */
        // ① [特殊情况] 如果 其所对应的结点 是一个“key结点”，说明 传入的前缀字符串 本身就是一个key，则：
        if (nodeForLastCharacter.value != null) {
            // 把 传入的前缀字符串 [收集到] keysQueue中...
            keysQueue.enqueue(passedStr);
        }

        // ② [一般情况] 如果 其所对应的结点不为null，说明 传入的前缀字符串 在3-way trie中存在，则：在中子树中查找、拼装并收集所有的key
        // Ⅰ 获取到 此结点 在tire中的中子树 原因👇
        // 作为key的前缀，字符串的最后一个字符必然是匹配的(pick)。所以key中“除前缀字符串外剩下的字符” 需要在中子树中(dive in)继续匹配
        Node<Value> subTreeToKeepMatching = nodeForLastCharacter.midSubtree;
        // Ⅱ 把前缀字符串转换成为 SB对象 - 用于在识别到key时，拼装出具体的key
        StringBuilder givenPrefixSB = new StringBuilder(passedStr);
        // Ⅲ 在中子树中，识别、拼装并收集 其中存在的所有key
        collectKeysWhosePrefixIsInto(subTreeToKeepMatching, givenPrefixSB, keysQueue);

        // #4 最终返回收集到的所有key
        return keysQueue;
    }

    // 收集 以currentNode作为根结点的trie树中所存在的、以指定字符串作为前缀的所有key 到指定集合中
    // 识别key的手段：某个node的value值不为null
    // 拼装/获取key的手段：从起始node到key node的路径中，由 所有 路由到中子树的结点中的字符 顺序拼接得到完整的字符串；
    // 收集key的手段：在拼接得到key之后，把key添加到一个集合中；
    private void collectKeysWhosePrefixIsInto(Node<Value> currentRootNode, StringBuilder currentAttemptStr, Queue<String> keysQueue) {
        // 递归遍历3-way trie结点的过程中...
        // #0 [递归终结条件] 如果结点为null，说明此分支已经探索完毕，则：
        if (currentRootNode == null)
            // 直接返回 以继续探索其他分支
            return;

        // #1 遍历左子树中的结点，来识别、拼装并收集key [递归地执行任务]
        // 说明：如果使用左子树，说明 没有选用“当前结点的字符”来组成 attemptStr，因此：前缀字符串保持不变
        collectKeysWhosePrefixIsInto(currentRootNode.leftSubtree, currentAttemptStr, keysQueue);

        // 递归遍历3-way trie结点的过程中，如果结点的value不为null，说明识别到了keyStr的尾字符对应的结点，则：[具体地执行任务]
        if (currentRootNode.value != null) { // [识别]
            // ① [组装出] key字符串
            String keyStr = currentAttemptStr.toString() + currentRootNode.character;
            // ② 把组装出的key字符串 [收集到]keysQueue中
            keysQueue.enqueue(keyStr);
            /* 🐖 这里不会return，因为后继的路径中仍旧可能存在有 有效的key。只有查找到null时，才会return */
        }

        // #2 遍历中子树中的结点，来收集key
        // 如果使用中子树，说明 选用了“当前结点的字符”来组成 attemptStr，则：
        // 把当前结点中的字符 追加到 attemptStr中 - 用于最终组装出key字符串
        collectKeysWhosePrefixIsInto(currentRootNode.midSubtree, currentAttemptStr.append(currentRootNode.character), keysQueue);

        // 在继续尝试在right sub-trie中查找之前，移除上一步中 向attemptStr中所添加的字符
        currentAttemptStr.deleteCharAt(currentAttemptStr.length() - 1);

        // #3 遍历右子树中的结点，来收集key
        // 说明：如果使用右子树，说明 没有选用“当前结点的字符”来组成 attemptStr，因此：前缀字符串保持不变
        collectKeysWhosePrefixIsInto(currentRootNode.rightSubtree, currentAttemptStr, keysQueue);
    }


    // 返回符号表中 匹配模式字符串的所有key，其中字符.会被解释成为一个 通配字符
    public Iterable<String> keysThatMatch(String patternStr) {
        Queue<String> keysQueue = new Queue<String>();
        findAndCollectKeysThatMatchInto(root, new StringBuilder(), patternStr, 0, keysQueue);
        return keysQueue;
    }

    // TBD
    private void findAndCollectKeysThatMatchInto(Node<Value> currentRootNode, StringBuilder currentAttemptStr, String patternStr, int currentCharacterSpot, Queue<String> keysQueue) {
        // [递归终结条件] 如果 当前结点为null，说明 #1 分支探索到了null结点; #2 当前模式字符在trie中不存在，则：
        if (currentRootNode == null)
            // 直接返回，不再继续递归
            return;

        // 获取到当前的模式字符 - 🐖 通配字符的匹配并不是“严格匹配”，因此 后继路径可能会延伸到任一子树中
        char currentPatternCharacter = patternStr.charAt(currentCharacterSpot);

        // #1 如果模式字符为通配字符 或者 模式字符比起当前结点中的字符更小，说明 需要在左子树中继续执行任务，则：
        if (currentPatternCharacter == '.' || currentPatternCharacter < currentRootNode.character)
            // 在左子树中继续 识别、拼装和收集key [递归地执行任务]
            findAndCollectKeysThatMatchInto(currentRootNode.leftSubtree, currentAttemptStr, patternStr, currentCharacterSpot, keysQueue);

        // #2 如果模式字符为通配字符 或者 模式字符与当前结点中的字符相等（匹配），说明 需要在中子树中继续执行任务，则：
        if (currentPatternCharacter == '.' || currentPatternCharacter == currentRootNode.character) {
            // [特殊情况] 如果 xxx，说明找到了key结点，则：[具体地执行任务]
            if (currentCharacterSpot == patternStr.length() - 1 && currentRootNode.value != null) {
                // 拼接出 key
                String keyStr = currentAttemptStr.toString() + currentRootNode.character;
                // 把 拼接出的key 收集到 集合中
                keysQueue.enqueue(keyStr);
            }

            // [一般情况] 当前模式字符 匹配 当前结点，但是并没有到达模式字符串的末尾，说明 还需要继续匹配，则：
            if (currentCharacterSpot < patternStr.length() - 1) {
                // 拼接上当前结点的字符，然后 在中子树中继续执行任务 - 识别、拼装与收集
                findAndCollectKeysThatMatchInto(currentRootNode.midSubtree, currentAttemptStr.append(currentRootNode.character), patternStr, currentCharacterSpot + 1, keysQueue);
                // 中子树调用完成后，把最后一个字符冲 currentAttemptStr中移除 - 以便继续从右子树中尝试新的key
                currentAttemptStr.deleteCharAt(currentAttemptStr.length() - 1);
            }
        }

        // #3 如果模式字符为通配字符 或者 模式字符比起当前结点中的字符更大（匹配），说明 需要在右子树中继续执行任务，则：
        if (currentPatternCharacter == '.' || currentPatternCharacter > currentRootNode.character)
            // 在右子树中继续 识别、拼装和收集key
            findAndCollectKeysThatMatchInto(currentRootNode.rightSubtree, currentAttemptStr, patternStr, currentCharacterSpot, keysQueue);
    }


    /**
     * Unit tests the {@code TST} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        // 从标准输入中 创建出符号表对象
        TSTWebsite<Integer> symbolTable = new TSTWebsite<Integer>();

        for (int currentSpot = 0; !StdIn.isEmpty(); currentSpot++) {
            String currentKey = StdIn.readString();
            symbolTable.put(currentKey, currentSpot);
        }

        // 打印符号表中的各个键值对
        if (symbolTable.size() < 100) {
            StdOut.println("keys(\"\"):");
            for (String key : symbolTable.getAllKeysInIterable()) { // 获取到符号表中所有键的可迭代形式
                StdOut.println(key + " " + symbolTable.get(key));
            }
            StdOut.println();
        }

        /* 尝试几个公开的API */
        // 能够作为指定字符串的前缀的最长key
        StdOut.println("longestPrefixOf(\"shellsort\"):");
        StdOut.println(symbolTable.keyWhoIsLongestPrefixOf("shellsort"));
        StdOut.println();

        StdOut.println("longestPrefixOf(\"shell\"):");
        StdOut.println(symbolTable.keyWhoIsLongestPrefixOf("shell"));
        StdOut.println();

        // 以指定字符串作为前缀的所有key
        StdOut.println("keysWithPrefix(\"shor\"):");
        for (String s : symbolTable.keysWhosePrefixIs("shor"))
            StdOut.println(s);
        StdOut.println();

        // 匹配指定模式字符串的所有key
        StdOut.println("keysThatMatch(\".he.l.\"):");
        for (String s : symbolTable.keysThatMatch(".he.l."))
            StdOut.println(s);
    }
}