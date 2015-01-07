package main.java.model;


public interface Message {
    void completed();

    int getMessageID();

    int getGroupID();

    int getCounter();

    void setCounter(int counter);

}
