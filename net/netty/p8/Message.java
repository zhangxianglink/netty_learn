package net.netty.p8;

import java.io.Serializable;

/**
 * x.z
 * Create in 2023/12/28
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private byte messageType;
    private int sequenceId;

    private String message;

    public Message(byte messageType, int sequenceId, String message) {
        this.messageType = messageType;
        this.sequenceId = sequenceId;
        this.message = message;
    }

    public Message() {
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageType=" + messageType +
                ", sequenceId=" + sequenceId +
                ", message='" + message + '\'' +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public byte getMessageType() {
        return messageType;
    }

    public void setMessageType(byte messageType) {
        this.messageType = messageType;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }

}
