package com.bath.tcpviz.dis.algorithms;

/**
 * TCP Keep-Alive ACK
 * Set when all of the following are true:
 * - This is not a Reused Ports packet
 * - The most recently seen packet in the reverse direction was a Keep-Alive.
 * - The packet is not a SYN, FIN, or RST.
 * - The segment size is zero.
 * - The window size hasn’t changed.
 * - The current Sequence Number is the same as the Next Expected Sequence Number.
 * - The current Acknowledgement Number is the same as the Last-Seen Acknowledgement Number.
 * <p>
 * - Supersedes “Dup ACK” and “ZeroWindowProbeAck”.
 */
public class KeepAliveAck {
    public boolean calculate(Segment segment) {
        if (segment.getAlgoReusedPorts()) {
            return false;
        }
        if (segment.getAlgoKeepAliveAck()) {
            return false;
        }
        if (!segment.isRevKeepAlive()) {
            return false;
        }
        if (segment.isSyn() || segment.isFin() || segment.isRst()) {
            return false;
        }
        if (segment.getSegDataSize() != 0) {
            return false;
        }
        if (segment.getLastSeenWinSize() != segment.getWinSize()) {
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
