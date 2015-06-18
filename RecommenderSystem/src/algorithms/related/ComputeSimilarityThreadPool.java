package algorithms.related;

import utils.Item;
import utils.SimilarityWeights;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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
    private SimilarityWeights similarityWeights;

    public void addToPool(LinkedList<Item> itemList) {
        pool = itemList;
    }

    private static synchronized void addToTarget(HashMap<Double, LinkedList<Item>> myTarget) {
        target.putAll(myTarget);
    }

    public ComputeSimilarityThreadPool(int numThread, LinkedList items,
                                       HashMap target, HashMap idfMap, Item targetItem,
                                       SimilarityWeights similarityWeights) {
        this.numThread = numThread;
        addToPool(items);
        ComputeSimilarityThreadPool.target = target;
        ComputeSimilarityThreadPool.idfMap = idfMap;
        ComputeSimilarityThreadPool.targetItem = targetItem;
        currentIndex = 0;
        this.similarityWeights = similarityWeights;
    }

    public ComputeSimilarityThreadPool(SimilarityWeights similarityWeights) {
        this.similarityWeights = similarityWeights;
    }

    public void startThreads() {
        threadList = new LinkedList<Thread>();
        int numItems = pool.size() / numThread;

        for(int i = 0; i < numThread; i++) {
            Thread tmpThread;
            tmpThread = new ComputeSimilarityThreadPool(similarityWeights );


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

    public synchronized void getItems(List<Item> list) {
        int myListSize = 1000;
        int endOfList = currentIndex + myListSize;
        if(endOfList >= pool.size()) {
            endOfList = pool.size() - 1;
        }

        if(currentIndex >= endOfList) {
            return ;
        }
        list.addAll(pool.subList(currentIndex, endOfList));
        currentIndex = endOfList;
        return;
    }

    public void run() {
        LinkedList<Item> myPool = new LinkedList<Item>();
        getItems(myPool);
        HashMap<Double, LinkedList<Item>> myTarget = new HashMap<Double, LinkedList<Item>>();
        Item item;
        while (myPool.size() != 0) {
            // get item
            item = myPool.pop();
            if(myPool.size() == 0) {
                getItems(myPool);
            }

            // compute similarity and add it to target
            Double similarity = ComputeSimilarity.getArticleSimilarity(targetItem, item, idfMap, similarityWeights);
            item.setScore(similarity);
            //

            if(!myTarget.containsKey(similarity)) {
                myTarget.put(similarity, new LinkedList<Item>());
            }

            (myTarget.get(similarity)).add(item);


        }
        addToTarget(myTarget);

    }
}
