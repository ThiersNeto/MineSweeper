package org.example;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String nickname;
    private Inventory inventory;
    private List<Game> wonGames; //list of games won by this player

    public Player(String nickname)
    {
        //nickname is already checked locally on main, so there's no need to check for inconsistencies here
        this.nickname = nickname;
        inventory = (new Inventory());
        wonGames = new ArrayList<>();
    }

    public String getNickname()
    {
        return this.nickname;
    }

    public void addWonGame(Game game)
    {
        wonGames.add(game); //maybe this can be a boolean for feedback of whether the op was successfull or not
    }

    @Override
    public boolean equals(Object a){
        if(this == a) return true; //it's the same object!
        if(a == null || this.getClass() != a.getClass()) return false; //different types!
        Player other = (Player) a;
        return this.getNickname().equals(other.getNickname());
    }
}
