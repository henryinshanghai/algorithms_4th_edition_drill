package com.henry.symbol_table_chapter_03.implementation_02.advanced.via_hash_table.via_chaining.implementation_02;

import java.util.Date;

// 用于验证 使用hashCode()方法 来 求取对象散列值的对象
public class Transaction {
    // 交易的三要素：时间、人物、金额
    private final String who;
    private final Date when;
    private final double amount;

    public Transaction(String who, Date when, double amount) {
        this.who = who;
        this.when = when;
        this.amount = amount;
    }

    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + who.hashCode();
        hash = 31 * hash + when.hashCode();
        hash = 31 * hash + ((Double) amount).hashCode();

        return hash;
    }
}
