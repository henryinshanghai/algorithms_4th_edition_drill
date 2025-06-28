package com.henry.leetcode_traning_camp.week_04.day02.generate_parenthesis_str.apis;

// 结论：String是不可变对象，因此当使用+操作对字符串进行修改时，会得到一个新的对象
// 结论：String类型删除最后一个字符的手段 str = str.substring(0, str.length()-1);
public class StringDemo {
    public static void main(String[] args) {
        String name = "henry";
        System.out.println(name.hashCode());

        int length = name.length();
        System.out.println(name.hashCode());

        // 在经过加法操作后，变量所指向的内存地址发生了变化。因此需要把新对象 重新赋值给name
        name = name + "T";
        System.out.println(name.hashCode());
        System.out.println(name);

        // String类型删除最后一个字符；手段：substring(0, length()-1),这同样会得到一个新对象
        name = name.substring(0, name.length() - 1);
        System.out.println(name.hashCode());
        System.out.println(name);
    }
}
