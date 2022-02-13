package com.henry.graph_chapter_04.no_direction_graph_01.path.dfs.go_through_graph;

import com.henry.graph_chapter_04.no_direction_graph_01.graph.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class DepthFirstPaths_drill02 {
    private boolean[] marked;
    private int[] edgeTo;
    private final int s;

    public DepthFirstPaths_drill02(Graph G, int s) {
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        this.s = s;

        dfs(G, s);
    }

    private void dfs(Graph G, int v) {
        marked[v] = true;

        for (int w : G.adj(v)) {
            if (!marked[w]) {
                edgeTo[w] = v; // v - w
                dfs(G, w);
            }
        }
    }

    // APIs
    public boolean hasPathTo(int v) {
        return marked[v];
    }

    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;

        Stack<Integer> pathInStack = new Stack<>();
        for (int x = v; x != s ; x = edgeTo[x]) {
            pathInStack.push(x);
        }
        pathInStack.push(s);

        return pathInStack;
    }

    public static void main(String[] args) {
        Graph graph = new Graph(new In(args[0]));
        int s = Integer.parseInt(args[1]);

        DepthFirstPaths_drill02 dfs = new DepthFirstPaths_drill02(graph, s);

        // print out the path(s->v)
        for (int v = 0; v < graph.V(); v++) {
            StdOut.print("path from " + s + " to " + v + ": ");

            if (dfs.hasPathTo(v)) {
                for (int x : dfs.pathTo(v)) {
                    if (x == s) StdOut.print(x);
                    else StdOut.print("-" + x);
                }
            }
            StdOut.println();
        }
    }
}
