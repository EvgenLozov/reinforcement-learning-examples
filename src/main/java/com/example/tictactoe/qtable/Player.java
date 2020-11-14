package com.example.tictactoe.qtable;

interface Player {
    int choosePosition(final Board board);
    int getSymbol();
    void addState(String state);
    void feedReward(double reward);
    void clearStates();
}
