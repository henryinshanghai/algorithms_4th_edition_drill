package com.henry.graph_chapter_04.direction_graph_02.path.dfs.strong_connected_components;

import com.henry.graph_chapter_04.direction_graph_02.graph.Digraph;
import com.henry.graph_chapter_04.direction_graph_02.path.dfs.vertex_traverse_order.DepthFirstOrder;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class KosarajuSCCLite {

    private boolean[] vertexToIsMarked; // 已经访问过的顶点
    private int[] vertexToItsComponentId; // 强连通分量的标识符
    private int componentAmount; // 强连通分量的数量

    public KosarajuSCCLite(Digraph digraph) {
        vertexToIsMarked = new boolean[digraph.getVertexAmount()];
        vertexToItsComponentId = new int[digraph.getVertexAmount()];

        // 获取到 有向图的反向图
        Digraph reversedDigraph = digraph.reverseEdgeDirection();
        // 获取到该反向图的 结点遍历所得到的结点序列
        DepthFirstOrder markedDigraphToGetWantedSequence = new DepthFirstOrder(reversedDigraph);

        // 获取到 反向图的逆后序遍历序列，然后顺序遍历序列中的结点：对结点进行标记和收集
        for (Integer currentVertex : markedDigraphToGetWantedSequence.vertexesInReversePostOrder()) {
            if (isNotMarked(currentVertex)) {
                markVertexesAndCollectToComponentViaDFS(digraph, currentVertex);
                componentAmount++;
            }
        }
    }

    private boolean isNotMarked(Integer currentVertex) {
        return !vertexToIsMarked[currentVertex];
    }

    private void markVertexesAndCollectToComponentViaDFS(Digraph digraph, Integer currentVertex) {
        vertexToIsMarked[currentVertex] = true;
        vertexToItsComponentId[currentVertex] = componentAmount;

        for (Integer currentAdjacentVertex : digraph.adjacentVertexesOf(currentVertex)) {
            if (isNotMarked(currentAdjacentVertex)) {
                // 标记结点 & 收集结点到component（组）中
                markVertexesAndCollectToComponentViaDFS(digraph, currentAdjacentVertex);
            }
        }
    }

    public boolean stronglyConnected(int vertexV, int vertexW) {
        return vertexToItsComponentId[vertexV] == vertexToItsComponentId[vertexW];
    }

    public int componentIdOf(int vertexV) {
        return vertexToItsComponentId[vertexV];
    }

    public int getComponentAmount() {
        return componentAmount;
    }

    /**
     * Unit tests the {@code KosarajuSharirSCC} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph digraph = new Digraph(in);
        KosarajuSCCLite vertexAssignedComponentId = new KosarajuSCCLite(digraph);

        // number of connected components
        int componentAmount = vertexAssignedComponentId.getComponentAmount();
        StdOut.println(componentAmount + " strong components");

        // compute list of vertices in each strong component
        Queue<Integer>[] components = (Queue<Integer>[]) new Queue[componentAmount];
        for (int currentComponentId = 0; currentComponentId < componentAmount; currentComponentId++) {
            components[currentComponentId] = new Queue<Integer>();
        }
        for (int currentVertex = 0; currentVertex < digraph.getVertexAmount(); currentVertex++) {
            int componentIdOfVertex = vertexAssignedComponentId.componentIdOf(currentVertex);
            components[componentIdOfVertex].enqueue(currentVertex);
        }

        // print results
        for (int currentComponentId = 0; currentComponentId < componentAmount; currentComponentId++) {
            Queue<Integer> currentComponent = components[currentComponentId];
            for (int currentVertex : currentComponent) {
                StdOut.print(currentVertex + " ");
            }
            StdOut.println();
        }
    }
}
