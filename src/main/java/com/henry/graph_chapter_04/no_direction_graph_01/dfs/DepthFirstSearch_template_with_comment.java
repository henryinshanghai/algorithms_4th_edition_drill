package com.henry.graph_chapter_04.no_direction_graph_01.dfs;

import com.henry.graph_chapter_04.no_direction_graph_01.graph.Graph;

public class DepthFirstSearch_template_with_comment {
    // 记录某个顶点是否可以通过起点到达 手段：使用一个size和图中顶点数量相同的boolean类型数组

    // 记录 被标记的节点数量


    // 构造器 - 完成成员变量的初始化
    public DepthFirstSearch_template_with_comment(Graph G, int s) {
        // 初始化 boolean数组 起始默认为false

        // 调用dfs 在遍历的同时：标记顶点、检查顶点

    }

    // 如何验证 通过这种方式能够遍历图中所有的顶点与边？
    // 深度优先搜索 只能够找到 图G中所有 与顶点s相连通的节点集合
    private void dfs(Graph G, int v) {
        // 标记顶点“已经被访问过”

        // 记录“被标记的节点”的数量

        // 对于 当前节点v的每一个相邻节点w

            // 如果节点w还没有被标记过，就对它进行深度搜索

    }

    // APIs
    // 1 判断顶点w是否已经被标记 aka 是否从起点s可达
    public boolean marked(int w) {
        return false;
    }

    // 2 获取到与起点s相连通的顶点的个数
    public int count() {
        return -1;
    }

    public static void main(String[] args) {
        // 从命令行参数中读取图

        // 从命令行参数中读取起点


        // 1 创建 DFS对象

        // 2 调用API来获取图的性质
        // 对于图中的每一个顶点v

            // 如果v与起点s是连通的...

                // 打印出此节点



        // 如果 与起点连通的顶点的数量 不等于 图中所有顶点的数量

    }
}
