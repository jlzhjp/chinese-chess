package org.henu.chess.common;

import org.henu.chess.common.messages.Message;
import org.henu.chess.common.messages.request.*;
import org.henu.chess.common.messages.response.*;

public abstract class MessageListener {
    public void onMessage(Message message) {
        if (message instanceof Request) {
            onRequest((Request) message);
        } else if (message instanceof Response) {
            onResponse((Response) message);
        }
    }

    public abstract void onRequest(Request request);

    public abstract void onResponse(Response response);
}
