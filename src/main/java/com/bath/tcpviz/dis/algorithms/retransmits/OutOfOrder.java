package com.bath.tcpviz.dis.algorithms.retransmits;

import com.bath.tcpviz.dis.algorithms.Segment;

/**
 * TCP Out-Of-Order
 * Set when all of the following are true:
 * - This is not a Keep-Alive packet.
 * - the Segment Length is greater than zero.
 * - The Maximum Sequence Number is greater than the current Sequence Number.
 * - The Last Next Expected Sequence Number and the current Sequence Number differ.
 * - The last segment arrived within the calculated RTT (3ms by default). <- FROM packet-tcp.c:
 * (Note: UPDATED TO 4MS TO DEAL WITH PROPAGATION DELAY)
 * <p>
 * - Supersedes “Spurious Retransmission” and “Retransmission”.
*/
public class OutOfOrder {
    public boolean calculate(Segment segment) {
        if (segment.isAlgoKeepAlive()){
            return false;
        }
        if (segment.getSegDataSize() == 0) {
            return false;
        }
        if (segment.getSeqNum() >= segment.getMaxSeqNum()) {
            return false;
        }
        if (segment.getLastNextExpSeqNum() == segment.getSeqNum()){
            return false;
        }
        if (!segment.isLastSegArrivedWithin4ms()){
            return false;
        }
        if (segment.getAlgoFastRetransmission()) {
            return false;
        }
        return true;
    }
}
