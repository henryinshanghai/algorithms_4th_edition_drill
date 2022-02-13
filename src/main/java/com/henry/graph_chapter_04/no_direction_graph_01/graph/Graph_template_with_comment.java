package com.henry.graph_chapter_04.no_direction_graph_01.graph;

import edu.princeton.cs.algs4.In;

public class Graph_template_with_comment {
    // 顶点数量

    // 图中当前边的数量

    // 邻接表数组(aka Bag数组) 用来表示图


    // 构造器 - 初始化所有的成员变量
    // 构造器1 - 传入图的顶点数V
    public Graph_template_with_comment(int V) {


        // 初始化邻接表数组 - 每一个顶点的邻接表 都初始化为空的bag对象


    }

    // 构造函数2 - 传入一个输入流 来 构建图（节点+边）
    public Graph_template_with_comment(In in) {
        // 初始化顶点数量、边的数量


        // 初始化图 - 手段：创建 文本文件所指定的节点所连接的边

        // 读取边的节点


        // 创建边
    }

    // 创建连接两个顶点的边
    private void addEdge(int v, int w) {
        // 添加边 v-w

        // 添加边 w-v （v-w 与 w-v 其实是一条边）

        // 所以 边的数量只需要增加1

    }

    // APIs
    // 1 获取到 节点v的所有相邻顶点 返回值类型是一个Iterable对象
    public Iterable<Integer> adj(int v) {
        return null;
    }

    // 2 节点v的度数 aka 从节点v引出的边的数量
    public int degree(int v) {
        return -1;
    }

    // 3 图的字符串表示方法
    public String toString() {
        // 创建 SB对象


        // 1 打印图的基本性质 - 顶点数量、边的数量


        // 2 打印每一个节点 -> 此节点的所有邻接节点

            // 2-1 当前节点

            // 2-2 当前节点的所有邻接节点


        return "";
    }

    public static void main(String[] args) {
        // 1 读取 文本文件

        // 2 创建图对象

        // 3 打印 图对象的字符串表示

    }

}
