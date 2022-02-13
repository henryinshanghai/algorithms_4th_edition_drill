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

public class GraphClient {

    // maximum degree
    /**
     * 获取到图中度数最大的顶点
     * @param G
     * @return
     */
    public static int maxDegree(Graph G) {
        // 准备一个int类型的变量来作为度数最大的顶点的度数
        int max = 0;

        // 遍历图中所有的顶点...
        for (int v = 0; v < G.V(); v++)
            // 如果当前顶点的度数比
            if (G.degree(v) > max)
                // 获取到当前顶点的度数并绑定到max上
                max = G.degree(v);
        return max;
    }

    // average degree
    /**
     * 图中所有节点的平均度数
     * @param G
     * @return
     */
    public static int avgDegree(Graph G) {
        // each edge incident on two vertices
        // 计算度数时，每一条边都会被使用两次
        return 2 * G.E() / G.V();
    }

    // number of self-loops
    /**
     * 图中自环的数量
     * 如何判断存在自环呢？
     * 一个顶点与自己相邻，则这个顶点与边形成了一个自环
     * @param G
     * @return
     */
    public static int numberOfSelfLoops(Graph G) {
        int count = 0;
        // 遍历图中所有的顶点
        for (int v = 0; v < G.V(); v++)
            // 遍历顶点v的所有相邻顶点集合中的顶点
            for (int w : G.adj(v))
                // 如果相邻顶点与当前顶点相同，说明当前顶点存在自环。则:
                // 把计数器+1
                if (v == w) count++;
        // 自环在邻接表中出现了两次
        return count/2;   // self loop appears in adjacency list twice
    }

    public static void main(String[] args) {
        // 从输入流中读取数据————用于生成图对象
        In in = new In(args[0]);
        // 使用Graph()构造器来创建图对象
        Graph G = new Graph(in);
        // 打印图对象G
        StdOut.println(G);


        StdOut.println("vertex of maximum degree = " + maxDegree(G)); // 图中最大度数的顶点的度数
        StdOut.println("average degree           = " + avgDegree(G)); // 图中所有顶点的平均度数
        StdOut.println("number of self loops     = " + numberOfSelfLoops(G)); // 图中存在的自环个数

    }

}
/*
任务：重定向命令行参数；
手段：
    1 配置命令行参数：作为数据输入流的文件的绝对路径；
    2 使用IDEA提供的功能对命令行参数进行重定向；
参考：https://www.jianshu.com/p/6b423699007d
 */