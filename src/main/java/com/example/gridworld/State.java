package com.example.gridworld;

import java.util.Arrays;

class State {

    private static final Position WIN_POSITION = new Position(0, 3);
    private static final Position LOSE_POSITION = new Position(1, 3);

    static final int ROWS = 3;
    static final int COLUMNS = 4;

    private final Position currentPosition;

    static State startState(){
        return new State(2, 0);
    }

    State(int row, int column) {
        this(new Position(row, column));
    }

    private State(Position position){
        this.currentPosition = position;
    }

    int giveReward(){
        if (currentPosition.equals(WIN_POSITION))
            return 1;

        if(currentPosition.equals(LOSE_POSITION))
            return -1;

        return 0;
    }

    State nextState(Action action){

        Position position;
        switch (action){
            case UP: {
                position = new Position(currentPosition.row - 1, currentPosition.column);
                break;
            }
            case DOWN:{
                position = new Position(currentPosition.row + 1, currentPosition.column);
                break;
            }
            case LEFT:{
                position = new Position(currentPosition.row , currentPosition.column - 1);
                break;
            }
            case RIGHT: {
                position = new Position(currentPosition.row , currentPosition.column + 1);
                break;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }

        return isValidPosition(position) ? new State(position) : this;
    }

    boolean isGameOver(){
        return currentPosition.equals(WIN_POSITION) || currentPosition.equals(LOSE_POSITION);
    }

    private boolean isValidPosition(Position position) {
        return position.row >= 0 && position.row < ROWS
            && position.column >=0 && position.column < COLUMNS
            && !position.equals(new Position(1, 1));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State state = (State) o;

        return currentPosition.equals(state.currentPosition);
    }

    @Override
    public int hashCode() {
        return currentPosition.hashCode();
    }

    private static class Position{
        final int row;
        final int column;

        public Position(int row, int column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Position position = (Position) o;

            if (row != position.row) return false;
            return column == position.column;
        }

        @Override
        public int hashCode() {
            int result = row;
            result = 31 * result + column;
            return result;
        }
    }
}
