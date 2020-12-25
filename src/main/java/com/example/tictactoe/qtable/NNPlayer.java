package com.example.tictactoe.qtable;

import org.apache.commons.math3.util.Precision;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NNPlayer implements Player {

    private double explorationRate;
    private double learningRate;
    private double decayGamma;

    private String name;
    private int symbol;
    private Stack<String> states;

    private MultiLayerNetwork network;

    static NNPlayer XneuralNetPlayer(MultiLayerNetwork network){
        return new NNPlayer("X-NN-Player", 1, network, 0.3, 0.1, 0.9);
    }

    static NNPlayer OneuralNetPlayer(MultiLayerNetwork network){
        return new NNPlayer("O-NN-Player", -1, network, 0.3, 0.1, 0.9);
    }

    private NNPlayer(String name, int symbol, MultiLayerNetwork network, double explorationRate, double learningRate, double decayGamma) {
        this.name = name;
        this.symbol = symbol;
        this.network = network;
        this.explorationRate = explorationRate;
        this.learningRate = learningRate;
        this.decayGamma = decayGamma;
        this.states = new Stack<>();
    }

    public int choosePosition(final Board board) {
        List<Integer> availablePositions = board.availablePositions();

        if (availablePositions.isEmpty()) {
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

    private int randomPosition(List<Integer> availablePositions) {
        int nextPosIdx = new Random().nextInt(availablePositions.size());
        return availablePositions.get(nextPosIdx);
    }

    private Integer greedyPosition(Board board, List<Integer> availablePositions) {
        return availablePositions.stream()
                .collect(Collectors.toMap(Function.identity(), pos -> {

                    Board boardCopy = board.copy();
                    boardCopy.occupyPosition(this.getSymbol(), pos);

                    List<Integer> state = new HashToState().apply(boardCopy.getHash());
                    INDArray feature = new StateToFeature().apply(state);

                    return network.output(feature).getDouble(0);
                }))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new IllegalArgumentException("No available positions"));
    }

    @Override
    public int getSymbol() {
        return symbol;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addState(String state) {
        states.push(state);
    }

    @Override
    public void feedReward(double reward) {
        while (!states.isEmpty()){
            String stateHash = states.pop();

            List<Integer> state = new HashToState().apply(stateHash);
            INDArray feature = new StateToFeature().apply(state);

            //TODO how to determine the network sees this state the first time ??
            double currentValue = network.output(feature).getDouble(0);
            double valueUpdated = currentValue + learningRate * (decayGamma * reward - currentValue);
            valueUpdated = Precision.round(valueUpdated, 5);

            INDArray label = Nd4j.zeros(1,1).addi(valueUpdated);
            network.fit(new DataSet(feature, label));

            reward = valueUpdated;
        }
    }

    @Override
    public void clearStates() {
        states.clear();
    }

    public MultiLayerNetwork getNetwork() {
        return network;
    }
}
