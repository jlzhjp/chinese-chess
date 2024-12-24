import io.github.jlzhjp.chess.common.messages.Message;
import io.github.jlzhjp.chess.common.messages.Result;
import io.github.jlzhjp.chess.common.messages.request.CreateRoomRequest;
import io.github.jlzhjp.chess.common.messages.request.JoinRoomRequest;
import io.github.jlzhjp.chess.common.messages.request.MovePieceRequest;
import io.github.jlzhjp.chess.common.messages.response.*;
import io.github.jlzhjp.chess.common.model.ChessBoardPoint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MessageParsingTest {
    @Test
    public void testCreateRoomRequestParsing() {
        CreateRoomRequest request = new CreateRoomRequest();
        request.setRoomID("TestRoom");
        request.setUserName("TestUser");

        String json = Message.toJsonString(request);
        System.out.println(json);

        Message parsedRequest = Message.parse(json);
        System.out.println(parsedRequest);

        Assertions.assertTrue(parsedRequest instanceof CreateRoomRequest);
        Assertions.assertEquals(request, parsedRequest);
    }

    @Test
    public void testJoinRoomRequestParsing() {
        JoinRoomRequest request = new JoinRoomRequest();
        request.setRoomID("TestRoom");
        request.setUserName("TestUser");

        String json = Message.toJsonString(request);
        System.out.println(json);

        Message parsedRequest = Message.parse(json);
        System.out.println(parsedRequest);

        Assertions.assertTrue(parsedRequest instanceof JoinRoomRequest);
        Assertions.assertEquals(request, parsedRequest);
    }

    @Test
    public void testMovePieceRequestParsing() {
        MovePieceRequest request = new MovePieceRequest();
        request.setRoomID("TestRoom");
        request.setUserName("TestUser");
        request.setFrom(new ChessBoardPoint(1, 1));
        request.setTo(new ChessBoardPoint(2, 2));

        String json = Message.toJsonString(request);
        System.out.println(json);

        Message parsedRequest = Message.parse(json);

        System.out.println(parsedRequest);

        Assertions.assertTrue(parsedRequest instanceof MovePieceRequest);
        Assertions.assertEquals(request, parsedRequest);
    }

    @Test
    public void testCreateRoomResponseParsing() {
        CreateRoomResponse response = new CreateRoomResponse();
        response.setMessage("TestMessage");
        response.setResult(Result.ERROR);
        response.setRoomID("TestRoom");

        String json = Message.toJsonString(response);
        System.out.println(json);

        Message parsedResponse = Message.parse(json);
        System.out.println(parsedResponse);

        Assertions.assertTrue(parsedResponse instanceof CreateRoomResponse);
        Assertions.assertEquals(response, parsedResponse);
    }

    @Test
    public void testGameOverResponseParsing() {
        GameOverResponse response = new GameOverResponse();
        response.setMessage("TestMessage");
        response.setResult(Result.ERROR);
        response.setWinner("TestUser");

        String json = Message.toJsonString(response);
        System.out.println(json);

        Message parsedResponse = Message.parse(json);
        System.out.println(parsedResponse);

        Assertions.assertTrue(parsedResponse instanceof GameOverResponse);
        Assertions.assertEquals(response, parsedResponse);
    }

    @Test
    public void testJoinRoomResponseParsing() {
        JoinRoomResponse response = new JoinRoomResponse();
        response.setMessage("TestMessage");
        response.setResult(Result.ERROR);

        String json = Message.toJsonString(response);
        System.out.println(json);

        Message parsedResponse = Message.parse(json);
        System.out.println(parsedResponse);

        Assertions.assertTrue(parsedResponse instanceof JoinRoomResponse);
        Assertions.assertEquals(response, parsedResponse);
    }

    @Test
    public void testMovePieceResponseParsing() {
        MovePieceResponse response = new MovePieceResponse();
        response.setMessage("TestMessage");
        response.setResult(Result.SUCCESS);
        response.setFrom(new ChessBoardPoint(1, 1));
        response.setTo(new ChessBoardPoint(2, 2));

        String json = Message.toJsonString(response);
        System.out.println(json);

        Message parsedResponse = Message.parse(json);

        System.out.println(parsedResponse);

        Assertions.assertTrue(parsedResponse instanceof MovePieceResponse);
        Assertions.assertEquals(response, parsedResponse);
    }

    @Test
    public void testStartGameResponseParsing() {
        StartGameResponse response = new StartGameResponse();
        response.setMessage("TestMessage");
        response.setResult(Result.SUCCESS);
        response.setBlackPlayerName("TestBlackPlayer");
        response.setRedPlayerName("TestRedPlayer");

        String json = Message.toJsonString(response);
        System.out.println(json);

        Message parsedResponse = Message.parse(json);
        System.out.println(parsedResponse);

        Assertions.assertTrue(parsedResponse instanceof StartGameResponse);
        Assertions.assertEquals(response, parsedResponse);
    }
}
