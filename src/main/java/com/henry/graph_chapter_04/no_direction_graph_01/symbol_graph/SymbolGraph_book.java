package com.henry.graph_chapter_04.no_direction_graph_01.symbol_graph;

import edu.princeton.cs.algs4.*;

public class SymbolGraph_book {
    /* 实现 符号图 所需要的底层数据结构 */
    // 符号表 用于 记录节点值 -> 索引值 的映射
    private ST<String, Integer> st;
    // 字符串数组 用于 记录索引值 -> 节点值 的关系    作用与st刚好相反
    private String[] keys;
    // 从文件中构建出的图 用于索引图中的顶点
    private Graph G;

    /* constructor */
    // 参数列表： 文件流对象、文件分隔符字符串
    public SymbolGraph_book(String stream, String sp) {
        /* 初始化成员变量 */
        // 初始化符号表 👇
        st = new ST<>();
        In in = new In(stream);
        // 逐行读取文件...
        while (in.hasNextLine()) {
            // 把当前行分割成为 字符串数组
            String[] a = in.readLine().split(sp);

            // 把 节点值 -> 节点值的索引 的映射关系存储到 符号表中
            // 节点值的索引 = 当前符号表的size
            for (int i = 0; i < a.length; i++) {
                if (!st.contains(a[i])) { // 避免重复添加
                    st.put(a[i], st.size());
                }
            }
        }

        // 初始化字符串数组keys 👇
        keys = new String[st.size()];

        for (String name : st.keys()) {
            // keys[<索引值>] = <节点的值>
            keys[st.get(name)] = name;
        }

        // 初始化图G 👇
        // 1 初始化图中的节点数量
        G = new Graph(st.size());
        in = new In(stream);

        while (in.hasNextLine()) {
            String[] a = in.readLine().split(sp);
            int v = st.get(a[0]);
            for (int i = 1; i < a.length; i++) {
                // 2 向图中不断添加新的边
                G.addEdge(v, st.get(a[i]));
            }
        }
    }

    /* public APIs */
    // 图中 是否存在 某个节点的值 为指定的字符串
    public boolean contains(String s) {
        return st.contains(s);
    }

    // 指定的字符串在图中的索引位置是多少？
    public int index(String s) {
        return st.get(s);
    }

    // 指定索引位置上节点的值是什么？
    public String name(int v) {
        return keys[v];
    }

    // 实现符号图的 底层的一般图 的字符串表示
    public Graph G() {
        return G;
    }

    public static void main(String[] args) {
        String filename = args[0];
        String delim = args[1];
        SymbolGraph_book sg = new SymbolGraph_book(filename, delim);

        Graph G = sg.G();

        // 获取 符号图 的各种性质
//        while (StdIn.hasNextLine()) {
//
//        }

        String source = args[2];
        for (int w : G.adj(sg.index(source))) {
            StdOut.println("   " + sg.name(w));
        }
    }
}
