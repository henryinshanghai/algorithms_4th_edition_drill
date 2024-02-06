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

            // ④ 如果“当前模式字符”是一个 “epsilon转换的触发字符”，则:...
            if (isTriggerEpsilonTransitionCharacter(regexCurrentCharacter))
                // 向NFA中添加 “从当前状态 -> 当前状态的下一个状态”的epsilon转换👇
                // 手段：在epsilon有向图中，添加边
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
     * @param txtStr the text
     * @return {@code true} if the text is matched by the regular expression,
     * {@code false} otherwise
     */
    public boolean recognizes(String txtStr) {
        DirectedDFS markedDigraph = new DirectedDFS(epsilonTransitionDigraph, 0);
        // reachableStates 用于表示/维护 （由状态0）可达的所有状态
        Bag<Integer> reachableStates = new Bag<Integer>();
        // #1 使用 NFA中，由状态0可以到达的所有状态构成的集合 来 初始化 reachableStates
        // 手段：可达性问题 - 有向图中，由指定顶点（顶点0）可达的所有其他顶点（包含起始顶点本身）
        initReachableStatesVia(markedDigraph, reachableStates);

        // Compute possible NFA states for txt[i+1]
        for (int currentTxtCharacterSpot = 0; currentTxtCharacterSpot < txtStr.length(); currentTxtCharacterSpot++) {
            // 获取得到 当前文本字符
            char txtCurrentCharacter = txtStr.charAt(currentTxtCharacterSpot);

            // 文本字符不允许是 正则表达式的元字符
            if (isRegexMetaCharacter(txtCurrentCharacter))
                throw new IllegalArgumentException("text contains the metacharacter '" + txtCurrentCharacter + "'");

            // 获取到 “当前可达的所有状态”中，与“当前文本字符”匹配时，“所到达的”状态集合
            Bag<Integer> matchTransferReachedStates = initMatchTransferReachedStatesVia(reachableStates, txtCurrentCharacter);
            if (matchTransferReachedStates.isEmpty()) continue;

            // 对于 匹配“所到达的”状态集合中的每一个状态，获取其 在NFA中，“通过epsilon转换所能够到达的状态”
            // 手段：使用自定义的方法 来 获取到 以”匹配后到达的顶点集合“中的顶点为起点，执行DFS后 结点被标记的图；
            reachableStates = renewReachableStatesVia(matchTransferReachedStates);

            // 如果”可达状态“的集合为空，则：提前return false
            if (reachableStates.size() == 0) return false;
        }

        // 检查 ”可接受状态（stateAmountInRegStr）“ 是否在 ”可达状态 reachableStates“中
        if (acceptedStateIncludeIn(reachableStates)) return true;

        return false;
    }

    private boolean acceptedStateIncludeIn(Bag<Integer> reachableStates) {
        for (int currentState : reachableStates)
            if (currentState == stateAmountInRegStr)
                return true;
        return false;
    }

    private Bag<Integer> renewReachableStatesVia(Bag<Integer> matchTransferredStates) {
        Bag<Integer> reachableStates;
        DirectedDFS markedDigraph;

        // 获取到 以指定顶点集合作为起点集合，可以到达的顶点
        markedDigraph = new DirectedDFS(epsilonTransitionDigraph, matchTransferredStates);
        reachableStates = new Bag<Integer>();

        for (int currentVertex = 0; currentVertex < epsilonTransitionDigraph.getVertexAmount(); currentVertex++)
            // 如果当前顶点 由任意起点可达，则：把它添加到 “可达顶点”的集合中
            if (markedDigraph.isAccessibleFromStartVertex(currentVertex))
                reachableStates.add(currentVertex);

        return reachableStates;
    }

    private Bag<Integer> initMatchTransferReachedStatesVia(Bag<Integer> reachableStates, char txtCurrentCharacter) {
        Bag<Integer> matchTransferReachedStates = new Bag<Integer>();
        // 检查 当前可达状态集合中，是否存在有 能够与当前文本字符 相匹配的状态
        for (int currentState : reachableStates) {
            if (isAcceptedState(currentState)) continue;

            char regexCurrentCharacter = regExpStr.charAt(currentState);
            // 如果当前状态上的模式字符 与 当前文本字符 相匹配，则：
            if (isMatchBetween(txtCurrentCharacter, regexCurrentCharacter))
                // 把 此匹配转换所到达的状态 添加到 “匹配所达的状态集合”中
                matchTransferReachedStates.add(currentState + 1);
        }
        return matchTransferReachedStates;
    }

    private boolean isAcceptedState(int currentState) {
        return currentState == stateAmountInRegStr;
    }

    private void initReachableStatesVia(DirectedDFS markedDigraph, Bag<Integer> reachableStates) {
        for (int currentVertex = 0; currentVertex < epsilonTransitionDigraph.getVertexAmount(); currentVertex++)
            // 如果当前结点 “由起始结点可达”，则：把它添加到 reachableStates集合中
            if (markedDigraph.isAccessibleFromStartVertex(currentVertex))
                reachableStates.add(currentVertex);
    }

    private boolean isMatchBetween(char txtCurrentCharacter, char regexCurrentCharacter) {
        return (regexCurrentCharacter == txtCurrentCharacter) || regexCurrentCharacter == '.';
    }

    private boolean isRegexMetaCharacter(char txtCurrentCharacter) {
        return txtCurrentCharacter == '*' || txtCurrentCharacter == '|' || txtCurrentCharacter == '(' || txtCurrentCharacter == ')';
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