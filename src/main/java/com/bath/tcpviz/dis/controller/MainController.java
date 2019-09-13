package com.bath.tcpviz.dis.controller;

import com.bath.tcpviz.dis.domain.Display;
import com.bath.tcpviz.dis.domain.Stream;
import com.bath.tcpviz.dis.domain.ViewLabel;
import com.bath.tcpviz.dis.algorithms.Segment;
import com.bath.tcpviz.dis.fileupload.controller.ShareFileName;
import com.bath.tcpviz.dis.domain.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private ShareFileName shareFileName;

    private String viewFileName;

    @RequestMapping(value = "/{streamId}", method = RequestMethod.GET)
    public String streamIdDisplay(@PathVariable Integer streamId, Model model) {
        Display display = new Display();
        Stream stream = new Stream();
        String fileName = "src/main/resources/uploads/Example.pcap";
        if (shareFileName.getFileName() != null) {
            fileName = shareFileName.getFileName();
        }
        HashMap<Integer, List<Segment>> segmentMap = stream.analysedPcapFileToStreamMap(fileName);
        display.passFileName(fileName);
        HashMap<Integer, List<ViewLabel>> analysisFlagList = display.createAnalysisFlagList(segmentMap);
        model.addAttribute("map", analysisFlagList);
        List<Integer> keys = display.getKeys(analysisFlagList);
        model.addAttribute("listOfKeys", keys);
        List<ViewLabel> displayStreamIdPackets = analysisFlagList.get(streamId);
        model.addAttribute("numberOfStreams", segmentMap.size());
        model.addAttribute("streamId", streamId);
        model.addAttribute("packets", displayStreamIdPackets);
        model.addAttribute("sender", displayStreamIdPackets.get(0).getSender());
        model.addAttribute("receiver", displayStreamIdPackets.get(0).getReceiver());
        model.addAttribute("isTCP", displayStreamIdPackets.get(0).getProtocol().equals("TCP") && displayStreamIdPackets.size() > 1);
        viewFileName = displayStreamIdPackets.get(0).getFileName().substring(27);
        model.addAttribute("fileName", viewFileName);
        Statistics statistics = new Statistics();
        List<List<Object>> dataTableSender = statistics.generateDataTableChartSender(streamId, analysisFlagList);
        List<List<Object>> dataTableReceiver = statistics.generateDataTableChartReceiver(streamId, analysisFlagList);
        model.addAttribute("dataTableSender", dataTableSender);
        model.addAttribute("dataTableReceiver", dataTableReceiver);
        return "index";
    }

    @RequestMapping(value = "/example", method = RequestMethod.GET)
    public String mainIndex(Model model) {
        Display display = new Display();
        Stream stream = new Stream();
        String fileName = "src/main/resources/uploads/Example.pcap";
        shareFileName.setFileName(fileName);
        HashMap<Integer, List<Segment>> segmentMap = stream.analysedPcapFileToStreamMap(fileName);
        display.passFileName(fileName);
        HashMap<Integer, List<ViewLabel>> analysisFlagList = display.createAnalysisFlagList(segmentMap);
        model.addAttribute("map", analysisFlagList);
        Statistics statistics = new Statistics();
        List<List<Object>> dataTable = statistics.generateDataTable(analysisFlagList);
        List<ViewLabel> displayStreamIdPackets = analysisFlagList.get(1);
        model.addAttribute("sankeyHeight", 7500);
        model.addAttribute("dataTable", dataTable);
        viewFileName = displayStreamIdPackets.get(0).getFileName().substring(27);
        model.addAttribute("fileName", viewFileName);
        return "streamSankey";
    }

}
