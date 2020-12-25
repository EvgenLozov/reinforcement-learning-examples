package com.example.tictactoe.qtable;

public class TicTacToeQTableTrain {

    public static void main(String[] args) {
        int rounds = 50000;

        BotPlayer playerX = BotPlayer.XbotPlayer();
        BotPlayer playerO = BotPlayer.ObotPlayer();

        TicTacToeGame game = new TicTacToeGame(playerX, playerO);

        for (int i = 0; i < rounds; i++) {
            System.out.println("Round : " + i);
            game.play();
        }

        new QTableFileStorage().save(playerX.getqValues(), "playerX.ser");
        new QTableFileStorage().save(playerO.getqValues(), "playerO.ser");

    }
}
