package webSocketMessages.userCommands;

public class ResignCmd extends UserGameCommand{
    private final int gameID;

    public ResignCmd(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = CommandType.RESIGN;
    }

    public int getGameID() {
        return gameID;
    }
}
