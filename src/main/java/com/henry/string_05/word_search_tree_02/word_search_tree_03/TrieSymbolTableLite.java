package com.henry.string_05.word_search_tree_02.word_search_tree_03;

import jdk.internal.org.objectweb.asm.tree.analysis.Value;

public class TrieSymbolTableLite {

    private static int R = 256;
    private Node rootNode;

    private static class Node {
        private Object value;
        private Node[] successorNodes = new Node[R];
    }

    public Value getAssociatedValueOf(String passedKey) {
        Node retrievedNode = getLastNodeOfPathThatStartFrom(rootNode, passedKey, 0);
        if (retrievedNode == null) return null;
        return (Value) retrievedNode.value;
    }

    // 作用：从 当前单词查找树中，查询到 与passedKey的 [currentCharacterSpot, passedKey.length - 1]闭区间对应的子字符串匹配的路径，并返回 尾字符所对应的结点
    // 特征：树中是否存在“字符对应的结点” <-> 字符在树中“逻辑上”是否存在
    /* 原始问题：在树中，查询 字符串键所对应的路径 */
    // 🐖 currentCharacterStartSpot参数 是实现 “更小的字符串”的一种手段
    private Node getLastNodeOfPathThatStartFrom(Node currentNode, String passedKey, int currentCharacterStartSpot) {
        /* #2 递归终结条件：① 对路径的查询 终结于null； ② 对路径的查询终结于“字符串的尾字符”所对应的结点 */
        // ① 对路径的查询 终结于null，说明 “当前字符预期在树中对应的结点”不存在，这是一次未命中的查找，则：返回null
        if (currentNode == null) return null;

        // ② 对路径的查询 终结于 字符串键的最后一个字符，说明“预期的查询路径”在单词查找树中存在，这可能是一次命中的查找，则：返回当前结点
        // 🐖 我们总是 先找到“字符对应的结点”(本次调用)，然后再 “判断路径是否已经到达字符串的尾字符”(下一次调用)，所以这里比较的是  length()
        if(currentCharacterStartSpot == passedKey.length()) return currentNode;

        /* #1 递归算法的子问题：在子树中，查询更短的路径(剩余的子字符串） & 使用 子问题的结果 来 帮助解决原始问题 */
        // #1-① 获取到 “当前字符预期在树中对应的链接/结点/单词查找树”
        /*
            🐖 单词查找树的任何结点中都不会实际存储字符，因此 我们无法（不会）直接 对字符进行比较
            手段：直接 尝试获取 “当前字符预期在树中对应的链接/结点/单词查找树”  - 如果结点不为null，说明 该字符在单词查找树中，“逻辑上存在”
         */
        char currentCharacter = passedKey.charAt(currentCharacterStartSpot); // 获取字符
        Node successorNodeForCharacter = currentNode.successorNodes[currentCharacter]; // 获取字符在树中对应的结点

        // #3 得到 “当前字符预期在树中对应的结点”后，接着 在此子树中，查询 子字符串对应的路径
        // 🐖 子问题的答案 就是 原始问题的答案
        return getLastNodeOfPathThatStartFrom(successorNodeForCharacter, passedKey, currentCharacterStartSpot + 1);
    }

    // 向符号表中添加一个 key -> value的映射
    public void putInPairOf(String passedKey, Value associatedValue) {
        rootNode = putInNodesOfPathThatStartFrom(rootNode, passedKey, associatedValue, 0);
    }

    // 在当前的单词查找树中，查询 与 passedKey的 [currentCharacterSpot, passedKey.length - 1]闭区间所对应的子字符串 匹配的路径，
    // 如果找到，则：更新尾字符对应的结点上的值。如果没找到，则：向树中逐一添加缺少的字符(结点)，并在尾字符的结点上绑定值
    /* 原始问题：在树中，插入 字符串键所对应的路径 */
    private Node putInNodesOfPathThatStartFrom(Node currentNode, String passedKey, Value associatedValue, int currentCharacterStartSpot) {
        /* #2 递归终结条件：① 对路径的查询终结于null; ② 对路径的查询终结于“字符串的尾字符”所对应的结点 */
        // ① 如果查询路径上遇到了空链接，说明 单词查找树中缺少针对当前字符的结点，则：创建一个空结点 链接上去
        // 手段：直接return新创建的结点
        if (currentNode == null) currentNode = new Node();

        // ② 对路径的查询 终结于 字符串键的最后一个字符，说明“预期的查询路径”在单词查找树中存在，这是一次命中的查找，则：更新键所对应的值，并 返回当前结点
        // 🐖 我们总是 先找到“字符对应的结点”(本次调用)，然后再 Ⅰ “判断路径是否已经到达字符串的尾字符”(下一次调用)、Ⅱ 获取结点的值、Ⅲ 更新结点的值...
        // 所以这里比较的是  length()
        if(currentCharacterStartSpot == passedKey.length()) {
            currentNode.value = associatedValue;
            return currentNode;
        }

        /* #1 子问题：在子树中，插入更短的路径 & 使用 子问题的结果 来 帮助解决原始问题 */
        // #1-① 获取到 “当前字符预期在树中对应的链接/结点/单词查找树”
        char currentCharacter = passedKey.charAt(currentCharacterStartSpot);
        Node nodesSuccessorNode = currentNode.successorNodes[currentCharacter];

        // #1-② 把在子树上递归调用的结果（添加了key->value所需要的结点的子树），绑定回 当前节点的后继子树 上
        currentNode.successorNodes[currentCharacter] = putInNodesOfPathThatStartFrom(nodesSuccessorNode, passedKey, associatedValue, currentCharacterStartSpot + 1);

        return currentNode;
    }
}
