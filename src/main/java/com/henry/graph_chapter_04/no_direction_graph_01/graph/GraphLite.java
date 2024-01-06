package com.henry.graph_chapter_04.no_direction_graph_01.graph;

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

    // 构造函数
    public GraphLite(int vertexAmount) {
        this.vertexAmount = vertexAmount;
        this.edgeAmount = 0;
        vertexToAdjacentVertexes = (Bag<Integer>[]) new Bag[vertexAmount];

        for (int currentVertex = 0; currentVertex < vertexAmount; currentVertex++) {
            vertexToAdjacentVertexes[currentVertex] = new Bag<Integer>();
        }
    }

    public GraphLite(In in) {
        this(in.readInt());

        int edgeAmount = in.readInt(); // 局部变量用来存储图中总的边的数量
//        this.edgeAmount = in.readInt(); // 成员变量用来记录当前构建的图中的边的数量

        for (int currentEdge = 0; currentEdge < edgeAmount; currentEdge++) {
            int vertexV = in.readInt();
            int vertexW = in.readInt();

            addEdge(vertexV, vertexW);
        }

    }

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

        // 遍历图的每一个顶点
        for (int currentVertex = 0; currentVertex < vertexAmount; currentVertex++) {
            graphPropertyStr.append(currentVertex + ": ");
            // 遍历当前顶点的每一个相邻节点
            for (int currentAdjacentVertex : vertexToAdjacentVertexes[currentVertex]) {
                graphPropertyStr.append(currentAdjacentVertex + " ");
            }
            graphPropertyStr.append(NEWLINE);
        }

        return graphPropertyStr.toString();
    }

    public static void main(String[] args) {
        // 从命令行参数中读取输入流
        In in = new In(args[0]);
        // 从输入流中，读取数据来构造图
        GraphLite constructedGraph = new GraphLite(in);
        // 打印图的结构
        StdOut.println(constructedGraph.toString());
    }
}
