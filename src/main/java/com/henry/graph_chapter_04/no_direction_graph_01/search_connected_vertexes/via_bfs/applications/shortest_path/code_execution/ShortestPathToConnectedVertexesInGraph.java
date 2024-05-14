package com.henry.graph_chapter_04.no_direction_graph_01.search_connected_vertexes.via_bfs.applications.shortest_path.code_execution;

import com.henry.graph_chapter_04.no_direction_graph_01.represent_graph.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

// 验证：可以使用在图中从指定起点开始进行BFS（广度优先搜索）的方式 来 得到图中“指定起始结点” 到 “其所有可达结点” 对应的“最短路径”
// 广度优先搜索的应用：找到 图中，由 起始顶点 到 指定目的顶点结束的最短路径；
// 原理/BFS的特征：在图中搜索边时，BFS会按照距离 起始顶点的远近 来 递进地遍历顶点。
public class ShortestPathToConnectedVertexesInGraph {
    private boolean[] vertexToIsMarked; // 顶点->顶点是否已经被标记的映射关系
    private int[] terminalVertexToDepartVertex; // 结束顶点->出发顶点的映射关系（用于描述一条边的术语）
    private final int startVertex; // 起始顶点（用于描述一条路径的术语）

    public ShortestPathToConnectedVertexesInGraph(Graph graph, int startVertex) {
        vertexToIsMarked = new boolean[graph.vertexAmount()];
        terminalVertexToDepartVertex = new int[graph.vertexAmount()];
        this.startVertex = startVertex;

        // 使用BFS 来 标记图中 所有由起始顶点可达的顶点
        markVertexesAndRecordEdgesInSPViaBFS(graph, startVertex);
    }

    // BFS - 用于标记图中所有“由起始顶点可达的所有顶点”
    // 🐖 BFS的主要作用就是“标记可达结点”，其他的作用都是由这个主要作用衍生出来的 所以方法名叫做 xxxViaBFS()
    private void markVertexesAndRecordEdgesInSPViaBFS(Graph graph, int startVertex) {
        // #1 把“起始顶点”设置为“已标记”
        vertexToIsMarked[startVertex] = true;
        // 准备一个队列 - 用于记录算法所需要处理的所有顶点
        Queue<Integer> vertexesToProcess = new Queue<>();
        // #2 把“起始顶点”入队到队列中 - BFS算法会 从它开始对图中顶点的标记过程
        vertexesToProcess.enqueue(startVertex);

        // #3 进行BFS的循环，直到图中所有“能够被标记的顶点”都已经被标记了
        while (!vertexesToProcess.isEmpty()) { // 循环结束的条件 - 队列中没有任何元素（aka 图中所有能够处理的节点都已经处理完成）
            // #3-1 出队“待处理的顶点”
            int currentVertex = vertexesToProcess.dequeue();
            // #3-2 处理它
            process(currentVertex, graph, vertexesToProcess);
        }
    }

    // 处理步骤
    private void process(int currentVertex, Graph graph, Queue<Integer> vertexesToProcess) {
        // 对于它的当前邻居顶点...
        for (int currentAdjacentVertex : graph.adjacentVertexesOf(currentVertex)) {
            // 执行固定的SOP三个步骤
            recordPathWhileMarkIt(currentAdjacentVertex, currentVertex, vertexesToProcess);
        }
    }

    private void recordPathWhileMarkIt(int terminalVertex, int departVertex, Queue<Integer> vertexesToProcess) {
        // 如果它(当前邻居顶点) 还没有被标记，说明算法还没有访问过此顶点，则：
        if (isNotMarked(terminalVertex)) {
            // #1 标记它 [基本步骤] 用于记录结点是不是已经被处理过了
            vertexToIsMarked[terminalVertex] = true;
            // #2 记录 结束顶点->出发顶点的边 [核心步骤/BFS基本模板的额外步骤] 用于回溯出 从“起始顶点”到“终止顶点”的完整路径
            terminalVertexToDepartVertex[terminalVertex] = departVertex;
            // #3 把它添加到“待处理的顶点队列“中 - 算法后继会对它做同样的处理
            vertexesToProcess.enqueue(terminalVertex);
        }
    }

    private boolean isNotMarked(int currentAdjacentVertex) {
        return !vertexToIsMarked[currentAdjacentVertex];
    }

    // public APIs
    public boolean doesStartVertexHasPathTo(int passedVertex) {
        return vertexToIsMarked[passedVertex];
    }

    // 🐖 由于BFS算法的特性，所以这里得到的路径是最短路径
    // 方法的实现 与 DepthFirstPaths中的同名方法 完全相同
    public Iterable<Integer> pathFromStartVertexTo(int endVertex) {
        // #1 获取路径之前，先判断是不是存在这条路径
        if(!doesStartVertexHasPathTo(endVertex)) return null;

        // 我们使用 terminalVertexToDepartVertex 这个数组来存储路径，它记录的是“结束顶点->出发顶点”的映射关系。而不是路径中的顶点
        // #2 所以需要想办法让客户端能够 直接获取到 路径中的顶点👇
        // 手段：使用一个栈 来 有序地记录路径中的所有顶点
        Stack<Integer> vertexSequence = new Stack<>();
        // 从数组的最后一个元素从后往前地 获取到路径中的顶点
        for (int backwardsVertexCursor = endVertex; backwardsVertexCursor != startVertex;
             backwardsVertexCursor = terminalVertexToDepartVertex[backwardsVertexCursor]) {
            // 把获取到 路径中的顶点添加到 栈中
            vertexSequence.push(backwardsVertexCursor);
        }

        // #3 把起始结点显式添加到栈中 - 因为上面不会把它添加到栈中
        vertexSequence.push(startVertex);

        return vertexSequence;
    }

    public static void main(String[] args) {
        // #1 创建图 并 指定起始顶点
        Graph graph = new Graph(new In(args[0]));
        int startVertex = Integer.parseInt(args[1]);

        // #2 使用BFS的方式 来 标记图中所有“由起始顶点可达的”所有顶点，并记录下所有“起始顶点->可达结点(作为终止顶点)”的路径
        ShortestPathToConnectedVertexesInGraph markedGraph = new ShortestPathToConnectedVertexesInGraph(graph, startVertex);

        // #3 打印出 所有 起始顶点->可达顶点 的路径
        // 对于图中的当前顶点...
        for (int currentVertex = 0; currentVertex < graph.vertexAmount(); currentVertex++) {
            StdOut.print("shortest path from " + startVertex + " to " + currentVertex + ": ");
            // #3-1 判断它是不是“由起始顶点可达的”
            if (markedGraph.doesStartVertexHasPathTo(currentVertex)) {
                // #3-2 如果是，则 获取到 由起始顶点到达它的路径
                for (int currentVertexInPath : markedGraph.pathFromStartVertexTo(currentVertex)) {
                    // 然后 打印出路径中的结点
                    if (currentVertexInPath == startVertex) StdOut.print(startVertex);
                    else StdOut.print("-" + currentVertexInPath);
                }
            }

            StdOut.println();
        }
    }
}
