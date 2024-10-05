package com.henry.leetcode_traning_camp.week_02.day5.aboutGraph;

/******************************************************************************
 *  Compilation:  javac Graph.java        
 *  Execution:    java Graph input.txt
 *  Dependencies: Bag.java StackViaNodeTemplate.java In.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/41graph/tinyG.txt
 *                https://algs4.cs.princeton.edu/41graph/mediumG.txt
 *                https://algs4.cs.princeton.edu/41graph/largeG.txt
 *
 *  A graph, implemented using an array of sets.
 *  Parallel edges and self-loops allowed.
 *
 *  % java Graph tinyG.txt
 *  13 vertices, 13 edges 
 *  0: 6 2 1 5 
 *  1: 0 
 *  2: 0 
 *  3: 5 4 
 *  4: 5 6 3 
 *  5: 3 4 0 
 *  6: 0 4 
 *  7: 8 
 *  8: 7 
 *  9: 11 10 12 
 *  10: 9 
 *  11: 9 12 
 *  12: 11 9 
 *
 *  % java Graph mediumG.txt
 *  250 vertices, 1273 edges 
 *  0: 225 222 211 209 204 202 191 176 163 160 149 114 97 80 68 59 58 49 44 24 15 
 *  1: 220 203 200 194 189 164 150 130 107 72 
 *  2: 141 110 108 86 79 51 42 18 14 
 *  ...
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * The {@code Graph} class represents an undirected graph of vertices
 * named 0 through <em>V</em> – 1.
 * It supports the following two primary operations: add an edge to the graph,
 * iterate over all of the vertices adjacent to a vertex. It also provides
 * methods for returning the degree of a vertex, the number of vertices
 * <em>V</em> in the graph, and the number of edges <em>E</em> in the graph.
 * Parallel edges and self-loops are permitted.
 * By convention, a self-loop <em>v</em>-<em>v</em> appears in the
 * adjacency list of <em>v</em> twice and contributes two to the degree
 * of <em>v</em>.
 * <p>
 * This implementation uses an <em>adjacency-lists representation</em>, which
 * is a vertex-indexed array of {@link Bag} objects.
 * It uses &Theta;(<em>E</em> + <em>V</em>) space, where <em>E</em> is
 * the number of edges and <em>V</em> is the number of vertices.
 * All instance methods take &Theta;(1) time. (Though, iterating over
 * the vertices returned by {@link #adjacentVertexesOf(int)} takes time proportional
 * to the degree of the vertex.)
 * Constructing an empty graph with <em>V</em> vertices takes
 * &Theta;(<em>V</em>) time; constructing a graph with <em>E</em> edges
 * and <em>V</em> vertices takes &Theta;(<em>E</em> + <em>V</em>) time.
 * <p>
 * For additional documentation, see
 * <a href="https://algs4.cs.princeton.edu/41graph">Section 4.1</a>
 * of <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
// 验证：可以使用 邻接表(由 结点 -> 结点在图中所有的相邻节点[由Bag类型表示] 所构成) 来 实现图这种逻辑结构
// 🐖 图的图形化表示/字符串表示 可能具有迷惑性：看上去不同的表示 但其实可能是 同一幅图
// 对于规模较小的图，我们能够进行有效的验证。而规模较大的图，我们就无能为力了😳
public class Graph {
    private static final String NEWLINE = System.getProperty("line.separator");

    private final int vertexAmount; // 结点数量
    private int edgeAmount; // 边的数量
    private Bag<Integer>[] vertexToItsAdjacentVertexes; // 结点 -> 结点所有的相邻结点

    // 使用 指定数量的结点 和 0条边 来 初始化一幅空的图
    public Graph(int vertexAmount) {
        if (vertexAmount < 0) {
            throw new IllegalArgumentException("Number of vertices must be nonnegative");
        }

        this.vertexAmount = vertexAmount;
        this.edgeAmount = 0;
        // 实例化
        vertexToItsAdjacentVertexes = (Bag<Integer>[]) new Bag[vertexAmount];
        // 初始化
        for (int currentVertex = 0; currentVertex < vertexAmount; currentVertex++) {
            vertexToItsAdjacentVertexes[currentVertex] = new Bag<Integer>();
        }
    }

    // 从指定的标准输入流中，初始化一幅图
    // 格式是：结点数量 + 边的数量 + 结点对(每个对由xxx分隔)
    public Graph(In inputStream) {
        if (inputStream == null) throw new IllegalArgumentException("argument is null");
        try {
            // #1 读取 图中结点的数量（由文件提供）
            this.vertexAmount = inputStream.readInt();
            if (vertexAmount < 0) {
                throw new IllegalArgumentException("number of vertices in a Graph must be nonnegative");
            }

            // 根据读取到的结点数量 来 创建对应大小的邻接表
            vertexToItsAdjacentVertexes = (Bag<Integer>[]) new Bag[vertexAmount];
            for (int currentVertex = 0; currentVertex < vertexAmount; currentVertex++) {
                vertexToItsAdjacentVertexes[currentVertex] = new Bag<Integer>();
            }

            // #2 读取 图中边的数量（由文件提供）
            int edgeAmount = inputStream.readInt();
            if (edgeAmount < 0) {
                throw new IllegalArgumentException("number of edges in a Graph must be nonnegative");
            }

            // #3 读取每条边中的结点，并 向空图中添加读取到的边
            for (int currentEdge = 0; currentEdge < edgeAmount; currentEdge++) {
                int vertexV = inputStream.readInt(); // 结点1
                int vertexW = inputStream.readInt(); // 结点2

                validateVertex(vertexV);
                validateVertex(vertexW);
                // 向图中添加边
                addEdge(vertexV, vertexW);
            }
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("invalid input format in Graph constructor", e);
        }
    }


    // 由一幅图 进行深拷贝 来 初始化得到一幅新的图
    public Graph(Graph passedGraph) {
        // #1 获取到结点的数量
        this.vertexAmount = passedGraph.getVertexAmount();
        // #2 获取到边的数量
        this.edgeAmount = passedGraph.getEdgeAmount();
        if (vertexAmount < 0) {
            throw new IllegalArgumentException("Number of vertices must be nonnegative");
        }

        // 初始化 各个结点的相邻结点列表
        vertexToItsAdjacentVertexes = (Bag<Integer>[]) new Bag[vertexAmount];
        for (int currentVertex = 0; currentVertex < vertexAmount; currentVertex++) {
            vertexToItsAdjacentVertexes[currentVertex] = new Bag<Integer>();
        }

        // 对于每一个结点...
        for (int currentVertex = 0; currentVertex < passedGraph.getVertexAmount(); currentVertex++) {
            // 把它的相邻结点列表 依次入栈到栈中 [in]
            Stack<Integer> vertexInReverseOrder = new Stack<Integer>();
            for (int currentAdjacentVertex : passedGraph.vertexToItsAdjacentVertexes[currentVertex]) {
                vertexInReverseOrder.push(currentAdjacentVertex);
            }
            // 把栈中的结点 依次添加到 当前结点的相邻结点列表中[out] - 从而得到完全的拷贝
            for (int backwardsVertexCursor : vertexInReverseOrder) {
                vertexToItsAdjacentVertexes[currentVertex].add(backwardsVertexCursor);
            }
        }
    }

    // 返回图中结点的数量
    public int getVertexAmount() {
        return vertexAmount;
    }

    // 返回图中边的数量
    public int getEdgeAmount() {
        return edgeAmount;
    }

    // 校验结点的合法性 [0, V)
    private void validateVertex(int passedVertex) {
        if (passedVertex < 0 || passedVertex >= vertexAmount)
            throw new IllegalArgumentException("vertex " + passedVertex + " is not between 0 and " + (vertexAmount - 1));
    }

    // 向图中添加无向边 v-w
    public void addEdge(int vertexV, int vertexW) {
        validateVertex(vertexV);
        validateVertex(vertexW);
        edgeAmount++;

        vertexToItsAdjacentVertexes[vertexV].add(vertexW);
        vertexToItsAdjacentVertexes[vertexW].add(vertexV);
    }

    // 以可迭代对象的形式 来 返回指定结点的所有相邻节点
    public Iterable<Integer> adjacentVertexesOf(int vertexV) {
        validateVertex(vertexV);
        return vertexToItsAdjacentVertexes[vertexV];
    }

    // 返回指定结点的“度数”
    public int degreeOf(int vertexV) {
        validateVertex(vertexV);
        return vertexToItsAdjacentVertexes[vertexV].size();
    }


    // 返回 图的字符串表示：{#1 结点数量; #2 边的数量; #3 结点 -> 结点所有的相邻结点所组成的列表}
    // 手段：使用 StringBuilder对象 来 连接所有需要的子字符串
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        // 连接 表示 结点数量 与 边的数量 的字符串
        strBuilder.append(vertexAmount + " vertices, " + edgeAmount + " edges " + NEWLINE);

        // 连接 表示 结点 -> 结点所有的相邻结点 的字符串
        for (int currentVertex = 0; currentVertex < vertexAmount; currentVertex++) {
            // #1 当前节点
            strBuilder.append(currentVertex + ": ");
            // #2 与当前节点相邻的所有其他结点
            for (int currentAdjacentVertex : vertexToItsAdjacentVertexes[currentVertex]) {
                strBuilder.append(currentAdjacentVertex + " ");
            }
            // #3 换行符
            strBuilder.append(NEWLINE);
        }

        return strBuilder.toString();
    }

    // “图数据类型”的单元测试
    public static void main(String[] args) {
        // #1 读取命令行参数，解析出标准输入
        In inputStream = new In(args[0]);
        // #2 由标准输入得到 图对象
        Graph constructedGraph = new Graph(inputStream);
        // #3 打印图对象的字符串表示
        StdOut.println(constructedGraph);
    }
}

// 启动步骤：在 program argument 输入框中，输入 图文件的绝对路径。这样 它就能作为命令行参数 传递给 程序执行