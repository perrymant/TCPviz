package com.bath.tcpviz.dis.algorithms.retransmits;

import com.bath.tcpviz.dis.algorithms.Segment;

/**
 * TCP Retransmission
 * Set when all of the following are true:
 * - This is not a Keep-Alive, Zero Window Probe, Spurious Retransmission, Fast Retransmission, Out of Order packet.
 * - The Window Size is greater than zero.
 * - The Segment Length is greater than zero.
 * - The Last Seen Segment Length and current Segment Length are the same.
 * - The Last Seen Sequence Number and the current Sequence Number are the same.
 * <p>
 * TODO:
 * If the single data byte from a Zero Window Probe is dropped by the receiver (not ACKed),
 * then a subsequent segment should not be flagged as retransmission
 * if all of the following conditions are true for that segment:
 * - The segment size is larger than one.
 * - The next expected sequence number is one less than the current sequence number.
 */
public class Retransmission {
    public boolean calculate(Segment segment) {
        if (segment.isAlgoKeepAlive()){
            return false;
        }
        if (segment.getAlgoZeroWindowProbe()){
            return false;
        }
        if (segment.getAlgoSpuriousRetransmission()){
            return false;
        }
        if (segment.getAlgoFastRetransmission()){
            return false;
        }
        if (segment.getOutOfOrder()){
            return false;
        }
        if (segment.getWinSize() == 0) {
            return false;
        }
        if (segment.getSegDataSize() == 0) {
            return false;
        }
        if (segment.getLastSeenSegmentLength() != segment.getSegDataSize()){
            return false;
        }
        if (segment.getLastSeenSeqNum() != segment.getSeqNum()){
            return false;
        }
        return true;
    }
}
