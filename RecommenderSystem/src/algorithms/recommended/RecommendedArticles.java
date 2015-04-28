package algorithms.recommended;

import algorithms.recommended.CorelatedUser;
import database.Database;
import database.TableName;
import javafx.util.Pair;
import utils.Item;
import utils.User;
import utils.Utils;

import java.util.*;

/**
 * Created by didii on 3/10/15.
 */
public class RecommendedArticles {
    private static final int MAX_USER_CLUSTER = 30;

    private static Pair<Double, Double> getUserCommonMeanRating(String userId,
                                                                String referenceUserId,
                                                                LinkedList<Item> itemList) {
        double meanUser = 0;
        double meanReferenceUser = 0;
        int num = 0;

        for(Item item : itemList) {

            Double userItemRating =  item.getRating().get(userId);
            Double referenceUserItemRating =  item.getRating().get(referenceUserId);

            if(userItemRating != null && referenceUserItemRating != null) {
                meanUser = (meanUser * num + userItemRating) / (num + 1);
                meanReferenceUser = (meanReferenceUser * num + referenceUserItemRating) / (num + 1);
                num++;
            }
        }

        return new Pair<Double, Double>(meanUser, meanReferenceUser);
    }

    private static double getCorelationBetweenUsers(User user, LinkedList<Item> userItems,
                                                    User secondUser, Pair<Double, Double> meanPair) {
        double upCorelation = 0;
        double downCorelation1 = 0;
        double downCorelation2 = 0;

        double secondUserMean = meanPair.getValue();
        double userMean = meanPair.getKey();

        if(secondUserMean == 0) {
            return Double.NaN;
        }
        //System.out.println(user.getUserId() + " = " + userMean + " " + secondUser.getUserId() + " = " + secondUserMean);
        for(Item item : userItems) {
            if(secondUser.getItemsHistory().contains(item.getItemId())) {
                Double userRating = item.getRating().get(user.getUserId());
                Double secondUserRating = item.getRating().get(secondUser.getUserId());
                if(userRating == null || secondUserRating == null) {
                    continue;
                }
                upCorelation = upCorelation + (userRating - userMean) * (secondUserRating - secondUserMean);

                downCorelation1 += ((userRating - userMean) * (userRating - userMean));
                downCorelation2 += ((secondUserRating - secondUserMean) * (secondUserRating - secondUserMean));
          //      System.out.println(userRating + " " + secondUserRating);

            }
        }

        if(downCorelation1 == 0 || downCorelation2 == 0) {
            return Double.NaN;
        }
        // System.out.println("up: " + upCorelation + " " + "down: " + downCorelation1 + " down: " + downCorelation2);
        return (upCorelation) / Math.sqrt(downCorelation1 * downCorelation2);
    }

    private static Pair<Double, Double> getRatedSumAndCount(LinkedList<Item> itemList, User user) {
        double sum = 0;
        double count = 0;

        for(Item item : itemList) {
            Double itemRating = item.getRating().get(user.getUserId());

            if(itemRating != null) {
                sum += itemRating;
                count++;
            }
        }

        return new Pair<Double, Double>(sum, count);
    }

    private static LinkedList<CorelatedUser> getUserCluster(User firstUser) {
        LinkedList<User> userList = Utils.getAllUsers(TableName.USERS.toString());
        LinkedList<CorelatedUser> userCluster = new LinkedList<CorelatedUser>();

        Comparator<String> reverseDoubleComparator = new Comparator<String>() {
            @Override public int compare( String s1, String s2) {
                double val1 = Double.parseDouble(s1);
                double val2 = Double.parseDouble(s2);
                return (int)((val2 - val1) * Integer.MAX_VALUE);
            }
        };
        SortedMap<String, LinkedList<User>> sortedMap = new TreeMap<String, LinkedList<User>>(reverseDoubleComparator);

        LinkedList<Item> firstUserItems = Database.getUserItems(firstUser.getUserId());

        // save main users items
        CorelatedUser.itemList = firstUserItems;

        Pair<Double, Double> firstUserSumAndCountRating = getRatedSumAndCount(firstUserItems, firstUser);
        CorelatedUser.referenceRatedSum = firstUserSumAndCountRating.getKey();
        CorelatedUser.referenceRatedItemsCount = firstUserSumAndCountRating.getValue();

        assert userList != null;
        for(User user : userList) {
            Pair<Double, Double> meanPair =
                    getUserCommonMeanRating(firstUser.getUserId(), user.getUserId(), firstUserItems);

            Double userCorelation = getCorelationBetweenUsers(firstUser, firstUserItems, user, meanPair);

            if(Double.compare(userCorelation, Double.NaN) == 0) {
                continue;
            }

            String userCorelationKey = userCorelation.toString();

            LinkedList<User> currentList = sortedMap.get(userCorelationKey);
            if(currentList == null) {
                currentList = new LinkedList<User>();
            }

            currentList.add(user);

            sortedMap.put(userCorelationKey, currentList);
        }


        for(Object userCorelation : sortedMap.keySet()) {
            LinkedList<User> sameCorelationsUsers = sortedMap.get(userCorelation);
            for(User user : sameCorelationsUsers) {

                userCluster.add(new CorelatedUser(user, Double.parseDouble((String) userCorelation)));

                if(userCluster.size() > MAX_USER_CLUSTER) {
                    return userCluster;
                }
            }
        }
        return userCluster;


    }

    private static LinkedList<Item> recommendItems(LinkedList<CorelatedUser> corelatedUserList,
                                                   User mainUser, int maxArticle) {
        LinkedList<Item> recommendItemList = new LinkedList<Item>();
        HashMap<Item, Pair<Double, Double>> top = new HashMap<Item, Pair<Double, Double>>();
        HashMap<Double, LinkedList<Item>> presumedItemRating = new HashMap<Double, LinkedList<Item>>();

        for(CorelatedUser user : corelatedUserList) {
            LinkedList<Item> ratedItems = Database.getUserRatedItems(user.user.getUserId());
            Pair<Double, Double> userCountAndSumRatings = getRatedSumAndCount(ratedItems, user.user);

            for(Item item : ratedItems) {
                if(!mainUser.getItemsHistory().contains(item.getItemId())) {
                    double itemRating = item.getRating().get(user.user.getUserId());

                    if(!top.containsKey(item)) {
                        top.put(item, new Pair<Double, Double>((double) 0, (double) 0));
                    }
                    Pair<Double, Double> pair =  top.get(item);

                    double topValue = pair.getKey();
                    double bottomValue = pair.getValue();

                    double currentMean = (userCountAndSumRatings.getKey() - itemRating) /
                            (userCountAndSumRatings.getValue() - 1);

                    topValue += ((itemRating - currentMean) * user.corelatedScore);
                    bottomValue += Math.abs(user.corelatedScore);

                    top.put(item, new Pair<Double, Double> (topValue, bottomValue));
                }
            }
        }

        for(Item item : top.keySet()) {
            Pair<Double, Double> pairRating = top.get(item);

            Double key = pairRating.getKey() / pairRating.getValue() +
                    CorelatedUser.referenceRatedSum / CorelatedUser.referenceRatedItemsCount;

            if(!presumedItemRating.containsKey(key)) {
                presumedItemRating.put(key, new LinkedList<Item>());
            }
            LinkedList<Item> itemList = presumedItemRating.get(key);
            itemList.add(item);
            presumedItemRating.put(key, itemList);
        }

        List<Double> keys = new ArrayList<Double>(presumedItemRating.keySet());

        Collections.sort(keys);
        Collections.reverse(keys);

        for(Double rating : keys) {
            LinkedList<Item> itemLinkedList = presumedItemRating.get(rating);

            for(Item item : itemLinkedList) {
                recommendItemList.add(item);
                if(recommendItemList.size() > maxArticle) {
                    return recommendItemList;
                }
            }
        }

        return recommendItemList;
    }

    public static LinkedList<Item> recommend(String userId, int maxArticle) {
        LinkedList<Item> recommendedItems;
        User mainUser = Database.getUser(userId);
        LinkedList<CorelatedUser> userCluster = getUserCluster(mainUser);

        recommendedItems = recommendItems(userCluster, mainUser, maxArticle);

        return recommendedItems;
    }
}
