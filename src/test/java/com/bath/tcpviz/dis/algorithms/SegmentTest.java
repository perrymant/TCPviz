package com.bath.tcpviz.dis.algorithms;

import com.bath.tcpviz.dis.domain.Packet;
import com.bath.tcpviz.dis.domain.Stream;
import com.bath.tcpviz.dis.domain.TcpDump;
import org.junit.Assert;
import org.junit.Test;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;

import java.util.HashMap;
import java.util.List;

public class SegmentTest {
    private TcpDump tcpDump = new TcpDump();
    private Stream stream = new Stream();


    // @Test
    // public void canConvertPacketsInStreamToSegments() {
    //     try {
    //         List<Packet> packetList = tcpDump.readPcapFile("src/main/resources/pcapFiles/ACK unseen seg test.pcap");
    //         HashMap<Integer, List<Packet>> streamMap = stream.createStreamMap(packetList);
    //         Assert.assertEquals(1, streamMap.size());
    //         Assert.assertEquals(5, streamMap.get(1).size());
    //         stream.makeSegments();
    //     } catch (PcapNativeException | NotOpenException e) {
    //         e.printStackTrace();
    //     }


    // }
}