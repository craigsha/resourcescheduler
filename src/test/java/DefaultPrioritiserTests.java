package test.java;

import main.java.controller.DefaultPrioritiser;
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


public class DefaultPrioritiserTests {

    private Message message;
    private Prioritiser prioritiser;

    private Message message1g1;
    private Message message2g2;

    @Before
    public void init() {
        message = mock(MessageImpl.class);
        prioritiser = new DefaultPrioritiser(PriorityStrategy.DEFAULT);

        message1g1 = new MessageImpl(1, 1);
        message1g1.setCounter(0);
        message2g2 = new MessageImpl(2, 2);
        message2g2.setCounter(1);
    }

    @Test
    public void testAddMessage() {
        prioritiser.addMessage(message);
        assertEquals(1, prioritiser.getQueueSize());

        prioritiser.addMessage(message);
        assertEquals(2, prioritiser.getQueueSize());
    }

    @Test
    public void testGetMessages() {
        prioritiser.addMessage(message1g1);
        prioritiser.addMessage(message2g2);

        try {
            Message nextMessage = prioritiser.getNextMessage();
            assertEquals(message1g1, nextMessage);

            //m4g1 should jump over m2g2 in queue since g1 is in progress
            Message message3g2 = new MessageImpl(3, 2);
            Message message4g1 = new MessageImpl(4, 1);
            message3g2.setCounter(2);
            message4g1.setCounter(3);

            prioritiser.addMessage(message3g2);
            prioritiser.addMessage(message4g1);

            nextMessage = prioritiser.getNextMessage();
            assertEquals(message4g1, nextMessage);

            Message message5g3 = new MessageImpl(5, 3);
            message5g3.setCounter(4);
            prioritiser.addMessage(message5g3);

            nextMessage = prioritiser.getNextMessage();
            assertEquals(message2g2, nextMessage);

            nextMessage = prioritiser.getNextMessage();
            assertEquals(message3g2, nextMessage);

            Message message6g2 = new MessageImpl(6, 2);
            message6g2.setCounter(5);
            prioritiser.addMessage(message6g2);

            //6g2 should jump to front since g2 is in progress
            nextMessage = prioritiser.getNextMessage();
            assertEquals(message6g2, nextMessage);

            nextMessage = prioritiser.getNextMessage();
            assertEquals(message5g3, nextMessage);

        } catch (QueueEmptyException qee) {
            fail("exception thrown getting message: " + qee.getMessage());
        }
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
        assertEquals(PriorityStrategy.DEFAULT, prioritiser.getPriorityStrategy());
    }

    @Test
    public void testCancel() {
        testCancel(prioritiser);
    }

    public void testCancel(Prioritiser prioritiser) {
        message1g1 = new MessageImpl(1, 1);
        message2g2 = new MessageImpl(2, 2);
        Message message3g2 = new MessageImpl(3, 2);
        Message message4g3 = new MessageImpl(4, 3);
        prioritiser.addMessage(message1g1);
        prioritiser.addMessage(message2g2);
        prioritiser.addMessage(message3g2);
        prioritiser.addMessage(message4g3);

        assertEquals(4, prioritiser.getQueueSize());

        //make sure cancelling a group that doesn't exit doesn't cause an error
        prioritiser.cancel(100);
        assertEquals(4, prioritiser.getQueueSize());

        prioritiser.cancel(1);
        assertEquals(3, prioritiser.getQueueSize());

        prioritiser.cancel(2);
        assertEquals(1, prioritiser.getQueueSize());

        prioritiser.cancel(3);
        assertEquals(0, prioritiser.getQueueSize());

        //make sure cancelling a group that doesn't exit doesn't cause an error
        prioritiser.cancel(100);
        assertEquals(0, prioritiser.getQueueSize());
    }
}
