package com.bath.tcpviz.dis.domain;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public
class Statistics {
    String link;

    public Statistics() {
    }

    Statistics(String sender, String receiver, int dataSent, int health, String link) {
        String webAddress = "http://localhost:8080/";
        String sender1 = sender;
        String receiver1 = receiver;
        int dataSent1 = dataSent;
        int health1 = health;
        this.link = webAddress + link;
    }

    Statistics(String senderHeader, String receiverHeader, String dataSentHeader, String healthHeader, String linkHeader) {
        String senderHeader1 = senderHeader;
        String receiverHeader1 = receiverHeader;
        String dataSentHeader1 = dataSentHeader;
        String healthHeader1 = healthHeader;
        String linkHeader1 = linkHeader;
    }

    public List<List<Object>> generateDataTable(HashMap<Integer, List<ViewLabel>> displayMap) {
        List<List<Object>> result = new ArrayList<>();
        List<Object> dataHeader = new ArrayList<>(Arrays.asList("Sender", "Receiver", "Packets in Stream", "Stream health", "Link"));
        result.add(dataHeader);
        for (int streamId = 1; streamId < displayMap.size() + 1; streamId++) {
            String sender = displayMap.get(streamId).get(0).getSender();
            String receiver = displayMap.get(streamId).get(0).getReceiver();
            int packetsInStream = Integer.parseInt(displayMap.get(streamId).get(0).getStatsPacketsInStream());
            int streamHealth = Integer.parseInt(displayMap.get(streamId).get(0).getStatsStreamHealth());
            String link = String.valueOf(streamId);
            List<Object> streamData = new ArrayList<>(Arrays.asList(sender, receiver, packetsInStream, streamHealth, link));
            result.add(streamData);
        }
        return result;
    }

    public List<List<Object>> generateDataTableChartSender(int streamId, HashMap<Integer, List<ViewLabel>> displayMap) {
        TimingCalculator tc = new TimingCalculator();
        List<List<Object>> result = new ArrayList<>();
        List<Object> dataHeader = new ArrayList<>(Arrays.asList("time", "seqNum"));
        result.add(dataHeader);
        for (int i = 0; i < displayMap.get(streamId).size(); i++) {
            if (displayMap.get(streamId).get(i).getDirection()) {
                long seqNum = displayMap.get(streamId).get(i).getStatsSeqNum();
                String time = displayMap.get(streamId).get(i).getStatsTime();
                double timeToLong = tc.convertTimeToLong(time);
                List<Object> streamData = new ArrayList<>(Arrays.asList(timeToLong, seqNum));
                result.add(streamData);
            }
        }
        return result;
    }

    public List<List<Object>> generateDataTableChartReceiver(int streamId, HashMap<Integer, List<ViewLabel>> displayMap) {
        TimingCalculator tc = new TimingCalculator();
        List<List<Object>> result = new ArrayList<>();
        List<Object> dataHeader = new ArrayList<>(Arrays.asList("time", "seqNum"));
        result.add(dataHeader);
        for (int i = 0; i < displayMap.get(streamId).size(); i++) {
            if (!displayMap.get(streamId).get(i).getDirection()) {
                long seqNum = displayMap.get(streamId).get(i).getStatsSeqNum();
                String time = displayMap.get(streamId).get(i).getStatsTime();
                double timeToLong = tc.convertTimeToLong(time);
                List<Object> streamData = new ArrayList<>(Arrays.asList(timeToLong, seqNum));
                result.add(streamData);
            }
        }
        return result;
    }
}