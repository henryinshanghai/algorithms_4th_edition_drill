package com.henry.leetcode_traning_camp.week_02.day5.aboutGraph;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;

public class GraphFromBook {

    // 底层数据结构及成员变量
    private final int V; // 图中顶点的数量
    private int E; // 图中边的数量

    // 使用顶点作为索引的整型链表数组
    private Bag<Integer>[] adj; // 存储图的底层数据结构：Bag类型的数组  而Bag本身是一个Integer类型数据的集合

    // 构造方法
    /**
     * 创建含有V个顶点但是不含有边的图
     * @param V
     */
    public GraphFromBook(int V) {
        // 初始化V、E
        this.V = V;
        this.E = 0;

        // 初始化邻接表Bag[]的每一个元素为空bag对象
        adj = (Bag<Integer>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<Integer>();
        }
    }

    /**
     * 从标准输入流中获取到V、E信息来创建一个图对象
     */
    public GraphFromBook(In in) {
        // 读取第一个数字    作为节点数量V
        // 使用读取到的顶点数来创建一个图————此时图中的各个顶点相互独立
        this(in.readInt()); // 调用第一个构造方法

        // 读取第二个数字  作为图中边的数量
        int E = in.readInt();

        // 使用接下来的数字对来创建图中的边
        for (int i = 0; i < E; i++) {
            // 获取到边的顶点
            int v = in.readInt();
            int w = in.readInt();
            // 使用顶点在图中创建一条边
            addEdge(v, w);
        }
    }


    /**
     * 在图中使用指定顶点来添加一条边
     * 手段：在邻接表中，找到指定顶点v，然后在相邻顶点的链表中添加另外一个顶点w
     * 反过来再做一遍
     * @param v
     * @param w
     */
    public void addEdge(int v, int w) {
        adj[v].add(w);
        adj[w].add(v);
        E++;
    }

    // 核心APIs
    public int V() {
        return V; // 图中顶点的数量
    }

    public int E() {
        return E; // 图中边的数量
    }
}
