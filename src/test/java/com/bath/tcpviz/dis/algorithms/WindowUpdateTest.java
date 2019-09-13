package com.bath.tcpviz.dis.algorithms;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * TCP Window Update
 * Set when the all of the following are true:
 * The segment size is zero.
 * The window size is non-zero and not equal to the last-seen window size.
 * The sequence number is equal to the next expected sequence number.
 * The acknowledgement number is equal to the last-seen acknowledgement number.
 * None of SYN, FIN, or RST are set.
 */
public class WindowUpdateTest {

    private Segment segment = makeGoodSegment();

    @Test
    public void successCase() {
        assertTrue(new WindowUpdate().calculate(makeGoodSegment()));
    }

    @Test
    public void failsWhenSegSizeIsNonZero() {
        segment.setSegDataSize(493);
        assertFalse(new WindowUpdate().calculate(segment));
    }

    @Test
    public void failsWhenWinSizeIsZero() {
        segment.setWinSize(0);
        assertFalse(new WindowUpdate().calculate(segment));
    }

    @Test
    public void failsWhenWinSizeIsEqualToLastSeenWinSize() {
        segment.setWinSize(100);
        segment.setLastSeenWinSize(100);
        assertFalse(new WindowUpdate().calculate(segment));
    }

    @Test
    public void failsWhenSeqNumIsNotEqualToNextExpSeqNum() {
        segment.setSeqNum(404);
        segment.setNextExpSeqNum(405);
        assertFalse(new WindowUpdate().calculate(segment));
    }

    @Test
    public void failsWhenAckNumIsNotEqualToLastSeenAckNum() {
        segment.setAckNum(504);
        segment.setLastSeenAckNum(503);
        assertFalse(new WindowUpdate().calculate(segment));
    }

    @Test
    public void failsWhenFlagsAreSet() {
        segment.setSyn(true);
        segment.setFin(true);
        segment.setRst(true);
        assertFalse(new WindowUpdate().calculate(segment));
    }

    private Segment makeGoodSegment() {
        Segment segment = new Segment();
        segment.setSegDataSize(0);
        segment.setWinSize(100);
        segment.setLastSeenWinSize(99);
        segment.setSeqNum(200);
        segment.setNextExpSeqNum(200);
        segment.setAckNum(300);
        segment.setLastSeenAckNum(300);
        segment.setSyn(false);
        segment.setFin(false);
        segment.setRst(false);

        Segment prev = new Segment();
        prev.setWinSize(99);
        segment.setPrev(prev);

        return segment;
    }
}