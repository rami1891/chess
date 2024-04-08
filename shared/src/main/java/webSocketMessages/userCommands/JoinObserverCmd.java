package webSocketMessages.userCommands;

public class JoinObserverCmd extends UserGameCommand{
    private final int gameID;

    public JoinObserverCmd(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = CommandType.JOIN_OBSERVER;
    }

    public int getGameID() {
        return gameID;
    }
}
