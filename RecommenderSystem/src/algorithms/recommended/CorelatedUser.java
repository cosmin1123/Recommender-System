package algorithms.recommended;

import utils.Item;
import utils.User;

import java.util.LinkedList;

/**
 * Created by didii on 3/11/15.
 */
public class CorelatedUser {
    public User user;

    public double corelatedScore;

    public static LinkedList<Item> itemList;
    public static double referenceRatedSum;
    public static double referenceRatedItemsCount;

    public CorelatedUser(User user, double corelatedScore) {
        this.user = user;
        this.corelatedScore = corelatedScore;
    }
}
