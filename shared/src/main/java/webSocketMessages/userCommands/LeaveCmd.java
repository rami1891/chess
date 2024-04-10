package webSocketMessages.userCommands;

public class LeaveCmd extends UserGameCommand{
    private final int gameID;

    public LeaveCmd(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = CommandType.LEAVE;
    }

    public int getGameID() {
        return gameID;
    }
}
