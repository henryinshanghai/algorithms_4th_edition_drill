package com.henry.graph_chapter_04.direction_graph_02.path.dfs.strong_connected_components.tarjan; /******************************************************************************
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

import com.henry.graph_chapter_04.direction_graph_02.graph.Digraph;
import com.henry.graph_chapter_04.direction_graph_02.path.dfs.graph_property.if_two_vertex_accessible.TransitiveClosure;
import com.henry.graph_chapter_04.direction_graph_02.path.dfs.strong_connected_components.kosaraju.KosarajuStrongConnectedComponentsLite;
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
 * {@link KosarajuStrongConnectedComponentsLite} and {@link GabowSCC}.
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class TarjanSCC {

    private boolean[] vertexToIsMarked;        // marked[v] = has v been visited?
    private int[] vertexToComponentId;                // id[v] = id of strong component containing v
    private int[] vertexToMinTraverseIdOfItsAccessibleVertexes;               // low[v] = low number of v
    private int cursorOfPreSequence;                 // preorder number counter
    private int componentAmount;               // number of strongly-connected components

    private Stack<Integer> vertexesStack;


    /**
     * Computes the strong components of the digraph {@code G}.
     *
     * @param digraph the digraph
     */
    public TarjanSCC(Digraph digraph) {
        vertexToIsMarked = new boolean[digraph.getVertexAmount()];
        vertexesStack = new Stack<Integer>();
        vertexToComponentId = new int[digraph.getVertexAmount()];
        vertexToMinTraverseIdOfItsAccessibleVertexes = new int[digraph.getVertexAmount()];

        for (int currentVertex = 0; currentVertex < digraph.getVertexAmount(); currentVertex++) {
            if (isNotMarked(currentVertex)) {
                dfs(digraph, currentVertex);
            }
        }

        // check that id[] gives strong components
        assert check(digraph);
    }

    private boolean isNotMarked(int currentVertex) {
        return !vertexToIsMarked[currentVertex];
    }

    private void dfs(Digraph digraph, int currentVertex) {
        // 标记 搜索路径上的“当前节点” 为 “已访问”
        vertexToIsMarked[currentVertex] = true;
        // 记录当前结点的traverseId / minTraverseIdOfItsAccessibleVertexes
        // 特征：在从结点返回之前，祖先节点的traverseId 总是会小于 后代结点的traverseId
        // 🐖 这里使用同一个变量 来 表示结点的两个变化的量这样
        vertexToMinTraverseIdOfItsAccessibleVertexes[currentVertex] = cursorOfPreSequence++;

        // 把“当前结点” 添加到一个显式的栈中 - 作用：记录所有已经访问过的“当前结点”，并在特定场景下 弹出结点 来 组成SCC。
        vertexesStack.push(currentVertex);

        // 设置一个变量，用于记录 “当前节点”的 所有可达结点（以及 它自己）中的 traverseId值最小的结点的traverseId值
        int minTraverseIdInAccessibleVertexesOfCurrentVertex = vertexToMinTraverseIdOfItsAccessibleVertexes[currentVertex];

        // 遍历 “当前结点”的所有邻居结点(aka 直接可达的子节点)
        for (int currentAdjacentVertex : digraph.adjacentVertexesOf(currentVertex)) {
            // 如果“当前邻居节点”还没有被标记，则：对它递归地执行DFS进行标记
            if (isNotMarked(currentAdjacentVertex)) {
                dfs(digraph, currentAdjacentVertex);
            }

            // #1 按照实际情况 来 更新 minTraverseId的变量
            if (vertexToMinTraverseIdOfItsAccessibleVertexes[currentAdjacentVertex] < minTraverseIdInAccessibleVertexesOfCurrentVertex) {
                minTraverseIdInAccessibleVertexesOfCurrentVertex = vertexToMinTraverseIdOfItsAccessibleVertexes[currentAdjacentVertex];
            }
        }

        // #2 根据traverseId是否被更新 来 确定：#2-1 是 继续收集SCC中的结点 还是 #2-2 开始从栈中弹出结点
        /* #2-1 继续收集SCC中的结点 */
        // 如果 “当前节点”的minTraverseId 被更新，说明 当前节点及其子节点中 存在能够返回 “当前节点的祖先结点”的边（返祖边），
        // 进一步说明 它是SCC中的一个结点，则：更新 “当前节点”的minTraverseId后，当前节点 即“处理完成”，直接处理 路径中的下一个结点
        if (minTraverseIdInAccessibleVertexesOfCurrentVertex < vertexToMinTraverseIdOfItsAccessibleVertexes[currentVertex]) {
            vertexToMinTraverseIdOfItsAccessibleVertexes[currentVertex] = minTraverseIdInAccessibleVertexesOfCurrentVertex;
            return;
        }

        /* #2-2 开始从栈中弹出结点 */
        // 如果 minTraverseId 没有被更新，说明 当前节点（及其子节点）无法返回到 它的祖先结点（返祖边），进一步说明 当前节点是 SCC的“桥接点”，则：从stack中获取到SCC中的所有结点
        int currentVertexInStack;

        // 从栈中弹出当前SCC中的结点 - 手段：不断弹出结点，直到遇到 当前节点
        do {
            currentVertexInStack = vertexesStack.pop();
            vertexToComponentId[currentVertexInStack] = componentAmount;
            // #3 弹出栈中SCC的结点时，把结点的low值 设置为 图中的结点数（来 防止对其他的SCC产生干扰）
            // 作用：避免 在从 所有“相邻结点”中，找到“当前节点”的minTraverseId时，其他SCC中结点的干扰
            vertexToMinTraverseIdOfItsAccessibleVertexes[currentVertexInStack] = digraph.getVertexAmount(); // or anything that larger
        } while (currentVertexInStack != currentVertex);

        componentAmount++;
    }


    /**
     * Returns the number of strong components.
     *
     * @return the number of strong components
     */
    public int componentAmount() {
        return componentAmount;
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
                        scc.vertexToMinTraverseIdOfItsAccessibleVertexes[currentVertex] + ") ");
            }
            StdOut.println();
        }
    }
}
