package com.henry.graph_chapter_04.direction_graph_02.search_accessible_vertexes.via_dfs.applications.strong_connected_components_in_digraph_05.tarjan.helper;

import edu.princeton.cs.algs4.Stack;

// 验证：栈的foreach语法 打印的是 从栈顶到栈底的元素
public class StackDemo {
    public static void main(String[] args) {
        Stack<Integer> integerStack = new Stack<>();

        integerStack.push(0);
        integerStack.push(1);
        integerStack.push(2);
        integerStack.push(3);
        integerStack.push(4);

        // iterator遍历
        for (Integer currentInt : integerStack) {
            System.out.print(currentInt + ", ");
        }
        System.out.println();
        System.out.println(integerStack.size());

        // 不断pop()
        while (!integerStack.isEmpty()) {
            Integer intOnStackTop = integerStack.pop();
            System.out.print(intOnStackTop + " ");
        }
        System.out.println();
        System.out.println(integerStack.size());
    }
}
