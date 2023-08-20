package com.henry.basic_chapter_01.data_abstract;

public class Counter { // 类名
    // 实例变量
    private final String name;
    private int count;

    // 构造方法
    public Counter(String id) {
        name = id;
    }

    /* 实例方法 👇 */
    public void increment() {
        count++;
    }

    public int tally() {
        return count;
    }

    public String toString() {
        return count + " " + name;
    }

    // 测试用例 👇
    public static void main(String[] args) {
        // 创建&初始化对象
        Counter heads = new Counter("heads"); // 手段：使用new 来 出发构造方法
        Counter tails = new Counter("tails");

        heads.increment();
        heads.increment();
        tails.increment();

        // 自动调用 toString()方法
        System.out.println(heads + " " + tails);
        System.out.println(heads.tally() + tails.tally()); // 对象名.实例方法名()
    }
}
