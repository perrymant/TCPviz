package com.bath.tcpviz.dis.algorithms;

/**
 * TCP Dup ACK <frame>#<acknowledgement number>
 * Set when all of the following are true:
 * - This is not a Keep-Alive or Keep-Alive ACK packet.
 * - SYN, FIN, and RST are not set.
 * - The segment size is zero.
 * - The window size is non-zero and has not changed.
 * The next expected sequence number and last-seen acknowledgment number are non-zero
 * (i.e. the connection has been established).
 * <p>
 * Keep track of frame and AckNum DupACK refer to.
 * If the ACK number changed we must reset the DupACK counters.
 */
public class DupAck {
    private int frameCountClientRelative;
    private int frameCountServerRelative;
    private int frameCountClientAbsolute;
    private int frameCountServerAbsolute;
    private int dupAckCountClient;
    private int dupAckCountServer;

    public boolean calculate(Segment segment, boolean direction) {
        if (direction) {
            if (segment.getLastSeenAckNum() != segment.getAckNum()) {
                frameCountClientRelative = segment.getRelativePacketNumber();
                frameCountClientAbsolute = segment.getPacketNumber();
                dupAckCountClient = 0;
                return false;
            }
        } else {
            if (segment.getLastSeenAckNum() != segment.getAckNum()) {
                frameCountServerRelative = segment.getRelativePacketNumber();
                frameCountServerAbsolute = segment.getPacketNumber();
                dupAckCountServer = 0;
                return false;
            }
        }
        if (segment.isAlgoKeepAlive() || segment.getAlgoKeepAliveAck()) {
            return false;
        }
        if (segment.isSyn() || segment.isFin() || segment.isRst()) {
            return false;
        }
        if (segment.getRelativePacketNumber() == 1) {
            return false;
        }
        if (segment.getSegDataSize() != 0) {
            return false;
        }
        if (segment.getWinSize() == 0) {
            return false;
        }
        if (segment.getLastSeenWinSize() != segment.getWinSize()) {
            return false;
        }
        if (segment.getNextExpSeqNum() == 0 && segment.getLastSeenAckNum() == 0) {
            return false;
        }
        // if AckNum is the same:
        if (direction) {
            if (segment.getLastSeenAckNum() == segment.getAckNum()) {
                dupAckCountClient++;
                segment.setDupAckFrameCountRelative(frameCountClientRelative);
                segment.setDupAckFrameCountAbsolute(frameCountClientAbsolute);
                segment.setDupAckCounter(dupAckCountClient);
            }
        } else {
            if (segment.getLastSeenAckNum() == segment.getAckNum()) {
                dupAckCountServer++;
                segment.setDupAckFrameCountRelative(frameCountServerRelative);
                segment.setDupAckFrameCountAbsolute(frameCountServerAbsolute);
                segment.setDupAckCounter(dupAckCountServer);
            }
        }
        return true;
    }
}