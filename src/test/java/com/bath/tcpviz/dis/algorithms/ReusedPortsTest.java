package com.bath.tcpviz.dis.algorithms;

import org.junit.Test;

import static org.junit.Assert.*;

public class ReusedPortsTest {
    private Segment segment = makeGoodSegment();

    @Test
    public void successCase() {
        assertTrue(new ReusedPorts().calculate(makeGoodSegment()));
    }

    @Test
    public void failsWhenSynFlagNotSet() {
        segment.setSyn(false);
        assertFalse(new ReusedPorts().calculate(segment));
    }

    @Test
    public void failsWhenNoExistingConversation() {
        segment.setPortUsed(false);
        assertFalse(new ReusedPorts().calculate(segment));
    }

    private Segment makeGoodSegment() {
        Segment segment = new Segment();
        segment.setSyn(true);
        segment.setPortUsed(true);
        return segment;
    }

}