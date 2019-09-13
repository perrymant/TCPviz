package com.bath.tcpviz.dis.algorithms;

import com.bath.tcpviz.dis.algorithms.retransmits.SpuriousRetransmission;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * TCP Spurious Retransmission
 * Checks for a retransmission based on analysis data in the reverse direction.
 * Set when all of the following are true:
 * The SYN or FIN flag is set.
 * This is not a keepalive packet.
 * The segment length is greater than zero.
 * Data for this flow has been acknowledged.
 * That is, the last-seen acknowledgement number has been set.
 * The next sequence number is less than or equal to the last-seen acknowledgement number.
 * Supersedes “Retransmission”.
 */
public class SpuriousRetransmissionTest {
    private Segment segment = makeGoodSegment();

    @Test
    public void successCase() {
        assertTrue(new SpuriousRetransmission().calculate(makeGoodSegment()));
    }

    @Test
    public void failsWhenAKeepAlive() {
        segment.setAlgoKeepAlive(true);
        assertFalse(new SpuriousRetransmission().calculate(segment));
    }

    @Test
    public void failsWhenSegSizeIsZero() {
        segment.setSegDataSize(0);
        assertFalse(new SpuriousRetransmission().calculate(segment));
    }

    @Test
    public void failsWhenNextSeqNumIsGreaterThanLastRevAckNum() {
        segment.setSeqNum(100);
        segment.setSegDataSize(1);
        segment.setLastRevAckNum(99);
        assertFalse(new SpuriousRetransmission().calculate(segment));
    }

    private Segment makeGoodSegment() {
        Segment segment = new Segment();
        segment.setSeqNum(100);
        segment.setSegDataSize(1);
        segment.setLastRevAckNum(333);
        segment.setAlgoKeepAlive(false);
        segment.setSegDataSize(1);
        segment.setOutOfOrder(false);
        return segment;
    }

}