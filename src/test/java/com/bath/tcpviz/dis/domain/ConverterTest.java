package com.bath.tcpviz.dis.domain;

import com.bath.tcpviz.dis.algorithms.Segment;
import org.junit.Assert;
import org.junit.Test;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConverterTest {
    private TcpDump tcpDump = new TcpDump();
    private List<Packet> packets = new ArrayList<>();
    private Converter converter = new Converter();

    private void readPcap(String fileName) {
        try {
            packets = tcpDump.readPcapFile(fileName);
        } catch (PcapNativeException | NotOpenException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void canConvertTCPDumpIntoListOfPacketsAndCheckSize() {
        String fileName = "src/main/resources/pcapFiles/TCPOC.pcap";
        readPcap(fileName);
        Assert.assertEquals(10, packets.size());
    }

    @Test
    public void givenTCPDumpWithOnePacket_CanConvertToSegment() {
        String fileName = "src/main/resources/pcapFiles/single syn test.pcap";
        readPcap(fileName);
        Assert.assertEquals(1, packets.size());
        List<Segment> convertedSegments = converter.segmentGenerator(packets);
        Assert.assertEquals(1, convertedSegments.size());
    }

    @Test
    public void givenTCPDumpWithDupack_CanConvertToSegmentAndCheckAlgo() {
        String fileName = "src/main/resources/pcapFiles/dupack test.pcap";
        readPcap(fileName);
        Assert.assertEquals(2, packets.size());
        List<Segment> convertedSegments = converter.segmentGenerator(packets);
        Assert.assertEquals(2, convertedSegments.size());
    }

    @Test
    public void givenTCPDumpWithTCPOC_Segments1_to_3_HaveExpectedPropertiesForAlgorithm() {
        String fileName = "src/main/resources/pcapFiles/TCPOC.pcap";
        readPcap(fileName);
        List<Segment> convertedSegments = converter.segmentGenerator(packets);
        Assert.assertEquals(packets.size(), convertedSegments.size());
        // Packets 1-3: [Syn, Syn+Ack, Ack]:
        Assert.assertTrue(convertedSegments.get(0).isSyn());
        Assert.assertFalse(convertedSegments.get(0).isAck());
        Assert.assertTrue(convertedSegments.get(1).isSyn());
        Assert.assertTrue(convertedSegments.get(1).isAck());
        Assert.assertTrue(convertedSegments.get(1).getPrev().isSyn());
        Assert.assertFalse(convertedSegments.get(2).isSyn());
        Assert.assertTrue(convertedSegments.get(2).isAck());
        Assert.assertTrue(convertedSegments.get(2).getPrev().isSyn());
        Assert.assertFalse(convertedSegments.get(3).isSyn());
    }

    @Test
    public void canReadCorrectSegmentSizeForSynAndFin() {
        String fileName = "src/main/resources/pcapFiles/TCPOC.pcap";
        // segments 1, 2, are Syn and 8, 9 are Fin:
        // they should have 1 phantom bit of data added
        // in packet -> segment conversion
        readPcap(fileName);
        List<Segment> convertedSegments = converter.segmentGenerator(packets);

        Assert.assertEquals(1, convertedSegments.get(0).getSegDataSize());
        Assert.assertEquals(1, convertedSegments.get(1).getSegDataSize());
        Assert.assertEquals(1, convertedSegments.get(7).getSegDataSize());
        Assert.assertEquals(1, convertedSegments.get(8).getSegDataSize());
    }

    @Test
    public void canReadCorrectSegmentSize() {
        String fileName = "src/main/resources/pcapFiles/TCPOC.pcap";
        // segments 1, 2, are Syn and 8, 9 are Fin:
        // they should have 1 phantom bit of data added
        // in packet -> segment conversion
        readPcap(fileName);
        List<Segment> convertedSegments = converter.segmentGenerator(packets);

        Assert.assertEquals(1, convertedSegments.get(0).getSegDataSize());
        Assert.assertEquals(1, convertedSegments.get(1).getSegDataSize());
        Assert.assertEquals(1, convertedSegments.get(7).getSegDataSize());
        Assert.assertEquals(1, convertedSegments.get(8).getSegDataSize());
    }

    @Test
    public void canCalculateWinSizeInRev() {
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        // String fileName = "src/main/resources/pcapFiles/window full 2.pcap";
        // issue with window full 2.pcap???
        String fileName = "src/main/resources/pcapFiles/testbif.pcap";
        readPcap(fileName);
        List<Segment> convertedSegments = converter.segmentGenerator(packets);
        algorithmConverter.calculateClientAndServer(convertedSegments);
        List<Segment> segmentsLSAN = algorithmConverter.calculateLastSeenAckNum(convertedSegments);
        List<Segment> segments = algorithmConverter.calculateReverseWindowProperties(segmentsLSAN);
        Assert.assertEquals(0, segments.get(0).getRevWinSize());
        Assert.assertEquals(5840, segments.get(1).getRevWinSize());
        Assert.assertEquals(5792, segments.get(2).getRevWinSize());
        Assert.assertEquals(5792, segments.get(3).getRevWinSize());
        Assert.assertEquals(5840, segments.get(4).getRevWinSize());
        Assert.assertEquals(5840, segments.get(5).getRevWinSize());
        Assert.assertEquals(5840, segments.get(6).getRevWinSize());
    }

    @Test
    public void LSANCalculate() {
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        List<Segment> segments = makeGoodLSANStream();
        algorithmConverter.calculateClientAndServer(segments);
        List<Segment> segmentsLSAN = algorithmConverter.calculateLastSeenAckNum(segments);

        Assert.assertEquals(-1, segmentsLSAN.get(0).getLastSeenAckNum());
        Assert.assertEquals(0, segmentsLSAN.get(1).getLastSeenAckNum());
        Assert.assertEquals(353, segmentsLSAN.get(2).getLastSeenAckNum());
        Assert.assertEquals(353, segmentsLSAN.get(3).getLastSeenAckNum());
        Assert.assertEquals(111, segmentsLSAN.get(4).getLastSeenAckNum());
    }

    private List<Segment> makeGoodLSANStream() {
        List<Segment> result = new ArrayList<>();
        Segment segment1 = new Segment();
        segment1.setRelativePacketNumber(0);
        segment1.setAckNum(0);
        segment1.setSrcIpAndPort("client");
        // segment1.setLastSeenAckNum(-1);

        Segment segment2 = new Segment();
        segment2.setRelativePacketNumber(1);
        segment2.setAckNum(353);
        segment2.setSrcIpAndPort("server");
        // segment2.setLastSeenAckNum(0);

        Segment segment3 = new Segment();
        segment3.setRelativePacketNumber(2);
        segment3.setAckNum(111);
        segment3.setSrcIpAndPort("client");
        // segment3.setLastSeenAckNum(353);

        Segment segment4 = new Segment();
        segment4.setRelativePacketNumber(3);
        segment4.setAckNum(88);
        segment4.setSrcIpAndPort("client");
        // segment4.setLastSeenAckNum(353);

        Segment segment5 = new Segment();
        segment5.setRelativePacketNumber(4);
        segment5.setAckNum(363);
        segment5.setSrcIpAndPort("server");
        // segment5.setLastSeenAckNum(111); <----

        result.add(segment1);
        result.add(segment2);
        result.add(segment3);
        result.add(segment4);
        result.add(segment5);

        return result;
    }

    @Test
    public void canCalculateLSAN() {
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        String fileName = "src/main/resources/pcapFiles/testbif.pcap";
        readPcap(fileName);
        List<Segment> convertedSegments = converter.segmentGenerator(packets);
        algorithmConverter.calculateClientAndServer(convertedSegments);
        List<Segment> segmentsLSAN = algorithmConverter.calculateLastSeenAckNum(convertedSegments);

        Assert.assertEquals(-1, segmentsLSAN.get(0).getLastSeenAckNum());
        Assert.assertEquals(0, segmentsLSAN.get(1).getLastSeenAckNum());
        Assert.assertEquals(3534527477L, segmentsLSAN.get(2).getLastSeenAckNum());
        Assert.assertEquals(3534527477L, segmentsLSAN.get(3).getLastSeenAckNum());
        Assert.assertEquals(3760361797L, segmentsLSAN.get(4).getLastSeenAckNum());

        // LSAN needs to be the maximum value, overriding the last seen one's value (which may be lower)
        // 3760368438 is the most-recent AckNum, but 3760369908L was seen one packet before - which is a greater value
        Assert.assertEquals(3760369908L, segmentsLSAN.get(23).getLastSeenAckNum());
    }

    @Test
    public void canCalculatePrevAckNum() {
        String fileName = "src/main/resources/pcapFiles/keepAliveAck.pcap";
        // packets 4, 6, 8 are keepAliveAck (according to WS)
        readPcap(fileName);
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        List<Segment> convertedSegments = converter.segmentGenerator(packets);
    }

    @Test
    public void canCalculateBytesInFlight() {
        // BIF required for Algo Window Full
        // TCP payload size is calculated by taking the "Total Length" from the IP header (ip.len)
        // and then substract the "IP header length" (ip.hdr_len) and the "TCP header length" (tcp.hdr_len).
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        // String fileName = "src/main/resources/pcapFiles/window full 2.pcap";
        // issue with window full 2.pcap???
        String fileName = "src/main/resources/pcapFiles/testbif.pcap";
        readPcap(fileName);
        List<Segment> convertedSegments = converter.segmentGenerator(packets);
        algorithmConverter.calculateClientAndServer(convertedSegments);
        List<Segment> segments = algorithmConverter.calculateIsRevKeepAlive(convertedSegments);
        List<Segment> segmentsLSAN = algorithmConverter.calculateLastSeenAckNum(segments);
        List<Segment> bifSegments = algorithmConverter.calculateBytesInFlight(segmentsLSAN);

        Assert.assertEquals(0, bifSegments.get(0).getBytesInFlight());
        Assert.assertEquals(0, bifSegments.get(1).getBytesInFlight());
        Assert.assertEquals(139, bifSegments.get(3).getBytesInFlight());
        Assert.assertEquals(1368, bifSegments.get(5).getBytesInFlight());
        Assert.assertEquals(1368, bifSegments.get(23).getBytesInFlight());
        Assert.assertEquals(65535, bifSegments.get(425).getBytesInFlight());// WindowFull
        Assert.assertEquals(61560, bifSegments.get(504).getBytesInFlight());
        Assert.assertEquals(65535, bifSegments.get(548).getBytesInFlight());
        Assert.assertEquals(65535, bifSegments.get(585).getBytesInFlight());// WindowFull
    }

    @Test
    public void canDetermineIPProtocol() {
        String arpMessage = "";
        String arpMessageResolved = "";
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        // String fileName = "src/main/resources/pcapFiles/window full 2.pcap";
        // String fileName = "src/main/resources/pcapFiles/ACKed Unseen Segment.pcap";
        String fileName = "src/main/resources/pcapFiles/multiple_protocols1.pcap";
        readPcap(fileName);
        List<Segment> convertedSegments = converter.segmentGenerator(packets);
        algorithmConverter.calculateClientAndServer(convertedSegments);
        for (Segment segment : convertedSegments) {
            if (segment.getProtocol().equals("ARP")) {
                arpMessage = segment.getArpMessage();
                arpMessageResolved = segment.getArpMessageResolved();
            }
        }
    }

    @Test
    public void canDisplayProtocolInformation() {
        // ie: ICMP error type, UDP message, DNS, HTTP?
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        // String fileName = "src/main/resources/pcapFiles/window full 2.pcap";
        String fileName = "src/main/resources/pcapFiles/arp_dns_example.pcap";
        // String fileName = "src/main/resources/pcapFiles/multiple_protocols1.pcap";
        readPcap(fileName);
        List<Segment> convertedSegments = converter.segmentGenerator(packets);
        algorithmConverter.calculateClientAndServer(convertedSegments);
        // for (Segment segment : convertedSegments) {
        //     if (segment.getProtocol().equals("ICMPv6") || segment.getProtocol().equals("ICMPv4")) {
        //         System.out.println(segment.getProtocol() + " " + segment.getSrcIpAndPort() + " " + segment.getIcmpMessage());
        //     } else if (segment.getProtocol().equals("ARP")) {
        //         System.out.println(segment.getProtocol() + " " + segment.getSrcIpAndPort() + " " + segment.getArpMessageResolved());
        //     } else if (segment.getProtocol().equals("DNS")) {
        //         if (segment.getDnsResponse() == null) {
        //             System.out.println(segment.getProtocol() + " " + segment.getSrcIpAndPort() + " Query: " + segment.getDnsQuery());
        //         } else {
        //             System.out.println(segment.getProtocol() + " " + segment.getSrcIpAndPort() + " Response: " + segment.getDnsQuery() + " -> " + segment.getDnsResponse());
        //         }
        //     } else {
        //         System.out.println(segment.getProtocol() + " " + segment.getSrcIpAndPort());
        //         // System.out.println(segment.getProtocol() + " " );
        //     }
        // }
    }

    @Test
    public void canDealWithSingleSidedTCP() {
        String fileName = "src/main/resources/pcapFiles/single sided TCP.pcap";
        // String fileName = "src/main/resources/pcapFiles/window full 2.pcap";
        readPcap(fileName);
        Converter converter = new Converter();
        HashMap<Integer, List<Segment>> streamGenerator = converter.streamGenerator(packets);
        // the only stream, with 1 segment
        List<Segment> segments = streamGenerator.get(1);
        System.out.println(segments.get(0).getSrcIpAndPort() + " --> " + segments.get(0).getDstIpAndPort());
        AlgorithmConverter ac = new AlgorithmConverter();
        List<Segment> segments1 = ac.applyAllAnalysisAlgorithms(segments);
        // single or double sided?
        // if (ac.isDoubleSidedConversation(segments)) {
        //     testme = ac.applyAllAnalysisAlgorithms(segments);
        // } else {
        //     testme = segments;
        // }
        System.out.println(segments1.size());
    }

    @Test
    public void canGenerateStreamMap() {
        String fileName = "src/main/resources/pcapFiles/Example.pcap";
        Stream stream = new Stream();
        HashMap<Integer, List<Segment>> analysedStreams = stream.analysedPcapFileToStreamMap(fileName);

        for (int i = 1; i < analysedStreams.size() + 1; i++) {
            List<Segment> segmentList = analysedStreams.get(i);
            System.out.println("\n" + i + "============================");
            for (Segment segment : segmentList) {
                List<String> analysisFlag = new ArrayList<>();
                if (segment.getAlgoSyn1()) {
                    analysisFlag.add("TCP Connection establishment: active open");
                } else if (segment.getAlgoSyn2()) {
                    analysisFlag.add("TCP Connection establishment: passive open");
                } else if (segment.getAlgoSyn3()) {
                    analysisFlag.add("TCP Connection establishment: three-way handshake complete");
                } else if (segment.getAlgoDupAck()) {
                    analysisFlag.add("TCP Duplicate ACK" + segment.getDupAckFrameCountRelative() + "#" + segment.getDupAckCounter());
                } else if (segment.isAlgoKeepAlive()) {
                    analysisFlag.add("TCP Keep-Alive");
                } else if (segment.getACKedUnseenSegment()) {
                    analysisFlag.add("TCP ACKed Unseen Segment");
                } else if (segment.isAlgoWindowUpdate()) {
                    analysisFlag.add("TCP Window Update");
                } else if (segment.isAlgoWindowFull()) {
                    analysisFlag.add("TCP Window Full");
                } else if (segment.getAlgoZeroWindow()) {
                    analysisFlag.add("TCP Zero Window");
                } else if (segment.getAlgoZeroWindowProbe()) {
                    analysisFlag.add("TCP Zero Window Probe");
                } else if (segment.getAlgoZeroWindowProbeAck()) {
                    analysisFlag.add("TCP Zero Window Probe ACK");
                } else if (segment.getAlgoSpuriousRetransmission()) {
                    analysisFlag.add("TCP Spurious Retransmission");
                } else if (segment.getAlgoFastRetransmission()) {
                    analysisFlag.add("TCP Fast Retransmission");
                } else if (segment.getOutOfOrder()) {
                    analysisFlag.add("TCP Out of Order");
                } else if (segment.getPotentialRetransmission()) {
                    analysisFlag.add("TCP Retransmission");
                } else if (segment.getPreviousSegmentNotCaptured()) {
                    analysisFlag.add("TCP Previous Segment Not Captured");
                }
                System.out.println(segment.getStreamId() + " " + segment.getPacketNumber() + " " + Arrays.toString(analysisFlag.toArray()));
            }
        }
    }

    @Test
    public void badseg() {
        // /Users/unityspace/Documents/BathUni/Semester 2/disertation-code-reboot/src/main/resources/pcapFiles/newbadseg.pcap
    }

    @Test
    public void canCalculateWindowScaling() {
        String fileName = "src/main/resources/example/Establish Connection.pcap";
        readPcap(fileName);
        Assert.assertEquals(10, packets.size());
        Assert.assertEquals(6, packets.get(0).getWindowScaling());
        Assert.assertEquals(7, packets.get(1).getWindowScaling());
        Assert.assertEquals(-2, packets.get(2).getWindowScaling());
    }

}