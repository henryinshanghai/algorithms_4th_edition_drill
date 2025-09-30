package com.henry.string_05.search_substring_03.KMP_01.execution.side_knowledge;

// 验证：可以 把一个 char类型的变量 作为 int类型 使用 - 可以 把 char变量 视为一个 取值范围更小的int
// 参考：https://www.cnblogs.com/liulaolaiu/p/11744410.html
// 区分：#1 按照 ASCII码表 对int<->char 进行转换； #2 按照字面值 对int<->char 进行转换；
// 前者 97 <-> 'a' 49 <-> '1', 后者 '1' <-> 1
// 总结：字符转数字 用-‘0’，数字转字符 用+‘0’
public class CharToIntDemo {
    public static void main(String[] args) {
        // char类型变量的定义 与 用法
        // 为 char类型的变量 绑定一个 int类型的值
        char c1 = 97;
        System.out.println(c1); // a 打印结果为a - 说明 对 int类型的值 自动进行了 int -> char的类型转换（ASCII码字符）

        // 为 int类型的变量 绑定一个 char类型的值
        int num1 = 'a';
        System.out.println(num1); // 打印结果为97 - 说明 对 char类型的值 自动进行了 char -> int的类型转换(ASCII码表)

        // 在 表达式 中 混用 int值 与 char值
        char c2 = 'a' + 1;
        int num2 = 'a' + 1;
        System.out.println("c2:          " + c2); // 打印 char值
        System.out.println("num2:        " + num2); // 打印 int值
        System.out.println("(char) num2: " + (char) num2); // 打印 char值

        // char类型变量的取值范围 => 0 至 2 ^ 32 -1，即 0 - 65535; 用十六进制码来看，则为：’\u0000’ - ‘\uffff’;
        char c3 = 65535;
        System.out.println(c3);
//        char c4 = 65536; // 编译报错 需要一个char值，但却提供了一个int值
//        System.out.println(c4);

        // 变量之间的赋值
        // 把 int变量 赋值给 char类型变量
        int num5 = 97;
//        char c5 = num5; // 编译报错 需要一个char值，但却提供了一个int值。因为 int变量 转换成 char变量时 可能会有损失
        char c5 = (char) num5; // 强制类型转换，可以 消除编译报错

        // 把 char变量 赋值给 int类型变量
        char c6 = 97;
        int num6 = c6;
        System.out.println("c6:   " + c6); // 打印char值 a
        System.out.println("num6: " + num6); // 打印int值 97

        // 把 char类型的变量 用作int
        // 特征：隐式转换时，会按照 ASCII表 进行转换 aka 'a'会 在这里 作为97 被使用
        char character = 'a';
        int[] intArr = new int[256];
        intArr[character] = 100;
//        printArr(intArr);

        /* 字符 与 int之间的转换 */
        // 字符 -> int
        char numChar = '1';
        // 如果采用 隐式转换，则：会按照 ASCII表的映射关系 进行转换
        intArr[numChar] = 999;
//        printArr(intArr);

        // 如果想要 按照字面值 进行转换的话，则: 可以把 char变量 放在一个 算术表达式中
        // 手段#1 使用 '1' - '0' 的方式, 能够得到 int 1；奇怪的是， + '0'的方式就不成
//        intArr[numChar - '0'] = 555;
        // 手段#2 借助工具类 Character
        intArr[Character.getNumericValue(numChar)] = 555;
//        printArr(intArr);

        // int -> 字符
        int charNum = 1;
        // 手段#1 使用 '1' + '0' 的方式, 能够得到 char '1'；奇怪的是， - '0'的方式就不能够
        System.out.println((char) charNum);
        char c10 = (char) (charNum + '0');
        System.out.println(c10);
//        System.out.println((char) (charNum - '0'));
        /* 总结：字符转数字用-‘0’，数字转字符用+‘0’ */
    }

    private static void printArr(int[] intArr) {
        for (int i = 0; i < intArr.length; i++) {
            System.out.println(i + " -> " + intArr[i]);
        }
    }
}
