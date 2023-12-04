package com.henry.graph_chapter_04.direction_graph_02.if_accessible_from_startVertex;

import com.henry.graph_chapter_04.direction_graph_02.graph.Digraph;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

// 结论：DFS算法 用于/能够标记 有向图中，从“指定的起始结点”可以到达的所有其他结点。
// 手段：使用一个名叫 vertexToIsMarked的数组 来 记录“指定的结点”是否“被标记”；
// 具体做法：对于 当前节点的所有相邻结点(可达结点)，如果它没有被标记，则：调用DFS对其进行标记（直到可达路径中的所有结点都被标记了，递归调用才会返回）。
public class DirectedDFS {
    private boolean[] vertexToIsMarked;

    public DirectedDFS(Digraph digraph, int startVertex) {
        vertexToIsMarked = new boolean[digraph.getVertexAmount()];
        markAllAccessibleVertexesStartFrom(digraph, startVertex);
    }

    public DirectedDFS(Digraph digraph, Iterable<Integer> startVertexes) {
        vertexToIsMarked = new boolean[digraph.getVertexAmount()];

        for (Integer currentStartVertex : startVertexes) {
            if (isNotMarked(currentStartVertex)) {
                markAllAccessibleVertexesStartFrom(digraph, currentStartVertex);
            }
        }
    }

    private boolean isNotMarked(Integer currentStartVertex) {
        return !vertexToIsMarked[currentStartVertex];
    }

    private void markAllAccessibleVertexesStartFrom(Digraph digraph, int currentVertex) {
        vertexToIsMarked[currentVertex] = true;

        for (Integer currentAdjacentVertex : digraph.adjacentVertexesOf(currentVertex)) {
            if (isNotMarked(currentAdjacentVertex)) {
                markAllAccessibleVertexesStartFrom(digraph, currentAdjacentVertex);
            }
        }
    }

    public boolean isAccessibleFromStartVertex(int vertexV) {
        return vertexToIsMarked[vertexV];
    }

    public static void main(String[] args) {
        Digraph digraph = new Digraph(new In(args[0]));

        Bag<Integer> startVertexes = new Bag<>();
        for (int currentArgSpot = 1; currentArgSpot < args.length; currentArgSpot++) {
            startVertexes.add(Integer.parseInt(args[currentArgSpot]));
        }

        DirectedDFS markedDigraph = new DirectedDFS(digraph, startVertexes);

        for (int currentVertex = 0; currentVertex < digraph.getVertexAmount(); currentVertex++) {
            if (markedDigraph.isAccessibleFromStartVertex(currentVertex)) {
                StdOut.print(currentVertex + " ");
            }
        }

        StdOut.println();
    }
}
