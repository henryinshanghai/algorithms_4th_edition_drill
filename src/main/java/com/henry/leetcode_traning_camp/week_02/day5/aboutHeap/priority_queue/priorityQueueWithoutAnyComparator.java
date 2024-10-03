package com.henry.leetcode_traning_camp.week_02.day5.aboutHeap.priority_queue;

import com.henry.leetcode_traning_camp.week_02.day5.aboutHeap.Member;

import java.util.PriorityQueue;

// 验证：#1 当没有指定任何比较器的时候，优先队列 会按照”小顶堆“的规则 来 排序“可比较元素”
// #2 优先队列的poll()方法 能够 获取并移除 PQ的队首元素(最值元素)；
public class priorityQueueWithoutAnyComparator {
    public static void main(String[] args) {
        // 不指定任何的比较器
        PriorityQueue<Member> memberPriorityQueue = new PriorityQueue();

        // 向优先队列中添加 member元素
        construct(memberPriorityQueue);
        print(memberPriorityQueue);

        Member polledMember = memberPriorityQueue.poll();
        System.out.println("== polledMember: " + polledMember + " ==");
        print(memberPriorityQueue);
    }

    private static void construct(PriorityQueue<Member> memberPriorityQueue) {
        memberPriorityQueue.add(new Member("henry", 15)); // 编译报错 - 要求传入的类型 必须是一个”可比较的类型“
        memberPriorityQueue.add(new Member("ada", 28));
        memberPriorityQueue.add(new Member("ben", 19));
        memberPriorityQueue.add(new Member("xinrui", 23));
        memberPriorityQueue.add(new Member("david", 13));
        memberPriorityQueue.add(new Member("ford", 6));
        memberPriorityQueue.add(new Member("goat", 9));
    }

    private static void print(PriorityQueue<Member> memberPriorityQueue) {
        for (Member currentMember : memberPriorityQueue) {
            System.out.println(currentMember + " -> ");
        }
        System.out.println();
    }
}
