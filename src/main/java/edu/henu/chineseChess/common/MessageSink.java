package edu.henu.chineseChess.common;

import edu.henu.chineseChess.common.messages.Message;
import edu.henu.chineseChess.common.socketManager.Sink;

public class MessageSink {
    Sink sink;

    public MessageSink(Sink sink) {
        this.sink = sink;
    }

    public void add(Message message) {
        sink.println(Message.toJsonString(message));
    }
}
