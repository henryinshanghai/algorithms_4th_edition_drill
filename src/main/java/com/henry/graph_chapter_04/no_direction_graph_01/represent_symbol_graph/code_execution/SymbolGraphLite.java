package com.henry.graph_chapter_04.no_direction_graph_01.represent_symbol_graph.code_execution;

import edu.princeton.cs.algs4.*;

// 验证：可以使用 简单图(图结点的值是int) 与 符号表  来 实现符号图（图结点的值是字符串）
public class SymbolGraphLite {
    /* 实现 符号图 所需要的底层数据结构 - 符号图中的结点 {vertexName->vertexId} 结点的两个属性:id、name */
    private Graph vertexesIdGraph; // 简单图 用于记录结点(id->id)之间的关联关系
    private ST<String, Integer> vertexNameToVertexIdST; // 符号表 用于记录单个结点 name->id 之间的映射关系
    private String[] vertexIdToNameArr; // 数组 用于记录 结点id->name 的逆向映射

    // 参数列表： 文件流对象、文件分隔符字符串
    public SymbolGraphLite(String fileName, String delimiter) {
        // 初始化底层所使用的 符号表(str->int)
        constructSTBasedOn(fileName, delimiter);

        // 初始化 记录 id->name逆向映射的数组
        initReverseArrForST();

        // 初始化 底层所使用的简单图(int->int)
        constructSimpleGraphBasedOn(fileName, delimiter);
    }

    private void constructSimpleGraphBasedOn(String fileName, String delimiter) {
        // #1 实例化图G
        vertexesIdGraph = new Graph(vertexNameToVertexIdST.size());
        In fileStream = new In(fileName);

        while (fileStream.hasNextLine()) {
            String[] vertexNameArr = fileStream.readLine().split(delimiter);
            String firstVertexName = vertexNameArr[0];
            int firstVertexId = vertexNameToVertexIdST.get(firstVertexName);

            // 对于当前行其余的keyStr...
            for (int currentSpot = 1; currentSpot < vertexNameArr.length; currentSpot++) {
                // #1 获取到它的index
                String currentVertexName = vertexNameArr[currentSpot];
                Integer itsVertexId = vertexNameToVertexIdST.get(currentVertexName);
                // #2 把 用于连接首个vertex的id -> 当前vertex的id的边 给添加到图中
                vertexesIdGraph.addEdge(firstVertexId, itsVertexId);
            }
        }
    }

    private void constructSTBasedOn(String fileName, String delimiter) {
        // 实例化符号表对象
        vertexNameToVertexIdST = new ST<>();
        // 从文件名中得到文件流
        In fileStream = new In(fileName);

        // 从文件流中逐行读取文件中的每一行 行中包含有由空格分隔的多个name
        while (fileStream.hasNextLine()) {
            // 使用分割字符 来 把当前行分割成为 字符串数组
            String[] nameArr = fileStream.readLine().split(delimiter);

            // 对于每一个str，把 str->int(??) 的映射关系存储到 符号表中
            for (int currentSpot = 0; currentSpot < nameArr.length; currentSpot++) {
                // #1 获取到当前str
                String currentVertexName = nameArr[currentSpot];

                // #2 如果 当前str 还不存在于 符号表中
                if (!vertexNameToVertexIdST.contains(currentVertexName)) { // 避免重复添加
                    // 则：把 name->id 顺序地从尾部追加到符号表
                    // 🐖 由于是尾部添加，所以这里的id = 当前符号表的size(字符串键在符号表中的次序)
                    vertexNameToVertexIdST.put(currentVertexName, vertexNameToVertexIdST.size());
                }
            }
        }
    }

    private void initReverseArrForST() {
        // 实例化数组
        vertexIdToNameArr = new String[vertexNameToVertexIdST.size()];

        // 对于符号表中的每一个entry中的key...
        for (String currentVertexName : vertexNameToVertexIdST.keys()) {
            // ST[<id>] = <name>
            Integer itsAssociatedId = vertexNameToVertexIdST.get(currentVertexName);
            vertexIdToNameArr[itsAssociatedId] = currentVertexName;
        }
    }

    /* public APIs */
    // 图中 是否存在 某个节点的值 为指定的字符串
    public boolean containsVertexWithName(String passedName) {
        return vertexNameToVertexIdST.contains(passedName);
    }

    // 指定的字符串在图中的索引位置是多少？
    public int indexOfVertexWithName(String name) {
        return vertexNameToVertexIdST.get(name);
    }

    // 指定索引位置上节点的值是什么？
    public String nameOfVertexWithIndex(int passedIndex) {
        return vertexIdToNameArr[passedIndex];
    }

    // 实现符号图的 底层的一般图 的字符串表示
    public Graph underlyingGraph() {
        return vertexesIdGraph;
    }

    public static void main(String[] args) {
        String filename = args[0];
        String delimiter = args[1];
        // #1 创建一个 符号图的对象
        SymbolGraphLite symbolGraph = new SymbolGraphLite(filename, delimiter);

        // #2 获取到符号图的底层图
        // 🐖 这里向用例 暴露了其底层实现 - 这不是很好的设计~
        Graph underlyingGraph = symbolGraph.underlyingGraph();

        // #3 获取 符号图 的各种性质
        while (StdIn.hasNextLine()) {
            // 对于 从标准输入中读取到的字符串...
            String inputStr = StdIn.readLine();

            int itsAssociatedIndex = symbolGraph.indexOfVertexWithName(inputStr);
            for (int currentNeighborIndex : underlyingGraph.adj(itsAssociatedIndex)) {
                String vertexesKeyStr = symbolGraph.nameOfVertexWithIndex(currentNeighborIndex);
                StdOut.println("   " + vertexesKeyStr);
            }
        }
    }
}
