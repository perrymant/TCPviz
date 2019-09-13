package com.bath.tcpviz.dis.algorithms;

/**
 * TCP ConnectionEstablish
 * Has three states:
 * 1) activeOpen:
 * - SYN is set
 * - ACK is not set
 * - The next segment is has both SYN and ACK set
 * 2) passiveOpen:
 * - SYN is set
 * - ACK is set
 * - The last segment had SYN set
 * 3) handshakeComplete:
 * - ACK is set
 * - The last segment had SYN and ACK set
 */
public class ConnectionEstablish {
    public boolean calculate1_activeOpen(Segment segment) {
        if (!segment.isSyn()) {
            return false;
        }
        if (segment.isAck()) {
            return false;
        }
        if (!segment.getNext().isSyn() && !segment.getNext().isAck()){
            return false;
        }
        return true;
    }

    public boolean calculate2_passiveOpen(Segment segment) {
        if (segment.getAlgoReusedPorts()) {
            return false;
        }
        if (!segment.isSyn()) {
            return false;
        }
        if (!segment.isAck()) {
            return false;
        }
        if (!segment.getPrev().isSyn()) {
            return false;
        }
        return true;
    }

    public boolean calculate3_handshakeComplete(Segment segment) {
        if (!segment.isAck()){
            return false;
        }
        if (!segment.getPrev().isSyn()) {
            return false;
        }
        if (!segment.getPrev().isAck()) {
            return false;
        }
        return true;
    }
}

