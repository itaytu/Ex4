package Algorithms;

import Elements.Game;
import Elements.Pacman;

public class CaclulatePath {

    private Game game;
    private Game gameCopy;

    private Pacman player;

    public CaclulatePath(Game game) {
        this.game = game;
        player = game.getPlayer();
        createCopy();
    }

    private void createCopy() {
        gameCopy = new Game(game);
    }

    public void INIT() {


    }
}
