package com.henry.graph_chapter_04.graphs_min_spanning_tree_03.Prim.lazy_implementation.exe;

import com.henry.graph_chapter_04.graphs_min_spanning_tree_03.represent_weighted_grpah.Edge;
import com.henry.graph_chapter_04.graphs_min_spanning_tree_03.represent_weighted_grpah.EdgeWeightedGraph;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;

// 结论：可以 使用Prim算法 来 获取 加权无向连通图中的“最小展开树（MST）”
// 概念：
// ① MST - 一棵包含有 图中所有节点的树，并且能够保证 树中所有边的权重之和 是最小的；
// ② 横切边 - 对于图中的所有节点，我们可以人为的把它们分成两组。而那些连接着 这两组节点的边，就叫做 横切边
// ③ 节点的分类：{图节点、MST节点}；边的分类：{图边、MST边、横切边、连接两个MST节点的非MST边}
// 原理：
// ① 对于一幅 加权无向连通图的任意二切分结果，其所有横切边中 最小的那个，总是会属于MST；（切分定理）
// ② 对于 所有的MST节点，都会有一组 它所关联的横切边 - 收集每一个MST节点所关联的 “有效”最小横切边，就构成了 MST
// “Prim算法延迟实现”的步骤：
// #1 把 当前节点 添加为“树结点”； #2 把 当前节点的所有横切边 添加到 优先队列中；
// #3 循环队列，取 最小的横切边（判断有效性） 添加到MST中；对“横切边的非树节点”，重复 #1，#2
public class LazyPrimMSTSimple {
    private boolean[] vertexToIsTreeVertex; // 最小生成树中的 顶点
    private Queue<Edge> edgesQueueInMST; // 最小生成树中的 边
    private MinPQ<Edge> crossEdgesPQ; // 横切边（包含有失效的边）

    public LazyPrimMSTSimple(EdgeWeightedGraph weightedGraph) { // weightedGraph是 无向加权连通图（最好边的权重各不相同）
        crossEdgesPQ = new MinPQ<>();
        vertexToIsTreeVertex = new boolean[weightedGraph.getVertexAmount()];
        edgesQueueInMST = new Queue<>();

        addVertexInMSTAndAddItsCrossEdgesInPQ(weightedGraph, 0);

        // 如果 横切边优先队列中 还存在有“横切边”（可能已经无效），说明 仍旧存在有 图节点，则：
        // 需要 ① 把 最小横切边 添加为 MST的边；② 继续把 图节点 添加为 MST树节点；
        while (!crossEdgesPQ.isEmpty()) {
            // 获取到 优先队列中 当前的最小横切边
            Edge minCrossEdge = crossEdgesPQ.delMin();

            // 如果 该边的两个端点都是 MST节点，说明 由于MST节点的不断添加 该边已经无效，则：跳过该边
            // 🐖 在使用到横切边时，才会判断横切边的有效性。这种做法 我们称之为“延迟实现”
            // 特征：横切边优先队列中 会包含 一些无效边，因此while循环 会在MST树已经生成结束后，再空转很多次 来 continue这些无效边
            if (bothEndsAreMSTVertex(minCrossEdge)) continue;

            /* ① 把 该最小横切边 添加到MST中 原理：切分定理 - 最小横切边一定属于MST */
            // 否则，把 该横切边 添加到MST中
            edgesQueueInMST.enqueue(minCrossEdge);

            /* ② 把 边的非MST节点 也添加为 MST节点 */
            repeatOnNonMSTVertex(weightedGraph, minCrossEdge);
        }
    }

    private void repeatOnNonMSTVertex(EdgeWeightedGraph weightedGraph, Edge minCrossEdge) {
        // 获取到 横切边的两个端点
        int oneVertex = minCrossEdge.eitherVertex(),
                theOtherVertex = minCrossEdge.theOtherVertexAgainst(oneVertex);

        // 对于 其所有的 非MST节点的端点：① 把该端点添加为MST节点；② 把该端点所关联的所有横切边 添加到 横切边优先队列中
        if (isNotMSTVertex(oneVertex)) addVertexInMSTAndAddItsCrossEdgesInPQ(weightedGraph, oneVertex);
        if (isNotMSTVertex(theOtherVertex)) addVertexInMSTAndAddItsCrossEdgesInPQ(weightedGraph, theOtherVertex);
    }

    private boolean isNotMSTVertex(int passedVertex) {
        return !vertexToIsTreeVertex[passedVertex];
    }

    private boolean bothEndsAreMSTVertex(Edge minCrossEdge) {
        int oneVertex = minCrossEdge.eitherVertex(),
                theOtherVertex = minCrossEdge.theOtherVertexAgainst(oneVertex);

        return vertexToIsTreeVertex[oneVertex] && vertexToIsTreeVertex[theOtherVertex];
    }

    // 对于传入的节点：① 把该节点 添加为MST节点； ② 把 该节点所关联的所有横切边 添加到 横切边队列中
    private void addVertexInMSTAndAddItsCrossEdgesInPQ(EdgeWeightedGraph weightedGraph, int currentVertex) {
        /* #1 把 图节点 添加为 MST节点 */
        vertexToIsTreeVertex[currentVertex] = true;

        /* #2 把 节点所关联的所有横切边 添加到 横切边优先队列中 */
        // 对于 当前节点所关联的所有边...
        for (Edge currentAssociatedEdge : weightedGraph.getAssociatedEdgesOf(currentVertex)) {
            // 获取到 该边的另外一个节点
            int theOtherVertexInEdge = currentAssociatedEdge.theOtherVertexAgainst(currentVertex);

            // 如果 该节点 不是 MST节点，说明 该边是一条 “连接 MST树节点 和 图节点”的横切边，则：
            if (!vertexToIsTreeVertex[theOtherVertexInEdge]) {
                // 把 这样的边（横切边） 添加到 横切边的优先队列 中
                // 🐖 在添加时，这是一条有效的横切边。但随着MST节点的添加，它可能会无效（因为边的两个端点都变成了MST节点）
                crossEdgesPQ.insert(currentAssociatedEdge);
            }
        }
    }

    // 以 可迭代集合的方式（这里选择的是queue） 来 返回MST中所有的边
    public Iterable<Edge> getMSTEdges() {
        return edgesQueueInMST;
    }
}
