package com.example.tictactoe.qtable;

import java.util.Scanner;
import java.util.stream.Collectors;

class HumanPlayer implements Player {

    private final int symbol;

    HumanPlayer(int symbol) {
        this.symbol = symbol;
    }

    @Override
    public int choosePosition(Board board) {
        System.out.println("Available positions: ");
        System.out.println(board.availablePositions().stream()
                .map(String::valueOf).collect(Collectors.joining(",")));

        Scanner in = new Scanner(System.in);

        int position = -1;

        while (!board.availablePositions().contains(position)) {
            System.out.println("Enter valid position: ");
            position = in.nextInt();
        }

        return position;
    }

    @Override
    public int getSymbol() {
        return symbol;
    }

    @Override
    public void addState(String state) {
    }

    @Override
    public void feedReward(double reward) {
    }

    @Override
    public void clearStates() {
    }
}
