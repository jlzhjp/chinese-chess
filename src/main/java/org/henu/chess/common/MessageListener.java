package org.henu.chess.common;

import org.henu.chess.common.messages.Message;
import org.henu.chess.common.messages.request.*;
import org.henu.chess.common.messages.response.*;

public class MessageListener {
    public void onMessage(Message message) {
        if (message instanceof Request) {
            onRequest((Request) message);
        } else if (message instanceof Response) {
            onResponse((Response) message);
        }
    }

    public void onRequest(Request request) {
    }

    public void onResponse(Response response) {
    }
}
