package com.bath.tcpviz.dis.domain;

import com.bath.tcpviz.dis.algorithms.Segment;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TimingCalculatorTest {

    @Test
    public void canCalculateSegmentOccurredWithin20msSinceLastDupAck() {
        TimingCalculator tc = new TimingCalculator();
        List<Segment> segments = tc.calculateDupAckArrivalTimeForFastRetransmission(makeGoodSegmentsWithTimings());
        // for (Segment segment : segments) {
        //     System.out.println(segment.getPacketNumber() + " " + segment.isLastDupAckArrivedWithin20ms());
        // }
        Assert.assertTrue(segments.get(3).isLastDupAckArrivedWithin20ms());
    }

    @Test
    public void canCalculateSegmentOccurredAfter200msSinceAck() {
        TimingCalculator tc = new TimingCalculator();
        List<Segment> segments = tc.calculateRetransmissionTime(makeGoodSegmentsWithTimings());
        for (Segment segment : segments) {
            System.out.println(segment.getPacketNumber() + " " + segment.isLastAckArrivedAfter200ms());
        }
        // Assert.assertTrue(segments.get(3).isLastDupAckArrivedWithin20ms());

    }

    @Test
    public void canCalculateSegmentOccurredWithin3ms() {
        //For OoO
        TimingCalculator tc = new TimingCalculator();
        List<Segment> segments = tc.calculateLastSegArrivedWithin3ms(makeGoodSegmentsWithTimings());
        for (Segment segment : segments) {
            System.out.println(segment.getPacketNumber() + " " + segment.isLastSegArrivedWithin4ms());
        }
        // Assert.assertTrue(segments.get(1).isLastSegArrivedWithin4ms());

    }


    private List<Segment> makeGoodSegmentsWithTimings() {
        List<Segment> result = new ArrayList<>();
        Segment segment1 = new Segment();
        segment1.setRelativePacketNumber(1);
        segment1.setTs(Timestamp.valueOf("2000-01-01 10:10:10.099000"));
        segment1.setSrcIpAndPort("client");

        Segment segment2 = new Segment();
        segment2.setRelativePacketNumber(2);
        segment2.setDupAckCounter(4);
        segment2.setTs(Timestamp.valueOf("2000-01-01 10:10:10.100000"));
        segment2.setSrcIpAndPort("server");

        Segment segment3 = new Segment();
        segment3.setRelativePacketNumber(3);
        segment3.setTs(Timestamp.valueOf("2000-01-01 10:10:10.120000"));
        segment3.setSrcIpAndPort("server");

        Segment segment4 = new Segment();
        segment4.setRelativePacketNumber(4);
        segment4.setDupAckCounter(4);
        segment4.setTs(Timestamp.valueOf("2000-01-01 10:10:10.123000"));
        segment4.setSrcIpAndPort("client");

        Segment segment5 = new Segment();
        segment5.setRelativePacketNumber(5);
        segment5.setTs(Timestamp.valueOf("2000-01-01 10:10:10.543000"));
        segment5.setSrcIpAndPort("server");

        result.add(segment1);
        result.add(segment2);
        result.add(segment3);
        result.add(segment4);
        result.add(segment5);

        return result;
    }

    @Test
    public void canCalculateRelativeTime() {
        TimingCalculator tc = new TimingCalculator();
        List<Segment> segments = tc.calculateRelativeTime(makeGoodSegmentsWithTimings());
        for (Segment segment : segments) {
            System.out.println(segment.getRelativePacketNumber() + " " + segment.getRelativeTime());
        }
        // Assert.assertTrue(segments.get(3).isLastDupAckArrivedWithin20ms());
    }
}