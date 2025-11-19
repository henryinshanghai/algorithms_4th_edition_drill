package com.henry.graph_chapter_04.no_direction_graph_01.search_connected_vertexes.via_dfs.applications.if_exist_cycle_in_graph.code_execution;

import com.henry.graph_chapter_04.no_direction_graph_01.represent_graph.Graph;
import edu.princeton.cs.algs4.In;

// 验证：可以使用 ① 在图中从指定起点开始进行DFS（递归地标记结点）的方式 & ② 结点被标记的特征 来 判断 给定的图中 是否存在 环结构
// 特征：“当前结点” 它的所有“被标记的邻居结点”中，存在有 不是“它的出发结点”的结点，说明存在环；
// 原理：当 按照DFS的方式 递归地标记 顶点 时，
// ① 如果 是一条 单向的路径，则：当前顶点的 ”已经被标记的“邻居顶点，只可能会是 路径中的”上一个结点“。
// ② 否则的话，说明 路径中 存在有环
public class CycleExistInGraph {
    // #1 用于记录 顶点 -> 顶点是否被标记过 的映射
    private boolean[] vertexToIsMarked;
    // #2 用于表示 图中是否有环
    private boolean hasCycle;

    /**
     * 构造器方法
     * 一般作用：用于 创建 当前类的 对象实例
     * 此处的具体作用：对 指定图 中的每一个顶点，按需执行DFS 来 得到图的一些性质
     * 特征：一般 在构造器方法中 完成对成员变量的初始化
     *
     * @param graph 指定的图
     */
    public CycleExistInGraph(Graph graph) {
        // 实例化 成员变量
        vertexToIsMarked = new boolean[graph.vertexAmount()];

        // 对于 图中的每一个顶点
        for (int currentVertex = 0; currentVertex < graph.vertexAmount(); currentVertex++) {
            // 如果 该顶点 没有被标记，说明 它还没有被遍历到，则：
            if (isNotMarked(currentVertex)) {
                // 以 一个dummyVertex作为出发顶点、该顶点作为终点顶点 来 在图中进行DFS
                // 🐖 之所以用一个dummyVertex，是由于 当前顶点 是“起始顶点”，因此 不存在 其自己的“departVertex”
                int dummyDepartVertex = -1; // currentVertex
                markVertexAndDecideExistCycleViaDFS(graph, currentVertex, dummyDepartVertex);
            }
        }
    }

    private boolean isNotMarked(int currentVertex) {
        return !vertexToIsMarked[currentVertex];
    }

    // 作用：判断 图G中 是否存在环???
    // 手段：判断 terminalVertex的 所有”已经访问过的“邻居节点 中，是不是存在有 ”非departVertex“的结点。如果是，则：必然 存在环

    /**
     * 以 指定顶点 作为起始顶点s，在 指定的图 中 执行DFS 来 判断 图中当前的连通分量中 是否存在有环
     * 原理：按照DFS的方式 遍历图中的连通节点时，如果是一个不存在环的路径，则👇
     * 对于 当前未被标记的节点，其 被标记的邻居节点，只可能是 其出发节点。否则，说明 当前连通分量中存在有环
     * 特征：为了记录 当前节点的出发节点，我们 在DFS()的方法中，添加了 departVertex的参数
     *
     * @param graph          指定的图
     * @param terminalVertex 路径中 当前边的终止顶点
     * @param departVertex   路径中 当前边的出发顶点
     */
    private void markVertexAndDecideExistCycleViaDFS(Graph graph,
                                                     int terminalVertex,
                                                     int departVertex) {
        // 标记 当前"结束顶点"
        vertexToIsMarked[terminalVertex] = true;

        // 对于 当前“结束顶点”的每一个邻居顶点
        for (int currentAdjacentVertex : graph.adjacentVertexesOf(terminalVertex)) {
            // 如果 该邻居顶点 还没有被标记，说明 它是路径中 尚未被访问到的顶点...
            if (isNotMarked(currentAdjacentVertex)) {
                // 则 继续递归地对 当前邻居顶点 进行标记
                markVertexAndDecideExistCycleViaDFS(graph, currentAdjacentVertex, terminalVertex);
            } else if (currentAdjacentVertex != departVertex) // 如果 “该邻居结点” 已经被标记
                // 并且 它 不是 DFS路径中”当前边“的“出发节点”，说明 当前连通分量中 必然存在环，则👇
                // 把 ”用于表示是否存在环“的 布尔变量 标记为true
                hasCycle = true;
        }
    }

    // APIs

    /**
     * 判断图中 是否存在有 环
     * 手段：在DFS执行完成后，检查 是否有环的flag变量的值
     * 🐖 图中可能存在有 多个连通分量，只要 任意一个连通分量中存在有环，我们 就会设置这个flag变量
     *
     * @return 如果图中存在有环，则 返回true。否则 返回false
     */
    public boolean doesExistCycle() {
        return hasCycle;
    }


    public static void main(String[] args) {
        In in = new In(args[0]);
        Graph graph = new Graph(in);
        CycleExistInGraph markedGraph = new CycleExistInGraph(graph);

        if (markedGraph.doesExistCycle()) {
            System.out.println("给定的图中 存在有 环");
        } else {
            System.out.println("给定的图中 不存在有 环");
        }

    }
}
