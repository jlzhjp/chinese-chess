package io.github.jlzhjp.chess.common;

import io.github.jlzhjp.chess.common.messages.Message;
import io.github.jlzhjp.chess.common.socketManager.Sink;

public class MessageSink {
    Sink sink;

    public MessageSink(Sink sink) {
        this.sink = sink;
    }

    public void add(Message message) {
        sink.println(Message.toJsonString(message));
    }
}
