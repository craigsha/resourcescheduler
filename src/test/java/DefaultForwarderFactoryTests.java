package test.java;

import main.java.controller.DefaultForwarderFactory;
import main.java.controller.Forwarder;
import main.java.controller.ForwarderFactory;
import main.java.controller.PriorityStrategy;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class DefaultForwarderFactoryTests {

    ForwarderFactory factory;

    @Before
    public void init() {
        factory = new DefaultForwarderFactory();
    }


    @Test
    public void testDefaultBuild() {
        Forwarder forwarder = factory.build(2, PriorityStrategy.DEFAULT);
        assertNotNull(forwarder);
        assertEquals(2, forwarder.getAvailableResources());
        assertEquals(PriorityStrategy.DEFAULT, forwarder.getPriorityStrategy());
    }

    @Test
    public void testFIFOBuild() {
        Forwarder forwarder = factory.build(4, PriorityStrategy.FIFO);
        assertNotNull(forwarder);
        assertEquals(4, forwarder.getAvailableResources());
        assertEquals(PriorityStrategy.FIFO, forwarder.getPriorityStrategy());

    }
}
