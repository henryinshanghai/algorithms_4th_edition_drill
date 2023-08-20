package com.henry.basic_chapter_01.java_basic.init_order;

// 验证：在调用构造方法之前，类中所有的成员变量会先被初始化
// 手段：不管成员变量（window123）如何分布，成员变量初始化的print语句，都会 先于构造方法中的print语句打印
public class AutoInitBeforeInvokeConstructorDemo {

    public static void main(String[] args) {
        // Ⅰ - 使用new来创建对象
        House house = new House();

        // Ⅱ - 调用实例方法
        house.foo();
    }
}

class Window {
    Window(int id) {
        System.out.println("Window(" + id + ")");
    }
}

class House {
    // 成员变量 Ⅰ-①
    Window window1 = new Window(1);

    // 构造方法
    House() {
        System.out.println("House");
        window3 = new Window(3);
    }

    // 成员变量 Ⅰ-②
    Window window2 = new Window(2);

    // 实例方法 Ⅱ
    void foo() {
        System.out.println("foo");
    }

    // 成员变量 Ⅰ-③
    Window window3 = new Window(3);
}