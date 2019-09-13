package com.bath.tcpviz.dis.algorithms;

import org.junit.Test;

import static org.junit.Assert.*;

/*
 * TCP ZeroWindow
 * Set when the window size is zero and none of SYN, FIN, or RST are set.
 * */
public class ZeroWindowTest {
    private Segment segment = makeGoodSegment();

    @Test
    public void successCase() {
        assertTrue(new ZeroWindow().calculate(makeGoodSegment()));
    }

    @Test
    public void failsWhenWinSizeIsNonZero() {
        segment.setWinSize(359);
        segment.setSyn(true);
        assertFalse(new ZeroWindow().calculate(segment));
    }

    @Test
    public void failsWhenWinSizeIsNonZeroAndFlagsAreSet() {
        segment.setWinSize(359);
        segment.setSyn(true);
        segment.setFin(true);
        segment.setRst(true);
        assertFalse(new ZeroWindow().calculate(segment));
    }

    @Test
    public void failsWhenWinSizeIsZeroAndFlagsAreSet() {
        segment.setWinSize(0);
        segment.setSyn(true);
        segment.setFin(true);
        segment.setRst(true);
        assertFalse(new ZeroWindow().calculate(segment));
    }

    private Segment makeGoodSegment() {
        Segment segment = new Segment();
        segment.setWinSize(0);
        segment.setSyn(false);
        segment.setFin(false);
        segment.setRst(false);
        return segment;
    }

}