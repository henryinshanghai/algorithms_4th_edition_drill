package com.henry.basic_chapter_01.java_basic;

// 验证：在某个构造方法中，使用this来调用另一个构造方法时，必须把this()的调用语句放在第一行
// 手段：如果交换17、18行，则：会出现编译报错 - Call to 'this()' must be first statement in constructor body
public class invokeConstructorViaThisDemo {

    private class Flower {
        int patelCount;
        String name;
        String color;

        public Flower(int patelCount) {
            this.patelCount = patelCount;
        }

        public Flower(int patelCount, String name) {
            this(patelCount);
            this.name = name;
        }
    }
}
