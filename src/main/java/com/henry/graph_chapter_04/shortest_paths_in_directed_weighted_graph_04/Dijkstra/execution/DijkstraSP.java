package com.henry.graph_chapter_04.shortest_paths_in_directed_weighted_graph_04.Dijkstra.execution;
/******************************************************************************
 *  Compilation:  javac DijkstraSP.java
 *  Execution:    java DijkstraSP input.txt s
 *  Dependencies: EdgeWeightedDigraph.java IndexMinPQ.java Stack.java DirectedEdge.java
 *  Data files:   https://algs4.cs.princeton.edu/44sp/tinyEWD.txt
 *                https://algs4.cs.princeton.edu/44sp/mediumEWD.txt
 *                https://algs4.cs.princeton.edu/44sp/largeEWD.txt
 *
 *  Dijkstra's algorithm. Computes the shortest path tree.
 *  Assumes all weights are non-negative.
 *
 *  % java DijkstraSP tinyEWD.txt 0
 *  0 to 0 (0.00)
 *  0 to 1 (1.05)  0->4  0.38   4->5  0.35   5->1  0.32
 *  0 to 2 (0.26)  0->2  0.26
 *  0 to 3 (0.99)  0->2  0.26   2->7  0.34   7->3  0.39
 *  0 to 4 (0.38)  0->4  0.38
 *  0 to 5 (0.73)  0->4  0.38   4->5  0.35
 *  0 to 6 (1.51)  0->2  0.26   2->7  0.34   7->3  0.39   3->6  0.52
 *  0 to 7 (0.60)  0->2  0.26   2->7  0.34
 *
 *  % java DijkstraSP mediumEWD.txt 0
 *  0 to 0 (0.00)
 *  0 to 1 (0.71)  0->44  0.06   44->93  0.07   ...  107->1  0.07
 *  0 to 2 (0.65)  0->44  0.06   44->231  0.10  ...  42->2  0.11
 *  0 to 3 (0.46)  0->97  0.08   97->248  0.09  ...  45->3  0.12
 *  0 to 4 (0.42)  0->44  0.06   44->93  0.07   ...  77->4  0.11
 *  ...
 *
 ******************************************************************************/


import com.henry.basic_chapter_01.collection_types.stack.implementation.via_linked_node.Stack;
import com.henry.graph_chapter_04.shortest_paths_in_directed_weighted_graph_04.DirectedEdge;
import com.henry.graph_chapter_04.shortest_paths_in_directed_weighted_graph_04.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.StdOut;

/**
 * The {@code DijkstraSP} class represents a data type for solving the
 * single-source shortest paths problem in edge-weighted digraphs
 * where the edge weights are non-negative.
 * <p>
 * This implementation uses <em>Dijkstra's algorithm</em> with a
 * <em>binary heap</em>. The constructor takes
 * &Theta;(<em>E</em> log <em>V</em>) time in the worst case,
 * where <em>V</em> is the number of vertices and <em>E</em> is
 * the number of edges. Each instance method takes &Theta;(1) time.
 * It uses &Theta;(<em>V</em>) extra space (not including the
 * edge-weighted digraph).
 * <p>
 * This correctly computes shortest paths if all arithmetic performed is
 * without floating-point rounding error or arithmetic overflow.
 * This is the case if all edge weights are integers and if none of the
 * intermediate results exceeds 2<sup>52</sup>. Since all intermediate
 * results are sums of edge weights, they are bounded by <em>V C</em>,
 * where <em>V</em> is the number of vertices and <em>C</em> is the maximum
 * weight of any edge.
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/44sp">Section 4.4</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
// 作用：计算出 加权有向图中，从 指定起点 到 其所有可达顶点的最短路径的集合，所构成的树(SPT - shortest path tree)
// 原理：最短路径的 最优性条件
// 思路：对于 “由起始顶点可达的”每一个图结点，为它维护一个 路径权重的属性。
// 特征：有向图中 不能存在有 权重为负数的边（它会导致算法选择错误的边）
// 步骤：#1 初始化 起始结点的属性 && 把它添加到PQ中；
// #2 弹出 PQ的最小结点;
// #3 对于 结点所关联的边，尝试 更新边的terminalVertex的属性，并 把 terminalVertex 添加到PQ中；
// 一句话描述算法：对于 当前图结点 所关联的所有边，按需更新 其terminalVertex的属性(路径权重、路径的最后一条边)，并 更新PQ中 其所对应的entry。
// 当 PQ为空 时，每个图结点 都已经记录下了 到达自己的最短路径的最后一条边。这时 使用回溯的手段 就能够得到 完整的路径
public class DijkstraSP {

    private double[] vertexToLightestPathWeightTowardsIt; // 用于记录 当前顶点 -> 由起始顶点到达当前顶点的最短路径的 距离/路径权重
    private DirectedEdge[] vertexToItsTowardsEdge; // 用于记录 当前顶点 -> 由起始顶点到达当前顶点的最短路径的 最后一条边
    private IndexMinPQ<Double> vertexToItsLightestPathWeightPQ; // 用于记录 当前顶点(index)->由起始顶点到它的最短路径的路径权重(key) 的映射关系

    // 计算出 在 加权有向图G 中，从 起始顶点s 到 其可达的所有其他结点的 最短路径集合，所构成的一个 最短路径树(SPT)
    public DijkstraSP(EdgeWeightedDigraph weightedDigraph, int startVertex) {
        validateEdgeWeightIn(weightedDigraph);

        int graphVertexAmount = weightedDigraph.getVertexAmount();
        instantiateVertexProperties(graphVertexAmount);

        validateVertex(startVertex);

        // #1 初始化 各个结点的“从起始节点到它”的“最短路径”的权重属性
        initPathWeight(startVertex, graphVertexAmount);

        // #2 根据 当前顶点距离起始顶点的远近(到起始顶点的距离) 来 由近到远地 放松结点
        while (!vertexToItsLightestPathWeightPQ.isEmpty()) {
            // ① 获取到 当前“距离起始顶点的路径权重最小的”结点
            // 🐖 这里取出 权重最小的节点 时，到达它的最短路径 也就同时被确定了
            int vertexWithMinPathWeight = vertexToItsLightestPathWeightPQ.delMin();
            // ② 获取到 图中该结点所关联的所有边   作用：为了获取到 其所有的可达顶点
            for (DirectedEdge currentAssociatedGraphEdge : weightedDigraph.associatedEdgesOf(vertexWithMinPathWeight))
                // 对 该关联边 进行放松  作用：尝试更新 其所有可达节点的路径权重
                relax(currentAssociatedGraphEdge);
        }

        // 检查 最优性条件
        assert check(weightedDigraph, startVertex);
    }

    private void initPathWeight(int startVertex, int graphVertexAmount) {
        initArrPathWeight(startVertex, graphVertexAmount);
        initPQEntryFor(startVertex, graphVertexAmount);
    }

    private void initPQEntryFor(int startVertex, int graphVertexAmount) {
        // 容量初始化
        vertexToItsLightestPathWeightPQ = new IndexMinPQ<Double>(graphVertexAmount);
        // 元素初始化
        vertexToItsLightestPathWeightPQ.insert(startVertex, vertexToLightestPathWeightTowardsIt[startVertex]);
    }

    private void instantiateVertexProperties(int graphVertexAmount) {
        vertexToLightestPathWeightTowardsIt = new double[graphVertexAmount];
        vertexToItsTowardsEdge = new DirectedEdge[graphVertexAmount]; // startVertex所对应的 “由起始节点到它的最轻路径的最后一条边”为null
    }

    private void validateEdgeWeightIn(EdgeWeightedDigraph weightedDigraph) {
        for (DirectedEdge currentEdge : weightedDigraph.edges()) {
            if (currentEdge.weight() < 0)
                throw new IllegalArgumentException("edge " + currentEdge + " has negative weight");
        }
    }

    private void initArrPathWeight(int startVertex, int graphVertexAmount) {
        // 初始化 由起始节点到当前节点的最短路径的权重值为 无穷大
        for (int currentVertex = 0; currentVertex < graphVertexAmount; currentVertex++)
            vertexToLightestPathWeightTowardsIt[currentVertex] = Double.POSITIVE_INFINITY;

        // 初始化 由起始节点到起始节点的最短路径的权重值为 0.0
        vertexToLightestPathWeightTowardsIt[startVertex] = 0.0;
    }

    // 放松 指定的边
    // 手段：更新 以 边的terminalVertex 作为endVertex的 路径的相关属性(结点属性)
    // 🐖 这里的relax，可以想象节点是一个重量为pathWeight的球。当球的pathWeight变小时，边自然也就被放松了
    private void relax(DirectedEdge passedEdge) {
        // #1 如果 “由起始顶点s到终止顶点terminalVertex”取用“当前边” 能够得到 更小的路径权重，说明 经由当前边来到达终止顶点 是更优的，则...
        if (makePathWeightLighterVia(passedEdge)) {
            // 更新 terminal节点的各种属性
            updateTerminalsPropertiesBy(passedEdge);
        }
        // 在边被relax之后，有 vertexToItsPathWeight[terminalVertex] = vertexToItsPathWeight[departVertex] + passedEdge.weight()
    }

    private void updateTerminalsPropertiesBy(DirectedEdge passedEdge) {
        // 〇 获取到边的 出发顶点 与 终止顶点
        int departVertex = passedEdge.departVertex(),
            terminalVertex = passedEdge.terminalVertex();

        // ① 更新 terminal顶点的 “路径权重”属性
        vertexToLightestPathWeightTowardsIt[terminalVertex] = vertexToLightestPathWeightTowardsIt[departVertex] + passedEdge.weight();
        // ② 更新 terminal顶点的 “路径的最后一条边”属性
        vertexToItsTowardsEdge[terminalVertex] = passedEdge;
        // ③ 更新 terminal顶点 在PQ中的相关entry
        updatePQEntryFor(terminalVertex);
    }

    private void updatePQEntryFor(int terminalVertex) {
        // 如果存在，则更改
        if (vertexToItsLightestPathWeightPQ.contains(terminalVertex))
            vertexToItsLightestPathWeightPQ.changeKey(terminalVertex, vertexToLightestPathWeightTowardsIt[terminalVertex]);
        else { // 如果不存在，则新增
            vertexToItsLightestPathWeightPQ.insert(terminalVertex, vertexToLightestPathWeightTowardsIt[terminalVertex]);
        }
    }

    // 传入当前边，判断 经由当前边到达terminalVertex 是否能使得 到达其的路径权重更小
    private boolean makePathWeightLighterVia(DirectedEdge passedEdge) {
        int departVertex = passedEdge.departVertex();
        int terminalVertex = passedEdge.terminalVertex();
        return vertexToLightestPathWeightTowardsIt[terminalVertex] > vertexToLightestPathWeightTowardsIt[departVertex] + passedEdge.weight();
    }

    // 返回 从起始顶点s 到指定顶点v的 一条最短路径的权重/长度
    public double minWeightOfPathTo(int passedVertex) {
        validateVertex(passedVertex);
        return vertexToLightestPathWeightTowardsIt[passedVertex];
    }

    // 如果 从起始顶点s 到 指定顶点v之间存在有一个path，则 返回true 否则返回false
    public boolean hasPathFromStartVertexTo(int passedVertex) {
        validateVertex(passedVertex);
        return vertexToLightestPathWeightTowardsIt[passedVertex] < Double.POSITIVE_INFINITY;
    }

    // 返回 从起始顶点s 到指定顶点v的一条 最短路径
    public Iterable<DirectedEdge> edgesOfShortestPathTo(int passedVertex) {
        validateVertex(passedVertex);

        if (!hasPathFromStartVertexTo(passedVertex)) return null;

        Stack<DirectedEdge> edgesStackInPath = new Stack<DirectedEdge>();
        for (DirectedEdge backwardsEdgeInPath = vertexToItsTowardsEdge[passedVertex]; backwardsEdgeInPath != null; backwardsEdgeInPath = vertexToItsTowardsEdge[backwardsEdgeInPath.departVertex()]) {
            edgesStackInPath.push(backwardsEdgeInPath);
        }
        return edgesStackInPath;
    }


    // check optimality conditions:
    // (i) for all edges e:            distTo[e.to()] <= distTo[e.from()] + e.weight()
    // (ii) for all edge e on the SPT: distTo[e.to()] == distTo[e.from()] + e.weight()
    private boolean check(EdgeWeightedDigraph weightedDigraph, int startVertex) {

        // check that edge weights are non-negative
        if (validateEdgeWeight(weightedDigraph)) return false;

        // check that distTo[v] and edgeTo[v] are consistent
        if (hasWrongProperties(startVertex)) return false;

        if (vertexesPropertiesNotConsistent(weightedDigraph, startVertex)) return false;

        // check that all edges e = v->w satisfy distTo[w] <= distTo[v] + e.weight()
        for (int currentVertex = 0; currentVertex < weightedDigraph.getVertexAmount(); currentVertex++) {
            if (isNotRelaxed(weightedDigraph, currentVertex))
                return false;
        }

        // check that all edges e = v->w on SPT（最短路径树） satisfy distTo[w] == distTo[v] + e.weight()
        for (int currentVertex = 0; currentVertex < weightedDigraph.getVertexAmount(); currentVertex++) {
            if (notAccessibleFromStartVertex(currentVertex)) continue;

            DirectedEdge towardsEdgeInPath = vertexToItsTowardsEdge[currentVertex];

            // 按照设计：当前顶点 一定是 路径的终点
            if (isNotFinalVertexOf(currentVertex, towardsEdgeInPath))
                return false;

            // 按照设计：当前边 一定是 紧张的
            if (isNotTight(towardsEdgeInPath, currentVertex)) return false;
        }
        return true;
    }

    private boolean isNotTight(DirectedEdge towardsEdgeInPath, int currentVertex) {
        int departVertex = towardsEdgeInPath.departVertex();
        if (vertexToLightestPathWeightTowardsIt[departVertex] + towardsEdgeInPath.weight() != vertexToLightestPathWeightTowardsIt[currentVertex]) {
            System.err.println("edge " + towardsEdgeInPath + " on shortest path not tight");
            return true;
        }
        return false;
    }

    private boolean isNotFinalVertexOf(int currentVertex, DirectedEdge towardsEdgeInPath) {
        return currentVertex != towardsEdgeInPath.terminalVertex();
    }

    private boolean notAccessibleFromStartVertex(int currentVertex) {
        return vertexToItsTowardsEdge[currentVertex] == null;
    }

    private boolean isNotRelaxed(EdgeWeightedDigraph weightedDigraph, int currentVertex) {
        for (DirectedEdge currentDigraphEdge : weightedDigraph.associatedEdgesOf(currentVertex)) {
            if (isNotRelaxed(currentDigraphEdge, currentVertex))
                return true;
        }
        return false;
    }

    private boolean isNotRelaxed(DirectedEdge currentDigraphEdge, int departVertex) {
        int terminalVertex = currentDigraphEdge.terminalVertex();
        if (vertexToLightestPathWeightTowardsIt[departVertex] + currentDigraphEdge.weight() < vertexToLightestPathWeightTowardsIt[terminalVertex]) {
            System.err.println("edge " + currentDigraphEdge + " not relaxed");
            return true;
        }
        return false;
    }

    private boolean vertexesPropertiesNotConsistent(EdgeWeightedDigraph weightedDigraph, int startVertex) {
        for (int currentDigraphVertex = 0; currentDigraphVertex < weightedDigraph.getVertexAmount(); currentDigraphVertex++) {
            if (currentDigraphVertex == startVertex) continue;
            if (vertexToItsTowardsEdge[currentDigraphVertex] == null && vertexToLightestPathWeightTowardsIt[currentDigraphVertex] != Double.POSITIVE_INFINITY) {
                System.err.println("distTo[] and edgeTo[] inconsistent");
                return true;
            }
        }
        return false;
    }

    private boolean hasWrongProperties(int startVertex) {
        if (vertexToLightestPathWeightTowardsIt[startVertex] != 0.0 || vertexToItsTowardsEdge[startVertex] != null) {
            System.err.println("distTo[s] and edgeTo[s] inconsistent");
            return true;
        }
        return false;
    }

    private boolean validateEdgeWeight(EdgeWeightedDigraph weightedDigraph) {
        for (DirectedEdge currentDigraphEdge : weightedDigraph.edges()) {
            if (currentDigraphEdge.weight() < 0) {
                System.err.println("negative edge weight detected");
                return true;
            }
        }
        return false;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = vertexToLightestPathWeightTowardsIt.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }

    /**
     * Unit tests the {@code DijkstraSP} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedDigraph weightedDigraph = new EdgeWeightedDigraph(in);
        int startVertex = Integer.parseInt(args[1]);

        // compute shortest paths
        DijkstraSP evaluatedGraph = new DijkstraSP(weightedDigraph, startVertex);

        // print shortest path
        for (int currentVertex = 0; currentVertex < weightedDigraph.getVertexAmount(); currentVertex++) {
            if (evaluatedGraph.hasPathFromStartVertexTo(currentVertex)) {
                StdOut.printf("%d to %d (%.2f)  ", startVertex, currentVertex, evaluatedGraph.minWeightOfPathTo(currentVertex));
                for (DirectedEdge currentEdgeInPath : evaluatedGraph.edgesOfShortestPathTo(currentVertex)) {
                    StdOut.print(currentEdgeInPath + "   ");
                }
                StdOut.println();
            } else {
                StdOut.printf("%d to %d         no path\n", startVertex, currentVertex);
            }
        }
    }
}