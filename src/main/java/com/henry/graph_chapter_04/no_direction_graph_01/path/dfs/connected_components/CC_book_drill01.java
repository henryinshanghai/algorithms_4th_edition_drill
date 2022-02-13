package com.henry.graph_chapter_04.no_direction_graph_01.path.dfs.connected_components;

import com.henry.graph_chapter_04.no_direction_graph_01.graph.Graph;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class CC_book_drill01 {
    // 记录节点有没有被标记过
    private boolean[] marked;
    // 记录顶点所属的分组 id[顶点x] = groupNum
    private int[] ids;
    // 记录当前组的Num index
    private int count;


    // 构造器
    public CC_book_drill01(Graph G) {
        // 初始化成员变量 count的初始值为0 已经初始化完成
        marked = new boolean[G.V()];
        ids = new int[G.V()];

        // 对于图中的每一个顶点
        for (int v = 0; v < G.V(); v++) {
            // 如果当前顶点还没有标记过...  这会有一些循环空转
            if (!marked[v]) {
                // 找到当前节点所连通的所有顶点 然后成组
                dfs(G, v);
                // 得到一个组之后，把groupNum+1
                count++;
            }
        }
    }

    // 作用：把图G中 当前顶点v所连通的所有顶点成组
    private void dfs(Graph G, int v) {
        // 标记当前节点
        marked[v] = true;
        // 为当前节点添加组名  当前节点所属的分组/子图/连通分量的ID为count - 第0组、第1组...
        ids[v] = count;

        // 对所有当前节点邻接表中的所有节点：标记 + 添加组名
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
    }

    // APIs
    // 判断顶点v 与 顶点w是否相连通
    public boolean connected(int v, int w) {
        return ids[v] == ids[w];
    }

    // 获取到当前节点v所属的分组
    public int ids(int v) {
        return ids[v];
    }

    // 获取当前图中所有子图的数量
    public int count() {
        return count;
    }


    public static void main(String[] args) {
        // 创建图 与 连通分量的对象
        Graph graph = new Graph(new In(args[0]));
        CC_book_drill01 cc = new CC_book_drill01(graph);
        // APIs获取图的性质 - 图中有几个子图
        int M = cc.count();
        StdOut.println("there are " + M + " sub-graph in G.");

        // 打印图中所有的连通分量 - 这需要准备邻接表数组
        // 手段：邻接表数组 aka Bag[]   泛型需要在声明时指定好
        Bag<Integer>[] components;
        components = new Bag[M];

        // 初始化components
        for (int i = 0; i < M; i++) {
            components[i] = new Bag<Integer>();
        }

        // 调用API，为components中的item逐一赋值
        for (int v = 0; v < graph.V(); v++) {
            // 把节点v添加到它所属的分组中 - 获取分组的手段：cc.ids[顶点]
            components[cc.ids(v)].add(v);
        }

        // 打印每一个分组中的节点  M个子图 循环M次
        for (int i = 0; i < M; i++) {
            // 对于子图中的每一个顶点...
            for (int v : components[i]) {
                // 打印
                StdOut.print(v + " ");
            }

            StdOut.println();
        }
    }
}
