package com.bath.tcpviz.dis.algorithms;

/**
 * TCP Window Update
 * Set when the all of the following are true:
 * - This is not a Keep-Alive packet.
 * - The Segment Length is zero.
 * - None of SYN, FIN, or RST are set.
 * - The Window Size is non-zero and not equal to the Last-Seen Window Size.
 * - The Sequence Number is equal to the Next Expected Sequence Number.
 * - The Acknowledgement Number is equal to the Last-Seen Acknowledgement Number.
 */
public class WindowUpdate {
    public boolean calculate(Segment segment) {
        if (segment.isAlgoKeepAlive()) {
            return false;
        }
        if (segment.isSyn() || segment.isFin() || segment.isRst()) {
            return false;
        }
        if (segment.getSegDataSize() != 0) {
            return false;
        }
        if (segment.getWinSize() == 0) {
            return false;
        }
        if (segment.getWinSize() == segment.getLastSeenWinSize()) {
            return false;
        }
        if (segment.getSeqNum() != segment.getNextExpSeqNum()) {
            return false;
        }
        if (segment.getAckNum() != segment.getLastSeenAckNum()) {
            return false;
        }
        return true;

    }
}
