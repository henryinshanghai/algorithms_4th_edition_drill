package com.henry.graph_chapter_04.direction_graph_02.graph;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;

public class Digraph {
    private final int vertexAmount;
    private int edgeAmount;
    private Bag<Integer>[] vertexToAdjacentVertexes;

    public Digraph(In in) {
        this(in.readInt()); // 这个语句必须是构造方法中的第一个语句

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
