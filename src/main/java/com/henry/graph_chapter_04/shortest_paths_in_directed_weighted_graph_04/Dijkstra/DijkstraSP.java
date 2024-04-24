package com.henry.graph_chapter_04.shortest_paths_in_directed_weighted_graph_04.Dijkstra;
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
public class DijkstraSP {
    private double[] vertexToItsPathWeight;          // distTo[v] = distance  of shortest s->v path
    private DirectedEdge[] vertexToItsTowardsEdge;    // edgeTo[v] = last edge on shortest s->v path
    private IndexMinPQ<Double> vertexToItsPathWeightPQ;    // priority queue of vertices

    /**
     * Computes a shortest-paths tree from the source vertex {@code s} to every other
     * vertex in the edge-weighted digraph {@code G}.
     *
     * @param weightedDigraph the edge-weighted digraph
     * @param startVertex     the source vertex
     * @throws IllegalArgumentException if an edge weight is negative
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     */
    public DijkstraSP(EdgeWeightedDigraph weightedDigraph, int startVertex) {
        for (DirectedEdge currentEdge : weightedDigraph.edges()) {
            if (currentEdge.weight() < 0)
                throw new IllegalArgumentException("edge " + currentEdge + " has negative weight");
        }

        int graphVertexAmount = weightedDigraph.getVertexAmount();
        vertexToItsPathWeight = new double[graphVertexAmount];
        vertexToItsTowardsEdge = new DirectedEdge[graphVertexAmount];

        validateVertex(startVertex);

        // 初始化 “从起始节点到图的各个节点”的“最短路径”的权重
        for (int currentVertex = 0; currentVertex < graphVertexAmount; currentVertex++)
            vertexToItsPathWeight[currentVertex] = Double.POSITIVE_INFINITY; // 起始结点 到 其他结点的路径权重为 无穷大
        vertexToItsPathWeight[startVertex] = 0.0; // 起始结点 到 起始结点的路径权重为0

        // relax vertices in order of distance from s
        // 根据 当前节点到起始结点的距离，由近到远地 放松结点
        vertexToItsPathWeightPQ = new IndexMinPQ<Double>(graphVertexAmount);
        vertexToItsPathWeightPQ.insert(startVertex, vertexToItsPathWeight[startVertex]);

        while (!vertexToItsPathWeightPQ.isEmpty()) {
            // 获取到 “当前距离起始节点的路径权重最小的结点”
            int vertexWithMinPathWeight = vertexToItsPathWeightPQ.delMin();
            // 获取到 图中该结点所有相关联的边
            for (DirectedEdge currentAssociatedEdge : weightedDigraph.associatedEdgesOf(vertexWithMinPathWeight))
                // 对边进行放松...
                relax(currentAssociatedEdge);
        }

        // check optimality conditions
        assert check(weightedDigraph, startVertex);
    }

    // relax edge e and update pq if changed
    // 放松指定的边，并更新 底层的三个数据
    private void relax(DirectedEdge passedEdge) {
        int departVertex = passedEdge.departVertex(),
            terminalVertex = passedEdge.terminalVertex();

        if (vertexToItsPathWeight[terminalVertex] > vertexToItsPathWeight[departVertex] + passedEdge.weight()) {
            // 更新 “从起始结点到当前结点的最短路径”的权重
            vertexToItsPathWeight[terminalVertex] = vertexToItsPathWeight[departVertex] + passedEdge.weight();
            // 更新 “从起始结点到当前结点的最短路径”的最后一条边
            vertexToItsTowardsEdge[terminalVertex] = passedEdge;

            // 更新PQ中，该结点所关联的“最短路径的权重”
            if (vertexToItsPathWeightPQ.contains(terminalVertex))
                vertexToItsPathWeightPQ.decreaseKey(terminalVertex, vertexToItsPathWeight[terminalVertex]);
            else vertexToItsPathWeightPQ.insert(terminalVertex, vertexToItsPathWeight[terminalVertex]);
        }
    }

    /**
     * Returns the length of a shortest path from the source vertex {@code s} to vertex {@code v}.
     *
     * @param passedVertex the destination vertex
     * @return the length of a shortest path from the source vertex {@code s} to vertex {@code v};
     * {@code Double.POSITIVE_INFINITY} if no such path
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public double minWeightOfPathTo(int passedVertex) {
        validateVertex(passedVertex);
        return vertexToItsPathWeight[passedVertex];
    }

    /**
     * Returns true if there is a path from the source vertex {@code s} to vertex {@code v}.
     *
     * @param passedVertex the destination vertex
     * @return {@code true} if there is a path from the source vertex
     * {@code s} to vertex {@code v}; {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public boolean hasPathFromStartVertexTo(int passedVertex) {
        validateVertex(passedVertex);
        return vertexToItsPathWeight[passedVertex] < Double.POSITIVE_INFINITY;
    }

    /**
     * Returns a shortest path from the source vertex {@code s} to vertex {@code v}.
     *
     * @param passedVertex the destination vertex
     * @return a shortest path from the source vertex {@code s} to vertex {@code v}
     * as an iterable of edges, and {@code null} if no such path
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<DirectedEdge> edgesOfPathTo(int passedVertex) {
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
    private boolean check(EdgeWeightedDigraph G, int s) {

        // check that edge weights are non-negative
        for (DirectedEdge e : G.edges()) {
            if (e.weight() < 0) {
                System.err.println("negative edge weight detected");
                return false;
            }
        }

        // check that distTo[v] and edgeTo[v] are consistent
        if (vertexToItsPathWeight[s] != 0.0 || vertexToItsTowardsEdge[s] != null) {
            System.err.println("distTo[s] and edgeTo[s] inconsistent");
            return false;
        }
        for (int v = 0; v < G.getVertexAmount(); v++) {
            if (v == s) continue;
            if (vertexToItsTowardsEdge[v] == null && vertexToItsPathWeight[v] != Double.POSITIVE_INFINITY) {
                System.err.println("distTo[] and edgeTo[] inconsistent");
                return false;
            }
        }

        // check that all edges e = v->w satisfy distTo[w] <= distTo[v] + e.weight()
        for (int v = 0; v < G.getVertexAmount(); v++) {
            for (DirectedEdge e : G.associatedEdgesOf(v)) {
                int w = e.terminalVertex();
                if (vertexToItsPathWeight[v] + e.weight() < vertexToItsPathWeight[w]) {
                    System.err.println("edge " + e + " not relaxed");
                    return false;
                }
            }
        }

        // check that all edges e = v->w on SPT satisfy distTo[w] == distTo[v] + e.weight()
        for (int w = 0; w < G.getVertexAmount(); w++) {
            if (vertexToItsTowardsEdge[w] == null) continue;
            DirectedEdge e = vertexToItsTowardsEdge[w];
            int v = e.departVertex();
            if (w != e.terminalVertex()) return false;
            if (vertexToItsPathWeight[v] + e.weight() != vertexToItsPathWeight[w]) {
                System.err.println("edge " + e + " on shortest path not tight");
                return false;
            }
        }
        return true;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = vertexToItsPathWeight.length;
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
                for (DirectedEdge currentEdgeInPath : evaluatedGraph.edgesOfPathTo(currentVertex)) {
                    StdOut.print(currentEdgeInPath + "   ");
                }
                StdOut.println();
            } else {
                StdOut.printf("%d to %d         no path\n", startVertex, currentVertex);
            }
        }
    }
}