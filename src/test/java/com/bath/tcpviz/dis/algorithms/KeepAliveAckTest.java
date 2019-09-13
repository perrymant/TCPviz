package com.bath.tcpviz.dis.algorithms;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * TCP Keep-Alive ACK
 * Set when all of the following are true:
 * The segment size is zero.
 * The window size is non-zero and hasn’t changed.
 * The current sequence number is the same as the next expected sequence number.
 * The current acknowledgement number is the same as the last-seen acknowledgement number.
 * The most recently seen packet in the reverse direction was a keepalive.
 * The packet is not a SYN, FIN, or RST.
 * Supersedes “Dup ACK” and “ZeroWindowProbeAck”.
 */
public class KeepAliveAckTest {
    private Segment segment = makeGoodSegment();

    @Test
    public void successCase() {
        assertTrue(new KeepAliveAck().calculate(makeGoodSegment()));
    }

    @Test
    public void failsWhenSegSizeIsNotZero() {
        segment.setSegDataSize(2);
        assertFalse(new KeepAliveAck().calculate(segment));
    }

    @Test
    public void failsWhenWinSizeIsZeroAndHasChanged() {
        segment.setWinSize(0);
        segment.setLastSeenWinSize(100);
        assertFalse(new KeepAliveAck().calculate(segment));
    }

    @Test
    public void failsWhenSeqNumIsNotTheSameAsNextExpSeqNum() {
        segment.setSeqNum(100);
        segment.setNextExpSeqNum(101);
        assertFalse(new KeepAliveAck().calculate(segment));
    }

    @Test
    public void failsWhenAckNumNotTheSameAsLastSeenAckNum() {
        segment.setAckNum(100);
        segment.setLastSeenAckNum(101);
        assertFalse(new KeepAliveAck().calculate(segment));
    }

    @Test
    public void failsWhenRecentRevWasNotAKeepAlive() {
        segment.setRevKeepAlive(false);
        assertFalse(new KeepAliveAck().calculate(segment));
    }

    @Test
    public void failsWhenSynFinOrRst() {
        segment.setSyn(true);
        segment.setFin(true);
        segment.setRst(true);
        assertFalse(new KeepAliveAck().calculate(segment));
    }

    private Segment makeGoodSegment() {
        Segment segment = new Segment();
        segment.setAlgoReusedPorts(false);
        segment.setSegDataSize(0);
        segment.setLastSeenWinSize(1);
        segment.setWinSize(1);
        segment.setSeqNum(100);
        segment.setNextExpSeqNum(100);
        segment.setAckNum(200);
        segment.setLastSeenAckNum(200);
        segment.setRevKeepAlive(true);
        segment.setAlgoKeepAliveAck(false);
        segment.setSyn(false);
        segment.setFin(false);
        segment.setRst(false);
        return segment;
    }
}