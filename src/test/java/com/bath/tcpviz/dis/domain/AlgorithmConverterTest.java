package com.bath.tcpviz.dis.domain;

import com.bath.tcpviz.dis.algorithms.Segment;
import org.junit.Assert;
import org.junit.Test;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlgorithmConverterTest {
    private TcpDump tcpDump = new TcpDump();
    private List<Packet> packets = new ArrayList<>();
    private Converter converter = new Converter();

    @Test
    public void givenTCPDumpWithTCPOC_Algo_ConnectionEstablish_Segments1_to_3_AreTrue() {
        String fileName = "src/main/resources/pcapFiles/TCPOC.pcap";
        // First 3 packets establish connection.
        readPcap(fileName);
        List<Segment> convertedSegments = converter.segmentGenerator(packets);
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        algorithmConverter.applyEstablishConnection(convertedSegments);
        Assert.assertTrue(convertedSegments.get(0).getAlgoSyn1());
        Assert.assertTrue(convertedSegments.get(1).getAlgoSyn2());
        Assert.assertTrue(convertedSegments.get(2).getAlgoSyn3());
    }

    private void readPcap(String fileName) {
        try {
            packets = tcpDump.readPcapFile(fileName);
        } catch (PcapNativeException | NotOpenException e) {
            e.printStackTrace();
        }
    }

    // @Test
    // public void canCreateListOfClientAndServerSegments() {
    //     List<Segment> segments = makeIdealClientAndServer();
    //     AlgorithmConverter algorithmConverter = new AlgorithmConverter();
    //     algorithmConverter.calculateClientAndServer(segments);
    //     List<Segment> clients = algorithmConverter.clientSelector(segments);
    //     List<Segment> servers = algorithmConverter.serverSelector(segments);
    //     Assert.assertEquals(2, clients.size());
    //     Assert.assertEquals(1, servers.size());
    // }
    //
    // private List<Segment> makeIdealClientAndServer() {
    //     List<Segment> segments = new ArrayList<>();
    //     Segment segment1 = new Segment();
    //     segment1.setSrcIpAndPort("192.0.0.1:1900");
    //     segments.add(segment1);
    //     Segment segment2 = new Segment();
    //     segment2.setSrcIpAndPort("300.300.300.300:80");
    //     segments.add(segment2);
    //     Segment segment3 = new Segment();
    //     segment3.setSrcIpAndPort("192.0.0.1:1900");
    //     segments.add(segment3);
    //     return segments;
    // }

    @Test
    public void canCalculateSizeOfClientOrServerListFromFile() {
        String fileName = "src/main/resources/pcapFiles/tcp issues.pcap";
        // File has 10 client segments and 9 server segments
        readPcap(fileName);
        List<Segment> convertedSegments = converter.segmentGenerator(packets);
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        algorithmConverter.calculateClientAndServer(convertedSegments);
        int clientSegments = 0;
        int serverSegments = 0;

        // System.out.println(algorithmConverter.client);
        // System.out.println(algorithmConverter.server);
        // Assert.assertEquals(10, clientSegments);
        // Assert.assertEquals(9, serverSegments);
    }

    @Test
    public void canRefactorZeroWindowAlgorithm() {
        String fileName = "src/main/resources/pcapFiles/tcp issues.pcap";
        // packets 4 and 18 have ZeroWindow.
        readPcap(fileName);
        List<Segment> convertedSegments = converter.segmentGenerator(packets);
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        algorithmConverter.calculateClientAndServer(convertedSegments);
        List<Segment> segments = algorithmConverter.applyZeroWindow(convertedSegments);
        Assert.assertFalse(segments.get(2).getAlgoZeroWindow());
        Assert.assertTrue(segments.get(3).getAlgoZeroWindow());
        Assert.assertTrue(segments.get(17).getAlgoZeroWindow());
    }

    @Test
    public void canCalculateAndSetOppositeDirectionProperties() {
        String fileName = "src/main/resources/pcapFiles/TCPOC.pcap";
        readPcap(fileName);
        List<Segment> convertedSegments = converter.segmentGenerator(packets);
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        algorithmConverter.calculateClientAndServer(convertedSegments);
        List<Segment> segmentsWithLSAN = algorithmConverter.calculateLastSeenAckNum(convertedSegments);
        Assert.assertEquals(-1, segmentsWithLSAN.get(0).getLastSeenAckNum());
        Assert.assertEquals(2427299989L, segmentsWithLSAN.get(2).getLastSeenAckNum());
        Assert.assertEquals(1044768424L, segmentsWithLSAN.get(4).getLastSeenAckNum());
    }


    @Test
    public void canRefactorWindowUpdate() {
        String fileName = "src/main/resources/pcapFiles/window update 2.pcap";
        // packet 9 has window update (according to ws)
        readPcap(fileName);
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        List<Segment> convertedSegments = converter.segmentGenerator(packets);
        algorithmConverter.calculateClientAndServer(convertedSegments);
        List<Segment> segments = algorithmConverter.applyWindowUpdate(convertedSegments);
        Assert.assertTrue(segments.get(8).isAlgoWindowUpdate());
        Assert.assertFalse(segments.get(0).isAlgoWindowUpdate());
    }

    @Test
    public void canCalculateZeroWindowProbe() {
        String fileName = "src/main/resources/pcapFiles/zero window probe.pcap";
        // Packet NUmber 11 & 13 are ZWP
        readPcap(fileName);
        List<Segment> convertedSegments = converter.segmentGenerator(packets);
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        algorithmConverter.calculateClientAndServer(convertedSegments);
        List<Segment> segments = algorithmConverter.calculateReverseWindowProperties(convertedSegments);
        List<Segment> zeroWindowProbeSegments = algorithmConverter.applyZeroWindowProbe(segments);
        Assert.assertTrue(zeroWindowProbeSegments.get(10).getAlgoZeroWindowProbe());
        Assert.assertTrue(zeroWindowProbeSegments.get(12).getAlgoZeroWindowProbe());
        Assert.assertFalse(zeroWindowProbeSegments.get(0).getAlgoZeroWindowProbe());
        Assert.assertFalse(zeroWindowProbeSegments.get(9).getAlgoZeroWindowProbe());
    }


    @Test
    public void canStoreMaxSeqToBeAcked() {
        List<Segment> segments = makeMaxSeqSegmentList();
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        algorithmConverter.calculateClientAndServer(segments);
        List<Segment> segmentList = algorithmConverter.storeMaxRevSeqAndData(segments);
        Assert.assertEquals(-1, algorithmConverter.storeMaxRevSeqAndData(segmentList).get(0).getMaxRevSeqAndData());
        Assert.assertEquals(1, algorithmConverter.storeMaxRevSeqAndData(segmentList).get(1).getMaxRevSeqAndData());
        Assert.assertEquals(1, algorithmConverter.storeMaxRevSeqAndData(segments).get(2).getMaxRevSeqAndData());
        Assert.assertEquals(12, algorithmConverter.storeMaxRevSeqAndData(segments).get(3).getMaxRevSeqAndData());
        Assert.assertEquals(1223, algorithmConverter.storeMaxRevSeqAndData(segments).get(4).getMaxRevSeqAndData());
    }

    private List<Segment> makeMaxSeqSegmentList() {
        List<Segment> result = new ArrayList<>();
        Segment segment1 = new Segment();
        segment1.setPacketNumber(0);
        segment1.setSeqNum(1);
        segment1.setSrcIpAndPort("client");
        Segment segment2 = new Segment();
        segment2.setPacketNumber(1);
        segment2.setSeqNum(12);
        segment2.setSrcIpAndPort("server");
        Segment segment3 = new Segment();
        segment3.setPacketNumber(2);
        segment3.setSeqNum(1);
        segment3.setSrcIpAndPort("server");
        Segment segment4 = new Segment();
        segment4.setPacketNumber(3);
        segment4.setSeqNum(1223);
        segment4.setSrcIpAndPort("client");
        Segment segment5 = new Segment();
        segment5.setPacketNumber(4);
        segment5.setSeqNum(122);
        segment5.setSrcIpAndPort("server");
        result.add(segment1);
        result.add(segment2);
        result.add(segment3);
        result.add(segment4);
        result.add(segment5);
        return result;
    }

    @Test
    public void canCalculateRelativeSeqNumAndAckNum() {
        String fileName = "src/main/resources/pcapFiles/ACKed Unseen Segment.pcap";
        // String fileName = "src/main/resources/pcapFiles/dupack 2.pcap";
        readPcap(fileName);
        List<Segment> convertedSegments = converter.segmentGenerator(packets);
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        algorithmConverter.calculateClientAndServer(convertedSegments);
        List<Segment> segments = algorithmConverter.initialSeqNumAndCalculateRelativeSeqAndAckNum(convertedSegments);
        // for (Segment segment : segments) {
        //     System.out.println(segment.getPacketNumber() + " " + segment.getSrcIpAndPort());
        //     System.out.println(" SN " + segment.getSeqNum() +  " RSN " + segment.getRelativeSeqNum());
        //     System.out.println(" AN " + segment.getAckNum() +  " RAN " + segment.getRelativeAckNum());
        // }
        Assert.assertEquals(0, segments.get(0).getRelativeSeqNum());
        Assert.assertEquals(0, segments.get(0).getRelativeAckNum());
        Assert.assertEquals(0, segments.get(1).getRelativeSeqNum());
        Assert.assertEquals(1, segments.get(1).getRelativeAckNum());
        Assert.assertEquals(122, segments.get(4).getRelativeAckNum());
        Assert.assertEquals(122, segments.get(10).getRelativeSeqNum());
        Assert.assertEquals(3443, segments.get(10).getRelativeAckNum());
        Assert.assertEquals(3443, segments.get(11).getRelativeSeqNum());
        Assert.assertEquals(123, segments.get(11).getRelativeAckNum());
    }

    @Test
    public void canCalculateACKedUnseenSegment() {
        String fileName = "src/main/resources/pcapFiles/ACKed Unseen Segment.pcap";
        // Packet Number 8 is ACKedUnseenSegment
        readPcap(fileName);
        List<Segment> convertedSegments = converter.segmentGenerator(packets);
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        algorithmConverter.calculateClientAndServer(convertedSegments);
        List<Segment> segments = algorithmConverter.applyACKedUnseenSegment(convertedSegments);

        Assert.assertTrue(segments.get(7).getACKedUnseenSegment());
        Assert.assertFalse(segments.get(0).getACKedUnseenSegment());
    }

    @Test
    public void canCalculatePreviousSegmentNotCaptured() {
        String fileName = "src/main/resources/pcapFiles/Previous segment not captured.pcap";
        // Packet Number 0, 26, 28 & 64 are PSNC
        readPcap(fileName);
        List<Segment> convertedSegments = converter.segmentGenerator(packets);
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        algorithmConverter.calculateClientAndServer(convertedSegments);
        List<Segment> segments = algorithmConverter.applyPreviousSegmentNotCaptured(convertedSegments);

        Assert.assertTrue(segments.get(1).getPreviousSegmentNotCaptured());
        Assert.assertTrue(segments.get(27).getPreviousSegmentNotCaptured());
        Assert.assertTrue(segments.get(29).getPreviousSegmentNotCaptured());
        Assert.assertTrue(segments.get(65).getPreviousSegmentNotCaptured());
        Assert.assertFalse(segments.get(0).getPreviousSegmentNotCaptured());
    }

    @Test
    public void canCalculateDeltaTimingForStream() {
        String fileName = "src/main/resources/pcapFiles/Previous segment not captured.pcap";
        readPcap(fileName);
        List<Segment> convertedSegments = converter.segmentGenerator(packets);
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        List<Segment> segments = algorithmConverter.calculateDeltaTiming(convertedSegments);

        Assert.assertEquals("0.000000", segments.get(0).getDeltaFormatted());
        Assert.assertEquals("0.049490", segments.get(1).getDeltaFormatted());
        Assert.assertEquals("0.000219", segments.get(2).getDeltaFormatted());
    }


    @Test
    public void canCalculateDupAck() {
        String fileName = "src/main/resources/pcapFiles/dupack 2.pcap";
        // String fileName = "src/main/resources/pcapFiles/Fast Retransmission 1.pcap";
        // packets 11, 12, 13, 15, 19, 31, 32, 33, 34  have dupack (according to ws)
        readPcap(fileName);
        List<Segment> convertedSegments = converter.segmentGenerator(packets);
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        algorithmConverter.calculateClientAndServer(convertedSegments);
        List<Segment> segments = algorithmConverter.applyDupAck(convertedSegments);

        Assert.assertTrue(segments.get(10).getAlgoDupAck());
        Assert.assertEquals(7, segments.get(10).getDupAckFrameCountRelative());
        Assert.assertEquals(1, segments.get(10).getDupAckCounter());
        Assert.assertTrue(segments.get(11).getAlgoDupAck());
        Assert.assertEquals(7, segments.get(11).getDupAckFrameCountRelative());
        Assert.assertEquals(2, segments.get(11).getDupAckCounter());
        Assert.assertTrue(segments.get(12).getAlgoDupAck());
        Assert.assertEquals(7, segments.get(12).getDupAckFrameCountRelative());
        Assert.assertEquals(3, segments.get(12).getDupAckCounter());
        Assert.assertTrue(segments.get(14).getAlgoDupAck());
        Assert.assertEquals(7, segments.get(14).getDupAckFrameCountRelative());
        Assert.assertEquals(4, segments.get(14).getDupAckCounter());
        Assert.assertTrue(segments.get(18).getAlgoDupAck());
        Assert.assertEquals(17, segments.get(18).getDupAckFrameCountRelative());
        Assert.assertEquals(1, segments.get(18).getDupAckCounter());
        Assert.assertTrue(segments.get(30).getAlgoDupAck());
        Assert.assertEquals(26, segments.get(30).getDupAckFrameCountRelative());
        Assert.assertEquals(1, segments.get(30).getDupAckCounter());
        Assert.assertTrue(segments.get(31).getAlgoDupAck());
        Assert.assertTrue(segments.get(32).getAlgoDupAck());
        Assert.assertTrue(segments.get(33).getAlgoDupAck());
        Assert.assertFalse(segments.get(0).getAlgoDupAck());
        Assert.assertFalse(segments.get(6).getAlgoDupAck());
    }

    @Test
    public void canCalculateFastRetransmission() {
        String fileName = "src/main/resources/pcapFiles/Fast Retransmission 1.pcap";
        // Packets 6, 7, 8 have Fast Retransmission.
        readPcap(fileName);
        List<Segment> convertedSegments = converter.segmentGenerator(packets);
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        algorithmConverter.calculateClientAndServer(convertedSegments);
        List<Segment> segments = algorithmConverter.applyFastRetransmission(convertedSegments);

        Assert.assertTrue(segments.get(1).getAlgoDupAck());
        Assert.assertTrue(segments.get(2).getAlgoDupAck());
        Assert.assertTrue(segments.get(3).getAlgoDupAck());
        Assert.assertTrue(segments.get(9).getAlgoDupAck());
        Assert.assertTrue(segments.get(10).getAlgoDupAck());
        Assert.assertTrue(segments.get(11).getAlgoDupAck());

        Assert.assertTrue(segments.get(5).getAlgoFastRetransmission());
        Assert.assertTrue(segments.get(6).getAlgoFastRetransmission());
        Assert.assertTrue(segments.get(7).getAlgoFastRetransmission());
        Assert.assertFalse(segments.get(0).getAlgoFastRetransmission());
    }

    @Test
    public void canCalculateLastRevDupAckDelta() {
        String fileName = "src/main/resources/pcapFiles/Fast Retransmission 1.pcap";
        readPcap(fileName);
        List<Segment> convertedSegments = converter.segmentGenerator(packets);
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        List<Segment> segmentsWithDeltaTiming = algorithmConverter.calculateDeltaTiming(convertedSegments);
        List<Segment> segmentsWithDupAck = algorithmConverter.applyDupAck(segmentsWithDeltaTiming);
        List<Segment> segments = algorithmConverter.applyFastRetransmission(segmentsWithDupAck);

        Assert.assertEquals("0.000000", segments.get(0).getDeltaFormatted());
        Assert.assertEquals("0.000001", segments.get(1).getDeltaFormatted());
        Assert.assertEquals("0.199270", segments.get(8).getDeltaFormatted());
    }

    @Test
    public void canCalculateSpuriousRetransmission() {
        String fileName = "src/main/resources/pcapFiles/Spurious Retransmission.pcap";
        // 22, 25, 27, 29, 32, 34, 36, 38, 50, 53, 55, 57, 59 have Spurious Retransmission
        readPcap(fileName);
        List<Segment> convertedSegments = converter.segmentGenerator(packets);
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        algorithmConverter.calculateClientAndServer(convertedSegments);
        List<Segment> segments = algorithmConverter.applySpuriousRetransmission(convertedSegments);

        Assert.assertTrue(segments.get(21).getAlgoSpuriousRetransmission());
        Assert.assertTrue(segments.get(24).getAlgoSpuriousRetransmission());
        Assert.assertTrue(segments.get(26).getAlgoSpuriousRetransmission());
        Assert.assertTrue(segments.get(28).getAlgoSpuriousRetransmission());
        Assert.assertTrue(segments.get(31).getAlgoSpuriousRetransmission());
        Assert.assertTrue(segments.get(33).getAlgoSpuriousRetransmission());
        Assert.assertTrue(segments.get(35).getAlgoSpuriousRetransmission());
        Assert.assertTrue(segments.get(49).getAlgoSpuriousRetransmission());
        Assert.assertTrue(segments.get(52).getAlgoSpuriousRetransmission());
        Assert.assertTrue(segments.get(54).getAlgoSpuriousRetransmission());
        Assert.assertTrue(segments.get(56).getAlgoSpuriousRetransmission());

        Assert.assertFalse(segments.get(0).getAlgoSpuriousRetransmission());
        Assert.assertFalse(segments.get(1).getAlgoSpuriousRetransmission());
        Assert.assertFalse(segments.get(2).getAlgoSpuriousRetransmission());
        Assert.assertFalse(segments.get(3).getAlgoSpuriousRetransmission());

        // WS labels 38 & 59 - as SR:
        // both not related to last-seen acknowledgement number - I believe they are normal Retransmissions
        // Assert.assertTrue(segments.get(37).getAlgoSpuriousRetransmission());
        // Assert.assertTrue(segments.get(58).getAlgoSpuriousRetransmission());
    }

    @Test
    public void canCalculateRetransmission() {
        String fileName = "src/main/resources/pcapFiles/Retransmission 4.pcap";
        // Packet 10 has Retransmission
        // String fileName = "src/main/resources/pcapFiles/testbif.pcap";
        // "src/main/resources/pcapFiles/Retransmission 1.pcap"; has an error in WS
        readPcap(fileName);
        List<Segment> convertedSegments = converter.segmentGenerator(packets);
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        algorithmConverter.calculateClientAndServer(convertedSegments);
        List<Segment> segments = algorithmConverter.applyRetransmission(convertedSegments);

        Assert.assertTrue(segments.get(9).getPotentialRetransmission());
        Assert.assertEquals(7, segments.get(9).getPotentialRetransmissionOf());
    }

    @Test
    public void canCalculateZeroWindowProbeAck() {
        String fileName = "src/main/resources/pcapFiles/zero window probe.pcap";
        // Packet NUmber 12 & 14 are ZWPA
        readPcap(fileName);
        List<Segment> convertedSegments = converter.segmentGenerator(packets);
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        algorithmConverter.calculateClientAndServer(convertedSegments);
        List<Segment> zeroWindowProbeAckSegments = algorithmConverter.applyZeroWindowProbeAck(convertedSegments);

        Assert.assertTrue(zeroWindowProbeAckSegments.get(11).getAlgoZeroWindowProbeAck());
        Assert.assertTrue(zeroWindowProbeAckSegments.get(13).getAlgoZeroWindowProbeAck());
        Assert.assertFalse(zeroWindowProbeAckSegments.get(0).getAlgoZeroWindowProbeAck());
        Assert.assertFalse(zeroWindowProbeAckSegments.get(10).getAlgoZeroWindowProbeAck());
    }

    @Test
    public void canCalculateClientAndServerWithJustOneMachine() {
        List<Segment> segments = makeClientServerSegmentList();
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        algorithmConverter.calculateClientAndServer(segments);
    }

    private List<Segment> makeClientServerSegmentList() {
        List<Segment> result = new ArrayList<>();
        Segment segment1 = new Segment();
        segment1.setPacketNumber(0);
        segment1.setProtocol("TCP");
        segment1.setStreamId(1);
        segment1.setSeqNum(1);
        segment1.setSrcIpAndPort("client");
        Segment segment2 = new Segment();
        segment2.setPacketNumber(1);
        segment2.setSeqNum(12);
        segment2.setSrcIpAndPort("server");
        Segment segment3 = new Segment();
        segment3.setPacketNumber(2);
        segment3.setSeqNum(1);
        segment3.setSrcIpAndPort("server");
        Segment segment4 = new Segment();
        segment4.setPacketNumber(3);
        segment4.setSeqNum(1223);
        segment4.setSrcIpAndPort("client");
        Segment segment5 = new Segment();
        segment5.setPacketNumber(4);
        segment5.setSeqNum(122);
        segment5.setSrcIpAndPort("server");
        result.add(segment1);
        result.add(segment2);
        result.add(segment3);
        result.add(segment4);
        result.add(segment5);
        return result;
    }

    @Test
    public void canCalculateKeepAlive() {
        String fileName = "src/main/resources/pcapFiles/keepalive.pcap";
        // packets 3, 5 & 7 are keepalive (according to WS).
        readPcap(fileName);
        List<Segment> convertedSegments = converter.segmentGenerator(packets);
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        algorithmConverter.calculateClientAndServer(convertedSegments);
        List<Segment> applyKeepAlive = algorithmConverter.applyKeepAlive(convertedSegments);
        Assert.assertFalse(applyKeepAlive.get(0).isAlgoKeepAlive());
        Assert.assertFalse(applyKeepAlive.get(1).isAlgoKeepAlive());
        Assert.assertTrue(applyKeepAlive.get(2).isAlgoKeepAlive());
        Assert.assertTrue(applyKeepAlive.get(4).isAlgoKeepAlive());
        Assert.assertTrue(applyKeepAlive.get(6).isAlgoKeepAlive());
    }

    @Test
    public void canCalculateKeepAliveAck() {
        String fileName = "src/main/resources/pcapFiles/keepAliveAck.pcap";
        // packets 4, 6, 8 are keepAliveAck (according to WS)
        readPcap(fileName);
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        List<Segment> convertedSegments = converter.segmentGenerator(packets);
        algorithmConverter.calculateClientAndServer(convertedSegments);
        List<Segment> applyKeepAliveAck = algorithmConverter.applyKeepAliveAck(convertedSegments);
        Assert.assertTrue(applyKeepAliveAck.get(3).isRevKeepAlive());
        Assert.assertTrue(applyKeepAliveAck.get(5).isRevKeepAlive());
        Assert.assertTrue(applyKeepAliveAck.get(7).isRevKeepAlive());
        Assert.assertFalse(applyKeepAliveAck.get(0).isRevKeepAlive());

        Assert.assertTrue(applyKeepAliveAck.get(3).getAlgoKeepAliveAck());
        Assert.assertTrue(applyKeepAliveAck.get(5).getAlgoKeepAliveAck());
        Assert.assertTrue(applyKeepAliveAck.get(7).getAlgoKeepAliveAck());
        Assert.assertFalse(applyKeepAliveAck.get(0).getAlgoKeepAliveAck());
        Assert.assertFalse(applyKeepAliveAck.get(1).getAlgoKeepAliveAck());
        Assert.assertFalse(applyKeepAliveAck.get(2).getAlgoKeepAliveAck());
    }

    @Test
    public void canCalculateOutOfOrder() {
        String fileName = "src/main/resources/pcapFiles/Out of Order.pcap";
        readPcap(fileName);
        List<Segment> convertedSegments = converter.segmentGenerator(packets);
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        algorithmConverter.calculateClientAndServer(convertedSegments);
        List<Segment> segments = algorithmConverter.applyOutOfOrder(convertedSegments);

        Assert.assertTrue(segments.get(8).getOutOfOrder());
    }

    @Test
    public void canCalculateStatistics() {
        String fileName = "src/main/resources/pcapFiles/Out of Order.pcap";
        readPcap(fileName);
        List<Segment> convertedSegments = converter.segmentGenerator(packets);
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        List<Segment> segments = algorithmConverter.applyAllAnalysisAlgorithms(convertedSegments);
        System.out.println(segments.get(0).getTotalPacketsInStream());
        System.out.println(segments.get(0).getTotalPacketsLost());
        System.out.println(segments.get(0).getTotalDupAcks());
        // total data (bytes) in stream
        Assert.assertEquals(9597, segments.get(0).getTotalDataInStream());
        Assert.assertEquals(9597, segments.get(1).getTotalDataInStream());
    }

    @Test
    public void canCalculateTheNumberOfAnalysisFlags() {

        String fileName = "src/main/resources/example/ReusedPorts.pcap";

        readPcap(fileName);
        List<Segment> convertedSegments = converter.segmentGenerator(packets);
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        List<Segment> segments = algorithmConverter.applyAllAnalysisAlgorithms(convertedSegments);
        for (Segment seg : segments) {
            System.out.println(seg.getRelativePacketNumber() +
                    " " + (seg.getAnalysisFlagMap().keySet().toArray()[0]) +
                    " " +  Arrays.toString(seg.getAnalysisFlagMap().values().toArray()));
        }

        // Assert.assertEquals(9597, segments.get(0).getNumberOfAnalysisFlags());
    }

    @Test
    public void canCalculateReusedPorts() {
            String fileName = "src/main/resources/example/ReusedPorts.pcap";
            readPcap(fileName);
            List<Segment> convertedSegments = converter.segmentGenerator(packets);
            AlgorithmConverter algorithmConverter = new AlgorithmConverter();
            List<Segment> segments = algorithmConverter.applyReusedPorts(convertedSegments);
            for (Segment segment : segments) {
                System.out.println(segment.getAlgoReusedPorts());
            }
    }

    @Test
    public void canCalculateCalculatedWindowSize() {
        // PCAP with known Window Scaling
        String fileName = "src/main/resources/example/Establish Connection.pcap";
        readPcap(fileName);
        List<Segment> convertedSegments = converter.segmentGenerator(packets);
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        algorithmConverter.calculateClientAndServer(convertedSegments);
        List<Segment> segments = algorithmConverter.calculateWindowScalingAndSize(convertedSegments);

        Assert.assertEquals(6, segments.get(2).getWindowScaling());
        Assert.assertEquals(6, segments.get(3).getWindowScaling());
        Assert.assertEquals(7, segments.get(4).getWindowScaling());

        Assert.assertEquals(65535, segments.get(0).getCalculatedWindowSize());
        Assert.assertEquals(28560, segments.get(1).getCalculatedWindowSize());
        Assert.assertEquals(131328, segments.get(2).getCalculatedWindowSize());
        Assert.assertEquals(29696, segments.get(5).getCalculatedWindowSize());

        // PCAP with unknown Window Scaling
        String fileName2 = "src/main/resources/example/GoodBehaviour.pcap";
        readPcap(fileName2);
        List<Segment> convertedSegments2 = converter.segmentGenerator(packets);
        AlgorithmConverter algorithmConverter2 = new AlgorithmConverter();
        algorithmConverter2.calculateClientAndServer(convertedSegments2);
        List<Segment> segments2 = algorithmConverter2.calculateWindowScalingAndSize(convertedSegments2);

        Assert.assertEquals(-2, segments2.get(0).getWindowScaling());
        Assert.assertEquals(-2, segments2.get(2).getWindowScaling());
        Assert.assertEquals(239, segments2.get(0).getCalculatedWindowSize());
        Assert.assertEquals(2025, segments2.get(9).getCalculatedWindowSize());
    }

    @Test
    public void canCalculateWindowFull() {
        String fileName = "src/main/resources/pcapFiles/testbif.pcap";
        readPcap(fileName);
        List<Segment> convertedSegments = converter.segmentGenerator(packets);
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        algorithmConverter.calculateClientAndServer(convertedSegments);
        List<Segment> windowFull = algorithmConverter.applyAlgoWindowFull(convertedSegments);

        Assert.assertTrue(windowFull.get(425).isAlgoWindowFull());
        Assert.assertTrue(windowFull.get(585).isAlgoWindowFull());
        Assert.assertTrue(windowFull.get(623).isAlgoWindowFull());
        Assert.assertTrue(windowFull.get(656).isAlgoWindowFull());
        Assert.assertTrue(windowFull.get(745).isAlgoWindowFull());
        Assert.assertFalse(windowFull.get(0).isAlgoWindowFull());
    }

}