package com.bath.tcpviz.dis.algorithms;

/**
 * TCP ACKed unseen segment
 * Set when the following are true:
 * - Both sides have sent a packet.
 * - the current Acknowledgement Number is greater than the Max Reverse Sequence Number And Data.
 * <p>
 * Store the highest continuous seq number seen so far for 'max seq to be acked',
 * If this segment acks beyond the 'max seq to be acked' in the other direction
 * then that means we have missed packets going in the other direction
 * We only check this if we have actually seen some seq numbers in the other direction.
 */
public class ACKedUnseenSegment {
    public boolean calculate(Segment segment) {
        if (!segment.getBothSidesSeen()) {
            return false;
        }
        if (segment.getAckNum() <= segment.getMaxRevSeqAndData()) {
            return false;
        }
        return true;
    }
}