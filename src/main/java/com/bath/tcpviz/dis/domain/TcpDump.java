package com.bath.tcpviz.dis.domain;

import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.*;
import org.pcap4j.packet.namednumber.ArpOperation;
import org.pcap4j.util.MacAddress;

import java.io.EOFException;
import java.net.InetAddress;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TcpDump {
    private static final int MAX_COUNT = 99999;

    public List<Packet> readPcapFile(String fileName) throws PcapNativeException, NotOpenException {
        List<Packet> packetList = new ArrayList<>();
        PcapHandle handle = null;
        try {
            handle = Pcaps.openOffline(fileName, PcapHandle.TimestampPrecision.NANO);
        } catch (PcapNativeException e) {
            handle = Pcaps.openOffline(fileName);
        }
        for (int packetNumber = 1; packetNumber < MAX_COUNT; packetNumber++) {
            Packet ps = new Packet();
            try {
                org.pcap4j.packet.Packet handleNextPacket = handle.getNextPacketEx();
                ps.setPacketNumber(packetNumber);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSS");
                String formattedTimeStamp = handle.getTimestamp().toLocalDateTime().format(formatter);
                ps.setTimestamp(formattedTimeStamp);
                ps.setTs(handle.getTimestamp());
                ps.setHeader(handleNextPacket.getHeader());
                ps.setPayload(handleNextPacket.getPayload());
                if (handleNextPacket.contains(UnknownPacket.class)) {
                    ps.setUnknownP(String.valueOf(handleNextPacket.get(UnknownPacket.class).getPayload()));
                    ps.setProtocol("Unsupported protocol");
                    ps.setSrcPortInt(1);
                    ps.setDstPortInt(1025);
                    ps.setSender(ps.getSrcAddr() + ":1");
                    ps.setReceiver(ps.getDstAddr() + ":1025");
                }
                if (handleNextPacket.contains(IpV6Packet.class)) {
                    IpV6Packet.IpV6Header iPv6header = handleNextPacket.get(IpV6Packet.class).getHeader();
                    ps.setSrcAddr(String.valueOf(iPv6header.getSrcAddr()));
                    ps.setDstAddr(String.valueOf(iPv6header.getDstAddr()));
                    ps.setProtocol(contentExtracter(String.valueOf(iPv6header.getProtocol())));
                    ps.setSrcPortInt(1);
                    ps.setDstPortInt(1025);
                    ps.setSender(ps.getSrcAddr() + ":1");
                    ps.setReceiver(ps.getDstAddr() + ":2");
                }
                if (handleNextPacket.contains(IpV4Packet.class)) {
                    IpV4Packet.IpV4Header ipV4Header = handleNextPacket.get(IpV4Packet.class).getHeader();
                    ps.setSrcAddr(String.valueOf(ipV4Header.getSrcAddr()));
                    ps.setDstAddr(String.valueOf(ipV4Header.getDstAddr()));
                    ps.setProtocol(contentExtracter(String.valueOf(ipV4Header.getProtocol())));
                    ps.setIpId(ipV4Header.getIdentificationAsInt());
                    ps.setTtl(ipV4Header.getTtlAsInt());
                    ps.setSrcPortInt(1);
                    ps.setDstPortInt(1025);
                    ps.setSender(ps.getSrcAddr() + ":1");
                    ps.setReceiver(ps.getDstAddr() + ":2");
                }
                if (handleNextPacket.contains(IcmpV4CommonPacket.class)) {
                    ps.setMacAddressSrc(String.valueOf(handleNextPacket.get(IpV4Packet.class).getHeader().getSrcAddr()));
                    ps.setMacAddressDst(String.valueOf(handleNextPacket.get(IpV4Packet.class).getHeader().getDstAddr()));
                    ps.setProtocol("ICMPv4");
                    createArpICMPSender(ps);
                    String icmpV4Type = String.valueOf(handleNextPacket.get(IcmpV4CommonPacket.class).getHeader().getType());
                    ps.setICMPmessage(contentExtracter(icmpV4Type));
                }
                if (handleNextPacket.contains(IcmpV6CommonPacket.class)) {
                    ps.setMacAddressSrc(String.valueOf(handleNextPacket.get(IpV6Packet.class).getHeader().getSrcAddr()));
                    ps.setMacAddressDst(String.valueOf(handleNextPacket.get(IpV6Packet.class).getHeader().getDstAddr()));
                    ps.setProtocol("ICMPv6");
                    createArpICMPSender(ps);
                    String icmpV6Type = String.valueOf(handleNextPacket.get(IcmpV6CommonPacket.class).getHeader().getType());
                    ps.setICMPmessage(contentExtracter(icmpV6Type));
                }
                if (handleNextPacket.contains(ArpPacket.class)) {
                    ps.setProtocol("ARP");
                    ps.setArp(String.valueOf(handleNextPacket.get(ArpPacket.class).getPayload()));
                    ps.setMacAddressSrc(String.valueOf(handleNextPacket.get(ArpPacket.class).getHeader().getSrcHardwareAddr()));
                    ps.setMacAddressDst(String.valueOf(handleNextPacket.get(ArpPacket.class).getHeader().getDstHardwareAddr()));
                    ps.setArpMessage(arpMessageExtractor(handleNextPacket));
                    ps.setArpMessageResolved(arpMessageExtractorResolved(handleNextPacket));
                    createArpICMPSender(ps);
                }
                if (handleNextPacket.contains(DnsPacket.class)) {
                    ps.setProtocol("DNS");
                    ps.setDns(String.valueOf(handleNextPacket.get(DnsPacket.class).getHeader()));
                    ps.setDnsQuery(String.valueOf(handleNextPacket.get(DnsPacket.class).getHeader().getQuestions().get(0).getQName()));
                }
                if (handleNextPacket.contains(UdpPacket.class)) {
                    ps.setUdp(String.valueOf(handleNextPacket.get(UdpPacket.class).getPayload()));
                    ps.setSrcPortInt(handleNextPacket.get(UdpPacket.class).getHeader().getSrcPort().valueAsInt());
                    ps.setDstPortInt(handleNextPacket.get(UdpPacket.class).getHeader().getDstPort().valueAsInt());
                    if (handleNextPacket.get(UdpPacket.class).getPayload() != null) {
                        ps.setUDPLength(handleNextPacket.get(UdpPacket.class).getPayload().length());
                    } else {
                        ps.setUDPLength(0);
                    }
                    createTcpUdpSender(ps);
                }
                if (handleNextPacket.contains(TcpPacket.class)) {
                    TcpPacket.TcpHeader tcpHeader = handleNextPacket.get(TcpPacket.class).getHeader();
                    ps.setHeader(tcpHeader);
                    ps.setAck(tcpHeader.getAck());
                    ps.setSyn(tcpHeader.getSyn());
                    ps.setFin(tcpHeader.getFin());
                    ps.setPsh(tcpHeader.getPsh());
                    ps.setUrg(tcpHeader.getUrg());
                    ps.setRst(tcpHeader.getRst());
                    if (tcpHeader.getReserved() == 1) {
                        ps.setECN(true);
                    } else if (tcpHeader.getReserved() == 2) {
                        ps.setCWR(true);
                    } else if (tcpHeader.getReserved() == 3) {
                        ps.setECN(true);
                        ps.setCWR(true);
                    }
                    ps.setOptions(String.valueOf(tcpHeader.getOptions()));
                    ps.setAckNumber(tcpHeader.getAcknowledgmentNumberAsLong());
                    ps.setSeqNumber(tcpHeader.getSequenceNumberAsLong());
                    ps.setSrcPort(String.valueOf(tcpHeader.getSrcPort()));
                    ps.setDstPort(String.valueOf(tcpHeader.getDstPort()));
                    if (tcpHeader.getSrcPort().valueAsInt() == 0 || tcpHeader.getDstPort().valueAsInt() == 0) {
                        ps.setSrcPortInt(1);
                        ps.setDstPortInt(1025);
                        ps.setSender(ps.getSrcAddr() + ":1");
                        ps.setReceiver(ps.getDstAddr() + ":1025");
                    } else {
                        ps.setSrcPortInt(tcpHeader.getSrcPort().valueAsInt());
                        ps.setDstPortInt(tcpHeader.getDstPort().valueAsInt());
                    }
                    createTcpUdpSender(ps);
                    ps.setWindowSize(tcpHeader.getWindowAsInt());
                    if (handleNextPacket.get(TcpPacket.class).getPayload() != null) {
                        ps.setDataSize(handleNextPacket.get(TcpPacket.class).getPayload().length());
                    } else if (handleNextPacket.get(TcpPacket.class).getHeader().getSyn() || handleNextPacket.get(TcpPacket.class).getHeader().getFin()) {
                        ps.setDataSize(0);
                    } else {
                        ps.setDataSize(0);
                    }
                    Pattern patternMSS = Pattern.compile("Size:\\s(\\d+)\\sbytes");
                    Matcher matcherMSS = patternMSS.matcher(tcpHeader.getOptions().toString());
                    if (matcherMSS.find()) {
                        ps.setMss(Integer.parseInt(matcherMSS.group(1)));
                    }
                    Pattern patternWS = Pattern.compile("Shift Count:\\s(\\d+)");
                    Matcher matcherWS = patternWS.matcher(tcpHeader.getOptions().toString());
                    if (matcherWS.find()) {
                        ps.setWindowScaling(Integer.parseInt(matcherWS.group(1)));
                    } else {
                        ps.setWindowScaling(-2);
                    }
                    org.pcap4j.packet.Packet payload = handleNextPacket.get(TcpPacket.class).getPayload();
                    if (payload != null && payload.length() >= 3) {
                        if ((payload.getRawData()[1] + " " + payload.getRawData()[2]).equals("3 3")) {
                            if (String.valueOf(payload.getRawData()[0]).equals("22")) {
                                ps.setTLSmessage("TLS Handshake");
                            } else if (String.valueOf(payload.getRawData()[0]).equals("23")) {
                                ps.setTLSmessage("TLS Application Data");
                            } else if (String.valueOf(payload.getRawData()[0]).equals("20")) {
                                ps.setTLSmessage("TLS Change Cipher Spec");
                            } else if (String.valueOf(payload.getRawData()[0]).equals("21")) {
                                ps.setTLSmessage("TLS Alert");
                            } else {
                                ps.setTLSmessage("TLS message code " + payload.getRawData()[0]);
                            }
                        }
                    }
                    if (payload != null) {
                        Pattern patternHTTPGET = Pattern.compile("47 45 54 20[0-9a-f\\s]*2f 20 48 54 54 50 2f 31 2e 31 0d 0a");
                        Matcher matcherHTTPGET = patternHTTPGET.matcher(payload.toString());
                        if (matcherHTTPGET.find()) {
                            ps.setHTTPmessage("HTTP GET");
                        }
                    }

                }
            } catch (TimeoutException ignored) {
            } catch (EOFException e) {
                break;
            }
            packetList.add(ps);
        }
        handle.close();
        return packetList;
    }

    private void createArpICMPSender(Packet ps) {
        ps.setSender(ps.getMacAddressSrc());
        ps.setReceiver(ps.getMacAddressDst());
    }

    private void createTcpUdpSender(Packet ps) {
        ps.setSender(ps.getSrcAddr() + ":" + ps.getSrcPortInt());
        ps.setReceiver(ps.getDstAddr() + ":" + ps.getDstPortInt());
    }

    private String arpMessageExtractor(org.pcap4j.packet.Packet handleNextPacket) {
        if (handleNextPacket.contains(ArpPacket.class)) {
            ArpPacket arp = handleNextPacket.get(ArpPacket.class);
            InetAddress srcIp = arp.getHeader().getSrcProtocolAddr();
            InetAddress dstIp = arp.getHeader().getDstProtocolAddr();
            MacAddress srcMac = arp.getHeader().getSrcHardwareAddr();
            if (arp.getHeader().getOperation().equals(ArpOperation.REQUEST)) {
                return "Request: Who has " + dstIp + "? Tell " + srcIp;
            } else if (arp.getHeader().getOperation().equals(ArpOperation.REPLY)) {
                return "Reply: " + srcIp + " is at " + srcMac;
            }
        }
        return null;
    }

    private String arpMessageExtractorResolved(org.pcap4j.packet.Packet handleNextPacket) {
        if (handleNextPacket.contains(ArpPacket.class)) {
            ArpPacket arp = handleNextPacket.get(ArpPacket.class);
            InetAddress srcIp = arp.getHeader().getSrcProtocolAddr();
            InetAddress dstIp = arp.getHeader().getDstProtocolAddr();
            MacAddress srcMac = arp.getHeader().getSrcHardwareAddr();
            if (arp.getHeader().getOperation().equals(ArpOperation.REQUEST)) {
                return " Request: Who has " + dstIp.getCanonicalHostName() + "? Tell " + srcIp.getCanonicalHostName();
            } else if (arp.getHeader().getOperation().equals(ArpOperation.REPLY)) {
                return " Reply: " + srcIp.getCanonicalHostName() + " is at " + srcMac;
            }
        }
        return null;
    }

    private String contentExtracter(String input) {
        return input.substring(input.indexOf("(") + 1, input.indexOf(")"));
    }

    private String contentDNSAnswerExtracter(String input) {
        String result = "";
        Pattern patternDNSAnswer = Pattern.compile("\\b(([0-9a-f]+?[.:]+?)*[0-9a-f]+)\\b");
        Matcher matcherDNSAnswer = patternDNSAnswer.matcher(input);
        if (matcherDNSAnswer.find()) {
            result = matcherDNSAnswer.group(1);
        }
        return result;
    }
}