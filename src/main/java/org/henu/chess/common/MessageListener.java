package org.henu.chess.common;

import org.henu.chess.common.messages.Message;

import java.util.HashMap;

public class MessageListener {
    @SuppressWarnings("rawtypes")
    HashMap<String, TypedMessageHandler> handlers = new HashMap<>();

    void onMessage(Message message) {
        var className = message.getClass().getName();
        if (handlers.containsKey(message.getClass().getName())) {
            //noinspection unchecked
            handlers.get(className).handle(message);
        }
    }

    public <T extends Message> MessageListener on(Class<T> type, TypedMessageHandler<T> handler) {
        handlers.put(type.getName(), handler);
        return this;
    }

    public interface TypedMessageHandler<T extends Message> {
        void handle(T message);
    }
}
