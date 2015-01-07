package test.java;

import main.java.exception.QueueEmptyException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class QueueEmptyExceptionTests {

    @Test
    public void testConstructor() {
        String msg = "Empty!";
        QueueEmptyException qee = new QueueEmptyException(msg);
        assertTrue(qee.getMessage().equals(msg));
    }
}
