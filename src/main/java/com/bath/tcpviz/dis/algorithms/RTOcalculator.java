package com.bath.tcpviz.dis.algorithms;

/*RTO calculator based on RFC 6298 section 2

To compute the current RTO, a TCP sender maintains two state
variables, SRTT (smoothed round-trip time) and RTTVAR (round-trip
time variation).  In addition, we assume a clock granularity of G
seconds.

The rules governing the computation of SRTT, RTTVAR, and RTO are as
   follows:

   (2.1) Until a round-trip time (RTT) measurement has been made for a
         segment sent between the sender and receiver, the sender SHOULD
         set RTO <- 1 second, though the "backing off" on repeated
         retransmission discussed in (5.5) still applies.

         Note that the previous version of this document used an initial
         RTO of 3 seconds [PA00].  A TCP implementation MAY still use
         this value (or any other value > 1 second).  This change in the
         lower bound on the initial RTO is discussed in further detail
         in Appendix A.

   (2.2) When the first RTT measurement R is made, the host MUST set

            SRTT <- R
            RTTVAR <- R/2
            RTO <- SRTT + max (G, K*RTTVAR)

         where K = 4.

   (2.3) When a subsequent RTT measurement R' is made, a host MUST set

            RTTVAR <- (1 - beta) * RTTVAR + beta * |SRTT - R'|
            SRTT <- (1 - alpha) * SRTT + alpha * R'

         The value of SRTT used in the update to RTTVAR is its value
         before updating SRTT itself using the second assignment.  That
         is, updating RTTVAR and SRTT MUST be computed in the above
         order.

         The above SHOULD be computed using alpha=1/8 and beta=1/4 (as
         suggested in [JK88]).

         After the computation, a host MUST update
         RTO <- SRTT + max (G, K*RTTVAR)

   (2.4) Whenever RTO is computed, if it is less than 1 second, then the
         RTO SHOULD be rounded up to 1 second.
*/

public class RTOcalculator {
    private final long CLOCK_GRANULARITY_MS = 1000;
    private final long K_FACTOR = 4;
    private final long ALPHA_FACTOR = 125;
    private final long BETA_FACTOR = 250;

    private static long srtt = 0;
    private static long rttvar = 0;
    private static long rto = 1000;

    public long updateRTO(long measurement) {
        if ((srtt == 0) || (rttvar == 0)) {
            srtt = measurement;
            rttvar = measurement / 2;
        } else {
            rttvar = (((1000 - BETA_FACTOR) * rttvar) / 1000) + ((BETA_FACTOR * Math.abs(srtt - measurement)) / 1000);
            srtt = (((1000 - ALPHA_FACTOR) * srtt) / 1000) + ((ALPHA_FACTOR * measurement) / 1000);
        }
        rto = srtt + Math.max(CLOCK_GRANULARITY_MS, (K_FACTOR * rttvar));
        if (rto < 1000) {
            rto = 1000;
        }
        return rto;
    }

    public long getRTO() {
        return rto;
    }

    // see RFC6298 section 7
    public void resetRTO(boolean syn_ack_lost) {
        srtt = 0;
        rttvar = 0;
        rto = syn_ack_lost ? 3000 : 1000;
    }
}


