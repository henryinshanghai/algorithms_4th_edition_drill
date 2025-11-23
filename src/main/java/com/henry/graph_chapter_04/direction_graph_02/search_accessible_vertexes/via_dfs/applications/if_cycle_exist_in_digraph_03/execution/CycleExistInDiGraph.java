package com.henry.graph_chapter_04.direction_graph_02.search_accessible_vertexes.via_dfs.applications.if_cycle_exist_in_digraph_03.execution;

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
    private final int[] terminalVertexToDepartVertex;    // 用于记录单条边中 结束顶点->出发顶点的映射关系 可以用来反溯出整个路径

    private final boolean[] vertexToIsInRecursionStack;  // 用于记录 当前路径中的结点 是否存在于 当前DFS的递归调用栈中
    private Stack<Integer> vertexesInCycleViaStack;    // 用于 临时记录 出现的 环中的所有顶点，只有在 找到环 的时候 才会使用它

    /**
     * 构造器方法
     * 一般作用：用于 创建 当前类的实例对象
     * 具体的作用：逐一 以 图中所有的节点 作为起始节点，在有向图中 执行DFS。
     * 应用：确定 在有向图G中 是否存在有 有向环，如果存在的话，找到 这个环
     * 🐖 在构造方法中执行任务
     *
     * @param digraph the digraph 待检查的有向图
     */
    public CycleExistInDiGraph(Digraph digraph) {
        // 成员变量的初始化
        vertexToIsMarked = new boolean[digraph.getVertexAmount()];
        vertexToIsInRecursionStack = new boolean[digraph.getVertexAmount()];
        terminalVertexToDepartVertex = new int[digraph.getVertexAmount()];

        // 对于当前顶点...
        for (int currentVertex = 0; currentVertex < digraph.getVertexAmount(); currentVertex++)
            // 如果 该顶点 还没有被标记 && 当前 还没有找到环...
            if (isNotMarked(currentVertex) && notFindCycleYet()) {
                // 则：以它为起始顶点，在有向图中进行DFS
                System.out.println("~~~ 1 当前节点" + currentVertex + " 还没有被标记，且 图中当前仍没有检测到环，则：以该节点" + currentVertex + "为起点 执行DFS ~~~");
                markVertexAndRecordVertexInCurrentPathViaDFS(digraph, currentVertex);
                System.out.println("~~~ 2 图中 以节点" + currentVertex + "作为起始节点 所进行的DFS结束，所有 由" + currentVertex + "可达的顶点 都已经被标记。 ~~~");
            }
    }

    private boolean notFindCycleYet() {
        return vertexesInCycleViaStack == null;
    }

    // 🐖  如果 有向图中 存在有 环，则：vertexesInCycle 会不为空

    /**
     * 在 指定的有向图 中，以 指定的顶点 作为 起始顶点 进行DFS；
     * 作用：标记 图中 所有 由起始顶点可达的 所有顶点
     * 应用：检测出 图中存在的有向环 并 收集有向环中的所有顶点
     *
     * @param digraph       指定的有向图
     * @param currentVertex 作为起始顶点的指定顶点
     */
    private void markVertexAndRecordVertexInCurrentPathViaDFS(Digraph digraph, int currentVertex) {
        // #1 为 相关成员变量中的 当前结点 按需绑定 正确的boolean值
        flag(currentVertex);

        // #2 对于 当前结点 所有的邻居节点，记录路径中的边 && 验证是否找到了环
        for (int currentAdjacentVertex : digraph.adjacentVertexesOf(currentVertex)) {
            System.out.println("@@@ 对于 当前顶点" + currentVertex + "的 当前邻居顶点" + currentAdjacentVertex + " @@@");
            // 〇 如果发现了环，说明 不再需要后继步骤，
            if (findACycle()) {
                System.out.println("### 检测到了 有向环的存在，直接返回到 上一级调用。 ###");
                // 则：short circuit(短路/提前返回)
                return;
            } else if (isNotMarked(currentAdjacentVertex)) { // Ⅰ 如果 “当前邻居结点” 是 未被标记的结点，说明 它还没有被访问到，
                System.out.println("$$$ 当前顶点" + currentVertex + "的 邻居顶点" + currentAdjacentVertex + " 还没有被标记，则：" +
                        "① 记录下 搜索路径中的当前边" + currentVertex + "->" + currentAdjacentVertex +
                        "; ② 以" + currentAdjacentVertex + "作为起始顶点，递归地进行DFS $$$");
                /* 则：继续 递归地 处理它 */
                // ① 在 搜索过程 中，记录下 搜索路径上 当前边的 结束顶点->出发顶点的映射关系  用于回溯出“路径本身”
                terminalVertexToDepartVertex[currentAdjacentVertex] = currentVertex;
                // ② 递归处理 此结点
                markVertexAndRecordVertexInCurrentPathViaDFS(digraph, currentAdjacentVertex);
                System.out.println("$$$ 以" + currentAdjacentVertex + "作为 起始顶点的DFS 已经结束，节点" + currentAdjacentVertex + "出栈！ $$$");
            } else if (inRecursionStack(currentAdjacentVertex)) { // Ⅱ 如果在 搜索过程 中，当前邻居结点 ① 已经被标记； ② 且仍旧在递归调用栈中，说明 图中出现了环
                System.out.println("%%% 1 当前邻居节点" + currentAdjacentVertex + "已经被标记，并且 存在于 递归调用栈中，说明 找到了一个有向环，则：收集有向环中的所有顶点 %%%");
                // 则：从 当前结点 开始，沿着路径，一直回溯到 它当前的邻居结点 来 得到环中所有的结点
                collectVertexesInCycle(currentVertex, currentAdjacentVertex);
                System.out.println("%%% 2 有向环中的所有顶点 都已经收集完成 %%%");
                assert verifyIfCycleReallyExist();
            }
        }

        // #3 正确地维护 节点 是否存在于 递归调用栈中 - 在 递归调用结束 后，需要把 当前结点 设置为 “不存在于递归调用栈”
        vertexToIsInRecursionStack[currentVertex] = false;
        System.out.println("^^^ 在当前顶点" + currentVertex + "的 所有邻居顶点处理完成后，把其在xxx数组中所对应的元素更新为false" + " ^^^");
        System.out.println();
    }

    // 判断 指定的顶点 是否存在于 递归调用栈中
    // 手段：检查 该节点 在手动维护的栈中的值 是否为true
    private boolean inRecursionStack(int currentAdjacentVertex) {
        return vertexToIsInRecursionStack[currentAdjacentVertex];
    }

    private void flag(int currentVertex) {
        // #1 把 当前结点 设置为 “已标记”
        vertexToIsMarked[currentVertex] = true;
        // #2 正确地维护 节点 是否存在于 递归调用栈 ① 在 递归调用 伊始，把 当前结点 设置为 “存在于递归调用栈”
        vertexToIsInRecursionStack[currentVertex] = true;
        System.out.println("!!! 底层数组中，当前顶点" + currentVertex + "所对应的元素 都已经 按需标记 !!!");
    }

    /**
     * 从 terminalVertexToDepartVertex[] 中，获取到 有向环中的所有顶点
     *
     * @param endVertex   路径中的最后一个顶点(终止顶点)
     * @param startVertex 路径中的第一个顶点(起始顶点)
     */
    private void collectVertexesInCycle(int endVertex, int startVertex) {
        System.out.println();
        // 准备一个节点容器（局部变量）- 这里使用栈
        vertexesInCycleViaStack = new Stack<Integer>();

        // #1 向stack中添加 由数组所记录下的结点
        System.out.println("&&& 1 向预备的栈中，逐个添加 有向环中的顶点 &&&");
        for (int backwardsVertexCursor = endVertex; backwardsVertexCursor != startVertex; backwardsVertexCursor = terminalVertexToDepartVertex[backwardsVertexCursor]) {
            System.out.println("&&& 当前添加的顶点为：" + backwardsVertexCursor + " &&&");
            vertexesInCycleViaStack.push(backwardsVertexCursor);
        }

        // #2 手动添加 环中的“起始结点”（因为for循环中不会添加它）
        vertexesInCycleViaStack.push(startVertex);
        System.out.println("&&& 2 手动添加 有向环的起始顶点" + startVertex + " &&&");

        // #3 手动添加“当前结点”，从而得到 字符形式上/物理意义上的环
        vertexesInCycleViaStack.push(endVertex);
        System.out.println("&&& 3 手动添加 有向环的终止顶点" + endVertex + "&&&");
        System.out.println();
    }

    private boolean isNotMarked(int currentAdjacentVertex) {
        return !vertexToIsMarked[currentAdjacentVertex];
    }

    /**
     * 有向图中 是否含有一个 有向环?
     *
     * @return 如果存在，则 返回true；否则 返回false
     */
    public boolean findACycle() {
        // 手段：检查 用于存储环中顶点的栈 是否为空
        return vertexesInCycleViaStack != null;
    }

    /**
     * 获取到 有向图中的 有向环(以 有向环中所有顶点的 可迭代集合的方式)
     *
     * @return 如果存在有环，则 返回环中所有节点的可迭代集合；否则 返回null
     */
    public Iterable<Integer> getVertexesInCycle() {
        // 手段：算法中，有向环中的顶点 会被顺序添加到 栈中👇
        return vertexesInCycleViaStack;
    }

    // 确保 算法的正确性：如果 算法 报告发现了 环，使用 此方法 验证算法的结论 是否正确
    private boolean verifyIfCycleReallyExist() {
        if (findACycle()) {
            // 验证 环 是否真的存在    手段：定指针 + 动指针
            int anchorCursor = -1, dynamicCursor = -1;
            for (int currentVertex : getVertexesInCycle()) {
                // anchorCursor 会一直指在 环的第一个结点上(aka 起始顶点)
                // 原理：只有第一次循环时，条件成立 anchorCursor会被赋值；其他条件下，不会执行此语句
                if (anchorCursor == -1) {
                    anchorCursor = currentVertex;
                }

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
     * 当前数据类型的单元测试
     *
     * @param args 命令行参数
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