package com.henry.graph_chapter_04.direction_graph_02.search_accessible_vertexes.via_dfs.applications.find_path_to_vertex_in_digraph_02; /******************************************************************************
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
 * The {@code DepthFirstDirectedPaths} class represents a data type for
 * finding directed paths from a source vertex <em>s</em> to every
 * other vertex in the digraph.
 * <p>
 * This implementation uses depth-first search.
 * The constructor takes &Theta;(<em>V</em> + <em>E</em>) time in the
 * worst case, where <em>V</em> is the number of vertices and <em>E</em>
 * is the number of edges.
 * Each instance method takes &Theta;(1) time.
 * It uses &Theta;(<em>V</em>) extra space (not including the digraph).
 * <p>
 * See {@link PathToAccessibleVertexesInDigraph} for a nonrecursive implementation.
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
// 结论：在 有向图的DFS算法 中，能够得到 “指定的起始结点” 到 “其可以到达的任意结点”的路径。
// 手段：使用一个名叫  terminalVertexToDepartVertex的数组 来 记录下 路径中所经历的各个结点
// 具体用法：在获取路径的API（pathFromStartVertexTo）中，使用一个for循环 来 从后往前读取 数组中的结点，并 添加到 一个栈集合中。
public class PathToAccessibleVertexesInDigraph {
    // 顶点 -> 顶点是否被标记(由起点可达) 的映射关系   用于表示顶点 是否已经被访问
    private boolean[] vertexToIsMarked;
    // 边的终点 -> 边的出发点 的映射关系 用于还原出路径
    private int[] terminalVertexToDepartVertex;
    // 起始顶点 作为成员变量，方便在多个方法中直接访问
    private final int startVertex;

    /**
     * Computes a directed path from {@code s} to every other vertex in digraph {@code G}.
     * 计算 在有向图中 从起始顶点到 其可达的所有其他顶点的 有向路径
     * @param digraph     the digraph
     * @param startVertex the source vertex
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     */
    public PathToAccessibleVertexesInDigraph(Digraph digraph, int startVertex) {
        vertexToIsMarked = new boolean[digraph.getVertexAmount()];
        terminalVertexToDepartVertex = new int[digraph.getVertexAmount()];
        this.startVertex = startVertex;
        validateVertex(startVertex);
        markAdjacentVertexesViaDFS(digraph, startVertex);
    }

    private void markAdjacentVertexesViaDFS(Digraph digraph, int currentVertex) {
        // 标记 当前顶点 为 已访问
        vertexToIsMarked[currentVertex] = true;

        for (int currentAdjacentVertex : digraph.adjacentVertexesOf(currentVertex)) {
            if (!vertexToIsMarked[currentAdjacentVertex]) {
                // 记录 当前边 从 终止顶点 -> 起始顶点 的映射关系
                terminalVertexToDepartVertex[currentAdjacentVertex] = currentVertex;
                markAdjacentVertexesViaDFS(digraph, currentAdjacentVertex);
            }
        }
    }

    // key API*1: 在图中，是否存在有 由起始顶点到指定顶点的路径?
    public boolean doesStartVertexHasPathTo(int passedVertex) {
        validateVertex(passedVertex);
        // 手段：查看 目标节点 是否被标记为“由起点可达的节点”
        return vertexToIsMarked[passedVertex];
    }


    // key API*2：返回图中 由起始顶点到指定顶点的有向路径（如果存在的话）。如果路径不存在，则返回null
    public Iterable<Integer> pathFromStartVertexTo(int passedVertex) {
        validateVertex(passedVertex);
        // 如果 传入的节点 是 不可达的，说明 不存在这样的路径，则：直接返回null
        if (!doesStartVertexHasPathTo(passedVertex)) return null;

        // 准备一个容器，用于存储 路径中所有顶点
        Stack<Integer> vertexPath = new Stack<Integer>();
        // 在 terminalVertexToDepartVertex数组中，按照 边的终止顶点 -> 出发顶点的映射关系。逆向逐一拾取路径中的顶点
        for (int backwardsVertexCursor = passedVertex; backwardsVertexCursor != startVertex; backwardsVertexCursor = terminalVertexToDepartVertex[backwardsVertexCursor])
            // 由于存储容器是一个栈，因此 路径中靠后的顶点 会先入栈（在栈底），靠前的顶点 后入栈（在栈顶）
            vertexPath.push(backwardsVertexCursor);

        // 上面的for循环 不会把 起始顶点入栈，在这里 手动入栈 起始顶点
        vertexPath.push(startVertex);

        // 返回 收集了所有路径顶点的栈容器
        return vertexPath;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int passedVertex) {
        int vertexAmount = vertexToIsMarked.length;
        if (passedVertex < 0 || passedVertex >= vertexAmount)
            throw new IllegalArgumentException("vertex " + passedVertex + " is not between 0 and " + (vertexAmount - 1));
    }

    /**
     * Unit tests the {@code DepthFirstDirectedPaths} data type.
     * 对数据类型的单元测试 - 对数据类型的功能进行测试，看它是否按照预期工作
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        // 使用命令行参数 得到文件流
        In in = new In(args[0]);
        // 使用文件流 得到有向图
        Digraph digraph = new Digraph(in);
        // StdOut.println(digraph);

        // 读取 起始顶点
        int startVertex = Integer.parseInt(args[1]);
        // 得到 起始顶点 到 其可达顶点的路径   手段：调用构造器方法
        PathToAccessibleVertexesInDigraph markedDigraph = new PathToAccessibleVertexesInDigraph(digraph, startVertex);

        // 遍历图中的每一个顶点...
        for (int currentVertex = 0; currentVertex < digraph.getVertexAmount(); currentVertex++) {
            // 如果 从起始顶点存在有 到该顶点的路径，则：
            if (markedDigraph.doesStartVertexHasPathTo(currentVertex)) {
                StdOut.printf("%d to %d:  ", startVertex, currentVertex);
                // 获取到路径所对应的栈，然后 迭代地 从栈中读取结点 - 栈中结点的顺序 就是 ·路径中结点的顺序
                for (int currentVertexInPath : markedDigraph.pathFromStartVertexTo(currentVertex)) {
                    // 如果 当前顶点 就是 起始顶点，说明 它是路径中的第一个顶点，则：直接打印它
                    if (currentVertexInPath == startVertex) StdOut.print(currentVertexInPath);
                    // 如果 不是，说明 需要使用 - vertex的格式 把它打印出来，则：👇
                    else StdOut.print("-" + currentVertexInPath);
                }
                StdOut.println();
            } else { // 如果 两个顶点之间 不存在路径，说明 两个顶点之间不相互连通，则：
                // 打印如下语句：xxx 与 ooo之间不相互连通
                StdOut.printf("%d to %d:  not connected\n", startVertex, currentVertex);
            }
        }
    }

}