package com.bath.tcpviz.dis.algorithms;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * TCP Dup ACK <frame>#<acknowledgement number>
 * Set when all of the following are true:
 * The segment size is zero.
 * The window size is non-zero and has not changed.
 * The next expected sequence number and last-seen acknowledgment number are non-zero (i.e. the connection has been established).
 * SYN, FIN, and RST are not set.
 */
public class DupAckTest {

    private Segment segment = makeGoodSegment();

    @Test
    public void successCase() {
        assertTrue(new DupAck().calculate(makeGoodSegment(), true));
    }

    @Test
    public void failsWhenSegSizeIsNonZero() {
        segment.setSegDataSize(493);
        assertFalse(new DupAck().calculate(segment, true));
    }

    @Test
    public void failsWhenWindowSizeIsZero() {
        segment.setWinSize(0);
        assertFalse(new DupAck().calculate(segment, true));
    }

    @Test
    public void failsWhenWindowSizeHasChanged() {
        segment.setLastSeenWinSize(100);
        segment.setWinSize(101);
        assertFalse(new DupAck().calculate(segment, true));
    }

    @Test
    public void failsWhenNextExpectedSeqNumAndLastSeenAckNumberAreZero() {
        segment.setNextExpSeqNum(0);
        segment.setLastSeenAckNum(0);
        assertFalse(new DupAck().calculate(segment, true));
    }

    @Test
    public void failsWhenSynFinOrRstAreSet() {
        segment.setSyn(true);
        segment.setFin(true);
        segment.setRst(true);
        assertFalse(new DupAck().calculate(segment, true));
    }

    private Segment makeGoodSegment() {
        Segment curr = new Segment();
        curr.setLastSeenAckNum(5);
        curr.setAckNum(5);
        curr.setSegDataSize(0);
        curr.setWinSize(1);
        curr.setLastSeenWinSize(1);
        curr.setNextExpSeqNum(1);
        curr.setSyn(false);
        curr.setFin(false);
        curr.setRst(false);

        return curr;
    }
}
