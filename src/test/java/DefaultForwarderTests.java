package test.java;

import main.java.controller.DefaultForwarder;
import main.java.controller.Forwarder;
import main.java.controller.Prioritiser;
import main.java.exception.QueueEmptyException;
import main.java.model.Gateway;
import main.java.model.Message;
import main.java.model.MessageImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Observable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class DefaultForwarderTests {

    private Forwarder forwarder;
    private int resources;
    private Prioritiser prioritiser;

    @Before
    public void init() {
        prioritiser = mock(Prioritiser.class);
        resources = 2;
        forwarder = new DefaultForwarder(prioritiser, mock(Gateway.class), resources);
    }

    @Test
    public void testUpdate() {
        Message m = mock(MessageImpl.class);

        assertEquals(resources, forwarder.getAvailableResources());
        forwarder.update((Observable) m, null);

        // should only increase if it won't break the resources limit
        assertEquals(resources, forwarder.getAvailableResources());
    }

    @Test
    public void testSendMessage() {
        Message m = new MessageImpl(1, 1);
        try {
            when(prioritiser.getNextMessage()).thenReturn(m);
        } catch (QueueEmptyException e) {
            fail("Queue should have returned a msg");
        }

        assertEquals(0, forwarder.getNumberInProgress());
        assertEquals(0, m.getCounter());
        assertEquals(resources, forwarder.getAvailableResources());

        forwarder.sendMessage(m);
        assertEquals(1, m.getCounter());
        assertEquals(1, forwarder.getNumberInProgress());
        assertEquals(resources - 1, forwarder.getAvailableResources());

        forwarder.update((Observable) m, null);
        assertEquals(0, forwarder.getNumberInProgress());
        assertEquals(resources, forwarder.getAvailableResources());
    }

    @Test
    public void testSendMessageEmptyQueueException() throws QueueEmptyException {
        when(prioritiser.getNextMessage()).thenThrow(new QueueEmptyException("Should fail"));
        Message m = mock(MessageImpl.class);
        forwarder.sendMessage(m);
        assertEquals(resources - 1, forwarder.getAvailableResources());
        assertEquals(0, forwarder.getNumberInProgress());
    }

    @Test
    public void testResourceSemaphore() {
        assertEquals(resources, forwarder.getAvailableResources());
        forwarder.sendMessage(mock(Message.class));
        assertEquals(resources - 1, forwarder.getAvailableResources());
    }

    @Test
    public void testNumberInProgress() {
        assertEquals(0, forwarder.getNumberInProgress());
        forwarder.sendMessage(mock(Message.class));
        assertEquals(1, forwarder.getNumberInProgress());
        forwarder.sendMessage(mock(Message.class));
        assertEquals(2, forwarder.getNumberInProgress());
    }

    @Test
    public void testCancel() {
        try {
            when(prioritiser.getNextMessage()).thenReturn(new MessageImpl(1, 1));
        } catch (QueueEmptyException e) {
            fail(e.getMessage());
        }
        Message message1g1 = new MessageImpl(1, 1);
        Message message2g2 = new MessageImpl(2, 2);
        forwarder.sendMessage(message1g1);
        forwarder.sendMessage(message2g2);

        assertEquals(2, forwarder.getNumberInProgress());

        forwarder.cancel(1);
        assertEquals(1, forwarder.getNumberInProgress());
    }
}
