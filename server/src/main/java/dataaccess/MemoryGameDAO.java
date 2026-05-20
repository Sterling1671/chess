package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MemoryGameDAO implements GameDAO{
    private static final List<GameData> allGames = new ArrayList<>();

    @Override
    public void clear() {
        allGames.clear();
    }

    @Override
    public void createGame(GameData gameData) {
        allGames.add(gameData);
    }

    @Override
    public GameData getGame(int gameID) {
        for(GameData data : allGames){
            if(gameID == data.gameId()){
                return data;
            }
        }
        return null;
    }

    @Override
    public Collection<GameData> listGames() {
        return allGames;
    }

    @Override
    public void updateGame(GameData gameData) {
        GameData prev = getGame(gameData.gameId());
        allGames.remove(prev);
        allGames.add(gameData);
    }
}
