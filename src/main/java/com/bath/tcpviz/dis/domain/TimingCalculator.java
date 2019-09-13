package com.bath.tcpviz.dis.domain;

import com.bath.tcpviz.dis.algorithms.Segment;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

class TimingCalculator {

    List<Segment> deltaCalculation(List<Segment> segments) {
        for (Segment segment : segments) {
            Timestamp diffInMilliseconds = diff(segment.getTs(), segment.getPrev().getTs());
            segment.setDelta(diffInMilliseconds);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("s.SSSSSS"); // add conditional if longer than 9.99sec
            String deltaFormatted = diffInMilliseconds.toLocalDateTime().format(formatter);
            segment.setDeltaFormatted(deltaFormatted);
        }
        return segments;
    }

    private Timestamp diff(java.util.Date t1, java.util.Date t2) {
        if (t1.compareTo(t2) < 0) {
            java.util.Date tmp = t1;
            t1 = t2;
            t2 = tmp;
        }

        long diffSeconds = (t1.getTime() / 1000) - (t2.getTime() / 1000);
        int nano1 = ((int) t1.getTime() % 1000) * 1000000;
        if (t1 instanceof Timestamp)
            nano1 = ((Timestamp) t1).getNanos();
        int nano2 = ((int) t2.getTime() % 1000) * 1000000;
        if (t2 instanceof Timestamp)
            nano2 = ((Timestamp) t2).getNanos();

        int diffNanos = nano1 - nano2;
        if (diffNanos < 0) {
            diffSeconds--;
            diffNanos += 1000000000;
        }

        Timestamp result = new Timestamp((diffSeconds * 1000) + (diffNanos / 1000000));
        result.setNanos(diffNanos);
        return result;
    }

    List<Segment> calculateDupAckArrivalTimeForFastRetransmission(List<Segment> segments) {
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        algorithmConverter.calculateClientAndServer(segments);
        String client = algorithmConverter.client;
        String server = algorithmConverter.server;
        long dupAckTimingInterval_20ms = 200000000;
        boolean dupAckNotYetSeenClient = true;
        boolean dupAckNotYetSeenServer = true;
        int lastClientDupAckPacketNumber = 0;
        int lastServerDupAckPacketNumber = 0;

        for (Segment segment : segments) {
            if (segment.getSrcIpAndPort().equals(client)) {
                if (segment.getDupAckCounter() > 2) {
                    lastClientDupAckPacketNumber = segment.getRelativePacketNumber();
                    dupAckNotYetSeenClient = false;
                }
                if (!dupAckNotYetSeenServer) {
                    Timestamp timeDifference = diff(segment.getTs(), segments.get(lastServerDupAckPacketNumber - 1).getTs());
                    if (timeDifference.getNanos() < dupAckTimingInterval_20ms) {
                        segment.setLastDupAckArrivedWithin20ms(true);
                    }
                }
            } else if (segment.getSrcIpAndPort().equals(server)) {
                if (segment.getDupAckCounter() > 2) {
                    lastServerDupAckPacketNumber = segment.getRelativePacketNumber();
                    dupAckNotYetSeenServer = false;
                }
                if (!dupAckNotYetSeenClient) {
                    Timestamp timeDifference = diff(segment.getTs(), segments.get(lastClientDupAckPacketNumber - 1).getTs());
                    if (timeDifference.getNanos() < dupAckTimingInterval_20ms) {
                        segment.setLastDupAckArrivedWithin20ms(true);
                    }
                }
            }
        }
        return segments;
    }

    List<Segment> calculateRetransmissionTime(List<Segment> segments) {
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        algorithmConverter.calculateClientAndServer(segments);
        String client = algorithmConverter.client;
        String server = algorithmConverter.server;
        long retransmissionTimingInterval_200ms = 200000000;
        boolean ackSeenClient = false;
        boolean ackSeenServer = false;
        int lastClientAckPacketNumber = 0;
        int lastServerAckPacketNumber = 0;

        for (Segment segment : segments) {
            if (segment.getSrcIpAndPort().equals(client)) {
                if (ackSeenServer) {
                    Timestamp timeDifference = diff(segment.getTs(), segments.get(lastServerAckPacketNumber - 1).getTs());
                    if (timeDifference.getNanos() < retransmissionTimingInterval_200ms) {
                        segment.setLastAckArrivedAfter200ms(true);
                    }
                }
                if (!ackSeenServer) {
                    lastServerAckPacketNumber = segment.getRelativePacketNumber();
                    ackSeenServer = true;
                }
            } else if (segment.getSrcIpAndPort().equals(server)) {
                if (ackSeenClient) {
                    Timestamp timeDifference = diff(segment.getTs(), segments.get(lastClientAckPacketNumber - 1).getTs());
                    if (timeDifference.getNanos() < retransmissionTimingInterval_200ms) {
                        segment.setLastDupAckArrivedWithin20ms(true);
                    }
                }
                if (!ackSeenClient) {
                    lastClientAckPacketNumber = segment.getRelativePacketNumber();
                    ackSeenClient = true;
                }
            }
        }
        return segments;
    }


    List<Segment> calculateLastSegArrivedWithin3ms(List<Segment> segments) {
        //setLastSegArrivedWithin4ms
        //for OoO: UPDATED TO 4 MS
        AlgorithmConverter algorithmConverter = new AlgorithmConverter();
        algorithmConverter.calculateClientAndServer(segments);
        String client = algorithmConverter.client;
        String server = algorithmConverter.server;
        long defaultRTT_3ms = 4000000;
        boolean lastSegNotYetSeenClient = true;
        boolean lastSegNotYetSeenServer = true;
        int lastClientSegNumPacketNumber = 0;
        int lastServerSegNumPacketNumber = 0;

        for (Segment segment : segments) {
            if (segment.getSrcIpAndPort().equals(client)) {
                if (lastSegNotYetSeenServer) {
                    lastClientSegNumPacketNumber = segment.getRelativePacketNumber();
                    lastSegNotYetSeenClient = false;
                }
                if (!lastSegNotYetSeenServer) {
                    Timestamp timeDifference = diff(segment.getTs(), segments.get(lastServerSegNumPacketNumber - 1).getTs());
                    if (timeDifference.getNanos() < defaultRTT_3ms) {
                        segment.setLastSegArrivedWithin4ms(true);
                    }
                }
            } else if (segment.getSrcIpAndPort().equals(server)) {
                if (lastSegNotYetSeenClient) {
                    lastServerSegNumPacketNumber = segment.getRelativePacketNumber();
                    lastSegNotYetSeenServer = false;
                }
                if (!lastSegNotYetSeenClient) {
                    Timestamp timeDifference = diff(segment.getTs(), segments.get(lastClientSegNumPacketNumber - 1).getTs());
                    if (timeDifference.getNanos() < defaultRTT_3ms) {
                        segment.setLastSegArrivedWithin4ms(true);
                    }
                }
            }
        }
        return segments;
    }

    List<Segment> calculateRelativeTime(List<Segment> segments) {
        List<Segment> result = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSS");
        Timestamp initialTime = segments.get(0).getTs();
        Timestamp toSubtract = Timestamp.valueOf("2000-01-01 00:00:00.000000");
        Timestamp diff2 = diff(initialTime, toSubtract);
        for (Segment segment : segments) {
            Timestamp diff = diff(segment.getTs(), diff2);
            String relativeTimeFormatted = diff.toLocalDateTime().format(formatter);
            segment.setRelativeTime(relativeTimeFormatted);
            result.add(segment);
        }
        return result;
    }

    double convertTimeToLong(String timestamp) {
        String formattedTime = "2000-01-01 " + timestamp;
        long timeZero = 946684800000L;
        Timestamp timestampFormatted = Timestamp.valueOf(formattedTime);
        long timeAsLong = timestampFormatted.getTime() - timeZero;
        long seconds = timeAsLong / 1000;
        double nanos = (double) timestampFormatted.getNanos() / 1000000000;
        return (seconds + nanos);
    }
}