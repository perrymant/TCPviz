package com.bath.tcpviz.dis.algorithms;

/**
 * TCP Port numbers reused
 * - Set when the SYN flag is set (not SYN+ACK),
 * - we have an existing conversation using the same addresses and ports,
 * - and the Sequence Number is different than the existing conversation’s initial Sequence Number.
 */
public class ReusedPorts {
    public boolean calculate(Segment segment) {
        if (!segment.isSyn()) {
            return false;
        }
        if (!segment.isPortUsed()){
            return false;
        }
        return true;
    }
}
