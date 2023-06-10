package edu.henu.chinesechess.common;

import edu.henu.chinesechess.common.messages.Message;
import edu.henu.chinesechess.common.socketManager.Sink;

public class MessageSink {
    Sink sink;

    public MessageSink(Sink sink) {
        this.sink = sink;
    }

    public void add(Message message) {
        sink.println(Message.toJsonString(message));
    }
}
