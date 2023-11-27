package com.henry.graph_chapter_04.no_direction_graph_01.symbol_graph;

import edu.princeton.cs.algs4.*;

public class SymbolGraphLite {
    /* 实现 符号图 所需要的底层数据结构 */
    // 符号表 用于 记录节点值 -> 索引值 的映射
    private ST<String, Integer> keyStrToIndexMap;
    // 字符串数组 用于 记录索引值 -> 节点值 的关系    作用与st刚好相反
    private String[] indexToKeyStrArr;
    // 从文件中构建出的图 用于索引图中的顶点
    private Graph indexConnectionGraph;

    /* constructor */
    // 参数列表： 文件流对象、文件分隔符字符串
    public SymbolGraphLite(String fileName, String sparator) {
        /* 初始化成员变量 */
        // 初始化符号表 👇
        keyStrToIndexMap = new ST<>();
        In fileStream = new In(fileName);
        // 逐行读取文件...
        while (fileStream.hasNextLine()) {
            // 把当前行分割成为 字符串数组
            String[] keyStrArr = fileStream.readLine().split(sparator);

            // 把 节点值 -> 节点值的索引 的映射关系存储到 符号表中
            // 节点值的索引 = 当前符号表的size(字符串键在符号表中的次序)
            for (int currentSpot = 0; currentSpot < keyStrArr.length; currentSpot++) {
                String currentKeyStr = keyStrArr[currentSpot];

                if (!keyStrToIndexMap.contains(currentKeyStr)) { // 避免重复添加
                    keyStrToIndexMap.put(currentKeyStr, keyStrToIndexMap.size());
                }
            }
        }

        // 初始化字符串数组indexToKeyStringArr中的元素 👇
        indexToKeyStrArr = new String[keyStrToIndexMap.size()];

        for (String keyStr : keyStrToIndexMap.keys()) {
            // keys[<索引值>] = <节点的值>
            Integer indexOfKey = keyStrToIndexMap.get(keyStr);
            indexToKeyStrArr[indexOfKey] = keyStr;
        }

        // 初始化图G 👇
        // #1 初始化图中的节点数量
        indexConnectionGraph = new Graph(keyStrToIndexMap.size());
        fileStream = new In(fileName);

        while (fileStream.hasNextLine()) {
            String[] keyStrArr = fileStream.readLine().split(sparator);
            String firstKeyStr = keyStrArr[0];
            int indexOfFirstKeyStr = keyStrToIndexMap.get(firstKeyStr);

            // 对于当前行其余的keyStr...
            for (int currentSpot = 1; currentSpot < keyStrArr.length; currentSpot++) {
                // #2 构造 index -> index的边，并添加到图中
                indexConnectionGraph.addEdge(indexOfFirstKeyStr, keyStrToIndexMap.get(keyStrArr[currentSpot]));
            }
        }
    }

    /* public APIs */
    // 图中 是否存在 某个节点的值 为指定的字符串
    public boolean contains(String keyStr) {
        return keyStrToIndexMap.contains(keyStr);
    }

    // 指定的字符串在图中的索引位置是多少？
    public int indexOf(String keyStr) {
        return keyStrToIndexMap.get(keyStr);
    }

    // 指定索引位置上节点的值是什么？
    public String nameOf(int passedIndex) {
        return indexToKeyStrArr[passedIndex];
    }

    // 实现符号图的 底层的一般图 的字符串表示
    public Graph underlyingGraph() {
        return indexConnectionGraph;
    }

    public static void main(String[] args) {
        String filename = args[0];
        String delimiter = args[1];
        SymbolGraphLite symbolGraph = new SymbolGraphLite(filename, delimiter);

        Graph underlyingGraph = symbolGraph.underlyingGraph();

        // 获取 符号图 的各种性质
        while (StdIn.hasNextLine()) {
            String source = StdIn.readLine();
            for (int currentIndex : underlyingGraph.adj(symbolGraph.indexOf(source))) {
                StdOut.println("   " + symbolGraph.nameOf(currentIndex));
            }
        }
    }
}
