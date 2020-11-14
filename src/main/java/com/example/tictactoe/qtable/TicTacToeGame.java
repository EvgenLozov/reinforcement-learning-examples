package com.example.tictactoe.qtable;

import java.util.Optional;

public class TicTacToeGame {

    Board board;

    Player playerOne;
    Player playerTwo;

    public TicTacToeGame(Player playerOne, Player playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public void play() {

        board = new Board();
        playerOne.clearStates();
        playerTwo.clearStates();

        while (!isEnd()) {

            System.out.println("Player One turn:");

            turn(playerOne);
            board.print();

            Optional<Integer> winner = board.getWinner();
            if (winner.isPresent()) {
                System.out.println("Player " + winner.get() +" is  winner ");
                giveReward();
                break;
            }

            System.out.println("Player Two turn:");

            turn(playerTwo);
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

        System.out.println("Player " + player.getSymbol() + " takes position : " + position);
    }

    private void giveReward() {
        board.getWinner().ifPresent( winner -> {
            if (winner == 1) {
                playerOne.feedReward(1);
                playerTwo.feedReward(0);
            } else if (winner == -1) {
                playerOne.feedReward(0);
                playerTwo.feedReward(1);
            } else {
                playerOne.feedReward(0.5);
                playerTwo.feedReward(0.5);
            }
        });
    }
}
