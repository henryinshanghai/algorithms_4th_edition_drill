package com.henry.string_05.word_search_tree_02.threeWay_trie_06.execution;

/******************************************************************************
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
    private int keysAmount;              // 符号表中键值对的数量
    private Node<Value> root;   // 三向trie树的根节点

    private static class Node<Value> {
        private char character;                        // 节点中 所持有的字符
        private Node<Value> leftSubtree, midSubtree, rightSubtree;  // 左子节点(树)，中子节点(树)，右子节点(树)
        private Value value;                     // key字符串 所关联的value（不是所有的节点 都有value，所以是optional的）
    }

    /**
     * 初始化一个空的 键为字符串的 符号表
     */
    public TSTWebsite() {
    }

    /**
     * 返回 符号表中 键值对的数量
     *
     * @return 符号表中 键值对的数量
     */
    public int size() {
        return keysAmount;
    }

    /**
     * 符号表中 包含有 指定的key吗？
     *
     * @param passedKeyStr 指定的key字符串
     * @return 如果 符号表中 包含有 指定的key，则 返回true。否则 返回false
     * @throws IllegalArgumentException 如果 传入的key为null
     */
    public boolean contains(String passedKeyStr) {
        if (passedKeyStr == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        return getAssociatedValueOf(passedKeyStr) != null;
    }

    /**
     * 获取到 符号表中 指定key 所关联的value
     *
     * @param passedStr 指定的key字符串
     * @return key所关联的value
     */
    public Value getAssociatedValueOf(String passedStr) {
        // 对 传入的字符串参数 进行校验
        if (passedStr == null) {
            throw new IllegalArgumentException("calls get() with null argument");
        }

        if (passedStr.length() == 0) {
            throw new IllegalArgumentException("key must have length >= 1");
        }

        // 获取到 传入的字符串的最后一个字符 在 三向trie树 中 所对应的结点
        Node<Value> foundNode = getLastNodeOfPathThatStartFrom(root, passedStr, 0);

        if (foundNode == null) return null;
        // 返回 node中所存储的value，即为 传入的keyStr所关联的value
        return foundNode.value;
    }

    /**
     * 获取到 当前三向trie树中，指定的key字符串 从指定位置到末尾位置的子字符串 的 对应路径中 的最后一个节点
     *
     * @param currentRootNode      当前的三向trie树
     * @param passedStr            传入的key字符串
     * @param currentCharacterSpot key字符串上的字符指针     为什么会想要这个参数呢？因为 链表、字符串、路径 都有相似的递归性 itself = current_item + rest_of(itself)
     * @return 路径中的最后一个节点
     */
    private Node<Value> getLastNodeOfPathThatStartFrom(Node<Value> currentRootNode,
                                                       String passedStr,
                                                       int currentCharacterSpot) {
        /* 〇 递归终止条件：查询到了 三向trie树的叶子节点 or 当前字符 在路径中 不存在对应的节点 */
        // 如果 当前结点 为 null，说明 查询结束 or 当前字符所对应的结点 在tire中 不存在，则：
        if (currentRootNode == null) {
            // 返回null 来 表示 trie中 不存在 passedStr所对应的结点
            return null;
        }
        if (passedStr.length() == 0) {
            throw new IllegalArgumentException("key must have length >= 1");
        }

        /* Ⅰ 本级递归要做的事情：① 判断 是否 已经找到了 预期节点；② 如果没有，则 继续查找 */
        // 子问题的结果 是 如何帮助解决原始问题的呢？ 答：子问题的结果 就是 原始问题的结果

        // 获取 “查询字符串”中的 当前字符
        char currentCharacter = passedStr.charAt(currentCharacterSpot);

        // 比较 当前字符 与 当前节点中所存储的字符，根据比较结果 来 进行对应的处理
        if (currentCharacter < currentRootNode.character) { // 如果 更小，说明 其对应的节点 在 左子trie树 中，则：
            // 在 左子树 中，继续 递归地查找
            return getLastNodeOfPathThatStartFrom(currentRootNode.leftSubtree,
                    passedStr,
                    currentCharacterSpot);
        } else if (currentCharacter > currentRootNode.character) { // 如果 更大，说明 在 右子trie树 中，则：
            // 在 右子树 中，继续 递归地查找
            return getLastNodeOfPathThatStartFrom(currentRootNode.rightSubtree,
                    passedStr,
                    currentCharacterSpot);
        } else if (hasNotReachSecondToLast(passedStr, currentCharacterSpot)) { // 如果 相等 && 字符指针 还没有到达 字符串末尾，说明 某个中间字符 命中，则：
            // 在 中子树 中，继续查找 下一个字符
            return getLastNodeOfPathThatStartFrom(currentRootNode.midSubtree,
                    passedStr,
                    currentCharacterSpot + 1);
        } else { // 如果 相等 && 字符指针 已经到达 字符串末尾，说明 查询过程结束，则：
            // 返回 这个结点
            return currentRootNode;
        }
    }

    // 判断 字符指针 是不是 已经移动到 字符串的倒数第二个字符 了
    private boolean hasNotReachSecondToLast(String passedStr, int currentCharacterSpot) {
        return currentCharacterSpot < passedStr.length() - 1;
    }

    /**
     * 把 键值对 插入到 符号表中，如果 键 已经存在于 符号表 中，则 覆盖 旧的value
     * 如果 传入的value 是 null，那么 这个操作就会 从 符号表 中 把 传入的key 给删除掉
     *
     * @param passedKey       指定的键
     * @param associatedValue 键所关联的值
     */
    public void putPairIntoSymbolTable(String passedKey, Value associatedValue) {
        if (passedKey == null) {
            throw new IllegalArgumentException("calls put() with null key");
        }

        // 如果 传入的key 是一个 新的key，则：
        if (!contains(passedKey)) {
            // 把 trie中的key计数 +1
            keysAmount++;
        } else if (associatedValue == null) {
            // 如果 传入的value 为 null，说明 使用方 想要删除 此键值对，则：
            // 把 trie中的key计数器 -1
            keysAmount--; // delete existing key
        }

        // 向 三向trie树 中，插入 passedStr中的各个字符 所对应的结点
        root = putNodesOfPathThatStartFrom(root, passedKey, associatedValue, 0);
    }

    /**
     * 向 当前trie树 中，插入 指定的key字符串 从指定位置到末尾位置的子字符串 中的各个字符 所对应的节点，并 在末尾节点处 绑定value
     *
     * @param currentRootNode      当前 三向trie树
     * @param passedKey            传入的key字符串
     * @param associatedValue      key所关联的value
     * @param currentCharacterSpot key字符串的字符指针
     * @return 插入了 各个字符 所对应的节点 后的 三向trie树（的根节点）
     */
    private Node<Value> putNodesOfPathThatStartFrom(Node<Value> currentRootNode,
                                                    String passedKey,
                                                    Value associatedValue,
                                                    int currentCharacterSpot) {
        /* 〇 递归的终止条件：not exist    这个实现中，并没有 显式地 写出 递归终止条件。因为 最后一层的递归 会像普通方法一样 执行完成，并 返回上层调用 */
        // 获取 key字符串中的 当前字符
        char currentCharacterOfPassedKey = passedKey.charAt(currentCharacterSpot);

        /* Ⅰ 本级递归要做的事情：① 查询 当前key字符 所对应的节点（如果其不存在，则 手动创建）； ② 在 key的末尾字符 所对应的节点 上 绑定value */
        // #1 如果 trie中的当前结点 为 null，说明 trie中 尚不存在有 此字符对应的节点，则：[具体地执行任务]
        if (currentRootNode == null) {
            // ① 在 trie树 中，为 当前字符 创建新结点 and
            currentRootNode = new Node<Value>();
            // ② 为 新创建的节点 绑定 当前字符
            currentRootNode.character = currentCharacterOfPassedKey;
        } // 🐖 这里创建完节点后，会进入到 第三个 或者 第四个 分支语句中

        // #2 根据 key的当前字符 与 三向trie树 当前结点中的字符 的比较结果 来 对应处理
        if (currentCharacterOfPassedKey < currentRootNode.character) { // 如果更小，说明 需要向左子树中插入，则：
            // 在左子树中 递归地插入 key的当前字符
            currentRootNode.leftSubtree =
                    putNodesOfPathThatStartFrom(currentRootNode.leftSubtree,
                            passedKey,
                            associatedValue,
                            currentCharacterSpot);
        } else if (currentCharacterOfPassedKey > currentRootNode.character) { // 如果更大，说明 需要向右子树中插入，则：
            // 在 右子树 中，继续 递归地插入 key的当前字符
            currentRootNode.rightSubtree =
                    putNodesOfPathThatStartFrom(currentRootNode.rightSubtree,
                            passedKey,
                            associatedValue,
                            currentCharacterSpot);
        } else if (hasNotReachSecondToLast(passedKey, currentCharacterSpot)) { // 如果 相等 && 字符指针还没有到末尾，说明 该中间字符 在三向trie树中 已经存在了，则：
            // 在 中子树 中，继续 递归地插入“下一个字符”
            currentRootNode.midSubtree =
                    putNodesOfPathThatStartFrom(currentRootNode.midSubtree,
                            passedKey,
                            associatedValue,
                            currentCharacterSpot + 1);
        } else { // 如果 相等 && 字符指针已经到了末尾，说明 查询到了 末尾字符对应的节点，则：
            // 为 此结点 绑定 associatedValue
            currentRootNode.value = associatedValue;
        }

        // Ⅱ 在向trie树中插入“字符串的每个字符”之后，返回三向单词查找树的根结点
        return currentRootNode;
    }


    /**
     * 获取到 符号表中存在的、能够作为 指定字符串 的最长前缀 的键。如果不存在，则返回null
     *
     * @param passedStr 指定的字符串
     * @return 符号表中所存在的、能够作为 指定字符串 的最长前缀 的键
     */
    public String keyWhoIsLongestPrefixOf(String passedStr) {
        // 〇 校验 传入的字符串参数 - {① null； ② 空字符串}
        if (passedStr == null) {
            throw new IllegalArgumentException("calls longestPrefixOf() with null argument");
        }
        if (passedStr.length() == 0)
            return null; // 返回null来表示：trie中不存在满足条件的key

        // Ⅰ 在 三向trie树 中，查找这个 唯一存在的key
        // ① 准备一些 在查找过程中，需要 被动态更新的 变量
        int currentKeysLength = 0; // 当前key的长度
        Node<Value> currentNode = root; // trie树中的当前结点
        int currentCharacterSpot = 0; // 字符串的字符指针 - 用于遍历 字符串中的字符

        // 在 trie树 中，按照 passedStr的guide 来 逐步深入地查找 key（找的key 必然是 其前缀），并 最终得到 最长的key(这意味着一个 比较&&更新 的过程)
        // say what, implement how
        while (hasNotReachLeaf(currentNode) && hasNotReachEnd(passedStr, currentCharacterSpot)) {
            // 获取到 当前的查询字符
            char currentCharacterInPassedStr = passedStr.charAt(currentCharacterSpot);

            // 根据 当前的查询字符 与 当前结点中的字符 的比较结果 来 导航到 正确的子树 中 继续查找
            if (currentCharacterInPassedStr < currentNode.character) // 如果更小，说明 在左子树中，
                // 则：更新 当前结点 以 在 正确的子树 中继续查找
                currentNode = currentNode.leftSubtree;
            else if (currentCharacterInPassedStr > currentNode.character) // 如果更大，说明 在右子树中，
                // 则：更新 当前结点 以 在 正确的子树 中继续查找
                currentNode = currentNode.rightSubtree;
            else { // 如果 相等, 说明 查询到了 当前字符，则：
                // ① 把 字符串指针 向后移动一个位置    用于 获取下一个字符
                currentCharacterSpot++;

                // ② 判断 是否是 key的末尾节点
                // 如果 当前结点的value 不为null，说明 找到了一个 新的有效的key，则：
                if (currentNode.value != null) {
                    // 更新 “当前key的长度” - 手段：使用 当前字符位置
                    currentKeysLength = currentCharacterSpot;
                }

                // ③ 更新 当前结点 以 在 正确的子树（中子树） 中继续查找 “下一个字符”
                currentNode = currentNode.midSubtree;
            }
        }

        // 从 查询字符串 中，使用 所得到的”当前key的长度“ 进行截取，得到 最长前缀键
        return passedStr.substring(0, currentKeysLength);
    }

    private boolean hasNotReachEnd(String passedStr, int currentCharacterSpot) {
        return currentCharacterSpot < passedStr.length();
    }

    private boolean hasNotReachLeaf(Node<Value> currentNode) {
        return currentNode != null;
    }


    /**
     * 获取到 符号表中 所有的key
     * 原理：得到 trie树中 所有的key
     *
     * @return 以 一个可迭代的集合 来 返回 符号表中所有的keys
     * 迭代keys的用法：foreach语法
     */
    public Iterable<String> getAllKeysInIterable() {
        Queue<String> keysQueue = new Queue<String>();
        collectKeysWhosePrefixIs(root, new StringBuilder(), keysQueue);
        return keysQueue;
    }

    /**
     * 获取到 符号表中 所有 以 指定字符串 作为前缀的key 所构成的集合
     *
     * @param passedStr 指定的前缀字符串(查询字符串)
     * @return 以Iterable集合的形式 来 返回 符号表中 所有 以 指定字符串 作为前缀的key 所构成的集合
     * @throws IllegalArgumentException 如果 传入的 前缀字符串参数 为null
     */
    public Iterable<String> keysWhosePrefixIs(String passedStr) {
        if (passedStr == null) {
            throw new IllegalArgumentException("calls keysWithPrefix() with null argument");
        }

        Queue<String> keysQueue = new Queue<String>();
        // #1 获取到 查询字符串的最后一个字符 在3-way trie树中 所对应的结点
        Node<Value> lastNodeInPath = getLastNodeOfPathThatStartFrom(root, passedStr, 0);

        /* #2 根据查询结果 进行分类讨论👇 */
        // 如果 最后一个节点 为 null，说明 字符串对应的路径 在3-way trie中 不存在，则：
        if (lastNodeInPath == null) {
            // 直接返回 空的queue
            return keysQueue;
        }

        // 如果 路径在trie中 存在，则：继续 分类讨论
        // ① [特殊情况] 如果 路径的最后一个节点 是一个 “key结点”，说明 传入的前缀字符串 本身就是一个key，则：
        if (lastNodeInPath.value != null) {
            // 把 查询字符串 直接收集到 keysQueue中...
            keysQueue.enqueue(passedStr);
        }

        // ② [一般情况] 如果 最后一个节点 不是 key节点，说明 还需要 继续 在trie树中查询，则：
        // 继续 在 中子树 中 查找并收集 以指定字符串作为前缀的 所有key  手段：借助另一个API
        Node<Value> trieToDig = lastNodeInPath.midSubtree;
        StringBuilder prefixStr = new StringBuilder(passedStr);
        collectKeysWhosePrefixIs(trieToDig, prefixStr, keysQueue);

        // #3 最终返回收集到的所有key
        return keysQueue;
    }

    /**
     * 收集 当前trie树中所存在的、以指定字符串作为前缀的 所有key 到 指定集合 中
     * 问题1：如何识别出 三向trie树中的key?  按照 三向trie树中节点的定义，只要节点的value不为null 它就是key的尾节点
     * 问题2：如何保证 key是 以指定字符串作为前缀的？ 在 三向trie树 中，按照 指定前缀字符串 所指示的路径 进行查询key
     * 问题3：怎么从 三向trie树中 得到key本身？ 使用 前缀字符串 拼接 当前尝试的字符
     * 问题4：怎么 把得到的key 收集到集合中?   queue作为方法参数，直接enqueue()即可
     *
     * @param currentRootNode   当前trie树
     * @param currentAttemptStr 当前拼接出来的字符串
     * @param keysQueue         key的集合
     */
    private void collectKeysWhosePrefixIs(Node<Value> currentRootNode,
                                          StringBuilder currentAttemptStr,
                                          Queue<String> keysQueue) {
        // 递归遍历3-way trie结点的过程中...
        /* 〇 递归终结条件：查询执行到了 trie树的叶子节点 */
        // 如果 当前结点 为null，说明 此分支 已经 探索完毕，则：
        if (reachToLeaf(currentRootNode))
            // 直接返回 以 继续探索 其他分支
            return;

        /* Ⅰ 本级递归需要做的事情：① 判断当前节点是不是key；② 继续在所有可能的路径中查找key； */
        // ① 判断当前节点是不是key；
        // 如果 当前节点 是一个 key节点，说明 识别到了 一个key，则：[具体地执行任务]
        if (isAKeyNode(currentRootNode)) { // [识别]
            String currentKeyStr = currentAttemptStr.toString() + currentRootNode.character;
            keysQueue.enqueue(currentKeyStr);
        } /* 🐖 这里不会 直接return，因为 后继的路径中 仍旧可能存在有 有效的key。只有 查询执行到null 时，才会return */

        // ② 继续 在 所有可能的路径 中 查找key；
        // 导航到 左子树中 查找key
        // 🐖：导航到 左子树，说明 没有选用 “当前结点的字符” 来 拼接 attemptStr，因此：前缀字符串 保持不变
        collectKeysWhosePrefixIs(currentRootNode.leftSubtree, currentAttemptStr, keysQueue);

        // 导航到 在中子树中 查找key
        // 导航到 中子树，说明 选用了“当前结点的字符” 来 拼接 attemptStr，则：需要 在前缀字符串中 拼接 当前字符
        collectKeysWhosePrefixIs(currentRootNode.midSubtree, currentAttemptStr.append(currentRootNode.character), keysQueue);

        // 在 继续尝试 在 右子树 中查找 之前，需要移除 导航到中子树时 在attemptStr中 所添加的字符
        currentAttemptStr.deleteCharAt(currentAttemptStr.length() - 1);

        // 导航到 在右子树中 查找key
        collectKeysWhosePrefixIs(currentRootNode.rightSubtree, currentAttemptStr, keysQueue);
    }

    private boolean isAKeyNode(Node<Value> currentRootNode) {
        return currentRootNode.value != null;
    }

    private boolean reachToLeaf(Node<Value> currentRootNode) {
        return currentRootNode == null;
    }


    // 返回 符号表中、能够匹配 模式字符串 的所有key，其中 字符. 会被解释成为 一个通配字符
    public Iterable<String> keysThatMatch(String patternStr) {
        Queue<String> matchedKeys = new Queue<String>();
        findAndCollectMatchedKeys(root,
                new StringBuilder(),
                patternStr,
                0,
                matchedKeys);
        return matchedKeys;
    }

    /**
     * 在 三向trie树 中，收集所有 与指定的模式字符串 相匹配(字符匹配&长度匹配) 的所有键
     * 问题1：如何判断 是key节点？ 三向trie树中，只要节点的value不为null，那 它就是一个key节点
     * 问题2：如何判断 key与模式字符串匹配？ 字符匹配：当前模式字符 与 trie树中的当前节点 是否相同；长度匹配：当前拼接的字符串 与 模式字符串 长度是否相同
     * 问题3：如何 得到key本身？  使用一个StringBuilder 来 不断收集 那些个 匹配模式字符成功的字符
     * 问题4：如何 收集匹配成功的key？   使用一个集合参数
     * 🐖 if/else if/else的语法结构 具有互斥性，而 在 模式字符是一个通配符 时，我们想要 尝试各个子树，所以 这里使用的是 3个并行的if结构
     * @param currentRootNode      当前trie树
     * @param currentAttemptStr    当前构造出的potential key
     * @param patternStr           指定的模式字符串
     * @param currentCharacterSpot 模式字符串的字符指针
     * @param keysQueue            用于收集 匹配成功的key的集合
     */
    private void findAndCollectMatchedKeys(Node<Value> currentRootNode,
                                           StringBuilder currentAttemptStr,
                                           String patternStr,
                                           int currentCharacterSpot,
                                           Queue<String> keysQueue) {
        /* 〇 递归终结条件：查询进行到 三向trie树的叶子节点 */
        if (reachToLeaf(currentRootNode))
            // 返回到 上一级调用 来 继续尝试 构造其他的potential key
            return;

        /* Ⅰ 本级递归需要做的事情：① 判断 当前节点 是不是 满足匹配条件的key；② 根据 当前的模式字符 来 扩展路径 以 找到 满足匹配条件的key */
        // 获取到 当前的模式字符 - 🐖 通配字符的匹配 并不是 “严格匹配”，因此 后继路径可能会延伸到任一子树中
        char currentPatternCharacter = patternStr.charAt(currentCharacterSpot);

        // [②]
        if (isWildcardCharacter(currentPatternCharacter) || needTakeLeft(currentRootNode, currentPatternCharacter))
            // 在左子树中 继续 识别、拼装和收集key [递归地执行任务]
            findAndCollectMatchedKeys(currentRootNode.leftSubtree,
                                        currentAttemptStr,
                                        patternStr,
                                        currentCharacterSpot,
                                        keysQueue);

        // [②]
        if (isWildcardCharacter(currentPatternCharacter) || needTakeMiddle(currentRootNode, currentPatternCharacter)) {
            // [特殊情况] 如果 条件1👇 并且 条件2👇，说明 找到了 key结点，则：[具体地执行任务] // [①]
            if (hasReachEnd(patternStr, currentCharacterSpot) && isAKeyNode(currentRootNode)) {
                String currentKeyStr = currentAttemptStr.toString() + currentRootNode.character;
                keysQueue.enqueue(currentKeyStr);
            }

            // [一般情况] 当前模式字符 匹配 当前结点，但是 还没有到达 模式字符串的 倒数第二个字符，说明 还需要 继续匹配，则：
            if (hasNotReachSecondToLast(patternStr, currentCharacterSpot)) {
                // ① 拼接上 当前结点的字符，然后 在中子树中 继续执行任务 - 识别、拼装与收集
                findAndCollectMatchedKeys(currentRootNode.midSubtree,
                                        currentAttemptStr.append(currentRootNode.character),
                                        patternStr,
                                        currentCharacterSpot + 1, // 因为这里+1了，所以 if条件是 未到达倒数第二个字符位置
                                        keysQueue);
                // ② 中子树 调用完成后，把 添加的字符(最后一个字符) 从currentAttemptStr中移除 - 以便 继续 从右子树中 尝试新的key
                currentAttemptStr.deleteCharAt(currentAttemptStr.length() - 1);
            }
        }

        // [②]
        if (isWildcardCharacter(currentPatternCharacter) || needTakeRight(currentRootNode, currentPatternCharacter))
            // 在右子树中 继续 识别、拼装和收集key
            findAndCollectMatchedKeys(currentRootNode.rightSubtree, currentAttemptStr, patternStr, currentCharacterSpot, keysQueue);
    }

    private boolean needTakeRight(Node<Value> currentRootNode, char currentPatternCharacter) {
        return currentPatternCharacter > currentRootNode.character;
    }

    private boolean hasReachEnd(String patternStr, int currentCharacterSpot) {
        return currentCharacterSpot == patternStr.length() - 1;
    }

    private boolean needTakeMiddle(Node<Value> currentRootNode, char currentPatternCharacter) {
        return currentPatternCharacter == currentRootNode.character;
    }

    private boolean needTakeLeft(Node<Value> currentRootNode, char currentPatternCharacter) {
        return currentPatternCharacter < currentRootNode.character;
    }

    private boolean isWildcardCharacter(char currentPatternCharacter) {
        return currentPatternCharacter == '.';
    }


    /**
     * 三向单词查找树 数据结构的单元测试
     * @param args 命令行参数
     */
    public static void main(String[] args) {

        /* 从标准输入中 创建出符号表对象 */
        TSTWebsite<Integer> symbolTable = new TSTWebsite<Integer>();

        for (int currentSpot = 0; !StdIn.isEmpty(); currentSpot++) {
            // 建立 key -> key's spot的映射关系
            String currentKey = StdIn.readString();
            symbolTable.putPairIntoSymbolTable(currentKey, currentSpot);
        }

        // 打印 符号表中的各个键值对
        if (symbolTable.size() < 100) {
            StdOut.println("keys(\"\"):");
            for (String currentKey : symbolTable.getAllKeysInIterable()) { // 获取到符号表中所有键的可迭代形式
                StdOut.println(currentKey + " " + symbolTable.getAssociatedValueOf(currentKey));
            }
            StdOut.println();
        }

        /* 尝试几个公开的API */
        // #1 符号表中存在的、能够作为指定字符串的前缀的最长key（单数）
        StdOut.println("longestPrefixOf(\"shellsort\"):");
        StdOut.println(symbolTable.keyWhoIsLongestPrefixOf("shellsort"));
        StdOut.println();

        StdOut.println("longestPrefixOf(\"shell\"):");
        StdOut.println(symbolTable.keyWhoIsLongestPrefixOf("shell"));
        StdOut.println();

        // #2 符号表中存在的、以指定字符串作为前缀的所有key（复数）
        StdOut.println("keysWithPrefix(\"shor\"):");
        for (String s : symbolTable.keysWhosePrefixIs("shor"))
            StdOut.println(s);
        StdOut.println();

        // #3 符号表中存在的、能够匹配指定模式字符串的所有key（复数）
        StdOut.println("keysThatMatch(\".he.l.\"):");
        for (String s : symbolTable.keysThatMatch(".he.l."))
            StdOut.println(s);
    }
}