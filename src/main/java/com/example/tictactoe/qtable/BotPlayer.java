package com.example.tictactoe.qtable;

import org.apache.commons.math3.util.Precision;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class BotPlayer implements Player {

    private static final double INIT_Q_VALUE = 0.0;

    private double explorationRate;
    private double learningRate;
    private double decayGamma;

    private String name;
    private int symbol;
    private Stack<String> states;

    private Map<String, Double> qValues;

    public static BotPlayer XbotPlayer(){
        return new BotPlayer("X-Bot-Player", 1, 0.3, 0.1, 0.9);
    }

    public static BotPlayer ObotPlayer(){
        return new BotPlayer("O-Bot-Player", -1, 0.3, 0.1, 0.9);
    }

    public static BotPlayer XbotPlayer(Map<String, Double> qValues){
        return new BotPlayer("X-Bot-Player", 1, qValues, 0.3, 0.1, 0.9);
    }

    public static BotPlayer ObotPlayer(Map<String, Double> qValues){
        return new BotPlayer("O-Bot-Player", -1, qValues, 0.3, 0.1, 0.9);
    }

    private BotPlayer(String name, int symbol, double explorationRate, double learningRate, double decayGamma) {
        this.name = name;
        this.symbol = symbol;
        this.explorationRate = explorationRate;
        this.learningRate = learningRate;
        this.decayGamma = decayGamma;
        this.qValues = new HashMap<>();
        this.states =new Stack<>();
    }

    private BotPlayer(String name, int symbol, Map<String, Double> qValues,
                     double explorationRate, double learningRate, double decayGamma) {
        this(name, symbol, explorationRate, learningRate, decayGamma);
        this.qValues = qValues;
    }

    int chooseRandomPosition(final Board board) {
        List<Integer> availablePositions = board.availablePositions();

        if (availablePositions.isEmpty()){
            throw new IllegalArgumentException("No available positions");
        }

        return randomPosition(availablePositions);
    }

    public int choosePosition(final Board board) {
        List<Integer> availablePositions = board.availablePositions();

        if (availablePositions.isEmpty()){
            throw new IllegalArgumentException("No available positions");
        }

        if (new Random().nextDouble() <= explorationRate) {
            //random
            return randomPosition(availablePositions);
//            return greedyPosition(board, availablePositions);
        } else {
            // greedy
            return greedyPosition(board, availablePositions);
        }
    }

    private Integer greedyPosition(Board board, List<Integer> availablePositions) {
        return availablePositions.stream()
                    .collect(Collectors.toMap(Function.identity(), pos -> {
                        Board boardCopy = board.copy();
                        boardCopy.occupyPosition(this.getSymbol(), pos);
                        return qValues.getOrDefault(boardCopy.getHash(), INIT_Q_VALUE);
                    }))
                    .entrySet()
                    .stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElseThrow(() -> new IllegalArgumentException("No available positions"));
    }

    private int randomPosition(List<Integer> availablePositions) {
        int nextPosIdx = new Random().nextInt(availablePositions.size());
        return availablePositions.get(nextPosIdx);
    }

    public int getSymbol(){
        return symbol;
    }

    public void addState(String state) {
        states.push(state);
    }

    @Override
    public String getName() {
        return name;
    }

    public void feedReward(double reward){
        while (!states.isEmpty()){
            String state = states.pop();
            if (qValues.containsKey(state)){
                double qValue = qValues.get(state);
                double qValueUpdated = qValue + learningRate * (decayGamma * reward - qValue);
                qValueUpdated = Precision.round(qValueUpdated, 5);
                qValues.put(state, qValueUpdated);
                reward = qValueUpdated;
            } else {
                qValues.put(state, INIT_Q_VALUE);
            }
        }
    }

    public void clearStates() {
        states.clear();
    }

    public Map<String, Double> getqValues() {
        return qValues;
    }
}
