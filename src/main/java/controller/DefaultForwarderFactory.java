package main.java.controller;


import main.java.model.Gateway;

import static org.mockito.Mockito.mock;

public class DefaultForwarderFactory implements ForwarderFactory {

    public Forwarder build(int numberOfResources, PriorityStrategy priorityStrategy) {
        Prioritiser prioritiser;
        switch (priorityStrategy) {
            case DEFAULT:
                prioritiser = new DefaultPrioritiser(priorityStrategy);
                break;
            case FIFO:
                prioritiser = new FIFOPrioritiser(priorityStrategy);
                break;
            default:
                prioritiser = new DefaultPrioritiser(priorityStrategy);
        }
        Gateway gateway = mock(Gateway.class);
        return new DefaultForwarder(prioritiser, gateway, numberOfResources);
    }


}
