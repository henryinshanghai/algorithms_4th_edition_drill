package com.henry.string_05.regex_04.execution;
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
import com.henry.graph_chapter_04.direction_graph_02.represent_digraph.Digraph;
import com.henry.graph_chapter_04.direction_graph_02.search_accessible_vertexes.via_dfs.applications.if_vertex_accessible_from_startVertex_01.AccessibleVertexesInDigraph;
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
// 验证：可以使用 正则表达式字符串的NFA 来 判断指定的文本字符串中是否存在有 与其相匹配的子字符串
public class NFA {

    private Digraph epsilonTransitionDigraph;     // digraph of epsilon transitions
    private String regexStr;     // regular expression
    private final int characterAmountInRegexStr;       // 正则表达式的字符长度 用作 正则表达式的NFA中，最后预留的”接受状态“

    /**
     * 根据给定的正则表达式字符串（模式字符串） 来 构造其所对应的NFA的∈-转换有向图
     * 关键：对需要添加ε转换的场景 进行分类条论；
     * ε转换的分类：#1 由当前状态转换为下一个状态； #2 用于支持闭包操作/重复操作； #3 用于支持选择/或操作
     *
     * 🐖 正则表达式的NFA中，结点中元素是“模式字符”，结点的状态是“模式字符在正则表达式字符串中的位置”
     * 特征：某一状态的结点，可能会向多个其他状态发生转移。
     *
     * 状态之间发生转移的原因是：当前模式字符的性质 - 对于不同类型的模式字符，它会有自己的状态转换规则👇
     * #1 如果模式字符是一个 字母字符，则：它会通过“匹配转换” 来 转换到下一个状态/字符；    特征：匹配转换会消耗掉一个 文本字符串中的字符
     * #2 如果模式字符是一个 “非字母字符”，则：它会通过“ε转换” 来 转换到下一个状态/字符；  特征：ε转换 不会消耗 文本字符串中的字符，也就是说 模式字符与文本字符没有匹配时，仍旧会进行状态转移
     */
    public NFA(String regexStr) {
        // 准备一个栈对象 用于记录下 open_character(用作ε转换的起点)的位置
        Stack<Integer> openCharactersSpotStack = new Stack<Integer>();
        this.regexStr = regexStr;
        characterAmountInRegexStr = regexStr.length(); // stateAmountInRegStr
        // 准备一个有向图 用于描述 正则表达式的NFA中的ε转换
        epsilonTransitionDigraph = new Digraph(characterAmountInRegexStr + 1);

        // 对于模式字符串中的 从0开始的当前位置/状态...
        for (int currentRegexCharacterSpot = 0; currentRegexCharacterSpot < characterAmountInRegexStr; currentRegexCharacterSpot++) {
            // 声明 ”左括号位置“变量，用于： #1 记录“当前左括号字符”的位置/状态； 或者 #2 模式字符的当前位置/状态[初始值]
            int leftParenthesisSpotCursor = currentRegexCharacterSpot;

            /* 对“当前位置上字符/模式字符”进行分类讨论👇 */
            // Ⅰ 如果当前位置上的字符是 选择操作相关的字符 ( | ) ① “启动字符”（左括号字符、或字符）；② “结束字符”（右括号字符） 的话,
            // 则：在遇到”结束字符“时，向NFA中添加 所需的ε转换
            // 副作用：把 左括号(字符的位置 绑定到 此变量
            leftParenthesisSpotCursor = whenRegexCharacterRelatedToChoose(regexStr, currentRegexCharacterSpot, openCharactersSpotStack, leftParenthesisSpotCursor);

            // Ⅱ 如果“当前位置上的字符”的后面 紧跟着“闭包操作符”, 则：向NFA中添加对应的ε转换 来 支持闭包/重复操作
            // 用法：#1 X* #2 (X)*
            // 🐖 这里只需要使用 leftParenthesisSpotCursor这个变量就能表示两种情况 - 因为这个变量的值 要么是 X, 要么是 (
            if (isLegitState(currentRegexCharacterSpot) && nextRegexCharacterIsAsterisk(regexStr, currentRegexCharacterSpot)) {
                supportClosureOperation(leftParenthesisSpotCursor, currentRegexCharacterSpot);
            }

            // Ⅲ 如果“当前位置上的字符”是 不是|的非字母字符，说明它无法产生一个匹配转换，则：
            // 需要为它添加一个ε转换
            // 手段：向NFA中添加 转换到下一个状态/位置的ε转换 来 使NFA继续下去
            if (isNonLetterCharacter(regexStr, currentRegexCharacterSpot))
                advanceNFAFrom(currentRegexCharacterSpot);
        }

        if (openCharactersSpotStack.size() != 0)
            throw new IllegalArgumentException("Invalid regular expression");
    }

    private int whenRegexCharacterRelatedToChoose(String regexStr, int characterSpot, Stack<Integer> departCharactersSpotStack, int leftParenthesisSpotCursor) {
        if (isDepartCharacterOn(regexStr, characterSpot)) // 如果当前位置上的模式字符 是 启动字符（左括号字符、或字符），则：
            // 把 当前模式字符的位置 作为启动字符的位置 记录到 一个栈结构中
            departCharactersSpotStack.push(characterSpot);
        else if (isTerminalCharacterOn(regexStr, characterSpot)) { // 如果当前位置上的模式字符 是 “结束字符”（右括号字符）,则：
            // 把 当前模式字符的位置 作为 |操作符的位置
            int rightParenthesisSpot = characterSpot;

            // 弹出以获取 栈顶当前所记录的“启动字符的位置”
            int departCharacterSpot = departCharactersSpotStack.pop();
            // 并进一步获取到”启动字符“ - 可能是 左括号字符，也可能是 或字符
            char departCharacter = regexStr.charAt(departCharacterSpot);

            /* 对“此启动字符”进行分类讨论👇 */
            if (departCharacter == '|') { // 如果“此启动字符”是“选择操作符”，说明需要向NFA中添加分支来支持选择的操作
                // 则：向NFA中添加对应的ε转换 来 支持选择操作

                // 把第一次弹出的”启动字符的位置“ 作为 |操作符的位置
                int orCharacterSpot = departCharacterSpot;
                // 再次弹出栈元素 来 获取 栈顶当前所记录的“启动字符的位置” - 断言：按照合法的正则表达式的规则约束，这会是左括号(字符；
                leftParenthesisSpotCursor = departCharactersSpotStack.pop();

                // 为”选择操作符“ 添加 ε转换 - (到B的首字符的ε转换 以及 |到右括号的ε转换
                supportChooseOperation(leftParenthesisSpotCursor, orCharacterSpot, rightParenthesisSpot);
            } else if (departCharacter == '(') { // 如果“此启动字符”是“左括号(字符”，则
                // 把它的位置 作为 leftParenthesisSpot变量的值 - 用于支持 重复操作符*的ε转换
                leftParenthesisSpotCursor = departCharacterSpot;
            } else
                assert false;
        }

        // 这是本方法的一个副作用：为此变量 绑定了 左括号字符的位置
        return leftParenthesisSpotCursor;
    }

    // 推进NFA到下一个状态
    private void advanceNFAFrom(int currentSpot) {
        // 向NFA中添加 “从当前状态 -> 当前状态的下一个状态”的epsilon转换👇
        // 手段：在epsilon有向图中，添加边
        epsilonTransitionDigraph.addEdge(currentSpot, currentSpot + 1);
    }

    // “闭包操作符” 在NFA中所能够产生的epsilon转换👇：
    // #1 如果出现在单个字符之后，则：在此字符 与 *字符之间，添加两条相互指向的epsilon转换；
    // #2 如果出现在 右括号字符之后，则：在当前左括号字符 与 此字符之间，添加两条相互指向的epsilon转换；
    private void supportClosureOperation(int leftParenthesisSpotCursor, int currentRegexCharacterSpot) {
        // 在NFA中添加 上述的一对epsilon转换（两种类型二选一）👇
        // 手段：在epsilon有向图中，添加边
        int asteriskSpot = currentRegexCharacterSpot + 1;
        epsilonTransitionDigraph.addEdge(leftParenthesisSpotCursor, asteriskSpot);
        epsilonTransitionDigraph.addEdge(asteriskSpot, leftParenthesisSpotCursor);
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

    private boolean isTerminalCharacterOn(String regExpStr, int passedSpot) {
        char regexCurrentCharacter = regExpStr.charAt(passedSpot);
        return regexCurrentCharacter == ')';
    }

    private boolean isDepartCharacterOn(String regExpStr, int givenSpot) {
        char patternCharacter = regExpStr.charAt(givenSpot);
        return patternCharacter == '(' || patternCharacter == '|';
    }

    private boolean isNonLetterCharacter(String regExpStr, int currentSpot) {
        char currentRegexCharacter = regExpStr.charAt(currentSpot);
        return currentRegexCharacter == '(' || currentRegexCharacter == '*' || currentRegexCharacter == ')';
    }

    private boolean nextRegexCharacterIsAsterisk(String regexStr, int currentSpot) {
        return regexStr.charAt(currentSpot + 1) == '*';
    }

    private boolean isLegitState(int currentState) {
        return currentState < characterAmountInRegexStr - 1;
    }

    /**
     * 如果文本被正则表达式匹配的话，则返回true，否则返回false
     */
    public boolean recognizes(String txtStr) {
        // #1 获取到NFA中，由状态0作为起点，经ε转换所能到达的状态集合 ”ε转换所到达状态的集合“
        // 这本质上是 有向图中的”单点可达性问题“
        Bag<Integer> εTransferReachedStates = getReachedStatesViaεTransferFrom0();

        for (int currentTxtCharacterSpot = 0; currentTxtCharacterSpot < txtStr.length(); currentTxtCharacterSpot++) {
            // 对于当前文本字符...
            char txtCurrentCharacter = txtStr.charAt(currentTxtCharacterSpot);

            dealWithBreachOf(txtCurrentCharacter);

            // #2 获取到NFA中，由“当前可达的所有状态集合”中的各个状态，经匹配转换(与文本字符匹配)所能到达的状态集合
            Bag<Integer> matchTransferReachedStates = getReachedStatesViaMatchTransferFrom(εTransferReachedStates, txtCurrentCharacter);
            // 如果当前文本字符不存在匹配转换，说明 在“当前文本位置”无法得到一个匹配, 则：继续在“下一个位置"尝试匹配
            if (matchTransferReachedStates.isEmpty()) continue;

            // #3 更新 ”ε转换所到达状态的集合“
            // 获取到NFA中，以”匹配后到达的状态集合“中的各个状态作为起点，经ε转换(不消耗文本字符)所能到达的状态集合；
            // 本质上是有向图中的“多点可达性问题”
            εTransferReachedStates = renewETransferReachedStatesVia(matchTransferReachedStates);

            // 如果”可达状态“的集合为空，说明NFA的运行中断，不可能运行到“接受状态”, 则：可以提前return false
            if (εTransferReachedStates.size() == 0) return false;
        }

        // #4 检查 ”可接受状态（stateAmountInRegStr）“ 是否被包含在 ”当前由epsilon转换所到达的状态集合“中
        // 🐖 NFA中最后的接受状态，一定是经由ε转换得到的 因为它是一个额外添加的状态
        if (acceptedStateIncludeIn(εTransferReachedStates)) return true;

        return false;
    }

    // 🐖 文本字符不允许是 正则表达式的元字符
    private void dealWithBreachOf(char txtCurrentCharacter) {
        if (isRegexMetaCharacter(txtCurrentCharacter))
            throw new IllegalArgumentException("text contains the metacharacter '" + txtCurrentCharacter + "'");
    }

    private boolean acceptedStateIncludeIn(Bag<Integer> reachedStates) {
        for (int currentReachedState : reachedStates)
            if (currentReachedState == characterAmountInRegexStr)
                return true;
        return false;
    }

    // 对于 NFA中“由匹配转换所到达的”状态集合（matchTransferReachedStates）中的每一个状态，获取其 “在NFA中通过epsilon转换所能够到达的状态”，并把得到的状态添加到”可达顶点“的集合中
    private Bag<Integer> renewETransferReachedStatesVia(Bag<Integer> startStates) {
        Bag<Integer> reachableStates = new Bag<Integer>();

        // #1 获取到 以指定顶点集合作为”起点集合“，所能够到达的所有顶点 aka 在NFA中通过epsilon转换所能够到达的状态
        AccessibleVertexesInDigraph markedDigraph = new AccessibleVertexesInDigraph(epsilonTransitionDigraph, startStates);

        for (int currentVertex = 0; currentVertex < epsilonTransitionDigraph.getVertexAmount(); currentVertex++)
            // 如果当前顶点 由”matchTransferReachedStates“中的任意起点可达，则：#2 把它添加到 “可达顶点”的集合中
            if (markedDigraph.isAccessibleFromStartVertex(currentVertex))
                reachableStates.add(currentVertex);

        return reachableStates;
    }

    private Bag<Integer> getReachedStatesViaMatchTransferFrom(Bag<Integer> startStates, char currentTxtCharacter) {
        Bag<Integer> matchTransferReachedStates = new Bag<Integer>();
        // 检查 当前可达状态集合中，是否存在有 能够与当前文本字符 相匹配的状态
        for (int currentStartState : startStates) {
            // 如果当前状态 已经是 “接受状态”，说明 在文本字符串中已经找到了一个 正则表达式模式字符串的匹配，则：不再继续处理集合中的其他状态
            if (isAcceptedState(currentStartState)) continue;

            char currentRegexCharacter = regexStr.charAt(currentStartState);
            // 如果当前状态上的模式字符 与 当前文本字符 相匹配，则：
            if (isMatchBetween(currentTxtCharacter, currentRegexCharacter))
                // 把 此匹配转换所到达的状态 添加到 “匹配所达的状态集合”中
                matchTransferReachedStates.add(currentStartState + 1);
        }
        return matchTransferReachedStates;
    }

    private boolean isAcceptedState(int currentState) {
        return currentState == characterAmountInRegexStr;
    }

    // 可达性问题 - 有向图中，由指定顶点（顶点0）可达的所有其他顶点（包含起始顶点本身）
    private Bag<Integer> getReachedStatesViaεTransferFrom0() {
        Bag<Integer> reachableStates = new Bag<Integer>();

        // 标记图中“从结点0可达的所有其他结点”
        AccessibleVertexesInDigraph markedDigraph = new AccessibleVertexesInDigraph(epsilonTransitionDigraph, 0);

        for (int currentVertex = 0; currentVertex < epsilonTransitionDigraph.getVertexAmount(); currentVertex++)
            // 如果当前结点 “由起始结点可达”，则：把它添加到 reachableStates集合中
            if (markedDigraph.isAccessibleFromStartVertex(currentVertex))
                reachableStates.add(currentVertex);

        return reachableStates;
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
        // 获取正则表达式模式字符串
        String originalRegexStr = args[0];
        String wrappedRegexStr = "(" + originalRegexStr + ")";
        // 获取文本字符串
        String txtStr = args[1];

        // #1 构造出 正则表达式模式字符串的NFA(由ε转换所构成的有向图)
        NFA regexConstructedNFA = new NFA(wrappedRegexStr);
        // #2 使用 其NFA 来 判断文本字符串中是否存在有 与正则表达式相匹配的子字符串
        boolean matchResult = doesExistMatchIn(txtStr, regexConstructedNFA);

        // 🐖 NFA只能提供一个boolean值的答案 - yes | no
        StdOut.println(matchResult);
    }

    private static boolean doesExistMatchIn(String txtStr, NFA regexConstructedNFA) {
        return regexConstructedNFA.recognizes(txtStr);
    }

}