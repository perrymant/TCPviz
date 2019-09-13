package com.bath.tcpviz.dis.domain;

import com.bath.tcpviz.dis.algorithms.Segment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

    class Converter {

    private int totalNumberOfPackets;

    HashMap<Integer, List<Segment>> streamGenerator(List<Packet> allPacketsInTcpDump) {
        totalNumberOfPackets = allPacketsInTcpDump.size();
        Stream stream = new Stream();
        HashMap<Integer, List<Packet>> streamMap = stream.createStreamMap(allPacketsInTcpDump);
        HashMap<Integer, List<Segment>> allStreams = new HashMap<>();
        Converter converter = new Converter();
        for (int streamId = 1; streamId < streamMap.size() + 1; streamId++) {
            List<Segment> segments = converter.segmentGenerator(streamMap.get(streamId));
            allStreams.put(streamId, segments);
        }
        return allStreams;
    }

        List<Segment> segmentGenerator(List<Packet> packets) {
        List<Segment> convertedSegments = new ArrayList<>();

        for (int i = 0; i < packets.size(); i++) {
            Packet packet = packets.get(i);
            Segment segment = new Segment();
            segment.setStreamId(packet.getStreamId());
            segment.setPacketNumber(packet.getPacketNumber());
            segment.setRelativePacketNumber(i + 1);
            segment.setTs(packet.getTs());
                long phantomBitData = 1;
            if (packet.isSyn() || packet.isFin()) {
                segment.setSegDataSize(packet.getDataSize() + phantomBitData);
            } else {
                segment.setSegDataSize(packet.getDataSize());
            }
            segment.setAck(packet.isAck());
            segment.setSyn(packet.isSyn());
            segment.setRst(packet.isRst());
            segment.setFin(packet.isFin());
            segment.setPsh(packet.isPsh());
            segment.setUrg(packet.isUrg());
            segment.setCWR(packet.isCWR());
            segment.setECN(packet.isECN());
            // NS
            segment.setProtocol(packet.getProtocol());
            segment.setMSS(packet.getMss());
            segment.setTTL(packet.getTtl());
            segment.setHeader(String.valueOf(packet.getHeader()));
            segment.setPayload(String.valueOf(packet.getPayload()));
            segment.setAbsoluteTime(packet.getTimestamp());
            segment.setSeqNum(packet.getSeqNumber());
            if (packet.isSyn() || packet.isFin()) {
                segment.setNextExpSeqNum(packet.getSeqNumber() + packet.getDataSize() + phantomBitData);
            } else {
                segment.setNextExpSeqNum(packet.getSeqNumber() + packet.getDataSize());
            }
            segment.setWinSize(packet.getWindowSize());
            segment.setWindowScaling(packet.getWindowScaling());
            segment.setIpId(packet.getIpId());
            segment.setSrcIpAndPort(slashRemover(packet.getSender()));
            segment.setDstIpAndPort(slashRemover(packet.getReceiver()));
            segment.setPortSrc(packet.getSrcPortInt());
            segment.setPortDst(packet.getDstPortInt());
            segment.setTLSerror(packet.getTLSmessage());
            segment.setHTTPmessage(packet.getHTTPmessage());
            segment.setAckNum(packet.getAckNumber());
            segment.setArpMessage(packet.getArpMessage());
            segment.setArpMessageResolved(packet.getArpMessageResolved());
            segment.setIcmpMessage(packet.getICMPmessage());
            segment.setDnsQuery(packet.getDnsQuery());
            segment.setDnsResponse(packet.getDnsResponse());
            segment.setUDPLength(packet.getUDPLength());
            segment.setTotalPacketsInDump(totalNumberOfPackets);
            // First:
            if (packets.size() > 1 && i == 0) {
                Segment next = new Segment();
                next.setAckNum(packets.get(i + 1).getAckNumber());
                next.setSyn(packets.get(i + 1).isSyn());
                next.setAck(packets.get(i + 1).isAck());
                segment.setNext(next);

                Segment prev = new Segment();
                prev.setSyn(false);
                prev.setAck(false);
                prev.setFin(false);
                prev.setTs(segment.getTs());
                segment.setPrev(prev);
            } else if (packets.size() > 1 && (i < packets.size() - 1)) {
                Segment prev = new Segment();
                prev.setAckNum(packets.get(i - 1).getAckNumber());
                prev.setSyn(packets.get(i - 1).isSyn());
                prev.setAck(packets.get(i - 1).isAck());
                prev.setFin(packets.get(i - 1).isFin());
                prev.setTs(packets.get(i - 1).getTs());
                segment.setPrev(prev);

                Segment next = new Segment();
                next.setAckNum(packets.get(i + 1).getAckNumber());
                segment.setNext(next);
            } else if (packets.size() > 1 && i == packets.size() - 1) {
                // System.out.println("LAST");
                Segment prev = new Segment();
                prev.setAckNum(packets.get(i - 1).getAckNumber());
                prev.setSyn(packets.get(i - 1).isSyn());
                prev.setAck(packets.get(i - 1).isAck());
                prev.setFin(packets.get(i - 1).isFin());
                prev.setTs(packets.get(i - 1).getTs());
                // ...
                // other properties
                segment.setPrev(prev);
            }
            convertedSegments.add(segment);
        }
        return convertedSegments;
    }

    private String slashRemover(String input) {
        if (input != null && input.length() > 1 && input.startsWith("/")) {
            return input.substring(1);
        } else {
            return input;
        }
    }

}