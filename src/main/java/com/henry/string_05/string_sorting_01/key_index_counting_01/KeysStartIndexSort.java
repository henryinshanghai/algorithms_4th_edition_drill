package com.henry.string_05.string_sorting_01.key_index_counting_01;

// 基础原理：组号按照自然数顺序排列，因此组号本身是有序的
public class KeysStartIndexSort {
    public static void main(String[] args) {
        Student[] studentArr = {
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
        int[] groupNoToItsStartIndexArr = new int[biggestGroupNo + 1];

        // #1 统计各个组号出现的频率,得到 groupNoPlus1ToGroupSizeArr[]
        for (int currentSpot = 0; currentSpot < studentAmount; currentSpot++) {
            int groupNoOfCurrentStudent = studentArr[currentSpot].getGroupNo();
            // 统计 当前组号中元素的总数量，并把结果 放置在 groupNo+1的位置上 - 为了方便地在#2中把频率转化为索引
            groupNoToItsStartIndexArr[groupNoOfCurrentStudent + 1]++;
        }

        // #2 把“组号出现的频率”转化为“该组号在最终排序结果中的起始索引”, 得到 groupNoToItsStartIndexArr[]
        // 手段：从左往右，把当前groupNo上的groupSize给累加到下一个groupNo上；
        for (int currentGroupNo = 0; currentGroupNo < biggestGroupNo; currentGroupNo++) {
            groupNoToItsStartIndexArr[currentGroupNo + 1] += groupNoToItsStartIndexArr[currentGroupNo];
        }

        // #3 得到有序的辅助数组aux
        // 手段：对于原始数组a[]的当前元素，使用 groupNoToItsStartIndexArr[] 来 确定 它会具体被排定到 aux[]的什么位置；
        for (int currentSpot = 0; currentSpot < studentAmount; currentSpot++) {
            // 正确语句👇 示例 - 重构改变了原始语句的语义~~~
//            aux[groupNoToItsStartIndexArr[studentArr[currentSpot].getGroupNo()]++] = studentArr[currentSpot];

            /*
                原因：
                #1 变量之间进行赋值时，相当于为内存地址添加了一个新的别名；
                #2 为变量绑定新的值时，只是把变量本身指向了新的内存地址；

                如下，如果提取变量后，对变量进行++，则：只是改变了变量所指向的值，而没有改变数组元素的值（这才是我们真正想要的）
             */
            int groupNoOfCurrentStudent = studentArr[currentSpot].getGroupNo();
            // 🐖 这里需要先为它添加一个新的别名变量（optional）...
            int startIndexForCurrentStudent = groupNoToItsStartIndexArr[groupNoOfCurrentStudent];
            aux[startIndexForCurrentStudent] = studentArr[currentSpot];
            // 再改变数组元素的值(mandatory)
            groupNoToItsStartIndexArr[groupNoOfCurrentStudent]++;
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

