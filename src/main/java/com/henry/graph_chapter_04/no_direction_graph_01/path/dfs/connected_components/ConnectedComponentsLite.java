package com.henry.graph_chapter_04.no_direction_graph_01.path.dfs.connected_components;

import com.henry.graph_chapter_04.no_direction_graph_01.graph.Graph;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

// 验证：可以使用在图中从指定起点开始进行DFS（递归地标记结点）的方式 来 获取到图中存在几个连通分量，以及各个连通分量中的结点
public class ConnectedComponentsLite {
    // 记录节点有没有被标记过
    private boolean[] vertexToIsMarked;
    // 记录顶点所属的分组 id[顶点x] = groupNum
    private int[] vertexToItsComponentId; // 这里的componentId 使用 componentAmount来设置
    // 记录当前组的Num index
    private int componentAmount;

    // 构造器
    public ConnectedComponentsLite(Graph graph) {
        // 初始化成员变量 count的初始值为0 已经初始化完成
        vertexToIsMarked = new boolean[graph.vertexAmount()];
        vertexToItsComponentId = new int[graph.vertexAmount()];

        // 对于图中的每一个顶点
        for(int currentVertex = 0; currentVertex < graph.vertexAmount(); currentVertex++) {
            // 如果当前顶点还没有标记过...
            if (isNotMarked(currentVertex)) {
                // 找到当前节点所连通的所有顶点 然后成组
                markVertexAndAssignItsComponentIdViaDFS(graph, currentVertex);
                // 得到一个组之后，把groupNum+1
                componentAmount++;
            }
        }
    }

    private boolean isNotMarked(int currentVertex) {
        return !vertexToIsMarked[currentVertex];
    }

    // 作用：把图G中 当前顶点v所连通的所有顶点成组
    private void markVertexAndAssignItsComponentIdViaDFS(Graph graph, int currentVertex) {
        // 标记当前节点
        vertexToIsMarked[currentVertex] = true;
        // 为当前节点添加组名  当前节点所属的分组/子图/连通分量的ID为count - 第0组、第1组...
        vertexToItsComponentId[currentVertex] = componentAmount;

        // 对所有当前节点邻接表中的所有节点：标记 + 添加组名
        for (int currentAdjacentVertex : graph.adjacentVertexesOf(currentVertex)) {
            if (isNotMarked(currentAdjacentVertex)) {
                markVertexAndAssignItsComponentIdViaDFS(graph, currentAdjacentVertex);
            }
        }
    }

    // APIs
    // 判断顶点v 与 顶点w是否相连通
    public boolean isConnectedBetween(int vertexV, int vertexW) {
        return vertexToItsComponentId[vertexV] == vertexToItsComponentId[vertexW];
    }

    // 获取到当前节点v所属的分组
    public int componentIdOf(int vertexV) {
        return vertexToItsComponentId[vertexV];
    }

    // 获取当前图中所有子图的数量
    public int componentAmount() {
        return componentAmount;
    }


    public static void main(String[] args) {
        // 创建图 与 连通分量的对象
        Graph graph = new Graph(new In(args[0]));
        ConnectedComponentsLite dividedGraph = new ConnectedComponentsLite(graph);

        // APIs获取图的性质 - 图中有几个子图
        int componentAmount = dividedGraph.componentAmount();
        StdOut.println(componentAmount + " components.");

        // 打印图中所有的连通分量 - 这需要准备邻接表数组
        // 手段：邻接表数组 aka Bag[]
        Bag<Integer>[] componentIdToComponent;
        componentIdToComponent = new Bag[componentAmount];

        // 初始化components
        for (int currentComponentId = 0; currentComponentId < componentAmount; currentComponentId++) {
            componentIdToComponent[currentComponentId] = new Bag<Integer>();
        }

        // 调用API，为components中的item逐一赋值
        for (int currentVertex = 0; currentVertex < graph.vertexAmount(); currentVertex++) {
            // 把节点v添加到它所属的分组中 - 获取分组id的手段：dividedGraph.ids[顶点]
            componentIdToComponent[dividedGraph.vertexToItsComponentId[currentVertex]].add(currentVertex);
        }

        // 打印每一个分组中的节点
        // M个子图 循环M次
        for (int currentComponentId = 0; currentComponentId < componentAmount; currentComponentId++) {
            // 对于子图中的每一个顶点...
            for (int currentVertex : componentIdToComponent[currentComponentId]) {
                // 打印
                StdOut.print(currentVertex + " ");
            }

            StdOut.println();
        }
    }
}
// 上海 居转户
//