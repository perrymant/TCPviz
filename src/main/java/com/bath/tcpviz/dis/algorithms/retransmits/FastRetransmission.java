package com.bath.tcpviz.dis.algorithms.retransmits;

import com.bath.tcpviz.dis.algorithms.Segment;

/**
 * TCP Fast Retransmission
 * Set when all of the following are true:
 * - This is not a Keep-Alive packet.
 * - In the forward direction, the segment size is greater than zero or the SYN or FIN is set.
 * - The Last Next Expected Sequence Number is greater than the current Sequence Number.
 * - The current Sequence Number equals the Last Reverse Acknowledgement Number.
 * - We have more than two duplicate ACKs in the reverse direction.
 * - If the packet occurs within 20ms of the last duplicate ack
 * <p>
 * Supersedes “Out-Of-Order”, “Spurious Retransmission”, and “Retransmission”.
 */
public class FastRetransmission {
    public boolean calculate(Segment segment) {
        if (segment.isAlgoKeepAlive()) {
            return false;
        }
        if (segment.getSegDataSize() == 0 && !segment.isSyn() && !segment.isFin()) {
            return false;
        }
        if (segment.getLastNextExpSeqNum() <= segment.getSeqNum()) {
            return false;
        }
        if (segment.getSeqNum() != segment.getLastRevAckNum()) {
            return false;
        }
        if (segment.getRevDupAckCounter() <= 2) {
            return false;
        }
        if (!segment.isLastDupAckArrivedWithin20ms()) {
            return false;
        }
        return true;
    }
}
