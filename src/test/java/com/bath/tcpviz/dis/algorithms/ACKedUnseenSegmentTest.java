package com.bath.tcpviz.dis.algorithms;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * TCP ACKed unseen segment
 * Set when the expected next acknowledgement number
 * is set for the reverse direction and
 * it’s less than the current acknowledgement number.
 * <p>
 * Store the highest continuous seq number seen so far for 'max seq to be acked',
 * <p>
 * If this segment acks beyond the 'max seq to be acked' in the other direction
 * then that means we have missed packets going in the other direction
 * We only check this if we have actually seen some seq numbers in the other direction.
 */
public class ACKedUnseenSegmentTest {
    private Segment segment = makeGoodSegment();

    private Segment makeGoodSegment() {
        Segment segment = new Segment();
        segment.setBothSidesSeen(true);
        segment.setAckNum(200);
        segment.setMaxRevSeqAndData(100);
        segment.setSyn(false);
        segment.setRst(false);
        return segment;
    }

    @Test
    public void successCase() {
        assertTrue(new ACKedUnseenSegment().calculate(makeGoodSegment()));
    }

    @Test
    public void failsWhenMaxSeqNumRev_IsLowerThanLSAN_Plus_AckNum() {
        segment.setMaxRevSeqAndData(300);
        assertFalse(new ACKedUnseenSegment().calculate(segment));
    }

    @Test
    public void failsIfSequenceNumbersHaveNotBeenSeenInOtherDirection() {
        segment.setBothSidesSeen(false);
        assertFalse(new ACKedUnseenSegment().calculate(segment));
    }

}