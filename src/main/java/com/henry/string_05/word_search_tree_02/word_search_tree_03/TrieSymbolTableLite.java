package com.henry.string_05.word_search_tree_02.word_search_tree_03;


// 验证：可以使用 Trie数据结构 来 实现 key为字符串类型的符号表
// #1 获取 符号表 中，指定的key 所对应的value值👇：
// 在 trie树 中，查找 key字符串 所对应的路径，并返回 路径的最后一个结点 所绑定的value值；
// #2 向 符号表 中，添加 key->value的键值对👇：
// 在 trie树 中，查找 key字符串所对应的路径，如果 没找到，则 创建路径。如果 找到了，则：更新 路径的最后一个结点 所关联的value值
public class TrieSymbolTableLite<Key, Value> {

    private static int characterOptionsAmount = 256; // 所有可能的字符的数量
    private Node rootNode;

    // 🐖 结点本身 可以表示 一个字符 - 手段：使用 指向结点的链接/引用
    private static class Node {
        private Object value; // value 用于表示 符号表中key 所绑定的value（它 对结点来说 是optional的，不是所有的节点 都有value值）
        private final Node[] characterToItsSubNode = new Node[characterOptionsAmount]; // 用于表示 当前结点 所链接到的 其他所有结点
    }

    // 获取 符号表中，指定的键 所关联的值
    // 在 Trie的实现 中，这 等价于 在树中 找到 ”字符串键“所对应的路径中，最后一个结点 的value值。
    public Value getAssociatedValueOf(String passedKey) {
        Node retrievedNode = getLastNodeOfPathThatStartFrom(rootNode, passedKey, 0);

        // 如果 查询得到的结果 是 null，说明 树中不存在 键字符串 对应的路径，则：
        if (retrievedNode == null) {
            // 返回 null 来 表示 符号表中 不存在 与指定key关联的值
            return null;
        }

        // 否则，说明 树中 存在有 对应的路径，则：返回 路径的尾节点 的value值
        return (Value) retrievedNode.value;
    }

    /**
     * 作用：从 当前单词查找树 中，查询到 与 “passedKey中[currentCharacterSpot, passedKey.length-1]闭区间所对应的子字符串” 相匹配的路径，并返回 尾字符所对应的结点
     * 原理：passedKey字符串 对应到 树中，是一条由节点顺序连接所构成的路径；
     * passedKey -> its path in tree -> last node of the path in tree
     * 问题转换为：树中 存在有 这样的路径 吗？    手段：递归查找
     *
     * @param currentRootNode           当前的trie树
     * @param passedKey                 传入的键字符串
     * @param currentStartCharacterSpot 当前子字符串的首字符的位置   🐖 currentCharacterStartSpot参数 是实现 “更小的字符串”的一种手段
     * @return 如果 找到了路径，则 返回路径中的最后一个节点；如果没找到，返回 null
     */
    private Node getLastNodeOfPathThatStartFrom(Node currentRootNode, String passedKey, int currentStartCharacterSpot) {
        /* #〇 递归终结条件：① 对路径的查询 终结于 null； ② 对路径的查询 终结于 “字符串的尾字符”所对应的结点 */
        // ① 对路径的查询 终结于null，说明 “当前字符 预期在树中对应的结点” 不存在，这是一次 未命中的查找，则：
        if (currentRootNode == null) {
            // 返回 null
            return null;
        }

        // ② 如果 对路径的查询 终结于 字符串键的最后一个字符，说明 “预期的查询路径” 在 单词查找树 中 存在，这可能是一次命中的查找，则：
        // 🐖 我们总是 先找到 “字符对应的结点”(本次调用)，然后再 “判断路径 是否已经到达 字符串的尾字符”(下一次调用)，所以 这里的spot 已经是 +1后的值，因此 与length()判断相等
        if (currentStartCharacterSpot == passedKey.length()) {
            // 返回 当前结点
            return currentRootNode;
        }

        /* #1 递归算法的子问题：在 子trie子树 中，查询 更短的路径(剩余的子字符串） */
        // 获取到 “当前字符 预期在树中对应的 链接/结点/子单词查找树”
        // 手段：树中 由”该字符“所索引的链接 所指向的节点 是否为null <=> 字符是否存在
        char currentCharacter = passedKey.charAt(currentStartCharacterSpot);
        Node correspondingSubNode = currentRootNode.characterToItsSubNode[currentCharacter];

        // #2 使用 子问题的结果 来 帮助解决原始问题
        // 手段：在此子树中，继续查询 “子字符串 所对应的路径”
        // 原理：子问题的答案 就是 原始问题的答案
        return getLastNodeOfPathThatStartFrom(correspondingSubNode, passedKey, currentStartCharacterSpot + 1);
    }

    // 向 符号表 中 添加一个 (key->value)的 映射条目
    public void putInPairOf(String passedKey, Value associatedValue) {
        rootNode = putInNodesOfPathThatStartFrom(rootNode, passedKey, associatedValue, 0);
    }

    /**
     * 在 当前trie树 中，插入 字符串键 所对应的路径
     * 手段：在 当前的单词查找树 中，查询 与 passedKey的 [currentCharacterSpot, passedKey.length - 1]闭区间 所对应的子字符串 相匹配的路径，
     * 如果 找到，则：更新 尾字符所对应的结点 上的value。
     * 如果 没找到，则：向树中 逐一添加 缺少的字符(结点)，并 在 尾字符的结点 上 绑定value
     *
     * @param currentRootNode           当前trie树
     * @param passedKey                 传入的 键字符串
     * @param associatedValue           键所关联的值
     * @param currentStartCharacterSpot 当前子字符串的首字符的位置
     * @return 返回 插入了路径后的trie树
     */
    private Node putInNodesOfPathThatStartFrom(Node currentRootNode,
                                               String passedKey,
                                               Value associatedValue,
                                               int currentStartCharacterSpot) {
        /* #〇 递归终结条件：① 对路径的查询 终结于 null; ② 对路径的查询 终结于 “字符串的尾字符” 所对应的结点 */
        // ① 如果 查询路径上 遇到了 空链接，说明 单词查找树中 缺少 针对当前字符的结点，则：
        if (currentRootNode == null) {
            // 创建一个空结点 链接上去     手段：new出 空节点 并 return给上一级调用
            currentRootNode = new Node();
        }

        // ② 如果 对路径的查询 终结于 字符串键的最后一个字符，说明 “预期的查询路径” 在单词查找树中存在，这是一次 命中的查找，则：
        if (currentStartCharacterSpot == passedKey.length()) { // 🐖 spot是+1后的值，所以 这里比较的length(),而不是length()-1
            // 更新 键所对应的值，并 返回 当前结点
            currentRootNode.value = associatedValue;
            return currentRootNode;
        }

        /* #1 子问题：在 子树 中，插入 更短的路径 & #2 使用 子问题的结果 来 帮助解决 原始问题 */
        // ① 获取到 “当前字符 预期在树中对应的链接/结点/单词查找树”
        char currentCharacter = passedKey.charAt(currentStartCharacterSpot);
        Node correspondingSubNode = currentRootNode.characterToItsSubNode[currentCharacter];

        // ② 把 插入了更短路径的 trie子树，绑定回到 当前节点的后继子树 上（#1 + #2）
        currentRootNode.characterToItsSubNode[currentCharacter] = putInNodesOfPathThatStartFrom(correspondingSubNode, passedKey, associatedValue, currentStartCharacterSpot + 1);

        // ③ 最终返回 添加了 key所需要的路径 的单词查找树(的根结点)
        return currentRootNode;
    }

    // 单元测试：验证 使用Trie实现的符号表 是否能够正确地 插入与取出 条目
    public static void main(String[] args) {
        TrieSymbolTableLite<String, Integer> nameToAgeMap = new TrieSymbolTableLite<>();

        nameToAgeMap.putInPairOf("henry", 30);
        nameToAgeMap.putInPairOf("jack", 31);
        nameToAgeMap.putInPairOf("lvpeng", 29);
        nameToAgeMap.putInPairOf("bowei", 30);

        Integer henryAge = nameToAgeMap.getAssociatedValueOf("henry");

        System.out.println("henry's age is: " + henryAge);
    }
}
