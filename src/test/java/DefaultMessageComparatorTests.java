package test.java;

import main.java.controller.DefaultMessageComparator;
import main.java.controller.GroupTracker;
import main.java.model.Message;
import main.java.model.MessageImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class DefaultMessageComparatorTests {


    private Message m1g1;
    private Message m2g2;
    private Message m1g2;

    private Comparator<Message> comparator;
    private GroupTracker groupTracker;


    @Before
    public void init() {
        int name1 = 1;
        int group1 = 1;

        int name2 = 2;
        int group2 = 2;
        m1g1 = new MessageImpl(name1, group1);
        m2g2 = new MessageImpl(name2, group2);
        m1g2 = new MessageImpl(name1, group2);

        groupTracker = mock(GroupTracker.class);
        comparator = new DefaultMessageComparator(groupTracker);
    }

    @Test
    public void testGroupEqualCompare() {
        assertTrue(comparator.compare(m2g2, m1g2) == 0);
    }

    @Test
    public void testGroupGreaterThanCompare() {
        when(groupTracker.isGroupInProgress(2)).thenReturn(true);
        assertTrue(comparator.compare(m1g2, m1g1) < 0);
    }


    @Test
    public void testGroupLessThanCompare() {
        when(groupTracker.isGroupInProgress(1)).thenReturn(true);
        assertTrue(comparator.compare(m1g2, m1g1) > 0);
    }

    @Test
    public void testBothGroupsStartedCompare() {
        when(groupTracker.isGroupInProgress(1)).thenReturn(true);
        when(groupTracker.isGroupInProgress(2)).thenReturn(true);
        assertTrue(comparator.compare(m1g2, m1g1) == 0);
        assertTrue(comparator.compare(m1g1, m1g2) == 0);
        assertTrue(comparator.compare(m1g1, m1g1) == 0);
    }
}
