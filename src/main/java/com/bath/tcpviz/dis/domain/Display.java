package com.bath.tcpviz.dis.domain;

import com.bath.tcpviz.dis.algorithms.Segment;

import java.util.*;

public class Display {
    public String sender = "";
    public String receiver = "";
    private String fileName;
    private String firstSender = "";

    public HashMap<Integer, List<ViewLabel>> createAnalysisFlagList(HashMap<Integer, List<Segment>> segmentMap) {
        HashMap<Integer, List<ViewLabel>> allViewLabels = new HashMap<>();
        for (Map.Entry<Integer, List<Segment>> me : segmentMap.entrySet()) {
            int streamId = me.getKey();
            List<ViewLabel> viewLabels = new ArrayList<>();
            List<Segment> segments = me.getValue();
            firstSender = segments.get(0).getSrcIpAndPort();
            for (Segment segment : segments) {
                ViewLabel viewLabel = new ViewLabel();
                addTimingMessages(segment, viewLabel);
                addArrowMessages(segment, viewLabel);
                addDeviceHeaders(segment, viewLabel);
                addStatistics(segment, viewLabel);
                switch (segment.getProtocol()) {
                    case "TCP":
                        addAnalysisMessages(segment, viewLabel);
                        break;
                    case "UDP":
                        viewLabel.setMessageTopRelative("UDP: Packet Length " + segment.getUDPLength() + " Bytes");
                        viewLabel.setMessageTopAbsolute("UDP: Packet Length " + segment.getUDPLength() + " Bytes");
                        break;
                    case "ARP":
                        viewLabel.setMessageTopRelative("ARP: " + segment.getArpMessage());
                        viewLabel.setMessageTopAbsolute("ARP: " + segment.getArpMessage());
                        break;
                    case "DNS":
                        viewLabel.setMessageTopRelative("DNS: " + segment.getDnsQuery());
                        viewLabel.setMessageTopAbsolute("DNS: " + segment.getDnsQuery());
                        // viewLabel.setMessageBottom("DNS: " + segment.getDnsResponse());// response still has an issue...
                        break;
                    case "ICMP":
                        viewLabel.setMessageTopRelative("ICMP: " + segment.getIcmpMessage());
                        viewLabel.setMessageTopAbsolute("ICMP: " + segment.getIcmpMessage());
                        break;
                    case "ICMPv6":
                        viewLabel.setMessageTopRelative("ICMPv6: " + segment.getIcmpMessage());
                        viewLabel.setMessageTopAbsolute("ICMPv6: " + segment.getIcmpMessage());
                        break;
                }
                viewLabels.add(viewLabel);
            }
            allViewLabels.put(streamId, viewLabels);
        }
        return allViewLabels;
    }

    private void addStatistics(Segment segment, ViewLabel viewLabel) {
        viewLabel.setStatsPacketsInStream(String.valueOf(segment.getTotalPacketsInStream()));
        viewLabel.setStatsStreamHealth(String.valueOf(segment.getHealth()));
        viewLabel.setStatsSeqNum(segment.getRelativeSeqNum());
        viewLabel.setStatsTime(segment.getRelativeTime());
    }

    private void addDeviceHeaders(Segment segment, ViewLabel viewLabel) {
        viewLabel.setFileName(fileName);
        viewLabel.setSender(sender);
        viewLabel.setReceiver(receiver);
    }

    private void addTimingMessages(Segment segment, ViewLabel viewLabel) {
        viewLabel.setStream(segment.getStreamId());
        viewLabel.setProtocol(segment.getProtocol());
        viewLabel.setRelativePacketNumber(segment.getRelativePacketNumber());
        viewLabel.setAbsolutePacketNumber(segment.getPacketNumber());
        viewLabel.setAbsoluteTime(segment.getAbsoluteTime());
        viewLabel.setRelativeTime(segment.getRelativeTime());
        viewLabel.setDeltaTime(segment.getDeltaFormatted());
    }

    private void addArrowMessages(Segment segment, ViewLabel viewLabel) {
        if (segment.getProtocol().equals("TCP") || segment.getProtocol().equals("UDP")) {
            if ((segment.getRelativePacketNumber() == 1) && segment.getPortSrc() >= 1024) {
                sender = segment.getSrcIpAndPort();
                receiver = segment.getDstIpAndPort();
            } else {
                sender = segment.getDstIpAndPort();
                receiver = segment.getSrcIpAndPort();
            }
            if (segment.getPortSrc() >= 1024) {
                viewLabel.setDirection(true);
            } else {
                viewLabel.setDirection(false);
            }
            if (segment.getPortSrc() >= 1024 && segment.getPortDst() >= 1024) {
                if (segment.getSrcIpAndPort().equals(firstSender)) {
                    viewLabel.setDirection(true);
                } else {
                    viewLabel.setDirection(false);
                }
            }
        } else {
            if (segment.getRelativePacketNumber() == 1) {
                sender = segment.getSrcIpAndPort();
                receiver = segment.getDstIpAndPort();
            } else {
                sender = segment.getDstIpAndPort();
                receiver = segment.getSrcIpAndPort();
            }
            if (segment.getSrcIpAndPort().equals(sender)) {
                viewLabel.setDirection(true);
            } else {
                viewLabel.setDirection(false);
            }

        }
        //if TCP:
        if (segment.getProtocol().equals("TCP")) {
            // controlbits
            viewLabel.setPSH(segment.isPsh());
            String controlFlags = (controlFlagsToString(segment));
            // absolute absoluteSeqStart:absoluteSeqEnd (data)
            String absoluteSeqStart = String.valueOf(segment.getSeqNum());
            String absoluteSeqEnd = String.valueOf((segment.getSeqNum() + segment.getSegDataSize()));
            String data = String.valueOf(segment.getSegDataSize());
            String seqAndDataAbsolute = (absoluteSeqStart + ":" + absoluteSeqEnd + "(" + data + ")");
            // absolute window: win window * windowscale
            if (segment.isSyn()) {
                viewLabel.setSYN(segment.isSyn());
                viewLabel.setRelativeWindowSize("win " + segment.getWinSize() + "*" + segment.getWindowScaling());
                viewLabel.setMSS("<mss " + segment.getMSS() + ">");
            } else {
                viewLabel.setRelativeWindowSize("win " + segment.getWinSize());
            }

            // relative absoluteSeqStart:absoluteSeqEnd (data)
            String relativeSeqStart = String.valueOf(segment.getRelativeSeqNum());
            String relativeSeqEnd = String.valueOf((segment.getRelativeSeqNum() + segment.getSegDataSize()));
            String seqAndDataRelative = (relativeSeqStart + ":" + relativeSeqEnd + "(" + data + ")");
            if (segment.getSegDataSize() == 0 && segment.isRst()) {
                viewLabel.setMessageTopRelative(controlFlags + " seq " + segment.getRelativeSeqNum() + ", ack 0");//mesasgeTopRelative
                viewLabel.setMessageTopAbsolute(controlFlags + " seq " + segment.getSeqNum() + ", ack 0");//
                viewLabel.setNotSynMessageRelative(" win " + segment.getCalculatedWindowSize());//messageBottomRelative
                viewLabel.setNotSynMessageAbsolute(" win " + segment.getWinSize() + " * " + segment.getWindowScaling());//
            } else if (segment.getSegDataSize() == 0) {
                viewLabel.setMessageTopRelative("seq " + segment.getRelativeSeqNum() + ", ack " + segment.getRelativeAckNum());//mesasgeTopRelative
                viewLabel.setMessageTopAbsolute("seq " + segment.getSeqNum() + ", ack " + segment.getAckNum());//
                viewLabel.setNotSynMessageRelative(" win " + segment.getCalculatedWindowSize());//messageBottomRelative
                viewLabel.setNotSynMessageAbsolute(" win " + segment.getWinSize() + " * " + segment.getWindowScaling());//messageBottomRelative
                if (segment.getWindowScaling() > 1) {
                    viewLabel.setNotSynMessageAbsolute(" win " + segment.getWinSize() + " * " + segment.getWindowScaling());//messageBottomAbsolute
                } else {
                    viewLabel.setNotSynMessageAbsolute(" win " + segment.getWinSize());//
                }
            } else {
                viewLabel.setMessageTopRelative(controlFlags + " " + seqAndDataRelative);
                viewLabel.setMessageTopAbsolute(controlFlags + " " + seqAndDataAbsolute);
            }
            if (segment.getSegDataSize() == 0 && (segment.isUrg() || segment.isPsh())) {
                viewLabel.setMessageTopRelative(controlFlags + " seq " + segment.getRelativeSeqNum() + ", ack " + segment.getRelativeAckNum());//mesasgeTopRelative
                viewLabel.setMessageTopAbsolute(controlFlags + " seq " + segment.getSeqNum() + ", ack " + segment.getAckNum());//
                viewLabel.setNotSynMessageRelative(" win " + segment.getCalculatedWindowSize());//messageBottomRelative
                viewLabel.setNotSynMessageAbsolute(" win " + segment.getWinSize() + " * " + segment.getWindowScaling());//messageBottomRelative
            }

            if (segment.isSyn()) {
                viewLabel.setCalculatedWindowSize("win " + segment.getCalculatedWindowSize());
                viewLabel.setMSS("<mss " + segment.getMSS() + ">");
                viewLabel.setSynMessageRelative("win " + segment.getCalculatedWindowSize() + " <mss " + segment.getMSS() + ">");
                if (segment.getWindowScaling() > 1) {
                    viewLabel.setSynMessageAbsolute(" win " + segment.getWinSize() + " * " + segment.getWindowScaling() + " <mss " + segment.getMSS() + ">");
                } else {
                    viewLabel.setSynMessageAbsolute(" win " + segment.getWinSize() + " <mss " + segment.getMSS() + ">");
                }
            } else {
                viewLabel.setCalculatedWindowSize("win " + segment.getCalculatedWindowSize());
                if (segment.getSegDataSize() != 0) {
                    viewLabel.setNotSynMessageRelative("ack " + segment.getRelativeAckNum() + ", win " + segment.getCalculatedWindowSize());
                    if (segment.getWindowScaling() > 1) {
                        viewLabel.setNotSynMessageAbsolute("ack " + segment.getAckNum() + ", win " + segment.getWinSize() + " * " + segment.getWindowScaling());
                    } else {
                        viewLabel.setNotSynMessageAbsolute("ack " + segment.getAckNum() + ", win " + segment.getWinSize());
                    }
                } else {
                    viewLabel.setNotSynMessageRelative("win " + segment.getCalculatedWindowSize());
                    if (segment.getWindowScaling() > 1) {
                        viewLabel.setNotSynMessageAbsolute("win " + segment.getWinSize() + " * " + segment.getWindowScaling());
                    } else {
                        viewLabel.setNotSynMessageAbsolute("win " + segment.getWinSize());
                    }
                }
            }
            viewLabel.setSegmentLength(segment.getSegDataSize());
            viewLabel.setAbsoluteSeqNum(segment.getSeqNum());
            viewLabel.setRelativeSeqNum(segment.getRelativeSeqNum());

            viewLabel.setAbsoluteAckNum(segment.getAckNum());
            viewLabel.setRelativeAckNum(segment.getRelativeAckNum());
        } else {
            //if other protocol
        }
    }

    private void addAnalysisMessages(Segment segment, ViewLabel viewLabel) {
        if (segment.getProtocol().equals("TCP")) {
            if (segment.getSegDataSize() >= 1 && !segment.isFin() && !segment.isSyn() && !segment.isRst() && segment.getRelativePacketNumber() != 1 && segment.getBothSidesSeen()) {
                viewLabel.setAnalysisMessage("TCP Application Data: " + segment.getBytesInFlight() + " bytes in flight");
                viewLabel.setAnalysisCSS("appData");
            } else if (segment.getSegDataSize() >= 1 && !segment.isFin() && !segment.isSyn() && !segment.isRst() && !segment.getBothSidesSeen()) {
                viewLabel.setAnalysisMessage("TCP Application Data");
                viewLabel.setAnalysisCSS("appData");
            } else if (segment.getSegDataSize() == 0 && segment.isAck() && (segment.getAckNum() == segment.getLastRevNextExpectedSequenceNumber())) {
                viewLabel.setAnalysisMessage("✓ All segments ACKed");
                viewLabel.setAnalysisCSS("goodAck");
            } else if (segment.getSegDataSize() == 0 && segment.isAck() && segment.getBytesInFlight() == 0 && (segment.getMaxRevSeqAndData() - segment.getAckNum() == 0)) {
                viewLabel.setAnalysisMessage("✓ ACKing an earlier occurring segment");
                viewLabel.setAnalysisCSS("goodAck");
            } else if (segment.getSegDataSize() == 0
                    && segment.isAck()
                    && segment.getBytesInFlight() == 0
                    && segment.getAckNum() < segment.getLastSeenAckNum()) {
                viewLabel.setAnalysisMessage((segment.getMaxRevSeqAndData() - segment.getAckNum()) + " bytes outstanding - This ACK is out of order!");
                viewLabel.setAnalysisCSS("ackOoo");
            } else if (segment.getSegDataSize() == 0 && segment.isAck() && segment.getBytesInFlight() == 0 && (segment.getMaxRevSeqAndData() - segment.getAckNum() > 0)) {
                viewLabel.setAnalysisMessage("✓ " + (segment.getMaxRevSeqAndData() - segment.getAckNum()) + " bytes outstanding");
                viewLabel.setAnalysisCSS("goodAck");
            } else if (segment.getSegDataSize() == 0 && segment.isAck() && segment.getBytesInFlight() != 0 && !segment.getFirstSegmentForDevice() && segment.getLastRevAckNum() != 0) {
                viewLabel.setAnalysisMessage("Unacknowledged data remaining: " + (segment.getSeqNum() - segment.getLastRevAckNum()) + " bytes unACKed");
                viewLabel.setAnalysisCSS("badAck");
            } else if (segment.getSegDataSize() == 0 && segment.isAck() && segment.getBytesInFlight() != 0 && !segment.getFirstSegmentForDevice() && segment.getLastRevAckNum() == 0) {
                viewLabel.setAnalysisMessage("Illegal Acknowledgement!");
                viewLabel.setAnalysisCSS("badAck");
            } else if (segment.getSegDataSize() == 0 && segment.isAck() && segment.getBytesInFlight() != 0 && segment.getFirstSegmentForDevice()) {
                viewLabel.setAnalysisMessage("Packet capture did not capture Connection Establishment");
                viewLabel.setAnalysisCSS("badAck");
            }
            if (segment.getTLSerror() != null) {
                viewLabel.setAnalysisMessage2(segment.getTLSerror());
                viewLabel.setAnalysisCSS2("tlsError");
            }
            if (segment.getHTTPmessage() != null) {
                viewLabel.setAnalysisMessage2(segment.getHTTPmessage());
                viewLabel.setAnalysisCSS2("httpMessage");
            }
            if (segment.getNumberOfAnalysisFlags() == 1) {
                String analysisFlag1 = segment.getAnalysisFlagMap().get(1).get(0);
                if (segment.getAnalysisFlagMap().get(2) != null) {
                    analysisFlag1 = segment.getAnalysisFlagMap().get(2).get(0);
                }
                analysisFlag1Switch(segment, viewLabel, analysisFlag1);
            }
            if (segment.getNumberOfAnalysisFlags() == 2) {
                String analysisFlag1 = segment.getAnalysisFlagMap().get(2).get(0);
                String analysisFlag2 = segment.getAnalysisFlagMap().get(2).get(1);
                analysisFlag1Switch(segment, viewLabel, analysisFlag1);
                analysisFlagSwitch2(segment, viewLabel, analysisFlag2);
            }
        } else {
            viewLabel.setAnalysisMessage(segment.getProtocol());
        }

    }

    private void analysisFlagSwitch2(Segment segment, ViewLabel viewLabel, String analysisFlag2) {
        switch (analysisFlag2) {
            case "syn1":
                viewLabel.setAnalysisMessage2("TCP Connection establishment: active open");
                viewLabel.setAnalysisCSS2("syn1");
                break;
            case "syn2":
                viewLabel.setAnalysisMessage2("TCP Connection establishment: passive open");
                viewLabel.setAnalysisCSS2("syn2");
                break;
            case "syn3":
                viewLabel.setAnalysisMessage2("TCP Connection establishment: three-way handshake complete");
                viewLabel.setAnalysisCSS2("syn3");
                break;
            case "reused":
                viewLabel.setAnalysisMessage2("TCP Port Numbers Reused");
                viewLabel.setAnalysisCSS2("reused");
                break;
            case "rst":
                viewLabel.setAnalysisMessage2("TCP Connection Reset!");
                viewLabel.setAnalysisCSS2("rst");
                break;
            case "fin":
                viewLabel.setAnalysisMessage2("TCP Connection Termination");
                viewLabel.setAnalysisCSS2("connectionTermination");
                break;
            case "dupAck":
                viewLabel.setAnalysisMessage2("TCP Duplicate ACK" + segment.getDupAckFrameCountRelative() + "#" + segment.getDupAckCounter());
                viewLabel.setAnalysisMessage2Absolute("TCP Duplicate ACK" + segment.getDupAckFrameCountAbsolute() + "#" + segment.getDupAckCounter());
                viewLabel.setAnalysisCSS2("dupAck");
                break;
            case "keepAlive":
                viewLabel.setAnalysisMessage2("TCP Keep-Alive");
                viewLabel.setAnalysisCSS2("keepAlive");
                break;
            case "keepAliveAck":
                viewLabel.setAnalysisMessage2("TCP Keep-Alive ACK");
                viewLabel.setAnalysisCSS2("keepAliveAck");
                break;
            case "ackUnseenSegment":
                viewLabel.setAnalysisMessage2("TCP ACKed Unseen Segment");
                viewLabel.setAnalysisCSS2("ackUnseenSegment");
                break;
            case "windowUpdate":
                viewLabel.setAnalysisMessage2("TCP Window Update");
                viewLabel.setAnalysisCSS2("windowUpdate");
                break;
            case "windowFull":
                viewLabel.setAnalysisMessage2("TCP Window Full");
                viewLabel.setAnalysisCSS2("windowFull");
                break;
            case "zeroWindowProbeAck":
                viewLabel.setAnalysisMessage2("TCP Zero Window Probe ACK");
                viewLabel.setAnalysisCSS2("zeroWindowProbeAck");
                break;
            case "zeroWindow":
                viewLabel.setAnalysisMessage2("TCP Zero Window");
                viewLabel.setAnalysisCSS2("zeroWindow");
                break;
            case "zeroWindowProbe":
                viewLabel.setAnalysisMessage2("TCP Zero Window Probe");
                viewLabel.setAnalysisCSS2("zeroWindowProbe");
                break;
            case "outOfOrder":
                viewLabel.setAnalysisMessage2("TCP Out of Order");
                viewLabel.setAnalysisCSS2("outOfOrder");
                break;
            case "spuriousRetransmission":
                viewLabel.setAnalysisMessage2("TCP Spurious Retransmission");
                viewLabel.setAnalysisCSS2("spuriousRetransmission");
                break;
            case "fastRetransmission":
                viewLabel.setAnalysisMessage2("TCP Fast Retransmission");
                viewLabel.setAnalysisCSS2("fastRetransmission");
                break;
            case "retransmission":
                if (segment.getPotentialRetransmissionOf() == 0) {
                    viewLabel.setAnalysisMessage2("TCP Retransmission");
                } else {
                    viewLabel.setAnalysisMessage2("TCP Retransmission of " + segment.getPotentialRetransmissionOf());
                }
                viewLabel.setAnalysisCSS2("retransmission");
                break;
            case "previousSegmentNotCaptured":
                viewLabel.setAnalysisMessage2("TCP Previous Segment Not Captured");
                viewLabel.setAnalysisCSS2("previousSegmentNotCaptured");
                break;
            default:
                viewLabel.setAnalysisMessage2("");
                break;
        }
    }

    private void analysisFlag1Switch(Segment segment, ViewLabel viewLabel, String analysisFlag1) {
        switch (analysisFlag1) {
            case "syn1":
                viewLabel.setAnalysisMessage("TCP Connection establishment: active open");
                viewLabel.setAnalysisCSS("syn1");
                break;
            case "syn2":
                viewLabel.setAnalysisMessage("TCP Connection establishment: passive open");
                viewLabel.setAnalysisCSS("syn2");
                break;
            case "syn3":
                viewLabel.setAnalysisMessage("TCP Connection establishment: three-way handshake complete");
                viewLabel.setAnalysisCSS("syn3");
                break;
            case "reused":
                viewLabel.setAnalysisMessage("TCP Port Numbers Reused");
                viewLabel.setAnalysisCSS("reused");
                break;
            case "rst":
                viewLabel.setAnalysisMessage("TCP Connection Reset!");
                viewLabel.setAnalysisCSS("rst");
                break;
            case "fin":
                viewLabel.setAnalysisMessage("TCP Connection Termination");
                viewLabel.setAnalysisCSS("connectionTermination");
                break;
            case "dupAck":
                viewLabel.setAnalysisMessage("TCP Duplicate ACK" + segment.getDupAckFrameCountRelative() + "#" + segment.getDupAckCounter());
                viewLabel.setAnalysisMessageAbsolute("TCP Duplicate ACK" + segment.getDupAckFrameCountAbsolute() + "#" + segment.getDupAckCounter());
                viewLabel.setAnalysisCSS("dupAck");
                break;
            case "keepAlive":
                viewLabel.setAnalysisMessage("TCP Keep-Alive");
                viewLabel.setAnalysisCSS("keepAlive");
                break;
            case "keepAliveAck":
                viewLabel.setAnalysisMessage("TCP Keep-Alive ACK");
                viewLabel.setAnalysisCSS("keepAliveAck");
                break;
            case "ackUnseenSegment":
                viewLabel.setAnalysisMessage("TCP ACKed Unseen Segment");
                viewLabel.setAnalysisCSS("ackUnseenSegment");
                break;
            case "windowUpdate":
                viewLabel.setAnalysisMessage("TCP Window Update");
                viewLabel.setAnalysisCSS("windowUpdate");
                break;
            case "windowFull":
                viewLabel.setAnalysisMessage("TCP Window Full");
                viewLabel.setAnalysisCSS("windowFull");
                break;
            case "zeroWindowProbeAck":
                viewLabel.setAnalysisMessage("TCP Zero Window Probe ACK");
                viewLabel.setAnalysisCSS("zeroWindowProbeAck");
                break;
            case "zeroWindow":
                viewLabel.setAnalysisMessage("TCP Zero Window");
                viewLabel.setAnalysisCSS("zeroWindow");
                break;
            case "zeroWindowProbe":
                viewLabel.setAnalysisMessage("TCP Zero Window Probe");
                viewLabel.setAnalysisCSS("zeroWindowProbe");
                break;
            case "outOfOrder":
                viewLabel.setAnalysisMessage("TCP Out of Order");
                viewLabel.setAnalysisCSS("outOfOrder");
                break;
            case "spuriousRetransmission":
                viewLabel.setAnalysisMessage("TCP Spurious Retransmission");
                viewLabel.setAnalysisCSS("spuriousRetransmission");
                break;
            case "fastRetransmission":
                viewLabel.setAnalysisMessage("TCP Fast Retransmission");
                viewLabel.setAnalysisCSS("fastRetransmission");
                break;
            case "retransmission":
                if (segment.getPotentialRetransmissionOf() == 0) {
                    viewLabel.setAnalysisMessage("TCP Retransmission");
                } else {
                    viewLabel.setAnalysisMessage("TCP Retransmission of " + segment.getPotentialRetransmissionOf());
                }
                viewLabel.setAnalysisCSS("retransmission");
                break;
            case "previousSegmentNotCaptured":
                viewLabel.setAnalysisMessage("TCP Previous Segment Not Captured");
                viewLabel.setAnalysisCSS("previousSegmentNotCaptured");
                break;
        }
    }

    String controlFlagsToString(Segment segment) {
        List<String> flags = new ArrayList<>();
        if (segment.isRst()) {
            flags.add("RST");
        }
        if (segment.isSyn()) {
            flags.add("SYN");
        }
        if (segment.isFin()) {
            flags.add("FIN");
        }
        if (segment.isUrg()) {
            flags.add("URG");
        }
        if (segment.isAck() && (segment.getAlgoSyn2() || segment.getAlgoSyn3() || segment.isFin() || segment.isUrg())) {
            flags.add("ACK");
        }
        if (segment.isPsh()) {
            flags.add("PSH");
        }
        if (segment.isECN()) {
            flags.add("ECE");
        }
        if (segment.isCWR()) {
            flags.add("CWR");
        }
        // NS
        StringBuilder sb = new StringBuilder();
        for (String flag : flags) {
            sb.append(flag);
            sb.append(", ");
        }
        if (sb.length() > 2) {
            return sb.toString().substring(0, sb.length() - 2);
        } else {
            return "";
        }
    }

    public List<Integer> getKeys(HashMap<Integer, List<ViewLabel>> hashMap) {
        return new ArrayList<>(hashMap.keySet());
    }

    public void passFileName(String fileName) {
        this.fileName = fileName;
    }
}
