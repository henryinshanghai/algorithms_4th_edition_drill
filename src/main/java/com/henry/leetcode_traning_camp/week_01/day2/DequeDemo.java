package com.henry.leetcode_traning_camp.week_01.day2;

import java.util.Deque;
import java.util.LinkedList;

public class DequeDemo {
    public static void main(String[] args) {
        // 功能更加完备的栈
        Deque<String> deque =  new LinkedList<String>();

//        deque.push("a");
//        deque.push("b");
//        deque.push("c");
//        deque.addLast("a");
//        deque.addLast("b");
//        deque.addLast("c"); // 从队列尾部添加 - 行为和队列相同

        deque.addFirst("a");
        deque.addFirst("b");
        deque.addFirst("c"); // 从队列头部添加 - 行为和栈相同

        System.out.println(deque);

        System.out.println("==============");
        String str = deque.peek();
        System.out.println(str);
        System.out.println(deque);

        System.out.println("***************");
        while(deque.size() > 0){
            System.out.print(deque.pop() + " & ");
        } // 这些个APIs最好还是成对使用，不然有点容易混乱 addFist()-removeFist()...

        System.out.println("---------------");
        System.out.println(deque);
    }
}
