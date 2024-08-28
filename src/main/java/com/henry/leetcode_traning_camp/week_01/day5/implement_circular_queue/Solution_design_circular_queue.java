package com.henry.leetcode_traning_camp.week_01.day5.implement_circular_queue;

// 验证：为了实现循环队列(能够有效利用front前面的数组空间)，可以使用%操作 来 把队尾位置（逻辑位置）放在队首位置的前面
public class Solution_design_circular_queue {
    public static void main(String[] args) {

        // 创建自定义的循环队列类型的对象 容量为3
        MyCircularQueue circularQueue = new MyCircularQueue(3);
        // 入队1
        circularQueue.enQueueItemOnRear(1);
        // 入队2
        circularQueue.enQueueItemOnRear(2);
        // 入队3，队列满员
        System.out.println(circularQueue.enQueueItemOnRear(3)); // 返回 true

        // 有点子难受，因为队列中的元素值必须要出队后才能继续遍历其他的元素。但是元素出队后，队列就已经改变了😳
        // 尝试入队4 - 返回 false，因为队列已满
        System.out.println(circularQueue.enQueueItemOnRear(4));
        // 读取队尾元素 结果应该为3
        circularQueue.getItemOnRear();
        System.out.println(circularQueue.isFull()); // 返回 true
        // 出队队首元素
        circularQueue.deQueueItemFromFront();
        // 入队4
        circularQueue.enQueueItemOnRear(4);
        // 读取队尾元素，并打印该元素4
        System.out.println(circularQueue.getItemOnRear());
    }
}

/**
 * 自定义的循环队列类型
 */
class MyCircularQueue {
    // 底层数据结构
    int[] itemArr;

    // 准备需要的指针
    int frontCursor;
    int rearCursor;

    // 准备需要的其他变量
    int capacity;

    public MyCircularQueue(int expectedCapacity) { // k为程序员指定的容量
        // 使用k来初始化capacity
        // 实际容量其实是指定容量+1 因为流出了一个空位置给rear指针
        capacity = expectedCapacity +1;
        itemArr = new int[capacity];

        // 初始化指针
        frontCursor = 0;
        rearCursor = 0;
    }

    /**
     * 在队尾入队新的元素
     * @param item
     * @return
     */
    public boolean enQueueItemOnRear(int item) {
        if (isFull()) {
            return false;
        }

        itemArr[rearCursor] = item;
        // 把 rear指针 后移一位，使得rear指针 指向最后一个元素的下一个位置
        // 如果rear指针指向数组靠后的位置，对capacity取余的操作 会得到“rear指针重置”的效果
        rearCursor = (rearCursor + 1) % capacity;

        return true;
    }

    /**
     * 辅助API：判断队列是否已经满员
     */
    public boolean isFull() {
        // 判断 rear指针的下一个位置 是不是 front指针
        return (rearCursor + 1) % capacity == frontCursor;
    }

    /**
     * 从队尾删除元素
     * @return
     */
    boolean deQueueItemFromFront(){
        if (isEmpty()) {
            return false;
        }

        // 如何从数组中删除元素呢？ 好像就只有覆盖操作了
        // 这里只是从数组中对元素进行逻辑删除（移动下指针）
        frontCursor = (frontCursor + 1) % capacity;
        return true;
    }

    public boolean isEmpty(){
        // 数组为空时，front指针与rear指针指向同一个位置
        return frontCursor == rearCursor;
    }

    /**
     * 读取队尾元素
     */
    int getItemOnRear(){
        if (isEmpty()) {
            return -1;
        }
        // expr1:由于实现循环队列的手段是：把队尾标识指针rear重新指向数组首元素，所以(rear - 1)的做法可能出现负数下标
        // 手段： (rear - 1 + 数组容量) % 数组容量  因为rear指向 最后一个元素的下一个位置
        // 特征：为了避免「队列为空」和「队列为满」的判别条件冲突，我们有意浪费了一个位置。
        // 原理：如果出现负下标，通过这种计算方式，rear指针会被“重置到”数组末尾————从而得到预期的元素
        return itemArr[(rearCursor - 1 + capacity) % capacity];
    }

    /**
     * 获取队首元素
     */
    public int getItemOnFront() {
        if (isEmpty()) {
            return -1;
        }
        // front指针就没有什么幺蛾子了
        return itemArr[frontCursor];
    }
}