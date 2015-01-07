package main.java.controller;


public interface GroupTracker {

    boolean isGroupInProgress(int group);

    void addGroupToList(int group);

    void removeGroupFromInProgress(int groupID);

}
