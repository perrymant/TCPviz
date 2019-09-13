package com.bath.tcpviz.dis.algorithms;

/**
 * TCP ZeroWindowProbe
 * Set when the following are true:
 * - The SYN, FIN, or RST flags are not set.
 * - This packet is not the first segment in the stream for the machine.
 * - The Segment Length is one.
 * - The next expected sequence number is one less than the current sequence number.
 * - Set when the next expected sequence number is not equal to the sequence number minus one.
 * - The Reverse Window Size is zero.
 * <p>
 * This affects “Fast Retransmission”, “Out-Of-Order”, or “Retransmission”.
 */
public class ZeroWindowProbe {
    public boolean calculate(Segment segment) {
        if (segment.isSyn() || segment.isFin() || segment.isRst()) {
            return false;
        }
        if (segment.getFirstSegmentForDevice()){
            return false;
        }
        if (segment.getSegDataSize() != 1) {
            return false;
        }
        if (segment.getNextExpSeqNum() == (segment.getSeqNum() - 1)) {
            return false;
        }
        if (segment.getRevWinSize() != 0) {
            return false;
        }
        if (segment.isAlgoKeepAlive()){
            segment.setAlgoKeepAlive(false);
        }
        return true;
    }
}