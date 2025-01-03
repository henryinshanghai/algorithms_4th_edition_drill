package com.henry.graph_chapter_04.direction_graph_02.search_accessible_vertexes.via_dfs.applications.strong_connected_components_in_digraph_05.kosaraju;

import com.henry.graph_chapter_04.direction_graph_02.represent_digraph.Digraph;
import com.henry.graph_chapter_04.direction_graph_02.search_accessible_vertexes.via_dfs.applications.construct_vertex_traverse_results_in_different_order_04.DigraphPreAndPostTraverseOrderViaDFS;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

// 结论：使用Kosaraju算法（按照反向图的逆后序序列来对结点执行DFS），其构造函数中的每一次dfs()递归调用，所标记的结点 都会在“同一个强连通分量”之中
// 原理：#1 反向图 与 原始图 具有完全相同的强连通分量； #2 ??
// 算法步骤：#1 获取原始有向图的反向图; #2 获取到反向图G'的逆后序遍历的结点序列; #3 顺序遍历#2序列中的结点，使用DFS对结点进行标记&分组；
// 手段：使用一个名叫 vertexToItsComponentId的数组 来 指明“结点所属的强连通分量的id”（使用componentAmount来赋值）
public class KosarajuSCCLite {

    private boolean[] vertexToIsMarked; // 已经访问过的顶点
    private int[] vertexToItsComponentId; // 强连通分量的标识符
    private int componentAmount; // 强连通分量的数量 - 用于 作为强连通分量的id

    public KosarajuSCCLite(Digraph digraph) {
        vertexToIsMarked = new boolean[digraph.getVertexAmount()];
        vertexToItsComponentId = new int[digraph.getVertexAmount()];

        // #1 获取到 有向图的反向图 G'
        Digraph reversedDigraph = digraph.reverseEdgeDirection();
        // #2 获取到该反向图的 结点遍历所得到的结点序列 - PreOrder, PostOrder, ReversedPostOrder
        DigraphPreAndPostTraverseOrderViaDFS markedDigraph = new DigraphPreAndPostTraverseOrderViaDFS(reversedDigraph);

        // #3 ① 获取到 反向图的“逆后序遍历序列(ReversedPostOrder)”，然后 ② 在“原始有向图”中，顺序遍历“序列中的结点” 来 对结点进行标记和收集
        // 🐖 “逆后序遍历序列”的作用 - 用于确定 遍历“有向图中结点”的顺序 VS. DFS中标准的结点遍历方式（自然数顺序）
        for (Integer currentVertex : markedDigraph.vertexesInReversePostOrder()) {
            if (isNotMarked(currentVertex)) {
                // 标记当前结点 & 为其指定其所属的componentId
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

        // 获取图中 强连通分量的个数
        int componentAmount = vertexAssignedComponentId.getComponentAmount();
        StdOut.println(componentAmount + " strong components");

        // 使用集合 来 收集每个强连通分量中的结点
        Queue<Integer>[] components = (Queue<Integer>[]) new Queue[componentAmount];
        for (int currentComponentId = 0; currentComponentId < componentAmount; currentComponentId++) {
            components[currentComponentId] = new Queue<Integer>();
        }
        for (int currentVertex = 0; currentVertex < digraph.getVertexAmount(); currentVertex++) {
            int componentIdOfVertex = vertexAssignedComponentId.componentIdOf(currentVertex);
            components[componentIdOfVertex].enqueue(currentVertex);
        }

        // 打印每一个强连通分量中的结点
        for (int currentComponentId = 0; currentComponentId < componentAmount; currentComponentId++) {
            Queue<Integer> currentComponent = components[currentComponentId];
            for (int currentVertex : currentComponent) {
                StdOut.print(currentVertex + " ");
            }
            StdOut.println();
        }
    }
}
