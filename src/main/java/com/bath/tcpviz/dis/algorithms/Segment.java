package com.bath.tcpviz.dis.algorithms;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

public class Segment {
    private int streamId;
    private String protocol;
    private int packetNumber;
    private int relativePacketNumber;
    private String absoluteTime;
    private Timestamp ts;
    private Timestamp delta;
    private String deltaFormatted;
    private long seqNum;
    private long ackNum;
    private long relativeSeqNum;
    private long relativeAckNum;
    private boolean urg, ack, psh, rst, syn, fin;
    private long winSize;
    private int windowScaling;
    private long calculatedWindowSize;
    private long segDataSize;
    private String srcIpAndPort;
    private int portSrc;
    private String dstIpAndPort;
    private int portDst;

    private int MSS;
    private String header;
    private String payload;
    private String arpMessage;
    private String arpMessageResolved;
    private String icmpMessage;
    private String dnsQuery;
    private String dnsResponse;


    private Segment prev;
    private Segment next;

    private long nextExpSeqNum;
    private long nextExpAckNum;
    private long lastSeenAckNum;
    private long lastSeenWinSize;
    private int dupAckCounter;
    private int dupAckFrameCountRelative;
    private long bytesInFlight;
    private long revWinSize;
    private boolean revKeepAlive;
    private boolean revLastSeenZeroWinProbe;
    private long existingConversationInitialSeqNum;
    private boolean existingConversation;
    private boolean lastDupAckArrivedWithin20ms;
    private long lastSegArriveTimeMs;
    private boolean algoSyn1;
    private boolean algoSyn2;
    private boolean algoSyn3;
    private boolean algoZeroWindow;
    private boolean algoWindowUpdate;
    private boolean algoDupAck;
    private boolean algoKeepAlive;
    private boolean algoKeepAliveAck;
    private boolean algoWindowFull;
    private boolean algoZeroWindowProbe;
    private boolean algoZeroWindowProbeAck;
    private long maxRevSeqAndData;
    private boolean ACKedUnseenSegment;
    private long lastNextExpSeqNum;
    private boolean previousSegmentNotCaptured;
    private long lastRevAckNum;
    private int ipId;
    private long maxSeqNum;
    private boolean outOfOrder;
    private boolean algoFastRetransmission;
    private int revDupAckCounter;
    private boolean algoSpuriousRetransmission;
    private boolean algoRetransmission;
    private long lastSeenSeqNum;
    private long lastSeenSegmentLength;
    private boolean potentialRetransmission;
    private int potentialRetransmissionOf;
    private boolean lastAckArrivedAfter200ms;
    private int TTL;
    private boolean lastSegArrivedWithin4ms;
    private String relativeTime;
    private int totalDataInStream;
    private int totalPacketsLost;
    private int totalDupAcks;
    private int totalPacketsInStream;
    private int health;
    private int UDPLength;
    private int dupAckFrameCountAbsolute;
    private int totalPacketsInDump;
    private int numberOfAnalysisFlags;
    private HashMap<Integer, List<String>> analysisFlagMap;
    private boolean firstSegmentForDevice;
    private boolean bothSidesSeen;
    private long lastRevNextExpectedSequenceNumber;
    private boolean portUsed;
    private boolean algoReusedPorts;
    private long revCalculatedWinSize;
    private int revWinScaling;
    private String TLSerror;
    private String HTTPmessage;
    private boolean CWR;
    private boolean ECN;

    public int getPortSrc() {
        return portSrc;
    }

    public void setPortSrc(int portSrc) {
        this.portSrc = portSrc;
    }

    public int getPortDst() {
        return portDst;
    }

    public void setPortDst(int portDst) {
        this.portDst = portDst;
    }

    public long getSegDataSize() {
        return segDataSize;
    }

    public void setSegDataSize(long segDataSize) {
        this.segDataSize = segDataSize;
    }

    public long getAckNum() {
        return ackNum;
    }

    public void setAckNum(long ackNum) {
        this.ackNum = ackNum;
    }

    public long getLastSeenAckNum() {
        return lastSeenAckNum;
    }

    public void setLastSeenAckNum(long lastSeenAckNum) {
        this.lastSeenAckNum = lastSeenAckNum;
    }

    public long getWinSize() {
        return winSize;
    }

    public void setWinSize(long winSize) {
        this.winSize = winSize;
    }

    public long getLastSeenWinSize() {
        return lastSeenWinSize;
    }

    public void setLastSeenWinSize(long lastSeenWinSize) {
        this.lastSeenWinSize = lastSeenWinSize;
    }

    public long getNextExpSeqNum() {
        return nextExpSeqNum;
    }

    public void setNextExpSeqNum(long nextExpSeqNum) {
        this.nextExpSeqNum = nextExpSeqNum;
    }

    public long getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(long seqNum) {
        this.seqNum = seqNum;
    }

    public Segment getPrev() {
        return prev;
    }

    public void setPrev(Segment prev) {
        this.prev = prev;
    }

    public Segment getNext() {
        return next;
    }

    public void setNext(Segment next) {
        this.next = next;
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

    public boolean isRst() {
        return rst;
    }

    public void setRst(boolean rst) {
        this.rst = rst;
    }

    public void setAlgoKeepAlive(boolean algoKeepAlive) {
        this.algoKeepAlive = algoKeepAlive;
    }

    public boolean isAlgoKeepAlive() {
        return algoKeepAlive;
    }

    public void setDupAckCounter(int dupAckCounter) {
        this.dupAckCounter = dupAckCounter;
    }

    public int getDupAckCounter() {
        return dupAckCounter;
    }

    public void setNextExpAckNum(long nextExpAckNum) {
        this.nextExpAckNum = nextExpAckNum;
    }

    public long getNextExpAckNum() {
        return nextExpAckNum;
    }

    public void setLastDupAckArrivedWithin20ms(boolean lastDupAckTimeMs) {
        this.lastDupAckArrivedWithin20ms = lastDupAckTimeMs;
    }

    public boolean isLastDupAckArrivedWithin20ms() {
        return lastDupAckArrivedWithin20ms;
    }

    public void setLastSegArriveTimeMs(long lastSegArriveTimeMs) {
        this.lastSegArriveTimeMs = lastSegArriveTimeMs;
    }

    public long getLastSegArriveTimeMs() {
        return lastSegArriveTimeMs;
    }

    public void setRevWinSize(long revWinSize) {
        this.revWinSize = revWinSize;
    }

    public long getRevWinSize() {
        return revWinSize;
    }

    public void setRevKeepAlive(boolean revKeepAlive) {
        this.revKeepAlive = revKeepAlive;
    }

    public boolean isRevKeepAlive() {
        return revKeepAlive;
    }

    public void setRevLastSeenZeroWinProbe(boolean revLastSeenZeroWinProbe) {
        this.revLastSeenZeroWinProbe = revLastSeenZeroWinProbe;
    }

    public boolean getRevLastSeenZeroWinProbe() {
        return revLastSeenZeroWinProbe;
    }

    public void setAck(boolean ack) {
        this.ack = ack;
    }

    public boolean isAck() {
        return ack;
    }

    public void setExistingConversation(boolean existingConversation) {
        this.existingConversation = existingConversation;
    }

    public boolean isExistingConversation() {
        return existingConversation;
    }

    public void setExistingConversationInitialSeqNum(long existingConversationInitialSeqNum) {
        this.existingConversationInitialSeqNum = existingConversationInitialSeqNum;
    }

    public long getExistingConversationInitialSeqNum() {
        return existingConversationInitialSeqNum;
    }

    public void setStreamId(int streamId) {
        this.streamId = streamId;
    }

    public int getStreamId() {
        return streamId;
    }

    public boolean isUrg() {
        return urg;
    }

    public void setUrg(boolean urg) {
        this.urg = urg;
    }

    public boolean isPsh() {
        return psh;
    }

    public void setPsh(boolean psh) {
        this.psh = psh;
    }

    public void setAlgoSyn1(boolean algoSyn1) {
        this.algoSyn1 = algoSyn1;
    }

    public boolean getAlgoSyn1() {
        return algoSyn1;
    }

    public void setAlgoSyn2(boolean algoSyn2) {
        this.algoSyn2 = algoSyn2;
    }

    public boolean getAlgoSyn2() {
        return algoSyn2;
    }

    public void setAlgoSyn3(boolean algoSyn3) {
        this.algoSyn3 = algoSyn3;
    }

    public boolean getAlgoSyn3() {
        return algoSyn3;
    }

    public void setAlgoZeroWindow(boolean algoZeroWindow) {
        this.algoZeroWindow = algoZeroWindow;
    }

    public boolean getAlgoZeroWindow() {
        return algoZeroWindow;
    }

    public String getSrcIpAndPort() {
        return srcIpAndPort;
    }

    public void setSrcIpAndPort(String srcIpAndPort) {
        this.srcIpAndPort = srcIpAndPort;
    }

    public int getPacketNumber() {
        return packetNumber;
    }

    public void setPacketNumber(int packetNumber) {
        this.packetNumber = packetNumber;
    }

    public boolean isAlgoWindowUpdate() {
        return algoWindowUpdate;
    }

    public void setAlgoWindowUpdate(boolean algoWindowUpdate) {
        this.algoWindowUpdate = algoWindowUpdate;
    }

    public void setAlgoDupAck(boolean algoDupAck) {
        this.algoDupAck = algoDupAck;
    }

    public boolean getAlgoDupAck() {
        return algoDupAck;
    }

    public void setDupAckFrameCountRelative(int dupAckFrameCountRelative) {
        this.dupAckFrameCountRelative = dupAckFrameCountRelative;
    }

    public int getDupAckFrameCountRelative() {
        return dupAckFrameCountRelative;
    }

    public void setAlgoKeepAliveAck(boolean algoKeepAliveAck) {
        this.algoKeepAliveAck = algoKeepAliveAck;
    }

    public boolean getAlgoKeepAliveAck() {
        return algoKeepAliveAck;
    }

    public boolean isAlgoWindowFull() {
        return algoWindowFull;
    }

    public void setAlgoWindowFull(boolean algoWindowFull) {
        this.algoWindowFull = algoWindowFull;
    }

    public void setWindowScaling(int windowScaling) {
        this.windowScaling = windowScaling;
    }

    public int getWindowScaling() {
        return windowScaling;
    }

    public void setCalculatedWindowSize(long calculatedWindowSize) {
        this.calculatedWindowSize = calculatedWindowSize;
    }

    public long getCalculatedWindowSize() {
        return calculatedWindowSize;
    }

    public long getBytesInFlight() {
        return bytesInFlight;
    }

    public void setBytesInFlight(long bytesInFlight) {
        this.bytesInFlight = bytesInFlight;
    }

    public void setAlgoZeroWindowProbe(boolean algoZeroWindowProbe) {
        this.algoZeroWindowProbe = algoZeroWindowProbe;
    }

    public boolean getAlgoZeroWindowProbe() {
        return algoZeroWindowProbe;
    }

    public void setAlgoZeroWindowProbeAck(boolean algoZeroWindowProbeAck) {
        this.algoZeroWindowProbeAck = algoZeroWindowProbeAck;
    }

    public boolean getAlgoZeroWindowProbeAck() {
        return algoZeroWindowProbeAck;
    }

    public long getMaxRevSeqAndData() {
        return maxRevSeqAndData;
    }

    public void setMaxRevSeqAndData(long maxRevSeqAndData) {
        this.maxRevSeqAndData = maxRevSeqAndData;
    }

    public void setACKedUnseenSegment(boolean acKedUnseenSegment) {
        this.ACKedUnseenSegment = acKedUnseenSegment;
    }

    public boolean getACKedUnseenSegment() {
        return ACKedUnseenSegment;
    }

    public void setRelativeSeqNum(long relativeSeqNum) {
        this.relativeSeqNum = relativeSeqNum;
    }

    public long getRelativeSeqNum() {
        return relativeSeqNum;
    }

    public void setRelativeAckNum(long relativeAckNum) {
        this.relativeAckNum = relativeAckNum;
    }

    public long getRelativeAckNum() {
        return relativeAckNum;
    }

    public void setLastNextExpSeqNum(long lastNextExpSeqNum) {
        this.lastNextExpSeqNum = lastNextExpSeqNum;
    }

    public long getLastNextExpSeqNum() {
        return lastNextExpSeqNum;
    }

    public void setPreviousSegmentNotCaptured(boolean previousSegmentNotCaptured) {
        this.previousSegmentNotCaptured = previousSegmentNotCaptured;
    }

    public boolean getPreviousSegmentNotCaptured() {
        return previousSegmentNotCaptured;
    }

    public Timestamp getTs() {
        return ts;
    }

    public void setTs(Timestamp ts) {
        this.ts = ts;
    }

    public void setDelta(Timestamp delta) {
        this.delta = delta;
    }

    public Timestamp getDelta() {
        return delta;
    }

    public void setDeltaFormatted(String deltaFormatted) {
        this.deltaFormatted = deltaFormatted;
    }

    public String getDeltaFormatted() {
        return deltaFormatted;
    }

    public long getLastRevAckNum() {
        return lastRevAckNum;
    }

    public void setLastRevAckNum(long lastRevAckNum) {
        this.lastRevAckNum = lastRevAckNum;
    }

    public void setIpId(int ipId) {
        this.ipId = ipId;
    }

    public int getIpId() {
        return ipId;
    }

    public void setMaxSeqNum(long maxSeqNum) {
        this.maxSeqNum = maxSeqNum;
    }

    public long getMaxSeqNum() {
        return maxSeqNum;
    }

    public void setOutOfOrder(boolean outOfOrder) {
        this.outOfOrder = outOfOrder;
    }

    public boolean getOutOfOrder() {
        return outOfOrder;
    }

    public void setAlgoFastRetransmission(boolean algoFastRetransmission) {
        this.algoFastRetransmission = algoFastRetransmission;
    }

    public boolean getAlgoFastRetransmission() {
        return algoFastRetransmission;
    }

    public int getRevDupAckCounter() {
        return revDupAckCounter;
    }

    public void setRevDupAckCounter(int revDupAckCounter) {
        this.revDupAckCounter = revDupAckCounter;
    }

    public void setAlgoSpuriousRetransmission(boolean algoSpuriousRetransmission) {
        this.algoSpuriousRetransmission = algoSpuriousRetransmission;
    }

    public boolean getAlgoSpuriousRetransmission() {
        return algoSpuriousRetransmission;
    }

    public void setAlgoRetransmission(boolean algoRetransmission) {
        this.algoRetransmission = algoRetransmission;
    }

    public boolean getAlgoRetransmission() {
        return algoRetransmission;
    }

    public void setLastSeenSeqNum(long lastSeenSeqNum) {
        this.lastSeenSeqNum = lastSeenSeqNum;
    }

    public long getLastSeenSeqNum() {
        return lastSeenSeqNum;
    }

    public void setLastSeenSegmentLength(long lastSeenSegmentLength) {
        this.lastSeenSegmentLength = lastSeenSegmentLength;
    }

    public long getLastSeenSegmentLength() {
        return lastSeenSegmentLength;
    }

    public void setPotentialRetransmission(boolean potentialRetransmission) {
        this.potentialRetransmission = potentialRetransmission;
    }

    public boolean getPotentialRetransmission() {
        return potentialRetransmission;
    }

    public void setPotentialRetransmissionOf(int potentialRetransmissionOf) {
        this.potentialRetransmissionOf = potentialRetransmissionOf;
    }

    public int getPotentialRetransmissionOf() {
        return potentialRetransmissionOf;
    }

    public boolean isLastAckArrivedAfter200ms() {
        return lastAckArrivedAfter200ms;
    }

    public void setLastAckArrivedAfter200ms(boolean lastAckArrivedAfter200ms) {
        this.lastAckArrivedAfter200ms = lastAckArrivedAfter200ms;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setArpMessage(String arpMessage) {
        this.arpMessage = arpMessage;
    }

    public String getArpMessage() {
        return arpMessage;
    }

    public void setMSS(int mss) {
        this.MSS = mss;
    }

    public int getMSS() {
        return MSS;
    }

    public void setTTL(int ttl) {
        this.TTL = ttl;
    }

    public int getTTL() {
        return TTL;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }

    public void setAbsoluteTime(String absoluteTime) {
        this.absoluteTime = absoluteTime;
    }

    public String getAbsoluteTime() {
        return absoluteTime;
    }

    public void setArpMessageResolved(String arpMessageResolved) {
        this.arpMessageResolved = arpMessageResolved;
    }

    public String getArpMessageResolved() {
        return arpMessageResolved;
    }

    public void setIcmpMessage(String icmPmessage) {
        this.icmpMessage = icmPmessage;
    }

    public String getIcmpMessage() {
        return icmpMessage;
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

    public void setDstIpAndPort(String dstIpAndPort) {
        this.dstIpAndPort = dstIpAndPort;
    }

    public String getDstIpAndPort() {
        return dstIpAndPort;
    }

    public boolean isLastSegArrivedWithin4ms() {
        return lastSegArrivedWithin4ms;
    }

    public void setLastSegArrivedWithin4ms(boolean lastSegArrivedWithin4ms) {
        this.lastSegArrivedWithin4ms = lastSegArrivedWithin4ms;
    }

    public void setRelativePacketNumber(int relativePacketNumber) {
        this.relativePacketNumber = relativePacketNumber;
    }

    public int getRelativePacketNumber() {
        return relativePacketNumber;
    }

    public String getRelativeTime() {
        return relativeTime;
    }

    public void setRelativeTime(String relativeTime) {
        this.relativeTime = relativeTime;
    }

    public int getTotalDataInStream() {
        return totalDataInStream;
    }

    public void setTotalDataInStream(int totalDataInStream) {
        this.totalDataInStream = totalDataInStream;
    }

    public void setTotalPacketsLost(int totalPacketsLost) {
        this.totalPacketsLost = totalPacketsLost;
    }

    public int getTotalPacketsLost() {
        return totalPacketsLost;
    }

    public void setTotalDupAcks(int totalDupAcks) {
        this.totalDupAcks = totalDupAcks;
    }

    public int getTotalDupAcks() {
        return totalDupAcks;
    }

    public void setTotalPacketsInStream(int totalPacketsInStream) {
        this.totalPacketsInStream = totalPacketsInStream;
    }

    public int getTotalPacketsInStream() {
        return totalPacketsInStream;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public void setUDPLength(int udpLength) {
        this.UDPLength = udpLength;
    }

    public int getUDPLength() {
        return UDPLength;
    }

    public void setDupAckFrameCountAbsolute(int dupAckFrameCountAbsolute) {
        this.dupAckFrameCountAbsolute = dupAckFrameCountAbsolute;
    }

    public int getDupAckFrameCountAbsolute() {
        return dupAckFrameCountAbsolute;
    }

    public void setTotalPacketsInDump(int totalPacketsInDump) {
        this.totalPacketsInDump = totalPacketsInDump;
    }

    public int getTotalPacketsInDump() {
        return totalPacketsInDump;
    }

    public void setNumberOfAnalysisFlags(int numberOfAnalysisFlags) {
        this.numberOfAnalysisFlags = numberOfAnalysisFlags;
    }

    public int getNumberOfAnalysisFlags() {
        return numberOfAnalysisFlags;
    }

    public void setAnalysisFlagMap(HashMap<Integer, List<String>> analysisFlagMap) {
        this.analysisFlagMap = analysisFlagMap;
    }

    public HashMap<Integer, List<String>> getAnalysisFlagMap() {
        return analysisFlagMap;
    }

    public void setFirstSegmentForDevice(boolean firstSegmentForDevice) {
        this.firstSegmentForDevice = firstSegmentForDevice;
    }

    public boolean getFirstSegmentForDevice() {
        return firstSegmentForDevice;
    }

    public void setBothSidesSeen(boolean bothSidesSeen) {
        this.bothSidesSeen = bothSidesSeen;
    }

    public boolean getBothSidesSeen() {
        return bothSidesSeen;
    }

    public void setLastRevNextExpectedSequenceNumber(long lastRevNextExpectedSequenceNumber) {
        this.lastRevNextExpectedSequenceNumber = lastRevNextExpectedSequenceNumber;
    }

    public long getLastRevNextExpectedSequenceNumber() {
        return lastRevNextExpectedSequenceNumber;
    }

    public boolean isPortUsed() {
        return portUsed;
    }

    public void setPortUsed(boolean portUsed) {
        this.portUsed = portUsed;
    }

    public void setAlgoReusedPorts(boolean algoReusedPorts) {
        this.algoReusedPorts = algoReusedPorts;
    }

    public boolean getAlgoReusedPorts() {
        return algoReusedPorts;
    }

    public void setRevCalculatedWinSize(long revCalculatedWinSize) {
        this.revCalculatedWinSize = revCalculatedWinSize;
    }

    public long getRevCalculatedWinSize() {
        return revCalculatedWinSize;
    }

    public void setRevWinScaling(int revWinScaling) {
        this.revWinScaling = revWinScaling;
    }

    public int getRevWinScaling() {
        return revWinScaling;
    }

    public void setTLSerror(String tlSerror) {
        this.TLSerror = tlSerror;
    }

    public String getTLSerror() {
        return TLSerror;
    }

    public void setHTTPmessage(String httPmessage) {
        this.HTTPmessage = httPmessage;
    }

    public String getHTTPmessage() {
        return HTTPmessage;
    }

    public void setCWR(boolean cwr) {
        this.CWR = cwr;
    }

    public boolean isCWR() {
        return CWR;
    }

    public void setECN(boolean ecn) {
        this.ECN = ecn;
    }

    public boolean isECN() {
        return ECN;
    }
}
