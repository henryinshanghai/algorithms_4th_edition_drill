package com.henry.leetcode_traning_camp.week_01.day5;

public class Solution_design_circular_deque_02 {
    public static void main(String[] args) {
        // 创建自定义的循环队列类型的对象
        MyCircularQueue circularQueue = new MyCircularQueue(3); // 设置长度为 3
        circularQueue.enQueue(1); // 返回 true
        circularQueue.enQueue(2); // 返回 true
        System.out.println(circularQueue.enQueue(3)); // 返回 true

        // 有点子难受，因为队列中的元素值必须要出队后才能继续遍历其他的元素。但是元素出队后，队列就已经改变了😳

        System.out.println(circularQueue.enQueue(4)); // 返回 false，队列已满
        circularQueue.Rear();  // 返回 3
        System.out.println(circularQueue.isFull()); // 返回 true
        circularQueue.deQueue(); // 返回 true
        circularQueue.enQueue(4); // 返回 true
        System.out.println(circularQueue.Rear()); // 返回 4

    }
}

class MyCircularDeque_01 {
    // 底层的数据结构
    int[] arr;

    // 指针
    int front; // front指针指向队列的队首元素
    int rear;

    int capacity;

    public MyCircularDeque_01(int k) {
        capacity = k + 1;
        arr = new int[capacity];

        front = 0;
        rear = 0;
    }

    // 核心APIs
    public boolean insertLast(int value) {
        if (isFull()) {
            return false;
        }

        arr[rear] = value;
        rear = (rear + 1) % capacity;
        return true;
    }

    public boolean insertFront(int value) {
        if (isFull()) {
            return false;
        }

        // 向前移动指针时，避免负指针    手段：如果出现负指针，就把指针向后移动一个capacity————这样它肯定在一个合法区间中
        front = (front - 1 + capacity) % capacity;
        arr[front] = value;

        return true;
    }

    /**
     * expr:删除时只要移动指针就可以了————逻辑删除
     * @return
     */
    public boolean deleteLast() {
        if (isEmpty()) {
            return false;
        }

        rear = (rear - 1 + capacity) % capacity;
        return true;
    }

    public boolean deleteFront() {
        if (isEmpty()) {
            return false;
        }

        front = (front + 1) % capacity;
        return true;
    }

    private boolean isEmpty() {
        return rear == front;
    }

    public boolean isFull() {
        return (rear + 1) % capacity == front;
    }

    public int getRear(){
        if (isEmpty()) {
            return -1;
        }

        return arr[(rear - 1 + capacity) % capacity]; // 要么循环了一个capacity，要么呆在原地
    }

    public int getFront() {
        if (isEmpty()) {
            return -1;
        }

        return arr[front];
    }

}
