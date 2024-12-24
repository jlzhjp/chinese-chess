package io.github.jlzhjp.chess.common.messages.response;

import io.github.jlzhjp.chess.common.messages.Message;
import io.github.jlzhjp.chess.common.messages.Result;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Response response = (Response) o;
        return result == response.result && Objects.equals(message, response.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(result, message);
    }

    @Override
    public String toString() {
        return "Response{" +
                "result=" + result +
                ", message='" + message + '\'' +
                '}';
    }
}
