package com.example.tictactoe.qtable;

import java.util.Map;

public class TicTacToePlayWithBot {

    public static void main(String[] args) {

        Map<String, Double> qValues = new QTableFileStorage().load("playerO.ser");

        Player human = HumanPlayer.XhumanPlayer();
        BotPlayer bot = BotPlayer.ObotPlayer(qValues);

        TicTacToeGame game = new TicTacToeGame(human, bot);
        game.play();

    }
}
