package com.henry.graph_chapter_04.no_direction_graph_01.search_connected_vertexes.via_dfs.applications.connected_components_in_graph.code_execution;

import com.henry.graph_chapter_04.no_direction_graph_01.represent_graph.Graph;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

// 验证：可以使用在图中从指定起点开始进行DFS（递归地标记结点）的方式 来 获取到图中存在几个连通分量，以及各个连通分量中的结点
public class ConnectedComponentsLite {
    // 用于 记录节点 => 它有没有被标记过 的映射
    private boolean[] vertexToIsMarked;
    // 用于 记录节点 => 节点所属的分组/连通分量ID 的映射
    private int[] vertexToItsComponentId; // 这里的componentId 使用 componentAmount来设置
    // 记录component的数量   应用：可以用来 设置组的id（因为它是按照自然数序列递增的）
    private int componentAmount;

    // 构造器  用于创建对象
    public ConnectedComponentsLite(Graph graph) {
        // 初始化成员变量 count的初始值为0 已经初始化完成
        vertexToIsMarked = new boolean[graph.vertexAmount()];
        vertexToItsComponentId = new int[graph.vertexAmount()];

        // 对于 图中的每一个顶点
        for (int currentVertex = 0; currentVertex < graph.vertexAmount(); currentVertex++) {
            // 如果 当前顶点 还 没有标记过...
            if (isNotMarked(currentVertex)) {
                // 找到 当前节点所连通的所有顶点，然后成组
                markVertexAndAssignItsComponentIdViaDFS(graph, currentVertex);
                // 得到一个组 之后，把groupNum+1
                componentAmount++;
            }
        }
    }

    private boolean isNotMarked(int currentVertex) {
        return !vertexToIsMarked[currentVertex];
    }

    /**
     * 把 图G中 当前顶点v 所连通的所有顶点 标记&成组
     * 手段：使用递归 对可达节点做DFS
     * @param graph 指定的图
     * @param currentVertex 指定的顶点
     */
    private void markVertexAndAssignItsComponentIdViaDFS(Graph graph, int currentVertex) {
        // 标记 当前节点
        vertexToIsMarked[currentVertex] = true;
        // 为 当前节点 添加组名  当前节点所属的分组/子图/连通分量的ID为count - 第0组、第1组...
        vertexToItsComponentId[currentVertex] = componentAmount;

        // 对 图中”当前节点“的所有 未被标记的相邻节点 递归地执行：标记 + 添加组名
        for (int currentAdjacentVertex : graph.adjacentVertexesOf(currentVertex)) {
            if (isNotMarked(currentAdjacentVertex)) {
                markVertexAndAssignItsComponentIdViaDFS(graph, currentAdjacentVertex);
            }
        }
    }

    // APIs
    // 判断 顶点v 与 顶点w 是否相连通(“连通” 是 无向图中的一个概念)
    public boolean isConnectedBetween(int vertexV, int vertexW) {
        // 手段：判断 两个顶点所属的组id 是否相同
        return vertexToItsComponentId[vertexV] == vertexToItsComponentId[vertexW];
    }

    // 获取到 当前节点v 所属的分组
    public int componentIdOf(int vertexV) {
        return vertexToItsComponentId[vertexV];
    }

    // 获取 当前图中 所有子图的数量
    public int componentAmount() {
        return componentAmount;
    }


    public static void main(String[] args) {
        // #1 创建图 与 连通分量的对象
        Graph graph = new Graph(new In(args[0]));
        // 通过 类的构造方法 来 完成此任务(统计 图中的连通分量数量)
        ConnectedComponentsLite graphComponentsInfo = new ConnectedComponentsLite(graph);

        /* #2 使用APIs 获取图的如下性质👇 */
        // ① 图中有几个子图
        int componentAmount = graphComponentsInfo.componentAmount();
        StdOut.println(componentAmount + " components.");

        // ② 打印 图中所有的连通分量 - 这需要 准备 邻接表数组
        // 获取到 图中存在的 所有连通分量的数组
        Bag<Integer>[] componentIdToComponent = getComponentsIn(graph, graphComponentsInfo);

        // 打印 每一个连通分量中的顶点
        printVertexesInEachComponent(componentIdToComponent);
    }

    /**
     * 打印 图中 每一个连通分量中的节点
     *
     * @param componentIdToComponent 图中的所有连通分量 所组成的数组
     */
    private static void printVertexesInEachComponent(Bag<Integer>[] componentIdToComponent) {
        // 对于当前子图/连通分量...
        for (int currentComponentId = 0; currentComponentId < componentIdToComponent.length; currentComponentId++) {
            // 获取到 该连通分量
            Bag<Integer> currentComponent = componentIdToComponent[currentComponentId];
            // 对于 该连通分量中的当前顶点...
            for (int currentVertex : currentComponent) {
                // 打印它（不换行）
                StdOut.print(currentVertex + " ");
            }

            // 当前连通分量中的所有节点 打印完成后，换行
            StdOut.println();
        }
    }

    /**
     * 把 指定图 中的 所有顶点，添加到 其所属的连通分量中，并 返回所有的连通分量
     *
     * @param graph               指定的图
     * @param graphsComponentInfo 用于 回答 该图的连通分量相关信息的 对象
     * @return 以数组形式 返回 所有的连通分量
     */
    private static Bag<Integer>[] getComponentsIn(Graph graph, ConnectedComponentsLite graphsComponentInfo) {
        // #1 初始化 以连通分量（具体形式是一个Bag对象）作为元素的数组
        Bag<Integer>[] componentIdToComponent = initComponentsArr(graphsComponentInfo);

        /* #2 调用需要的API，为 components中的item 逐一赋值 */
        // 对于 当前顶点，把 该节点 添加到 它所属的分组 中👇
        for (int currentVertex = 0; currentVertex < graph.vertexAmount(); currentVertex++) {
            // ① 先 获取到 该顶点 所属的分组
            int componentIdOfVertex = graphsComponentInfo.vertexToItsComponentId[currentVertex];
            // ② 再 把 它 添加到 对应分组中
            componentIdToComponent[componentIdOfVertex].add(currentVertex);
        }

        // #3 返回 连通分量的数组
        return componentIdToComponent;
    }


    private static Bag<Integer>[] initComponentsArr(ConnectedComponentsLite dividedGraph) {
        // #1 初始化 componentId[]的大小(元素数量)
        int componentAmount = dividedGraph.componentAmount();
        Bag<Integer>[] componentIdToComponent = new Bag[componentAmount];

        // #2 实例化 components元素
        for (int currentComponentId = 0; currentComponentId < componentAmount; currentComponentId++) {
            componentIdToComponent[currentComponentId] = new Bag<Integer>();
        }

        return componentIdToComponent;
    }
}
// 上海 居转户
//