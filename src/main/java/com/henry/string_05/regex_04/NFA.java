package com.henry.string_05.regex_04;
/******************************************************************************
 *  Compilation:  javac NFA.java
 *  Execution:    java NFA regexp text
 *  Dependencies: Stack.java Bag.java Digraph.java DirectedDFS.java
 *
 *  % java NFA "(A*B|AC)D" AAAABD
 *  true
 *
 *  % java NFA "(A*B|AC)D" AAAAC
 *  false
 *
 *  % java NFA "(a|(bc)*d)*" abcbcd
 *  true
 *
 *  % java NFA "(a|(bc)*d)*" abcbcbcdaaaabcbcdaaaddd
 *  true
 *
 *  Remarks
 *  -----------
 *  The following features are not supported:
 *    - The + operator
 *    - Multiway or
 *    - Metacharacters in the text
 *    - Character classes.
 *
 ******************************************************************************/

import com.henry.basic_chapter_01.collection_types.stack.implementation.via_linked_node.Stack;
import com.henry.graph_chapter_04.direction_graph_02.graph.Digraph;
import com.henry.graph_chapter_04.direction_graph_02.if_accessible_from_startVertex.DirectedDFS;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.StdOut;

/**
 * The {@code NFA} class provides a data type for creating a
 * <em>nondeterministic finite state automaton</em> (NFA) from a regular
 * expression and testing whether a given string is matched by that regular
 * expression.
 * It supports the following operations: <em>concatenation</em>,
 * <em>closure</em>, <em>binary or</em>, and <em>parentheses</em>.
 * It does not support <em>mutiway or</em>, <em>character classes</em>,
 * <em>metacharacters</em> (either in the text or pattern),
 * <em>capturing capabilities</em>, <em>greedy</em> or <em>reluctant</em>
 * modifiers, and other features in industrial-strength implementations
 * such as {@link java.util.regex.Pattern} and {@link java.util.regex.Matcher}.
 * <p>
 * This implementation builds the NFA using a digraph and a stack
 * and simulates the NFA using digraph search (see the textbook for details).
 * The constructor takes time proportional to <em>m</em>, where <em>m</em>
 * is the number of characters in the regular expression.
 * The <em>recognizes</em> method takes time proportional to <em>m n</em>,
 * where <em>n</em> is the number of characters in the text.
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/54regexp">Section 5.4</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class NFA {

    private Digraph epsilonTransitionDigraph;     // digraph of epsilon transitions
    private String regExpStr;     // regular expression
    private final int stateAmountInRegStr;       // number of characters in regular expression

    /**
     * Initializes the NFA from the specified regular expression.
     * 根据给定的正则表达式 来 构造其所对应的NFA的∈-转换有向图
     *
     * @param regExpStr the regular expression
     */
    public NFA(String regExpStr) {
        Stack<Integer> openCharactersSpotStack = new Stack<Integer>();
        this.regExpStr = regExpStr;
        stateAmountInRegStr = regExpStr.length();
        epsilonTransitionDigraph = new Digraph(stateAmountInRegStr + 1);

        for (int currentState = 0; currentState < stateAmountInRegStr; currentState++) {
            // 获取到 当前位置上的“模式字符”
            char regexCurrentCharacter = regExpStr.charAt(currentState);

            // 声明 leftParenthesisSpot变量，用于：#1 要么表示当前状态； #2 要么表示“当前左括号字符”的位置/状态
            // 🐖 leftParenthesisSpot的值 初始化为 当前位置/状态
            int leftParenthesisSpot = currentState;

            // 对“当前模式字符”的情形，进行分类讨论👇
            // ① 如果当前模式字符 是 “启动字符”（左括号字符、或字符）,则：...
            if (isOpenCharacter(regexCurrentCharacter))
                // 把“启动字符” 记录到 一个栈结构中
                openCharactersSpotStack.push(currentState);
            else if (isCloseCharacter(regexCurrentCharacter)) { // ② 如果当前模式字符 是 “结束字符”（右括号字符）,则：...
                // ②-Ⅰ 弹出以获取 栈顶当前所记录的“启动字符”
                int openCharacterSpot = openCharactersSpotStack.pop();
                char openCharacter = regExpStr.charAt(openCharacterSpot);

                // ②-Ⅱ 对“此启动字符”进行分类讨论👇
                // ②-Ⅱ-1 如果“此启动字符”是“或操作符”，则：...
                // 或操作符 在NFA中所能够产生的 两种类型的epsilon转换 👇
                // #1 从当前左括号字符->或字符的下一个字符 的epsilon转换；
                // #2 从或字符本身->当前右括号字符 的epsilon转换；
                if (openCharacter == '|') {
                    // 获取左括号字符的位置
                    leftParenthesisSpot = openCharactersSpotStack.pop();

                    // 把位置作为状态，在NFA中添加 左括号字符->或字符的下一个字符 的epsilon转移👇
                    int leftParenthesisState = leftParenthesisSpot;
                    int firstCharacterStateInB = openCharacterSpot + 1;
                    epsilonTransitionDigraph.addEdge(leftParenthesisState, firstCharacterStateInB);

                    // 把位置作为状态，在NFA中添加 或字符->当前右括号字符 的epsilon转移👇
                    int orCharacterState = openCharacterSpot;
                    int rightParenthesisState = currentState;
                    epsilonTransitionDigraph.addEdge(orCharacterState, rightParenthesisState);
                } else if (openCharacter == '(') // ②-Ⅱ-2 如果“此启动字符”是“左括号字符”，则：...
                    // 更新 leftParenthesisSpot变量的值 为 左括号字符的位置
                    leftParenthesisSpot = openCharacterSpot;

                else assert false;
            }

            // ③ 如果“当前模式字符”的后面紧跟着“闭包操作符”
            // “闭包操作符” 在NFA中所能够产生的epsilon转换👇：
            // ③-Ⅰ 如果出现在单个字符之后，则：在此字符 与 *字符之间，添加两条相互指向的epsilon转换；
            // ③-Ⅱ 如果出现在 右括号字符之后，则：在当前左括号字符 与 此字符之间，添加两条相互指向的epsilon转换；
            // 🐖 由于 leftParenthesisSpot变量的二义性，这里就只需要使用 下面这一段代码
            if (isLegitState(currentState) && nextRegexCharacterIsClosure(regExpStr, currentState)) {
                // 在NFA中添加 上述的一对epsilon转换（两种类型二选一）👇
                // 手段：在epsilon有向图中，添加边
                epsilonTransitionDigraph.addEdge(leftParenthesisSpot, currentState + 1);
                epsilonTransitionDigraph.addEdge(currentState + 1, leftParenthesisSpot);
            }

            // ④ 如果“当前模式字符”是一个 “不是或字符的”的非英文字母字符，则:...
            if (isTriggerEpsilonTransitionCharacter(regexCurrentCharacter))
                // 创建 “从当前状态 -> 当前状态的下一个状态”的epsilon转换👇
                epsilonTransitionDigraph.addEdge(currentState, currentState + 1);
        }

        if (openCharactersSpotStack.size() != 0)
            throw new IllegalArgumentException("Invalid regular expression");
    }

    private boolean isCloseCharacter(char regexCurrentCharacter) {
        return regexCurrentCharacter == ')';
    }

    private boolean isOpenCharacter(char regexCurrentCharacter) {
        return regexCurrentCharacter == '(' || regexCurrentCharacter == '|';
    }

    private boolean isTriggerEpsilonTransitionCharacter(char regexCurrentCharacter) {
        return regexCurrentCharacter == '(' || regexCurrentCharacter == '*' || regexCurrentCharacter == ')';
    }

    private boolean nextRegexCharacterIsClosure(String regExpStr, int currentState) {
        return regExpStr.charAt(currentState + 1) == '*';
    }

    private boolean isLegitState(int currentState) {
        return currentState < stateAmountInRegStr - 1;
    }

    /**
     * Returns true if the text is matched by the regular expression.
     *
     * @param txt the text
     * @return {@code true} if the text is matched by the regular expression,
     * {@code false} otherwise
     */
    public boolean recognizes(String txt) {
        DirectedDFS dfs = new DirectedDFS(epsilonTransitionDigraph, 0);
        Bag<Integer> pc = new Bag<Integer>();
        for (int v = 0; v < epsilonTransitionDigraph.getVertexAmount(); v++)
            if (dfs.isAccessibleFromStartVertex(v)) pc.add(v);

        // Compute possible NFA states for txt[i+1]
        for (int i = 0; i < txt.length(); i++) {
            if (txt.charAt(i) == '*' || txt.charAt(i) == '|' || txt.charAt(i) == '(' || txt.charAt(i) == ')')
                throw new IllegalArgumentException("text contains the metacharacter '" + txt.charAt(i) + "'");

            Bag<Integer> match = new Bag<Integer>();
            for (int v : pc) {
                if (v == stateAmountInRegStr) continue;
                if ((regExpStr.charAt(v) == txt.charAt(i)) || regExpStr.charAt(v) == '.')
                    match.add(v + 1);
            }
            if (match.isEmpty()) continue;

            dfs = new DirectedDFS(epsilonTransitionDigraph, match);
            pc = new Bag<Integer>();
            for (int v = 0; v < epsilonTransitionDigraph.getVertexAmount(); v++)
                if (dfs.isAccessibleFromStartVertex(v)) pc.add(v);

            // optimization if no states reachable
            if (pc.size() == 0) return false;
        }

        // check for accept state
        for (int v : pc)
            if (v == stateAmountInRegStr) return true;
        return false;
    }

    /**
     * Unit tests the {@code NFA} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        String regexp = "(" + args[0] + ")";
        String txt = args[1];
        NFA nfa = new NFA(regexp);
        StdOut.println(nfa.recognizes(txt));
    }

}