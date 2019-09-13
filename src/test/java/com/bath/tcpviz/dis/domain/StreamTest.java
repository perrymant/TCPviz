package com.bath.tcpviz.dis.domain;

import com.bath.tcpviz.dis.algorithms.Segment;
import com.bath.tcpviz.dis.connection.Connection;
import org.junit.Assert;
import org.junit.Test;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;

import java.util.HashMap;
import java.util.List;

public class StreamTest {
    private Stream stream = new Stream();
    private TcpDump tcpDump = new TcpDump();

    @Test
    public void canCreateSeparateStreamLists() {
        try {
            String fileName = "src/main/resources/pcapFiles/tcp_stream_test.pcap";
            List<Packet> allPacketsInTcpDump = tcpDump.readPcapFile(fileName);
            HashMap<Integer, List<Packet>> streamMap = stream.createStreamMap(allPacketsInTcpDump);
            Assert.assertEquals(2, streamMap.size());
            Assert.assertEquals(2, streamMap.get(1).size());
            List<Packet> stream1 = streamMap.get(1);
            Assert.assertEquals(1, stream1.get(0).getStreamId());
            Assert.assertEquals(1, stream1.get(1).getStreamId());
            List<Packet> stream2 = streamMap.get(2);
            Assert.assertEquals(2, stream2.get(0).getStreamId());
            Assert.assertEquals(2, stream2.get(1).getStreamId());
        } catch (PcapNativeException | NotOpenException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void canCreateSeparateSegmentMapsIfAllPacketsAreTCP() {
        try {
            String fileName = "src/main/resources/pcapFiles/amazon just TCP.pcap";
            List<Packet> allPacketsInTcpDump = tcpDump.readPcapFile(fileName);
            HashMap<Integer, List<Packet>> streamMap = stream.createStreamMap(allPacketsInTcpDump);
            Assert.assertEquals(141, streamMap.size());
            Assert.assertEquals(46, streamMap.get(1).size());
            Assert.assertEquals(24, streamMap.get(2).size());
            Assert.assertEquals(2, streamMap.get(5).size());
            Assert.assertEquals(1, streamMap.get(6).size());

            HashMap<Integer, List<Segment>> allStreams = new HashMap<>();
            Converter converter = new Converter();
            for (int streamId = 1; streamId < streamMap.size() + 1; streamId++) {
                List<Segment> segments = converter.segmentGenerator(streamMap.get(streamId));
                allStreams.put(streamId, segments);
            }
            Assert.assertEquals(streamMap.size(), allStreams.size());
        } catch (PcapNativeException | NotOpenException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void canCreateSeparateSegmentMapsIfMultipleProtocolsInPCAP() {
        try {
            String fileName = "src/main/resources/uploads/Example.pcap";
            List<Packet> allPacketsInTcpDump = tcpDump.readPcapFile(fileName);
            HashMap<Integer, List<Packet>> streamMap = stream.createStreamMap(allPacketsInTcpDump);
            Assert.assertEquals(286, streamMap.size());

            HashMap<Integer, List<Segment>> allStreams = new HashMap<>();
            Converter converter = new Converter();
            for (int streamId = 1; streamId < streamMap.size() + 1; streamId++) {
                List<Segment> segments = converter.segmentGenerator(streamMap.get(streamId));
                allStreams.put(streamId, segments);
            }
            // // Could be used in StreamId Dropdown???
            // for (int i = 1; i < allStreams.size() + 1; i++) {
            //     System.out.println(i +
            //             ": " + allStreams.get(i).get(0).getProtocol() +
            //             " (" + allStreams.get(i).size() +
            //             ") " + allStreams.get(i).get(0).getSrcIpAndPort() +
            //             " -> " + allStreams.get(i).get(0).getDstIpAndPort() +
            //             "");
            // }

            // // or just TCP only? have a preference... Also add StreamHealth!
            // for (int i = 1; i < allStreams.size() + 1; i++) {
            //     if (allStreams.get(i).get(0).getProtocol().equals("TCP")) {
            //         System.out.println(i +
            //                 ": " + allStreams.get(i).get(0).getProtocol() +
            //                 " (" + allStreams.get(i).size() +
            //                 ") " + allStreams.get(i).get(0).getSrcIpAndPort() +
            //                 " -> " + allStreams.get(i).get(0).getDstIpAndPort() +
            //                 "");
            //     }
            // }
        } catch (PcapNativeException | NotOpenException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void canConvertTcpDumpToMapOfSegments() {
        try {
            String fileName = "src/main/resources/pcapFiles/tcp_stream_test.pcap";
            List<Packet> allPacketsInTcpDump = tcpDump.readPcapFile(fileName);
            HashMap<Integer, List<Packet>> streamId_Packet_Map = stream.createStreamMap(allPacketsInTcpDump);
            //
            HashMap<Integer, List<Segment>> segmentMap = stream.createMapOfSegments(streamId_Packet_Map);
            // System.out.println(segmentMap.get(1).get(0).getRelativePacketNumber());

        } catch (PcapNativeException | NotOpenException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void canDealWithIGMPandProtocolsThatDontUsePorts() {
        try {
            String fileName = "src/main/resources/uploads/IGMP.pcap";
            List<Packet> allPacketsInTcpDump = tcpDump.readPcapFile(fileName);
            System.out.println(allPacketsInTcpDump.size());
            Packet packet = allPacketsInTcpDump.get(0);
            System.out.println(packet.getSender());
            System.out.println(packet.getReceiver());
            Connection connection = new Connection(packet.getSender(), packet.getReceiver());

            HashMap<Integer, List<Packet>> streamId_Packet_Map = stream.createStreamMap(allPacketsInTcpDump);
            //
            // HashMap<Integer, List<Segment>> segmentMap = stream.createMapOfSegments(streamId_Packet_Map);
            // System.out.println(segmentMap.get(1).get(0).getRelativePacketNumber());

        } catch (PcapNativeException | NotOpenException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void canDealWithBreakingPCAPS() {
        try {
            String fileName = "src/main/resources/pcapFiles/pcapbreak/traceroute_MPLS.pcap";
            List<Packet> allPacketsInTcpDump = tcpDump.readPcapFile(fileName);
            System.out.println(allPacketsInTcpDump.size());
            Packet packet = allPacketsInTcpDump.get(0);
            System.out.println(packet.getSender());
            System.out.println(packet.getReceiver());
            Connection connection = new Connection(packet.getSender(), packet.getReceiver());

            HashMap<Integer, List<Packet>> streamId_Packet_Map = stream.createStreamMap(allPacketsInTcpDump);
            //
            System.out.println(streamId_Packet_Map.size());
            HashMap<Integer, List<Segment>> segmentMap = stream.createMapOfSegments(streamId_Packet_Map);
            System.out.println(segmentMap.size());
            // System.out.println(segmentMap.get(1).get(1).getRelativePacketNumber());

        } catch (PcapNativeException | NotOpenException e) {
            e.printStackTrace();
        }
    }
}