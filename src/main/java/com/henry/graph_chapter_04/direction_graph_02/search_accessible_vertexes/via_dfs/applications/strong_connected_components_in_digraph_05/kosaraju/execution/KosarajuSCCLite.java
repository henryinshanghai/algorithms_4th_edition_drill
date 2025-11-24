package com.henry.graph_chapter_04.direction_graph_02.search_accessible_vertexes.via_dfs.applications.strong_connected_components_in_digraph_05.kosaraju.execution;

import edu.princeton.cs.algs4.DepthFirstOrder;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

// 结论：使用Kosaraju算法（按照反向图的逆后序序列来对结点执行DFS），其构造函数中的每一次dfs()递归调用，所标记的结点 都会在“同一个强连通分量”之中
// 原理：#1 反向图 与 原始图 具有完全相同的强连通分量； #2 ??
// 算法步骤：#1 获取 原始有向图的反向图; #2 获取到 反向图G'的 逆后序遍历的结点序列; #3 顺序遍历#2序列中的结点，使用DFS 对结点进行标记&分组；
// 手段：使用一个名叫 vertexToItsComponentId的数组 来 指明“结点所属的强连通分量的id”（使用componentAmount来赋值）
public class KosarajuSCCLite {

    private boolean[] vertexToIsMarked; // 已经访问过的顶点
    private int[] vertexToItsComponentId; // 强连通分量的标识符
    private int componentAmount; // 强连通分量的数量 - 用于 作为强连通分量的id

    public KosarajuSCCLite(Digraph digraph) {
        vertexToIsMarked = new boolean[digraph.V()];
        vertexToItsComponentId = new int[digraph.V()];

        // #1 获取到 有向图的反向图 G'
        Digraph reversedDigraph = digraph.reverse();
        System.out.println("~~~ 获取到 原始图" + "的 反向图" + reversedDigraph.toString() + " ~~~");

        // #2 获取到 该反向图的 结点遍历所得到的结点序列 - PreOrder, PostOrder, ReversedPostOrder
        DepthFirstOrder vertexesSequences = new DepthFirstOrder(reversedDigraph);
        System.out.println("!!! 获取到 反向图中 节点的各种遍历方式的结果序列。我们需要的序列是 逆后序序列:" + printVertexSeq(vertexesSequences.post()) + " !!!");

        /* #3 按照特定的顶点序列 来 在图中执行DFS */
        // ① 获取到 反向图的“逆后序遍历序列(ReversedPostOrder)”
        // ② 然后 在“原始有向图”中，顺序遍历 “序列中的结点” 来 执行DFS
        // 🐖 “逆后序遍历序列”的作用 - 用于确定 遍历“有向图中结点”的顺序 VS. DFS中标准的结点遍历方式（自然数顺序）
        System.out.println("@@@ 对 逆后序序列中的节点，顺序执行DFS @@@");
        for (Integer currentVertex : vertexesSequences.post()) {
            if (isNotMarked(currentVertex)) {
                // 标记当前结点 & 为其指定其所属的componentId
                markVertexesAndCollectToComponentViaDFS(digraph, currentVertex);
                componentAmount++;
            }
        }
    }

    private String printVertexSeq(Iterable<Integer> vertexSequence) {
        StringBuilder sb = new StringBuilder();
        for (Integer currentVertex : vertexSequence) {
            sb.append(currentVertex + ", ");
        }

        return sb.substring(0, sb.length());
    }

    private boolean isNotMarked(Integer currentVertex) {
        return !vertexToIsMarked[currentVertex];
    }

    // 标准的DFS流程
    private void markVertexesAndCollectToComponentViaDFS(Digraph digraph, Integer currentVertex) {
        System.out.println("### 1 在图中，以 当前顶点" + currentVertex + "作为 起始顶点的DFS过程 开始 ###");

        // 标记 当前节点
        vertexToIsMarked[currentVertex] = true;
        // 为 当前节点 指定 正确的分组ID
        vertexToItsComponentId[currentVertex] = componentAmount;

        // 对于 其所有的可达节点...
        for (Integer currentAdjacentVertex : digraph.adj(currentVertex)) {
            // 如果 该节点 尚未被标记，则：
            if (isNotMarked(currentAdjacentVertex)) {
                // 继续递归地 以该节点作为起始顶点 在图中执行DFS
                System.out.println("$$$ 1 以 该节点" + currentVertex + "的邻居节点" + currentAdjacentVertex + " 作为起始顶点，继续执行DFS $$$");
                markVertexesAndCollectToComponentViaDFS(digraph, currentAdjacentVertex);
                System.out.println("$$$ 2 以 该节点" + currentVertex + "的邻居节点" + currentAdjacentVertex + " 作为起始顶点的DFS 结束并返回 $$$");
            }
        }
        System.out.println("### 2 在图中，以 当前顶点" + currentVertex + "作为 起始顶点的DFS过程 结束 ###");
    }

    public boolean stronglyConnected(int vertexV, int vertexW) {
        return vertexToItsComponentId[vertexV] == vertexToItsComponentId[vertexW];
    }

    public int componentIdOf(int vertexV) {
        return vertexToItsComponentId[vertexV];
    }

    public int getComponentAmount() {
        return componentAmount;
    }

    /**
     * 当前数据类型的 单元测试
     * 使用 构造器方法 + APIs 来 得到 有向图的一些复杂性质
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph digraph = new Digraph(in);
        // 对 有向图 执行 Kosaraju算法
        KosarajuSCCLite vertexAssignedComponentId = new KosarajuSCCLite(digraph);

        /* 获取 图的一些性质 */
        // #1 获取图中 强连通分量的个数
        int componentAmount = vertexAssignedComponentId.getComponentAmount();
        StdOut.println(componentAmount + " strong components");

        /* #2 打印 每一个强连通分量 中的所有节点 */
        // ① 使用集合 来 收集每个强连通分量中的结点
        Queue<Integer>[] components = (Queue<Integer>[]) new Queue[componentAmount];
        for (int currentComponentId = 0; currentComponentId < componentAmount; currentComponentId++) {
            components[currentComponentId] = new Queue<Integer>();
        }
        for (int currentVertex = 0; currentVertex < digraph.V(); currentVertex++) {
            int componentIdOfVertex = vertexAssignedComponentId.componentIdOf(currentVertex);
            components[componentIdOfVertex].enqueue(currentVertex);
        }

        // ② 打印 每一个强连通分量中的结点
        for (int currentComponentId = 0; currentComponentId < componentAmount; currentComponentId++) {
            Queue<Integer> currentComponent = components[currentComponentId];
            // 打印 当前强连通分量中的 所有顶点
            for (int currentVertex : currentComponent) {
                StdOut.print(currentVertex + " ");
            }
            StdOut.println();
        }
    }
}
