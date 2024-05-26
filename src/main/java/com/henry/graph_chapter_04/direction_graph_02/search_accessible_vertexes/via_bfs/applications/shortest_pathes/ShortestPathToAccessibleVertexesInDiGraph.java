package com.henry.graph_chapter_04.direction_graph_02.search_accessible_vertexes.via_bfs.applications.shortest_pathes;

/******************************************************************************
 *  Compilation:  javac BreadthFirstDirectedPaths.java
 *  Execution:    java BreadthFirstDirectedPaths digraph.txt s
 *  Dependencies: Digraph.java Queue.java Stack.java
 *  Data files:   https://algs4.cs.princeton.edu/42digraph/tinyDG.txt
 *                https://algs4.cs.princeton.edu/42digraph/mediumDG.txt
 *                https://algs4.cs.princeton.edu/42digraph/largeDG.txt
 *
 *  Run breadth-first search on a digraph.
 *  Runs in O(E + V) time.
 *
 *  % java BreadthFirstDirectedPaths tinyDG.txt 3
 *  3 to 0 (2):  3->2->0
 *  3 to 1 (3):  3->2->0->1
 *  3 to 2 (1):  3->2
 *  3 to 3 (0):  3
 *  3 to 4 (2):  3->5->4
 *  3 to 5 (1):  3->5
 *  3 to 6 (-):  not connected
 *  3 to 7 (-):  not connected
 *  3 to 8 (-):  not connected
 *  3 to 9 (-):  not connected
 *  3 to 10 (-):  not connected
 *  3 to 11 (-):  not connected
 *  3 to 12 (-):  not connected
 *
 ******************************************************************************/

import com.henry.graph_chapter_04.direction_graph_02.represent_digraph.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * The {@code BreadthDirectedFirstPaths} class represents a data type for
 * finding shortest paths (number of edges) from a source vertex <em>s</em>
 * (or set of source vertices) to every other vertex in the digraph.
 * <p>
 * This implementation uses breadth-first search.
 * The constructor takes &Theta;(<em>V</em> + <em>E</em>) time in the
 * worst case, where <em>V</em> is the number of vertices and <em>E</em> is
 * the number of edges.
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
// 结论：在有向图的BFS算法中，能够得到 “指定的起始结点” 到 “其可以到达的任意结点”的最短路径。
// 手段：#1 使用一个名叫  terminalVertexToDepartVertex的数组 来 记录下 路径中所经历的各个结点；
// #2 使用一个名叫 vertexToPathLength 来 记录 当前结点距离起始结点（作为方法参数传入）的最短距离
// 具体用法：在获取路径的API（pathFromStartVertexTo）中，使用一个for循环 来 从后往前读取数组中的结点，并添加到一个栈集合中
// 核心步骤：#1 标记结点； #2 入队结点；
public class ShortestPathToAccessibleVertexesInDiGraph {
    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] vertexToHasMarked;  // marked[v] = is there an s->v path?
    private int[] terminalVertexToDepartVertex;      // edgeTo[v] = last edge on shortest s->v path
    private int[] vertexToItsMinPathLength;      // distTo[v] = length of shortest s->v path

    /**
     * Computes the shortest path from {@code s} and every other vertex in graph {@code G}.
     *
     * @param digraph     the digraph
     * @param startVertex the source vertex
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public ShortestPathToAccessibleVertexesInDiGraph(Digraph digraph, int startVertex) {
        vertexToHasMarked = new boolean[digraph.getVertexAmount()];
        vertexToItsMinPathLength = new int[digraph.getVertexAmount()];
        terminalVertexToDepartVertex = new int[digraph.getVertexAmount()];

        for (int currentVertex = 0; currentVertex < digraph.getVertexAmount(); currentVertex++)
            vertexToItsMinPathLength[currentVertex] = INFINITY;
        validateVertex(startVertex);
        markAdjacentVertexesViaBFS(digraph, startVertex);
    }

    /**
     * Computes the shortest path from any one of the source vertices in {@code sources}
     * to every other vertex in graph {@code G}.
     *
     * @param digraph       the digraph
     * @param startVertexes the source vertices
     * @throws IllegalArgumentException if {@code sources} is {@code null}
     * @throws IllegalArgumentException if {@code sources} contains no vertices
     * @throws IllegalArgumentException unless each vertex {@code v} in
     *                                  {@code sources} satisfies {@code 0 <= v < V}
     */
    public ShortestPathToAccessibleVertexesInDiGraph(Digraph digraph, Iterable<Integer> startVertexes) {
        vertexToHasMarked = new boolean[digraph.getVertexAmount()];
        vertexToItsMinPathLength = new int[digraph.getVertexAmount()];
        terminalVertexToDepartVertex = new int[digraph.getVertexAmount()];

        for (int currentVertex = 0; currentVertex < digraph.getVertexAmount(); currentVertex++)
            vertexToItsMinPathLength[currentVertex] = INFINITY;

        validateVertices(startVertexes);

        markAdjacentVertexesViaBFS(digraph, startVertexes);
    }

    // BFS from single source
    private void markAdjacentVertexesViaBFS(Digraph digraph, int startVertex) {
        Queue<Integer> vertexQueue = new Queue<Integer>();
        vertexToHasMarked[startVertex] = true;
        vertexToItsMinPathLength[startVertex] = 0;

        vertexQueue.enqueue(startVertex);

        while (!vertexQueue.isEmpty()) {
            int currentVertex = vertexQueue.dequeue();

            for (int currentAdjacentVertex : digraph.adjacentVertexesOf(currentVertex)) {
                if (!vertexToHasMarked[currentAdjacentVertex]) {
                    // #1 标记结点
                    vertexToHasMarked[currentAdjacentVertex] = true;
                    // #2 更新结点对应的底层成员变量
                    terminalVertexToDepartVertex[currentAdjacentVertex] = currentVertex;
                    vertexToItsMinPathLength[currentAdjacentVertex] = vertexToItsMinPathLength[currentVertex] + 1;

                    // #3 把节点添加到队列中
                    vertexQueue.enqueue(currentAdjacentVertex);
                }
            }
        }
    }

    // BFS from multiple sources
    private void markAdjacentVertexesViaBFS(Digraph digraph, Iterable<Integer> startVertexes) {
        Queue<Integer> vertexSequence = new Queue<Integer>();

        for (int startVertex : startVertexes) {
            vertexToHasMarked[startVertex] = true;
            vertexToItsMinPathLength[startVertex] = 0;
            vertexSequence.enqueue(startVertex);
        }

        while (!vertexSequence.isEmpty()) {
            int currentVertex = vertexSequence.dequeue();
            for (int currentAdjacentVertex : digraph.adjacentVertexesOf(currentVertex)) {
                if (!vertexToHasMarked[currentAdjacentVertex]) {
                    terminalVertexToDepartVertex[currentAdjacentVertex] = currentVertex;
                    vertexToItsMinPathLength[currentAdjacentVertex] = vertexToItsMinPathLength[currentVertex] + 1;
                    vertexToHasMarked[currentAdjacentVertex] = true;

                    vertexSequence.enqueue(currentAdjacentVertex);
                }
            }
        }
    }

    /**
     * Is there a directed path from the source {@code s} (or sources) to vertex {@code v}?
     *
     * @param passedVertex the vertex
     * @return {@code true} if there is a directed path, {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public boolean doesStartVertexHasPathTo(int passedVertex) {
        validateVertex(passedVertex);
        return vertexToHasMarked[passedVertex];
    }

    /**
     * Returns the number of edges in a shortest path from the source {@code s}
     * (or sources) to vertex {@code v}?
     *
     * @param passedVertex the vertex
     * @return the number of edges in such a shortest path
     * (or {@code Integer.MAX_VALUE} if there is no such path)
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int pathLengthTo(int passedVertex) {
        validateVertex(passedVertex);
        return vertexToItsMinPathLength[passedVertex];
    }

    /**
     * Returns a shortest path from {@code s} (or sources) to {@code v}, or
     * {@code null} if no such path.
     *
     * @param passedVertex the vertex
     * @return the sequence of vertices on a shortest path, as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<Integer> pathFromStartVertexTo(int passedVertex) {
        validateVertex(passedVertex);

        if (!doesStartVertexHasPathTo(passedVertex)) return null;

        Stack<Integer> vertexPath = new Stack<Integer>();
        int currentVertexInPath;

        for (currentVertexInPath = passedVertex; vertexToItsMinPathLength[currentVertexInPath] != 0; currentVertexInPath = terminalVertexToDepartVertex[currentVertexInPath])
            vertexPath.push(currentVertexInPath);
        vertexPath.push(currentVertexInPath);

        return vertexPath;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int passedVertex) {
        int V = vertexToHasMarked.length;
        if (passedVertex < 0 || passedVertex >= V)
            throw new IllegalArgumentException("vertex " + passedVertex + " is not between 0 and " + (V - 1));
    }

    // throw an IllegalArgumentException if vertices is null, has zero vertices,
    // or has a vertex not between 0 and V-1
    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("argument is null");
        }
        int vertexCount = 0;
        for (Integer currentVertex : vertices) {
            vertexCount++;
            if (currentVertex == null) {
                throw new IllegalArgumentException("vertex is null");
            }
            validateVertex(currentVertex);
        }
        if (vertexCount == 0) {
            throw new IllegalArgumentException("zero vertices");
        }
    }

    /**
     * Unit tests the {@code BreadthFirstDirectedPaths} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph digraph = new Digraph(in);
        // StdOut.println(digraph);

        int startVertex = Integer.parseInt(args[1]);
        ShortestPathToAccessibleVertexesInDiGraph markedDigraph = new ShortestPathToAccessibleVertexesInDiGraph(digraph, startVertex);

        for (int currentVertex = 0; currentVertex < digraph.getVertexAmount(); currentVertex++) {
            if (markedDigraph.doesStartVertexHasPathTo(currentVertex)) {
                StdOut.printf("%d to %d (%d):  ", startVertex, currentVertex, markedDigraph.pathLengthTo(currentVertex));
                for (int currentVertexInPath : markedDigraph.pathFromStartVertexTo(currentVertex)) {
                    if (currentVertexInPath == startVertex) StdOut.print(currentVertexInPath);
                    else StdOut.print("->" + currentVertexInPath);
                }
                StdOut.println();
            } else {
                StdOut.printf("%d to %d (-):  not connected\n", startVertex, currentVertex);
            }

        }
    }


}