package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MemoryGameDAO implements GameDAO{
    private static final List<GameData> games = new ArrayList<>();

    @Override
    public void clear() {
        games.clear();
    }

    @Override
    public void createGame(GameData gameData) {
        games.add(gameData);
    }

    @Override
    public GameData getGame(int gameID) {
        for(GameData data : games){
            if(gameID == data.gameID()){
                return data;
            }
        }
        return null;
    }

    @Override
    public Collection<GameData> listGames() {
        return games;
    }

    @Override
    public void updateGame(GameData gameData) {
        GameData prev = getGame(gameData.gameID());
        games.remove(prev);
        games.add(gameData);
    }
}
