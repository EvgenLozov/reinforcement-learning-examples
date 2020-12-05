package com.example.tictactoe.qtable;

import java.io.IOException;

public class TicTacToeTrain {

    public static void main(String[] args) throws IOException {

        double explorationRate = 0.3;
        double learningRate = 0.1;
        double decayGamma = 0.9;

        BotPlayer playerOne = new BotPlayer("p1", 1, explorationRate, learningRate, decayGamma);
        BotPlayer playerTwo = new BotPlayer("p2", -1, explorationRate, learningRate, decayGamma);

        TicTacToeGame game = new TicTacToeGame(playerOne, playerTwo);

        //training
        for (int i = 0; i < 30000; i++) {
            System.out.println("Round : " + i);
            game.play();
        }

        new QTableFileStorage().save(playerOne.getqValues(), "playerOne.ser");
        new QTableFileStorage().save(playerTwo.getqValues(), "playerTwo.ser");

    }
}
