package org.henu.chess.common.messages;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.henu.chess.common.messages.request.CreateRoomRequest;
import org.henu.chess.common.messages.request.JoinRoomRequest;
import org.henu.chess.common.messages.request.MovePieceRequest;
import org.henu.chess.common.messages.response.*;

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
        jsonObject.remove("$className");

        if (className.equals(CreateRoomRequest.class.getName())) {
            return gson.fromJson(jsonObject, CreateRoomRequest.class);
        }

        if (className.equals(JoinRoomRequest.class.getName())) {
            return gson.fromJson(jsonObject, JoinRoomRequest.class);
        }

        if (className.equals(MovePieceRequest.class.getName())) {
            return gson.fromJson(jsonObject, MovePieceRequest.class);
        }

        if (className.equals(CheckMateResponse.class.getName())) {
            return gson.fromJson(jsonObject, CheckMateResponse.class);
        }

        if (className.equals(CreateRoomResponse.class.getName())) {
            return gson.fromJson(jsonObject, CreateRoomResponse.class);
        }

        if (className.equals(GameOverResponse.class.getName())) {
            return gson.fromJson(jsonObject, GameOverResponse.class);
        }

        if (className.equals(JoinRoomResponse.class.getName())) {
            return gson.fromJson(jsonObject, JoinRoomResponse.class);
        }

        if (className.equals(MovePieceResponse.class.getName())) {
            return gson.fromJson(jsonObject, MovePieceResponse.class);
        }

        if (className.equals(StartGameResponse.class.getName())) {
            return gson.fromJson(jsonObject, StartGameResponse.class);
        }

        throw new IllegalArgumentException("Unknown message type: " + className);
    }
}
