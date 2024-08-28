package com.henry.leetcode_traning_camp.week_01.day5.implement_circular_deque;

// 验证：对于双端队列两端的增删操作，对应的 其实都只是按需移动数组指针位置的操作
public class Solution_design_circular_deque {
    public static void main(String[] args) {
        MyCircularDeque circularDeque = new MyCircularDeque(3); // 设置容量大小为3
        // 在队尾入队1
        circularDeque.insertItemOnRear(1);
        // 在队尾入队2
        circularDeque.insertItemOnRear(2);
        // 在队首入队3
        circularDeque.insertItemOnFront(3);
        // 在队首入队4 - 由于容量已经满员，因此此次入队会失败
        System.out.println(circularDeque.insertItemOnFront(4));			        // 已经满了，返回 false

        // 读取队尾元素并打印
        System.out.println(circularDeque.getItemOnRear());  				// 打印结果为2
        // 队列是否满员
        System.out.println(circularDeque.isFull());				        // true
        // 从队尾删除元素
        circularDeque.deleteItemOnRear();
        // 在队首入队4
        circularDeque.insertItemOnFront(4);
        // 读取队首元素并打印
        System.out.println(circularDeque.getItemOnFront());				// 返回 4
    }
}

class MyCircularDeque {
    // 底层的数据结构
    int[] itemArr;

    // 指针
    int frontCursor; // front指针指向队列的队首元素
    int rearCursor;

    int actualCapacity;

    public MyCircularDeque(int expectedCapacity) {
        actualCapacity = expectedCapacity + 1;
        itemArr = new int[actualCapacity];

        frontCursor = 0;
        rearCursor = 0;
    }

    // 核心APIs
    public boolean insertItemOnRear(int item) {
        if (isFull()) {
            return false;
        }

        itemArr[rearCursor] = item;
        // 插入元素后，把rear指针向后移动一个位置
        rearCursor = (rearCursor + 1) % actualCapacity;
        return true;
    }

    public boolean insertItemOnFront(int item) {
        // 插入之前 先判断 队列是否已经满员了
        if (isFull()) {
            return false;
        }

        // 在队首插入元素时，先把front指针向前移动一个位置
        // 向前移动指针时，避免负指针    手段：如果出现负指针，就把指针向后移动一个capacity————这样它肯定在一个合法区间中
        // 由于队列没有满，因此这里肯定是一个空位置
        frontCursor = (frontCursor - 1 + actualCapacity) % actualCapacity;
        itemArr[frontCursor] = item;

        return true;
    }

    /**
     * expr:删除时只要移动指针就可以了————逻辑删除
     *
     * @return
     */
    public boolean deleteItemOnRear() {
        if (isEmpty()) {
            return false;
        }

        // 逻辑删除     手段：把rear指针向前移动一个位置
        // 为了避免负指针  这里+capacity
        rearCursor = (rearCursor - 1 + actualCapacity) % actualCapacity;
        return true;
    }

    public boolean deleteItemOnFront() {
        if (isEmpty()) {
            return false;
        }

        frontCursor = (frontCursor + 1) % actualCapacity;
        return true;
    }

    private boolean isEmpty() {
        return rearCursor == frontCursor;
    }

    public boolean isFull() {
        return (rearCursor + 1) % actualCapacity == frontCursor;
    }

    public int getItemOnRear() {
        if (isEmpty()) {
            return -1;
        }

        return itemArr[(rearCursor - 1 + actualCapacity) % actualCapacity]; // 要么循环了一个capacity，要么呆在原地
    }

    public int getItemOnFront() {
        if (isEmpty()) {
            return -1;
        }

        return itemArr[frontCursor];
    }

}
