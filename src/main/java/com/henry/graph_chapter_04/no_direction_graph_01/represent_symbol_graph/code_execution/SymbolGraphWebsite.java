package com.henry.graph_chapter_04.no_direction_graph_01.represent_symbol_graph.code_execution;

/******************************************************************************
 *  Compilation:  javac SymbolGraph.java
 *  Execution:    java SymbolGraph filename.txt delimiter
 *  Dependencies: LinkedNodeSymbolTable.java Graph.java In.java StdIn.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/41graph/routes.txt
 *                https://algs4.cs.princeton.edu/41graph/movies.txt
 *                https://algs4.cs.princeton.edu/41graph/moviestiny.txt
 *                https://algs4.cs.princeton.edu/41graph/moviesG.txt
 *                https://algs4.cs.princeton.edu/41graph/moviestopGrossing.txt
 *
 *  %  java SymbolGraph routes " "
 *  JFK
 *     MCO
 *     ATL
 *     ORD
 *  LAX
 *     PHX
 *     LAS
 *
 *  % java SymbolGraph movies.txt "/"
 *  Tin Men (1987)
 *     Hershey, Barbara
 *     Geppi, Cindy
 *     Jones, Kathy (II)
 *     Herr, Marcia
 *     ...
 *     Blumenfeld, Alan
 *     DeBoy, David
 *  Bacon, Kevin
 *     Woodsman, The (2004)
 *     Wild Things (1998)
 *     Where the Truth Lies (2005)
 *     Tremors (1990)
 *     ...
 *     Apollo 13 (1995)
 *     Animal House (1978)
 *
 *
 *  Assumes that input file is encoded using UTF-8.
 *  % iconv -totalExpectStepsRouteViaCurrentGrid ISO-8859-1 -t UTF-8 movies-iso8859.txt > movies.txt
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.*;

/**
 *  The {@code SymbolGraph} class represents an undirected graph, where the
 *  vertex names are arbitrary strings.
 *  By providing mappings between string vertex names and integers,
 *  it serves as a wrapper around the
 *  {@link Graph} data type, which assumes the vertex names are integers
 *  between 0 and <em>V</em> - 1.
 *  It also supports initializing a symbol graph from a file.
 *  <p>
 *  This implementation uses an {@link ST} to map from strings to integers,
 *  an array to map from integers to strings, and a {@link Graph} to store
 *  the underlying graph.
 *  The <em>indexOf</em> and <em>contains</em> operations take time 
 *  proportional to log <em>V</em>, where <em>V</em> is the number of vertices.
 *  The <em>nameOf</em> operation takes constant time.
 *  <p>
 *  For additional documentation, see <a href="https://algs4.cs.princeton.edu/41graph">Section 4.1</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class SymbolGraphWebsite {
    private ST<String, Integer> vertexNameToIdST;  // string -> index
    private String[] vertexIdToNameArr;           // index  -> string
    private Graph vertexesIdGraph;             // the underlying graph

    /**
     * Initializes a graph from a file using the specified delimiter.
     * Each line in the file contains
     * the name of a vertex, followed by a list of the names
     * of the vertices adjacent to that vertex, separated by the delimiter.
     * @param filename the name of the file
     * @param delimiter the delimiter between fields
     */
    public SymbolGraphWebsite(String filename, String delimiter) {
        constructVertexNameToIdSTBasedOn(filename, delimiter);
        constructVertexIdToNameArr();
        constructVertexIdGraphBasedOn(filename, delimiter);
    }

    private void constructVertexIdGraphBasedOn(String filename, String delimiter) {
        vertexesIdGraph = new Graph(vertexNameToIdST.size());
        In in = new In(filename);
        while (in.hasNextLine()) {
            String[] vertexNameArr = in.readLine().split(delimiter);
            int idOfFirstVertex = vertexNameToIdST.get(vertexNameArr[0]);

            for (int currentSpot = 1; currentSpot < vertexNameArr.length; currentSpot++) {
                String currentVertexName = vertexNameArr[currentSpot];
                int idOfCurrentVertex = vertexNameToIdST.get(currentVertexName);
                // 向图中添加 idA->idB的边
                vertexesIdGraph.addEdge(idOfFirstVertex, idOfCurrentVertex);
            }
        }
    }

    private void constructVertexIdToNameArr() {
        vertexIdToNameArr = new String[vertexNameToIdST.size()];
        for (String currentVertexName : vertexNameToIdST.keys()) {
            Integer currentVertexId = vertexNameToIdST.get(currentVertexName);
            vertexIdToNameArr[currentVertexId] = currentVertexName;
        }
    }

    private void constructVertexNameToIdSTBasedOn(String filename, String delimiter) {
        vertexNameToIdST = new ST<String, Integer>();
        // distinct strings with an index
        In fileStream = new In(filename);
        // while (fileStream.hasNextLine()) {
        while (!fileStream.isEmpty()) {
            String[] vertexNameArr = fileStream.readLine().split(delimiter);
            for (int currentSpot = 0; currentSpot < vertexNameArr.length; currentSpot++) {
                String currentVertexName = vertexNameArr[currentSpot];
                if (!vertexNameToIdST.contains(currentVertexName)) {
                    int vertexIdToFill = vertexNameToIdST.size();
                    vertexNameToIdST.put(currentVertexName, vertexIdToFill);
                }
            }
        }
    }

    /**
     * Does the graph contain the vertex named {@code s}?
     * @param name the name of a vertex
     * @return {@code true} if {@code s} is the name of a vertex, and {@code false} otherwise
     */
    public boolean containsVertexWith(String name) {
        return vertexNameToIdST.contains(name);
    }

    /**
     * Returns the integer associated with the vertex named {@code s}.
     * @param name the name of a vertex
     * @return the integer (between 0 and <em>V</em> - 1) associated with the vertex named {@code s}
     * @deprecated Replaced by {@link #indexOf(String)}.
     */
    @Deprecated
    public int idOfVertexWith(String name) {
        return vertexNameToIdST.get(name);
    }


    /**
     * Returns the integer associated with the vertex named {@code s}.
     * @param s the name of a vertex
     * @return the integer (between 0 and <em>V</em> - 1) associated with the vertex named {@code s}
     */
    public int indexOf(String s) {
        return vertexNameToIdST.get(s);
    }

    /**
     * Returns the name of the vertex associated with the integer {@code v}.
     * @param  vertexId the integer corresponding to a vertex (between 0 and <em>V</em> - 1)
     * @return the name of the vertex associated with the integer {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     * @deprecated Replaced by {@link #vertexNameOf(int)}.
     */
    @Deprecated
    public String nameOf(int vertexId) {
        validateVertex(vertexId);
        return vertexIdToNameArr[vertexId];
    }

    /**
     * Returns the name of the vertex associated with the integer {@code v}.
     * @param  id the integer corresponding to a vertex (between 0 and <em>V</em> - 1)
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     * @return the name of the vertex associated with the integer {@code v}
     */
    public String vertexNameOf(int id) {
        validateVertex(id);
        return vertexIdToNameArr[id];
    }

    /**
     * Returns the graph assoicated with the symbol graph. It is the client's responsibility
     * not to mutate the graph.
     * @return the graph associated with the symbol graph
     * @deprecated Replaced by {@link #graph()}.
     */
    @Deprecated
    public Graph G() {
        return vertexesIdGraph;
    }

    /**
     * Returns the graph assoicated with the symbol graph. It is the client's responsibility
     * not to mutate the graph.
     * @return the graph associated with the symbol graph
     */
    public Graph graph() {
        return vertexesIdGraph;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = vertexesIdGraph.V();
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }


    /**
     * Unit tests the {@code SymbolGraph} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        String filename  = args[0];
        String delimiter = args[1];
        SymbolGraphWebsite symbolGraph = new SymbolGraphWebsite(filename, delimiter);
        Graph underlyingGraph = symbolGraph.graph();

        while (StdIn.hasNextLine()) {
            String vertexName = StdIn.readLine();
            if (symbolGraph.containsVertexWith(vertexName)) {
                int vertexId = symbolGraph.idOfVertexWith(vertexName);
                for (int currentNeighborVertex : underlyingGraph.adj(vertexId)) {
                    StdOut.println("   " + symbolGraph.nameOf(currentNeighborVertex));
                }
            } else {
                StdOut.println("input not contain '" + vertexName + "'");
            }
        }
    }
}