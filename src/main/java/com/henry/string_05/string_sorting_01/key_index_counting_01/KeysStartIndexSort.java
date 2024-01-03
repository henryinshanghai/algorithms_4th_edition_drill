package com.henry.string_05.string_sorting_01.key_index_counting_01;

// 验证：可以使用 “键索引计数法” 来 实现一堆字符串集合的组间排序&组内排序（非完全有序）；
// 基础原理：组号按照自然数顺序排列，因此组号本身是有序的
// 手段：对“索引相同”的键，进行计数 来 得到 该键在最终排序结果中的位置；
// 特征：最终排序结果中，#1 索引按序排列（组间有序）； #2 键之间的相对顺序 与 在原始序列中相同（组内未必有序）
// 键：学生的名字； 索引：学生的组号； 对“索引相同的键”（组号相同的学生）进行计数 来 得到“任意键”（学生）在最终序列中的位置
public class KeysStartIndexSort {
    public static void main(String[] args) {
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

        int studentAmount = studentArr.length;
        int biggestGroupNo = 5;

        Student[] aux = new Student[studentAmount];
        // 这里之所以记录 groupNo+1 -> groupSize的映射关系，纯粹是为了 之后能够方便地转化为 groupNo->itsStartSpot
        int[] groupNoPlus1ToGroupSizeArr = new int[biggestGroupNo + 1];
        int[] groupNoToItsStartSpotInResultSequence = new int[biggestGroupNo + 1];

        // #1 统计各个组号出现的频率,得到 groupNoPlus1ToGroupSizeArr[]
        for (int currentSpot = 0; currentSpot < studentAmount; currentSpot++) {
            int groupNoOfCurrentStudent = studentArr[currentSpot].getGroupNo();
            // 统计 当前组号中元素的总数量，并把结果 放置在 groupNo+1的位置上 - 为了方便地在#2中把频率转化为索引
            groupNoPlus1ToGroupSizeArr[groupNoOfCurrentStudent + 1]++;
        }

        // #2 把“组号出现的频率”转化为“该组号在最终排序结果中的起始索引”, 得到 groupNoPlus1ToGroupSizeArr[]
        // 手段：从左往右，把当前groupNo上的groupSize给累加到下一个groupNo上；
        for (int currentGroupNo = 0; currentGroupNo < groupNoToItsStartSpotInResultSequence.length; currentGroupNo++) {
            // 初始化 “当前组”->“当前组的第一个元素在最终结果中的起始位置”的映射关系 🐖 初始值并不正确
            groupNoToItsStartSpotInResultSequence[currentGroupNo] = groupNoPlus1ToGroupSizeArr[currentGroupNo];
        }

        for (int currentGroupNo = 0; currentGroupNo < biggestGroupNo; currentGroupNo++) {
            // 当前元素的值 = “当前元素”的当前值 + “其前一个元素”的值
            groupNoToItsStartSpotInResultSequence[currentGroupNo + 1] += groupNoToItsStartSpotInResultSequence[currentGroupNo];
        }

        // #3 得到有序的辅助数组aux
        // 手段：对于原始数组a[]的当前元素，使用 groupNoPlus1ToGroupSizeArr[] 来 确定 它会具体被排定到 aux[]的什么位置；
        for (int currentSpot = 0; currentSpot < studentAmount; currentSpot++) {
            // 正确语句👇 示例 - 重构改变了原始语句的语义~~~
//            aux[groupNoPlus1ToGroupSizeArr[studentArr[currentSpot].getGroupNo()]++] = studentArr[currentSpot];

            /*
                原因：
                #1 变量之间进行赋值时，相当于为内存地址添加了一个新的别名；
                #2 为变量绑定新的值时，只是把变量本身指向了新的内存地址；

                如下，如果提取变量后，对变量进行++，则：只是改变了变量所指向的值，而没有改变数组元素的值（这才是我们真正想要的）
             */
            // #1 获取当前位置上元素的组号
            int groupNoOfCurrentStudent = studentArr[currentSpot].getGroupNo();
            // #2 获取到 该组号在“最终排序结果中 第一个元素所在的位置”
            int startSpotForCurrentStudent = groupNoToItsStartSpotInResultSequence[groupNoOfCurrentStudent];
            // #3 在辅助数学组“该组号第一个元素所在的位置”上，绑定 原始序列“当前位置上的元素”
            aux[startSpotForCurrentStudent] = studentArr[currentSpot];

            // #4 更新“组号中的元素在最终排序结果中的位置” 来 正确地绑定 组中的下一个元素
            groupNoToItsStartSpotInResultSequence[groupNoOfCurrentStudent]++;
        }

        // #4 把辅助数组中的数据回写到原数组
        for (int currentSpot = 0; currentSpot < studentAmount; currentSpot++) {
            studentArr[currentSpot] = aux[currentSpot];
        }

        // 打印数组中的元素
        for (Student currentStudent : studentArr) {
            System.out.println(currentStudent.getGroupNo() + " -> " + currentStudent.getName());
        }
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

