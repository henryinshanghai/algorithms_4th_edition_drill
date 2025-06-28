package com.henry.leetcode_traning_camp.week_04.day02.generate_parenthesis_str.method_parameter_value;

// 结论：当参数是一个引用类型/对象时，传递给方法的 是对象引用的一个副本（而不是对象本身的副本）
// 由于 引用的副本 与 原始引用 指向的都是同一个对象，因此 使用它修改对象时，修改的就是原始对象
public class passObjectDemo {
    public static void main(String[] args) {
        Student student = new Student(28, "henry");
        System.out.println(student); // henry

        modifyObject(student);

        System.out.println("~~~");
        System.out.println(student); // #1 jane | #2 henry
    }

    // 当参数是一个引用类型/对象时，传递给方法的 是对象引用的一个副本（而不是对象本身的副本）
    private static void modifyObject(Student student) {
        // #1 在方法中修改对象，其实是对原始对象的修改
        student.setName("Jane");

        // #2 在方法中 把引用指向其他的对象，不会对原始对象产生影响
//        student = new Student(29, "Jack");
//        System.out.println("在方法中修改引用所指向的对象：" + student);
    }
}
