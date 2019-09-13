package com.bath.tcpviz.dis.domain;

import java.sql.Timestamp;

public class Packet {
    private int packetNumber;
    private String timestamp;
    private Timestamp ts;
    private String srcAddr;
    private String srcPort;//remove
    private int srcPortInt;
    private String dstAddr;
    private String dstPort;//remove
    private int dstPortInt;
    private String protocol;
    private String dns;
    private String udp;
    private String unknownP;
    private String arp;
    private String arpMessage;
    private long ackNumber;
    private long seqNumber;
    private boolean urg;
    private boolean ack;
    private boolean psh;
    private boolean rst;
    private boolean syn;
    private boolean fin;
    private int windowSize;
    private int windowScaling;
    private int mss;
    private String sender;
    private String receiver;
    private int streamId;
    private org.pcap4j.packet.Packet.Header header;
    private org.pcap4j.packet.Packet payload;
    private int dataSize;
    private int ipId;
    private int ttl;
    private String arpMessageResolved;
    private String macAddressSrc;
    private String macAddressDst;
    private String ICMPmessage;
    private String dnsQuery;
    private String dnsResponse;
    private String options;
    private int udpLength;
    private String TLSmessage;
    private String HTTPmessage;
    private boolean ECN;
    private boolean CWR;


    public int getPacketNumber() {
        return packetNumber;
    }

    public void setPacketNumber(int packetNumber) {
        this.packetNumber = packetNumber;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Timestamp getTs() {
        return ts;
    }

    public void setTs(Timestamp ts) {
        this.ts = ts;
    }

    public String getDns() {
        return dns;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }

    public String getUdp() {
        return udp;
    }

    public void setUdp(String udp) {
        this.udp = udp;
    }

    public String getUnknownP() {
        return unknownP;
    }

    public void setUnknownP(String unknownP) {
        this.unknownP = unknownP;
    }

    public String getSrcAddr() {
        return srcAddr;
    }

    public void setSrcAddr(String srcAddr) {
        this.srcAddr = srcAddr;
    }

    public String getDstAddr() {
        return dstAddr;
    }

    public void setDstAddr(String dstAddr) {
        this.dstAddr = dstAddr;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public boolean isAck() {
        return ack;
    }

    public void setAck(boolean ack) {
        this.ack = ack;
    }

    public boolean isSyn() {
        return syn;
    }

    public void setSyn(boolean syn) {
        this.syn = syn;
    }

    public boolean isFin() {
        return fin;
    }

    public void setFin(boolean fin) {
        this.fin = fin;
    }

    public boolean isPsh() {
        return psh;
    }

    public void setPsh(boolean psh) {
        this.psh = psh;
    }

    public boolean isUrg() {
        return urg;
    }

    public void setUrg(boolean urg) {
        this.urg = urg;
    }

    public boolean isRst() {
        return rst;
    }

    public void setRst(boolean rst) {
        this.rst = rst;
    }

    public long getAckNumber() {
        return ackNumber;
    }

    public void setAckNumber(long ackNumber) {
        this.ackNumber = ackNumber;
    }

    public long getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(long seqNumber) {
        this.seqNumber = seqNumber;
    }

    public String getSrcPort() {
        return srcPort;
    }

    public void setSrcPort(String srcPort) {
        this.srcPort = srcPort;
    }

    public int getSrcPortInt() {
        return srcPortInt;
    }

    public void setSrcPortInt(int srcPortInt) {
        this.srcPortInt = srcPortInt;
    }

    public String getDstPort() {
        return dstPort;
    }

    public void setDstPort(String dstPort) {
        this.dstPort = dstPort;
    }

    public int getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(int window) {
        this.windowSize = window;
    }

    public int getMss() {
        return mss;
    }

    public void setMss(int mss) {
        this.mss = mss;
    }

    public int getWindowScaling() {
        return windowScaling;
    }

    public void setWindowScaling(int ws) {
        this.windowScaling = ws;
    }

    public void setArp(String arp) {
        this.arp = arp;
    }

    public String getArp() {
        return arp;
    }

    public void setArpMessage(String arpMessage) {
        this.arpMessage = arpMessage;
    }

    public String getArpMessage() {
        return arpMessage;
    }

    public void setHeader(org.pcap4j.packet.Packet.Header header) {
        this.header = header;
    }

    public org.pcap4j.packet.Packet.Header getHeader() {
        return header;
    }

    public void setPayload(org.pcap4j.packet.Packet payload) {
        this.payload = payload;
    }

    public org.pcap4j.packet.Packet getPayload() {
        return payload;
    }

    public void setDstPortInt(int dstPortInt) {
        this.dstPortInt = dstPortInt;
    }

    public int getDstPortInt() {
        return dstPortInt;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setStreamId(int streamId) {
        this.streamId = streamId;
    }

    public int getStreamId() {
        return streamId;
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    public int getDataSize() {
        return dataSize;
    }

    public void setIpId(int ipId) {
        this.ipId = ipId;
    }

    public int getIpId() {
        return ipId;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public int getTtl() {
        return ttl;
    }

    public void setArpMessageResolved(String arpMessageResolved) {
        this.arpMessageResolved = arpMessageResolved;
    }

    public String getArpMessageResolved() {
        return arpMessageResolved;
    }

    public void setMacAddressSrc(String macAddressSrc) {
        this.macAddressSrc = macAddressSrc;
    }

    public String getMacAddressSrc() {
        return macAddressSrc;
    }

    public void setMacAddressDst(String macAddressDst) {
        this.macAddressDst = macAddressDst;
    }

    public String getMacAddressDst() {
        return macAddressDst;
    }

    public void setICMPmessage(String icmPmessage) {
        this.ICMPmessage = icmPmessage;
    }

    public String getICMPmessage() {
        return ICMPmessage;
    }

    public void setDnsQuery(String dnsQuery) {
        this.dnsQuery = dnsQuery;
    }

    public String getDnsQuery() {
        return dnsQuery;
    }

    public void setDnsResponse(String dnsResponse) {
        this.dnsResponse = dnsResponse;
    }

    public String getDnsResponse() {
        return dnsResponse;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getOptions() {
        return options;
    }

    public int getUDPLength() {
        return udpLength;
    }

    public void setUDPLength(int udpLength) {
        this.udpLength = udpLength;
    }

    public void setTLSmessage(String tlsError) {
        this.TLSmessage = tlsError;
    }

    public String getTLSmessage() {
        return TLSmessage;
    }

    public void setHTTPmessage(String httPmessage) {
        this.HTTPmessage = httPmessage;
    }

    public String getHTTPmessage() {
        return HTTPmessage;
    }

    public void setECN(boolean ecn) {
        this.ECN = ecn;
    }

    public boolean isECN() {
        return ECN;
    }

    public void setCWR(boolean cwr) {
        this.CWR = cwr;
    }

    public boolean isCWR() {
        return CWR;
    }
}
