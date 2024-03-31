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
    private final int characterAmountInRegStr;       // number of characters in regular expression

    /**
     * 根据给定的正则表达式字符串（模式字符串） 来 构造其所对应的NFA的∈-转换有向图
     * <p>
     * 🐖 正则表达式的NFA中，结点中元素是“模式字符”，结点的状态是“模式字符在正则表达式字符串中的位置”
     * 特征：某一状态的结点，可能会向多个其他状态发生转移。
     * 状态之间发生转移的原因是：当前模式字符的性质 - 对于不同类型的模式字符，它会有自己的状态转换规则👇
     * #1 如果模式字符是一个 字母字符，则：它会通过“匹配转换” 来 转换到下一个状态/字符；    特征：匹配转换会消耗一个文本字符串中的字符
     * #2 如果模式字符是一个 “非字母字符”，则：它会通过“ε转换” 来 转换到下一个状态/字符；  特征：ε转换 不会消耗 文本字符串中的字符，也就是说 模式字符与文本字符没有匹配时，仍旧会进行状态转移
     * ε转换的分类：#1 由当前状态转换为下一个状态； #2 支持闭包操作/重复操作； #3 支持选择/或操作
     */
    public NFA(String regExpStr) {
        Stack<Integer> openCharactersSpotStack = new Stack<Integer>();
        this.regExpStr = regExpStr;
        characterAmountInRegStr = regExpStr.length(); // stateAmountInRegStr
        epsilonTransitionDigraph = new Digraph(characterAmountInRegStr + 1);

        for (int currentSpot = 0; currentSpot < characterAmountInRegStr; currentSpot++) { // 对于模式字符串中的每一个位置/状态...
            // 声明 leftParenthesisSpot变量，用于记录“当前左括号字符”的位置/状态
            // 🐖 leftParenthesisSpot的值 初始化为 当前位置/状态
            int leftParenthesisSpotCursor = currentSpot;

            /* 对“当前位置上字符/模式字符”进行分类讨论👇 */
            // Ⅰ 如果当前位置上的字符是 “启动字符”（左括号字符、或字符） 或者 “结束字符”（右括号字符）的话,则：在遇到结束字符时，向NFA中添加所需的ε转换
            leftParenthesisSpotCursor = whenItsBoundaryCharacterOn(regExpStr, currentSpot, openCharactersSpotStack, leftParenthesisSpotCursor);

            // Ⅱ 如果“当前位置上的字符”的后面紧跟着“闭包操作符”,则：向NFA中添加对应的ε转换 来 支持闭包/重复操作
            // 用法：#1 X* #2 (X)*     🐖 这里只需要使用 leftParenthesisSpotCursor这个变量就能表示两种情况
            if (isLegitState(currentSpot) && nextRegexCharacterIsAsterisk(regExpStr, currentSpot)) {
                supportClosureOperation(currentSpot, leftParenthesisSpotCursor);
            }

            // Ⅲ 如果“当前位置上的字符”需要进行一个ε转换（因为它不可能产生一个匹配转换），则: 向NFA中添加 转换到下一个状态的ε转换 来 使NFA继续下去
            if (isRequireEpsilonTransitionCharacter(regExpStr, currentSpot))
                keepNFAContinue(currentSpot);
        }

        if (openCharactersSpotStack.size() != 0)
            throw new IllegalArgumentException("Invalid regular expression");
    }

    private int whenItsBoundaryCharacterOn(String regExpStr, int currentSpot, Stack<Integer> openCharactersSpotStack, int leftParenthesisSpot) {
        if (isOpenCharacterOn(regExpStr, currentSpot)) // 如果是启动字符（左括号字符、或字符），则：不管是什么字符，都...
            // 把当前位置 记录到 一个栈结构中
            openCharactersSpotStack.push(currentSpot);
        else if (isCloseCharacterOn(regExpStr, currentSpot)) { // 如果当前模式字符 是 “结束字符”（右括号字符）,则：...
            // 弹出以获取 栈顶当前所记录的“启动字符”
            int openCharacterSpot = openCharactersSpotStack.pop();
            char openCharacter = regExpStr.charAt(openCharacterSpot);
            int rightParenthesisSpot = currentSpot;

            /* 对“此启动字符”进行分类讨论👇 */
            if (openCharacter == '|') { // 如果“此启动字符”是“或操作符”，则：向NFA中添加对应的ε转换 来 支持选择/或操作
                // 再次弹出栈元素 来 获取 栈顶当前所记录的“启动字符”（按照合法的正则表达式的规则，会是左括号字符）的位置 - 由于对合法正则表达式字符串的定义，这里得到的必然是一个左括号字符
                leftParenthesisSpot = openCharactersSpotStack.pop();
                int orCharacterSpot = openCharacterSpot;
                supportChooseOperation(leftParenthesisSpot, orCharacterSpot, rightParenthesisSpot);
            } else if (openCharacter == '(') {// 如果“此启动字符”是“左括号字符”，则：...
                // 用它来更新 leftParenthesisSpot变量的值
                leftParenthesisSpot = openCharacterSpot;
            } else
                assert false;
        }
        return leftParenthesisSpot;
    }

    private void keepNFAContinue(int currentSpot) {
        // 向NFA中添加 “从当前状态 -> 当前状态的下一个状态”的epsilon转换👇
        // 手段：在epsilon有向图中，添加边
        epsilonTransitionDigraph.addEdge(currentSpot, currentSpot + 1);
    }

    // “闭包操作符” 在NFA中所能够产生的epsilon转换👇：
    // ③-Ⅰ 如果出现在单个字符之后，则：在此字符 与 *字符之间，添加两条相互指向的epsilon转换；
    // ③-Ⅱ 如果出现在 右括号字符之后，则：在当前左括号字符 与 此字符之间，添加两条相互指向的epsilon转换；
    private void supportClosureOperation(int currentSpot, int leftParenthesisSpotCursor) {
        // 在NFA中添加 上述的一对epsilon转换（两种类型二选一）👇
        // 手段：在epsilon有向图中，添加边
        epsilonTransitionDigraph.addEdge(leftParenthesisSpotCursor, currentSpot + 1);
        epsilonTransitionDigraph.addEdge(currentSpot + 1, leftParenthesisSpotCursor);
    }

    // 或操作符 在NFA中所能够产生的 两种类型的epsilon转换 👇
    // #1 从当前左括号字符->或字符的下一个字符 的epsilon转换；
    // #2 从或字符本身->当前右括号字符 的epsilon转换；
    private void supportChooseOperation(int leftParenthesisSpot, int orCharacterSpot, int rightParenthesisSpot) {
        // 把位置作为状态，在NFA中添加 左括号字符->或字符的下一个字符 的epsilon转移👇
        int leftParenthesisState = leftParenthesisSpot;
        int firstCharacterStateInB = orCharacterSpot + 1;
        epsilonTransitionDigraph.addEdge(leftParenthesisState, firstCharacterStateInB);

        // 把位置作为状态，在NFA中添加 或字符->当前右括号字符 的epsilon转移👇
        int orCharacterState = orCharacterSpot;
        int rightParenthesisState = rightParenthesisSpot;
        epsilonTransitionDigraph.addEdge(orCharacterState, rightParenthesisState);
    }

    private boolean isCloseCharacterOn(String regExpStr, int passedSpot) {
        char regexCurrentCharacter = regExpStr.charAt(passedSpot);
        return regexCurrentCharacter == ')';
    }

    private boolean isOpenCharacterOn(String regExpStr, int givenSpot) {
        char patternCharacter = regExpStr.charAt(givenSpot);
        return patternCharacter == '(' || patternCharacter == '|';
    }

    private boolean isRequireEpsilonTransitionCharacter(String regExpStr, int currentSpot) {
        char regexCurrentCharacter = regExpStr.charAt(currentSpot);
        return regexCurrentCharacter == '(' || regexCurrentCharacter == '*' || regexCurrentCharacter == ')';
    }

    private boolean nextRegexCharacterIsAsterisk(String regExpStr, int currentState) {
        return regExpStr.charAt(currentState + 1) == '*';
    }

    private boolean isLegitState(int currentState) {
        return currentState < characterAmountInRegStr - 1;
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

            // #2 获取到 “当前可达的所有状态”中，与“当前文本字符”匹配时，“所到达的”状态集合
            Bag<Integer> matchTransferReachedStates = initMatchTransferReachedStatesVia(reachableStates, txtCurrentCharacter);
            if (matchTransferReachedStates.isEmpty()) continue;

            // #3 对于 NFA中“由匹配转换所到达的”状态集合中的每一个状态，获取其 “在NFA中通过epsilon转换所能够到达的状态”
            // 手段：使用自定义的方法 来 获取到 以”匹配后到达的顶点集合“中的顶点为起点，执行DFS后 结点被标记的图；
            reachableStates = renewReachableStatesVia(matchTransferReachedStates);

            // 如果”可达状态“的集合为空，则：提前return false
            if (reachableStates.size() == 0) return false;
        }

        // #4 检查 ”可接受状态（stateAmountInRegStr）“ 是否被包含在 ”可达状态 reachableStates“中
        if (acceptedStateIncludeIn(reachableStates)) return true;

        return false;
    }

    private boolean acceptedStateIncludeIn(Bag<Integer> reachableStates) {
        for (int currentState : reachableStates)
            if (currentState == characterAmountInRegStr)
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
        return currentState == characterAmountInRegStr;
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