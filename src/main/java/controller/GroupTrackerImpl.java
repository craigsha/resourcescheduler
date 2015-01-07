package main.java.controller;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GroupTrackerImpl implements GroupTracker {

    private List<Integer> inProgressGroups;

    public GroupTrackerImpl() {
        inProgressGroups = new ArrayList<Integer>();
    }

    public void removeGroupFromInProgress(int groupID) {
        Iterator<Integer> it = inProgressGroups.iterator();
        while (it.hasNext()) {
            if (it.next() == groupID) {
                it.remove();
            }
        }
    }

    public boolean isGroupInProgress(int groupID) {
        if (inProgressGroups.contains(groupID)) {
            return true;
        }
        return false;
    }

    public void addGroupToList(int groupID) {
        if (!inProgressGroups.contains(groupID)) {
            inProgressGroups.add(groupID);
        }
    }
}
