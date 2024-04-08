package webSocketMessages.userCommands;

public class MakeMove extends UserGameCommand{
    private final int gameID;
    private final String move;

    public MakeMove(String authToken, int gameID, String move) {
        super(authToken);
        this.gameID = gameID;
        this.move = move;
        this.commandType = CommandType.MAKE_MOVE;
    }

    public int getGameID() {
        return gameID;
    }

    public String getMove() {
        return move;
    }
}
