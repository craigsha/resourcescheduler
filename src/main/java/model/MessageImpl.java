package main.java.model;

import java.util.Observable;


public class MessageImpl extends Observable implements Message {

    private int messageID;
    private int groupID;
    private int counter;

    public MessageImpl(int messageID, int groupName) {
        this.messageID = messageID;
        this.groupID = groupName;
        counter = 0;
    }

    public void completed() {
        setChanged();
        notifyObservers();
    }

    public int getMessageID() {
        return messageID;
    }

    public int getGroupID() {
        return groupID;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
