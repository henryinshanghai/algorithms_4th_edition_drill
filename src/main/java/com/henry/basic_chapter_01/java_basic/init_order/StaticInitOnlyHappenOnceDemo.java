package com.henry.basic_chapter_01.java_basic.init_order;

// 验证#1：在执行初始化时，会 先初始化”静态对象“，再初始化”非静态对象“； - Bowl(4), Bowl(5) 先于 Bowl(3)打印
// 验证#2：静态域（变量）的初始化只会执行一次 - Bowl(1), Bowl(2)只被打印了一次
// 验证#3：非静态变量的初始化会根据需要执行多次； Bowl(3) 在不同时机（加载 Cupboard类时, 使用new调用构造器时）被打印了多次
public class StaticInitOnlyHappenOnceDemo {

    public static void main(String[] args) {
        System.out.println("create new cupBoard() in main #1 ");
        new Cupboard();

        System.out.println("create new cupBoard() in main #2 ");
        new Cupboard();

        table.too(1);
        cupBoard.coo(1);
    }

    // 静态成员变量
    static Table table = new Table();
    static Cupboard cupBoard = new Cupboard();
}

class Bowl {
    Bowl(int id) {
        System.out.println("Bowl(" + id + ")");
    }

    // 实例方法
    void boo(int id) {
        System.out.println("boo(" + id + ")");
    }
}

class Table {
    // 静态成员
    static Bowl bowl01 = new Bowl(1);

    // 构造器
    Table() {
        System.out.println("Table()");
        bowl02.boo(1);
    }

    // 实例方法
    void too(int id) {
        System.out.println("too(" + id + ")");
    }

    static Bowl bowl02 = new Bowl(2);
}

class Cupboard {
    Bowl bowl03 = new Bowl(3);
    static Bowl bowl04 = new Bowl(4);

    Cupboard() {
        System.out.println("Cupboard()");
        bowl04.boo(2);
    }

    void coo(int id) {
        System.out.println("coo(" + id + ")");
    }

    static Bowl bowl05 = new Bowl(5);
}