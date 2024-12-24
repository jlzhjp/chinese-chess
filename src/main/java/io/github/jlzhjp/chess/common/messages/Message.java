package io.github.jlzhjp.chess.common.messages;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

        try {
            Class<?> klass = Class.forName(className);
            return (Message) gson.fromJson(jsonObject, klass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
