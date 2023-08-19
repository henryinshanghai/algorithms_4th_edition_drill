package com.henry.basic_chapter_01.java_basic.init_order;

// 验证#1：在执行初始化时，会 先初始化”静态成员“，再初始化”非静态成员“,最后执行“构造方法”； - 手段：Ⅰ&Ⅱ，成员的打印顺序
// 验证#2：静态域（变量）的初始化只会执行一次 - 手段：静态成员I - {Bowl(1), Bowl(2)} & Ⅱ - {Bowl(4), Bowl(5)} 只被打印了一次
// 验证#3：非静态变量的初始化会根据需要执行多次； - 手段：Bowl(3) 在不同时机（加载 Cupboard类时, 使用new调用构造器[每次调用都会打印（Ⅲ-①     Ⅳ-①）]时）被打印了多次
public class StaticInitOnlyHappenOnceDemo {

    public static void main(String[] args) {
        System.out.println("create new cupBoard() in main #1 ");
        // Ⅲ
        new Cupboard();

        System.out.println("~~~~~~");

        System.out.println("create new cupBoard() in main #2 ");
        // Ⅳ
        new Cupboard();

        System.out.println("+++++++");

        // Ⅴ
        table.too(1);
        cupBoard.coo(1);
    }

    // 静态成员变量
    // Ⅰ
    static Table table = new Table();
    // Ⅱ
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
    // Ⅰ-①
    static Bowl bowl01 = new Bowl(1);

    // 构造器
    // Ⅰ-③
    Table() {
        System.out.println("Table()");
        bowl02.boo(1);
    }

    // 实例方法 - Ⅴ-①
    void too(int id) {
        System.out.println("too(" + id + ")");
    }

    // 静态成员
    // Ⅰ-②
    static Bowl bowl02 = new Bowl(2);
}

class Cupboard {
    // Ⅱ-③ 非静态成员    Ⅲ-①     Ⅳ-①
    Bowl bowl03 = new Bowl(3);
    // Ⅱ-① 静态成员
    static Bowl bowl04 = new Bowl(4);

    // Ⅱ-④ 构造方法     Ⅲ-②     Ⅳ-②
    Cupboard() {
        System.out.println("Cupboard()");
        bowl04.boo(2);
    }

    // 实例方法
    void coo(int id) { // Ⅴ-②
        System.out.println("coo(" + id + ")");
    }

    // Ⅱ-② 静态成员
    static Bowl bowl05 = new Bowl(5);
}