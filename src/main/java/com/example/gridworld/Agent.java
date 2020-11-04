package com.example.gridworld;

import org.apache.commons.math3.util.Precision;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class Agent {

    double DECAY_GAMMA = 0.9;
    double LEARNING_RATE = 0.1;
    double EXPLORATION_RATE = 0.3;

    private Stack<Pair<State, Action>> statesActions;
    private State currentState;

    private Map<State, ActionsReward> qValues;

    Agent() {
        this.currentState = State.initialState();
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

    void play(int rounds){

        while (rounds-- > 0){

            currentState = State.initialState();

            //play round
            while (!currentState.isGameOver()){
                Action action = chooseAction();
                statesActions.push(new Pair<>(currentState, action));
                System.out.println(String.format("current position {%s} action {%s}", currentState, action));

                currentState = currentState.nextState(action);
                System.out.println("Next state: " + currentState);
            }

            //back propagation reward
            double reward = currentState.giveReward();
            System.out.println("Round is ended, reward: " + reward);

            // explicitly assign the end state to reward values
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
        }

    }

    private Action chooseAction(){
        if (new Random().nextDouble() <= EXPLORATION_RATE){
            //random action
            return getRandomAction();
        } else {
            //greedy action
            return qValues.get(currentState).actionWithMaxReward();
        }
    }

    private Action getRandomAction() {
        RandomCollection<Action> randomActions = new RandomCollection<>();
        randomActions.add(1, Action.UP);
        randomActions.add(1, Action.DOWN);
        randomActions.add(1, Action.LEFT);
        randomActions.add(1, Action.RIGHT);

        return randomActions.next();
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
