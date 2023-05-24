package org.henu.chess.common.messages.response;

import org.henu.chess.common.messages.Message;
import org.henu.chess.common.messages.Result;

public class Response extends Message {
    private Result result;

    private String message;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
