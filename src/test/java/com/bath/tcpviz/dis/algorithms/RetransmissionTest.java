package com.bath.tcpviz.dis.algorithms;

import com.bath.tcpviz.dis.algorithms.retransmits.FastRetransmission;
import com.bath.tcpviz.dis.algorithms.retransmits.Retransmission;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * TCP Retransmission
 * Set when all of the following are true:
 * This is not a keepalive packet.
 * In the forward direction, the segment length is greater than zero or the SYN or FIN flag is set.
 * The next expected sequence number is greater than the current sequence number.
 *
 * If the single data byte from a Zero Window Probe is dropped by the receiver (not ACKed),
 * then a subsequent segment should not be flagged as retransmission
 * if all of the following conditions are true for that segment:
 * - The segment size is larger than one.
 * - The next expected sequence number is one less than the current sequence number.
 */
public class RetransmissionTest {
    private Segment segment = makeGoodSegment();

    @Test
    public void successCase() {
        assertTrue(new Retransmission().calculate(makeGoodSegment()));
    }

    @Test
    public void failsWhenSegSizeIsZeroOrSynOrFinAreNotSet() {
        segment.setSegDataSize(0);
        segment.setSyn(false);
        segment.setFin(false);
        assertFalse(new FastRetransmission().calculate(segment));
    }

    @Test
    public void failsWhenWinSizeIsZero() {
        segment.setWinSize(0);
        assertFalse(new Retransmission().calculate(segment));
    }

    @Test
    public void failsWhenLastSeenSegLengthIsNotTheSameAsSegDataSize() {
        segment.setLastSeenSegmentLength(100);
        segment.setSegDataSize(101);
        assertFalse(new Retransmission().calculate(segment));
    }

    @Test
    public void failsWhenItIsAKeepAlivePacket() {
        segment.setAlgoKeepAlive(true);
        assertFalse(new Retransmission().calculate(segment));
    }

    @Test
    public void failsWhenItIsAZeroWindowProbe() {
        segment.setAlgoZeroWindowProbe(true);
        assertFalse(new Retransmission().calculate(segment));
    }

    @Test
    public void failsWhenItIsASpuriousRetransmission() {
        segment.setAlgoSpuriousRetransmission(true);
        assertFalse(new Retransmission().calculate(segment));
    }

    @Test
    public void failsWhenItIsAFastRetransmission() {
        segment.setAlgoFastRetransmission(true);
        assertFalse(new Retransmission().calculate(segment));
    }

    @Test
    public void failsWhenItIsAnOutOfOrder() {
        segment.setOutOfOrder(true);
        assertFalse(new Retransmission().calculate(segment));
    }

    private Segment makeGoodSegment() {
        Segment segment = new Segment();
        segment.setSegDataSize(1);
        segment.setWinSize(1);
        segment.setLastSeenSegmentLength(1);
        segment.setLastSeenSeqNum(100);
        segment.setSeqNum(100);
        segment.setAlgoKeepAlive(false);
        segment.setAlgoZeroWindowProbe(false);
        segment.setAlgoSpuriousRetransmission(false);
        segment.setAlgoFastRetransmission(false);
        segment.setOutOfOrder(false);
        segment.setSyn(true);
        segment.setFin(true);
        return segment;
    }
}