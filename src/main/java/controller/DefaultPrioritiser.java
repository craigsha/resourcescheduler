package main.java.controller;

import main.java.exception.QueueEmptyException;
import main.java.model.Message;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;


public class DefaultPrioritiser implements Prioritiser {

    private PriorityQueue<Message> backlog;

    private PriorityStrategy priorityStrategy;

    private GroupTracker groupTracker;

    public DefaultPrioritiser(PriorityStrategy priorityStrategy) {
        /*
         Could make completed messages drop out of in progress list if the requirement on
         not overlapping groups is only applied if a message in the group is in progress.
         Could be done by making this class an observer of the Message so it will be updated
         on completion.
         */
        groupTracker = new GroupTrackerImpl();
        Comparator<Message> comparator = new DefaultMessageComparator(groupTracker);
        this.backlog = new PriorityQueue<Message>(11, comparator);
        this.priorityStrategy = priorityStrategy;
    }

    public Message getNextMessage() throws QueueEmptyException {
        // need to force the head of queue to be sorted after
        // in progress updated, therefore peeking first
        Message next = backlog.peek();
        if (next == null) {
            throw new QueueEmptyException("Queue is empty");
        }
        groupTracker.addGroupToList(next.getGroupID());
        backlog.poll();
        return next;
    }

    public void addMessage(Message message) {
        backlog.offer(message);
    }

    public int getQueueSize() {
        return backlog.size();
    }

    public PriorityStrategy getPriorityStrategy() {
        return priorityStrategy;
    }

    public void cancel(int groupID) {
        removeFromBacklog(groupID);
        groupTracker.removeGroupFromInProgress(groupID);
    }

    private void removeFromBacklog(int groupID) {
        Iterator<Message> it = backlog.iterator();
        while (it.hasNext()) {
            Message message = it.next();
            if (message.getGroupID() == groupID) {
                it.remove();
            }
        }
    }
}
