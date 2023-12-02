package com.henry.graph_chapter_04.direction_graph_02.symbol_graph; /******************************************************************************
 *  Compilation:  javac SymbolDigraph.java
 *  Execution:    java SymbolDigraph
 *  Dependencies: ST.java Digraph.java In.java
 *  Data files:   https://algs4.cs.princeton.edu/42digraph/routes.txt
 *
 *  %  java SymbolDigraph routes.txt " "
 *  JFK
 *     MCO
 *     ATL
 *     ORD
 *  ATL
 *     HOU
 *     MCO
 *  LAX
 *
 ******************************************************************************/

import com.henry.graph_chapter_04.direction_graph_02.graph.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 *  The {@code SymbolDigraph} class represents a digraph, where the
 *  vertex names are arbitrary strings.
 *  By providing mappings between string vertex names and integers,
 *  it serves as a wrapper around the
 *  {@link Digraph} data type, which assumes the vertex names are integers
 *  between 0 and <em>V</em> - 1.
 *  It also supports initializing a symbol digraph from a file.
 *  <p>
 *  This implementation uses an {@link ST} to map from strings to integers,
 *  an array to map from integers to strings, and a {@link Digraph} to store
 *  the underlying graph.
 *  The <em>indexOf</em> and <em>contains</em> operations take time
 *  proportional to log <em>V</em>, where <em>V</em> is the number of vertices.
 *  The <em>nameOf</em> operation takes constant time.
 *  <p>
 *  For additional documentation, see <a href="https://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class SymbolDigraph {
    private ST<String, Integer> keyStrToVertexIndexMap;  // string -> index/vertex
    private String[] vertexIndexToKeyStr;           // index/vertex  -> string
    private Digraph underlyingDigraph;           // the underlying digraph

    /**
     * Initializes a digraph from a file using the specified delimiter.
     * Each line in the file contains
     * the name of a vertex, followed by a list of the names
     * of the vertices adjacent to that vertex, separated by the delimiter.
     * @param filename the name of the file
     * @param delimiter the delimiter between fields
     */
    public SymbolDigraph(String filename, String delimiter) {
        keyStrToVertexIndexMap = new ST<String, Integer>();

        // First pass builds the index by reading strings to associate
        // distinct strings with an index
        // 构造出 结点字符串 -> 结点值的映射关系
        In fileStream = new In(filename);
        while (fileStream.hasNextLine()) {
            String[] keyStrArr = fileStream.readLine().split(delimiter);
            for (int currentSpot = 0; currentSpot < keyStrArr.length; currentSpot++) {
                String currentKeyStr = keyStrArr[currentSpot];
                if (mapNotContain(currentKeyStr))
                    // 为 keyStr 绑定 其所对应的 结点值（也就是 符号表中的 元素数量）
                    keyStrToVertexIndexMap.put(currentKeyStr, keyStrToVertexIndexMap.size());
            }
        }

        // 构造 从 结点值 -> 结点字符串键 之间的映射关系
        vertexIndexToKeyStr = new String[keyStrToVertexIndexMap.size()];
        for (String keyStr : keyStrToVertexIndexMap.keys()) {
            Integer vertexOfKeyStr = keyStrToVertexIndexMap.get(keyStr);
            vertexIndexToKeyStr[vertexOfKeyStr] = keyStr;
        }

        // second pass builds the digraph by connecting first vertex on each
        // line to all others
        // 使用邻接的方式 从文件中构造出图
        underlyingDigraph = new Digraph(keyStrToVertexIndexMap.size());
        fileStream = new In(filename);

        while (fileStream.hasNextLine()) {
            // 读取当前行的所有字符串，并得到 所有字符串组成的数组
            String[] keyStrArr = fileStream.readLine().split(delimiter);
            // 获取第一个字符串
            String firstKeyStr = keyStrArr[0];
            // 获取到 该字符串所对应的结点值
            int vertexOfFirstKeyStr = keyStrToVertexIndexMap.get(firstKeyStr);

            // 使用 剩余的字符串 作为结点的字符串，并把 结点A的值 -> 结点B的值 添加到图中
            // 疑问：为什么没有一个叫做 Vertex的内部类？
            for (int currentSpot = 1; currentSpot < keyStrArr.length; currentSpot++) {
                // 获取当前字符串对应的结点值
                String currentKeyStr = keyStrArr[currentSpot];
                int vertexOfCurrentKeyStr = keyStrToVertexIndexMap.get(currentKeyStr);

                // 向图中添加 结点值所关联的边
                underlyingDigraph.addEdge(vertexOfFirstKeyStr, vertexOfCurrentKeyStr);
            }
        }
    }

    private boolean mapNotContain(String currentKeyStr) {
        return !keyStrToVertexIndexMap.contains(currentKeyStr);
    }

    /**
     * Does the digraph contain the vertex named {@code s}?
     * @param keyStr the name of a vertex
     * @return {@code true} if {@code s} is the name of a vertex, and {@code false} otherwise
     */
    public boolean containsVertexWithName(String keyStr) { // vertex's name = vertex's keyStr
        return keyStrToVertexIndexMap.contains(keyStr);
    }

    /**
     * Returns the integer associated with the vertex named {@code s}.
     * @param s the name of a vertex
     * @return the integer (between 0 and <em>V</em> - 1) associated with the vertex named {@code s}
     * @deprecated Replaced by {@link #vertexIndexWithName(String)}.
     */
    @Deprecated
    public int index(String s) {
        return keyStrToVertexIndexMap.get(s);
    }

    /**
     * Returns the integer associated with the vertex named {@code s}.
     * @param passedName the name of a vertex
     * @return the integer (between 0 and <em>V</em> - 1) associated with the vertex named {@code s}
     */
    public int vertexIndexWithName(String passedName) {
        return keyStrToVertexIndexMap.get(passedName);
    }

    /**
     * Returns the name of the vertex associated with the integer {@code v}.
     * @param  v the integer corresponding to a vertex (between 0 and <em>V</em> - 1)
     * @return the name of the vertex associated with the integer {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     * @deprecated Replaced by {@link #nameOfVertexWith(int)}.
     */
    @Deprecated
    public String name(int v) {
        validateVertex(v);
        return vertexIndexToKeyStr[v];
    }

    /**
     * Returns the name of the vertex associated with the integer {@code v}.
     * @param  passedIndex the integer corresponding to a vertex (between 0 and <em>V</em> - 1)
     * @return the name of the vertex associated with the integer {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public String nameOfVertexWith(int passedIndex) {
        validateVertex(passedIndex);
        return vertexIndexToKeyStr[passedIndex];
    }

    /**
     * Returns the digraph associated with the symbol graph. It is the client's responsibility
     * not to mutate the digraph.
     *
     * @return the digraph associated with the symbol digraph
     * @deprecated Replaced by {@link #underlyingDigraph()}.
     */
    @Deprecated
    public Digraph G() {
        return underlyingDigraph;
    }

    /**
     * Returns the digraph associated with the symbol graph. It is the client's responsibility
     * not to mutate the digraph.
     *
     * @return the digraph associated with the symbol digraph
     */
    public Digraph underlyingDigraph() {
        return underlyingDigraph;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int vertex) {
        int vertexAmount = underlyingDigraph.getVertexAmount();
        if (vertex < 0 || vertex >= vertexAmount)
            throw new IllegalArgumentException("vertex " + vertex + " is not between 0 and " + (vertexAmount-1));
    }

    /**
     * Unit tests the {@code SymbolDigraph} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        String filename  = args[0];
        String delimiter = args[1];

        // 从命令行输入中，构造出一个 符号图（图的结点 包含有 字符串键 & 整数的值）
        SymbolDigraph symbolDigraph = new SymbolDigraph(filename, delimiter);

        // 获取到 符号图 底层所使用的 图
        Digraph digraph = symbolDigraph.underlyingDigraph();

        while (!StdIn.isEmpty()) {
            // 读取标准输入流中的当前行键入
            String vertexName = StdIn.readLine();
            //
            int vertexValue = symbolDigraph.vertexIndexWithName(vertexName);
            for (int currentAdjacentVertex : digraph.adjacentVertexesOf(vertexValue)) {
                StdOut.println("   " + symbolDigraph.nameOfVertexWith(currentAdjacentVertex));
            }
        }
    }
}