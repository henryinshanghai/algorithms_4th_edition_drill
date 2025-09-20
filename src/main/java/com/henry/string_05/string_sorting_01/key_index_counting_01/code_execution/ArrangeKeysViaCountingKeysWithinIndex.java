package com.henry.string_05.string_sorting_01.key_index_counting_01.code_execution;

// 验证：可以使用 “键索引计数法” 来 实现一堆字符串集合的 组间排序&组内元素相对顺序不变（元素非完全有序）；
// 基础原理：组号 按照 自然数顺序 排列，因此 组号本身是有序的
// 手段：对“索引相同”的键，进行计数 来 得到 该键在“最终排列结果序列”中的位置；
// 特征：最终“排列结果”中，#1 索引按序排列（组间有序）； #2 键之间的相对顺序 与 在原始序列中相同（组内未必有序）
// 键：学生的组号； 索引：学生元素在结果序列中的位置； 对“键相同的元素”（组号相同的学生）进行计数 来 得到任意元素（学生）在最终序列中的位置
// 一句话描述：为了从无序元素序列，得到“组间有序、组内相对顺序与原始序列相同”的结果序列，可以通过对组中元素计数，并据此累加出“组内第一个元素”的排定位置
public class ArrangeKeysViaCountingKeysWithinIndex {
    public static void main(String[] args) {
        Student[] studentArr = prepareOriginalSequence();

        int studentAmount = studentArr.length;
        int biggestGroupNo = 5;

        Student[] aux = new Student[studentAmount];
        // 这里 之所以记录 groupNo+1 -> groupSize的映射关系，纯粹是为了 之后能够 方便地转化为 groupNo->itsStartSpot
        int[] groupNoPlus1ToGroupSizeArr = new int[biggestGroupNo + 1];
        int[] groupNoToItsStartSpotInResultSequence = new int[biggestGroupNo + 1];

        // #1 统计 各个组号 出现的频率,得到 groupNoPlus1ToGroupSizeArr[]
        for (int currentSpot = 0; currentSpot < studentAmount; currentSpot++) {
            int groupNoOfCurrentStudent = studentArr[currentSpot].getGroupNo();
            // 统计 当前组号中元素的总数量，并 把结果放置在 groupNo+1的位置上 - 为了方便地在#2中 把 频率 转化为 索引
            groupNoPlus1ToGroupSizeArr[groupNoOfCurrentStudent + 1]++;
        }

        printFrequencyArr(groupNoPlus1ToGroupSizeArr);

        // #2 把 组号出现的频率 转化为 该组号 在最终排列结果中的起始索引位置, 得到 groupNoPlus1ToGroupSizeArr[]
        // 手段：从左往右，把 当前groupNo上的groupSize 给累加到 下一个groupNo上；
        calculateGroupsStartSpotArr(groupNoPlus1ToGroupSizeArr, groupNoToItsStartSpotInResultSequence);

        // #3 得到 有序的辅助数组aux
        // 手段：对于 原始数组a[]的当前元素，使用 groupNoPlus1ToGroupSizeArr[] 来 确定 它具体会被排定到 aux[]的什么位置；
        for (int currentSpot = 0; currentSpot < studentAmount; currentSpot++) {
            // ① 在 辅助数组aux 中，排定 当前位置上的元素。并返回 被排定元素所属的组
            int groupNoOfCurrentStudent = arrangeCurrentItemInto(aux, groupNoToItsStartSpotInResultSequence, studentArr[currentSpot]);

            // ② 然后 更新“该组号中的元素 在最终结果序列中的位置”（+1） 以便能够 正确地排定 组中的下一个元素
            // 🐖 这里的startSpot就是所谓的“索引”
            groupNoToItsStartSpotInResultSequence[groupNoOfCurrentStudent]++;
        }

        printTransformedArr(groupNoToItsStartSpotInResultSequence);

        // #4 把 辅助数组中的数据 回写到 原数组
        copyItemBackTo(studentArr, aux);

        // 打印数组中的元素
        printItemIn(studentArr);
    }

    private static void printFrequencyArr(int[] groupNoPlus1ToGroupSizeArr) {
        System.out.println("~ 打印 组号->组中元素数量 的映射关系~");
        for (int currentGroupNo = 0; currentGroupNo < groupNoPlus1ToGroupSizeArr.length - 1; currentGroupNo++) {
            System.out.println("key为" + currentGroupNo + "的组中，有" + groupNoPlus1ToGroupSizeArr[currentGroupNo + 1] + "个元素");
        }
    }

    private static void printTransformedArr(int[] groupNoToItsStartSpotInResultSequence) {
        System.out.println("= 打印关键数组 count[]中的元素 =");
        for (int currentGroupNo = 0; currentGroupNo < groupNoToItsStartSpotInResultSequence.length; currentGroupNo++) {
            System.out.println("key为" + (currentGroupNo + 1) + "的元素，它们 在结果序列中所排定的开始位置 为：" + groupNoToItsStartSpotInResultSequence[currentGroupNo]);
        }
    }

    private static void calculateGroupsStartSpotArr(int[] groupNoPlus1ToGroupSizeArr, int[] groupNoToItsStartSpotInResultSequence) {
        initGroupStartSpot(groupNoPlus1ToGroupSizeArr, groupNoToItsStartSpotInResultSequence);
        updateGroupStartSpot(groupNoToItsStartSpotInResultSequence);
    }

    private static Student[] prepareOriginalSequence() {
        Student[] studentArr = {
                // 🐖 这里 “键之间的相对顺序”就已经是“按自然字母表有序”的了
                new Student(2, "Anderson"),
                new Student(3, "Brown"),
                new Student(3, "Davis"),
                new Student(4, "Garcia"),
                new Student(1, "Harris"),
                new Student(3, "Jackson"),
                new Student(4, "Johnson"),
                new Student(3, "Jones"),
                new Student(1, "Martin"),
                new Student(2, "Martinez"),
                new Student(2, "Miller"),
                new Student(1, "Moore"),
                new Student(2, "Robinson"),
                new Student(4, "Smith"),
                new Student(3, "Taylor"),
                new Student(4, "Thomas"),
                new Student(4, "Thompson"),
                new Student(2, "White"),
                new Student(3, "Williams"),
                new Student(4, "Wilson")};
        return studentArr;
    }

    private static void updateGroupStartSpot(int[] groupNoToItsStartSpotInResultSequence) {
        int biggestGroupNo = groupNoToItsStartSpotInResultSequence.length - 1;
        for (int currentGroupNo = 0; currentGroupNo < biggestGroupNo; currentGroupNo++) { // biggestGroupNo
            // 计算 当前元素值 的递推公式：当前元素的值 = “当前元素”的当前值 + “其前一个元素”的值
            groupNoToItsStartSpotInResultSequence[currentGroupNo + 1] += groupNoToItsStartSpotInResultSequence[currentGroupNo];
        }
    }

    private static void initGroupStartSpot(int[] groupNoPlus1ToGroupSizeArr, int[] groupNoToItsStartSpotInResultSequence) {
        for (int currentGroupNo = 0; currentGroupNo < groupNoToItsStartSpotInResultSequence.length; currentGroupNo++) {
            // 初始化 “当前组” -> “当前组的第一个元素 在最终排列结果中的起始位置”的映射关系 🐖 这里的初始值 并不正确
            groupNoToItsStartSpotInResultSequence[currentGroupNo] = groupNoPlus1ToGroupSizeArr[currentGroupNo];
        }
    }

    private static void copyItemBackTo(Student[] originalArr, Student[] auxArr) {
        for (int currentSpot = 0; currentSpot < originalArr.length; currentSpot++) {
            originalArr[currentSpot] = auxArr[currentSpot];
        }
    }

    private static void printItemIn(Student[] studentArr) {
        for (Student currentStudent : studentArr) {
            System.out.println(currentStudent.getGroupNo() + " -> " + currentStudent.getName());
        }
    }

    private static int arrangeCurrentItemInto(Student[] aux, int[] groupNoToItsStartSpotInResultSequence, Student studentOnSpot) {
        // 正确语句👇 示例 - 重构改变了原始语句的语义~~~
//            aux[groupNoPlus1ToGroupSizeArr[studentArr[currentSpot].getGroupNo()]++] = studentArr[currentSpot];

            /*
                原因：
                #1 变量之间进行赋值时，相当于为内存地址添加了一个新的别名；
                #2 为变量绑定新的值时，只是把变量本身指向了新的内存地址；

                如下，如果提取变量后，对变量进行++，则：只是改变了变量所指向的值，而没有改变数组元素的值（这才是我们真正想要的）
             */
        // #1 获取当前位置上元素的组号  item ->> its key
        int groupNoOfCurrentStudent = studentOnSpot.getGroupNo();
        // #2 获取到 该组号在“最终排序结果中 第一个元素所在的位置” key -> its start spot
        int startSpotForCurrentStudent = groupNoToItsStartSpotInResultSequence[groupNoOfCurrentStudent];
        // #3 在辅助数学组“该组号第一个元素所在的位置”上，绑定 原始序列“当前位置上的元素” spot -> item
        aux[startSpotForCurrentStudent] = studentOnSpot;

        return groupNoOfCurrentStudent;
    }


}

class Student {
    int groupNo;
    String name;

    public int getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(int groupNo) {
        this.groupNo = groupNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Student(int groupNo, String name) {
        this.groupNo = groupNo;
        this.name = name;
    }
}

