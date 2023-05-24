package org.henu.chess.common.messages;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.henu.chess.common.messages.request.*;
import org.henu.chess.common.messages.response.*;
import org.henu.chess.common.model.ChessBoardPoint;

public class Message {
    private static final Gson gson = new Gson();

    public static String toJsonString(Message message) {
        JsonObject jsonObject = gson.toJsonTree(message).getAsJsonObject();
        jsonObject.addProperty("$className", message.getClass().getName());
        return jsonObject.toString();
    }

    public static Message parse(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

        String className = jsonObject.get("$className").getAsString();

        if (className.equals(CreateRoomRequest.class.getName())) {
            return new CreateRoomRequest();

        } else if (className.equals(CreateRoomResponse.class.getName())) {
            CreateRoomResponse createRoomResponse = new CreateRoomResponse();
            parsePartialResponse(jsonObject, createRoomResponse);
            createRoomResponse.setRoomID(jsonObject.get("roomID").getAsString());
            return createRoomResponse;

        } else if (className.equals(JoinRoomRequest.class.getName())) {
            JoinRoomRequest joinRoomRequest = new JoinRoomRequest();
            parsePartialRequest(jsonObject, joinRoomRequest);
            joinRoomRequest.setName(jsonObject.get("name").getAsString());
            return joinRoomRequest;

        } else if (className.equals(JoinRoomResponse.class.getName())) {
            JoinRoomResponse joinRoomResponse = new JoinRoomResponse();
            parsePartialResponse(jsonObject, joinRoomResponse);
            return joinRoomResponse;

        } else if (className.equals(MovePieceRequest.class.getName())) {
            MovePieceRequest movePieceRequest = new MovePieceRequest();
            parsePartialRequest(jsonObject, movePieceRequest);
            movePieceRequest.setFrom(parsePoint(jsonObject.get("from").getAsJsonObject()));
            movePieceRequest.setTo(parsePoint(jsonObject.get("to").getAsJsonObject()));
            return movePieceRequest;

        } else if (className.equals(MovePieceResponse.class.getName())) {
            MovePieceResponse movePieceResponse = new MovePieceResponse();
            parsePartialResponse(jsonObject, movePieceResponse);
            return movePieceResponse;

        } else if (className.equals(GameOverResponse.class.getName())) {
            GameOverResponse gameOverResponse = new GameOverResponse();
            parsePartialRequest(jsonObject, gameOverResponse);
            gameOverResponse.setWinner(jsonObject.get("winner").getAsString());
            return gameOverResponse;

        } else {
            throw new RuntimeException("Unknown message type: " + className);
        }
    }

    private static void parsePartialResponse(JsonObject jsonObject, Response response) {
        response.setMessage(jsonObject.get("message").getAsString());
        response.setResult(Result.valueOf(jsonObject.get("result").getAsString()));
    }

    private static void parsePartialRequest(JsonObject jsonObject, Request request) {
        request.setRoomID(jsonObject.get("roomID").getAsString());
    }

    private static ChessBoardPoint parsePoint(JsonObject jsonObject) {
        return new ChessBoardPoint(jsonObject.get("x").getAsInt(), jsonObject.get("y").getAsInt());
    }
}
