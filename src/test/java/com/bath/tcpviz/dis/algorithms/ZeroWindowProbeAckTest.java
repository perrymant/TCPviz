package com.bath.tcpviz.dis.algorithms;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * TCP ZeroWindowProbeAck
 * Set when the all of the following are true:
 * The segment size is zero.
 * The window size is zero.
 * The sequence number is equal to the next expected sequence number.
 * The acknowledgement number is equal to the last-seen acknowledgement number.
 * The last-seen packet in the reverse direction was a zero window probe.
 * Supersedes “TCP Dup ACK”.
 */
public class ZeroWindowProbeAckTest {
    private Segment segment = makeGoodSegment();

    @Test
    public void successCase() {
        assertTrue(new ZeroWindowProbeAck().calculate(makeGoodSegment()));
    }

    @Test
    public void failsWhenSegSizeIsNotZero() {
        segment.setSegDataSize(1);
        assertFalse(new ZeroWindowProbeAck().calculate(segment));
    }

    @Test
    public void failsWhenWinSizeIsNotZero() {
        segment.setWinSize(1);
        assertFalse(new ZeroWindowProbeAck().calculate(segment));
    }

    @Test
    public void failsWhenSeqNumIsNotEqualToTheNextExpSeqNum() {
        segment.setSeqNum(100);
        segment.setNextExpSeqNum(101);
        assertFalse(new ZeroWindowProbeAck().calculate(segment));
    }

    @Test
    public void failsWhenAckNumIsNotEqualToTheLastSeenAckNum() {
        segment.setAckNum(100);
        segment.setLastSeenAckNum(101);
        assertFalse(new ZeroWindowProbeAck().calculate(segment));
    }

    @Test
    public void failsWhenLastSeenPacketInTheRevWasNotAZeroWinProbe() {
        segment.setRevLastSeenZeroWinProbe(false);
        assertFalse(new ZeroWindowProbeAck().calculate(segment));
    }

    private Segment makeGoodSegment() {
        Segment segment = new Segment();
        segment.setSegDataSize(0);
        segment.setWinSize(0);
        segment.setSeqNum(200);
        segment.setNextExpSeqNum(200);
        segment.setAckNum(100);
        segment.setLastSeenAckNum(100);
        segment.setRevLastSeenZeroWinProbe(true);
        return segment;
    }


}