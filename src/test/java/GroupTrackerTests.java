package test.java;


import main.java.controller.GroupTracker;
import main.java.controller.GroupTrackerImpl;
import main.java.model.Message;
import main.java.model.MessageImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GroupTrackerTests {

    private GroupTracker groupTracker;
    private Message message1g1;
    private Message message2g2;

    @Before
    public void init() {
        groupTracker = new GroupTrackerImpl();
        message1g1 = new MessageImpl(1, 1);
        message1g1.setCounter(0);
        message2g2 = new MessageImpl(2, 2);
        message2g2.setCounter(1);

    }

    @Test
    public void testIsGroupInProgress() {
        message1g1 = new MessageImpl(1, 1);

        assertFalse(groupTracker.isGroupInProgress(1));
        groupTracker.addGroupToList(message1g1.getGroupID());
        assertTrue(groupTracker.isGroupInProgress(1));

        groupTracker.removeGroupFromInProgress(1);
        assertFalse(groupTracker.isGroupInProgress(1));
    }
}
