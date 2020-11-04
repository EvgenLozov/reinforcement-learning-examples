package com.example.gridworld;

import org.apache.commons.math3.util.Precision;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Agent {


    double DECAY_GAMMA = 0.9;
    double LEARNING_RATE = 0.1;
    double EXPLORATION_RATE = 0.3;
    RandomCollection<Action> randomActions;

    Stack<Pair<State, Action>> statesActions;
    State currentState;

    final Map<State, ActionsReward> qValues;

    public Agent() {
        this.currentState = State.startState();
        this.statesActions = new Stack<>();

        this.qValues = new HashMap<>();
        for (int i = 0; i < State.ROWS; i++) {
            for (int j = 0; j < State.COLUMNS; j++) {
                ActionsReward initRewards = new ActionsReward(
                        Arrays.stream(Action.values())
                                .collect(Collectors.toMap(Function.identity(), a -> 0.0)));
                qValues.put(new State(i, j), initRewards);
            }
        }

        randomActions = new RandomCollection<>();
        randomActions.add(1, Action.UP);
        randomActions.add(1, Action.DOWN);
        randomActions.add(1, Action.LEFT);
        randomActions.add(1, Action.RIGHT);
    }

    Action chooseAction(){
        if (new Random().nextDouble() <= EXPLORATION_RATE){
            //random action
            return randomActions.next();
        } else {
            //greedy action
            return qValues.get(currentState).actionWithMaxReward();
        }
    }

    void play(int rounds){

        while (rounds > 0){
            if (currentState.isGameOver()){
                double reward = currentState.giveReward();
                System.out.println("Round is ended, reward: " + reward);

                // explicitly assign end state to reward values
                double finalReward = reward;
                qValues.put(currentState, new ActionsReward(
                        Arrays.stream(Action.values())
                                .collect(Collectors.toMap(Function.identity(), a -> finalReward))));

                while (!statesActions.isEmpty()){
                    Pair<State, Action> stateAction = statesActions.pop();

                    double currentQValue = qValues.get(stateAction.first).getReward(stateAction.second);

                    reward = currentQValue + LEARNING_RATE * (DECAY_GAMMA * reward - currentQValue );
                    reward = Precision.round(reward, 5);

                    qValues.get(stateAction.first).put(stateAction.second, reward);
                }

                currentState = State.startState();
                rounds--;

            } else {
                Action action = chooseAction();
                statesActions.push(new Pair<>(currentState, action));
                System.out.println(String.format("current position {%s} action {%s}", currentState, action));

                currentState = currentState.nextState(action);
                System.out.println("Next state: " + currentState);
            }
        }

    }

    void showStateValues(){
        for (int i = 0; i < State.ROWS; i++) {
            for (int j = 0; j < State.COLUMNS; j++) {
                State state = new State(i, j);
                Action action = qValues.get(new State(i, j)).actionWithMaxReward();
                double qValue = qValues.get(state).getReward(action);

                System.out.print(action + " : " + qValue + " | ");
            }
            System.out.println();
        }

    }

}
