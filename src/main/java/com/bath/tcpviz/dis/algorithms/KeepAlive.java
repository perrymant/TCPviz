package com.bath.tcpviz.dis.algorithms;

/**
 * TCP Keep-Alive
 * Set when all of the following are true:
 * - This is not a Reused Ports or Zero Window Probe packet.
 * - The Segment Length is zero or one.
 * - SYN, FIN, or RST flags are not set.
 * - This is not the first packet in a stream.
 * - The current Sequence Number is one byte less than the Last Next Expected Sequence Number.
 * <p>
 * Supersedes “Fast Retransmission”, “Out-Of-Order”, “Spurious Retransmission”, and “Retransmission”.
 */
public class KeepAlive {
    public boolean calculate(Segment segment) {
        if (segment.getAlgoReusedPorts()) {
            return false;
        }
        if (segment.getAlgoZeroWindowProbe()) {
            return false;
        }
        if (segment.getSegDataSize() >= 2) {
            return false;
        }
        if (segment.isSyn() || segment.isFin() || segment.isRst()) {
            return false;
        }
        if (segment.getRelativePacketNumber() == 1) {
            return false;
        }
        if (segment.getSeqNum() != (segment.getLastNextExpSeqNum() - 1)) {
            return false;
        }
        return true;
    }
}

