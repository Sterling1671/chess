package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryGameDAO implements GameDAO{
    private static final List<GameData> GAMES = new ArrayList<>();
    AtomicInteger gameIDCounter = new AtomicInteger(1);

    @Override
    public void clear() {
        GAMES.clear();
    }

    @Override
    public int createGame(GameData gameData) {
        int gameId = gameIDCounter.getAndIncrement();
        GameData gameToSave = new GameData(gameData, gameId);
        GAMES.add(gameData);
        return gameId;
    }

    @Override
    public GameData getGame(int gameID) {
        for(GameData data : GAMES){
            if(gameID == data.gameID()){
                return data;
            }
        }
        return null;
    }

    @Override
    public Collection<GameData> listGames() {
        return GAMES;
    }

    @Override
    public void updateGame(GameData gameData) {
        GameData prev = getGame(gameData.gameID());
        GAMES.remove(prev);
        GAMES.add(gameData);
    }
}
