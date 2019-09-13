package com.bath.tcpviz.dis.algorithms;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * TCP Previous segment not captured
 * Set when the current sequence number is greater than the next expected sequence number.
 * Set when there are no analysis flags and and for zero window probes.
 */
public class PreviousSegmentNotCapturedTest {
    private Segment segment = makeGoodSegment();

    @Test
    public void successCase() {
        assertTrue(new PreviousSegmentNotCaptured().calculate(makeGoodSegment()));
    }

    @Test
    public void failsWhenSeqNumIsEqualToLastNextExpSeqNum() {
        segment.setLastNextExpSeqNum(199);
        segment.setSeqNum(199);
        assertFalse(new PreviousSegmentNotCaptured().calculate(segment));
    }

    @Test
    public void failsWhenRSTisSet() {
        segment.setRst(true);
        assertFalse(new PreviousSegmentNotCaptured().calculate(segment));
    }

    private Segment makeGoodSegment() {
        Segment segment = new Segment();
        segment.setLastNextExpSeqNum(101);
        segment.setSeqNum(1901);
        segment.setFin(false);
        segment.setSyn(false);
        segment.setRst(false);
        return segment;
    }

}