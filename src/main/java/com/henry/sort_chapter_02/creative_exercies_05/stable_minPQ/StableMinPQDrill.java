package com.henry.sort_chapter_02.creative_exercies_05.stable_minPQ;

public class StableMinPQDrill<Item extends Comparable<Item>> {

    private Item[] spotToItemArray;
    private int[] spotToTimestampArray;

    private int itemAmount;
    private int timestamp = 1;

    public StableMinPQDrill(int initCapacity) {
        spotToItemArray = (Item[]) new Comparable[initCapacity];
        spotToTimestampArray = new int[initCapacity];

        itemAmount = 0;
    }

    public void insert(Item item) {
        if (itemAmount == spotToItemArray.length - 1) resize(spotToItemArray.length * 2);
        itemAmount++;

        spotToItemArray[itemAmount] = item;
        spotToTimestampArray[itemAmount] = ++timestamp;

        swim(itemAmount);
    }

    private void resize(int newCapacity) {
        Item[] tempSpotToItemArray = (Item[]) new Comparable[newCapacity];
        int[] tempSpotToTimestampArray = new int[newCapacity];

        for (int current = 1; current <= itemAmount; current++) {
            tempSpotToItemArray[current] = spotToItemArray[current];
        }
        for (int current = 1; current <= itemAmount; current++) {
            tempSpotToTimestampArray[current] = spotToTimestampArray[current];
        }

        spotToItemArray = tempSpotToItemArray;
        spotToTimestampArray = tempSpotToTimestampArray;
    }

    private void swim(int currentNodeSpot) {
        while (currentNodeSpot > 1 && less(currentNodeSpot, currentNodeSpot / 2)) {
            exch(currentNodeSpot, currentNodeSpot / 2);
            currentNodeSpot = currentNodeSpot / 2;
        }
    }

    private void exch(int spotI, int spotJ) {
        Item temp = spotToItemArray[spotI];
        spotToItemArray[spotI] = spotToItemArray[spotJ];
        spotToItemArray[spotJ] = temp;

        int tempTimestamp = spotToTimestampArray[spotI];
        spotToTimestampArray[spotI] = spotToTimestampArray[spotJ];
        spotToTimestampArray[spotJ] = tempTimestamp;
    }

    private boolean less(int spotI, int spotJ) {
        int result = spotToItemArray[spotI].compareTo(spotToItemArray[spotJ]);
        if (result < 0) return true;
        if (result > 0) return false;
        return spotToTimestampArray[spotI] < spotToTimestampArray[spotJ];
    }

    public Item delMin() {
        Item minItem = spotToItemArray[1];

        exch(1, itemAmount--);
        sink(1);

        return minItem;
    }

    private void sink(int currentNodeSpot) {
        while (currentNodeSpot * 2 < itemAmount) {
            int smallerChildSpot = currentNodeSpot * 2;
            if (less(smallerChildSpot + 1, smallerChildSpot )) smallerChildSpot++;

            if (!less(smallerChildSpot, currentNodeSpot)) break;

            exch(currentNodeSpot, smallerChildSpot);
            currentNodeSpot = smallerChildSpot;
        }
    }

    // 定义静态内部类 Tuple
    private static final class Tuple implements Comparable<Tuple> {
        private String name;
        private int id;

        public Tuple(String name, int id) {
            this.name = name;
            this.id = id;
        }


        @Override
        public int compareTo(Tuple that) {
            return this.name.compareTo(that.name);
        }

        @Override
        public String toString() {
            return name + " " + id;
        }
    }

    public boolean isEmpty() {
        return itemAmount == 0;
    }

    public static void main(String[] args) {
        String text = "it was the best of times it was the worst of times it was the "
                + "age of wisdom it was the age of foolishness it was the epoch "
                + "belief it was the epoch of incredulity it was the season of light "
                + "it was the season of darkness it was the spring of hope it was the "
                + "winter of despair";
        String[] strings = text.split(" ");

        StableMinPQDrill<Tuple> stableMinPQ = new StableMinPQDrill<>(10);

        for (int current = 0; current < strings.length; current++) {
            stableMinPQ.insert(new Tuple(strings[current], current));
        }

        while (!stableMinPQ.isEmpty()) {
            Tuple currentMinTuple = stableMinPQ.delMin();
            System.out.println(currentMinTuple);
        }
    }
}
