package com.henry.graph_chapter_04.direction_graph_02.path.dfs.strong_connected_components.tarjan;

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
