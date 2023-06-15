package com.henry.basic_chapter_01.java_basic;

// 验证：如果方法的局部变量没有被初始化，则：使用此变量时，Java编译器会给出编译错误；
// 手段：如果删除 第8行中 = 0，则：在第10行 编译器报错 Variable 'number' might not have been initialized
public class requestInitVariableDemo {

    public void calculate() {
        int number = 0;

        number++;
    }
}
