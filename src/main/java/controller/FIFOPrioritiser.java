package main.java.controller;

import main.java.exception.QueueEmptyException;
import main.java.model.Message;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class FIFOPrioritiser implements Prioritiser {

    private List<Message> queue;
    private PriorityStrategy priorityStrategy;
    private GroupTracker groupTracker;

    public FIFOPrioritiser(PriorityStrategy priorityStrategy) {
        groupTracker = new GroupTrackerImpl();

        queue = new ArrayList<Message>();
        this.priorityStrategy = priorityStrategy;
    }

    public void addMessage(Message message) {
        queue.add(message);
    }

    public Message getNextMessage() throws QueueEmptyException {
        if (queue.size() > 0) {
            Message next = queue.remove(0);
            groupTracker.addGroupToList(next.getGroupID());
            return next;
        } else {
            throw new QueueEmptyException("Queue is empty");
        }
    }

    public int getQueueSize() {
        return queue.size();
    }

    public PriorityStrategy getPriorityStrategy() {
        return priorityStrategy;
    }

    public void cancel(int groupID) {
        Iterator<Message> it = queue.iterator();
        while (it.hasNext()) {
            if (it.next().getGroupID() == groupID) {
                it.remove();
            }
        }
        groupTracker.removeGroupFromInProgress(groupID);
    }
}
