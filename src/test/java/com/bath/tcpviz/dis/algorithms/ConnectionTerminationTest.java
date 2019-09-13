package com.bath.tcpviz.dis.algorithms;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConnectionTerminationTest {
    private Segment segment1 = activeCloseMakeGoodSegment();

    @Test
    public void activeCloseSuccessCase() {
        assertTrue(new ConnectionTermination().calculate1_activeClose(activeCloseMakeGoodSegment()));
    }

    @Test
    public void activeCloseFailsWhenFinIsFalse() {
        segment1.setFin(false);
        assertFalse(new ConnectionTermination().calculate1_activeClose(segment1));
    }

    private Segment activeCloseMakeGoodSegment() {
        Segment curr = new Segment();
        curr.setFin(true);
        return curr;
    }


}