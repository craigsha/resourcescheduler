package main.java.controller;

import main.java.exception.QueueEmptyException;
import main.java.model.Message;


public interface Prioritiser {

    void addMessage(Message message);

    Message getNextMessage() throws QueueEmptyException;

    int getQueueSize();

    PriorityStrategy getPriorityStrategy();

    void cancel(int groupID);
}
