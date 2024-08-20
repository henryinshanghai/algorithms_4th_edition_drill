package com.henry.leetcode_traning_camp.week_01.day2.my_min_stack;

import java.util.Stack;

// 验证：为了获取到栈中的最小元素，可以使用一个单独的栈 来 保存数据栈中当前的最小元素
public class Solution_min_stack_02 {
    public static void main(String[] args) {
        MyMinStack myStack = constructMyMinStack();
        System.out.println("当前栈结构中的最小元素为： " + myStack.getMin()); // 2

        int popedItem = myStack.pop();// 删除16
        System.out.println("当前弹出的元素为： " + popedItem);
        System.out.println("当前栈结构中的最小元素为： " + myStack.getMin()); // 预期结果：2

    }

    private static MyMinStack constructMyMinStack() {
        MyMinStack myStack = new MyMinStack();

        myStack.push(10);
        myStack.push(5);
        myStack.push(9);
        myStack.push(3);
        myStack.push(2); // 最小元素为2
        myStack.push(16); // 最后一个入栈元素为16

        return myStack;
    }
}

class MyMinStack{
    // 底层数据结构
    Stack<Integer> dataStack; // 数据栈 用于记录所有数据本身
    Stack<Integer> minStack; // 最小元素栈 用于记录最小元素

    /* 按照题设要求来实现public API */
    public MyMinStack() {
        this.dataStack = new Stack<>();
        this.minStack = new Stack<>();
    }

    public void push(int passedItem) {
        dataStack.push(passedItem);

        // 如果传入的item 比起 当前最小栈的栈顶元素 还要小
        if (minStack.isEmpty() || passedItem < minStack.peek()) { // peek(): 查看最小栈的栈顶元素
            // 则：把它添加到最小栈中
            minStack.push(passedItem);
        }
    }

    public int pop() {
        int currentItem = dataStack.pop();

        // 如果栈顶元素 恰好是 最小栈的栈顶元素
        if (currentItem == minStack.peek()) {
            // 则 把最小栈的栈顶元素也一并移除
            minStack.pop();
        }

        return currentItem;
    }

    // 查看(而不移除)栈顶元素
    public int top(){
        // 使用现成的 peek()方法
        return dataStack.peek();
    }

    // 获取到 栈中的最小元素
    // 🐖 这个是 相比于标准的栈 新增的一个API，也是我们添加 minStack的原因
    public int getMin(){
        return minStack.peek();
    }
}