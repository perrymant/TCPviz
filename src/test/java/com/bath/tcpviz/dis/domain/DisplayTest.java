package com.bath.tcpviz.dis.domain;

import com.bath.tcpviz.dis.algorithms.Segment;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DisplayTest {


    @Test
    public void canCreateListOfAnalysisFlagsPresentInPcap() {
        Display display = new Display();
        String fileName = "src/main/resources/pcapFiles/TCPOC.pcap";
        int streamId = 1;
        Stream stream = new Stream();
        HashMap<Integer, List<Segment>> segmentMap = stream.analysedPcapFileToStreamMap(fileName);
        HashMap<Integer, List<ViewLabel>> analysisFlagList = display.createAnalysisFlagList(segmentMap);

        Assert.assertEquals("TCP Connection establishment: active open", analysisFlagList.get(streamId).get(0).getAnalysisMessage());
        Assert.assertEquals("TCP Connection establishment: passive open", analysisFlagList.get(streamId).get(1).getAnalysisMessage());
        Assert.assertEquals("TCP Connection establishment: three-way handshake complete", analysisFlagList.get(streamId).get(2).getAnalysisMessage());
        Assert.assertEquals("TCP Application Data: 302 bytes in flight", analysisFlagList.get(streamId).get(3).getAnalysisMessage());
    }

    @Test
    public void canCreateControlFlagDisplay() {
        List<Segment> segments = makeControlBitSegments();
        Display display = new Display();
        Assert.assertEquals("SYN", display.controlFlagsToString(segments.get(0)));
        Assert.assertEquals("", display.controlFlagsToString(segments.get(1)));
        Assert.assertEquals("PSH", display.controlFlagsToString(segments.get(2)));
        Assert.assertEquals("SYN", display.controlFlagsToString(segments.get(4)));
        Assert.assertEquals("RST", display.controlFlagsToString(segments.get(5)));
        Assert.assertEquals("SYN, PSH", display.controlFlagsToString(segments.get(6)));

    }

    private List<Segment> makeControlBitSegments() {
        List<Segment> result = new ArrayList<>();
        Segment segment1 = new Segment();
        segment1.setSyn(true);

        Segment segment2 = new Segment();
        segment2.setAck(true);

        Segment segment3 = new Segment();
        segment3.setPsh(true);

        Segment segment4 = new Segment();
        segment4.setPsh(true);
        segment4.setAck(true);

        Segment segment5 = new Segment();
        segment5.setSyn(true);
        segment5.setAck(true);

        Segment segment6 = new Segment();
        segment6.setRst(true);

        Segment segment7 = new Segment();
        segment7.setSyn(true);
        segment7.setAck(true);
        segment7.setPsh(true);

        result.add(segment1);
        result.add(segment2);
        result.add(segment3);
        result.add(segment4);
        result.add(segment5);
        result.add(segment6);
        result.add(segment7);

        return result;
    }

    @Test
    public void canCreateArrowTopAndBottomMessages() {
        Display display = new Display();
        // String fileName = "src/main/resources/pcapFiles/TCPOC.pcap";
        // String fileName = "src/main/resources/pcapFiles/zero window probe.pcap";
        // String fileName = "src/main/resources/pcapFiles/tcp_stream_test.pcap";
        String fileName = "src/main/resources/pcapFiles/Example.pcap";
        Stream stream = new Stream();
        HashMap<Integer, List<Segment>> segmentMap = stream.analysedPcapFileToStreamMap(fileName);
        HashMap<Integer, List<ViewLabel>> analysisFlagList = display.createAnalysisFlagList(segmentMap);
        // HashMap<Integer, List<String>> viewDisplay = display.viewLabelsToStringLists(analysisFlagList);
        List<Integer> streadIdList = display.getKeys(analysisFlagList);
        System.out.println(analysisFlagList.get(4).get(2).getAbsolutePacketNumber());
        System.out.println(analysisFlagList.get(4).get(2).getRelativePacketNumber());
    }

    @Test
    public void canDetermineDirectionOfStream() {
        // Client if:
        //  1. first SYN
        //  2. Port Number >= 1024
        //  3. Private IP range :
        //   (10.0.0.0 – 10.255.255.255, 172.16.0.0 – 172.31.255.255, 192.168.0.0 - 192.168.255.255)
        Display display = new Display();
        String fileName = "src/main/resources/pcapFiles/TCPOC.pcap";
        // String fileName = "src/main/resources/pcapFiles/zero window probe.pcap";
        // String fileName = "src/main/resources/pcapFiles/tcp_stream_test.pcap";
        // String fileName = "src/main/resources/pcapFiles/Example.pcap";
        Stream stream = new Stream();
        HashMap<Integer, List<Segment>> segmentMap = stream.analysedPcapFileToStreamMap(fileName);
        HashMap<Integer, List<ViewLabel>> analysisFlagList = display.createAnalysisFlagList(segmentMap);

        if (analysisFlagList.get(1).get(0).getSYN() ||
                analysisFlagList.get(1).get(0).getSenderPort() >= 1024 ||
                analysisFlagList.get(1).get(0).getReceiverPort() < 1024) {
            // SRC is likely to be client, DST is likely to be Server
        }
    }

    @Test
    public void canPassSenderAndReceiverToThymeleaf() {
        int streamId = 3;
        Display display = new Display();
        Stream stream = new Stream();
        String fileName = "src/main/resources/pcapFiles/Example.pcap";
        HashMap<Integer, List<Segment>> segmentMap = stream.analysedPcapFileToStreamMap(fileName);
        HashMap<Integer, List<ViewLabel>> analysisFlagList = display.createAnalysisFlagList(segmentMap);
        // model.addAttribute("map", analysisFlagList);
        List<Integer> keys = display.getKeys(analysisFlagList);
        // model.addAttribute("listOfKeys", keys);
        List<ViewLabel> displayStreamIdPackets = analysisFlagList.get(streamId);

        System.out.println(displayStreamIdPackets.get(0).getSender());
        System.out.println(displayStreamIdPackets.get(0).getReceiver());
        System.out.println(displayStreamIdPackets.get(0).getRelativePacketNumber());
        System.out.println(displayStreamIdPackets.get(1).getRelativePacketNumber());

        // model.addAttribute("streamId", streamId);
        // model.addAttribute("packets", displayStreamIdPackets);
    }

    @Test
    public void canViewDeltaTime() {
        int streamId = 4;
        Display display = new Display();
        Stream stream = new Stream();
        String fileName = "src/main/resources/pcapFiles/Example.pcap";
        HashMap<Integer, List<Segment>> segmentMap = stream.analysedPcapFileToStreamMap(fileName);
        HashMap<Integer, List<ViewLabel>> analysisFlagList = display.createAnalysisFlagList(segmentMap);
        // model.addAttribute("map", analysisFlagList);
        List<Integer> keys = display.getKeys(analysisFlagList);
        // model.addAttribute("listOfKeys", keys);
        List<ViewLabel> displayStreamIdPackets = analysisFlagList.get(streamId);

        for (ViewLabel displayStreamIdPacket : displayStreamIdPackets) {
            System.out.println("RPN " + displayStreamIdPacket.getRelativePacketNumber());
            System.out.println("APN " + displayStreamIdPacket.getAbsolutePacketNumber());
            System.out.println("RSN " + displayStreamIdPacket.getRelativeSeqNum());
            System.out.println("ASN " + displayStreamIdPacket.getAbsoluteSeqNum());
            System.out.println("RAN " + displayStreamIdPacket.getRelativeAckNum());
            System.out.println("AAN " + displayStreamIdPacket.getAbsoluteAckNum());
            System.out.println("RT " + displayStreamIdPacket.getRelativeTime());
            System.out.println("AT " + displayStreamIdPacket.getAbsoluteTime());
            System.out.println("DT " + displayStreamIdPacket.getDeltaTime());
            System.out.println("+++++++++==========++++++++++++");
        }

    }

//    java.lang.IndexOutOfBoundsException: Index: 71,

    // @Test
    // public void checkError() {
    //         int streamId = 1;
    //         Display display = new Display();
    //         Stream stream = new Stream();
    //         // String fileName = "src/main/resources/pcapFiles/Example.pcap";
    //         String fileName = "src/main/resources/pcapFiles/newbadseg.pcap";
    //         HashMap<Integer, List<Segment>> segmentMap = stream.analysedPcapFileToStreamMap(fileName);
    //         HashMap<Integer, List<ViewLabel>> analysisFlagList = display.createAnalysisFlagList(segmentMap);
    //         System.out.println(analysisFlagList.get(streamId).get(1));
    // }

    @Test
    public void canGenerateUDPMessage() {
        int streamId = 1;
        Display display = new Display();
        Stream stream = new Stream();
        String fileName = "src/main/resources/pcapFiles/Example.pcap";
        HashMap<Integer, List<Segment>> segmentMap = stream.analysedPcapFileToStreamMap(fileName);
        HashMap<Integer, List<ViewLabel>> analysisFlagList = display.createAnalysisFlagList(segmentMap);

        List<ViewLabel> displayStreamIdPackets = analysisFlagList.get(streamId);
        for (ViewLabel packet : displayStreamIdPackets) {
            System.out.println(packet.getUdpMessage());
        }
    }

    @Test
    public void checkUDPlengthAndReadUDPMessage() {
        String fileName = "src/main/resources/pcapFiles/Example.pcap";
        int streamId = 1;
        Display display = new Display();
        Stream stream = new Stream();
        HashMap<Integer, List<Segment>> segmentMap = stream.analysedPcapFileToStreamMap(fileName);
        HashMap<Integer, List<ViewLabel>> analysisFlagList = display.createAnalysisFlagList(segmentMap);
        Assert.assertEquals("UDP", analysisFlagList.get(streamId).get(0).getProtocol());
        Assert.assertEquals(318, segmentMap.get(streamId).get(0).getUDPLength());
        Assert.assertEquals("UDP: Packet Length 318 Bytes", analysisFlagList.get(streamId).get(0).getMessageTopRelative());
    }

    @Test
    public void checkICMPMessage() {
        int streamId = 1;
        Display display = new Display();
        Stream stream = new Stream();
        String fileName = "src/main/resources/pcapFiles/icmp.pcap";
        HashMap<Integer, List<Segment>> segmentMap = stream.analysedPcapFileToStreamMap(fileName);
        HashMap<Integer, List<ViewLabel>> analysisFlagList = display.createAnalysisFlagList(segmentMap);

        Assert.assertEquals(("ICMPv6"), analysisFlagList.get(streamId).get(0).getProtocol());
        Assert.assertEquals("ICMPv6: Neighbor Solicitation", analysisFlagList.get(streamId).get(0).getMessageTopRelative());
        Assert.assertEquals("13:01:16.921961", analysisFlagList.get(streamId).get(0).getAbsoluteTime());
        Assert.assertEquals("00:00:00.000000", analysisFlagList.get(streamId).get(0).getRelativeTime());
        Assert.assertEquals("13:01:26.334106", analysisFlagList.get(4).get(1).getAbsoluteTime());
        Assert.assertEquals("00:00:04.645950", analysisFlagList.get(4).get(1).getRelativeTime());
        Assert.assertEquals("4.645950", analysisFlagList.get(4).get(1).getDeltaTime());
    }

    @Test
    public void canDisplayFileName() {
        String fileNameExample = "src/main/resources/pcapFiles/icmp.pcap";
        String substring = fileNameExample.substring(29);
        System.out.println(substring);
    }

    @Test
    public void dupack2Test() {
        String fileName = "src/main/resources/uploads/dupack 2.pcap";
        Display display = new Display();
        Stream stream = new Stream();
        HashMap<Integer, List<Segment>> segmentMap = stream.analysedPcapFileToStreamMap(fileName);
        HashMap<Integer, List<ViewLabel>> analysisFlagList = display.createAnalysisFlagList(segmentMap);
        for (ViewLabel viewLabel : analysisFlagList.get(1)) {
            System.out.println(viewLabel.getReceiverPort() );
        }
    }

    @Test
    public void canDisplayReusedPorts() {
        String fileName = "src/main/resources/example/ReusedPorts.pcap";
        int streamId = 1;
        Display display = new Display();
        Stream stream = new Stream();
        HashMap<Integer, List<Segment>> segmentMap = stream.analysedPcapFileToStreamMap(fileName);
        HashMap<Integer, List<ViewLabel>> analysisFlagList = display.createAnalysisFlagList(segmentMap);
        for (ViewLabel viewLabel : analysisFlagList.get(streamId)) {
            System.out.println(viewLabel.getRelativePacketNumber() + " " + viewLabel.getAnalysisMessage() + " " + viewLabel.getAnalysisMessage2());
        }
        Assert.assertEquals("TCP Port Numbers Reused", analysisFlagList.get(streamId).get(5).getAnalysisMessage() );
        Assert.assertEquals("TCP Port Numbers Reused", analysisFlagList.get(streamId).get(6).getAnalysisMessage() );
        Assert.assertEquals("TCP Port Numbers Reused", analysisFlagList.get(streamId).get(8).getAnalysisMessage() );
    }

    @Test
    public void canDisplayBreakingPcaps() {
        // Does not combine UDP and ICMPv4 in the same stream... WS does...
        String fileName = "src/main/resources/pcapFiles/pcapbreak/traceroute_MPLS.pcap";
        Display display = new Display();
        Stream stream = new Stream();
        HashMap<Integer, List<Segment>> segmentMap = stream.analysedPcapFileToStreamMap(fileName);
        HashMap<Integer, List<ViewLabel>> analysisFlagList = display.createAnalysisFlagList(segmentMap);

        Assert.assertEquals("UDP", analysisFlagList.get(1).get(0).getProtocol());
        Assert.assertEquals("ICMPv4", analysisFlagList.get(2).get(0).getProtocol());
    }

    @Ignore
    @Test
    public void canDisplayBreakingPcaps2() {
        // CAN NOT BE OPENED IN WIRESHARK EITHER
        String fileName = "src/main/resources/pcapFiles/pcapbreak/search.pcap";
        int streamId = 1;
        Display display = new Display();
        Stream stream = new Stream();
        HashMap<Integer, List<Segment>> segmentMap = stream.analysedPcapFileToStreamMap(fileName);
        System.out.println(segmentMap.size());
    }

    @Ignore
    @Test
    public void canDisplayBreakingPcaps5() {
        String fileName = "src/main/resources/pcapFiles/pcapbreak/aarp-heapoverflow-1.pcap";
        int streamId = 1;
        Display display = new Display();
        Stream stream = new Stream();
        HashMap<Integer, List<Segment>> segmentMap = stream.analysedPcapFileToStreamMap(fileName);
    }

    @Test
    public void canDisplayTLSerrors() {
        String fileName = "src/main/resources/example/TLSencryptedAlert1.pcap";
        int streamId = 1;
        Stream stream = new Stream();
        HashMap<Integer, List<Segment>> segmentMap = stream.analysedPcapFileToStreamMap(fileName);

        Assert.assertEquals("TLS Handshake", segmentMap.get(streamId).get(5).getTLSerror());
        Assert.assertEquals("TLS Change Cipher Spec", segmentMap.get(streamId).get(7).getTLSerror());
        Assert.assertEquals("TLS Alert", segmentMap.get(streamId).get(9).getTLSerror());
    }

    @Test
    public void canDissectECN() {
        String fileName = "src/main/resources/pcapFiles/tcp-ecn-sample.pcap";
        int streamId = 1;
        Display display = new Display();
        Stream stream = new Stream();
        HashMap<Integer, List<Segment>> segmentMap = stream.analysedPcapFileToStreamMap(fileName);
        Assert.assertTrue(segmentMap.get(streamId).get(0).isCWR());
        Assert.assertTrue(segmentMap.get(streamId).get(0).isECN());
        Assert.assertFalse(segmentMap.get(streamId).get(1).isCWR());
        Assert.assertTrue(segmentMap.get(streamId).get(1).isECN());
        Assert.assertFalse(segmentMap.get(streamId).get(2).isECN());

        HashMap<Integer, List<ViewLabel>> analysisFlagList = display.createAnalysisFlagList(segmentMap);
        Assert.assertEquals("SYN, ECE, CWR 179265614:179265615(1)", analysisFlagList.get(streamId).get(0).getMessageTopAbsolute());
        Assert.assertEquals("SYN, ACK, ECE 2798152218:2798152219(1)", analysisFlagList.get(streamId).get(1).getMessageTopAbsolute());
        Assert.assertEquals("CWR 13892:14428(536)", analysisFlagList.get(streamId).get(82).getMessageTopRelative());

    }
}