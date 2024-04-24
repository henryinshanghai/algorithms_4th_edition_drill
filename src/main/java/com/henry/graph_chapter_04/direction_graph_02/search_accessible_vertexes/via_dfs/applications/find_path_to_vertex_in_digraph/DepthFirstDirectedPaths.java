package com.henry.graph_chapter_04.direction_graph_02.search_accessible_vertexes.via_dfs.applications.find_path_to_vertex_in_digraph; /******************************************************************************
 *  Compilation:  javac DepthFirstDirectedPaths.java
 *  Execution:    java DepthFirstDirectedPaths digraph.txt s
 *  Dependencies: Digraph.java Stack.java
 *  Data files:   https://algs4.cs.princeton.edu/42digraph/tinyDG.txt
 *                https://algs4.cs.princeton.edu/42digraph/mediumDG.txt
 *                https://algs4.cs.princeton.edu/42digraph/largeDG.txt
 *
 *  Determine reachability in a digraph from a given vertex using
 *  depth-first search.
 *  Runs in O(E + V) time.
 *
 *  % java DepthFirstDirectedPaths tinyDG.txt 3
 *  3 to 0:  3-5-4-2-0
 *  3 to 1:  3-5-4-2-0-1
 *  3 to 2:  3-5-4-2
 *  3 to 3:  3
 *  3 to 4:  3-5-4
 *  3 to 5:  3-5
 *  3 to 6:  not connected
 *  3 to 7:  not connected
 *  3 to 8:  not connected
 *  3 to 9:  not connected
 *  3 to 10:  not connected
 *  3 to 11:  not connected
 *  3 to 12:  not connected
 *
 ******************************************************************************/

import com.henry.graph_chapter_04.direction_graph_02.represent_digraph.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 *  The {@code DepthFirstDirectedPaths} class represents a data type for
 *  finding directed paths from a source vertex <em>s</em> to every
 *  other vertex in the digraph.
 *  <p>
 *  This implementation uses depth-first search.
 *  The constructor takes &Theta;(<em>V</em> + <em>E</em>) time in the
 *  worst case, where <em>V</em> is the number of vertices and <em>E</em>
 *  is the number of edges.
 *  Each instance method takes &Theta;(1) time.
 *  It uses &Theta;(<em>V</em>) extra space (not including the digraph).
 *  <p>
 *  See {@link DepthFirstDirectedPaths} for a nonrecursive implementation.
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
// 结论：在有向图的DFS算法中，能够得到 “指定的起始结点” 到 “其可以到达的任意结点”的路径。
// 手段：使用一个名叫  terminalVertexToDepartVertex的数组 来 记录下 路径中所经历的各个结点
// 具体用法：在获取路径的API（pathFromStartVertexTo）中，使用一个for循环 来 从后往前读取数组中的结点，并添加到一个栈集合中。
public class DepthFirstDirectedPaths {
    private boolean[] vertexToIsMarked;  // marked[v] = true iff v is reachable from s
    private int[] terminalVertexToDepartVertex;      // edgeTo[v] = last edge on path from s to v
    private final int startVertex;       // source vertex

    /**
     * Computes a directed path from {@code s} to every other vertex in digraph {@code G}.
     * @param  digraph the digraph
     * @param  startVertex the source vertex
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     */
    public DepthFirstDirectedPaths(Digraph digraph, int startVertex) {
        vertexToIsMarked = new boolean[digraph.getVertexAmount()];
        terminalVertexToDepartVertex = new int[digraph.getVertexAmount()];
        this.startVertex = startVertex;
        validateVertex(startVertex);
        markAdjacentVertexesViaDFS(digraph, startVertex);
    }

    private void markAdjacentVertexesViaDFS(Digraph digraph, int currentVertex) {
        vertexToIsMarked[currentVertex] = true;

        for (int currentAdjacentVertex : digraph.adjacentVertexesOf(currentVertex)) {
            if (!vertexToIsMarked[currentAdjacentVertex]) {
                terminalVertexToDepartVertex[currentAdjacentVertex] = currentVertex;
                markAdjacentVertexesViaDFS(digraph, currentAdjacentVertex);
            }
        }
    }

    /**
     * Is there a directed path from the source vertex {@code s} to vertex {@code v}?
     * @param  passedVertex the vertex
     * @return {@code true} if there is a directed path from the source
     *         vertex {@code s} to vertex {@code v}, {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public boolean doesStartVertexHasPathTo(int passedVertex) {
        validateVertex(passedVertex);
        return vertexToIsMarked[passedVertex];
    }


    /**
     * Returns a directed path from the source vertex {@code s} to vertex {@code v}, or
     * {@code null} if no such path.
     * @param  passedVertex the vertex
     * @return the sequence of vertices on a directed path from the source vertex
     *         {@code s} to vertex {@code v}, as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<Integer> pathFromStartVertexTo(int passedVertex) {
        validateVertex(passedVertex);
        if (!doesStartVertexHasPathTo(passedVertex)) return null;

        Stack<Integer> vertexPath = new Stack<Integer>();
        for (int backwardsVertexCursor = passedVertex; backwardsVertexCursor != startVertex; backwardsVertexCursor = terminalVertexToDepartVertex[backwardsVertexCursor])
            vertexPath.push(backwardsVertexCursor);

        vertexPath.push(startVertex);
        return vertexPath;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int passedVertex) {
        int vertexAmount = vertexToIsMarked.length;
        if (passedVertex < 0 || passedVertex >= vertexAmount)
            throw new IllegalArgumentException("vertex " + passedVertex + " is not between 0 and " + (vertexAmount-1));
    }

    /**
     * Unit tests the {@code DepthFirstDirectedPaths} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph digraph = new Digraph(in);
        // StdOut.println(digraph);

        int startVertex = Integer.parseInt(args[1]);
        DepthFirstDirectedPaths markedDigraph = new DepthFirstDirectedPaths(digraph, startVertex);

        for (int currentVertex = 0; currentVertex < digraph.getVertexAmount(); currentVertex++) {
            if (markedDigraph.doesStartVertexHasPathTo(currentVertex)) {
                StdOut.printf("%d to %d:  ", startVertex, currentVertex);
                // 从栈中读取结点 - 栈中结点的顺序 就是 路径中结点的顺序
                for (int currentVertexInPath : markedDigraph.pathFromStartVertexTo(currentVertex)) {
                    if (currentVertexInPath == startVertex) StdOut.print(currentVertexInPath);
                    else        StdOut.print("-" + currentVertexInPath);
                }
                StdOut.println();
            }

            else {
                StdOut.printf("%d to %d:  not connected\n", startVertex, currentVertex);
            }

        }
    }

}