package com.example.gridworld;

import java.util.*;

public class Agent {


    double learningRate = 0.1;
    double explorationRate = 0.3;

    Stack<State> states;
    State currentState;

    final Map<State, Double> stateValues;

    public Agent() {
        this.currentState = State.startState();
        this.states = new Stack<>();
        this.stateValues = new HashMap<>();
        for (int i = 0; i < State.ROWS; i++) {
            for (int j = 0; j < State.COLUMNS; j++) {
                stateValues.put(new State(i, j), 0.0);
            }
        }
    }

    Action chooseAction(){
        if (new Random().nextDouble() <= explorationRate){
            //random action
            return Action.values()[new Random().nextInt(Action.values().length)];
        } else {
            //greedy action
            Action nextAction = Action.UP;
            double maxNextReward = stateValues.get(currentState.nextState(nextAction));

            for (Action action : Action.values()) {
                double reward = stateValues.get(currentState.nextState(action));
                if (reward >= maxNextReward){
                    nextAction = action;
                    maxNextReward = reward;
                }
            }

            return nextAction;
        }
    }

    void play(int rounds){

        while (rounds > 0){
            if (currentState.isGameOver()){
                //back propagation values
                double roundReward = currentState.giveReward();

                stateValues.put(states.pop(), roundReward); // explicitly assign end state to reward values

                while (!states.isEmpty()){
                    State state = states.pop();
                    double stateReward = stateValues.get(state) + learningRate * (roundReward - stateValues.get(state));
                    stateValues.put(state, Math.round(stateReward * 1000.0)/1000.0 );
                }

                currentState = State.startState();
                rounds--;

            } else {
                Action action = chooseAction();

                State nextState = currentState.nextState(action);
                states.push(nextState);

                currentState = nextState;
            }
        }

    }

    void showStateValues(){
        for (int i = 0; i < State.ROWS; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < State.COLUMNS; j++) {
                sb.append(" | ").append( stateValues.get(new State(i, j)));
            }
            System.out.println(sb.toString());
        }
    }

}
