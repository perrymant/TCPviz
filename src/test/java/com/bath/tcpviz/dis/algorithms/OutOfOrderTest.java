package com.bath.tcpviz.dis.algorithms;

import com.bath.tcpviz.dis.algorithms.retransmits.OutOfOrder;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * TCP Out-Of-Order
 * Set when all of the following are true:
 * This is not a keepalive packet.
 * In the forward direction, the segment length is greater than zero or the SYN or FIN is set.
 * The next expected sequence number is greater than the current sequence number.
 * The next expected sequence number and the next sequence number differ.
 * The last segment arrived within the calculated RTT (3ms by default).
 * Supersedes “Spurious Retransmission” and “Retransmission”.
 */
public class OutOfOrderTest {

    private Segment segment = makeGoodSegment();

    @Test
    public void successCase() {
        assertTrue(new OutOfOrder().calculate(makeGoodSegment()));
    }

    @Test
    public void failsWhenSeqNumIsLessThanMaxSeqNum() {
        segment.setSeqNum(222);
        segment.setMaxSeqNum(100);
        assertFalse(new OutOfOrder().calculate(segment));
    }

    @Test
    public void failsWhenLastNextExpSeqNumAndSeqNumAreTheSame() {
        segment.setLastNextExpSeqNum(100);
        segment.setSeqNum(100);
        assertFalse(new OutOfOrder().calculate(segment));
    }

    @Test
    public void failsWhenLastSeqArrivedOutsideOf3Ms() {
        segment.setLastSegArrivedWithin4ms(false);
        assertFalse(new OutOfOrder().calculate(segment));
    }

    @Test
    public void failsWhenItIsAKeepAliveSegment() {
        segment.setAlgoKeepAlive(true);
        assertFalse(new OutOfOrder().calculate(segment));
    }

    @Test
    public void failsWhenItIsFastRetransmission() {
        segment.setAlgoFastRetransmission(true);
        assertFalse(new OutOfOrder().calculate(segment));
    }

    @Test
    public void failsWhenSegLenIsZeroOrSynOrFinAreSet() {
        segment.setSegDataSize(0);
        segment.setSyn(false);
        segment.setFin(false);
        assertFalse(new OutOfOrder().calculate(segment));
    }

    private Segment makeGoodSegment() {
        Segment segment = new Segment();
        segment.setSeqNum(100);
        segment.setMaxSeqNum(101);
        segment.setLastNextExpSeqNum(99);
        segment.setLastSegArrivedWithin4ms(true);
        segment.setAlgoKeepAlive(false);
        segment.setAlgoFastRetransmission(false);
        segment.setSegDataSize(1);
//        segment.setSyn(true);
//        segment.setFin(true);
        return segment;
    }


}