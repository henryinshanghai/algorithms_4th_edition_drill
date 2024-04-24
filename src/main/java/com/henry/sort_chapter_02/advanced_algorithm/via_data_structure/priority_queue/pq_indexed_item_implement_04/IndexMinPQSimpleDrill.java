package com.henry.sort_chapter_02.advanced_algorithm.via_data_structure.priority_queue.pq_indexed_item_implement_04;

public class IndexMinPQSimpleDrill<Item extends Comparable<Item>> {

    private Item[] indexToItemArray;
    private int[] spotToIndexArray;
    private int[] indexToSpotArray;

    private int itemAmount;

    public IndexMinPQSimpleDrill(int initCapacity) {
        indexToItemArray = (Item[]) new Comparable[initCapacity + 1];
        spotToIndexArray = new int[initCapacity + 1];
        indexToSpotArray = new int[initCapacity + 1];

        for (int i = 0; i < indexToSpotArray.length; i++) {
            indexToSpotArray[i] = -1;
        }
    }

    public void insert(int index, Item item) {
        itemAmount++;

        indexToItemArray[index] = item;
        spotToIndexArray[itemAmount] = index;
        indexToSpotArray[index] = itemAmount;

        swim(itemAmount);
    }

    public int delMin() {
        int indexOfMinItem = spotToIndexArray[1];
        Item minItem = indexToItemArray[indexOfMinItem];

        exch(1, itemAmount--);
        sink(1);

        // 这里需要清楚地知道怎么设置各个数组
        indexToItemArray[indexOfMinItem] = null;
        spotToIndexArray[itemAmount + 1] = -1;
        indexToSpotArray[indexOfMinItem] = -1;

        return indexOfMinItem;
    }

    private void sink(int currentNodeSpot) {
        while (currentNodeSpot * 2 < itemAmount) {
            int smallerChildSpot = currentNodeSpot * 2;
            if (greater(smallerChildSpot, smallerChildSpot+1)) smallerChildSpot++;

            if (greater(currentNodeSpot, smallerChildSpot)) exch(currentNodeSpot, smallerChildSpot);
            currentNodeSpot = smallerChildSpot;
        }
    }

    private void swim(int currentNodeSpot) {
        while (currentNodeSpot > 1 && greater(currentNodeSpot / 2, currentNodeSpot)) {
            exch(currentNodeSpot / 2, currentNodeSpot);

            currentNodeSpot = currentNodeSpot / 2;
        }
    }

    // 交换堆中 spotI位置 与 spotJ位置上的元素
    // 🐖 这里只会对 spotToIndexArray执行交换，并更新indexToSpotArray
    private void exch(int spotI, int spotJ) {
        int temp = spotToIndexArray[spotI];
        spotToIndexArray[spotI] = spotToIndexArray[spotJ];
        spotToIndexArray[spotJ] = temp;

        // 更新 indexToSpotArray
        int indexOfSpotI = spotToIndexArray[spotI];
        indexToSpotArray[indexOfSpotI] = spotI;
        int indexOfSpotJ = spotToIndexArray[spotJ];
        indexToSpotArray[indexOfSpotJ] = spotJ;
    }

    // 比较堆中 spotI位置 与 spotJ位置上的元素
    private boolean greater(int spotI, int spotJ) {
        int indexOfSpotI = spotToIndexArray[spotI];
        int indexOfSpotJ = spotToIndexArray[spotJ];

        return indexToItemArray[indexOfSpotI].compareTo(indexToItemArray[indexOfSpotJ]) > 0;
    }

    public Item minItem() {
        return indexToItemArray[spotToIndexArray[1]];
    }

    public static void main(String[] args) {
//        String[] a = {"it", "is", "only", "with", "the", "heart", "that", "one", "can", "see", "rightly"};
        String[] a = {"to", "be", "or", "not", "to", "be", "there's", "no", "try"};

        IndexMinPQSimpleDrill<String> indexMinPQ = new IndexMinPQSimpleDrill<>(11);

        for (int i = 0; i < a.length; i++) {
            indexMinPQ.insert(i, a[i]);
        }

        while (!indexMinPQ.isEmpty()) {
            int indexOfMinItem = indexMinPQ.delMin();

            System.out.println(indexOfMinItem + " -> " + a[indexOfMinItem]);
        }
    }

    public boolean isEmpty() {
        return itemAmount == 0;
    }
}
