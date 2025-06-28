package com.henry.leetcode_traning_camp.week_04.day02.generate_parenthesis_str.apis;

// 结论：StringBuilder是可变对象，因此 当使用append()进行修改时，修改的是原始对象
// 结论：StringBuilder对象，可以使用 deleteCharAt(index) 来 移除最后一个字符
public class StringBuilderDemo {
    public static void main(String[] args) {
        StringBuilder name = new StringBuilder("henry");
        System.out.println(name.hashCode());

        // 在追加了字符T之后，变量所指向的对象地址没有发生变化
        name.append("T");
        System.out.println(name.hashCode());

        // 删除最后一个字符
        name.deleteCharAt(name.length() - 1);
        System.out.println(name.hashCode());
        System.out.println(name);
    }
}
