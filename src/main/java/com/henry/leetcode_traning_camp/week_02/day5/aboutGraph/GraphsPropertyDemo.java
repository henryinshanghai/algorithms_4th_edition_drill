package com.henry.leetcode_traning_camp.week_02.day5.aboutGraph;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/******************************************************************************
 *  Compilation:  javac GraphClient.java
 *  Execution:    java GraphClient graph.txt
 *  Dependencies: Graph.java
 *
 *  Typical graph-processing code. 一些典型的图相关操作的代码
 *
 *  % java GraphClient tinyG.txt
 *  13 13
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
 *  vertex of maximum degree = 4
 *  average degree           = 2
 *  number of self loops     = 0
 *
 ******************************************************************************/
// 验证：由于图是一个复杂的逻辑结构，因此它的有些性质 不是那么直观的
// 我们需要把图作为参数，对图进行解析后，才能得到这些性质 - 结点的最大度数、
public class GraphsPropertyDemo {

    // 获取到图的所有顶点中的“最大度数”
    public static int getMaxDegreeOfVertexIn(Graph passedGraph) {
        // 准备一个int类型的变量 来 作为“度数最大的顶点”的度数
        int maxDegreeOfVertex = 0;

        // 遍历图中所有的顶点...
        for (int currentVertex = 0; currentVertex < passedGraph.getVertexAmount(); currentVertex++)
            // 如果当前顶点的度数 比 “顶点的最大度数” 更大，说明 需要更新“顶点的最大度数”，则：
            if (passedGraph.degreeOf(currentVertex) > maxDegreeOfVertex)
                // 获取到“当前顶点的度数” 并 绑定到max上
                maxDegreeOfVertex = passedGraph.degreeOf(currentVertex);

        return maxDegreeOfVertex;
    }


    // 计算图中所有节点的平均度数
    public static int avgDegreeOfAllVertexesIn(Graph passedGraph) {
        // 计算公式：图的边的数量 * 2 / 图的结点的数量
        // 原理：在计算度数时，每一条边都会被使用两次（因为一条边的两个顶点）
        return 2 * passedGraph.getEdgeAmount() / passedGraph.getVertexAmount();
    }

    // 获取图中存在的自环的数量
    // 自环：如果一个结点 与自己相邻，则 此结点与边就形成了一个自环
    public static int selfLoopAmountIn(Graph passedGraph) {
        int selfLoopAmount = 0;
        // 遍历图中所有的顶点
        for (int currentVertex = 0; currentVertex < passedGraph.getVertexAmount(); currentVertex++)
            // 遍历“顶点v的相邻顶点集合”中的所有顶点
            for (int currentAdjacentVertex : passedGraph.adjacentVertexesOf(currentVertex))
                // 如果“相邻顶点”与“当前顶点”相同，说明当前顶点存在有“自环”。则:
                if (currentVertex == currentAdjacentVertex) {
                    // 把计数器+1
                    selfLoopAmount++;
                }

        // 因为使用邻接表来表示图，因此 同一个自环 会出现了两次
        return selfLoopAmount / 2;   // self loop appears in adjacency list twice
    }

    // 单元测试
    public static void main(String[] args) {
        // 从输入流中读取图相关的数据————用于生成图对象
        In in = new In(args[0]);
        // 使用Graph()构造器 来 创建图对象
        Graph constructedGraph = new Graph(in);
        // 打印图对象的字符串表示
        StdOut.println(constructedGraph);

        // 获取并打印 图对象的一些性质
        StdOut.println("maximum degree of all vertexes = " + getMaxDegreeOfVertexIn(constructedGraph)); // 图中最大度数的顶点的度数
        StdOut.println("average degree of all vertexes = " + avgDegreeOfAllVertexesIn(constructedGraph)); // 图中所有顶点的平均度数
        StdOut.println("number of self loops in graph = " + selfLoopAmountIn(constructedGraph)); // 图中存在的自环个数
    }

}
/*
任务：重定向“命令行参数”；
手段：
    1 配置“命令行参数”：作为数据输入流的文件的绝对路径；
    2 使用IDEA提供的功能对命令行参数进行重定向；
参考：https://www.jianshu.com/p/6b423699007d
 */