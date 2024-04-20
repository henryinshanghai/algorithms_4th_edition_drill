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
public class CycleExistInDiGraph {
    private final boolean[] vertexToIsMarked;        // 作为DFS算法的基础操作 用于标记顶点是否已经被标记
    private final int[] terminalVertexToDepartVertex;            // 用于记录单条边中 结束顶点->出发顶点的映射关系 可以用来反溯出整个路径
    private final boolean[] vertexToIsBelongToCurrentPath;       // 用于记录 当前路径中的结点/结点是否属于当前路径 可以用来 在出现被标记的邻居顶点时，判断是否出现了环

    private Stack<Integer> vertexesInCycleViaStack;    // 用于临时记录出现的环中的所有顶点 只有在找到环的时候才会使用它

    /**
     * Determines whether the digraph {@code G} has a directed cycle and, if so,
     * finds such a cycle.
     * 确定有向图G中是否存在有有向环，如果存在的话，找到这个环
     * 🐖 在构造方法中执行任务
     * @param digraph the digraph 待检查的有向图
     */
    public CycleExistInDiGraph(Digraph digraph) {
        vertexToIsMarked = new boolean[digraph.getVertexAmount()];
        vertexToIsBelongToCurrentPath = new boolean[digraph.getVertexAmount()];
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
            } else if (isNotMarked(currentAdjacentVertex)) { // Ⅰ 如果“当前邻居结点”是未被标记的结点，则：继续递归地处理它
                // 在搜索过程中，记录下搜索路径上当前边的 结束顶点->出发顶点的映射关系  用于回溯出“路径本身”
                terminalVertexToDepartVertex[currentAdjacentVertex] = currentVertex;
                // 递归处理此结点
                markVertexAndRecordVertexInCurrentPathViaDFS(digraph, currentAdjacentVertex);
            } else if (belongToCurrentSearchPath(currentAdjacentVertex)) { // Ⅲ 如果在搜索过程中，当前邻居结点 ① 已经被标记； ② 且属于当前路径中，说明 图中出现了环
                // 则：从 当前结点 开始，沿着路径，一直回溯到 它当前的邻居结点 来 得到环中所有的结点
                collectVertexesInCycleIntoStack(currentVertex, currentAdjacentVertex);
                assert verifyIfCycleReallyExist();
            }
        }

        // #3 递归结束后，需要把 当前结点 从当前路径中移除 - 这样DFS算法才能够继续尝试其他的路径
        vertexToIsBelongToCurrentPath[currentVertex] = false;
    }

    private boolean belongToCurrentSearchPath(int currentAdjacentVertex) {
        return vertexToIsBelongToCurrentPath[currentAdjacentVertex];
    }

    private void flag(int currentVertex) {
        // #1 把 当前结点 设置为 “已标记”
        vertexToIsMarked[currentVertex] = true;
        // #2 把 当前结点 设置为 “属于当前路径”
        vertexToIsBelongToCurrentPath[currentVertex] = true;
    }

    // 从 terminalVertexToDepartVertex[] 中，获取到 环路径中的所有顶点
    // endVertex - 路径中的最后一个顶点(终止顶点) startVertex - 路径中的第一个顶点(起始顶点)
    private void collectVertexesInCycleIntoStack(int endVertex, int startVertex) {
        vertexesInCycleViaStack = new Stack<Integer>();
        // #1 向stack中添加 由数组记录下的结点
        for (int backwardsVertexCursor = endVertex; backwardsVertexCursor != startVertex; backwardsVertexCursor = terminalVertexToDepartVertex[backwardsVertexCursor]) {
            vertexesInCycleViaStack.push(backwardsVertexCursor);
        }

        // #2 手动添加 环中的“起始结点”（for循环中不会添加它）
        vertexesInCycleViaStack.push(startVertex);

        // #3 手动添加“当前结点”，从而得到 字符形式上/物理意义上的环
        vertexesInCycleViaStack.push(endVertex);
    }

    private boolean isNotMarked(int currentAdjacentVertex) {
        return !vertexToIsMarked[currentAdjacentVertex];
    }

    // 有向图中是否含有一个 有向环?
    public boolean findACycle() {
        // 手段：检查用于存储环中顶点的栈 是否为空
        return vertexesInCycleViaStack != null;
    }

    // 获取到有向图中的有向环(以有向环中所有顶点的可迭代集合的方式)
    // 如果不存在环，则：返回null
    public Iterable<Integer> getVertexesInCycle() {
        // 手段：算法中，有向环中的顶点 会被顺序添加到 栈中👇
        return vertexesInCycleViaStack;
    }


    // 确保算法的正确性：如果算法报告发现了环，使用此方法验证算法的结论
    private boolean verifyIfCycleReallyExist() {
        if (findACycle()) {
            // 验证环是否真的存在    手段：定指针 + 动指针
            int anchorCursor = -1, dynamicCursor = -1;
            for (int currentVertex : getVertexesInCycle()) {
                // anchorCursor 会一直指在 环的第一个结点上(aka 起始顶点)
                if (anchorCursor == -1) anchorCursor = currentVertex;

                // dynamicCursor 会沿着环上的结点一直向后移动 - 最终指在 栈的最后一个结点上(aka 起始顶点)
                dynamicCursor = currentVertex;
            }

            // 预期：如果存在环的话，两个指针指向的位置/结点 应该是相等的
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