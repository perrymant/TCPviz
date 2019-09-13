package com.bath.tcpviz.dis.algorithms;

/**
 * TCP Previous segment not captured (common at capture start)
 * Set when the following are true:
 * - This is not an Out-of-Order packet.
 * - This is not the first segment for the device.
 * - The current Sequence Number is greater than the Last Next Expected Sequence Number.
 * - The current Sequence Number is not equal to the Last Reverse Acknowledgement Number.
 * - The RST flag is not set.
 * Set when there are no analysis flags and and for zero window probes.
 * <p>
 * LOST PACKET
 * If this segment is beyond the last seen nextseq we must have missed some previous segment
 * RST packets are not checked for this.
 * We only check for this if we have actually seen segments prior to this one.
 */
public class PreviousSegmentNotCaptured {
    public boolean calculate(Segment segment) {
        if (segment.getOutOfOrder()){
            return false;
        }
        if (segment.getFirstSegmentForDevice()){
            return false;
        }
        if(segment.getSeqNum() <= segment.getLastNextExpSeqNum()){
            return false;
        }
        if (segment.getLastRevAckNum() == segment.getSeqNum()){
            return false;
        }
        if (segment.isRst()) {
            return false;
        }
        return true;
    }
}
