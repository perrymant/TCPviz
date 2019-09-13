package com.bath.tcpviz.dis.algorithms;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * TCP Keep-Alive
 * Set when the segment size is zero or one,
 * the current sequence number is one byte less than the next expected sequence number,
 * and any of SYN, FIN, or RST are set.
 * Supersedes “Fast Retransmission”, “Out-Of-Order”, “Spurious Retransmission”, and “Retransmission”.
 */
public class KeepAliveTest {
    private Segment segment = makeGoodSegment();

    @Test
    public void successCase() {
        assertTrue(new KeepAlive().calculate(makeGoodSegment()));
    }

    @Test
    public void failsWhenIsAlgoReusedPorts() {
        segment.setAlgoReusedPorts(true);
        assertFalse(new KeepAlive().calculate(segment));
    }

    @Test
    public void failsWhenSegmentIsFirstInConversation() {
        segment.setRelativePacketNumber(1);
        assertFalse(new KeepAlive().calculate(segment));
    }

    @Test
    public void failsWhenIsAlgoZeroWindowProbe() {
        segment.setAlgoZeroWindowProbe(true);
        assertFalse(new KeepAlive().calculate(segment));
    }

    @Test
    public void failsWhenSegSizeIsNotZeroOrOne() {
        segment.setSegDataSize(2);
        assertFalse(new KeepAlive().calculate(segment));
    }

    @Test
    public void failsWhenSeqNumIsNotOneByteLessThanTheLastNextExpSeqNum() {
        segment.setSeqNum(90);
        segment.setLastNextExpSeqNum(100);
        assertFalse(new KeepAlive().calculate(segment));
    }

    @Test
    public void failsWhenSynFinOrRstAreSet() {
        segment.setSyn(true);
        segment.setFin(false);
        segment.setRst(false);
        assertFalse(new KeepAlive().calculate(segment));
    }

    private Segment makeGoodSegment() {
        Segment segment = new Segment();
        segment.setAlgoReusedPorts(false);
        segment.setRelativePacketNumber(2);
        segment.setAlgoZeroWindowProbe(false);
        segment.setSegDataSize(1);
        segment.setSeqNum(99);
        segment.setLastNextExpSeqNum(100);
        segment.setSyn(false);
        segment.setFin(false);
        segment.setRst(false);
        return segment;
    }
}