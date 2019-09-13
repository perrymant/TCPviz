package com.bath.tcpviz.dis.connection;

import org.junit.Test;

import static org.junit.Assert.*;
public class ConnectionTest {

    private static final String EXPECTED_SENDER_IPV4 = "192.168.0.1:100";
    private static final String EXPECTED_RECEIVER_IPV4 = "255.0.0.1:64000";
    private static final String EXPECTED_SENDER_IPV6 = "2a02:c7d:8d9:ce00:dde3:9aa1:dad8:db55:63512";
    private static final String EXPECTED_RECEIVER_IPV6 = "2a00:1450:400c:c09::bc:5228";

    @Test
    public void canRetrieveConstructorParametersAgain_IPv4() {
        Connection connection = new Connection(EXPECTED_SENDER_IPV4, EXPECTED_RECEIVER_IPV4);
        assertEquals(EXPECTED_SENDER_IPV4, connection.getSender());
        assertEquals(EXPECTED_RECEIVER_IPV4, connection.getReceiver());
    }

    @Test
    public void identicalObjectsAreEqual_IPv4() {
        Connection connection1 = new Connection(EXPECTED_SENDER_IPV4, EXPECTED_RECEIVER_IPV4);
        Connection connection2 = new Connection(EXPECTED_SENDER_IPV4, EXPECTED_RECEIVER_IPV4);
        assertEquals(connection1, connection2);
    }

    @Test
    public void swappedSenderAndReceiverAreInSameConversation_IPv4() {
        Connection connection1 = new Connection(EXPECTED_SENDER_IPV4, EXPECTED_RECEIVER_IPV4);
        Connection connection2 = new Connection(EXPECTED_RECEIVER_IPV4, EXPECTED_SENDER_IPV4);
        assertEquals(connection1, connection2);
    }
    @Test
    public void canRetrieveConstructorParametersAgain_IPv6() {
        Connection connection = new Connection(EXPECTED_SENDER_IPV6, EXPECTED_RECEIVER_IPV6);
        assertEquals(EXPECTED_SENDER_IPV6, connection.getSender());
        assertEquals(EXPECTED_RECEIVER_IPV6, connection.getReceiver());
    }

    @Test
    public void identicalObjectsAreEqual_IPv6() {
        Connection connection1 = new Connection(EXPECTED_SENDER_IPV6, EXPECTED_RECEIVER_IPV6);
        Connection connection2 = new Connection(EXPECTED_SENDER_IPV6, EXPECTED_RECEIVER_IPV6);
        assertEquals(connection1, connection2);
    }

    @Test
    public void swappedSenderAndReceiverAreInSameConversation_IPv6() {
        Connection connection1 = new Connection(EXPECTED_SENDER_IPV6, EXPECTED_RECEIVER_IPV6);
        Connection connection2 = new Connection(EXPECTED_RECEIVER_IPV6, EXPECTED_SENDER_IPV6);
        assertEquals(connection1, connection2);
    }


}
