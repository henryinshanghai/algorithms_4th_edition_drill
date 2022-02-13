package com.henry.graph_chapter_04.no_direction_graph_01.path.dfs.go_through_graph;

import com.henry.graph_chapter_04.no_direction_graph_01.graph.Graph;
import edu.princeton.cs.algs4.Stack;

public class DepthFirstPaths_template_with_comment {
    /* 根据具体任务进行成员变量的设置 */
    // 当前节点有没有被标记过

    // 记录路径 - 手段：一个记录节点的数组

    // 起始顶点 - 为什么这欧里的起点s需要作为成员变量？   因为路径中需要这个顶点s，而且使用成员变量方便在方法中使用它


    // 构造方法的语法中不能够添加返回值类型
    public DepthFirstPaths_template_with_comment(Graph G, int s) {
        // 初始状态都是未标记

        // 数组中所有位置的值初始都是0 -  路径的长度 不会超过 图中总节点的数量

        // 初始化起点s

        // 处理“单点路径”的任务

    }

    // 作用：标记节点 + 记录路径中的节点
    private void dfs(Graph G, int v) {
        // 标记当前顶点

        // 对于当前顶点的所有相邻节点

            // 如果相邻节点还没有被标记过...

                // 记录 从当前节点到当前相邻节点的边 v->w from v to w

                // 对当前节点进行同样的操作


    }

    // APIs
    // 指定的顶点v与起点s是不是连通的
    public boolean hasPathTo(int v) {
        return false;
    }

    // 获取到从起点s 到 指定顶点v的路径 - 具体方式：一个可迭代的数据
    public Iterable<Integer> pathTo(int v) {
        // 判断是不是存在这样一个路径


        // 从数组中转化出 路径中的所有节点
        // 准备一个栈对象

        // 从路径的最后一个节点，往前回溯 从而得到整个路径（有序）
        // x作为当前节点 初始条件：为当前节点绑定路径中的最后一个节点  判断条件：当前节点是不是起始节点 更新当前节点：使用当前节点的上一个节点来更新当前节点
        // v - w; from v to w, edge[w] = v  当前节点w的上一个节点：edgeTo[w]

            // 把当前节点添加到栈数据中

        // 把起始节点添加到栈数据中


        // 返回栈数据
        return new Stack<>();
    }

    public static void main(String[] args) {
        // 创建图与起点

        // 创建 path对象


        // 对于图中的每一个顶点

            // 从起点到 当前顶点的路径为...


            // 判断从起始节点s 到当前节点是否连通

                // 获取到 路径的可迭代对象(aka 栈对象) - 手段：foreach语法

                    // 从栈结构中顺序获取到路径中的节点     形式: a - b - c - d




    }
}
