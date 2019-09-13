package com.bath.tcpviz.dis.algorithms;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConnectionEstablishTest {
    private Segment segment1 = activeOpenMakeGoodSegment();
    private Segment segment2 = passiveOpenMakeGoodSegment();
    private Segment segment3 = handshakeCompleteGoodSegment();

    @Test
    public void activeOpenSuccessCase() {
        assertTrue(new ConnectionEstablish().calculate1_activeOpen(activeOpenMakeGoodSegment()));
    }

    @Test
    public void activeOpenFailsWhenSynIsFalse() {
        segment1.setSyn(false);
        assertFalse(new ConnectionEstablish().calculate1_activeOpen(segment1));
    }

    @Test
    public void activeOpenFailsWhenAckIsTrue() {
        segment1.setAck(true);
        assertFalse(new ConnectionEstablish().calculate1_activeOpen(segment1));
    }

    @Test
    public void activeOpenFailsWhenNextSynAndAckAreFalse() {
        segment1.getNext().setSyn(false);
        segment1.getNext().setAck(false);
        assertFalse(new ConnectionEstablish().calculate1_activeOpen(segment1));
    }

    private Segment activeOpenMakeGoodSegment() {
        Segment curr = new Segment();
        curr.setSyn(true);
        curr.setAck(false);
        // curr.setSegDataSize(0);
        Segment next = new Segment();
        next.setSyn(true);
        next.setAck(true);
        curr.setNext(next);
        return curr;
    }

    @Test
    public void passiveOpenSuccessCase() {
        assertTrue(new ConnectionEstablish().calculate2_passiveOpen(passiveOpenMakeGoodSegment()));
    }

    @Test
    public void passiveOpenFailsWhenSynIsFalse() {
        segment2.setSyn(false);
        assertFalse(new ConnectionEstablish().calculate2_passiveOpen(segment2));
    }

    @Test
    public void passiveOpenFailsWhenAckIsFalse() {
        segment2.setAck(false);
        assertFalse(new ConnectionEstablish().calculate2_passiveOpen(segment2));
    }

    @Test
    public void passiveOpenFailsWhenPrevSynIsFalse() {
        segment2.getPrev().setSyn(false);
        assertFalse(new ConnectionEstablish().calculate2_passiveOpen(segment2));
    }

    private Segment passiveOpenMakeGoodSegment() {
        Segment curr = new Segment();
        curr.setSyn(true);
        curr.setAck(true);
        // curr.setSegDataSize(0);
        Segment prev = new Segment();
        prev.setSyn(true);
        prev.setAck(false);
        curr.setPrev(prev);
        return curr;
    }

    @Test
    public void handshakeCompleteFailsWhenPrevNotSynAndNotAck() {
        segment3.getPrev().setSyn(false);
        segment3.getPrev().setAck(false);
        assertFalse(new ConnectionEstablish().calculate3_handshakeComplete(segment3));
    }

    private Segment handshakeCompleteGoodSegment() {
        Segment curr = new Segment();
        curr.setAck(true);
        Segment prev = new Segment();
        prev.setSyn(true);
        prev.setAck(true);
        curr.setPrev(prev);
        return curr;
    }

}