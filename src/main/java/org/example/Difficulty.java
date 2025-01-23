package org.example;

import java.util.*;

public enum Difficulty {
    Starter(1, new Settings(9,9,10)),
    Pro(2, new Settings(10,10,16)),
    Guru(3, new Settings(12,12,24));

    private int value;
    private Settings settings;
    private static Map map = new HashMap<>();

    //construtor do enum que impoe um valor chave e associa um tipo de settings
    private Difficulty(int value, Settings settings){
        this.value = value;
        this.settings = settings;
    }

    //mapeamento dos pares chave valor (ex: valor 1 = dificuldade starter)
    static {
        for (Difficulty difficulty : Difficulty.values()){
            map.put(difficulty.value, difficulty);
        }
    }

    //basicamente o getter pelo seu valor inteiro
    public static Difficulty valueOfInt(int difficulty){
        return (Difficulty) map.get(difficulty);
    }

    //checks if a difficulty exists and returns true or false
    public static boolean exists(int value)
    {
        return map.containsKey(value);
    }

    public int getValue(){
        return value;
    }

    public Settings getSettings(){
        return this.settings;
    }

    @Override
    public String toString(){
        return switch (this) {
            case Starter -> "Starter";
            case Pro -> "Pro";
            case Guru -> "Guru";
        };
    }
}


