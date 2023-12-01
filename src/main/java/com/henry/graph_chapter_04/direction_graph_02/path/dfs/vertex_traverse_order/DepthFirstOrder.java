package com.henry.graph_chapter_04.direction_graph_02.path.dfs.vertex_traverse_order; /******************************************************************************
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

import com.henry.graph_chapter_04.direction_graph_02.graph.Digraph;
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
public class DepthFirstOrder {
    private boolean[] vertexToIsMarked;          // marked[v] = has v been marked in dfs?
    private Queue<Integer> vertexesInPreOrder;   // vertices in preorder
    private Queue<Integer> vertexesInPostOrder;  // vertices in postorder

    private int[] vertexToItsSpotInPreSequence;                 // pre[v]    = preorder  number of v
    private int[] vertexToItsSpotInPostSequence;                // post[v]   = postorder number of v
    private int cursorOfPreSequence;            // counter or preorder numbering
    private int cursorOfPostSequence;           // counter for postorder numbering

    /**
     * Determines a depth-first order for the digraph {@code G}.
     *
     * @param digraph the digraph
     */
    public DepthFirstOrder(Digraph digraph) {
        vertexToItsSpotInPreSequence = new int[digraph.getVertexAmount()];
        vertexToItsSpotInPostSequence = new int[digraph.getVertexAmount()];

        vertexesInPostOrder = new Queue<Integer>();
        vertexesInPreOrder = new Queue<Integer>();

        vertexToIsMarked = new boolean[digraph.getVertexAmount()];

        for (int currentVertex = 0; currentVertex < digraph.getVertexAmount(); currentVertex++)
            if (isNotMarked(currentVertex))
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

    // run DFS in digraph G from vertex v and compute preorder/postorder
    private void markVertexesAndPickToSequenceViaDFS(Digraph digraph, int currentVertex) {
        // 标记结点
        vertexToIsMarked[currentVertex] = true;

        // 得到前序遍历的序列 - 手段：在递归调用之前/当前调用中，把当前结点添加到队列中
        vertexToItsSpotInPreSequence[currentVertex] = cursorOfPreSequence++;
        vertexesInPreOrder.enqueue(currentVertex);

        for (int currentAdjacentVertex : digraph.adjacentVertexesOf(currentVertex)) {
            if (isNotMarked(currentAdjacentVertex)) {
                markVertexesAndPickToSequenceViaDFS(digraph, currentAdjacentVertex);
            }
        }

        // 得到后序遍历的序列 - 手段：在递归调用完成之后，把当前结点添加到队列中
        vertexesInPostOrder.enqueue(currentVertex);
        vertexToItsSpotInPostSequence[currentVertex] = cursorOfPostSequence++;
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

    /**
     * Returns the preorder number of vertex {@code v}.
     *
     * @param currentVertex the vertex
     * @return the preorder number of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int spotInPreOrderSequence(int currentVertex) {
        validateVertex(currentVertex);
        return vertexToItsSpotInPreSequence[currentVertex];
    }

    /**
     * Returns the postorder number of vertex {@code v}.
     *
     * @param currentVertex the vertex
     * @return the postorder number of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int spotInPostOrderSequence(int currentVertex) {
        validateVertex(currentVertex);
        return vertexToItsSpotInPostSequence[currentVertex];
    }

    /**
     * Returns the vertices in postorder.
     *
     * @return the vertices in postorder, as an iterable of vertices
     */
    public Iterable<Integer> vertexesInPostOrder() {
        return vertexesInPostOrder;
    }

    /**
     * Returns the vertices in preorder.
     *
     * @return the vertices in preorder, as an iterable of vertices
     */
    public Iterable<Integer> vertexesInPreOrder() {
        return vertexesInPreOrder;
    }

    /**
     * Returns the vertices in reverse postorder.
     *
     * @return the vertices in reverse postorder, as an iterable of vertices
     */
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

        DepthFirstOrder markedDigraph = new DepthFirstOrder(digraph);
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
