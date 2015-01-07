package main.java.controller;

import main.java.model.Message;

import java.util.Comparator;


public class DefaultMessageComparator implements Comparator<Message> {

    private GroupTracker groupTracker;

    public DefaultMessageComparator(GroupTracker groupTracker) {
        this.groupTracker = groupTracker;
    }

    public int compare(Message m1, Message m2) {
        int m1GroupStarted = groupStarted(m1.getGroupID());
        int m2GroupStarted = groupStarted(m2.getGroupID());
        int result = 0;

        //if both are started, then equal priority on this rule
        if (!(m1GroupStarted == -1 && m2GroupStarted == -1)) {
            result = m1GroupStarted - m2GroupStarted;
        }

        if (result == 0) {
            //sort on counter - default received order
            result = m1.getCounter() - m2.getCounter();
        }
        return result;
    }

    private int groupStarted(int groupID) {
        if (groupTracker.isGroupInProgress(groupID)) {
            //if started, then should be further ahead in queue (lower)
            return -1;
        }
        return 0;
    }
}
