package com.henry.graph_chapter_04.no_direction_graph_01.path.bfs;

import com.henry.graph_chapter_04.no_direction_graph_01.graph.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class BreadthFirstPath_template_with_comment {
    // 节点是否已经被标记/处理

    // 用于回溯出路径的数组

    // 指定的起点顶点


    public BreadthFirstPath_template_with_comment(Graph G, int s) {
        // 初始化成员变量


        // 执行任务

    }

    // 为了一般性 这里把顶点的名字叫做v
    // BFS - 先添加到数据结构中的边，先处理
    private void bfs(Graph G, int v) {
        // 准备一个队列


        // 标记起点


        // 入队起点


        // 进行BFS的循环，直到图中所有能标记的点都已经被标记了
        // 循环结束的条件 - 数据结构中没有节点（aka 图中所有能够处理的节点都已经处理完成）

            // 出队节点

            // 处理当前节点的所有相邻节点


                    // 标记

                    // 记录边

                    // 入队 - 入队之前先标记好节点
    }

    // public APIs
    public boolean hasPathTo(int v) {
        return false;
    }

    // pathTo
    public Iterable<Integer> shortestPathTo(int v) {
        if(!hasPathTo(v)) return null;

        Stack<Integer> pathIfExist = new Stack<>();
        // 从edgeTo[]数组中回溯出路径中的所有节点


        return pathIfExist;
    }

    public static void main(String[] args) {
        // 创建图 与 起点

        // 创建对象

        // 打印 以s作为起点，到它所连通的所有顶点的所有最短路径

    }

}
