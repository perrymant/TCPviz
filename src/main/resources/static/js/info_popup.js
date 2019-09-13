$(".dupAck").popover({
    trigger: 'hover',
    placement: "right",
    title: "Duplicate Acknowledgement",
    content: "DupAcks occur when a device receives more than one acknowledgment with the same sequence number, and is used as part of the Fast Retransmist and Recovery strategy in an attempt to reduce the time the sender waits before retransmitting lost segments. When a device receives three DupAcks, they can be quite confident that a packet was lost, and they will initiate a Fast Retransmission segment."
});

$(".appData").popover({
    trigger: 'hover',
    placement: "right",
    title: "Data transfer",
    content: "When all is going well on a TCP connection, data will be passed from one device to another"
});

$(".windowUpdate").popover({
    trigger: 'hover',
    placement: "right",
    title: "winupdate",
    content: "A Window Update analysis flag can be seen when the acknowledgement number is the same as the the previous, but the window size has changed."
});

$(".syn1").popover({
    trigger: 'hover',
    placement: "right",
    title: "Establish Connection",
    content: "To establish a reliable and connection-oriented data stream, TCP begins with a three-way handshake. This initial segment contains a SYN flag, the initial sequence number, as well as the window size and scaling factor and the Maximum Segment Size. Additional options can also be sent."
});

$(".syn2").popover({
    trigger: 'hover',
    placement: "right",
    title: "Establish Connection",
    content: "When the first SYN segment is received by the other device (usually a server), the server will respond with a segment that has its SYN and ACK flag set. It will also communicate information about its window size and window scaling, as well as its Maximum Segment Size and Options."
});

$(".syn3").popover({
    trigger: 'hover',
    placement: "right",
    title: "Establish Connection",
    content: "The three-way handshake is concluded with this ACK segment. The two devices have now established a reliable connection."
});

$(".connectionTermination").popover({
    trigger: 'hover',
    placement: "right",
    title: "Terminate Connection",
    content: "To close the data stream in an orderly manner, TCP finishes with a pair of FIN ACK control bits."
});

$(".rst").popover({
    trigger: 'hover',
    placement: "right",
    title: "Reset Connection",
    content: "RFC-793 Section 3.5.: The RST control bit indicates that the connection should be terminated immediately. "
});

$(".windowFull").popover({
    trigger: 'hover',
    placement: "right",
    title: "Window Full",
    content: "This analysis flag is seen when the Bytes In Flight (the number of unacknowledged bytes) exceeds the calculated window size."
});

$(".zeroWindow").popover({
    trigger: 'hover',
    placement: "right",
    title: "Zero Window",
    content: "When a devices' buffer fills up, because the application isn't processing the data fast enough, it needs to advertise that it cannot accept more data - so the window size will drop to zero."
});

$(".zeroWindowProbe").popover({
    trigger: 'hover',
    placement: "right",
    title: "Zero Window Probe",
    content: "After a Zero Window is seen, the other device might send out a Zero Window Probe to check if the window size is no longer zero - at which point data transfer can continue."
});

$(".zeroWindowProbeAck").popover({
    trigger: 'hover',
    placement: "right",
    title: "Zero Window Probe ACK",
    content: "After a Zero Window Probe is seen, the other device will send out a Zero Window Probe ACK to confirm it has seen the probe, and to indicate that the device still has a zero window and that no data transfer should take place."
});

$(".keepAlive").popover({
    trigger: 'hover',
    placement: "right",
    title: "Keep-Alive",
    content: "If a device hasn't received a segment for a determined time (based on the Keep-Alive timer), it will send out a Keep-Alive segment to check the other device is still responding, and will await a Keep-Alive ACK segment."
});

$(".keepAliveAck").popover({
    trigger: 'hover',
    placement: "right",
    title: "Keep-Alive ACK",
    content: "A Keep-Alive ACK segment is given as a response to a Keep-Alive segment, it indicates that the device should keep the connection alive."
});

$(".spuriousRetransmission").popover({
    trigger: 'hover',
    placement: "right",
    title: "Spurious Retransmission",
    content: "If the current sequence number plus segment length is less than or equal to the last ACK, the packet contains duplicate data and may be considered spurious."
});

$(".fastRetransmission").popover({
    trigger: 'hover',
    placement: "right",
    title: "Fast Retransmission",
    content: "If there were more than two duplicate ACKs in the reverse direction and if this sequence number matches those ACKs and if the packet occurs within 20ms of the last duplicate ACK then this is a fast retransmission"
});

$(".outOfOrder").popover({
    trigger: 'hover',
    placement: "right",
    title: "Out-of-Order",
    content: "If the segment came relatively close since the segment with the highest seen sequence number and it doesn't look like a retransmission then it is an Out-of-Order segment."
});

    $(".retransmission").popover({
    trigger: 'hover',
    placement: "right",
    title: "Retransmission",
    content: "If data sent has not been acknowledged within a certain time frame (normally based on a multiple of RTT), the data is retransmitted to the remote host. The data is repackaged and the packets might not be identical to the original packets."
});

$(".previousSegmentNotCaptured").popover({
    trigger: 'hover',
    placement: "right",
    title: "Previous Segment Not Captured",
    content: "A Lost Packet is assumed because the segments' sequence numbers have jumped, leaving a hole of sequence number values."
});

$(".ackUnseenSegment").popover({
    trigger: 'hover',
    placement: "right",
    title: "ACKed unseen segment",
    content: "This segment's acknowledgement number is higher than expected, which should be the previous segment from the other device's sequence number plus segment length (the number of bytes of data sent)."
});

$(".tlsError").popover({
    trigger: 'hover',
    placement: "right",
    title: "TLS Message",
    content: "Transport Layer Security is a cryptographic protocol, offering an encrypted channel of communication. It requires a reliable transport layer protocol, which will often be TCP."
});

$(".httpMessage").popover({
    trigger: 'hover',
    placement: "right",
    title: "HTTP GET Request",
    content: "HTTP GET Request: A resource is requested."
});

$(".goodAck").popover({
    trigger: 'hover',
    placement: "right",
    title: "Acknowledgement",
    content: "RFC-793 Section 3.1.: The acknowledgement is the value of the next sequence number the sender of the segment is expecting to receive."
});

$(".ackOoo").popover({
    trigger: 'hover',
    placement: "right",
    title: "Acknowledgement Out-of-Order",
    content: "This ACK is out of order."
});

$(".badAck").popover({
    trigger: 'hover',
    placement: "right",
    title: "Acknowledgement",
    content: "There is an issue with this ACK."
});

$("#numberSystemA").popover({
    trigger: 'hover',
    placement: "bottom",
    title: "Switch to Relative Numbering",
    content: "Switch between Absolute and Relative time, sequence and acknowledgement numbers."
});

$("#numberSystemR").popover({
    trigger: 'hover',
    placement: "bottom",
    title: "Switch to Absolute Numbering",
    content: "Switch between Absolute and Relative time, sequence and acknowledgement numbers."
});

$(".chartToggleInfo").popover({
    trigger: 'hover',
    placement: "bottom",
    title: "Charts",
    content: "The charts display sequence number against time. Ideal data transfer results in a line with a positive trend."
});

$(".submitInfo").popover({
    trigger: 'hover',
    placement: "right",
    title: "Uploading a PCAP file",
    content: "Large files might take a while to process, so ideally keep your file within 2MB for best results. You can use Wireshark to convert PCAPNG files to PCAP. No loading feature is provided, and erroneous files redirect to the home page."
});

$(".navigateInfo").popover({
    trigger: 'hover',
    placement: "bottom",
    title: "Stream Navigation",
    content: "Use these tools to navigate between Streams in the PCAP file."
});
