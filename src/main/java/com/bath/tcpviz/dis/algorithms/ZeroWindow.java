package com.bath.tcpviz.dis.algorithms;

/*
 * TCP ZeroWindow
 * Set when the following are true:
 * - The Window Size is zero.
 * - The SYN, FIN, or RST flags are not set.
 * */
public class ZeroWindow {
    public boolean calculate(Segment segment) {
        if ((segment.getWinSize() != 0)) {
            return false;
        }
        if ((segment.getWinSize() == 0) && (segment.isSyn() || segment.isFin() || segment.isRst())) {
            return false;
        }
        return true;
    }
}
