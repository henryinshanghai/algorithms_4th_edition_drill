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
import com.henry.graph_chapter_04.direction_graph_02.search_accessible_vertexes.via_dfs.applications.if_vertex_accessible_from_startVertex_01.execution.AccessibleVertexesInDigraph;
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
// 验证：可以使用 正则表达式字符串的NFA 来 判断 指定的文本字符串中 是否存在有 与其相匹配的子字符串
public class NFA {

    private Digraph epsilonTransitionDigraph;     // epsilon转换的有向图
    private String regexStr;     // 正则表达式
    private final int characterAmountInRegexStr;       // 正则表达式的字符数量 用作 正则表达式的NFA中，最后预留的”接受状态“

    /**
     * 根据 给定的正则表达式字符串（模式字符串） 来 构造 其所对应的NFA的 ∈-转换有向图
     * 关键：对 需要添加ε转换的场景 进行 分类讨论；
     * ε转换的分类：#1 由 当前状态 转换为 下一个状态； #2 用于支持 闭包操作/重复操作； #3 用于支持 选择/或操作
     * <p>
     * 🐖 正则表达式的NFA中，结点中的元素 是 “模式字符”，结点的状态 是 “模式字符 在正则表达式字符串中的 位置”
     * 特征：某一状态的结点，可能会 向 多个其他状态 发生转移。
     * <p>
     * 状态之间 发生转移的 原因是：当前模式字符的性质 - 对于 不同类型的模式字符，它会有 自己的 状态转换规则👇
     * #1 如果 模式字符 是一个 字母字符，则：它 会通过 “匹配转换” 来 转换到 下一个状态/字符；    特征：匹配转换 会消耗掉一个 文本字符串中的字符
     * #2 如果 模式字符 是一个 “非字母字符”，则：它会通过 “ε转换” 来 转换到 下一个状态/字符；  特征：ε转换 不会消耗 文本字符串中的字符，也就是说 模式字符 与 文本字符 没有匹配时，仍旧会进行 状态转移
     */
    public NFA(String regexStr) {
        System.out.println("== 当前模式字符串为：" + regexStr + " ==");
        // 准备一个栈对象 用于记录下 open_character(用作 ε转换的起点)的位置
        Stack<Integer> openCharactersSpotStack = new Stack<Integer>();
        this.regexStr = regexStr;
        characterAmountInRegexStr = regexStr.length(); // stateAmountInRegStr
        // 准备一个有向图 用于描述 正则表达式的NFA中的 ε转换
        epsilonTransitionDigraph = new Digraph(characterAmountInRegexStr + 1);

        // 对于模式字符串中的 从0开始的当前位置/状态...
        for (int currentRegexCharacterSpot = 0; currentRegexCharacterSpot < characterAmountInRegexStr; currentRegexCharacterSpot++) {
            // 声明 ”多用途“变量，用于： ① 记录“当前左括号字符”的位置/状态； 或者 ② 模式字符的当前位置/状态[初始值]
            int leftParenthesisSpot = currentRegexCharacterSpot;

            /* 对 当前位置上字符/模式字符 进行分类讨论👇 */
            // Ⅰ 如果 当前位置上的字符 是 与 选择操作的场景 相关，则：在 需要 时，向NFA中 添加 选择操作 所需的ε转换
            // 返回值：左括号指针的位置
            leftParenthesisSpot = isChooseOperationScenarios(regexStr,
                    currentRegexCharacterSpot,
                    openCharactersSpotStack,
                    leftParenthesisSpot);

            // Ⅱ 如果 当前位置上的字符 与 闭包操作的场景 相关, 则：向NFA中添加 闭包操作 所需要的 ε转换
            // 🐖 这里 只需要使用 左括号指针 就能表示 两种情况 - 因为 左括号指针的值 要么是 X, 要么是 (
            if (isClosureOperationScenarios(regexStr, currentRegexCharacterSpot)) {
                supportClosureOperation(leftParenthesisSpot, currentRegexCharacterSpot);
            }

            // Ⅲ 如果 “当前位置上的字符”是一个 非字母字符，说明 它无法产生 一个 ”匹配转换“，则：
            /* 需要 为它添加 一个ε转换 */
            // 手段：向NFA中添加 转换到下一个状态/位置的ε转换 来 使NFA继续下去
            if (isNonLetterCharacter(regexStr, currentRegexCharacterSpot))
                advanceNFAFrom(currentRegexCharacterSpot);
        }

        if (openCharactersSpotStack.size() != 0)
            throw new IllegalArgumentException("Invalid regular expression");
    }


    private boolean isClosureOperationScenarios(String regexStr, int currentRegexCharacterSpot) {
        // 手段：查看 “当前位置上的字符”的后面 是不是 紧跟着 “闭包操作符”
        return isLegitState(currentRegexCharacterSpot)
                && nextRegexCharacterIsAsterisk(regexStr, currentRegexCharacterSpot);
    }

    /**
     * 作用：
     * ① 如果 当前模式字符 属于选择操作场景中的字符，则 在正确的时机 为NFA添加 其所需要的ε转换
     * ② 返回 左括号指针
     *
     * @param regexStr                  模式字符串
     * @param characterSpot             当前字符的位置
     * @param departCharactersSpotStack 出发字符位置的栈集合
     * @param leftParenthesisSpot       用于指示 左括号位置 的变量
     * @return
     */
    private int isChooseOperationScenarios(String regexStr,
                                           int characterSpot,
                                           Stack<Integer> departCharactersSpotStack,
                                           int leftParenthesisSpot) {
        // 如果 当前位置上的模式字符 是 出发字符（左括号字符、或字符），则：
        if (isDepartCharacterOn(regexStr, characterSpot))
            // 把 该位置 记录到 一个栈结构中
            departCharactersSpotStack.push(characterSpot);
        else if (isTerminalCharacterOn(regexStr, characterSpot)) { // 如果 当前位置上的模式字符 是 “终点字符)”,则：
            // 把 该位置 作为 rightParenthesisSpot变量的值 - 用于支持 选择操作所需的ε转换
            int rightParenthesisSpot = characterSpot;

            // 弹出以获取 栈顶当前所记录的 “启动字符的位置”
            int departCharacterSpot = departCharactersSpotStack.pop();
            // 并进一步获取到 该位置上的”启动字符“ - 可能是 (字符，也可能是 |字符
            char departCharacter = regexStr.charAt(departCharacterSpot);

            /* 对 “此启动字符” 进行分类讨论👇 */
            if (departCharacter == '|') { // 如果 “此出发字符” 是 “|操作符”，说明 当前右括号 是一个 选择操作中的右括号，则...
                /* 向NFA中 添加 对应的ε转换 来 支持选择操作 */

                // 把 第一次弹出的”启动字符的位置“ 作为 |操作符的位置
                int orCharacterSpot = departCharacterSpot;
                // 再次弹出栈元素 来 作为 (操作符的位置 - 断言：按照 合法的正则表达式 的规则约束，这会是 左括号(字符；
                leftParenthesisSpot = departCharactersSpotStack.pop();

                // 为 ”选择操作符“ 添加 其对应的ε转换 - ① ( 到 B的首字符的 ε转换 以及 ② | 到 右括号的 ε转换
                supportChooseOperation(leftParenthesisSpot, orCharacterSpot, rightParenthesisSpot);
            } else if (departCharacter == '(') { // 如果 “此出发字符” 是 (字符，说明 当前右括号 是一个 与左括号相对应的右括号，则：
                /* 记录 此(字符 的位置 - 作用: 用于 为 闭包操作符 添加 需要的ε转换 */
                leftParenthesisSpot = departCharacterSpot;
            } else // 出发字符 只允许是(或者|，如果 不是两者之一，说明 代码出现了错误，直接 assert false 来 抛出错误，终止执行
                assert false;
        }

        // 返回 左括号指针 所指向的位置
        return leftParenthesisSpot;
    }

    // 推进 NFA 到 下一个状态
    private void advanceNFAFrom(int currentSpot) {
        System.out.println("~~ 为 NFA中的非文本字符 添加 从 当前位置" + currentSpot + " 到 下一个位置" + (currentSpot + 1) + "的 添加ε转换 ~~");
        // 向NFA中添加 “从当前状态 -> 当前状态的下一个状态”的epsilon转换👇
        // 手段：在 epsilon有向图 中，添加 边
        epsilonTransitionDigraph.addEdge(currentSpot, currentSpot + 1);
    }

    // “闭包操作符” 在NFA中 所能够产生的 epsilon转换👇：
    // 闭包操作符的两种使用方式（X 作为 当前字符）：① X* ② (X)*
    // ① 如果 闭包操作符* 出现在 单个字符 之后，则：在 该字符 与 *字符 之间，添加 两条相互指向的epsilon转换；
    // ② 如果 闭包操作符* 出现在 右括号字符 之后，则：在 当前左括号字符 与 该字符 之间，添加 两条相互指向的epsilon转换；
    private void supportClosureOperation(int leftParenthesisSpotCursor, int currentRegexCharacterSpot) {
        // 在NFA中 添加 上述的 一对epsilon转换（两种类型 二选一）👇
        // 手段：在 epsilon有向图 中，添加边
        int asteriskSpot = currentRegexCharacterSpot + 1;
        System.out.println("++ 为 闭包操作 添加ε转换（左括号位置：" + leftParenthesisSpotCursor + ", 星号位置：" + asteriskSpot + "） ++");
        epsilonTransitionDigraph.addEdge(leftParenthesisSpotCursor, asteriskSpot);
        epsilonTransitionDigraph.addEdge(asteriskSpot, leftParenthesisSpotCursor);
    }

    // 或操作符 在NFA中所能够产生的 两种类型的epsilon转换 👇
    // #1 从当前左括号字符->或字符的下一个字符 的epsilon转换；
    // #2 从或字符本身->当前右括号字符 的epsilon转换；
    private void supportChooseOperation(int leftParenthesisSpot, int orCharacterSpot, int rightParenthesisSpot) {
        System.out.println("-- 为 选择操作 添加 ε转换（左括号位置：" + leftParenthesisSpot + ", 或操作符位置：" + orCharacterSpot + ", 右括号位置：" + rightParenthesisSpot + "） --");

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
     * 如果 文本字符串 被 正则表达式 所匹配的话，则：返回true，否则 返回false
     */
    public boolean recognizes(String txtStr) {
        // #1 获取到 在 正则表达式字符串的NFA 中，由 状态0 作为起点，经ε转换 所能到达的状态集合 ”ε转换所到达状态的集合“
        // 这 本质上是 有向图中的”单点可达性问题“
        Bag<Integer> εTransferReachedStates = getReachedStatesViaεTransferFromSpot0();
        System.out.println("@@ NFA中，从位置0/状态0 经ε转换 可达的 所有状态为：{" + printItems(εTransferReachedStates) + "} @@");

        // 对于 当前文本字符的位置...
        for (int currentTxtCharacterSpot = 0; currentTxtCharacterSpot < txtStr.length(); currentTxtCharacterSpot++) {
            // 获取 该位置上的文本字符
            char txtCurrentCharacter = txtStr.charAt(currentTxtCharacterSpot);

            dealWithBreachOf(txtCurrentCharacter);

            // #2 获取到 NFA中，由 “当前可达的所有状态集合”中的各个状态，经过 匹配转换（与当前文本字符的匹配） 所能到达的状态集合
            Bag<Integer> matchTransferReachedStates = getReachedStatesViaMatchTransferFrom(εTransferReachedStates, txtCurrentCharacter);
            System.out.println("%% NFA中，从 当前εTransferReachedStates {" + printItems(εTransferReachedStates) + "} 中的所有状态，经由 与第" + currentTxtCharacterSpot + "个位置上的当前文本字符" + txtCurrentCharacter + "的匹配转换 可达的 所有状态为：{" + printItems(matchTransferReachedStates) + "} %%");

            // 如果 当前文本字符 不存在 匹配转换，说明 在“当前文本位置” 无法得到 一个匹配, 则：继续在“下一个位置"尝试匹配
            if (matchTransferReachedStates.isEmpty()) continue;

            // #3 更新 ”ε转换所到达状态的集合“
            // 获取到 NFA中，以 ”匹配后到达的状态集合“中的 各个状态 作为起点，经 ε转换(不消耗文本字符) 所能到达的 状态集合；
            // 🐖 本质上是 有向图中的“多点可达性问题”
            εTransferReachedStates = reachedStatesViaεTransferFrom(matchTransferReachedStates);
            System.out.println("^^ NFA中，从 当前matchTransferReachedStates {" + printItems(matchTransferReachedStates) + "}中的所有状态，经由 ε转换 可达的 所有状态为：{" + printItems(εTransferReachedStates) + "} ^^");

            // 如果 ”经由ε转换可达的状态“的集合 为空，说明 NFA的运行中断，不可能 运行到“接受状态”, 则：可以提前return false
            if (εTransferReachedStates.size() == 0) return false;
        }

        System.out.println("&& for循环结束后，当前 由ε转换可达的 所有状态为：{" + printItems(εTransferReachedStates) + "} &&");

        // #4 检查 ”可接受状态（stateAmountInRegStr）“ 是否被包含在 ”当前由epsilon转换所到达的状态集合“ 中
        // 🐖 NFA中 最后的接受状态，一定是 经由ε转换得到的 因为它是一个 额外添加的状态
        if (acceptedStateIncludeIn(εTransferReachedStates)) {
            System.out.println("** 最终的可达状态集合{" + printItems(εTransferReachedStates) + "}中，包含有结束状态" + characterAmountInRegexStr + "。正则表达式" + regexStr + "成功识别了 文本字符串" + txtStr + "**");
            return true;
        }

        return false;
    }

    private String printItems(Bag<Integer> items) {
        StringBuilder itemsSB = new StringBuilder();
        for (Integer currentItem : items) {
            itemsSB.append(currentItem + " ");
        }

        return itemsSB.toString().substring(0, itemsSB.length() - 1);
    }

    // 🐖 文本字符 不允许是 正则表达式的元字符
    private void dealWithBreachOf(char txtCurrentCharacter) {
        if (isRegexMetaCharacter(txtCurrentCharacter))
            throw new IllegalArgumentException("text contains the metacharacter '" + txtCurrentCharacter + "'");
    }

    private boolean acceptedStateIncludeIn(Bag<Integer> reachedStates) {
        for (int currentReachedState : reachedStates)
            // 如果 接受状态 在 可达的状态集合中，则：
            if (currentReachedState == characterAmountInRegexStr)
                // 正则表达式 可以 成功识别 文本字符串
                return true;
        return false;
    }

    // 对于 NFA中“由匹配转换所到达的”状态集合（matchTransferReachedStates）中的每一个状态，
    // ① 获取其 “在NFA中 通过epsilon转换 所能够到达的状态”，并 ② 把 得到的状态 添加到”可达顶点“的集合中
    private Bag<Integer> reachedStatesViaεTransferFrom(Bag<Integer> startStates) {
        Bag<Integer> reachableStates = new Bag<Integer>();

        // 获取到 在 NFA对应的有向图 中，以 指定顶点集合 作为”起点集合“，所能够到达的 所有顶点
        // aka 在NFA中 通过epsilon转换 所能够到达的状态
        // #1 先标记有向图中的结点；
        AccessibleVertexesInDigraph markedDigraph = new AccessibleVertexesInDigraph(epsilonTransitionDigraph, startStates);

        // #2 再收集 所有“被标记的节点”
        for (int currentVertex = 0; currentVertex < epsilonTransitionDigraph.getVertexAmount(); currentVertex++)
            // 如果 当前顶点 由 ”matchTransferReachedStates“中的任意起点 可达，则：
            if (markedDigraph.isAccessibleFromStartVertex(currentVertex)) {
                // 把 它 添加到 “可达顶点”的集合中
                reachableStates.add(currentVertex);
            }

        return reachableStates;
    }

    private Bag<Integer> getReachedStatesViaMatchTransferFrom(Bag<Integer> startStates, char currentTxtCharacter) {
        Bag<Integer> matchTransferReachedStates = new Bag<Integer>();
        // 检查 当前可达状态集合中，是否存在有 能够与当前文本字符 相匹配的状态
        for (int currentStartState : startStates) {
            // 如果 当前状态 已经是 “接受状态”，说明 在文本字符串中 已经找到了一个 正则表达式模式字符串的匹配，
            // 则：不再继续处理 集合中的其他状态
            if (isAcceptedState(currentStartState)) continue;

            // 获取到 当前状态所对应的模式字符
            char currentRegexCharacter = regexStr.charAt(currentStartState);
            // 如果 当前状态上的模式字符 与 当前文本字符 相匹配，则：
            if (isMatchBetween(currentTxtCharacter, currentRegexCharacter))
                // 把 此匹配转换 所到达的状态(当前状态+1) 添加到 “匹配所达的状态集合”中
                matchTransferReachedStates.add(currentStartState + 1);
        }

        return matchTransferReachedStates;
    }

    private boolean isAcceptedState(int currentState) {
        return currentState == characterAmountInRegexStr;
    }

    // 可达性问题 - 有向图中，由 指定顶点（顶点0）可达的 所有其他顶点（包含 起始顶点 本身）
    private Bag<Integer> getReachedStatesViaεTransferFromSpot0() {
        Bag<Integer> reachableStates = new Bag<Integer>();

        // 先标记 图中“从结点0可达的 所有其他结点”
        AccessibleVertexesInDigraph markedDigraph = new AccessibleVertexesInDigraph(epsilonTransitionDigraph, 0);

        // 再收集 所有被标记了的节点
        for (int currentVertex = 0; currentVertex < epsilonTransitionDigraph.getVertexAmount(); currentVertex++)
            // 如果 当前结点 “由起始结点0可达”，则：把 它 添加到 reachableStates集合 中
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
        // 获取到 正则表达式模式字符串
        String originalRegexStr = args[0];
        String wrappedRegexStr = "(" + originalRegexStr + ")";
        // 获取到 文本字符串
        String txtStr = args[1];

        // #1 构造出 正则表达式模式字符串的NFA(aka 由 ε转换 所构成的 有向图)
        NFA regexConstructedNFA = new NFA(wrappedRegexStr);
        // #2 使用 其NFA 来 判断 在文本字符串中 是否存在有 与正则表达式相匹配的 子字符串
        boolean matchResult = doesExistMatchIn(txtStr, regexConstructedNFA);

        // 🐖 NFA 只能提供一个 boolean值的答案 - yes | no
        StdOut.println(matchResult);
    }

    private static boolean doesExistMatchIn(String txtStr, NFA regexConstructedNFA) {
        return regexConstructedNFA.recognizes(txtStr);
    }

}