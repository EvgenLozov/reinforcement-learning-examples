package com.example.gridworld;

import java.util.EnumMap;

class State {

    private static final Position WIN_POSITION = new Position(0, 3);
    private static final Position LOSE_POSITION = new Position(1, 3);

    static final int ROWS = 3;
    static final int COLUMNS = 4;

    private final Position currentPosition;

    private EnumMap<Action, RandomCollection<Action>> takingActionChances;

    static State initialState(){
        return new State(2, 0);
    }

    State(int row, int column) {
        this(new Position(row, column));
    }

    private State(Position position){
        this.currentPosition = position;

        takingActionChances = new EnumMap<>(Action.class);

        RandomCollection<Action> up = new RandomCollection<>();
        up.add(0.8, Action.UP);
        up.add(0.1, Action.RIGHT);
        up.add(0.1, Action.LEFT);
        takingActionChances.put(Action.UP, up);

        RandomCollection<Action> down = new RandomCollection<>();
        down.add(0.8, Action.DOWN);
        down.add(0.1, Action.RIGHT);
        down.add(0.1, Action.LEFT);
        takingActionChances.put(Action.DOWN, down);

        RandomCollection<Action> left = new RandomCollection<>();
        left.add(0.8, Action.LEFT);
        left.add(0.1, Action.UP);
        left.add(0.1, Action.DOWN);
        takingActionChances.put(Action.LEFT, left);

        RandomCollection<Action> right = new RandomCollection<>();
        right.add(0.8, Action.RIGHT);
        right.add(0.1, Action.UP);
        right.add(0.1, Action.DOWN);
        takingActionChances.put(Action.RIGHT, right);
    }

    int giveReward(){
        if (currentPosition.equals(WIN_POSITION))
            return 1;

        if(currentPosition.equals(LOSE_POSITION))
            return -1;

        return 0;
    }

    State nextState(Action action){

        //Non-deterministic means that the agent will not be able to go where it intends to go.
        // When it takes an action, it will has a probability to crash in a different action.
        Position position = determinePosition(takingActionChances.get(action).next());
//        Position position = determinePosition(action);

        return isValidPosition(position) ? new State(position) : this;
    }

    boolean isGameOver(){
        return currentPosition.equals(WIN_POSITION) || currentPosition.equals(LOSE_POSITION);
    }

    private Position determinePosition(Action action) {
        switch (action){
            case UP: {
                return new Position(currentPosition.row - 1, currentPosition.column);
            }
            case DOWN:{
                return new Position(currentPosition.row + 1, currentPosition.column);
            }
            case LEFT:{
                return new Position(currentPosition.row , currentPosition.column - 1);
            }
            case RIGHT: {
                return new Position(currentPosition.row , currentPosition.column + 1);
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
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

        @Override
        public String toString() {
            return "(" + row + ", " + column + ")";
        }
    }

    @Override
    public String toString() {
        return currentPosition.toString();
    }
}
