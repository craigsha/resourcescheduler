package main.java.controller;

import main.java.exception.QueueEmptyException;
import main.java.model.Gateway;
import main.java.model.Message;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.Semaphore;


public class DefaultForwarder implements Forwarder {

    private Prioritiser prioritiser;
    private List<Message> inProgressMessages;
    private Semaphore resourcesSemaphore;
    private Gateway gateway;
    private Logger logger;

    private int numberOfResources;
    private int messageCounter;

    public DefaultForwarder(Prioritiser prioritiser, Gateway gateway, int numberOfResources) {
        logger = Logger.getLogger(this.getClass());
        logger.setLevel(Level.INFO);
        BasicConfigurator.configure();

        this.prioritiser = prioritiser;
        this.gateway = gateway;

        inProgressMessages = new ArrayList<Message>();
        resourcesSemaphore = new Semaphore(numberOfResources, true);
        this.numberOfResources = numberOfResources;
        messageCounter = 0;
    }

    public void sendMessage(Message message) {
        try {
            message.setCounter(++messageCounter);
            logger.info("Adding message" + message.getMessageID() + " group" + message.getGroupID());
            logger.info("Message Counter: " + messageCounter);
            prioritiser.addMessage(message);
            printAvailableResourcesAndInProcess();

            logger.info("Acquiring resource");
            resourcesSemaphore.acquire();
            logger.info("Resource acquired. Sending top of backlog to gateway");
            try {
                Message nextMessage = prioritiser.getNextMessage();
                inProgressMessages.add(nextMessage);
                gateway.send(nextMessage);
                printAvailableResourcesAndInProcess();
            } catch (QueueEmptyException e) {
                logger.info("No messages in the backlog");
            }
        } catch (InterruptedException ie) {
            logger.error("Failed to acquire resource lock.");
            logger.error(ie.getMessage());
        }
    }


    public void update(Observable o, Object arg) {
        if (o instanceof Message) {
            Message message = (Message) o;
            logger.info("Message completed: Message" + message.getMessageID() + " Group" + message.getGroupID());
            removeFromInProgress(o);

            //Semaphore is just a blocking counter, so need to be careful when releasing
            if (getAvailableResources() < numberOfResources)
                resourcesSemaphore.release();
        }
        printAvailableResourcesAndInProcess();
    }

    private void removeFromInProgress(Object o) {
        int index = inProgressMessages.indexOf(o);
        if (index >= 0) {
            inProgressMessages.remove(index);
        }
    }

    public int getNumberInProgress() {
        return inProgressMessages.size();
    }

    public int getAvailableResources() {
        return resourcesSemaphore.availablePermits();
    }

    private void printAvailableResourcesAndInProcess() {
        logger.info("Free Resources: " + resourcesSemaphore.availablePermits());
        logger.info("Number In Progress: " + inProgressMessages.size());
    }

    public PriorityStrategy getPriorityStrategy() {
        return prioritiser.getPriorityStrategy();
    }

    public void cancel(int groupId) {
        prioritiser.cancel(groupId);
        for (Message message : inProgressMessages) {
            if (message.getGroupID() == groupId) {
                //since message is cancelled, will use same behaviour as complete
                update((Observable) message, null);
                /*
                How will the gateway know the message is cancelled? It it can't be terminated, then the
                call to update should be removed since this will free a resource.
                */
            }
        }
    }
}
