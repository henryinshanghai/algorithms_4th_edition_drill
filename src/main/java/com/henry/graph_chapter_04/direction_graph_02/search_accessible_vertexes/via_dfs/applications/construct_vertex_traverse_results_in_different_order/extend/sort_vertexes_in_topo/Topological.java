package com.henry.graph_chapter_04.direction_graph_02.search_accessible_vertexes.via_dfs.applications.construct_vertex_traverse_results_in_different_order.extend.sort_vertexes_in_topo;

/******************************************************************************
 *  Compilation:  javac Topological.java
 *  Execution:    java  Topological filename.txt delimiter
 *  Dependencies: Digraph.java DepthFirstOrder.java DirectedCycle.java
 *                EdgeWeightedDigraph.java EdgeWeightedDirectedCycle.java
 *                SymbolDigraph.java
 *  Data files:   https://algs4.cs.princeton.edu/42digraph/jobs.txt
 *
 *  Compute topological ordering of a DAG or edge-weighted DAG.
 *  Runs in O(E + V) time.
 *
 *  % java Topological jobs.txt "/"
 *  Calculus
 *  Linear Algebra
 *  Introduction to CS
 *  Advanced Programming
 *  Algorithms
 *  Theoretical CS
 *  Artificial Intelligence
 *  Robotics
 *  Machine Learning
 *  Neural Networks
 *  Databases
 *  Scientific Computing
 *  Computational Biology
 *
 ******************************************************************************/

import com.henry.graph_chapter_04.direction_graph_02.represent_digraph.Digraph;
import com.henry.graph_chapter_04.direction_graph_02.search_accessible_vertexes.via_dfs.applications.if_cycle_exist_in_digraph.CycleExistInDiGraph;
import com.henry.graph_chapter_04.direction_graph_02.search_accessible_vertexes.via_dfs.applications.construct_vertex_traverse_results_in_different_order.DepthFirstOrder;
import com.henry.graph_chapter_04.direction_graph_02.represent_a_symbol_graph.SymbolDigraph;
import edu.princeton.cs.algs4.StdOut;

/**
 * The {@code Topological} class represents a data type for
 * determining a topological order of a <em>directed acyclic graph</em> (DAG).
 * A digraph has a topological order if and only if it is a DAG.
 * The <em>hasOrder</em> operation determines whether the digraph has
 * a topological order, and if so, the <em>order</em> operation
 * returns one.
 * <p>
 * This implementation uses depth-first search.
 * The constructor takes &Theta;(<em>V</em> + <em>E</em>) time in the
 * worst case, where <em>V</em> is the number of vertices and <em>E</em>
 * is the number of edges.
 * Each instance method takes &Theta;(1) time.
 * It uses &Theta;(<em>V</em>) extra space (not including the digraph).
 * <p>
 * See {@link CycleExistInDiGraph}, {@link DirectedCycleX}, and
 * {@link EdgeWeightedDirectedCycle} for computing a directed cycle
 * if the digraph is not a DAG.
 * See {@link TopologicalX} for a nonrecursive queue-based algorithm
 * for computing a topological order of a DAG.
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
// 结论：有向图的拓扑排序结果(如果存在的话)，就是 图的逆后序遍历的结点顺序
// 拓扑排序的应用：优先级限制下的调度问题（比如 在多个课程中，存在先导课程的限制。而学生一次只能修一门课）
// 证明：见 introduce
// 🐖 记录“结点在序列中的位置”是一个好主意 - 这样能够从另一个方面提供图的信息给用例使用
public class Topological {
    private Iterable<Integer> vertexesInTopoOrder;  // topological order
    private int[] vertexToItsSpotInTopoSequence;               // rank[v] = rank of vertex v in order

    /**
     * Determines whether the digraph {@code G} has a topological order and, if so,
     * finds such a topological order.
     *
     * @param digraph the digraph
     */
    public Topological(Digraph digraph) {
        CycleExistInDiGraph cycleFoundDigraph = new CycleExistInDiGraph(digraph);
        if (!cycleFoundDigraph.findACycle()) {
            DepthFirstOrder vertexesTraversedDigraph = new DepthFirstOrder(digraph);
            // 图中结点的拓扑排序结果 就是 图中结点的逆后序遍历的结果
            vertexesInTopoOrder = vertexesTraversedDigraph.vertexesInReversePostOrder();

            // 初始化 vertex在拓扑排序结果中的位置/排名
            vertexToItsSpotInTopoSequence = new int[digraph.getVertexAmount()];
            int spotInTopoSequence = 0;
            for (int currentVertex : vertexesInTopoOrder)
                vertexToItsSpotInTopoSequence[currentVertex] = spotInTopoSequence++;
        }
    }

    /**
     * Determines whether the edge-weighted digraph {@code G} has a topological
     * order and, if so, finds such an order.
     * @param G the edge-weighted digraph
     */
//    public Topological(EdgeWeightedDigraph G) {
//        EdgeWeightedDirectedCycle finder = new EdgeWeightedDirectedCycle(G);
//        if (!finder.hasCycle()) {
//            DepthFirstOrder dfs = new DepthFirstOrder(G);
//            vertexesInTopoOrder = dfs.reversePost();
//        }
//    }

    /**
     * Returns a topological order if the digraph has a topological order,
     * and {@code null} otherwise.
     *
     * @return a topological order of the vertices (as an iterable) if the
     * digraph has a topological order (or equivalently, if the digraph is a DAG),
     * and {@code null} otherwise
     */
    public Iterable<Integer> getVertexesInTopoOrder() {
        return vertexesInTopoOrder;
    }

    /**
     * Does the digraph have a topological order?
     *
     * @return {@code true} if the digraph has a topological order (or equivalently,
     * if the digraph is a DAG), and {@code false} otherwise
     */
    public boolean hasTopoOrder() {
        return vertexesInTopoOrder != null;
    }

    /**
     * Does the digraph have a topological order?
     *
     * @return {@code true} if the digraph has a topological order (or equivalently,
     * if the digraph is a DAG), and {@code false} otherwise
     * @deprecated Replaced by {@link #hasTopoOrder()}.
     */
    @Deprecated
    public boolean isDAG() {
        return hasTopoOrder();
    }

    /**
     * The rank of vertex {@code v} in the topological order;
     * -1 if the digraph is not a DAG
     *
     * @param vertex the vertex
     * @return the position of vertex {@code v} in a topological order
     * of the digraph; -1 if the digraph is not a DAG
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int rankingInTopoSequence(int vertex) {
        validateVertex(vertex);
        if (hasTopoOrder()) return vertexToItsSpotInTopoSequence[vertex];
        else return -1;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int passedVertex) {
        int vertexAmount = vertexToItsSpotInTopoSequence.length;
        if (passedVertex < 0 || passedVertex >= vertexAmount)
            throw new IllegalArgumentException("vertex " + passedVertex + " is not between 0 and " + (vertexAmount - 1));
    }

    /**
     * Unit tests the {@code Topological} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        String filename = args[0];
        String delimiter = args[1];
        // 构造出符号图
        SymbolDigraph symbolDigraph = new SymbolDigraph(filename, delimiter);

        // 获取到 符号图底层的有向图的 拓扑排序结果
        Topological topoOrderedGraph = new Topological(symbolDigraph.underlyingDigraph());

        // 打印拓扑排序结果（结点序列）中的每个结点
        for (int currentVertex : topoOrderedGraph.getVertexesInTopoOrder()) {
            StdOut.println(symbolDigraph.nameOfVertexWith(currentVertex));
        }
    }

}
