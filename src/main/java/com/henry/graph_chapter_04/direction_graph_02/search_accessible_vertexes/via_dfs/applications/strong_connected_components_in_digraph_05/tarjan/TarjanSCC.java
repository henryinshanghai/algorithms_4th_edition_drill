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
import com.henry.graph_chapter_04.direction_graph_02.search_accessible_vertexes.via_dfs.applications.if_vertex_accessible_from_startVertex_01.extend.if_two_vertex_access_each_other.TransitiveClosure;
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
// 结论：使用 Tarjan算法，其构造函数中的每一次dfs()递归调用，所标记的结点 都会在“同一个强连通分量”之中??
public class TarjanSCC {

    private boolean[] vertexToIsMarked;        // marked[v] = has v been visited?
    private int[] vertexToComponentId;                // id[v] = id of strong component containing v

    // Tarjan算法 为图中的每个结点所维护的2个变量
    private int[] vertexToItsTraverseId;    // 结点->结点的traverseId
    private int[] vertexToItsMinTraverseId;  // vertexToMinTraverseIdOfItsAccessibleVertexes 结点->结点及其所有可达结点中的minTraverseId
    /* 隐藏变量 vertexToItsCompleteOrder 结点->结点的“完成次序” */

    private int counterOfPreSequence;                 // preorder number counter
    private int SCCAmount;               // number of strongly-connected components

    private Stack<Integer> accessedVertexesStack;


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
        for (int currentVertex = 0; currentVertex < digraph.getVertexAmount(); currentVertex++) {
            // 如果结点还没有被标记...
            if (isNotMarked(currentVertex)) {
                // 则：标记结点，并更新结点的minTraverseId的值 来 决定是否继续查找SCC中的结点
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

        // #3 设置一个变量，用于记录 “当前节点”的 所有可达结点（以及 它自己）中的 最小的traverseId - 初始值设置为“当前结点自己的traverseId”
        int minTraverseIdOfCurrentVertex = vertexToItsMinTraverseId[currentVertex];

        // 遍历 “当前结点”的所有邻居结点(aka 直接可达的子节点)
        for (int currentAdjacentVertex : digraph.adjacentVertexesOf(currentVertex)) {
            // DFS的标准操作：如果“当前邻居节点”还没有被标记，则：对它递归地执行DFS进行标记
            if (isNotMarked(currentAdjacentVertex)) {
                markVertexAndUpdateVertexesMinTraverseIdToDecideSCCViaDFS(digraph, currentAdjacentVertex);
            }

            /* Tarjan算法的实际操作👇 */
            // #1 在DFS返回后，按照实际情况 使用“当前邻居结点”的minTraverseId 来 尝试更新minTraverseIdOfCurrentVertex变量的值
            minTraverseIdOfCurrentVertex = update(minTraverseIdOfCurrentVertex, currentAdjacentVertex);
        }

        // #2 根据traverseId是否被更新 来 决定：① 是 继续收集SCC中的结点 还是 ② 开始从栈中弹出SCC的结点
        /* ① 继续收集SCC中的结点 */
        // 如果 “当前节点”的minTraverseId 被更新，说明 当前节点及其子节点中 存在能够返回 “当前节点的祖先结点”的边（返祖边），
        // 进一步说明 它是SCC中的一个结点，则：更新 “当前节点”的minTraverseId后，当前节点 即“处理完成”，直接处理 路径中的下一个结点
        if (isAnSCCVertex(currentVertex, minTraverseIdOfCurrentVertex)) return;

        /* ② 开始从栈中弹出SCC的结点 */
        // 如果 minTraverseId 没有被更新，说明 当前节点（及其子节点）无法返回到 它的祖先结点（返祖边），进一步说明 当前节点是 SCC的“桥接结点”
        // 则：从stack中获取到SCC中的所有结点
        collectSCCVertexes(digraph, currentVertex);

        SCCAmount++;
    }

    private boolean isAnSCCVertex(int currentVertex, int minTraverseIdOfCurrentVertex) {
        if (minTraverseIdOfCurrentVertex < vertexToItsMinTraverseId[currentVertex]) {
            vertexToItsMinTraverseId[currentVertex] = minTraverseIdOfCurrentVertex;
            return true;
        }
        return false;
    }

    private void collectSCCVertexes(Digraph digraph, int currentVertex) {
        int currentVertexInStack;

        // 从栈中弹出当前SCC中的结点 - 手段：不断弹出结点，直到遇到 当前节点
        do {
            currentVertexInStack = accessedVertexesStack.pop();
            vertexToComponentId[currentVertexInStack] = SCCAmount;
            // #3 弹出栈中SCC的结点时，把结点的low值 设置为 图中的结点数（来 防止对其他的SCC产生干扰）
            // 作用：避免 在从 所有“相邻结点”中，找到“当前节点”的minTraverseId时，其他SCC中结点的干扰
            vertexToItsMinTraverseId[currentVertexInStack] = digraph.getVertexAmount(); // or anything that larger
        } while (currentVertexInStack != currentVertex);
    }

    private int update(int minTraverseIdOfCurrentVertex, int currentAdjacentVertex) {
        // 🐖 这里比较的是 邻居节点的minTraverseId，而不是traverseId - 这样才能得到正确的 minTraverseId
        if (vertexToItsMinTraverseId[currentAdjacentVertex] < minTraverseIdOfCurrentVertex) {
            minTraverseIdOfCurrentVertex = vertexToItsMinTraverseId[currentAdjacentVertex];
        }
        return minTraverseIdOfCurrentVertex;
    }

    private void setup(int currentVertex) {
        // #1 初始化当前结点的traverseId / minTraverseIdOfItsAccessibleVertexes
        // 特征：在从结点返回之前，祖先节点的traverseId 总是会小于 后代结点的traverseId
        // 🐖 这里使用同一个变量 来 表示“结点的所有可达结点中”最小的traverseId
        vertexToItsTraverseId[currentVertex] = counterOfPreSequence++;
        vertexToItsMinTraverseId[currentVertex] = vertexToItsTraverseId[currentVertex]; // 初始化时，两者的值相同

        // #2 把“当前结点” 添加到一个显式的栈中 - 作用：记录所有已经访问过的“当前结点”，并在特定场景下 弹出结点 来 组成SCC。
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
        In in = new In(args[0]);
        Digraph digraph = new Digraph(in);
        TarjanSCC scc = new TarjanSCC(digraph);

        // number of connected components
        int componentAmount = scc.componentAmount();
        StdOut.println(componentAmount + " components");

        // compute list of vertices in each strong component
        Queue<Integer>[] components = (Queue<Integer>[]) new Queue[componentAmount];
        for (int currentComponentId = 0; currentComponentId < componentAmount; currentComponentId++) {
            components[currentComponentId] = new Queue<Integer>();
        }

        for (int currentVertex = 0; currentVertex < digraph.getVertexAmount(); currentVertex++) {
            int componentIdOfCurrentVertex = scc.componentId(currentVertex);
            components[componentIdOfCurrentVertex].enqueue(currentVertex);
        }

        // print results
        for (int currentComponentId = 0; currentComponentId < componentAmount; currentComponentId++) {
            for (int currentVertex : components[currentComponentId]) {
                StdOut.print(currentVertex + " " + "(minTraverseId:" +
                        scc.vertexToItsMinTraverseId[currentVertex] + ") ");
            }
            StdOut.println();
        }
    }
}
