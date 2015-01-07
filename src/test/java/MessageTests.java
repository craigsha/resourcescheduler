package test.java;

import main.java.model.Message;
import main.java.model.MessageImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Observable;
import java.util.Observer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class MessageTests {

    private int name;
    private int group;
    private Message message;

    @Before
    public void setup() {
        name = 1;
        group = 2;
        message = new MessageImpl(name, group);
    }

    @Test
    public void testNotifyOnComplete() {
        Observer observer = mock(Observer.class);
        ((Observable) message).addObserver(observer);

        message.completed();
        verify(observer).update((Observable) message, null);
    }

    @Test
    public void testGetMessageName() {
        assertTrue(message.getMessageID() == name);
    }

    @Test
    public void testGetGroupName() {
        assertTrue(message.getGroupID() == group);
    }

    @Test
    public void testCounter() {
        message.setCounter(2);
        assertEquals(message.getCounter(), 2);
    }
}
