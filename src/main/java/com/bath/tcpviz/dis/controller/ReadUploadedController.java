package com.bath.tcpviz.dis.controller;

import com.bath.tcpviz.dis.domain.Display;
import com.bath.tcpviz.dis.domain.Stream;
import com.bath.tcpviz.dis.domain.ViewLabel;
import com.bath.tcpviz.dis.algorithms.Segment;
import com.bath.tcpviz.dis.fileupload.controller.ShareFileName;
import com.bath.tcpviz.dis.fileupload.service.FileStorageService;
import com.bath.tcpviz.dis.domain.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;

@Controller
public class ReadUploadedController {

    @Autowired
    private ShareFileName shareFileName;

    @Autowired
    private FileStorageService fileStorageService;

    private String viewFileName;

    @RequestMapping(value = "/readUploaded", method = RequestMethod.GET)
    public String afterUploading(Model model) {
        Display display = new Display();
        Stream stream = new Stream();
        String fileName = "src/main/resources/pcapFiles/Example.pcap";
        if (shareFileName.getFileName() != null) {
            fileName = shareFileName.getFileName();
        }
        HashMap<Integer, List<Segment>> segmentMap = stream.analysedPcapFileToStreamMap(fileName);
        display.passFileName(fileName);
        HashMap<Integer, List<ViewLabel>> analysisFlagList = display.createAnalysisFlagList(segmentMap);
        if (analysisFlagList.size() == 1) {
            return "redirect:/1";
        }
        model.addAttribute("map", analysisFlagList);
        List<Integer> keys = display.getKeys(analysisFlagList);
        model.addAttribute("listOfKeys", keys);
        Statistics statistics = new Statistics();
        int numberOfStreams = segmentMap.size();
        int totalNumberOfPackets = stream.totalNumberOfPackets;
        double sankeyHeight = 775;
        if (numberOfStreams > 25) {
            sankeyHeight = (int) ((double) totalNumberOfPackets / numberOfStreams) * 775;
        }
        model.addAttribute("sankeyHeight", sankeyHeight);
        List<List<Object>> dataTable = statistics.generateDataTable(analysisFlagList);
        model.addAttribute("dataTable", dataTable);
        List<ViewLabel> displayStreamIdPackets = analysisFlagList.get(1);
        viewFileName = displayStreamIdPackets.get(0).getFileName().substring(27);
        model.addAttribute("fileName", viewFileName);
        return "streamSankey";
    }
}
