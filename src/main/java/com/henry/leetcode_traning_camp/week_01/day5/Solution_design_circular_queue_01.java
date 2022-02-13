package com.henry.leetcode_traning_camp.week_01.day5;

public class Solution_design_circular_queue_01 {
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

/**
 * 自定义的循环队列类型
 */
class MyCircularQueue {
    // 底层数据结构
    int[] arr;

    // 准备需要的指针
    int front;
    int rear;

    // 准备需要的其他变量
    int capacity;

    public MyCircularQueue(int k) { // k为程序员指定的容量
        // 使用k来初始化capacity
        capacity = k+1;
        arr = new int[capacity];

        // 初始化指针
        front = 0;
        rear = 0;
    }

    /**
     * 在队尾入队新的元素
     * @param value
     * @return
     */
    public boolean enQueue(int value) {
        if (isFull()) {
            return false;
        }

        arr[rear] = value;
        rear = (rear + 1) % capacity;

        return true;
    }

    /**
     * 辅助API：判断队列是否已经满员
     */
    public boolean isFull() {
        return (rear + 1) % capacity == front;
    }

    /**
     * 从队尾删除元素
     * @return
     */
    boolean deQueue(){
        if (isEmpty()) {
            return false;
        }

        // 如何从数组中删除元素呢？ 好像就只有覆盖操作了
        // 这里只是逻辑删除（移动下指针）
        front = (front + 1) % capacity;
        return true;
    }

    public boolean isEmpty(){
        return front == rear;
    }

    /**
     * 获取队尾元素
     */
    int Rear(){
        if (isEmpty()) {
            return -1;
        }
        // expr1:由于实现循环队列的做法是：把队尾标识指针rear重新指向数组首元素，所以(rear - 1)的做法可能出现负数下标
        // 手段： (rear -1 + 数组容量) % 数组容量
        // 原理：如果出现负下标，通过这种计算方式，下标会跑到数组末尾————从而得到预期的元素
        return arr[(rear - 1 + capacity) % capacity];
    }

    /**
     * 获取队首元素
     */
    public int Front() {
        if (isEmpty()) {
            return -1;
        }
        // front指针就没有什么幺蛾子了
        return arr[front];
    }
}