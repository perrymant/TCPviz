package com.bath.tcpviz.dis.domain;

import com.bath.tcpviz.dis.algorithms.Segment;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class StatisticsTest {

    private List<Statistics> createDataTable() {
        List<Statistics> statisticsList = new ArrayList<>();
        Statistics s1 = new Statistics("Sender", "Receiver", "Data sent", "Health", "Link");
        Statistics s2 = new Statistics("192.0.0.1:23", "200.200.0.23", 47, 100, "4");
        Statistics s3 = new Statistics("192.0.0.1:23", "101.100.0.55", 10, 50, "5");
        statisticsList.add(s1);
        statisticsList.add(s2);
        statisticsList.add(s3);
        return statisticsList;
    }

    @Test
    public void canReadLink() {
        List<Statistics> dataTable = createDataTable();
        // System.out.println(dataTable.get(1).link);
        Assert.assertEquals("http://localhost:8080/4", dataTable.get(1).link);
    }

    @Test
    public void canGenerateDataTableFromFile() {
        Display display = new Display();
        Stream stream = new Stream();
        String fileName = "src/main/resources/uploads/Example.pcap";
        HashMap<Integer, List<Segment>> segmentMap = stream.analysedPcapFileToStreamMap(fileName);
        HashMap<Integer, List<ViewLabel>> analysisFlagList = display.createAnalysisFlagList(segmentMap);

        Assert.assertEquals(4816, segmentMap.get(3).get(0).getTotalDataInStream());
        Assert.assertEquals(46, segmentMap.get(3).get(0).getTotalPacketsInStream());
        Assert.assertEquals(1, segmentMap.get(1).get(0).getTotalPacketsInStream());

        Statistics statistics = new Statistics();
        List<List<Object>> dataTable = statistics.generateDataTable(analysisFlagList);
        for (List<Object> objects : dataTable) {
            // System.out.println(Arrays.toString(objects.toArray()));
        }
        // System.out.println(dataTable.get(1).get(1));
    }

    @Test
    public void canGenerateData2TableFromFile() {
        Display display = new Display();
        Stream stream = new Stream();
        String fileName = "src/main/resources/uploads/Example.pcap";
        HashMap<Integer, List<Segment>> segmentMap = stream.analysedPcapFileToStreamMap(fileName);
        HashMap<Integer, List<ViewLabel>> analysisFlagList = display.createAnalysisFlagList(segmentMap);

        Assert.assertEquals(4816, segmentMap.get(3).get(0).getTotalDataInStream());
        Assert.assertEquals(46, segmentMap.get(3).get(0).getTotalPacketsInStream());
        Assert.assertEquals(1, segmentMap.get(1).get(0).getTotalPacketsInStream());

        Statistics statistics = new Statistics();
        int streamId = 4;
        List<List<Object>> dataTable = statistics.generateDataTableChartSender(streamId, analysisFlagList);
        for (List<Object> objects : dataTable) {
            System.out.println(Arrays.toString(objects.toArray()));
        }
        // System.out.println(dataTable.get(1).get(1));
    }
}