package com.henry.graph_chapter_04.direction_graph_02.search_accessible_vertexes.via_dfs.applications.if_cycle_exist_in_digraph_03.extend.sort_vertexes_in_topo;

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

import com.henry.graph_chapter_04.direction_graph_02.represent_a_symbol_graph.SymbolDigraph;
import com.henry.graph_chapter_04.direction_graph_02.represent_digraph.Digraph;
import com.henry.graph_chapter_04.direction_graph_02.search_accessible_vertexes.via_dfs.applications.construct_vertex_traverse_seq_in_different_order_04.execution.DigraphDiffTraverseOrderViaDFS;
import com.henry.graph_chapter_04.direction_graph_02.search_accessible_vertexes.via_dfs.applications.if_cycle_exist_in_digraph_03.execution.CycleExistInDiGraph;
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
// 结论：有向图的 拓扑排序结果(如果存在的话)，就是 图的 逆后序遍历的结点顺序
// 拓扑排序的应用：优先级限制下的 调度问题（比如 在多个课程中，存在“先导课程”的限制。而学生一次 只能修一门课）
// 证明：见 introduce
// 🐖 记录 “结点在序列中的位置” 是一个好主意 - 这样能够 从另一个方面 提供图的信息 给用例使用
public class TopoSortDigraph {
    private Iterable<Integer> vertexesInTopoOrder;  // 图中所有节点的拓扑排序结果序列
    private int[] vertexToItsSpotInTopoSequence;    // 节点 -> 节点在拓扑排序结果序列中的位置 的映射关系

    /**
     * 确定 指定的有向图 是否 存在有 拓扑排序的结果，如果有，找到 一个拓扑排序的结果序列
     *
     * @param digraph 指定的有向图
     */
    public TopoSortDigraph(Digraph digraph) {
        // 先判断 有向图中 是否存在有环 - 如果有环的话，则：其 不存在 拓扑排序结果
        System.out.println("~~~ 先判断该有向图中 是否存在有环 - 只有无环图，才存在有 拓扑排序结果 ~~~");
        CycleExistInDiGraph cycleFoundDigraph = new CycleExistInDiGraph(digraph);

        // 如果 有向图中没有 环，说明 其存在有 拓扑排序，则：
        if (!cycleFoundDigraph.findACycle()) {
            System.out.println("!!! 当前有向图中 不存在有 环，因此 存在有 拓扑排序 !!!");
            // 获取到 有向图 经多种遍历方式 所得到的 顶点序列结果
            DigraphDiffTraverseOrderViaDFS vertexesTraversedDigraph = new DigraphDiffTraverseOrderViaDFS(digraph);
            // 而 图中结点的拓扑排序结果 <=> 图中所有结点的 逆后序遍历的结果
            vertexesInTopoOrder = vertexesTraversedDigraph.vertexesInReversePostOrder();
            System.out.println("@@@ 获取到了 该有向图的拓扑排序结果序列（顶点的逆后序结果序列） @@@");

            // 获取到 vertex 在 拓扑排序结果序列 中的位置/排名
            vertexToItsSpotInTopoSequence = new int[digraph.getVertexAmount()];
            int spotInTopoSequence = 0;
            for (int currentVertex : vertexesInTopoOrder)
                vertexToItsSpotInTopoSequence[currentVertex] = spotInTopoSequence++;
        }
    }

    /**
     * 确定 指定的 加权有向图 是否 有一个拓扑排序，如果有，找到具体的拓扑排序结果。
     * @param G 指定的加权有向图
     */
//    public Topological(EdgeWeightedDigraph G) {
//        EdgeWeightedDirectedCycle finder = new EdgeWeightedDirectedCycle(G);
//        if (!finder.hasCycle()) {
//            DepthFirstOrder dfs = new DepthFirstOrder(G);
//            vertexesInTopoOrder = dfs.reversePost();
//        }
//    }

    /**
     * 如果 指定的有向图 存在有 拓扑排序，则 返回 其拓扑排序结果序列。否则 返回null
     * 如果有向图 存在有 拓扑顺序（或者等效地说 有向图是一个 有向无环图），则 以可迭代形式 来 返回节点的拓扑排序结果
     * 否则的话 返回null
     */
    public Iterable<Integer> getVertexesInTopoOrder() {
        return vertexesInTopoOrder;
    }

    /**
     * 指定的有向图 是否存在有 拓扑排序？
     * 如果 有拓扑排序（或者 等效地讲，有向图是一个 有向无环图），则 返回true。否则 返回false
     */
    public boolean hasTopoOrder() {
        return vertexesInTopoOrder != null;
    }

    /**
     * 指定的有向图 是否存在有 拓扑排序？
     * 如果 有拓扑排序（或者 等效地讲，有向图是一个 有向无环图），则 返回true。否则 返回false
     *
     * @deprecated 被 hasTopoOrder()方法 所取代.
     */
    @Deprecated
    public boolean isDAG() {
        return hasTopoOrder();
    }

    /**
     * 获取到 指定的顶点v 在所有顶点的拓扑排序结果中的 排名/位置。
     * 如果 有向图 不是一个 有向无环图的话，则 返回-1
     *
     * @param vertex 指定的节点
     *               该指定的顶点v 在 有向图的拓扑排序结果序列 中的位置；如果 有向图 不是一个 有向无环图的话，返回-1
     * @throws IllegalArgumentException 如果 顶点v 不在有效范围 [0, V]内
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
     * 当前数据类型的 单元测试
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        String filename = args[0]; // 文件名
        String delimiter = args[1]; // 分隔符
        // 构造出 有向符号图
        SymbolDigraph symbolDigraph = new SymbolDigraph(filename, delimiter);

        // 调用当前类的构造器 来 求出 该有向符号图的 拓扑排序结果序列
        TopoSortDigraph topoOrderedGraph = new TopoSortDigraph(symbolDigraph.underlyingDigraph());

        // 获取到 拓扑排序结果序列，并 打印 序列中的每个结点
        for (int currentVertex : topoOrderedGraph.getVertexesInTopoOrder()) {
            StdOut.println(symbolDigraph.nameOfVertexWith(currentVertex));
        }
    }

}
