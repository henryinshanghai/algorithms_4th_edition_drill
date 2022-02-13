package com.henry.leetcode_traning_camp.week_01.day2;

import java.util.Stack;

public class Solution_min_stack_02 {
    public static void main(String[] args) {
        MyMinStack myStack = new MyMinStack();

        myStack.push(10);
        myStack.push(5);
        myStack.push(9);
        myStack.push(3);
        myStack.push(2);
        myStack.push(16);

        System.out.println("当前栈结构中的最小元素为： " + myStack.getMin()); // 2

        System.out.println("-------------------------------");
        myStack.pop(); // 删除16
        System.out.println("当前栈结构中的最小元素为： " + myStack.getMin()); // 预期结果：2

    }
}

class MyMinStack{
    // 底层数据结构
    Stack<Integer> dataStack;
    Stack<Integer> minStack;

    public MyMinStack() {
        this.dataStack = new Stack<>();
        this.minStack = new Stack<>();
    }

    public void push(int x) {
        dataStack.push(x);

        if (minStack.isEmpty() || x < minStack.peek()) {
            minStack.push(x);
        }
    }

    public int pop() {
        int res = dataStack.pop();

        if (res == minStack.peek()) {
            minStack.pop();
        }

        return res;
    }

    public int top(){
        return dataStack.peek();
    }

    public int getMin(){
        return minStack.peek();
    }
}