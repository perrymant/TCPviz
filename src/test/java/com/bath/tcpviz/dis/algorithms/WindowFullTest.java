package com.bath.tcpviz.dis.algorithms;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * TCP Window Full
 * Set when the segment size is non-zero,
 * we know the window size in the reverse direction,
 * and our segment size exceeds the window size in the reverse direction.
 * <p>
 * If we know the window scaling
 * and if this segment contains data and goes all the way to the
 * edge of the advertised window
 * then we mark it as WINDOW FULL
 * SYN/RST/FIN packets are never WINDOW FULL
 */
public class WindowFullTest {
    private Segment segment = makeGoodSegment();

    @Test
    public void successCase() {
        assertTrue(new WindowFull().calculate(makeGoodSegment()));
    }

    @Test
    public void failsWhenWindowScalingIsNotKnownAndRevWinSizeIsNot65535() {
        segment.setWindowScaling(-2);
        segment.setWindowScaling(1);
    }

    @Test
    public void failsWhenBIFLessThanRevCalculatedWinSize() {
        segment.setBytesInFlight(99);
        segment.setRevCalculatedWinSize(100);
        assertFalse(new WindowFull().calculate(segment));
    }

    @Test
    public void failsWhenSYN_RST_FIN_flagsAreSet() {
        segment.setSyn(true);
        segment.setFin(true);
        segment.setRst(true);
        assertFalse(new WindowFull().calculate(segment));
    }

    @Test
    public void failsWhenSegSizeIsZero() {
        segment.setSegDataSize(0);
        assertFalse(new WindowFull().calculate(segment));
    }

    private Segment makeGoodSegment() {
        Segment segment = new Segment();
        segment.setWindowScaling(1);
        segment.setRevWinSize(99);
        segment.setBytesInFlight(20);
        segment.setRevCalculatedWinSize(10);
        segment.setSegDataSize(100);
        segment.setSyn(false);
        segment.setFin(false);
        segment.setRst(false);
        return segment;
    }
}