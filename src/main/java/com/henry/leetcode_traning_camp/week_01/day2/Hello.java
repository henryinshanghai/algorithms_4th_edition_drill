package com.henry.leetcode_traning_camp.week_01.day2;

//import edu.princeton.cs.algs4.Queue;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

public class Hello {
    public static void main(String[] args) {
        System.out.println("are u okay?");

//        new StackViaNodeTemplate<>();

        Queue<String> queue = new LinkedList<String>();
        // 用法 - 特征 - 源码（原理）
        queue.add("henry");
        queue.add("henry2");// henry - henry2

        queue.remove();// remove(): 从链表头部删除节点

        System.out.println(queue); // 结果：[henry2]

        // PriorityQueue
        new PriorityQueue<>();
    }
}
