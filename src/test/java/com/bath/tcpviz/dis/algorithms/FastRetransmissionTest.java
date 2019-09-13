package com.bath.tcpviz.dis.algorithms;

import com.bath.tcpviz.dis.algorithms.retransmits.FastRetransmission;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * TCP Fast Retransmission
 * Set when all of the following are true:
 * This is not a keepalive packet.
 * In the forward direction, the segment size is greater than zero or the SYN or FIN is set.
 * The LastNextExpSeqNum is greater than the current sequence number.
 * We have more than two duplicate ACKs in the reverse direction.
 * The current sequence number equals the LastRevAckNum.
 * We saw the LastDupAck less than 20ms ago.
 * Supersedes “Out-Of-Order”, “Spurious Retransmission”, and “Retransmission”.
 */
public class FastRetransmissionTest {

    private Segment segment = makeGoodSegment();

    @Test
    public void successCase() {
        assertTrue(new FastRetransmission().calculate(makeGoodSegment()));
    }

    @Test
    public void failsWhenItIsAKeepAlivePacket() {
        segment.setAlgoKeepAlive(true);
        assertFalse(new FastRetransmission().calculate(segment));
    }

    @Test
    public void failsWhenSegSizeIsZeroOrSynOrFinAreNotSet() {
        segment.setSegDataSize(0);
        segment.setSyn(false);
        segment.setFin(false);
        assertFalse(new FastRetransmission().calculate(segment));
    }

    @Test
    public void failsWhenLastNextExpSeqNumIsLessThanCurrSeqNum() {
        segment.setLastNextExpSeqNum(99);
        segment.setSeqNum(100);
        assertFalse(new FastRetransmission().calculate(segment));
    }

    @Test
    public void failsWhenThereAreLessThan2DupAcks() {
        segment.setRevDupAckCounter(1);
        assertFalse(new FastRetransmission().calculate(segment));
    }

    @Test
    public void failsWhenSeqNumDoesNotEqualLastRevAckNum() {
        segment.setSeqNum(99);
        segment.setLastRevAckNum(100);
        assertFalse(new FastRetransmission().calculate(segment));
    }

    @Test
    public void failsWhenLastDupAckAppearedGreaterThan20msAgo() {
        segment.setLastDupAckArrivedWithin20ms(false);
        assertFalse(new FastRetransmission().calculate(segment));
    }

    private Segment makeGoodSegment() {
        Segment segment = new Segment();
        segment.setAlgoKeepAlive(false);
        segment.setSegDataSize(1);
        segment.setSyn(true);
        segment.setFin(true);
        segment.setLastNextExpSeqNum(101);
        segment.setSeqNum(100);
        segment.setRevDupAckCounter(3);
        segment.setLastRevAckNum(100);
        segment.setLastDupAckArrivedWithin20ms(true);
        return segment;
    }

}