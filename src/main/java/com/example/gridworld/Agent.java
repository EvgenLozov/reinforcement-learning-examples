package com.example.gridworld;

import org.apache.commons.math3.util.Precision;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Agent {


    double DECAY_GAMMA = 0.9;
    double LEARNING_RATE = 0.1;
    double EXPLORATION_RATE = 0.3;

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
    }

    Action chooseAction(){
        if (new Random().nextDouble() <= EXPLORATION_RATE){
            //random action
            return Action.values()[new Random().nextInt(Action.values().length)];
        } else {
            //greedy action
            return qValues.get(currentState).actionWithMaxReward();
        }
    }

    void play(int rounds){

        while (rounds > 0){
            if (currentState.isGameOver()){
                double roundReward = currentState.giveReward();
                System.out.println("Round is ended, reward: " + roundReward);

                // explicitly assign end state to reward values
                qValues.put(currentState, new ActionsReward(
                        Arrays.stream(Action.values())
                                .collect(Collectors.toMap(Function.identity(), a -> roundReward))));

                while (!statesActions.isEmpty()){
                    Pair<State, Action> stateAction = statesActions.pop();

                    double currentQValue = qValues.get(stateAction.first).getReward(stateAction.second);
//                    reward = current_q_value + self.lr * (self.decay_gamma * reward - current_q_value)
                    currentQValue = currentQValue + LEARNING_RATE * (DECAY_GAMMA * roundReward - currentQValue );
//                    currentQValue = Precision.round(currentQValue, 5);
                    qValues.get(stateAction.first).put(stateAction.second, currentQValue);
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
        for (Map.Entry<State, ActionsReward> stateActionsRewardEntry : qValues.entrySet()) {
            System.out.println(stateActionsRewardEntry.getKey().toString() + stateActionsRewardEntry.getValue());
        }
    }

}
