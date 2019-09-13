package com.bath.tcpviz.dis.domain;

public class ViewLabel {
    private int stream;
    private String analysisMessage;
    private int absolutePacketNumber;
    private int relativePacketNumber;
    private String absoluteTime;
    private String relativeTime;
    private String deltaTime;
    private long absoluteSeqNum;
    private long relativeSeqNum;
    private long absoluteAckNum;
    private long relativeAckNum;
    private long segmentLength;
    private String seqAndDataAbsolute;
    private String sender;
    private int senderPort;
    private String receiver;
    private int receiverPort;
    private boolean SYN;
    private boolean PSH;
    private String MSS;
    private String protocol;
    private String analysisCSS;
    private String seqAndDataRelative;
    private String relativeWindowSize;
    private String calculatedWindowSize;
    private boolean direction;
    private String synMessage;
    private String notSynMessage;
    private String statsPacketsInStream;
    private String statsStreamHealth;
    private String udpMessage;
    private String messageTop;
    private String fileName;
    private long statsSeqNum;
    private String statsTime;
    private String analysisMessage2;
    private String analysisCSS2;
    private String messageTopRelative;
    private String messageTopAbsolute;
    private String messageBottomRelative;
    private String notSynMessageAbsolute;
    private String notSynMessageRelative;
    private String synMessageRelative;
    private String synMessageAbsolute;
    private String analysisMessage2Absolute;
    private String analysisMessageAbsolute;

    public String getAnalysisMessage() {
        return analysisMessage;
    }

    public void setAnalysisMessage(String analysisMessage) {
        this.analysisMessage = analysisMessage;
    }

    public void setAbsolutePacketNumber(int packetNumber) {
        this.absolutePacketNumber = packetNumber;
    }

    public int getAbsolutePacketNumber() {
        return absolutePacketNumber;
    }

    public void setSegmentLength(long segmentLength) {
        this.segmentLength = segmentLength;
    }

    public long getSegmentLength() {
        return segmentLength;
    }

    public void setAbsoluteSeqNum(long absoluteSeqNum) {
        this.absoluteSeqNum = absoluteSeqNum;
    }

    public long getAbsoluteSeqNum() {
        return absoluteSeqNum;
    }

    public void setAbsoluteAckNum(long absoluteAckNum) {
        this.absoluteAckNum = absoluteAckNum;
    }

    public long getAbsoluteAckNum() {
        return absoluteAckNum;
    }

    public void setSender(String origin) {
        this.sender = origin;
    }

    public String getSender() {
        return sender;
    }

    public void setRelativePacketNumber(int relativePacketNumber) {
        this.relativePacketNumber = relativePacketNumber;
    }

    public int getRelativePacketNumber() {
        return relativePacketNumber;
    }

    public void setPSH(boolean psh) {
        this.PSH = psh;
    }

    public boolean getPSH() {
        return PSH;
    }

    public void setCalculatedWindowSize(String calculatedWindowSize) {
        this.calculatedWindowSize = calculatedWindowSize;
    }

    public String getCalculatedWindowSize() {
        return calculatedWindowSize;
    }

    public void setMSS(String mss) {
        this.MSS = mss;
    }

    public String getMSS() {
        return MSS;
    }

    public void setRelativeAckNum(long relativeAckNum) {
        this.relativeAckNum = relativeAckNum;
    }

    public long getRelativeAckNum() {
        return relativeAckNum;
    }

    public void setRelativeSeqNum(long relativeSeqNum) {
        this.relativeSeqNum = relativeSeqNum;
    }

    public long getRelativeSeqNum() {
        return relativeSeqNum;
    }

    public void setAbsoluteTime(String absoluteTime) {
        this.absoluteTime = absoluteTime;
    }

    public String getAbsoluteTime() {
        return absoluteTime;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setStream(int stream) {
        this.stream = stream;
    }

    public int getStream() {
        return stream;
    }

    public void setAnalysisCSS(String analysisCSS) {
        this.analysisCSS = analysisCSS;
    }

    public String getAnalysisCSS() {
        return analysisCSS;
    }

    public void setRelativeTime(String relativeTime) {
        this.relativeTime = relativeTime;
    }

    public String getRelativeTime() {
        return relativeTime;
    }

    public void setDeltaTime(String deltaTime) {
        this.deltaTime = deltaTime;
    }

    public String getDeltaTime() {
        return deltaTime;
    }

    public void setRelativeWindowSize(String relativeWindowSize) {
        this.relativeWindowSize = relativeWindowSize;
    }

    public String getRelativeWindowSize() {
        return relativeWindowSize;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setSenderPort(int senderPort) {
        this.senderPort = senderPort;
    }

    public int getSenderPort() {
        return senderPort;
    }

    public void setReceiverPort(int receiverPort) {
        this.receiverPort = receiverPort;
    }

    public int getReceiverPort() {
        return receiverPort;
    }

    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    public boolean getDirection() {
        return direction;
    }

    public void setSYN(boolean syn) {
        this.SYN = syn;
    }

    public boolean getSYN() {
        return SYN;
    }

    public String getStatsPacketsInStream() {
        return statsPacketsInStream;
    }

    public void setStatsPacketsInStream(String statsPacketsInStream) {
        this.statsPacketsInStream = statsPacketsInStream;
    }

    public String getStatsStreamHealth() {
        return statsStreamHealth;
    }

    public void setStatsStreamHealth(String statsStreamHealth) {
        this.statsStreamHealth = statsStreamHealth;
    }

    public String getUdpMessage() {
        return udpMessage;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setStatsSeqNum(long statsSeqNum) {
        this.statsSeqNum = statsSeqNum;
    }

    public long getStatsSeqNum() {
        return statsSeqNum;
    }

    public void setStatsTime(String statsTime) {
        this.statsTime = statsTime;
    }

    public String getStatsTime() {
        return statsTime;
    }

    public void setAnalysisMessage2(String analysisMessage2) {
        this.analysisMessage2 = analysisMessage2;
    }

    public String getAnalysisMessage2() {
        return analysisMessage2;
    }

    public void setAnalysisCSS2(String analysisCSS2) {
        this.analysisCSS2 = analysisCSS2;
    }

    public String getAnalysisCSS2() {
        return analysisCSS2;
    }

    public void setMessageTopRelative(String messageTopRelative) {
        this.messageTopRelative = messageTopRelative;
    }

    public String getMessageTopRelative() {
        return messageTopRelative;
    }

    public void setMessageTopAbsolute(String messageTopAbsolute) {
        this.messageTopAbsolute = messageTopAbsolute;
    }

    public String getMessageTopAbsolute() {
        return messageTopAbsolute;
    }

    public void setMessageBottomRelative(String messageBottomRelative) {
        this.messageBottomRelative = messageBottomRelative;
    }

    public String getMessageBottomRelative() {
        return messageBottomRelative;
    }

    public void setNotSynMessageAbsolute(String notSynMessageAbsolute) {
        this.notSynMessageAbsolute = notSynMessageAbsolute;
    }

    public String getNotSynMessageAbsolute() {
        return notSynMessageAbsolute;
    }

    public void setNotSynMessageRelative(String notSynMessageRelative) {
        this.notSynMessageRelative = notSynMessageRelative;
    }

    public String getNotSynMessageRelative() {
        return notSynMessageRelative;
    }

    public void setSynMessageRelative(String synMessageRelative) {
        this.synMessageRelative = synMessageRelative;
    }

    public String getSynMessageRelative() {
        return synMessageRelative;
    }

    public void setSynMessageAbsolute(String synMessageAbsolute) {
        this.synMessageAbsolute = synMessageAbsolute;
    }

    public String getSynMessageAbsolute() {
        return synMessageAbsolute;
    }

    public void setAnalysisMessage2Absolute(String analysisMessage2Absolute) {
        this.analysisMessage2Absolute = analysisMessage2Absolute;
    }

    public String getAnalysisMessage2Absolute() {
        return analysisMessage2Absolute;
    }

    public void setAnalysisMessageAbsolute(String analysisMessageAbsolute) {
        this.analysisMessageAbsolute = analysisMessageAbsolute;
    }

    public String getAnalysisMessageAbsolute() {
        return analysisMessageAbsolute;
    }
}
