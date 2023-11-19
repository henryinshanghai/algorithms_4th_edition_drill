package com.henry.string_05.word_search_tree_02.word_search_tree_03;

import jdk.internal.org.objectweb.asm.tree.analysis.Value;

public class TrieSymbolTableLite {

    private static int R = 256;
    private Node rootNode;

    private static class Node {
        private Object nodesValue;
        private Node[] nodesSuccessorNodes = new Node[R];
    }

    public Value getAssociatedValueOf(String passedKey) {
        Node retrievedNode = getAssociatedValueFrom(rootNode, passedKey, 0);
        if (retrievedNode == null) return null;
        return (Value) retrievedNode.nodesValue;
    }

    // 作用：从 当前单词查找树中，查询到 与passedKey的 [currentCharacterSpot, passedKey.length - 1]闭区间对应的子字符串匹配的路径，并返回 尾字符所对应的结点
    // 特征：树中是否存在“字符对应的结点” <-> 字符在树中“逻辑上”是否存在
    private Node getAssociatedValueFrom(Node currentNode, String passedKey, int currentCharacterSpot) {
        // #0 / #4 如果 当前结点为null的话，说明 “当前字符预期在树中对应的结点”不存在，这是一次未命中的查找，则：返回null
        if (currentNode == null) return null;

        // #1 在继续递归查找之前，先判断当前位置 是不是 待查找字符串键的最后一个字符
        // 如果是，说明 查询可能命中，则：返回当前结点
        if(currentCharacterSpot == passedKey.length()) return currentNode;

        // #2 获取到 “当前字符预期在树中对应的链接/结点/单词查找树”
        /*
            🐖 单词查找树的任何结点中都不会实际存储字符，因此 我们无法（不会）直接 对字符进行比较
            手段：直接 尝试获取 “当前字符预期在树中对应的链接/结点/单词查找树”  - 如果结点不为null，说明 该字符在单词查找树中，“逻辑上存在”
         */
        char currentCharacter = passedKey.charAt(currentCharacterSpot); // 获取字符
        Node successorNodeForCharacter = currentNode.nodesSuccessorNodes[currentCharacter]; // 获取字符在树中对应的结点

        // #3 得到 “当前字符预期在树中对应的结点”后，接着 在此子树中，递归地 - 查找 [currentCharacterSpot, key.length -1]闭区间对应子的字符串，并返回“尾字符所对应的结点”
        // 递归算法的子问题：在子树中，查询剩余的连续字符（子字符串）
        // 🐖 子问题的答案 就是 原始问题的答案
        return getAssociatedValueFrom(successorNodeForCharacter, passedKey, currentCharacterSpot + 1);
    }

    // 向符号表中添加一个 key -> value的映射
    public void putInPairOf(String passedKey, Value associatedValue) {
        rootNode = putPairInto(rootNode, passedKey, associatedValue, 0);
    }

    // 在当前的单词查找树中，查询 与 passedKey的 [currentCharacterSpot, passedKey.length - 1]闭区间所对应的子字符串 匹配的路径，
    // 如果找到，则：更新尾字符对应的结点上的值。如果没找到，则：向树中逐一添加缺少的字符(结点)，并在尾字符的结点上绑定值
    private Node putPairInto(Node currentNode, String passedKey, Value associatedValue, int currentCharacterSpot) {
        // #0 如果查询路径上遇到了空链接，说明 单词查找树中缺少针对当前字符的结点，则：创建一个空结点 链接上去 - 手段：直接return这个结点
        if (currentNode == null) currentNode = new Node();

        // #1 如果 键的尾字符 在树中已经存在，说明 单词查找树中，已经存在 用于表示被查找键的路径，则：更新键所对应的值
        // 手段：为尾字符对应的结点绑定传入的value，并返回该结点
        if(currentCharacterSpot == passedKey.length()) {
            currentNode.nodesValue = associatedValue;
            return currentNode;
        }

        // #2 获取到 “当前字符预期在树中对应的链接/结点/单词查找树”
        char currentCharacter = passedKey.charAt(currentCharacterSpot); // 找到第d个字符 所对应的子单词查找树
        Node nodesSuccessorNode = currentNode.nodesSuccessorNodes[currentCharacter];

        // #3 把在子树上递归调用的结果（添加了key->value所需要的结点的子树），绑定回 当前节点的后继子树 上
        currentNode.nodesSuccessorNodes[currentCharacter] = putPairInto(nodesSuccessorNode, passedKey, associatedValue, currentCharacterSpot + 1);

        return currentNode;
    }
}
