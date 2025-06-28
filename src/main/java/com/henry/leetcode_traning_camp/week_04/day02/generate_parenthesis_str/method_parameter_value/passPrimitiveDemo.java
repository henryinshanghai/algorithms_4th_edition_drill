package com.henry.leetcode_traning_camp.week_04.day02.generate_parenthesis_str.method_parameter_value;

// 验证：当方法参数是基本类型时，传递给方法的是 实际参数值的一个新副本，因此 在方法中对此变量/参数的操作 不会影响原始对象
public class passPrimitiveDemo {
    public static void main(String[] args) {
        int a = 10;
        System.out.println("方法调用之前变量a=" + a); // 10

        modifyPrimitive(a);

        System.out.println("方法调用之后变量a=" + a); // 10
    }

    private static void modifyPrimitive(int a) {
        // 在方法种，对变量的值进行修改
        a = a + 10;
        System.out.println("方法内修改后的变量a=" + a);
    }
}
