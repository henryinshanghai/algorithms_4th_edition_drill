package com.henry.graph_chapter_04.no_direction_graph_01.path.dfs.connected_components;

import com.henry.graph_chapter_04.no_direction_graph_01.graph.Graph;

public class CC_template_with_comment {
    // 记录节点有没有被标记过

    // 记录顶点所属的分组 id[顶点x] = groupNum

    // 记录当前组的Num index


    // 构造器
    public CC_template_with_comment(Graph G) {
        // 初始化成员变量 count的初始值为0 已经初始化完成


        // 对于图中的每一个顶点

            // 如果当前顶点还没有标记过...

                // 找到当前节点所连通的所有顶点 然后成组

                // 得到一个组之后，把groupNum+1

    }

    // 作用：把图G中 当前顶点v所连通的所有顶点成组
    private void dfs(Graph G, int v) {
        // 标记当前节点

        // 为当前节点添加组名  当前节点所属的分组/子图/连通分量的ID为count - 第0组、第1组...


        // 对所有当前节点邻接表中的所有节点：标记 + 添加组名

    }

    // APIs
    // 判断顶点v 与 顶点w是否相连通
    public boolean connected(int v, int w) {
        return false;
    }

    // 获取到当前节点v所属的分组
    public int ids(int v) {
        return -1;
    }

    // 获取当前图中所有子图的数量
    public int count() {
        return -1;
    }


    public static void main(String[] args) {
        // 创建图 与 连通分量的对象

        // APIs获取图的性质 - 图中有几个子图

        // 打印图中所有的连通分量 - 这需要准备邻接表数组
        // 手段：邻接表数组 aka Bag[]

        // 初始化components

        // 调用API，为components中的item逐一赋值

            // 把节点v添加到它所属的分组中 - 获取分组的手段：cc.ids[顶点]

        // 打印每一个分组中的节点
        // M个子图 循环M次
            // 对于子图中的每一个顶点...
                // 打印

    }
}
