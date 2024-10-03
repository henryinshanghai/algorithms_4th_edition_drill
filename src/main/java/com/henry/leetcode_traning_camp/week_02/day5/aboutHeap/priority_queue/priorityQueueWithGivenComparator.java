package com.henry.leetcode_traning_camp.week_02.day5.aboutHeap.priority_queue;

import com.henry.leetcode_traning_camp.week_02.day5.aboutHeap.Member;

import java.util.PriorityQueue;

// 验证：当在PQ的构造器中指定 比较器时，PQ会优先使用比较器的规则 来 对元素进行“排序” - 这里的排序不是“连续升序/降序”，而是“堆升序/降序”；
public class priorityQueueWithGivenComparator {
    public static void main(String[] args) {
        // 指定比较器 - 比较器的规则(降序) 与 Member类型本身所指定的比较规则(升序) 相反
        // 手段：使用 lambda表达式；
        // 🐖 为了使lambda表达式能够识别参数类型，需要在构造器中提供泛型的具体类型
        PriorityQueue<Member> memberPriorityQueue = new PriorityQueue<Member>((member1, member2) -> member2.getVotes() - member1.getVotes());

        // 向优先队列中添加 member元素
        construct(memberPriorityQueue);
        print(memberPriorityQueue); // 元素序列 “堆有序”（降序）

        Member polledMember = memberPriorityQueue.poll(); // 28
        System.out.println("== polledMember: " + polledMember + " ==");
        print(memberPriorityQueue); // 元素序列 “堆有序”（降序）
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
