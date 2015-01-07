package main.java.controller;

import main.java.model.Message;

import java.util.Observer;


public interface Forwarder extends Observer {

    void cancel(int groupId);

    void sendMessage(Message message);

    int getNumberInProgress();

    int getAvailableResources();

    PriorityStrategy getPriorityStrategy();
}
