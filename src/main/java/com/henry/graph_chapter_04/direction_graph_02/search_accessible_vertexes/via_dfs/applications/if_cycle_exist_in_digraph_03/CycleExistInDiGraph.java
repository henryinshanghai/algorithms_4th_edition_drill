package com.henry.graph_chapter_04.direction_graph_02.search_accessible_vertexes.via_dfs.applications.if_cycle_exist_in_digraph_03;

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

import com.henry.graph_chapter_04.direction_graph_02.represent_digraph.Digraph;
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
// 结论#1：在 有向图的DFS算法 中，能够得到 “有向图中 是否存在有 环”的答案。
// 手段：使用一个名叫 vertexToIsBelongToCurrentPath的数组 来 记录“结点 是不是属于 当前路径”
// 原理：在 使用DFS 对结点进行标记与查找 时，如果 在“当前查找路径”中，遇到了 “已经被标记的结点”，则：说明 有向图中 存在有环
// 结论#2：在 有向图的DFS算法 中，能够获取到 “环中的所有结点”。
// 手段：使用 名为 terminalVertexToDepartVertex的数组，指定 正确的 backwardsVertexCursor 与 startVertex 就能够 使用for循环 来 把所有结点收集到栈集合中
public class CycleExistInDiGraph {
    private final boolean[] vertexToIsMarked;        // 作为DFS算法的基础操作 用于标记顶点是否已经被标记
    private final int[] terminalVertexToDepartVertex;            // 用于记录单条边中 结束顶点->出发顶点的映射关系 可以用来反溯出整个路径

    private final boolean[] vertexToIsInRecursionStack;       // 用于记录 当前路径中的结点 是否存在于 当前DFS的递归调用栈中
    private Stack<Integer> vertexesInCycleViaStack;    // 用于 临时记录 出现的 环中的所有顶点，只有在 找到环 的时候 才会使用它

    /**
     * Determines whether the digraph {@code G} has a directed cycle and, if so,
     * finds such a cycle.
     * 确定有向图G中 是否存在 有有向环，如果存在的话，找到这个环
     * 🐖 在构造方法中执行任务
     * @param digraph the digraph 待检查的有向图
     */
    public CycleExistInDiGraph(Digraph digraph) {
        vertexToIsMarked = new boolean[digraph.getVertexAmount()];
        vertexToIsInRecursionStack = new boolean[digraph.getVertexAmount()];
        terminalVertexToDepartVertex = new int[digraph.getVertexAmount()];

        // 对于当前顶点...
        for (int currentVertex = 0; currentVertex < digraph.getVertexAmount(); currentVertex++)
            // 如果它还没有被标记 && 当前还没有找到环...
            if (isNotMarked(currentVertex) && notFindCycleYet())
                // 则：对它进行标记，并把它记录进当前路径中...
                markVertexAndRecordVertexInCurrentPathViaDFS(digraph, currentVertex);
    }

    private boolean notFindCycleYet() {
        return vertexesInCycleViaStack == null;
    }

    // 🐖  如果有向图中存在有环，则：vertexesInCycle 会不为空
    private void markVertexAndRecordVertexInCurrentPathViaDFS(Digraph digraph, int currentVertex) {
        // #1 为成员变量中的当前结点 绑定 boolean值
        flag(currentVertex);

        // #2 对于 当前结点 所有的邻居节点，记录路径中的边 && 验证是否找到了环
        for (int currentAdjacentVertex : digraph.adjacentVertexesOf(currentVertex)) {
            if (findACycle()) { // 〇 如果发现了环，则：short circuit(短路/提前返回)
                return;
            } else if (isNotMarked(currentAdjacentVertex)) { // Ⅰ 如果 “当前邻居结点” 是 未被标记的结点，则：继续 递归地 处理它
                // 在 搜索过程 中，记录下 搜索路径上 当前边的 结束顶点->出发顶点的映射关系  用于回溯出“路径本身”
                terminalVertexToDepartVertex[currentAdjacentVertex] = currentVertex;
                // 递归处理 此结点
                markVertexAndRecordVertexInCurrentPathViaDFS(digraph, currentAdjacentVertex);
            } else if (inRecursionStack(currentAdjacentVertex)) { // Ⅲ 如果在 搜索过程 中，当前邻居结点 ① 已经被标记； ② 且仍旧在递归调用栈中，说明 图中出现了环
                // 则：从 当前结点 开始，沿着路径，一直回溯到 它当前的邻居结点 来 得到环中所有的结点
                collectVertexesInCycle(currentVertex, currentAdjacentVertex);
                assert verifyIfCycleReallyExist();
            }
        }

        // #3 正确地维护 节点是否存在于递归调用栈 ① 在递归调用结束后，需要把 当前结点 设置为 “不存在于递归调用栈”
        vertexToIsInRecursionStack[currentVertex] = false;
    }

    private boolean inRecursionStack(int currentAdjacentVertex) {
        return vertexToIsInRecursionStack[currentAdjacentVertex];
    }

    private void flag(int currentVertex) {
        // #1 把 当前结点 设置为 “已标记”
        vertexToIsMarked[currentVertex] = true;
        // #2 正确地维护 节点是否存在于递归调用栈 ① 在递归调用伊始，把 当前结点 设置为 “存在于递归调用栈”
        vertexToIsInRecursionStack[currentVertex] = true;
    }

    // 从 terminalVertexToDepartVertex[] 中，获取到 环路径中的所有顶点
    // endVertex - 路径中的最后一个顶点(终止顶点) startVertex - 路径中的第一个顶点(起始顶点)
    private void collectVertexesInCycle(int endVertex, int startVertex) {
        // 准备一个节点容器（局部变量）- 这里使用栈
        vertexesInCycleViaStack = new Stack<Integer>();

        // #1 向stack中添加 由数组所记录下的结点
        for (int backwardsVertexCursor = endVertex; backwardsVertexCursor != startVertex; backwardsVertexCursor = terminalVertexToDepartVertex[backwardsVertexCursor]) {
            vertexesInCycleViaStack.push(backwardsVertexCursor);
        }

        // #2 手动添加 环中的“起始结点”（因为for循环中不会添加它）
        vertexesInCycleViaStack.push(startVertex);

        // #3 手动添加“当前结点”，从而得到 字符形式上/物理意义上的环
        vertexesInCycleViaStack.push(endVertex);
    }

    private boolean isNotMarked(int currentAdjacentVertex) {
        return !vertexToIsMarked[currentAdjacentVertex];
    }

    // key API*1:有向图中 是否含有一个 有向环?
    public boolean findACycle() {
        // 手段：检查用于存储环中顶点的栈 是否为空
        return vertexesInCycleViaStack != null;
    }

    // key API*2:获取到 有向图中的 有向环(以 有向环中所有顶点的 可迭代集合的方式)
    // 如果不存在环，则：返回null
    public Iterable<Integer> getVertexesInCycle() {
        // 手段：算法中，有向环中的顶点 会被顺序添加到 栈中👇
        return vertexesInCycleViaStack;
    }


    // 确保 算法的正确性：如果 算法 报告发现了 环，使用 此方法 验证算法的结论
    private boolean verifyIfCycleReallyExist() {
        if (findACycle()) {
            // 验证环 是否真的存在    手段：定指针 + 动指针
            int anchorCursor = -1, dynamicCursor = -1;
            for (int currentVertex : getVertexesInCycle()) {
                // anchorCursor 会一直指在 环的第一个结点上(aka 起始顶点)
                // 原理：只有第一次循环时，条件成立 anchorCursor会被赋值；其他条件下，不会执行此语句
                if (anchorCursor == -1) anchorCursor = currentVertex;

                // dynamicCursor 会 沿着 环上的结点 一直向后移动 - 最终指在 栈的最后一个结点上(aka 起始顶点)
                dynamicCursor = currentVertex;
            }

            // 预期：如果 存在环的话，两个指针 最终指向的位置/结点 应该是相等的
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
        // #1 构造图
        Digraph digraph = new Digraph(in);
        // #2 执行任务 - 检查有向图中的环   手段：调用构造方法
        CycleExistInDiGraph markedDigraph = new CycleExistInDiGraph(digraph);
        // #3 获取任务的执行结果     手段：调用public APIs
        if (markedDigraph.findACycle()) {
            // 有环的话，打印出来
            StdOut.print("Directed cycle: ");
            for (int currentVertex : markedDigraph.getVertexesInCycle()) {
                StdOut.print(currentVertex + " ");
            }
            StdOut.println();
        } else {
            // 没有的话，打印语句
            StdOut.println("No directed cycle");
        }
        StdOut.println();
    }

}