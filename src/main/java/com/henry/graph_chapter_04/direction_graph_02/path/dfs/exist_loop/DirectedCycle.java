package com.henry.graph_chapter_04.direction_graph_02.path.dfs.exist_loop;

/******************************************************************************
 *  Compilation:  javac DirectedCycle.java
 *  Execution:    java DirectedCycle input.txt
 *  Dependencies: Digraph.java Stack.java StdOut.java In.java
 *  Data files:   https://algs4.cs.princeton.edu/42digraph/tinyDG.txt
 *                https://algs4.cs.princeton.edu/42digraph/tinyDAG.txt
 *
 *  Finds a directed cycle in a digraph.
 *
 *  % java DirectedCycle tinyDG.txt
 *  Directed cycle: 3 5 4 3
 *
 *  %  java DirectedCycle tinyDAG.txt
 *  No directed cycle
 *
 ******************************************************************************/

import com.henry.graph_chapter_04.direction_graph_02.graph.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Topological;

/**
 * The {@code DirectedCycle} class represents a data type for
 * determining whether a digraph has a directed cycle.
 * The <em>hasCycle</em> operation determines whether the digraph has
 * a simple directed cycle and, if so, the <em>cycle</em> operation
 * returns one.
 * <p>
 * This implementation uses depth-first search.
 * The constructor takes &Theta;(<em>V</em> + <em>E</em>) time in the worst
 * case, where <em>V</em> is the number of vertices and <em>E</em> is
 * the number of edges.
 * Each instance method takes &Theta;(1) time.
 * It uses &Theta;(<em>V</em>) extra space (not including the digraph).
 * <p>
 * See {@link Topological} to compute a topological order if the
 * digraph is acyclic.
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
// 结论#1：在有向图的DFS算法中，能够得到 “有向图中是否存在有环”的答案。
// 手段：使用一个名叫 vertexToIsBelongToCurrentPath的数组 来 记录“结点是不是属于当前路径”
// 原理：在使用DFS对结点进行标记与查找时，如果在“当前查找路径”中，遇到了“已经被标记的结点”，则：说明有向图中存在有环
// 结论#2：在有向图的DFS算法中，能够“获取到环中的所有结点”。
// 手段：使用名为 terminalVertexToDepartVertex的数组，指定 正确的 backwardsVertexCursor 与 startVertex 就能用for循环，把所有结点收集到栈集合中
public class DirectedCycle {
    private boolean[] vertexToIsMarked;        // marked[v] = has vertex v been marked?
    private int[] terminalVertexToDepartVertex;            // edgeTo[v] = previous vertex on path to v
    private boolean[] vertexToIsBelongToCurrentPath;       // onStack[v] = is vertex on the stack?
    private Stack<Integer> vertexesInCycle;    // directed cycle (or null if no such cycle)

    /**
     * Determines whether the digraph {@code G} has a directed cycle and, if so,
     * finds such a cycle.
     *
     * @param digraph the digraph
     */
    public DirectedCycle(Digraph digraph) {
        vertexToIsMarked = new boolean[digraph.getVertexAmount()];
        vertexToIsBelongToCurrentPath = new boolean[digraph.getVertexAmount()];
        terminalVertexToDepartVertex = new int[digraph.getVertexAmount()];

        for (int currentVertex = 0; currentVertex < digraph.getVertexAmount(); currentVertex++)
            if (!vertexToIsMarked[currentVertex] && vertexesInCycle == null)
                markAdjacentVertexesAndFindCycleViaDFS(digraph, currentVertex);
    }

    // run DFS and find a directed cycle (if one exists)
    // 🐖  如果有向图中存在有环，则：vertexesInCycle 会不为空
    private void markAdjacentVertexesAndFindCycleViaDFS(Digraph digraph, int currentVertex) {
        // 把 当前结点 设置为 “属于当前路径”
        vertexToIsBelongToCurrentPath[currentVertex] = true;
        // 把 当前结点 设置为 “已标记”
        vertexToIsMarked[currentVertex] = true;

        // 对于 当前结点 所有的邻居节点
        for (int currentAdjacentVertex : digraph.adjacentVertexesOf(currentVertex)) {

            // #1 如果发现了环，则：short circuit(短路/提前返回)
            if (vertexesInCycle != null) return;

                // #2 如果发现了未被标记的结点，则：继续标记
            else if (isNotMarked(currentAdjacentVertex)) {
                terminalVertexToDepartVertex[currentAdjacentVertex] = currentVertex;
                markAdjacentVertexesAndFindCycleViaDFS(digraph, currentAdjacentVertex);
            }

            // #3 // 如果当前邻居结点 #1 已经被标记； #2 且在当前路径中，说明 出现了环
            else if (vertexToIsBelongToCurrentPath[currentAdjacentVertex]) {
                // 则：从 当前结点 开始，沿着路径，一直回溯到 它当前的邻居结点 - 得到环中所有的结点
                vertexesInCycle = new Stack<Integer>();
                for (int backwardsVertexCursor = currentVertex; backwardsVertexCursor != currentAdjacentVertex; backwardsVertexCursor = terminalVertexToDepartVertex[backwardsVertexCursor]) {
                    vertexesInCycle.push(backwardsVertexCursor);
                }

                // 手动添加 环中的“起始结点”
                vertexesInCycle.push(currentAdjacentVertex);

                // 手动添加“当前结点”，得到 字符形式意义上的环
                vertexesInCycle.push(currentVertex);
                assert check();
            }
        }

        // 🐖 递归结束后，需要把 当前结点 从当前路径中移除 - 栈元素被弹出
        vertexToIsBelongToCurrentPath[currentVertex] = false;
    }

    private boolean isNotMarked(int currentAdjacentVertex) {
        return !vertexToIsMarked[currentAdjacentVertex];
    }

    /**
     * Does the digraph have a directed cycle?
     *
     * @return {@code true} if the digraph has a directed cycle, {@code false} otherwise
     */
    public boolean hasCycle() {
        return vertexesInCycle != null;
    }

    /**
     * Returns a directed cycle if the digraph has a directed cycle, and {@code null} otherwise.
     *
     * @return a directed cycle (as an iterable) if the digraph has a directed cycle,
     * and {@code null} otherwise
     */
    public Iterable<Integer> getVertexesInCycle() {
        return vertexesInCycle;
    }


    // certify(保证) that digraph has a directed cycle if it reports one
    private boolean check() {

        if (hasCycle()) {
            // verify cycle
            int anchorCursor = -1, dynamicCursor = -1;
            for (int currentVertex : getVertexesInCycle()) {
                // anchorCursor 会一直指在 环的第一个结点上
                if (anchorCursor == -1) anchorCursor = currentVertex;

                // dynamicCursor 会最终指在 环的最后一个结点上
                dynamicCursor = currentVertex;
            }

            // 预期：两个指针指向的位置/结点 应该是相等的
            if (anchorCursor != dynamicCursor) {
                System.err.printf("cycle begins with %d and ends with %d\n", anchorCursor, dynamicCursor);
                return false;
            }
        }

        return true;
    }

    /**
     * Unit tests the {@code DirectedCycle} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        // 构造图
        Digraph digraph = new Digraph(in);

        // 执行任务 - 检查有向图中的环
        DirectedCycle markedDigraph = new DirectedCycle(digraph);
        if (markedDigraph.hasCycle()) {
            StdOut.print("Directed cycle: ");
            for (int currentVertex : markedDigraph.getVertexesInCycle()) {
                StdOut.print(currentVertex + " ");
            }
            StdOut.println();
        } else {
            StdOut.println("No directed cycle");
        }
        StdOut.println();
    }

}