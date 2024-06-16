package com.henry.string_05.word_search_tree_02.word_search_tree_03;

import jdk.internal.org.objectweb.asm.tree.analysis.Value;

// 验证：可以使用 Trie数据结构 来 实现key为字符串的符号表
// 获取符号表中，指定的key所对应的value值：在trie树中，查找 key字符串所对应的路径，并返回路径的最后一个结点上所绑定的value值；
// 向符号表中，添加 key->value的键值对：在trie树中，查找 key字符串所对应的路径，如果没找到，则创建路径。如果找到了，则：更新路径的最后一个结点所关联的value值
public class TrieSymbolTableLite {

    private static int characterOptionsAmount = 256; // 所有可能的字符的数量
    private Node rootNode;

    // 🐖 结点本身可以表示一个字符 - 手段：使用 指向结点的链接/引用
    private static class Node {
        private Object value; // value用于表示 符号表中key所绑定的value（它对结点本身是optional的）
        private Node[] characterToSuccessorNodes = new Node[characterOptionsAmount]; // 用于表示 当前结点 所链接到的其他所有结点
    }

    // 获取符号表中，指定的键所关联的值
    // 在Trie的实现中，等价于 在树中找到”字符串键“所对应的路径中，最后一个结点的value值。
    public Value getAssociatedValueOf(String passedKey) {
        Node retrievedNode = getLastNodeOfPathThatStartFrom(rootNode, passedKey, 0);
        if (retrievedNode == null) return null;
        return (Value) retrievedNode.value;
    }

    // 作用：从 当前单词查找树中，查询到 与 “passedKey中[currentCharacterSpot, passedKey.length-1]闭区间所对应的子字符串” 相匹配的路径，并返回 尾字符所对应的结点
    // 方法作用的流程：passedKey -> its path in tree -> last node of the path in tree
    // 特征：树中是否存在“字符对应的结点” <-> 字符在树中“逻辑上”是否存在
    /* 原始问题：在树中，查询 字符串键所对应的路径 */
    // 🐖 currentCharacterStartSpot参数 是实现 “更小的字符串”的一种手段
    private Node  getLastNodeOfPathThatStartFrom(Node currentRootNode, String passedKey, int currentStartCharacterSpot) {
        /* #2 递归终结条件：① 对路径的查询 终结于null； ② 对路径的查询终结于“字符串的尾字符”所对应的结点 */
        // ① 对路径的查询 终结于null，说明 “当前字符预期在树中对应的结点”不存在，这是一次未命中的查找，则：返回null
        if (currentRootNode == null) return null;

        // ② 如果 对路径的查询 终结于 字符串键的最后一个字符，说明“预期的查询路径”在单词查找树中存在，这可能是一次命中的查找，则：返回当前结点
        // 🐖 我们总是 先找到“字符对应的结点”(本次调用)，然后再 “判断路径是否已经到达字符串的尾字符”(下一次调用)，所以这里比较的是  length()
        if(currentStartCharacterSpot == passedKey.length()) return currentRootNode;

        /* #1 递归算法的子问题：在子树中，查询更短的路径(剩余的子字符串） & 使用 子问题的结果 来 帮助解决原始问题 */
        // #1-① 获取到 “当前字符预期在树中对应的链接/结点/单词查找树”
        /*
            🐖 单词查找树的任何结点中都不会实际存储字符，因此 我们无法（不会）直接 对字符进行比较
            手段：直接 尝试获取 “当前字符预期在树中对应的链接/结点/单词查找树”  - 如果结点不为null，说明 该字符在单词查找树中，“逻辑上存在”
         */
        // 获取到“当前位置”参数上的字符
        char currentCharacter = passedKey.charAt(currentStartCharacterSpot);
        // 获取到“字符在树中所对应的结点” 🐖 结点的“是否存在性” <-> 树中 是否存在 结点表示的字符/字符所对应的结点
        Node successorNodeForCharacter = currentRootNode.characterToSuccessorNodes[currentCharacter];

        // #3 得到 “当前字符预期在树中对应的结点”后，接着 在此子树中，查询 “子字符串所对应的路径”
        // 子树 <-> 以结点作为根结点的树 “子字符串所对应的路径” <-> passedKey + 指定的起始字符
        // 🐖 子问题的答案 就是 原始问题的答案
        return getLastNodeOfPathThatStartFrom(successorNodeForCharacter, passedKey, currentStartCharacterSpot + 1);
    }

    // 向符号表中添加一个 key -> value的映射
    // 在Trie的实现中，
    public void putInPairOf(String passedKey, Value associatedValue) {
        rootNode = putInNodesOfPathThatStartFrom(rootNode, passedKey, associatedValue, 0);
    }

    // 在当前的单词查找树中，查询 与 passedKey的 [currentCharacterSpot, passedKey.length - 1]闭区间所对应的子字符串 匹配的路径，
    // 如果找到，则：更新尾字符对应的结点上的值。如果没找到，则：向树中逐一添加缺少的字符(结点)，并在尾字符的结点上绑定值
    /* 原始问题：在树中，插入 字符串键所对应的路径 */
    private Node putInNodesOfPathThatStartFrom(Node currentRootNode, String passedKey, Value associatedValue, int currentStartCharacterSpot) {
        /* #2 递归终结条件：① 对路径的查询终结于null; ② 对路径的查询终结于“字符串的尾字符”所对应的结点 */
        // ① 如果查询路径上遇到了空链接，说明 单词查找树中缺少针对当前字符的结点，则：创建一个空结点 链接上去
        // 手段：直接return新创建的结点
        if (currentRootNode == null) currentRootNode = new Node();

        // ② 如果 对路径的查询 终结于 字符串键的最后一个字符，说明“预期的查询路径”在单词查找树中存在，这是一次命中的查找，则：更新键所对应的值，并 返回当前结点
        // 🐖 我们总是 先找到“字符对应的结点”(本次调用)，然后再 Ⅰ “判断路径是否已经到达字符串的尾字符”(下一次调用)、Ⅱ 获取结点的值、Ⅲ 更新结点的值...
        // 所以这里比较的是  length()
        if(currentStartCharacterSpot == passedKey.length()) {
            currentRootNode.value = associatedValue;
            return currentRootNode;
        }

        /* #1 子问题：在子树中，插入更短的路径 & 使用 子问题的结果 来 帮助解决原始问题 */
        // #1-① 获取到 “当前字符预期在树中对应的链接/结点/单词查找树”
        char currentCharacter = passedKey.charAt(currentStartCharacterSpot);
        Node successorNodeForCharacter = currentRootNode.characterToSuccessorNodes[currentCharacter];

        // #1-② 把在子树上递归调用的结果（添加了key->value所需要的结点的子树），绑定回 当前节点的后继子树 上
        currentRootNode.characterToSuccessorNodes[currentCharacter] = putInNodesOfPathThatStartFrom(successorNodeForCharacter, passedKey, associatedValue, currentStartCharacterSpot + 1);

        // 最终返回 添加了key所需要的路径的单词查找树(的根结点)
        return currentRootNode;
    }
}
