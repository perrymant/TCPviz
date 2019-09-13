package com.bath.tcpviz.dis.algorithms;

/**
 * TCP ZeroWindowProbeAck
 * Set when the all of the following are true:
 * - The SYN, FIN, or RST flags are not set.
 * - The Segment Length is zero.
 * - The Window Size is zero.
 * - The Sequence Number is equal to the Next Expected Sequence Number.
 * - The Acknowledgement Number is equal to the Last-Seen Acknowledgement Number.
 * - The last-seen packet in the reverse direction was a zero window probe.
 * <p>
 * - Supersedes “TCP Dup ACK”.
 */
public class ZeroWindowProbeAck {
    public boolean calculate(Segment segment) {
        if (segment.isSyn() || segment.isFin() || segment.isRst()) {
            return false;
        }
        if (segment.getSegDataSize() != 0) {
            return false;
        }
        if (segment.getWinSize() != 0) {
            return false;
        }
        if (segment.getSeqNum() != segment.getNextExpSeqNum()) {
            return false;
        }
        if (segment.getAckNum() != segment.getLastSeenAckNum()) {
            return false;
        }
        if (!segment.getRevLastSeenZeroWinProbe()) {
            return false;
        }
        if (segment.getAlgoKeepAliveAck()){
            segment.setAlgoKeepAliveAck(false);
        }
        return true;
    }
}
