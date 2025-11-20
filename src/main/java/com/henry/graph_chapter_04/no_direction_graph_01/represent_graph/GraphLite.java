package com.henry.graph_chapter_04.no_direction_graph_01.represent_graph;

import com.henry.graph_chapter_04.no_direction_graph_01.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

// 验证：可以使用 结点 -> 与结点相邻的所有结点集合（顺序无所谓） 来 表示一幅图
public class GraphLite {
    private static final String NEWLINE = System.getProperty("line.separator");

    // 底层数据结构
    private int vertexAmount;
    private int edgeAmount;
    private Bag<Integer>[] vertexToAdjacentVertexes;

    /**
     * 类的构造器
     * 作用：用于 创建 当前类的 实例对象
     * 特征：一般 在构造器中 完成 对成员变量的初始化
     *
     * @param vertexAmount 指定的顶点数量
     */
    public GraphLite(int vertexAmount) {
        this.vertexAmount = vertexAmount;
        this.edgeAmount = 0;
        // 初始化 数组容量
        vertexToAdjacentVertexes = (Bag<Integer>[]) new Bag[vertexAmount];

        // 初始化 数组元素
        for (int currentVertex = 0; currentVertex < vertexAmount; currentVertex++) {
            vertexToAdjacentVertexes[currentVertex] = new Bag<Integer>();
        }
    }

    // 使用一个输入流 来 创建图对象
    public GraphLite(In in) {
        this(in.readInt());

        int edgeAmount = in.readInt(); // 局部变量 用来存储 图中总的边的数量
//        this.edgeAmount = in.readInt(); // 成员变量 用来记录 当前构建的图中的 边的数量

        for (int currentEdge = 0; currentEdge < edgeAmount; currentEdge++) {
            int vertexV = in.readInt();
            int vertexW = in.readInt();

            // 向图对象中 添加当前边
            addEdge(vertexV, vertexW);
        }

    }

    // 由于边没有方向，所以 这里 会把关联关系添加两次
    private void addEdge(int vertexV, int vertexW) {
        edgeAmount++;
        vertexToAdjacentVertexes[vertexV].add(vertexW);
        vertexToAdjacentVertexes[vertexW].add(vertexV);
    }

    // APIs
    public Iterable<Integer> getAdjacentVertexesOf(int vertexV) {
        return vertexToAdjacentVertexes[vertexV];
    }

    public int degreeOf(int vertexV) {
        return vertexToAdjacentVertexes[vertexV].size();
    }

    public String toString() {
        StringBuilder graphPropertyStr = new StringBuilder();
        // 打印图的基本性质
        graphPropertyStr.append(vertexAmount + " vertices, " + edgeAmount + " edges " + NEWLINE); // NEWLINE没用到呀

        // 对于图的每一个顶点
        for (int currentVertex = 0; currentVertex < vertexAmount; currentVertex++) {
            graphPropertyStr.append(currentVertex + ": ");
            // 对于当前顶点的每一个相邻节点...
            for (int currentAdjacentVertex : vertexToAdjacentVertexes[currentVertex]) {
                graphPropertyStr.append(currentAdjacentVertex + " ");
            }
            graphPropertyStr.append(NEWLINE);
        }

        return graphPropertyStr.toString();
    }

    public static void main(String[] args) {
        // 从 命令行参数 中 读取输入流
        In in = new In(args[0]);
        // 从 输入流 中，读取数据 来 构造图
        GraphLite constructedGraph = new GraphLite(in);
        // 打印 图的结构
        StdOut.println(constructedGraph.toString());
    }
}
