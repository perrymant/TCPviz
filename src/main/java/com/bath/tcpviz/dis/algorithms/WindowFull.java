package com.bath.tcpviz.dis.algorithms;

/**
 * TCP Window Full
 * Set when the following are true:
 * - The segment size is non-zero.
 * - The SYN, RST and FIN flags are not set.
 * - The Bytes in Flight exceeds the window size in the reverse direction.
 * - If the Window Scaling is not known, but the Reverse Window Size is.
 * <p>
 * If we know the window scaling
 * and if this segment contains data and goes all the way to the
 * edge of the advertised window
 * then we mark it as WINDOW FULL
 */
public class WindowFull {
    public boolean calculate(Segment segment) {
        if (segment.getSegDataSize() == 0) {
            return false;
        }
        if (segment.isSyn() || segment.isRst() || segment.isFin()) {
            return false;
        }
        if (segment.getWindowScaling() == -2 && segment.getRevWinSize() <= 65534) { // No SYN SEEN -> Set this to -2.
            return false;
        }
        if (segment.getBytesInFlight() < segment.getRevCalculatedWinSize()) {
            return false;
        }
        return true;
    }
}
