package com.bath.tcpviz.dis.domain;

import com.bath.tcpviz.dis.algorithms.Segment;
import com.bath.tcpviz.dis.connection.Connection;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Stream {
    private TcpDump tcpDump = new TcpDump();
    private List<Packet> packets = new ArrayList<>();
    public int totalNumberOfPackets;

    HashMap<Integer, List<Packet>> createStreamMap(List<Packet> packetList) {
        HashMap<Integer, Connection> connectionMap = new HashMap<>();
        HashMap<Integer, List<Packet>> streamMap = new HashMap<>();
        int streamId = 1;
        for (Packet packet : packetList) {
            Connection connection = new Connection(packet.getSender(), packet.getReceiver());
            if (connectionMap.size() == 0) {
                connectionMap.put(streamId, connection);
                packet.setStreamId(streamId);
                List<Packet> packets = new ArrayList<>();
                packets.add(packet);
                streamMap.put(streamId, packets);
            } else {
                if (connectionMap.containsValue(connection)) {
                    int id = getKey(connectionMap, connection);
                    packet.setStreamId(id);
                    List<Packet> packets = streamMap.get(id);
                    packets.add(packet);
                    streamMap.put(id, packets);
                } else {
                    streamId++;
                    connectionMap.put(streamId, connection);
                    packet.setStreamId(streamId);
                    List<Packet> packets = new ArrayList<>();
                    packets.add(packet);
                    streamMap.put(streamId, packets);
                }
            }
        }
        return streamMap;
    }

    private int getKey(HashMap<Integer, Connection> hashMap, Connection valueToSearch) {
        final int[] result = {0};
        hashMap.forEach((key, value) -> {
            if (value.equals(valueToSearch)) {
                result[0] = key;
            }
        });
        return result[0];
    }

    HashMap<Integer, List<Segment>> createMapOfSegments(HashMap<Integer, List<Packet>> streamMap) {
        HashMap<Integer, List<Segment>> segmentsMap = new HashMap<>();
        Converter converter = new Converter();
        List<Segment> convertedSegments = converter.segmentGenerator(streamMap.get(1));
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        algorithmConverter.calculateClientAndServer(convertedSegments);
        return segmentsMap;
    }

    public HashMap<Integer, List<Segment>> analysedPcapFileToStreamMap(String fileName) {
        Converter converter = new Converter();
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        readPcap(fileName);
        HashMap<Integer, List<Segment>> streamGeneratorMap = converter.streamGenerator(packets);
        HashMap<Integer, List<Segment>> analysedStreams = new HashMap<>();
        for (int streamId = 1; streamId < streamGeneratorMap.size() + 1; streamId++) {
            List<Segment> segments = algorithmConverter.applyAllAnalysisAlgorithms(streamGeneratorMap.get(streamId));
            analysedStreams.put(streamId, segments);
        }
        return analysedStreams;
    }

    private void readPcap(String fileName) {
        try {
            packets = tcpDump.readPcapFile(fileName);
            totalNumberOfPackets = packets.size();
        } catch (PcapNativeException | NotOpenException e) {
            e.printStackTrace();
        }
    }

}