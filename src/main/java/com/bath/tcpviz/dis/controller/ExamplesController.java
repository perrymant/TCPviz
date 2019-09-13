package com.bath.tcpviz.dis.controller;

import com.bath.tcpviz.dis.algorithms.Segment;
import com.bath.tcpviz.dis.domain.Display;
import com.bath.tcpviz.dis.domain.Statistics;
import com.bath.tcpviz.dis.domain.Stream;
import com.bath.tcpviz.dis.domain.ViewLabel;
import com.bath.tcpviz.dis.fileupload.controller.ShareFileName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;

@Controller
public class ExamplesController {

    @Autowired
    private ShareFileName shareFileName;

    @RequestMapping(value = "/syn1", method = RequestMethod.GET)
    public String syn1(Model model) {
        String fileName = "src/main/resources/example/Establish Connection.pcap";
        int streamId = 1;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/syn2", method = RequestMethod.GET)
    public String syn2(Model model) {
        String fileName = "src/main/resources/example/Establish Connection.pcap";
        int streamId = 1;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/syn3", method = RequestMethod.GET)
    public String syn3(Model model) {
        String fileName = "src/main/resources/example/Establish Connection.pcap";
        int streamId = 1;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/connectionTermination", method = RequestMethod.GET)
    public String fin(Model model) {
        String fileName = "src/main/resources/example/Terminate Connection.pcap";
        int streamId = 1;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/dupAck", method = RequestMethod.GET)
    public String dupAck(Model model) {
        String fileName = "src/main/resources/example/DupAck.pcap";
        int streamId = 1;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/windowUpdate", method = RequestMethod.GET)
    public String windowUpdate(Model model) {
        String fileName = "src/main/resources/example/Window Update.pcap";
        int streamId = 1;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/windowFull", method = RequestMethod.GET)
    public String windowFull(Model model) {
        String fileName = "src/main/resources/example/Window Full.pcap";
        int streamId = 1;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/zeroWindow", method = RequestMethod.GET)
    public String zeroWindow(Model model) {
        String fileName = "src/main/resources/example/Zero Window.pcap";
        int streamId = 1;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/zeroWindowProbe", method = RequestMethod.GET)
    public String zeroWindowProbe(Model model) {
        String fileName = "src/main/resources/example/Zero Window Probe.pcap";
        int streamId = 1;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/zeroWindowProbeAck", method = RequestMethod.GET)
    public String zeroWindowProbeAck(Model model) {
        String fileName = "src/main/resources/example/Zero Window Probe Ack.pcap";
        int streamId = 1;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/keepAlive", method = RequestMethod.GET)
    public String keepAlive(Model model) {
        String fileName = "src/main/resources/example/Keep Alive.pcap";
        int streamId = 1;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/keepAliveAck", method = RequestMethod.GET)
    public String keepAliveAck(Model model) {
        String fileName = "src/main/resources/example/Keep Alive Ack.pcap";
        int streamId = 1;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/spuriousRetransmission", method = RequestMethod.GET)
    public String spuriousRetransmission(Model model) {
        String fileName = "src/main/resources/example/Spurious Retransmission.pcap";
        int streamId = 1;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/fastRetransmission", method = RequestMethod.GET)
    public String fastRetransmission(Model model) {
        String fileName = "src/main/resources/example/Fast Retransmission.pcap";
        int streamId = 1;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/retransmission", method = RequestMethod.GET)
    public String retransmission(Model model) {
        String fileName = "src/main/resources/example/Retransmission.pcap";
        int streamId = 1;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/outOfOrder", method = RequestMethod.GET)
    public String outOfOrder(Model model) {
        String fileName = "src/main/resources/example/OutOfOrder.pcap";
        int streamId = 1;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/previousSegmentNotCaptured", method = RequestMethod.GET)
    public String previousSegmentNotCaptured(Model model) {
        String fileName = "src/main/resources/example/Previous Segment Not Captured.pcap";
        int streamId = 1;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/ackUnseenSegment", method = RequestMethod.GET)
    public String ackUnseenSegment(Model model) {
        String fileName = "src/main/resources/example/ACKed Unseen Segment.pcap";
        int streamId = 1;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/goodAck", method = RequestMethod.GET)
    public String goodAck(Model model) {
        String fileName = "src/main/resources/example/Example.pcap";
        int streamId = 3;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/badAck", method = RequestMethod.GET)
    public String badAck(Model model) {
        String fileName = "src/main/resources/example/Example.pcap";
        int streamId = 100;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/appData", method = RequestMethod.GET)
    public String appData(Model model) {
        String fileName = "src/main/resources/example/Example.pcap";
        int streamId = 5;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/rst", method = RequestMethod.GET)
    public String rst(Model model) {
        String fileName = "src/main/resources/example/Home.pcap";
        int streamId = 4;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/good", method = RequestMethod.GET)
    public String good(Model model) {
        String fileName = "src/main/resources/example/GoodBehaviour.pcap";
        int streamId = 2;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/slowStart", method = RequestMethod.GET)
    public String slowStart(Model model) {
        String fileName = "src/main/resources/example/SlowStart.pcap";
        int streamId = 1;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/ackOoo", method = RequestMethod.GET)
    public String ackOoo(Model model) {
        String fileName = "src/main/resources/example/SlowStart.pcap";
        int streamId = 1;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/reusedPorts", method = RequestMethod.GET)
    public String reused(Model model) {
        String fileName = "src/main/resources/example/ReusedPorts.pcap";
        int streamId = 1;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/ManyDupacks", method = RequestMethod.GET)
    public String ManyDupacks(Model model) {
        String fileName = "src/main/resources/example/ManyDupacks.pcap";
        int streamId = 1;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/FINandRST", method = RequestMethod.GET)
    public String FINandRST(Model model) {
        String fileName = "src/main/resources/example/FINandRST.pcap";
        int streamId = 1;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/SegmentsAfterRST", method = RequestMethod.GET)
    public String SegmentsAfterRST(Model model) {
        String fileName = "src/main/resources/example/SegmentsAfterRST.pcap";
        int streamId = 1;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/FRisOOO", method = RequestMethod.GET)
    public String FRisOOO(Model model) {
        String fileName = "src/main/resources/example/FRisOOO.pcap";
        int streamId = 1;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/TLSencryptedAlert1", method = RequestMethod.GET)
    public String TLSencryptedAlert1(Model model) {
        String fileName = "src/main/resources/example/TLSencryptedAlert1.pcap";
        int streamId = 1;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/tlsError", method = RequestMethod.GET)
    public String tlsError(Model model) {
        String fileName = "src/main/resources/example/TLSencryptedAlert1.pcap";
        int streamId = 1;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/httpMessage", method = RequestMethod.GET)
    public String httpMessage(Model model) {
        String fileName = "src/main/resources/example/http.pcap";
        int streamId = 1;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/TLSencryptedAlert2", method = RequestMethod.GET)
    public String TLSencryptedAlert2(Model model) {
        String fileName = "src/main/resources/example/TLSencryptedAlert2.pcap";
        int streamId = 1;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/URGACKPSHflood", method = RequestMethod.GET)
    public String URGACKPSHflood(Model model) {
        String fileName = "src/main/resources/example/URG-ACK-PSHflood.pcap";
        shareFileName.setFileName(fileName);
        return "redirect:/readUploaded";
    }

    @RequestMapping(value = "/LargerExample", method = RequestMethod.GET)
    public String LargerExample(Model model) {
        String fileName = "src/main/resources/example/LargerExample.pcap";
        int streamId = 1;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    @RequestMapping(value = "/ecn", method = RequestMethod.GET)
    public String ecn(Model model) {
        String fileName = "src/main/resources/pcapFiles/tcp-ecn-sample.pcap";
        int streamId = 1;
        modelSetup(model, fileName, streamId);
        return "index";
    }

    private void modelSetup(Model model, String fileName, int streamId) {
        shareFileName.setFileName(fileName);
        Display display = new Display();
        Statistics statistics = new Statistics();
        Stream stream = new Stream();
        display.passFileName(fileName);
        HashMap<Integer, List<Segment>> segmentMap = stream.analysedPcapFileToStreamMap(fileName);
        HashMap<Integer, List<ViewLabel>> analysisFlagList = display.createAnalysisFlagList(segmentMap);
        List<Integer> keys = display.getKeys(analysisFlagList);
        List<List<Object>> dataTableSender = statistics.generateDataTableChartSender(streamId, analysisFlagList);
        List<List<Object>> dataTableReceiver = statistics.generateDataTableChartReceiver(streamId, analysisFlagList);
        List<ViewLabel> displayStreamIdPackets = analysisFlagList.get(streamId);
        String viewFileName = displayStreamIdPackets.get(0).getFileName().substring(27);
        model.addAttribute("map", analysisFlagList);
        model.addAttribute("listOfKeys", keys);
        model.addAttribute("numberOfStreams", segmentMap.size());
        model.addAttribute("streamId", streamId);
        model.addAttribute("packets", displayStreamIdPackets);
        model.addAttribute("sender", displayStreamIdPackets.get(0).getSender());
        model.addAttribute("receiver", displayStreamIdPackets.get(0).getReceiver());
        model.addAttribute("isTCP", displayStreamIdPackets.get(0).getProtocol().equals("TCP") && displayStreamIdPackets.size() > 1);
        model.addAttribute("fileName", viewFileName);
        model.addAttribute("dataTableSender", dataTableSender);
        model.addAttribute("dataTableReceiver", dataTableReceiver);
    }
}