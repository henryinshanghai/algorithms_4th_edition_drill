package com.henry.graph_chapter_04.direction_graph_02.search_accessible_vertexes.via_dfs.applications.strong_connected_components_in_digraph_05.tarjan.helper;

// 验证：把变量a赋值给变量b，相当于 为变量a的值 添加了一个新的引用 变量b
public class OperatorDemo {
    public static void main(String[] args) {
        int[] a = new int[10];
        int[] b = new int[10];

        int counter = 0;
        for (int i = 0; i < 3; i++) {
            a[i] = counter++;
            b[i] = a[i];
        }

        print(a);
        print(b);
    }

    private static void print(int[] arr) {
        for (int item : arr) {
            System.out.print(item + " ");
        }
        System.out.println();
    }
}
