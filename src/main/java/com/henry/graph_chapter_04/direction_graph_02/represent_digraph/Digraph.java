package com.henry.graph_chapter_04.direction_graph_02.represent_digraph;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;

// 结论：可以使用邻接表 来 实现有向图这种逻辑结构；
// 用法：在构造方法中，读取 结点数量、边的数量、构成每条边的两个结点，然后使用 addEdge()来构造出完整的图
// 特征：相比于无向图，新增了一个 reverseEdgeDirection()的API，用于 得到原始有向图的反向图
public class Digraph {
    private final int vertexAmount;
    private int edgeAmount;
    private Bag<Integer>[] vertexToAdjacentVertexes; // 结点->相邻节点的集合

    public Digraph(In in) {
        this(in.readInt()); // 🐖 this()语句（对其他构造方法调用的快捷方式） 必须作为 构造方法中的第一个语句

        int edgeAmount = in.readInt(); // 局部变量用来存储图中总的边的数量
//        this.edgeAmount = in.readInt(); // 成员变量用来记录当前构建的图中的边的数量

        for (int currentEdge = 0; currentEdge < edgeAmount; currentEdge++) {
            int vertexV = in.readInt();
            int vertexW = in.readInt();

            addEdge(vertexV, vertexW);
        }

    }

    public Digraph(int vertexAmount) {
        this.vertexAmount = vertexAmount;
        this.edgeAmount = 0;
        vertexToAdjacentVertexes = new Bag[vertexAmount];

        for (int currentVertex = 0; currentVertex < vertexAmount; currentVertex++) {
            vertexToAdjacentVertexes[currentVertex] = new Bag<>();
        }

    }

    public int getVertexAmount() {
        return vertexAmount;
    }

    public int getEdgeAmount() {
        return edgeAmount;
    }

    public void addEdge(int vertexV, int vertexW) {
        vertexToAdjacentVertexes[vertexV].add(vertexW);
        edgeAmount++;
    }

    public Iterable<Integer> adjacentVertexesOf(int vertexV) {
        return vertexToAdjacentVertexes[vertexV];
    }

    // 反转有向图中边的方向，得到其反向图
    public Digraph reverseEdgeDirection() {
        Digraph edgeReversedGraph = new Digraph(vertexAmount);
        // 🐖 DFS中标准的结点遍历方式 - 按照自然数的顺序 来 遍历 有向图中的结点
        for (int currentVertex = 0; currentVertex < vertexAmount; currentVertex++) {
            for (Integer currentAdjacentVertex : adjacentVertexesOf(currentVertex)) {
                edgeReversedGraph.addEdge(currentAdjacentVertex, currentVertex);
            }
        }

        return edgeReversedGraph;
    }
}
