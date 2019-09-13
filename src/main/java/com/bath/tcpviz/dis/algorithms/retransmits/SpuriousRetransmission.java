package com.bath.tcpviz.dis.algorithms.retransmits;

import com.bath.tcpviz.dis.algorithms.Segment;

/**
 * TCP Spurious Retransmission
 * Set when all of the following are true:
 * - This is not a Keep-Alive or Out-of-Order packet.
 * - The segment length is greater than zero.
 * - The current Sequence Number + Segment Length is less than or equal to the Last Reverse Acknowledgement Number
 * - Supersedes “Retransmission”.
 * <p>
 * Check for spurious retransmission. If the current seq + segment length
 * is less than or equal to the current lastack, the packet contains
 * duplicate data and may be considered spurious.
 * The SYN or FIN flag is set. <---- NOT TRUE
 */
public class SpuriousRetransmission {
    public boolean calculate(Segment segment) {
        if (segment.isAlgoKeepAlive()) {
            return false;
        }
        if (segment.getOutOfOrder()) {
            return false;
        }
        if (segment.getSegDataSize() == 0) {
            return false;
        }
        if ((segment.getSeqNum() + segment.getSegDataSize()) > segment.getLastRevAckNum()) {
            return false;
        }
        return true;
    }
}
