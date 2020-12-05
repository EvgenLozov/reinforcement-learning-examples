package com.example.tictactoe.qtable;

import java.io.IOException;
import java.util.Map;

public class TicTacToePlayWithBot {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Map<String, Double> qValues = new QTableFileStorage().load("playerTwo.ser");

        Player human = new HumanPlayer(1);
        BotPlayer botOne = new BotPlayer("botOne", -1, qValues);

        TicTacToeGame game = new TicTacToeGame(human, botOne);
        game.play();

    }
}
