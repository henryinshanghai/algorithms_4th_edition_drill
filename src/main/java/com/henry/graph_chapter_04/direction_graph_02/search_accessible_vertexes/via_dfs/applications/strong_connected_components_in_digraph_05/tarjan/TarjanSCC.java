package com.henry.graph_chapter_04.direction_graph_02.search_accessible_vertexes.via_dfs.applications.strong_connected_components_in_digraph_05.tarjan;

/******************************************************************************
 *  Compilation:  javac TarjanSCC.java
 *  Execution:    Java TarjanSCC V E
 *  Dependencies: Digraph.java Stack.java TransitiveClosure.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/42digraph/tinyDG.txt
 *                https://algs4.cs.princeton.edu/42digraph/mediumDG.txt
 *                https://algs4.cs.princeton.edu/42digraph/largeDG.txt
 *
 *  Compute the strongly-connected components of a digraph using
 *  Tarjan's algorithm.
 *
 *  Runs in O(E + V) time.
 *
 *  % java TarjanSCC tinyDG.txt
 *  5 components
 *  1
 *  0 2 3 4 5
 *  9 10 11 12
 *  6 8
 *  7
 *
 ******************************************************************************/

import com.henry.graph_chapter_04.direction_graph_02.represent_digraph.Digraph;
import com.henry.graph_chapter_04.direction_graph_02.search_accessible_vertexes.via_dfs.applications.if_vertex_accessible_from_startVertex_01.extend.if_2vertex_reach_each_other.TransitiveClosure;
import com.henry.graph_chapter_04.direction_graph_02.search_accessible_vertexes.via_dfs.applications.strong_connected_components_in_digraph_05.kosaraju.KosarajuSCCLite;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * The {@code TarjanSCC} class represents a data type for
 * determining the strong components in a digraph.
 * The <em>id</em> operation determines in which strong component
 * a given vertex lies; the <em>areStronglyConnected</em> operation
 * determines whether two vertices are in the same strong component;
 * and the <em>count</em> operation determines the number of strong
 * components.
 * <p>
 * The <em>component identifier</em> of a component is one of the
 * vertices in the strong component: two vertices have the same component
 * identifier if and only if they are in the same strong component.
 * <p>
 * This implementation uses Tarjan's algorithm.
 * The constructor takes &Theta;(<em>V</em> + <em>E</em>) time,
 * where <em>V</em> is the number of vertices and <em>E</em> is the
 * number of edges.
 * Each instance method takes &Theta;(1) time.
 * It uses &Theta;(<em>V</em>) extra space (not including the digraph).
 * For alternative implementations of the same API, see
 * {@link KosarajuSCCLite} and {@link /GabowSCC}.
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
// 验证：使用 Tarjan算法 能够通过一次对图的DFS操作 来 得到有向图中所有的强连通分量
// Tarjan算法原理：
// ① 对于一个SCC来说，进行DFS搜索时，其 入口节点的搜索次序 总是要 小于 其他节点的搜索次序；
// ② 对于 节点的minTraverseId，可以使用 其邻居节点的minTraverseId 来 更新它，从而得到 正确的值；
// ③ 使用一个栈 在DFS调用开始时 不断存储节点，并在 当前节点是SCC的入口节点时，从中弹出节点 来 得到当前SCC的节点。
public class TarjanSCC {

    private boolean[] vertexToIsMarked;        // 节点 -> 节点是否已经被访问
    private int[] vertexToComponentId;                // 节点 -> 节点所属的强连通分量的id

    // Tarjan算法 为 “图中的每个结点” 所维护的2个变量
    private int[] vertexToItsTraverseId;    // 结点 -> 结点的traverseId
    private int[] vertexToItsMinTraverseId;  //  结点 -> 结点及其所有可达结点中的minTraverseId vertexToMinTraverseIdOfItsAccessibleVertexes
    /* 隐藏变量 vertexToItsCompleteOrder 结点 -> 结点的“完成次序” */

    private int counterOfPreSequence;                 // 前序序列的游标指针
    private int SCCAmount;               // 有向图中 强连通分量的数量

    private Stack<Integer> accessedVertexesStack; // 由已访问的节点 所构成的栈??


    /**
     * Computes the strong components of the digraph {@code G}.
     *
     * @param digraph the digraph
     */
    public TarjanSCC(Digraph digraph) {
        vertexToIsMarked = new boolean[digraph.getVertexAmount()];
        accessedVertexesStack = new Stack<Integer>();
        vertexToComponentId = new int[digraph.getVertexAmount()];
        vertexToItsTraverseId = new int[digraph.getVertexAmount()];
        vertexToItsMinTraverseId = new int[digraph.getVertexAmount()];

        // 对于有向图中的当前结点...
        int serialNumber = 1;
        for (int currentVertex = 0; currentVertex < digraph.getVertexAmount(); currentVertex++) {
            // 如果结点还没有被标记...
            if (isNotMarked(currentVertex)) {
                System.out.println("== 第" + (serialNumber++) + "次调用DFS，" + "当前执行DFS的节点：" + currentVertex + " ==");
                // 则：标记结点，并 更新“结点的minTraverseId的值” 来 决定 是否继续查找 SCC中的结点
                markVertexAndUpdateVertexesMinTraverseIdToDecideSCCViaDFS(digraph, currentVertex);
            }
        }

        // check that id[] gives strong components
        assert check(digraph);
    }

    private boolean isNotMarked(int currentVertex) {
        return !vertexToIsMarked[currentVertex];
    }

    private void markVertexAndUpdateVertexesMinTraverseIdToDecideSCCViaDFS(Digraph digraph, int currentVertex) {
        // DFS标准操作：标记 搜索路径上的“当前节点” 为 “已访问”
        flag(currentVertex);

        /* Tarjan算法的准备操作👇 */
        setup(currentVertex);

        // #1 设置一个变量，用于记录 “当前节点”的 所有可达结点（以及 它自己）中的 最小的traverseId - 初始值 设置为 “当前结点自己的traverseId”
        int minTraverseIdOfCurrentVertex = vertexToItsMinTraverseId[currentVertex];

        // 遍历 “当前结点”的所有邻居结点(aka 直接可达的子节点)
        for (int currentAdjacentVertex : digraph.adjacentVertexesOf(currentVertex)) {
            // 如果“当前邻居节点” 还没有被标记...
            if (isNotMarked(currentAdjacentVertex)) {
                System.out.println("+ 在当前节点" + currentVertex + "的当前邻居节点" + currentAdjacentVertex + "上进行DFS递归调用：DFS(" + currentAdjacentVertex +")开始执行 +");
                // 则：对它递归地执行DFS进行标记 - DFS的标准操作
                markVertexAndUpdateVertexesMinTraverseIdToDecideSCCViaDFS(digraph, currentAdjacentVertex);
                System.out.println("= DFS(" + currentAdjacentVertex + ")执行完成 =");
            }

            /* Tarjan算法的实际操作👇 */
            // #2 在 DFS返回 后，按照实际情况 使用 “当前邻居结点”的minTraverseId 来 尝试更新 minTraverseIdOfCurrentVertex变量的值
            System.out.println("- 尝试 使用当前邻居节点" + currentAdjacentVertex + "的minTraverseId(" + vertexToItsMinTraverseId[currentAdjacentVertex]
                    +  ") 来 对当前节点" + currentVertex + "的minTraverseId(" + minTraverseIdOfCurrentVertex + ")进行更新 -");
            minTraverseIdOfCurrentVertex = update(minTraverseIdOfCurrentVertex, currentAdjacentVertex);
        }

        // #3 根据 minTraverseId是否被更新 来 决定：① 是 继续收集SCC中的结点 还是 ② 开始从栈中弹出SCC的结点
        /* ① 继续收集SCC中的结点 */
        // 如果 “当前节点”的minTraverseId 被更新，说明 当前节点及其子节点中 存在有 能够返回到“当前节点的祖先结点”的边（返祖边），
        // 进一步说明 它是SCC中的一个结点，则：在 更新 “当前节点”的minTraverseId 后，当前节点 即“处理完成”，直接return 以 处理 路径中的下一个结点
        if (isAnIntermediateSCCVertex(currentVertex, minTraverseIdOfCurrentVertex)) return;

        /* ② 开始 从栈中弹出 SCC的结点 */
        // 如果 minTraverseId 没有被更新，说明 当前节点（及其子节点）无法返回到 它的祖先结点（返祖边），进一步说明 当前节点是 SCC的“入口结点”
        // 则：从stack中 获取到 SCC中的所有结点
        collectVertexesInCurrentSCC(digraph, currentVertex);

        SCCAmount++;
        System.out.println("当前SCC的所有节点 从栈中弹出后，SCCAmount = " + SCCAmount);
        System.out.println();
    }

    private boolean isAnIntermediateSCCVertex(int currentVertex, int minTraverseIdOfCurrentVertex) {
        // 如果 当前节点的minTraverseId 比起 其初始值 更小，说明 它存在有指向其祖先节点的边(这是它变小的原因)，
        if (minTraverseIdOfCurrentVertex < vertexToItsMinTraverseId[currentVertex]) {
            System.out.println("~ 当前节点" + currentVertex + "的minTraverseId(原始值为：" + vertexToItsMinTraverseId[currentVertex] + ")，" +
                    "在遍历其所有直接邻居节点后变小了(变化后的值为：" + minTraverseIdOfCurrentVertex + ")，因此 它是当前SCC的一个中间节点。则：继续收集当前SCC中的其他节点 ~");
            // 因此 它是SCC的一个中间节点，则：
            // ① 更新 节点的minTraverseId；② 返回true 表示 它是SCC的中间节点
            vertexToItsMinTraverseId[currentVertex] = minTraverseIdOfCurrentVertex;

            printVertexesInStack(accessedVertexesStack);

            return true;
        }

        System.out.println("~ 当前节点 " + currentVertex + " 的 minTraverseId没有变小，因此它是 当前SCC的入口节点。则：开始获取当前SCC的节点集合 ✌");
        return false;
    }

    int callTime = 1;
    private void collectVertexesInCurrentSCC(Digraph digraph, int currentVertex) {
        System.out.println();
        System.out.println("# 第" + (callTime++) + "次调用 收集SCC中节点的方法，由节点" + currentVertex + "触发 #");
        printVertexesInStack(accessedVertexesStack);

        int currentVertexInStack;

        /* 从栈中弹出 当前SCC中的结点，并 为其归组 */
        // 手段：不断 弹出结点，直到遇到 当前节点
        do {
            // 弹出栈顶节点
            currentVertexInStack = accessedVertexesStack.pop();
            // 为 栈顶节点 归组
            vertexToComponentId[currentVertexInStack] = SCCAmount;
            // #3 弹出 栈中SCC的结点 时，把 结点的low值 设置为 图中的结点数（来 防止 对其他的SCC产生干扰）
            // 作用：避免 在从 所有“相邻结点”中，找到“当前节点”的minTraverseId时，其他SCC中结点的干扰
            vertexToItsMinTraverseId[currentVertexInStack] = digraph.getVertexAmount(); // or anything that larger
        } while (currentVertexInStack != currentVertex);
    }

    private void printVertexesInStack(Stack<Integer> accessedVertexesStack) {
        System.out.println("当前栈中的元素集合（栈顶->栈底）为：");
        for (Integer currentVertex : accessedVertexesStack) {
            System.out.print(currentVertex  + " ");
        }
        System.out.println();
    }

    private int update(int minTraverseIdOfCurrentVertex, int currentAdjacentVertex) {
        // 🐖 这里比较的是 邻居节点的minTraverseId，而不是traverseId - 这样才能得到正确的 minTraverseId
        // 如果 当前邻居节点 来自于 一个已经遍历完成的SCC，则：其traverseId可能已经被更新为一个较小的值。而这里我们想要从当前SCC重新计算traverseId
        // 为了避免干扰，在上一个SCC弹出节点后，需要把节点的traverseId设置为一个较大值
        if (vertexToItsMinTraverseId[currentAdjacentVertex] < minTraverseIdOfCurrentVertex) {
            minTraverseIdOfCurrentVertex = vertexToItsMinTraverseId[currentAdjacentVertex];
            System.out.println("* 执行了更新,更新后的minTraverseId为：" + minTraverseIdOfCurrentVertex + " *");
        }

        return minTraverseIdOfCurrentVertex;
    }

    private void setup(int currentVertex) {
        // #1 初始化 当前结点的traverseId && minTraverseIdOfItsAccessibleVertexes
        // 特征：在 从结点返回之前，“祖先节点”的traverseId 总是会小于 后代结点的traverseId
        // 🐖 这里使用 同一个变量 来 表示“结点的所有可达结点中”最小的traverseId
        vertexToItsTraverseId[currentVertex] = counterOfPreSequence++;
        vertexToItsMinTraverseId[currentVertex] = vertexToItsTraverseId[currentVertex]; // 初始化时，两者的值相同

        // #2 把“当前结点” 添加到 一个显式的栈中 - 作用：记录所有 已经访问过的“当前结点”，并 在特定场景下 弹出结点 来 组成SCC。
        System.out.println("在DFS(" + currentVertex + ")开始执行时, 把当前节点" + currentVertex + "入栈");
        accessedVertexesStack.push(currentVertex);
    }

    private void flag(int currentVertex) {
        vertexToIsMarked[currentVertex] = true;
    }


    /**
     * Returns the number of strong components.
     *
     * @return the number of strong components
     */
    public int componentAmount() {
        return SCCAmount;
    }


    /**
     * Are vertices {@code v} and {@code w} in the same strong component?
     *
     * @param vertexV one vertex
     * @param vertexW the other vertex
     * @return {@code true} if vertices {@code v} and {@code w} are in the same
     * strong component, and {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     * @throws IllegalArgumentException unless {@code 0 <= w < V}
     */
    public boolean stronglyConnected(int vertexV, int vertexW) {
        validateVertex(vertexV);
        validateVertex(vertexW);
        return vertexToComponentId[vertexV] == vertexToComponentId[vertexW];
    }

    /**
     * Returns the component id of the strong component containing vertex {@code v}.
     *
     * @param vertexV the vertex
     * @return the component id of the strong component containing vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int componentId(int vertexV) {
        validateVertex(vertexV);
        return vertexToComponentId[vertexV];
    }

    // does the id[] array contain the strongly connected components?
    private boolean check(Digraph digraph) {
        TransitiveClosure markedMatrix = new TransitiveClosure(digraph);

        for (int currentVertexV = 0; currentVertexV < digraph.getVertexAmount(); currentVertexV++) {
            for (int currentVertexW = 0; currentVertexW < digraph.getVertexAmount(); currentVertexW++) {
                if (stronglyConnected(currentVertexV, currentVertexW) !=
                        (markedMatrix.reachable(currentVertexV, currentVertexW) && markedMatrix.reachable(currentVertexW, currentVertexV)))
                    return false;
            }
        }
        return true;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int vertexV) {
        int vertexAmount = vertexToIsMarked.length;
        if (vertexV < 0 || vertexV >= vertexAmount)
            throw new IllegalArgumentException("vertex " + vertexV + " is not between 0 and " + (vertexAmount - 1));
    }

    /**
     * Unit tests the {@code TarjanSCC} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        // 文件名 -> 文件流 -> 有向图对象 -> SCC对象
        In in = new In(args[0]);
        Digraph digraph = new Digraph(in);
        TarjanSCC scc = new TarjanSCC(digraph);

        // 有向图中 强连通分量的数量
        int componentAmount = scc.componentAmount();
        StdOut.println(componentAmount + " components");

        // 每一个强连通分量中的 节点列表
        Queue<Integer>[] components = (Queue<Integer>[]) new Queue[componentAmount];
        for (int currentComponentId = 0; currentComponentId < componentAmount; currentComponentId++) {
            // 强连通分量的id -> 强连通分量中的节点
            components[currentComponentId] = new Queue<Integer>();
        }

        // 对于每一个节点...
        for (int currentVertex = 0; currentVertex < digraph.getVertexAmount(); currentVertex++) {
            // 获取到 节点所属的 强连通分量id
            int componentIdOfCurrentVertex = scc.componentId(currentVertex);
            // 把节点 添加到 正确的强连通分量中
            components[componentIdOfCurrentVertex].enqueue(currentVertex);
        }

        // 对于每一个强连通分量...
        for (int currentComponentId = 0; currentComponentId < componentAmount; currentComponentId++) {
            // 对于 当前强连通分量中的每一个节点...
            for (int currentVertex : components[currentComponentId]) {
                // 打印 节点 -> 该节点(及其所有可达节点)的最小traverseId
                StdOut.print(currentVertex + " " + "(minTraverseId:" +
                        scc.vertexToItsMinTraverseId[currentVertex] + ") ");
            }
            StdOut.println();
        }
    }
}
