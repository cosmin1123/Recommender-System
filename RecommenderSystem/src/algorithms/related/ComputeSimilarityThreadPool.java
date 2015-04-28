package algorithms.related;

import utils.Item;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by didii on 4/21/15.
 */
public class ComputeSimilarityThreadPool extends Thread{
    private int numThread;
    private static Item targetItem;
    private static HashMap<String,Double> idfMap;
    private static HashMap<Double, LinkedList<Item>> target;

    private  LinkedList<Thread> threadList;
    private static LinkedList<Item> pool;

    private static int currentIndex;

    public void addToPool(LinkedList<Item> itemList) {
        pool = itemList;
    }

    private static synchronized void addToTarget(Double similarity, Item item) {
        if(!target.containsKey(similarity)) {
            target.put(similarity, new LinkedList<Item>());
        }

        (target.get(similarity)).add(item);
    }

    public ComputeSimilarityThreadPool(int numThread, LinkedList items,
                                       HashMap target, HashMap idfMap, Item targetItem) {
        this.numThread = numThread;
        addToPool(items);
        ComputeSimilarityThreadPool.target = target;
        ComputeSimilarityThreadPool.idfMap = idfMap;
        ComputeSimilarityThreadPool.targetItem = targetItem;
        currentIndex = 0;
    }

    public ComputeSimilarityThreadPool() {
        super();
    }

    public void startThreads() {
        threadList = new LinkedList<Thread>();
        int numItems = pool.size() / numThread;

        for(int i = 0; i < numThread; i++) {
            Thread tmpThread;
            tmpThread = new ComputeSimilarityThreadPool();


            threadList.add(tmpThread);
            tmpThread.start();
        }
    }

    public void waitToFinish() {
        try {
            for(int i = 0; i < numThread; i++) {
                threadList.get(i).join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized Item getItem() {
        if(currentIndex >= pool.size()) {
            return null;
        }
        return pool.get(currentIndex++);
    }

    public void run() {
        Item item = getItem();
        while (item != null) {
            Double similiarity = ComputeSimilarity.getArticleSimilarity(targetItem, item, idfMap);
            addToTarget(similiarity, item);
            item = getItem();
        }

    }
}
