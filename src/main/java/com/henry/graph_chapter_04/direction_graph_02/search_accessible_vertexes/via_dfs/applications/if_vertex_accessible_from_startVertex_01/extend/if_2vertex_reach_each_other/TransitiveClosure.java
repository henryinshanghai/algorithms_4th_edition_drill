package com.henry.graph_chapter_04.direction_graph_02.search_accessible_vertexes.via_dfs.applications.if_vertex_accessible_from_startVertex_01.extend.if_2vertex_reach_each_other;

/******************************************************************************
 *  Compilation:  javac TransitiveClosure.java
 *  Execution:    java TransitiveClosure filename.txt
 *  Dependencies: Digraph.java DepthFirstDirectedPaths.java In.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/42digraph/tinyDG.txt
 *
 *  Compute transitive closure of a digraph and support
 *  reachability queries.
 *
 *  Preprocessing time: O(V(E + V)) time.
 *  Query time: O(1).
 *  Space: O(V^2).
 *
 *  % java TransitiveClosure tinyDG.txt
 *         0  1  2  3  4  5  6  7  8  9 10 11 12
 *  --------------------------------------------
 *    0:   T  T  T  T  T  T
 *    1:      T
 *    2:   T  T  T  T  T  T
 *    3:   T  T  T  T  T  T
 *    4:   T  T  T  T  T  T
 *    5:   T  T  T  T  T  T
 *    6:   T  T  T  T  T  T  T        T  T  T  T
 *    7:   T  T  T  T  T  T  T  T  T  T  T  T  T
 *    8:   T  T  T  T  T  T  T  T  T  T  T  T  T
 *    9:   T  T  T  T  T  T           T  T  T  T
 *   10:   T  T  T  T  T  T           T  T  T  T
 *   11:   T  T  T  T  T  T           T  T  T  T
 *   12:   T  T  T  T  T  T           T  T  T  T
 *
 ******************************************************************************/

import com.henry.graph_chapter_04.direction_graph_02.represent_digraph.Digraph;
import com.henry.graph_chapter_04.direction_graph_02.search_accessible_vertexes.via_dfs.applications.if_vertex_accessible_from_startVertex_01.AccessibleVertexesInDigraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * The {@code TransitiveClosure} class represents a data type for
 * computing the transitive closure of a digraph.
 * <p>
 * This implementation runs depth-first search from each vertex.
 * The constructor takes &Theta;(<em>V</em>(<em>V</em> + <em>E</em>))
 * in the worst case, where <em>V</em> is the number of vertices and
 * <em>E</em> is the number of edges.
 * Each instance method takes &Theta;(1) time.
 * It uses &Theta;(<em>V</em><sup>2</sup>) extra space (not including the digraph).
 * <p>
 * For large digraphs, you may want to consider a more sophisticated algorithm.
 * <a href = "http://www.cs.hut.fi/~enu/thesis.html">Nuutila</a> proposes two
 * algorithm for the problem (based on strong components and an interval representation)
 * that runs in &Theta;(<em>E</em> + <em>V</em>) time on typical digraphs.
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
// 作用：用于计算 一个有向图的 transitive closure；
// 有向图的传递闭包的定义? AI解释: 传递闭包 就是在 原图的基础上，把所有 “能到达”的关系 都显式地 用边表示出来！
public class TransitiveClosure {
    // 顶点 -> 由此顶点可达的所有顶点 所构成的集合??
    private AccessibleVertexesInDigraph[] vertexToItsAccessibleVertexesInDigraph;  // tc[v] = reachable from v

    /**
     * Computes the transitive closure of the digraph {@code G}.
     * 计算一个有向图的 传递闭包??
     * @param digraph the digraph
     */
    public TransitiveClosure(Digraph digraph) {
        // 容量初始化
        vertexToItsAccessibleVertexesInDigraph = new AccessibleVertexesInDigraph[digraph.getVertexAmount()];

        // 元素初始化
        for (int currentVertex = 0; currentVertex < digraph.getVertexAmount(); currentVertex++)
            // 以 当前顶点 作为 起始顶点，得到 它在图中的 所有可达顶点
            vertexToItsAccessibleVertexesInDigraph[currentVertex] = new AccessibleVertexesInDigraph(digraph, currentVertex);
    }

    /**
     * Is there a directed path from vertex {@code v} to vertex {@code w} in the digraph?
     * 在有向图中，存在有 从顶点v 到顶点w 的有向路径吗?
     * @param vertexV the source vertex 起始顶点
     * @param vertexW the target vertex 目标顶点
     * @return {@code true} if there is a directed path from {@code v} to {@code w},
     * {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     * @throws IllegalArgumentException unless {@code 0 <= w < V}
     */
    public boolean reachable(int vertexV, int vertexW) {
        validateVertex(vertexV);
        validateVertex(vertexW);
        // 先获取到 顶点 所映射到的 AVID对象（🐖 对象中包含有 由此顶点可达的所有顶点 信息）
        AccessibleVertexesInDigraph accessibleVertexesFromVertexV = vertexToItsAccessibleVertexesInDigraph[vertexV];
        // 判断 顶点W 是否 由顶点V可达；    手段：AVID对象暴露的API，底层是检查 marked[]数组元素
        return accessibleVertexesFromVertexV.isAccessibleFromStartVertex(vertexW);
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    // 验证 顶点的有效性
    private void validateVertex(int vertexV) {
        int accessibleVertexAmount = vertexToItsAccessibleVertexesInDigraph.length;
        if (vertexV < 0 || vertexV >= accessibleVertexAmount)
            throw new IllegalArgumentException("vertex " + vertexV + " is not between 0 and " + (accessibleVertexAmount - 1));
    }

    /**
     * Unit tests the {@code TransitiveClosure} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In digraphFile = new In(args[0]);
        Digraph digraph = new Digraph(digraphFile);

        TransitiveClosure digraphsTC = new TransitiveClosure(digraph);

        // print header
        StdOut.print("     ");
        for (int currentVertex = 0; currentVertex < digraph.getVertexAmount(); currentVertex++)
            StdOut.printf("%3d", currentVertex);
        StdOut.println();
        StdOut.println("--------------------------------------------");

        // print transitive closure
        for (int vertexV = 0; vertexV < digraph.getVertexAmount(); vertexV++) {
            StdOut.printf("%3d: ", vertexV);
            for (int vertexW = 0; vertexW < digraph.getVertexAmount(); vertexW++) {
                if (digraphsTC.reachable(vertexV, vertexW)) StdOut.printf("  T");
                else StdOut.printf("   ");
            }
            StdOut.println();
        }
    }

}