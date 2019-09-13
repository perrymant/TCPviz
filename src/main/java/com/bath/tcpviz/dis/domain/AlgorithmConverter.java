package com.bath.tcpviz.dis.domain;

import com.bath.tcpviz.dis.algorithms.*;
import com.bath.tcpviz.dis.algorithms.retransmits.FastRetransmission;
import com.bath.tcpviz.dis.algorithms.retransmits.OutOfOrder;
import com.bath.tcpviz.dis.algorithms.retransmits.Retransmission;
import com.bath.tcpviz.dis.algorithms.retransmits.SpuriousRetransmission;

import java.util.*;

public class AlgorithmConverter {
    String client;
    public String server;
    private boolean direction;
    private PreviousSegmentNotCaptured previousSegmentNotCaptured = new PreviousSegmentNotCaptured();
    private ConnectionEstablish connectionEstablish = new ConnectionEstablish();
    private ReusedPorts reusedPorts = new ReusedPorts();
    private ZeroWindow zeroWindow = new ZeroWindow();
    private KeepAlive keepAlive = new KeepAlive();
    private WindowUpdate windowUpdate = new WindowUpdate();
    private DupAck dupAck = new DupAck();
    private KeepAliveAck keepAliveAck = new KeepAliveAck();
    private WindowFull windowFull = new WindowFull();
    private ZeroWindowProbe zeroWindowProbe = new ZeroWindowProbe();
    private ZeroWindowProbeAck zeroWindowProbeAck = new ZeroWindowProbeAck();
    private ACKedUnseenSegment aCKedUnseenSegment = new ACKedUnseenSegment();
    private OutOfOrder outOfOrder = new OutOfOrder();
    private FastRetransmission fastRetransmission = new FastRetransmission();
    private SpuriousRetransmission spuriousRetransmission = new SpuriousRetransmission();
    private Retransmission retransmission = new Retransmission();

    List<Segment> applyEstablishConnection(List<Segment> convertedSegments) {
        List<Segment> result = new ArrayList<>();
        for (Segment convertedSegment : convertedSegments) {
            boolean calculate1ActiveOpen = connectionEstablish.calculate1_activeOpen(convertedSegment);
            convertedSegment.setAlgoSyn1(calculate1ActiveOpen);
            boolean calculate2PassiveOpen = connectionEstablish.calculate2_passiveOpen(convertedSegment);
            convertedSegment.setAlgoSyn2(calculate2PassiveOpen);
            boolean calculate3HandshakeComplete = connectionEstablish.calculate3_handshakeComplete(convertedSegment);
            convertedSegment.setAlgoSyn3(calculate3HandshakeComplete);
            result.add(convertedSegment);
        }
        return result;
    }

    List<Segment> applyReusedPorts(List<Segment> convertedSegments) {
        List<Segment> result = new ArrayList<>();
        HashSet<String> setClient = new HashSet<>();
        HashSet<String> setServer = new HashSet<>();
        for (Segment convertedSegment : convertedSegments) {
            if (!setClient.contains(convertedSegment.getSrcIpAndPort())) {
                setClient.add(convertedSegment.getSrcIpAndPort());
                convertedSegment.setAlgoReusedPorts(false);
            } else if (!setServer.contains(convertedSegment.getDstIpAndPort())) {
                setServer.add(convertedSegment.getDstIpAndPort());
                convertedSegment.setAlgoReusedPorts(false);
            } else {
                convertedSegment.setPortUsed(true);
                boolean calculateReusedPorts = reusedPorts.calculate(convertedSegment);
                convertedSegment.setAlgoReusedPorts(calculateReusedPorts);
            }
            result.add(convertedSegment);
        }
        return result;
    }

    List<Segment> applyKeepAlive(List<Segment> convertedSegments) {
        long packetCounter = 0;
        List<Segment> result = new ArrayList<>();
        List<Segment> segmentsLastNextExpSeqNum = calculateLastNextExpSeqNum(convertedSegments);
        List<Segment> segmentsLastReverseProperties = calculateLastReverseProperties(segmentsLastNextExpSeqNum);
        for (Segment segment : segmentsLastReverseProperties) {
            boolean calculateKeepAlive = keepAlive.calculate(segment);
            segment.setAlgoKeepAlive(calculateKeepAlive);
            // distinguish between KeepAlive and DupAck:
            if (calculateKeepAlive || (segment.getSeqNum() == (segment.getLastRevAckNum() - 1)) && packetCounter == 0) {
                segment.setAlgoKeepAlive(true);
                packetCounter = segment.getRelativePacketNumber();
            }
            if (segment.getRelativePacketNumber() == packetCounter + 2) {
                if (segment.getSeqNum() == (segment.getLastRevAckNum() - 1)) {
                    segment.setAlgoKeepAlive(true);
                }
                packetCounter = 0;
            }
            // Remove conflict with Reused Ports
            if (segment.getAlgoReusedPorts() || segment.isSyn() || segment.isFin()) {
                segment.setAlgoKeepAlive(false);
            }
            result.add(segment);
        }
        return result;
    }

    List<Segment> applyKeepAliveAck(List<Segment> segments) {
        List<Segment> result = new ArrayList<>();
        List<Segment> applyDupAck = applyDupAck(segments);
        List<Segment> applyKeepAlive = applyKeepAlive(applyDupAck);
        List<Segment> segmentsWithLSAN = calculateLastSeenAckNum(applyKeepAlive);
        List<Segment> lastSeenMachineProperties = calculateLastSeenMachineProperties(segmentsWithLSAN);
        List<Segment> segmentsRevKA = calculateIsRevKeepAlive(lastSeenMachineProperties);
        for (Segment segment : segmentsRevKA) {
            boolean calculateKeepAliveAck = keepAliveAck.calculate(segment);
            segment.setAlgoKeepAliveAck(calculateKeepAliveAck);
            result.add(segment);
        }
        return result;
    }

    List<Segment> applyWindowUpdate(List<Segment> convertedSegment) {
        List<Segment> result = new ArrayList<>();
        List<Segment> segments = calculateLastSeenMachineProperties(convertedSegment);
        for (Segment segment : segments) {
            boolean calculateWindowUpdate = windowUpdate.calculate(segment);
            segment.setAlgoWindowUpdate(calculateWindowUpdate);
            result.add(segment);
        }
        return result;
    }

    List<Segment> applyZeroWindow(List<Segment> convertedSegment) {
        List<Segment> result = new ArrayList<>();
        for (Segment segment : convertedSegment) {
            boolean calculateZeroWindow = zeroWindow.calculate(segment);
            segment.setAlgoZeroWindow(calculateZeroWindow);
            result.add(segment);
        }
        return result;
    }

    List<Segment> applyZeroWindowProbe(List<Segment> segments) {
        List<Segment> result = new ArrayList<>();
        for (Segment segment : segments) {
            boolean calculateZeroWindowProbe = zeroWindowProbe.calculate(segment);
            segment.setAlgoZeroWindowProbe(calculateZeroWindowProbe);
            result.add(segment);
        }
        return result;
    }

    List<Segment> applyAlgoWindowFull(List<Segment> segments) {
        List<Segment> result = new ArrayList<>();
        List<Segment> segments1 = calculateWindowScalingAndSize(segments);
        List<Segment> segments2 = calculateReverseWindowProperties(segments1);
        List<Segment> segments3 = calculateLastSeenAckNum(segments2);
        List<Segment> segments4 = calculateLastReverseProperties(segments3);
        List<Segment> segments5 = calculateLastNextExpSeqNum(segments4);
        List<Segment> segments6 = calculateBytesInFlight(segments5);
        for (Segment segment : segments6) {
            //heuristic: stats on BIF, if avg under 65536 and errors over 65536, set winscale to 0?.
        }
        for (Segment segment : segments6) {
            // segment.setWindowScaling(0);
            boolean calculateWindowFull = windowFull.calculate(segment);
            segment.setAlgoWindowFull(calculateWindowFull);
            result.add(segment);
        }
        return result;
    }

    List<Segment> applyACKedUnseenSegment(List<Segment> segments) {
        List<Segment> result = new ArrayList<>();
        List<Segment> storeMaxRevSeqNum = storeMaxRevSeqAndData(segments);
        List<Segment> bothSidesSeen = initialSeqNumAndCalculateRelativeSeqAndAckNum(storeMaxRevSeqNum);
        for (Segment segment : bothSidesSeen) {
            boolean calculateACKedUnseenSegment = aCKedUnseenSegment.calculate(segment);
            segment.setACKedUnseenSegment(calculateACKedUnseenSegment);
            result.add(segment);
        }
        return result;
    }

    List<Segment> applyPreviousSegmentNotCaptured(List<Segment> segments) {
        List<Segment> result = new ArrayList<>();
        List<Segment> calculateLastNextExpSeqNum = calculateLastNextExpSeqNum(segments);
        for (Segment segment : calculateLastNextExpSeqNum) {
            boolean calculatePreviousSegmentNotCaptured = previousSegmentNotCaptured.calculate(segment);
            segment.setPreviousSegmentNotCaptured(calculatePreviousSegmentNotCaptured);
            if (calculatePreviousSegmentNotCaptured && segment.getAlgoDupAck()) {
                segment.setAlgoDupAck(false);
            }
            result.add(segment);
        }
        return result;
    }

    List<Segment> applyDupAck(List<Segment> segments) {
        List<Segment> result = new ArrayList<>();
        List<Segment> segmentWithLastSeenMachineProperties = calculateLastSeenMachineProperties(segments);
        for (Segment segment : segmentWithLastSeenMachineProperties) {
            direction = segment.getSrcIpAndPort().equals(client);
            boolean calculateDupAck = dupAck.calculate(segment, direction);
            segment.setAlgoDupAck(calculateDupAck);
            result.add(segment);
        }
        return result;
    }

    List<Segment> applySpuriousRetransmission(List<Segment> segments) {
        List<Segment> result = new ArrayList<>();
        List<Segment> segments1 = calculateLastNextExpSeqNum(segments);
        List<Segment> segments2 = calculateLastSeenMachineProperties(segments1);
        List<Segment> segments3 = calculateLastReverseProperties(segments2);
        for (Segment segment : segments3) {
            boolean calculateSpuriousRetransmission = spuriousRetransmission.calculate(segment);
            segment.setAlgoSpuriousRetransmission(calculateSpuriousRetransmission);
            result.add(segment);
        }
        return result;
    }

    List<Segment> applyRetransmission(List<Segment> segments) {
        List<Segment> result = new ArrayList<>();
        List<Segment> segments1 = calculateLastNextExpSeqNum(segments);
        List<Segment> segments2 = calculateLastSeenMachineProperties(segments1);
        List<Segment> segments3 = calculateLastReverseProperties(segments2);
        List<Segment> segments4 = storeMaxSeqNum(segments3);
        List<Segment> segments5 = calculateSeqNumAlreadySeenForRetransmission(segments4);
        List<Segment> segments6 = applySpuriousRetransmission(segments5);
        for (Segment segment : segments6) {
            boolean calculateRetransmission = retransmission.calculate(segment);
            segment.setAlgoRetransmission(calculateRetransmission);
            result.add(segment);
        }
        return result;
    }

    List<Segment> applyFastRetransmission(List<Segment> segments) {
        List<Segment> result = new ArrayList<>();
        List<Segment> segmentsWithDupAck = applyDupAck(segments);
        List<Segment> segments1 = calculateLastNextExpSeqNum(segmentsWithDupAck);
        List<Segment> segments2 = calculateLastSeenMachineProperties(segments1);
        List<Segment> segments3 = calculateLastReverseProperties(segments2);
        TimingCalculator tc = new TimingCalculator();
        List<Segment> segments4 = tc.calculateDupAckArrivalTimeForFastRetransmission(segments3);
        for (Segment segment : segments4) {
            boolean calculateFastRetransmission = fastRetransmission.calculate(segment);
            segment.setAlgoFastRetransmission(calculateFastRetransmission);
            result.add(segment);
        }
        return result;
    }

    List<Segment> applyOutOfOrder(List<Segment> segments) {
        List<Segment> result = new ArrayList<>();
        List<Segment> segmentsMaxSeqNum = storeMaxSeqNum(segments);
        TimingCalculator tc = new TimingCalculator();
        List<Segment> segments4 = tc.calculateLastSegArrivedWithin3ms(segmentsMaxSeqNum);
        for (Segment segment : segments4) {
            boolean calculateOutOfOrder = outOfOrder.calculate(segment);
            segment.setOutOfOrder(calculateOutOfOrder);
            result.add(segment);
        }
        return result;
    }

    List<Segment> applyZeroWindowProbeAck(List<Segment> segments) {
        List<Segment> result = new ArrayList<>();
        List<Segment> segments1 = calculateLastSeenMachineProperties(segments);
        List<Segment> segments2 = calculateWindowScalingAndSize(segments1);
        //
        List<Segment> segments3 = calculateReverseWindowProperties(segments2);
        List<Segment> applyZeroWindowProbe = applyZeroWindowProbe(segments3);
        List<Segment> segments4 = calculateRevLastSeenZeroWinProbe(applyZeroWindowProbe);
        for (Segment segment : segments4) {
            boolean calculateZeroWindowProbeAck = zeroWindowProbeAck.calculate(segment);
            segment.setAlgoZeroWindowProbeAck(calculateZeroWindowProbeAck);
            result.add(segment);
        }
        return result;
    }

    List<Segment> applyAllAnalysisAlgorithms(List<Segment> segments) {
        TimingCalculator tc = new TimingCalculator();
        List<Segment> allSegments;
        if (isDoubleSidedConversation(segments)) {
            calculateClientAndServer(segments);
            List<Segment> calculateRelativeTime = tc.calculateRelativeTime(segments);
            List<Segment> calculateWindowScalingAndSize = calculateWindowScalingAndSize(calculateRelativeTime);
            List<Segment> segmentsWithDeltaTiming = calculateDeltaTiming(calculateWindowScalingAndSize);
            List<Segment> segmentsWithRelativeAckAndSeq = initialSeqNumAndCalculateRelativeSeqAndAckNum(segmentsWithDeltaTiming);
            List<Segment> applyReusedPorts = applyReusedPorts(segmentsWithRelativeAckAndSeq);
            List<Segment> applyEstablishConnection = applyEstablishConnection(applyReusedPorts);
            List<Segment> applyKeepAlive = applyKeepAlive(applyEstablishConnection);
            List<Segment> applyKeepAliveAck = applyKeepAliveAck(applyKeepAlive);
            List<Segment> applyACKedUnseenSegment = applyACKedUnseenSegment(applyKeepAliveAck);
            List<Segment> applyDupAck = applyDupAck(applyACKedUnseenSegment);
            List<Segment> applyWindowUpdate = applyWindowUpdate(applyDupAck);
            List<Segment> applyAlgoWindowFull = applyAlgoWindowFull(applyWindowUpdate);
            List<Segment> applyZeroWindow = applyZeroWindow(applyAlgoWindowFull);
            List<Segment> applyZeroWindowProbe = applyZeroWindowProbe(applyZeroWindow);
            List<Segment> applyZeroWindowProbeAck = applyZeroWindowProbeAck(applyZeroWindowProbe);
            List<Segment> applySpuriousRetransmission = applySpuriousRetransmission(applyZeroWindowProbeAck);
            List<Segment> applyFastRetransmission = applyFastRetransmission(applySpuriousRetransmission);
            List<Segment> applyOutOfOrder = applyOutOfOrder(applyFastRetransmission);
            List<Segment> applyRetransmission = applyRetransmission(applyOutOfOrder);
            List<Segment> applyPreviousSegmentNotCaptured = applyPreviousSegmentNotCaptured(applyRetransmission);
            allSegments = calculateStatistics(applyPreviousSegmentNotCaptured);
        } else {
            List<Segment> calculateRelativeTime = tc.calculateRelativeTime(segments);
            allSegments = calculateStatistics(calculateRelativeTime);
        }
        return allSegments;
    }

    private boolean isDoubleSidedConversation(List<Segment> segments) {
        client = segments.get(0).getSrcIpAndPort();
        Set<String> machines = new HashSet<>();
        for (Segment segment : segments) {
            machines.add(segment.getSrcIpAndPort());
        }
        machines.remove(client);
        return machines.toArray().length != 0;
    }

    // Client if:
    //  1. first SYN
    //  2. Port Number >= 1024
    //  3. Private IP range :
    //   (10.0.0.0 – 10.255.255.255, 172.16.0.0 – 172.31.255.255, 192.168.0.0 - 192.168.255.255)
    void calculateClientAndServer(List<Segment> segments) {
        client = segments.get(0).getSrcIpAndPort();
        Set<String> machines = new HashSet<>();
        for (Segment segment : segments) {
            machines.add(segment.getSrcIpAndPort());
        }
        machines.remove(client);
        if (machines.toArray().length == 0) {
            server = segments.get(0).getDstIpAndPort();
        } else {
            server = (String) machines.toArray()[0];
        }
    }

    List<Segment> calculateIsRevKeepAlive(List<Segment> segments) {
        int lastSeenServerPacketNumber = -1;
        int lastSeenClientPacketNumber = -1;

        for (int i = 0; i < segments.size(); i++) {
            if (segments.get(i).getSrcIpAndPort().equals(client)) {
                if (lastSeenServerPacketNumber != -1) {
                    segments.get(i).setRevKeepAlive(segments.get(lastSeenServerPacketNumber).isAlgoKeepAlive());
                }
                lastSeenClientPacketNumber = i;
            }
            if (segments.get(i).getSrcIpAndPort().equals(server)) {
                if (lastSeenServerPacketNumber != -1) {
                    segments.get(i).setRevKeepAlive(segments.get(lastSeenClientPacketNumber).isAlgoKeepAlive());
                }
                lastSeenServerPacketNumber = i;
            }
        }
        return segments;
    }

    List<Segment> calculateBytesInFlight(List<Segment> segments) {
        for (Segment segment : segments) {
            if (!segment.isSyn() && !segment.isFin() && !segment.isRst()) {
                long bytesInFlight = (segment.getNextExpSeqNum() - segment.getLastSeenAckNum());
                segment.setBytesInFlight(bytesInFlight);
            }
        }
        return segments;
    }

    List<Segment> calculateLastSeenAckNum(List<Segment> segments) {
        long lastSeenAckNumForClient = -1;
        long lastSeenAckNumForServer = -1;
        boolean firstSegment = true;

        for (Segment segment : segments) {
            if (segment.getSrcIpAndPort().equals(client)) {
                if (firstSegment) {
                    segment.setLastSeenAckNum(-1);
                    lastSeenAckNumForClient = segment.getAckNum();
                } else {
                    if (segment.getAckNum() > lastSeenAckNumForClient) {
                        lastSeenAckNumForClient = segment.getAckNum();
                    }
                    segment.setLastSeenAckNum(lastSeenAckNumForServer);
                }
            }
            if (segment.getSrcIpAndPort().equals(server)) {
                if (firstSegment) {
                    segment.setLastSeenAckNum(-1);
                    lastSeenAckNumForServer = segment.getAckNum();
                } else {
                    if (segment.getAckNum() > lastSeenAckNumForServer) {
                        lastSeenAckNumForServer = segment.getAckNum();
                    }
                    segment.setLastSeenAckNum(lastSeenAckNumForClient);
                }
            }
            firstSegment = false;
        }
        return segments;
    }

    List<Segment> storeMaxRevSeqAndData(List<Segment> segments) {
        long maxSeqAndDataForClient = -1;
        long maxSeqAndDataForServer = -1;
        boolean firstClientSegment = true;
        boolean firstServerSegment = true;
        for (Segment segment : segments) {
            if (segment.getSrcIpAndPort().equals(client)) {
                if (firstClientSegment) {
                    maxSeqAndDataForClient = segment.getSeqNum() + segment.getSegDataSize();
                } else {
                    if ((segment.getSeqNum() + segment.getSegDataSize()) > maxSeqAndDataForClient) {
                        maxSeqAndDataForClient = segment.getSeqNum() + segment.getSegDataSize();
                    }
                }
                segment.setMaxRevSeqAndData(maxSeqAndDataForServer);
                firstClientSegment = false;
            }
            if (segment.getSrcIpAndPort().equals(server)) {
                if (firstServerSegment) {
                    maxSeqAndDataForServer = segment.getSeqNum() + segment.getSegDataSize();
                } else {
                    if ((segment.getSeqNum() + segment.getSegDataSize()) > maxSeqAndDataForServer) {
                        maxSeqAndDataForServer = segment.getSeqNum() + segment.getSegDataSize();
                    }
                }
                segment.setMaxRevSeqAndData(maxSeqAndDataForClient);
                firstServerSegment = false;
            }
        }
        return segments;
    }

    List<Segment> initialSeqNumAndCalculateRelativeSeqAndAckNum(List<Segment> segments) {
        boolean firstClientSegment = true;
        boolean serverSeen = false;
        long initialClientSeqNum;
        long initialServerSeqNum;
        long initialClientAckNum;
        long initialServerAckNum;
        if (segments.get(0).isSyn() && !segments.get(0).isAck() && segments.size() >= 2) {
            initialClientSeqNum = segments.get(0).getSeqNum();
            initialClientAckNum = segments.get(1).getSeqNum();
            initialServerSeqNum = segments.get(1).getSeqNum();
            initialServerAckNum = segments.get(0).getSeqNum();
        } else {
            initialClientSeqNum = segments.get(0).getSeqNum() - 1;
            initialClientAckNum = segments.get(0).getAckNum() - 1;
            initialServerSeqNum = segments.get(0).getAckNum() - 1;
            initialServerAckNum = segments.get(0).getSeqNum() - 1;
        }
        for (Segment segment : segments) {
            if (segment.getSrcIpAndPort().equals(client)) {
                if (firstClientSegment) {

                    if (segment.isSyn() && !segment.isAck()) {
                        segment.setRelativeAckNum(0);
                    } else {
                        segment.setRelativeSeqNum(mod32bit(segment.getSeqNum() - initialClientSeqNum));
                        segment.setRelativeAckNum(mod32bit(segment.getAckNum() - initialClientAckNum));
                    }
                    firstClientSegment = false;
                } else {
                    segment.setRelativeSeqNum(mod32bit(segment.getSeqNum() - initialClientSeqNum));
                    segment.setRelativeAckNum(mod32bit(segment.getAckNum() - initialClientAckNum));
                }
            }
            if (segment.getSrcIpAndPort().equals(server)) {
                serverSeen = true;
                segment.setRelativeSeqNum(mod32bit(segment.getSeqNum() - initialServerSeqNum));
                segment.setRelativeAckNum(mod32bit(segment.getAckNum() - initialServerAckNum));
            }
            if (!firstClientSegment && serverSeen) {
                segment.setBothSidesSeen(true);
            }
        }
        return segments;
    }

    private long mod32bit(long x) {
        long result = x % 4294967296L;
        if (result < 0) {
            result += 4294967296L;
        }
        return result;
    }

    List<Segment> calculateDeltaTiming(List<Segment> segments) {
        TimingCalculator timingCalculator = new TimingCalculator();
        return timingCalculator.deltaCalculation(segments);
    }

    private List<Segment> storeMaxSeqNum(List<Segment> segments) {
        long maxSeqNumForClient = -1;
        long maxSeqNumForServer = -1;
        boolean firstClientSegment = true;
        boolean firstServerSegment = true;
        for (Segment segment : segments) {
            if (segment.getSrcIpAndPort().equals(client)) {
                if (firstClientSegment) {
                    maxSeqNumForClient = segment.getSeqNum();
                } else {
                    if (segment.getSeqNum() > maxSeqNumForClient) {
                        maxSeqNumForClient = segment.getSeqNum();
                    }
                }
                segment.setMaxSeqNum(maxSeqNumForClient);
                firstClientSegment = false;
            }
            if (segment.getSrcIpAndPort().equals(server)) {
                if (firstServerSegment) {
                    maxSeqNumForServer = segment.getSeqNum();
                } else {
                    if (segment.getSeqNum() > maxSeqNumForServer) {
                        maxSeqNumForServer = segment.getSeqNum();
                    }
                }
                segment.setMaxSeqNum(maxSeqNumForServer);
                firstServerSegment = false;
            }
        }
        return segments;
    }

    private List<Segment> calculateLastNextExpSeqNum(List<Segment> segments) {
        long lastNextExpSeqNumClient = -1;
        long lastNextExpSeqNumServer = -1;
        boolean firstClient = true;
        boolean firstServer = true;

        for (Segment segment : segments) {
            if (segment.getSrcIpAndPort().equals(client)) {
                if (firstClient) {
                    segment.setFirstSegmentForDevice(true);
                    lastNextExpSeqNumClient = segment.getNextExpSeqNum();
                } else {
                    segment.setLastNextExpSeqNum(lastNextExpSeqNumClient);
                    lastNextExpSeqNumClient = segment.getNextExpSeqNum();
                }
                firstClient = false;
            }
            if (segment.getSrcIpAndPort().equals(server)) {
                if (firstServer) {
                    segment.setFirstSegmentForDevice(true);
                    lastNextExpSeqNumServer = segment.getNextExpSeqNum();
                } else {
                    segment.setLastNextExpSeqNum(lastNextExpSeqNumServer);
                    lastNextExpSeqNumServer = segment.getNextExpSeqNum();
                }
                firstServer = false;
            }
        }
        return segments;
    }

    private List<Segment> calculateLastSeenMachineProperties(List<Segment> segments) {
        boolean firstClient = true;
        boolean firstServer = true;
        //WinSize
        long lastSeenWinSizeServer = 0;
        long lastSeenWinSizeClient = 0;
        //AckNum
        long lastSeenAckNumServer = 0;
        long lastSeenAckNumClient = 0;
        // data size
        long lastSeenSegmentLengthClient = 0;
        long lastSeenSegmentLengthServer = 0;
        //seqnum
        long lastSeenSeqNumClient = 0;
        long lastSeenSeqNumServer = 0;

        for (Segment segment : segments) {
            if (segment.getSrcIpAndPort().equals(client)) {
                if (firstClient) {
                    segment.setFirstSegmentForDevice(true);
                    lastSeenWinSizeClient = segment.getWinSize();
                    lastSeenAckNumClient = segment.getAckNum();
                    lastSeenSegmentLengthClient = segment.getSegDataSize();
                    lastSeenSeqNumClient = segment.getSeqNum();
                } else {
                    segment.setLastSeenWinSize(lastSeenWinSizeClient);
                    lastSeenWinSizeClient = segment.getWinSize();
                    segment.setLastSeenAckNum(lastSeenAckNumClient);
                    lastSeenAckNumClient = segment.getAckNum();
                    segment.setLastSeenSegmentLength(lastSeenSegmentLengthClient);
                    lastSeenSegmentLengthClient = segment.getSegDataSize();
                    segment.setLastSeenSeqNum(lastSeenSeqNumClient);
                    lastSeenSeqNumClient = segment.getSeqNum();
                }
                firstClient = false;
            }
            if (segment.getSrcIpAndPort().equals(server)) {
                if (firstServer) {
                    segment.setFirstSegmentForDevice(true);
                    lastSeenWinSizeServer = segment.getWinSize();
                    lastSeenAckNumServer = segment.getAckNum();
                    lastSeenSegmentLengthServer = segment.getSegDataSize();
                    lastSeenSeqNumServer = segment.getSeqNum();
                } else {
                    segment.setLastSeenWinSize(lastSeenWinSizeServer);
                    lastSeenWinSizeServer = segment.getWinSize();
                    segment.setLastSeenAckNum(lastSeenAckNumServer);
                    lastSeenAckNumServer = segment.getAckNum();
                    segment.setLastSeenSegmentLength(lastSeenSegmentLengthServer);
                    lastSeenSegmentLengthServer = segment.getSegDataSize();
                    segment.setLastSeenSeqNum(lastSeenSeqNumServer);
                    lastSeenSeqNumServer = segment.getSeqNum();
                }
                firstServer = false;
            }
        }
        return segments;
    }

    private List<Segment> calculateLastReverseProperties(List<Segment> segments) {
        int lastSeenClientDupAckCount = 0;
        int lastSeenServerDupAckCount = 0;
        long lastSeenClientAckNum = 0;
        long lastSeenServerAckNum = 0;
        long lastRevClientNextExpectedSequenceNumber = 0;
        long lastRevServerNextExpectedSequenceNumber = 0;

        for (Segment segment : segments) {
            if (segment.getSrcIpAndPort().equals(client)) {
                segment.setRevDupAckCounter(lastSeenServerDupAckCount);
                lastSeenClientDupAckCount = segment.getDupAckCounter();
                segment.setLastRevAckNum(lastSeenServerAckNum);
                lastSeenClientAckNum = segment.getAckNum();
                segment.setLastRevNextExpectedSequenceNumber(lastRevServerNextExpectedSequenceNumber);
                lastRevClientNextExpectedSequenceNumber = segment.getNextExpSeqNum();
            } else {
                segment.setRevDupAckCounter(lastSeenClientDupAckCount);
                lastSeenServerDupAckCount = segment.getDupAckCounter();
                segment.setLastRevAckNum(lastSeenClientAckNum);
                lastSeenServerAckNum = segment.getAckNum();
                segment.setLastRevNextExpectedSequenceNumber(lastRevClientNextExpectedSequenceNumber);
                lastRevServerNextExpectedSequenceNumber = segment.getNextExpSeqNum();
            }
        }
        return segments;
    }

    private List<Segment> calculateSeqNumAlreadySeenForRetransmission(List<Segment> segments) {
        Map<Long, Integer> mapClientRelative = new HashMap<>();
        Map<Long, Integer> mapServerRelative = new HashMap<>();

        for (Segment segment : segments) {
            long seqNum = segment.getSeqNum();
            if (segment.getSrcIpAndPort().equals(client) && segment.getSegDataSize() != 0) {
                if (mapClientRelative.containsKey(seqNum) && segments.get(mapClientRelative.get(seqNum)).getSegDataSize() != 0) {
                    segment.setPotentialRetransmission(true);
                    segment.setPotentialRetransmissionOf(mapClientRelative.get(seqNum));
                } else {
                    mapClientRelative.put(seqNum, segment.getRelativePacketNumber());
                }
            }
            if (segment.getSrcIpAndPort().equals(server) && segment.getSegDataSize() != 0) {
                if (mapServerRelative.containsKey(seqNum) && segments.get(mapServerRelative.get(seqNum)).getSegDataSize() != 0) {
                    segment.setPotentialRetransmission(true);
                    segment.setPotentialRetransmissionOf(mapServerRelative.get(seqNum));
                } else {
                    mapServerRelative.put(seqNum, segment.getRelativePacketNumber());
                }
            }
        }
        return segments;
    }

    List<Segment> calculateReverseWindowProperties(List<Segment> segments) {
        int lastSeenServerPacketNumber = -1;
        int lastSeenClientPacketNumber = -1;
        boolean clientSeen = false;
        boolean serverSeen = false;

        for (int i = 0; i < segments.size(); i++) {
            if (segments.get(i).getSrcIpAndPort().equals(client)) {
                if (!serverSeen) {
                    segments.get(i).setFirstSegmentForDevice(true);
                    segments.get(i).setRevWinSize(0);
                    segments.get(i).setRevCalculatedWinSize(0);
                    segments.get(i).setRevWinScaling(-2);
                } else {
                    segments.get(i).setRevWinSize(segments.get(lastSeenServerPacketNumber).getWinSize());
                    segments.get(i).setRevCalculatedWinSize(segments.get(lastSeenServerPacketNumber).getCalculatedWindowSize());
                    segments.get(i).setRevWinScaling(segments.get(lastSeenServerPacketNumber).getWindowScaling());

                }
                lastSeenClientPacketNumber = i;
                clientSeen = true;
            }
            if (segments.get(i).getSrcIpAndPort().equals(server)) {
                if (!clientSeen) {
                    segments.get(i).setFirstSegmentForDevice(true);
                    segments.get(i).setRevWinSize(0);
                    segments.get(i).setRevCalculatedWinSize(0);
                    segments.get(i).setRevWinScaling(-2);
                } else {
                    segments.get(i).setRevWinSize(segments.get(lastSeenClientPacketNumber).getWinSize());
                    segments.get(i).setRevCalculatedWinSize(segments.get(lastSeenClientPacketNumber).getCalculatedWindowSize());
                    segments.get(i).setRevWinScaling(segments.get(lastSeenClientPacketNumber).getWindowScaling());

                }
                lastSeenServerPacketNumber = i;
                serverSeen = true;
            }
        }
        return segments;
    }

    private List<Segment> calculateRevLastSeenZeroWinProbe(List<Segment> segments) {
        int lastSeenServerPacketNumber = -1;
        int lastSeenClientPacketNumber = -1;
        int clientSeen = -1;
        int serverSeen = -1;
        for (int i = 0; i < segments.size(); i++) {
            if (segments.get(i).getSrcIpAndPort().equals(client)) {
                if (serverSeen == -1) {
                    lastSeenClientPacketNumber = i;
                } else {
                    segments.get(i).setRevLastSeenZeroWinProbe(segments.get(lastSeenServerPacketNumber).getAlgoZeroWindowProbe());
                    lastSeenClientPacketNumber = i;
                }
                clientSeen = 0;
            }
            if (segments.get(i).getSrcIpAndPort().equals(server)) {
                if (clientSeen == -1) {
                    lastSeenServerPacketNumber = i;
                } else {
                    segments.get(i).setRevLastSeenZeroWinProbe(segments.get(lastSeenClientPacketNumber).getAlgoZeroWindowProbe());
                    lastSeenServerPacketNumber = i;
                }
                serverSeen = 0;
            }
        }
        return segments;
    }

    private List<Segment> calculateStatistics(List<Segment> segments) {
        List<Segment> result = new ArrayList<>();
        int totalPacketsInStream = segments.size();
        int totalData = 0;
        int totalPacketsLost = 0; //PreviousSegmentNotCaptured
        int totalDupAcks = 0;
        for (Segment segment : segments) {
            totalData += segment.getSegDataSize();
            totalPacketsLost += segment.getPreviousSegmentNotCaptured() ? 1 : 0;
            totalDupAcks += segment.getAlgoDupAck() ? 1 : 0;
            segment.setAnalysisFlagMap(getNumberOfAnalysisFlags(segment));
            segment.setNumberOfAnalysisFlags((Integer) segment.getAnalysisFlagMap().keySet().toArray()[0]);
        }
        int health = (int) Math.round(100.0 - ((double) totalPacketsLost / (double) totalPacketsInStream) * 100.0);
        for (Segment segment : segments) {
            segment.setTotalPacketsInStream(totalPacketsInStream);
            segment.setTotalDataInStream(totalData);
            segment.setTotalPacketsLost(totalPacketsLost);
            segment.setTotalDupAcks(totalDupAcks);
            segment.setHealth(health);
            result.add(segment);
        }
        return result;
    }

    private HashMap<Integer, List<String>> getNumberOfAnalysisFlags(Segment segment) {
        int numberOfAnalysisFlags = 0;
        List<String> flagList = new ArrayList<>();
        if (segment.getAlgoSyn1()) {
            numberOfAnalysisFlags += 1;
            flagList.add("syn1");
        }
        if (segment.getAlgoSyn2()) {
            numberOfAnalysisFlags += 1;
            flagList.add("syn2");
        }
        if (segment.getAlgoSyn3()) {
            numberOfAnalysisFlags += 1;
            flagList.add("syn3");
        }
        if (segment.getAlgoReusedPorts()) {
            numberOfAnalysisFlags += 1;
            flagList.add("reused");
        }
        if (segment.isRst()) {
            numberOfAnalysisFlags += 1;
            flagList.add("rst");
        }
        if (segment.isFin()) {
            numberOfAnalysisFlags += 1;
            flagList.add("fin");
        }
        if (segment.isAlgoWindowUpdate()) {
            numberOfAnalysisFlags += 1;
            flagList.add("windowUpdate");
        }
        if (segment.isAlgoWindowFull()) {
            numberOfAnalysisFlags += 1;
            flagList.add("windowFull");
        }
        if (segment.getACKedUnseenSegment()) {
            numberOfAnalysisFlags += 1;
            flagList.add("ackUnseenSegment");
        }
        if (segment.getAlgoDupAck()) {
            numberOfAnalysisFlags += 1;
            flagList.add("dupAck");
        }
        if (segment.getAlgoZeroWindow()) {
            numberOfAnalysisFlags += 1;
            flagList.add("zeroWindow");
        }
        if (segment.getAlgoZeroWindowProbe()) {
            numberOfAnalysisFlags += 1;
            flagList.add("zeroWindowProbe");
        }
        if (segment.getAlgoZeroWindowProbeAck()) {
            numberOfAnalysisFlags += 1;
            flagList.add("zeroWindowProbeAck");
        }
        if (segment.isAlgoKeepAlive()) {
            numberOfAnalysisFlags += 1;
            flagList.add("keepAlive");
        }
        if (segment.getAlgoKeepAliveAck()) {
            numberOfAnalysisFlags += 1;
            flagList.add("keepAliveAck");
        }
        if (segment.getAlgoSpuriousRetransmission()) {
            numberOfAnalysisFlags += 1;
            flagList.add("spuriousRetransmission");
        }
        if (segment.getAlgoFastRetransmission()) {
            numberOfAnalysisFlags += 1;
            flagList.add("fastRetransmission");
        }
        if (segment.getOutOfOrder()) {
            numberOfAnalysisFlags += 1;
            flagList.add("outOfOrder");
        }
        if (segment.getAlgoRetransmission()) {
            numberOfAnalysisFlags += 1;
            flagList.add("retransmission");
        }
        if (segment.getPreviousSegmentNotCaptured()) {
            numberOfAnalysisFlags += 1;
            flagList.add("previousSegmentNotCaptured");
        }
        HashMap<Integer, List<String>> map = new HashMap<>();
        map.put(numberOfAnalysisFlags, flagList);
        return map;
    }

    List<Segment> calculateWindowScalingAndSize(List<Segment> convertedSegments) {
        int windowScalingClient = -2;
        int windowScalingServer = -2;
        boolean firstClientSegment = true;
        boolean firstServerSegment = true;
        for (Segment segment : convertedSegments) {
            if (segment.getSrcIpAndPort().equals(client)) {
                if (firstClientSegment) {
                    windowScalingClient = segment.getWindowScaling();
                    segment.setCalculatedWindowSize(segment.getWinSize());
                    firstClientSegment = false;
                } else {
                    segment.setWindowScaling(windowScalingClient);
                    if (windowScalingClient < 1) {
                        segment.setCalculatedWindowSize(segment.getWinSize());
                    } else if (windowScalingClient == 1) {
                        segment.setCalculatedWindowSize(segment.getWinSize() * 2);
                    } else {
                        segment.setCalculatedWindowSize(segment.getWinSize() * (long) Math.pow(2, windowScalingClient));
                    }
                }
            }
            if (segment.getSrcIpAndPort().equals(server)) {
                if (firstServerSegment) {
                    windowScalingServer = segment.getWindowScaling();
                    segment.setCalculatedWindowSize(segment.getWinSize());
                    firstServerSegment = false;
                } else {
                    segment.setWindowScaling(windowScalingServer);
                    if (windowScalingServer < 1) {
                        segment.setCalculatedWindowSize(segment.getWinSize());
                    } else if (windowScalingServer == 1) {
                        segment.setCalculatedWindowSize(segment.getWinSize() * 2);
                    } else {
                        segment.setCalculatedWindowSize(segment.getWinSize() * (long) Math.pow(2, windowScalingServer));
                    }
                }
            }
        }
        return convertedSegments;
    }
}