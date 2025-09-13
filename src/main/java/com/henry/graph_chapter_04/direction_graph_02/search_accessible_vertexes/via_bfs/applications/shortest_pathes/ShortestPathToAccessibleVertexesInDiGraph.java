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
    private boolean[] vertexToHasMarked;  // 节点 -> 图中是否存在有 从起始节点到该节点 的路径
    private int[] terminalVertexToDepartVertex;      // 节点 -> 从起始节点到该节点的路径的最后一条边??
    private int[] vertexToItsMinPathLength;      // 节点 -> 从起始节点到该节点的最短路径的长度

    /**
     * Computes the shortest path from {@code s} and every other vertex in graph {@code G}.
     * 计算图中 从s到 其每一个可达节点的最短路径
     * @param digraph     the digraph
     * @param startVertex the source vertex
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public ShortestPathToAccessibleVertexesInDiGraph(Digraph digraph, int startVertex) {
        vertexToHasMarked = new boolean[digraph.getVertexAmount()];
        vertexToItsMinPathLength = new int[digraph.getVertexAmount()];
        terminalVertexToDepartVertex = new int[digraph.getVertexAmount()];

        // 初始化 起始节点到每一个节点的距离 都是 无穷大
        for (int currentVertex = 0; currentVertex < digraph.getVertexAmount(); currentVertex++)
            vertexToItsMinPathLength[currentVertex] = INFINITY;

        validateVertex(startVertex);
        markAdjacentVertexesViaBFS(digraph, startVertex);
    }

    /**
     * Computes the shortest path from any one of the source vertices in {@code sources}
     * to every other vertex in graph {@code G}.
     * 计算图中 给定的起始节点集合 到其可达节点的最短路径
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

    // 单个起始顶点的BFS过程
    private void markAdjacentVertexesViaBFS(Digraph digraph, int startVertex) {
        // 准备 队列 用于存储节点
        Queue<Integer> vertexQueue = new Queue<Integer>();
        // 标记节点
        vertexToHasMarked[startVertex] = true;
        // 初始化 起始节点 到 当前节点的 距离为0
        vertexToItsMinPathLength[startVertex] = 0;

        // 入队起始节点
        vertexQueue.enqueue(startVertex);

        while (!vertexQueue.isEmpty()) {
            // 出队队首节点
            int currentVertex = vertexQueue.dequeue();

            // 对于节点的所有邻居节点...
            for (int currentAdjacentVertex : digraph.adjacentVertexesOf(currentVertex)) {
                // 如果 该邻居节点 还没有被标记，说明还没有搜索到此节点，则：
                if (!vertexToHasMarked[currentAdjacentVertex]) {
                    // #1 标记 该邻居结点
                    vertexToHasMarked[currentAdjacentVertex] = true;
                    // #2 更新 该邻居结点 所对应的底层成员变量
                    terminalVertexToDepartVertex[currentAdjacentVertex] = currentVertex;
                    vertexToItsMinPathLength[currentAdjacentVertex] = vertexToItsMinPathLength[currentVertex] + 1;

                    // #3 把 该邻居节点 添加到队列中
                    vertexQueue.enqueue(currentAdjacentVertex);
                }
            }
        }
    }

    // 多个起始节点的BFS过程
    private void markAdjacentVertexesViaBFS(Digraph digraph, Iterable<Integer> startVertexes) {
        // 准备节点队列
        Queue<Integer> vertexSequence = new Queue<Integer>();

        // 对于起始节点集合中的每一个起始节点..
        for (int startVertex : startVertexes) {
            // 初始化 该起始节点 所对应的成员变量
            vertexToHasMarked[startVertex] = true;
            vertexToItsMinPathLength[startVertex] = 0;
            // 把 该起始顶点 入队
            vertexSequence.enqueue(startVertex);
        }

        while (!vertexSequence.isEmpty()) {
            // 出队 队首元素
            int currentVertex = vertexSequence.dequeue();
            // 对于 其每一个邻居节点...
            for (int currentAdjacentVertex : digraph.adjacentVertexesOf(currentVertex)) {
                // 如果 该邻居节点 还没有被标记...
                if (!vertexToHasMarked[currentAdjacentVertex]) {
                    // 标记它 && 更新其所对应的成员变量
                    terminalVertexToDepartVertex[currentAdjacentVertex] = currentVertex;
                    vertexToItsMinPathLength[currentAdjacentVertex] = vertexToItsMinPathLength[currentVertex] + 1;
                    vertexToHasMarked[currentAdjacentVertex] = true;

                    // 入队该邻居节点
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
        // 文件名 -> 文件流 -> 有向图
        In in = new In(args[0]);
        Digraph digraph = new Digraph(in);
        // StdOut.println(digraph);

        // 起始节点
        int startVertex = Integer.parseInt(args[1]);
        // 经过BFS标记后的有向图
        ShortestPathToAccessibleVertexesInDiGraph markedDigraph = new ShortestPathToAccessibleVertexesInDiGraph(digraph, startVertex);

        // 对于当前顶点...
        for (int currentVertex = 0; currentVertex < digraph.getVertexAmount(); currentVertex++) {
            // 如果 从起始顶点可以到达它
            if (markedDigraph.doesStartVertexHasPathTo(currentVertex)) {
                // 打印 最短路径的长度
                StdOut.printf("%d to %d (%d):  ", startVertex, currentVertex, markedDigraph.pathLengthTo(currentVertex));
                // 打印出 具体的最短路径
                for (int currentVertexInPath : markedDigraph.pathFromStartVertexTo(currentVertex)) {
                    if (currentVertexInPath == startVertex) StdOut.print(currentVertexInPath);
                    else StdOut.print("->" + currentVertexInPath);
                }
                StdOut.println();
            } else {
                // 打印 此节点由起始顶点不可达
                StdOut.printf("%d to %d (-):  not connected\n", startVertex, currentVertex);
            }

        }
    }
}