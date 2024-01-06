package com.henry.graph_chapter_04.no_direction_graph_01.symbol_graph;

import edu.princeton.cs.algs4.*;

// 验证：可以使用 符号表 与 简单图(图结点的值是整数) 来 实现符号图（图结点的值是字符串）
public class SymbolGraphLite {
    /* 实现 符号图 所需要的底层数据结构 */
    // 符号表 用于 记录节点值 -> 索引值 的映射
    private ST<String, Integer> vertexStrToVertexIndexST;
    // 字符串数组 用于 记录索引值 -> 节点值 的关系    作用与st刚好相反
    private String[] vertexIndexToVertexStrArr;
    // 从文件中构建出的图 用于索引图中的顶点
    private Graph vertexIndexGraph;

    /* constructor */
    // 参数列表： 文件流对象、文件分隔符字符串
    public SymbolGraphLite(String fileName, String delimiter) {
        /* 初始化成员变量 */
        // 初始化符号表 👇
        vertexStrToVertexIndexST = new ST<>();
        In fileStream = new In(fileName);
        // 逐行读取文件...
        while (fileStream.hasNextLine()) {
            // 把当前行分割成为 字符串数组
            String[] keyStrArr = fileStream.readLine().split(delimiter);

            // 把 节点值 -> 节点值的索引 的映射关系存储到 符号表中
            // 节点值的索引 = 当前符号表的size(字符串键在符号表中的次序)
            for (int currentSpot = 0; currentSpot < keyStrArr.length; currentSpot++) {
                String currentKeyStr = keyStrArr[currentSpot];

                if (!vertexStrToVertexIndexST.contains(currentKeyStr)) { // 避免重复添加
                    vertexStrToVertexIndexST.put(currentKeyStr, vertexStrToVertexIndexST.size());
                }
            }
        }

        // 初始化字符串数组indexToKeyStringArr中的元素 👇
        vertexIndexToVertexStrArr = new String[vertexStrToVertexIndexST.size()];

        for (String keyStr : vertexStrToVertexIndexST.keys()) {
            // keys[<索引值>] = <节点的值>
            Integer indexOfKey = vertexStrToVertexIndexST.get(keyStr);
            vertexIndexToVertexStrArr[indexOfKey] = keyStr;
        }

        // 初始化图G 👇
        // #1 初始化图中的节点数量
        vertexIndexGraph = new Graph(vertexStrToVertexIndexST.size());
        fileStream = new In(fileName);

        while (fileStream.hasNextLine()) {
            String[] keyStrArr = fileStream.readLine().split(delimiter);
            String firstKeyStr = keyStrArr[0];
            int indexOfFirstKeyStr = vertexStrToVertexIndexST.get(firstKeyStr);

            // 对于当前行其余的keyStr...
            for (int currentSpot = 1; currentSpot < keyStrArr.length; currentSpot++) {
                // #2 构造 index -> index的边，并添加到图中
                vertexIndexGraph.addEdge(indexOfFirstKeyStr, vertexStrToVertexIndexST.get(keyStrArr[currentSpot]));
            }
        }
    }

    /* public APIs */
    // 图中 是否存在 某个节点的值 为指定的字符串
    public boolean containsVertexWithName(String passedName) {
        return vertexStrToVertexIndexST.contains(passedName);
    }

    // 指定的字符串在图中的索引位置是多少？
    public int indexOfVertexWithName(String name) {
        return vertexStrToVertexIndexST.get(name);
    }

    // 指定索引位置上节点的值是什么？
    public String nameOfVertexWithIndex(int passedIndex) {
        return vertexIndexToVertexStrArr[passedIndex];
    }

    // 实现符号图的 底层的一般图 的字符串表示
    public Graph underlyingGraph() {
        return vertexIndexGraph;
    }

    public static void main(String[] args) {
        String filename = args[0];
        String delimiter = args[1];
        SymbolGraphLite symbolGraph = new SymbolGraphLite(filename, delimiter);

        // 这里向用例 暴露了其底层实现 - 这不是很好的设计~
        Graph underlyingGraph = symbolGraph.underlyingGraph();

        // 获取 符号图 的各种性质
        while (StdIn.hasNextLine()) {
            String source = StdIn.readLine();
            for (int currentIndex : underlyingGraph.adj(symbolGraph.indexOfVertexWithName(source))) {
                StdOut.println("   " + symbolGraph.nameOfVertexWithIndex(currentIndex));
            }
        }
    }
}
