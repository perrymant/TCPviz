package com.bath.tcpviz.dis.algorithms;

/*
* ConnectionTermination
* Set when: the FIN flag is set.
* */
public class ConnectionTermination {

    public boolean calculate1_activeClose(Segment segment) {
        if (!segment.isFin()) {
            return false;
        }
        return true;
    }
}
