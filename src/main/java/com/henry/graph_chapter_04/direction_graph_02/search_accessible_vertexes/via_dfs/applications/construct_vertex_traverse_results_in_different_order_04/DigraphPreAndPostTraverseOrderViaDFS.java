package com.henry.graph_chapter_04.direction_graph_02.search_accessible_vertexes.via_dfs.applications.construct_vertex_traverse_results_in_different_order_04;

/******************************************************************************
 *  Compilation:  javac DepthFirstOrder.java
 *  Execution:    java DepthFirstOrder digraph.txt
 *  Dependencies: Digraph.java Queue.java Stack.java StdOut.java
 *                EdgeWeightedDigraph.java DirectedEdge.java
 *  Data files:   https://algs4.cs.princeton.edu/42digraph/tinyDAG.txt
 *                https://algs4.cs.princeton.edu/42digraph/tinyDG.txt
 *
 *  Compute preorder and postorder for a digraph or edge-weighted digraph.
 *  Runs in O(E + V) time.
 *
 *  % java DepthFirstOrder tinyDAG.txt
 *     v  pre post
 *  --------------
 *     0    0    8
 *     1    3    2
 *     2    9   10
 *     3   10    9
 *     4    2    0
 *     5    1    1
 *     6    4    7
 *     7   11   11
 *     8   12   12
 *     9    5    6
 *    10    8    5
 *    11    6    4
 *    12    7    3
 *  Preorder:  0 5 4 1 6 9 11 12 10 2 3 7 8
 *  Postorder: 4 5 1 12 11 10 9 6 0 3 2 7 8
 *  Reverse postorder: 8 7 2 3 0 6 9 10 11 12 1 5 4
 *
 ******************************************************************************/

import com.henry.graph_chapter_04.direction_graph_02.represent_digraph.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * The {@code DepthFirstOrder} class represents a data type for
 * determining depth-first search ordering of the vertices in a digraph
 * or edge-weighted digraph, including preorder, postorder, and reverse postorder.
 * <p>
 * This implementation uses depth-first search.
 * Each constructor takes &Theta;(<em>V</em> + <em>E</em>) time,
 * where <em>V</em> is the number of vertices and <em>E</em> is the
 * number of edges.
 * Each instance method takes &Theta;(1) time.
 * It uses &Theta;(<em>V</em>) extra space (not including the digraph).
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
// 结论：对于 有向图 这种非线性的数据结构，在 DFS算法 中，能够 以各种顺序 来 收集图中的结点（前序、后序、逆后序）
// 原理#1：图的前序遍历中 结点的顺序 即为 DFS中访问结点的顺序，后序遍历中 结点的顺序 即为 DFS中 节点处理完成(DFS调用完成)的顺序，逆后序 则是后序的相反顺序
// 原理#2：使用DFS 对 图中的所有结点 进行标记时，DFS能够保证 每个结点 都只会被访问一次 & 处理完成一次
// 手段：使用一个名叫 vertexesInPreOrder的队列 来 收集前序遍历序列中的当前结点...
// 🐖 记录“结点在序列中的位置” 会是一个好主意 - 这样能够 从另一个方面 提供图的信息 给用例使用
public class DigraphPreAndPostTraverseOrderViaDFS {
    private boolean[] vertexToIsMarked;          // 节点 -> 是否已经被DFS算法标记?
    private Queue<Integer> vertexesInPreOrder;   // 对图进行前序遍历 所得到的节点集合
    private Queue<Integer> vertexesInPostOrder;  // 对图进行后序遍历 所得到的节点集合

    private int[] vertexToItsSpotInPreSequence;                 // 节点 -> 其在前序遍历结果集合中的位置
    private int[] vertexToItsSpotInPostSequence;                // 节点 -> 其在后序遍历结果集合中的位置
    private int cursorOfPreSequence;            // 前序序列 所使用的游标指针
    private int cursorOfPostSequence;           // 后序序列 所使用的游标指针

    /**
     * Determines a depth-first order for the digraph {@code G}.
     *
     * @param digraph the digraph
     */
    public DigraphPreAndPostTraverseOrderViaDFS(Digraph digraph) {
        // 顶点在前序序列中的位置
        vertexToItsSpotInPreSequence = new int[digraph.getVertexAmount()];
        // 顶点在后序序列中的位置
        vertexToItsSpotInPostSequence = new int[digraph.getVertexAmount()];

        vertexesInPostOrder = new Queue<Integer>();
        vertexesInPreOrder = new Queue<Integer>();

        vertexToIsMarked = new boolean[digraph.getVertexAmount()];

        // 顺序遍历所有顶点...
        for (int currentVertex = 0; currentVertex < digraph.getVertexAmount(); currentVertex++)
            // 如果当前节点 没有被标记，说明 其所属的子图还没有被处理过，则：
            if (isNotMarked(currentVertex))
                // 以当前节点作为起始节点，开始在图中执行DFS
                markVertexesAndPickToSequenceViaDFS(digraph, currentVertex);

        assert check();
    }

    private boolean isNotMarked(int currentVertex) {
        return !vertexToIsMarked[currentVertex];
    }

    /**
     * Determines a depth-first order for the edge-weighted digraph {@code G}.
     *
     * @param digraph       the edge-weighted digraph
     * @param currentVertex
     */
//    public DepthFirstOrder(EdgeWeightedDigraph G) {
//        vertexToxxx = new int[G.V()];
//        vertexToooo = new int[G.V()];
//        vertexesInPostOrder = new Queue<Integer>();
//        vertexesInPreOrder = new Queue<Integer>();
//        vertexToIsMarked = new boolean[G.V()];
//        for (int v = 0; v < G.V(); v++)
//            if (!vertexToIsMarked[v]) dfs(G, v);
//    }

    // 在有向图G中，以指定节点作为起始节点 执行DFS，并 计算出其 前序节点序列 以及 后序节点序列
    private void markVertexesAndPickToSequenceViaDFS(Digraph digraph, int currentVertex) {
        // 标记结点
        vertexToIsMarked[currentVertex] = true;

        // 得到前序遍历的序列 - 手段：在递归调用之前/当前调用中，把当前结点添加到队列中
        constructPreOrderSequence(currentVertex);

        for (int currentAdjacentVertex : digraph.adjacentVertexesOf(currentVertex)) {
            if (isNotMarked(currentAdjacentVertex)) {
                markVertexesAndPickToSequenceViaDFS(digraph, currentAdjacentVertex);
            }
        }

        // 得到后序遍历的序列 - 手段：在递归调用完成之后，把当前结点添加到队列中
        constructPostOrderSequence(currentVertex);
    }

    private void constructPostOrderSequence(int currentVertex) {
        vertexesInPostOrder.enqueue(currentVertex);
        vertexToItsSpotInPostSequence[currentVertex] = cursorOfPostSequence++;
    }

    private void constructPreOrderSequence(int currentVertex) {
        vertexToItsSpotInPreSequence[currentVertex] = cursorOfPreSequence++;
        vertexesInPreOrder.enqueue(currentVertex);
    }

    // run DFS in edge-weighted digraph G from vertex v and compute preorder/postorder
//    private void dfs(EdgeWeightedDigraph G, int v) {
//        vertexToIsMarked[v] = true;
//        vertexToxxx[v] = preCounter++;
//        vertexesInPreOrder.enqueue(v);
//        for (DirectedEdge e : G.adj(v)) {
//            int w = e.to();
//            if (!vertexToIsMarked[w]) {
//                dfs(G, w);
//            }
//        }
//        vertexesInPostOrder.enqueue(v);
//        vertexToooo[v] = postCounter++;
//    }

    // key API*1: 获取到 指定顶点在前序遍历结果序列中的位置
    public int spotInPreOrderSequence(int currentVertex) {
        validateVertex(currentVertex);
        return vertexToItsSpotInPreSequence[currentVertex];
    }

    // key API*2: 获取到 指定顶点在后序遍历结果序列中的位置
    public int spotInPostOrderSequence(int currentVertex) {
        validateVertex(currentVertex);
        return vertexToItsSpotInPostSequence[currentVertex];
    }

    // key API*3: 以可迭代的方式获取到 有向图结点的后序遍历结果序列
    public Iterable<Integer> vertexesInPostOrder() {
        return vertexesInPostOrder;
    }

    // key API*4: 以可迭代的方式获取到 有向图结点的前序遍历结果序列
    public Iterable<Integer> vertexesInPreOrder() {
        return vertexesInPreOrder;
    }

    // key API*5: 以可迭代的方式获取到 有向图结点的逆后序遍历结果序列
    public Iterable<Integer> vertexesInReversePostOrder() {
        Stack<Integer> reversedVertexes = new Stack<Integer>();

        for (int currentVertex : vertexesInPostOrder)
            reversedVertexes.push(currentVertex);

        return reversedVertexes;
    }


    // check that pre() and post() are consistent with pre(v) and post(v)
    private boolean check() {

        // check that post(v) is consistent with post()
        int currentSpot = 0;
        for (int currentVertex : vertexesInPostOrder()) {
            if (spotInPostOrderSequence(currentVertex) != currentSpot) {
                StdOut.println("post(currentVertex) and post() inconsistent");
                return false;
            }
            currentSpot++;
        }

        // check that pre(v) is consistent with pre()
        currentSpot = 0;
        for (int currentVertex : vertexesInPreOrder()) {
            if (spotInPreOrderSequence(currentVertex) != currentSpot) {
                StdOut.println("pre(currentVertex) and pre() inconsistent");
                return false;
            }
            currentSpot++;
        }

        return true;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int vertex) {
        int vertexAmount = vertexToIsMarked.length;
        if (vertex < 0 || vertex >= vertexAmount)
            throw new IllegalArgumentException("vertex " + vertex + " is not between 0 and " + (vertexAmount - 1));
    }

    /**
     * Unit tests the {@code DepthFirstOrder} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph digraph = new Digraph(in);

        DigraphPreAndPostTraverseOrderViaDFS markedDigraph = new DigraphPreAndPostTraverseOrderViaDFS(digraph);
        StdOut.println("currentVertex  spotInPreOrderSequence spotInPostOrderSequence");
        StdOut.println("----------------------------------------------------------------------");
        for (int currentVertex = 0; currentVertex < digraph.getVertexAmount(); currentVertex++) {
            StdOut.printf("%8d %16d %25d\n", currentVertex, markedDigraph.spotInPreOrderSequence(currentVertex), markedDigraph.spotInPostOrderSequence(currentVertex));
        }

        StdOut.print("Preorder:  ");
        for (int currentVertex : markedDigraph.vertexesInPreOrder()) {
            StdOut.print(currentVertex + " ");
        }
        StdOut.println();

        StdOut.print("Postorder: ");
        for (int currentVertex : markedDigraph.vertexesInPostOrder()) {
            StdOut.print(currentVertex + " ");
        }
        StdOut.println();

        StdOut.print("Reverse postorder: ");
        for (int currentVertex : markedDigraph.vertexesInReversePostOrder()) {
            StdOut.print(currentVertex + " ");
        }
        StdOut.println();

    }

}
