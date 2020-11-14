package com.example.tictactoe.qtable;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

public class TicTacToePlayWithBot {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Map<String, Double> qValues = loadQValues("playerTwo.ser");

        Player human = new HumanPlayer(1);
        BotPlayer botOne = new BotPlayer("botOne", -1, qValues);

        TicTacToeGame game = new TicTacToeGame(human, botOne);
        game.play();

    }

    private static Map<String, Double> loadQValues(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Map<String, Double> map = (HashMap) ois.readObject();
        ois.close();
        fis.close();

        return map;
    }
}
