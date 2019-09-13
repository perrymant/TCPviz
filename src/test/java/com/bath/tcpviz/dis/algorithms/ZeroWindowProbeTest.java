package com.bath.tcpviz.dis.algorithms;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * TCP ZeroWindowProbe
 * Set when the sequence number is equal to the next expected sequence number,
 * the segment size is one, and last-seen window size in the reverse direction was zero.
 *
 *
 * If the single data byte from a Zero Window Probe is dropped by the receiver (not ACKed),
 * then a subsequent segment should not be flagged as retransmission if
 * all of the following conditions are true for that segment:
 * - The segment size is larger than one.
 * - The next expected sequence number is one less than the current sequence number.
 * This affects “Fast Retransmission”, “Out-Of-Order”, or “Retransmission”.
 */
public class ZeroWindowProbeTest {

    private Segment segment = makeGoodSegment();

    @Test
    public void successCase() {
        assertTrue(new ZeroWindowProbe().calculate(makeGoodSegment()));
    }

    @Test
    public void failsWhenNextExpSeqNumIsOneLessThanSeqNum() {
        segment.setSeqNum(200);
        segment.setNextExpSeqNum(199);
        assertFalse(new ZeroWindowProbe().calculate(segment));
    }

    @Test
    public void failsWhenSegSizeIsOneAndLastSeenWinSizeWasZero() {
        segment.setSegDataSize(200);
        segment.setRevWinSize(100);
        assertFalse(new ZeroWindowProbe().calculate(segment));
    }

    private Segment makeGoodSegment() {
        Segment segment = new Segment();

        segment.setSeqNum(200);
        segment.setNextExpSeqNum(200);
        segment.setSegDataSize(1);
        segment.setRevWinSize(0);

        return segment;
    }

}