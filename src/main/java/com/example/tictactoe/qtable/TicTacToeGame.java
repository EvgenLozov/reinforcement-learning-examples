package com.example.tictactoe.qtable;

import java.util.Optional;

public class TicTacToeGame {

    Board board;

    Player playerX;
    Player playerO;

    public TicTacToeGame(Player playerX, Player playerO) {
        if (playerX.getSymbol() != 1){
            throw new IllegalArgumentException("PlayerX has wrong symbol. Must be 1.");
        }
        if (playerO.getSymbol() != -1){
            throw new IllegalArgumentException("PlayerO has wrong symbol. Must be -1.");
        }
        this.playerX = playerX;
        this.playerO = playerO;
    }

    public void play() {
        System.out.println("TicTacToe game. " + playerX.getName() + " plays with " + playerO.getName());

        board = new Board();
        playerX.clearStates();
        playerO.clearStates();

        while (!isEnd()) {

            System.out.println("Player X turn:");

            turn(playerX);
            board.print();

            Optional<Integer> winner = board.getWinner();
            if (winner.isPresent()) {
                System.out.println("Player " + winner.get() +" is  winner ");
                giveReward();
                break;
            }

            System.out.println("Player O turn:");

            turn(playerO);
            board.print();

            winner = board.getWinner();
            if (winner.isPresent()) {
                System.out.println("Player " + winner.get() +" is  winner ");
                giveReward();
                break;
            }
        }
    }

    private boolean isEnd() {
        return board.availablePositions().isEmpty();
    }

    private void turn(Player player){
        int position = player.choosePosition(board);
        board.occupyPosition(player.getSymbol(), position);
        player.addState(board.getHash());

//        System.out.println("Player " + player.getSymbol() + " takes position : " + position);
    }

    private void giveReward() {
        board.getWinner().ifPresent( winner -> {
            if (winner == 1) {
                playerX.feedReward(1);
                playerO.feedReward(0);
            } else if (winner == -1) {
                playerX.feedReward(0);
                playerO.feedReward(1);
            } else {
                playerX.feedReward(0.5);
                playerO.feedReward(0.5);
            }
        });
    }
}
