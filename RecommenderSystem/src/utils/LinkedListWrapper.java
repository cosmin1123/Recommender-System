package utils;

import java.util.LinkedList;

/**
 * Created by didii on 2/24/15.
 */
public class LinkedListWrapper<E> {
    private LinkedList<E> linkedList;

    public LinkedListWrapper(LinkedList<E> linkedList) {
        this.linkedList = linkedList;
    }

    public LinkedList<E> getLinkedList() {
        return linkedList;
    }
}
