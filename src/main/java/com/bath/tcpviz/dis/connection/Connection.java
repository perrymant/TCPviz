package com.bath.tcpviz.dis.connection;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.validation.constraints.NotNull;

public class Connection {
    private final String sender;
    private final String receiver;

    public Connection(@NotNull String sender, @NotNull String receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    String getSender() {
        return sender;
    }

    String getReceiver() {
        return receiver;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Connection)) {
            return false;
        }
        Connection other = (Connection) obj;
        return this.sender.equals(other.sender) && this.receiver.equals(other.receiver)
                || this.sender.equals(other.receiver) && this.receiver.equals(other.sender);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(sender)
                .append(receiver)
                .toHashCode();
    }
}