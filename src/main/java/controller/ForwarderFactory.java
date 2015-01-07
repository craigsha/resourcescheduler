package main.java.controller;


public interface ForwarderFactory {
    Forwarder build(int numberOfResources, PriorityStrategy priorityStrategy);
}
