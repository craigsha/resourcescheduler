package test.java;

import main.java.controller.FIFOPrioritiser;
import main.java.controller.Prioritiser;
import main.java.controller.PriorityStrategy;
import main.java.exception.QueueEmptyException;
import main.java.model.Message;
import main.java.model.MessageImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;


public class FIFOPrioritiserTests {

    private Prioritiser prioritiser;

    private Message message1g1;
    private Message message2g1;
    private Message message;
    private DefaultPrioritiserTests defaultPrioritiserTests;

    @Before
    public void init() {
        message = mock(MessageImpl.class);
        prioritiser = new FIFOPrioritiser(PriorityStrategy.FIFO);

        message1g1 = new MessageImpl(1, 1);
        message1g1.setCounter(0);
        message2g1 = new MessageImpl(2, 1);
        message2g1.setCounter(1);
        defaultPrioritiserTests = new DefaultPrioritiserTests();
    }

    @Test
    public void testAddMessage() {
        prioritiser.addMessage(message);
        assertEquals(1, prioritiser.getQueueSize());

        prioritiser.addMessage(message);
        assertEquals(2, prioritiser.getQueueSize());
    }

    @Test
    public void testGetMessage() throws QueueEmptyException {
        prioritiser.addMessage(message1g1);
        prioritiser.addMessage(message2g1);

        Message next = prioritiser.getNextMessage();
        assertEquals(1, next.getMessageID());
        assertEquals(1, next.getGroupID());

        next = prioritiser.getNextMessage();
        assertEquals(2, next.getMessageID());
        assertEquals(1, next.getGroupID());

        prioritiser.addMessage(message2g1);
        prioritiser.addMessage(message1g1);
        prioritiser.addMessage(message1g1);
        prioritiser.addMessage(message2g1);

        next = prioritiser.getNextMessage();
        assertEquals(2, next.getMessageID());
        assertEquals(1, next.getGroupID());

        next = prioritiser.getNextMessage();
        assertEquals(1, next.getMessageID());
        assertEquals(1, next.getGroupID());

        next = prioritiser.getNextMessage();
        assertEquals(1, next.getMessageID());
        assertEquals(1, next.getGroupID());

        next = prioritiser.getNextMessage();
        assertEquals(2, next.getMessageID());
        assertEquals(1, next.getGroupID());
    }

    @Test
    public void testExceptionWhenQueueEmpty() {
        try {
            prioritiser.getNextMessage();
            fail("Exception expected - queue is empty at start");
        } catch (QueueEmptyException qee) {
        }
    }

    @Test
    public void testGetStrategyType() {
        assertEquals(PriorityStrategy.FIFO, prioritiser.getPriorityStrategy());
    }

    @Test
    public void testCancel() {
        defaultPrioritiserTests.testCancel(prioritiser);
    }
}
