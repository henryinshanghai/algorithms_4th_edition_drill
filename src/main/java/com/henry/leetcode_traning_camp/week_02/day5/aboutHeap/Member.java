package com.henry.leetcode_traning_camp.week_02.day5.aboutHeap;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Member implements Comparable<Member> { // 声明为”可比较的类型“ 以支持 向优先队列中添加此类型的对象
    private String name;
    private int votes;

    // 定义比较的依据（votes字段） 以及 大小规则（升序??）
    @Override
    public int compareTo(Member that) {
        return this.votes - that.votes;
    }
}
